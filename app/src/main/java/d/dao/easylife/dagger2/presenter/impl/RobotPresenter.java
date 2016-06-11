package d.dao.easylife.dagger2.presenter.impl;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.model.bean.robot.BaseRobotResponseData;
import d.dao.easylife.dagger2.model.bean.robot.RobotResponseMsg;
import d.dao.easylife.dagger2.ui.RobotActivity;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import d.dao.easylife.dagger2.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/10/16.
 */
public class RobotPresenter {


    private RobotActivity mRobotActivity;
    private ApiService mApiService;
    private CompositeSubscription mCompositeSubscription;
    private ReservoirUtils mReservoirUtils;

    public RobotPresenter(RobotActivity robotActivity, ApiService apiService,
                          CompositeSubscription compositeSubscription,
                          ReservoirUtils reservoirUtils) {
        this.mRobotActivity = robotActivity;
        this.mApiService = apiService;
        this.mCompositeSubscription = compositeSubscription;
        this.mReservoirUtils = reservoirUtils;
    }

    public void loadRobot(String info, String key) {
        Observable<RobotResponseMsg> msg = mApiService.loadRobot(info, key);
        Observable<BaseRobotResponseData> data = msg.map(new Func1<RobotResponseMsg, BaseRobotResponseData>() {
            @Override
            public BaseRobotResponseData call(RobotResponseMsg robotResponseMsg) {
                return robotResponseMsg.result;
            }
        }).compose(RxUtils.<BaseRobotResponseData>applyIOToMainThreadSchedulers());
        mCompositeSubscription.add(data.subscribe(new Subscriber<BaseRobotResponseData>() {
            @Override
            public void onCompleted() {
                if (mCompositeSubscription != null) {
                    mCompositeSubscription.remove(this);
                }
            }

            @Override
            public void onError(Throwable e) {
                mRobotActivity.onGetRobotResponseFailure(e);
            }

            @Override
            public void onNext(BaseRobotResponseData baseRobotResponseData) {
                mRobotActivity.onGetRobotResponseSuccess(baseRobotResponseData);
            }
        }));
    }
}
