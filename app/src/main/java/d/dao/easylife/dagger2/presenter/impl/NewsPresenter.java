package d.dao.easylife.dagger2.presenter.impl;

import android.util.Log;

import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.model.bean.news.BaseNewsData;
import d.dao.easylife.dagger2.model.bean.news.News;
import d.dao.easylife.dagger2.presenter.INewsPresenter;
import d.dao.easylife.dagger2.ui.MainActivity;
import d.dao.easylife.dagger2.ui.view.IMainView;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import d.dao.easylife.dagger2.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/9/16.
 */
public class NewsPresenter implements INewsPresenter{

    private static int pageSize = 10;

    private MainActivity mMainActivity;
    private ApiService mApiService;
    private CompositeSubscription mCompositeSubscription;
    private ReservoirUtils mReservoirUtils;

    public NewsPresenter(MainActivity mainActivity, ApiService apiService,
                         CompositeSubscription compositeSubscription,
                         ReservoirUtils reservoirUtils) {
        this.mMainActivity = mainActivity;
        this.mApiService = apiService;
        this.mCompositeSubscription = compositeSubscription;
        this.mReservoirUtils = reservoirUtils;
    }


    @Override
    public void attachView(IMainView view) {

    }

    public void loadNews(final boolean firstRefresh) {
        long tempTime = System.currentTimeMillis();

        Observable<News> news = mApiService.loadNews(tempTime, pageSize);
        Observable<List<BaseNewsData>> list = news.map(new Func1<News, List<BaseNewsData>>() {
            @Override
            public List<BaseNewsData> call(News news) {
                return news.detail;
            }
        }).compose(RxUtils.<List<BaseNewsData>>applyIOToMainThreadSchedulers());

        mCompositeSubscription.add(list.subscribe(new Subscriber<List<BaseNewsData>>() {
            @Override
            public void onCompleted() {
                if (mCompositeSubscription != null) {
                    mCompositeSubscription.remove(this);
                }
            }
            @Override
            public void onError(final Throwable e) {
                //如果是第一次进入时刷新,没有请求到网络数据
                if (firstRefresh) {
                    //获取本地储存数据
                    Type resultType = new TypeToken<List<BaseNewsData>>() {
                    }.getType();
                    mReservoirUtils.get("news", resultType, new ReservoirGetCallback<List<BaseNewsData>>() {
                        @Override
                        public void onSuccess(List<BaseNewsData> object) {
                            //获取到本地数据
                            if (object.size() > 0) {
                                mMainActivity.onGetNewsSuccess(object);
                            } else {
                                mMainActivity.onGetNewsFailure(e);
                            }
                        }
                        @Override
                        public void onFailure(Exception e) {
                            //获取本地缓存信息也失败,显示提示加载信息
                            mMainActivity.onGetNewsFailure(e);
                        }
                    });
                } else {
                    //显示提示加载信息
                    mMainActivity.onGetNewsFailure(e);
                }
            }
            @Override
            public void onNext(List<BaseNewsData> baseNewsDatas) {

                //获取数据成功
                mMainActivity.onGetNewsSuccess(baseNewsDatas);
                // 刷新缓存
                mReservoirUtils.refresh("news", baseNewsDatas);
            }
        }));
    }

    public void loadMore(long time){
        Observable<News> news =   mApiService.loadNews(time, pageSize);
        Observable<List<BaseNewsData>> list = news.map(new Func1<News, List<BaseNewsData>>() {
            @Override
            public List<BaseNewsData> call(News news) {
                return news.detail;
            }
        }).compose(RxUtils.<List<BaseNewsData>>applyIOToMainThreadSchedulers());
        mCompositeSubscription.add(list.subscribe(new Subscriber<List<BaseNewsData>>() {
            @Override
            public void onCompleted() {
                //移除
                if (mCompositeSubscription != null) {
                    mCompositeSubscription.remove(this);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("", e.toString());
                mMainActivity.onLoadNewsFailure(e);
            }

            @Override
            public void onNext(List<BaseNewsData> baseNewsDatas) {
                Log.e("news", baseNewsDatas.toString());
                //获取数据成功
                mMainActivity.onLoadNewsSuccess(baseNewsDatas);

            }
        }));
    }

    @Override
    public void detachView(IMainView view) {

    }
}
