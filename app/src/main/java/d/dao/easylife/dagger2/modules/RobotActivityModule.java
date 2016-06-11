package d.dao.easylife.dagger2.modules;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.presenter.impl.RobotPresenter;
import d.dao.easylife.dagger2.qualifiers.ApiServiceRobotQualifier;
import d.dao.easylife.dagger2.scopes.ActivityScope;
import d.dao.easylife.dagger2.ui.RobotActivity;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/9/16.
 */
@Module
public class RobotActivityModule {
    private RobotActivity mRobotActivity;

    public RobotActivityModule(RobotActivity robotActivity){
        this.mRobotActivity = robotActivity;
    }

    @Provides
    @ActivityScope
    RobotActivity provideRobotActivity(){
        return mRobotActivity;
    }
//
//    @Provides
//    @ActivityScope
//    RobotPresenter provideRobotPresenter(RobotActivity robotActivity,
//                                        @ApiServiceRobotQualifier ApiService apiService,
//                                        CompositeSubscription compositeSubscription,
//                                        ReservoirUtils reservoirUtils){
//    return new RobotPresenter(robotActivity,apiService,compositeSubscription,reservoirUtils);
//    }


}
