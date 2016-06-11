package d.dao.easylife.dagger2.presenter.impl;

import android.util.Log;

import java.util.List;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.model.bean.weather.BaseWeatherData;
import d.dao.easylife.dagger2.model.bean.weather.Weather;
import d.dao.easylife.dagger2.ui.WeatherActivity;
import d.dao.easylife.dagger2.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/9/16.
 */
public class WeatherPresenter {

    private static int pageSize = 10;

    private WeatherActivity mWeatherActivity;
    private ApiService mApiService;
    private CompositeSubscription mCompositeSubscription;


    public WeatherPresenter(WeatherActivity weatherActivity, ApiService apiService,
                            CompositeSubscription compositeSubscription) {
        this.mWeatherActivity = weatherActivity;
        this.mApiService = apiService;
        this.mCompositeSubscription = compositeSubscription;
    }

    public void loadWeather(String city, int more) {
        if (more < 1) {
            more = 1;
        } else if (more > 2) {
            more = 2;
        }
        Observable<Weather> data = mApiService.loadWeather(city, more);
        Observable<List<BaseWeatherData>> list = data.map(new Func1<Weather, List<BaseWeatherData>>() {
            @Override
            public List<BaseWeatherData> call(Weather weather) {
                return weather.detail;
            }
        }).compose(RxUtils.<List<BaseWeatherData>>applyIOToMainThreadSchedulers());

        mCompositeSubscription.add(list.subscribe(new Subscriber<List<BaseWeatherData>>() {
            @Override
            public void onCompleted() {
                if (mCompositeSubscription != null) {
                    mCompositeSubscription.remove(this);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("failure", e.toString());
                mWeatherActivity.onLoadWeatherFailure(e);
            }

            @Override
            public void onNext(List<BaseWeatherData> baseWeatherDatas) {
                Log.e("get success", baseWeatherDatas.toString());
                mWeatherActivity.OnLoadWeatherSuccess(baseWeatherDatas);
            }
        }));
    }
}
