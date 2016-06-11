package d.dao.easylife.dagger2.presenter.impl;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.model.bean.ip.BaseIpData;
import d.dao.easylife.dagger2.model.bean.ip.Ip;
import d.dao.easylife.dagger2.ui.fragment.FragmentQueryIp;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import d.dao.easylife.dagger2.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/9/16.
 */
public class QueryIpPresenter {

    private static int pageSize = 10;

    private FragmentQueryIp mFragmentQueryIp;
    private ApiService mApiService;
    private CompositeSubscription mCompositeSubscription;
    private ReservoirUtils mReservoirUtils;

    public QueryIpPresenter(FragmentQueryIp fragmentQueryIp, ApiService apiService,
                            CompositeSubscription compositeSubscription,
                            ReservoirUtils reservoirUtils) {
        this.mFragmentQueryIp = fragmentQueryIp;
        this.mApiService = apiService;
        this.mCompositeSubscription = compositeSubscription;
        this.mReservoirUtils = reservoirUtils;
    }

    public void queryIp(String ip, String key) {
        Observable<Ip> msg = mApiService.queryIp(ip, key);
        Observable<BaseIpData> data = msg.map(new Func1<Ip, BaseIpData>() {
            @Override
            public BaseIpData call(Ip ip) {
                return ip.result;
            }
        })
                .compose(RxUtils.<BaseIpData>applyIOToMainThreadSchedulers());
        mCompositeSubscription.add(data.subscribe(new Subscriber<BaseIpData>() {
            @Override
            public void onCompleted() {
                if (mCompositeSubscription != null) {
                    mCompositeSubscription.remove(this);
                }
            }
            @Override
            public void onError(Throwable e) {
                mFragmentQueryIp.onQueryIpFailure(e);
            }

            @Override
            public void onNext(BaseIpData baseIpData) {
                mFragmentQueryIp.onQueryIpSuccess(baseIpData);
            }
        }));


    }

}
