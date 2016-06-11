package d.dao.easylife.dagger2.presenter.impl;

import android.util.Log;

import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.model.bean.joke.BaseJokeFirstData;
import d.dao.easylife.dagger2.model.bean.joke.JokeFirst;
import d.dao.easylife.dagger2.ui.view.IJokeFirstView;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import d.dao.easylife.dagger2.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/9/16.
 */
public class JokeFirstPresenter {

    private static int pageSize = 10;

    private IJokeFirstView mView;
    private ApiService mApiService;
    private CompositeSubscription mCompositeSubscription;
    private ReservoirUtils mReservoirUtils;

    public JokeFirstPresenter(IJokeFirstView jokeFirstView, ApiService apiService,
                              CompositeSubscription compositeSubscription,ReservoirUtils reservoirUtils) {
        this.mView = jokeFirstView;
        this.mApiService = apiService;
        this.mCompositeSubscription = compositeSubscription;
        this.mReservoirUtils = reservoirUtils;
    }
    public void loadFirstJoke(int maxId, int minId, int size) {
        Observable<JokeFirst> data = mApiService.loadFirstJoke(maxId,minId,size);
        Observable<List<BaseJokeFirstData>> list = data.map(new Func1<JokeFirst, List<BaseJokeFirstData>>() {
            @Override
            public List<BaseJokeFirstData> call(JokeFirst jokeFirst) {
                return jokeFirst.detail;
            }
        }).compose(RxUtils.<List<BaseJokeFirstData>>applyIOToMainThreadSchedulers());
        mCompositeSubscription.add(list.subscribe(new Subscriber<List<BaseJokeFirstData>>() {
            @Override
            public void onCompleted() {
                if(mCompositeSubscription!=null){
                    mCompositeSubscription.remove(this);
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.onGetJokeFailure(e);
            }

            @Override
            public void onNext(List<BaseJokeFirstData> baseJokeFirstDatas) {
                Log.e("result", baseJokeFirstDatas.toString());
                mView.onGetJokeSuccess(baseJokeFirstDatas);
            }
        }));
    }

    public void loadFirstJokeMore(final int maxId, int minId, int size){
        Observable<JokeFirst> data = mApiService.loadFirstJoke(maxId,minId,size);
        Observable<List<BaseJokeFirstData>> list = data.map(new Func1<JokeFirst, List<BaseJokeFirstData>>() {
            @Override
            public List<BaseJokeFirstData> call(JokeFirst jokeFirst) {
                return jokeFirst.detail;
            }
        }).compose(RxUtils.<List<BaseJokeFirstData>>applyIOToMainThreadSchedulers());
        mCompositeSubscription.add(list.subscribe(new Subscriber<List<BaseJokeFirstData>>() {
            @Override
            public void onCompleted() {
                if(mCompositeSubscription!=null){
                    mCompositeSubscription.remove(this);
                }
            }

            @Override
            public void onError(final Throwable e) {
                if (maxId == 0) {//如果是第一次进入刷新
                    //获取缓存信息
                    Type resultType = new TypeToken<List<BaseJokeFirstData>>() {
                    }.getType();
                    mReservoirUtils.get("jokefirst", resultType, new ReservoirGetCallback<List<BaseJokeFirstData>>() {
                        @Override
                        public void onSuccess(List<BaseJokeFirstData> object) {
                            //获取到本地数据
                            if (object.size() > 0) {
                                //如果得到缓存的信息
                                mView.onLoadJokeSuccess(object);
                            } else {
                                mView.onGetJokeFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            //获取本地缓存信息也失败,显示提示加载信息
                            mView.onGetJokeFailure(e);
                        }
                    });

                }else{
                    mView.onGetJokeFailure(e);
                }
            }

            @Override
            public void onNext(List<BaseJokeFirstData> baseJokeFirstDatas) {
                Log.e("result", baseJokeFirstDatas.toString());
                mView.onGetJokeSuccess(baseJokeFirstDatas);
            }
        }));
    }


}
