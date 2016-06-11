package d.dao.easylife.dagger2.modules;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.presenter.impl.NewsPresenter;
import d.dao.easylife.dagger2.scopes.ActivityScope;
import d.dao.easylife.dagger2.ui.MainActivity;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/9/16.
 */
@Module
public class MainActivityModule {
    private MainActivity mMainActivity;

    public MainActivityModule(MainActivity mainActivity){
        this.mMainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActivity(){
        return mMainActivity;
    }

//    @Provides
//    @ActivityScope
//    NewsPresenter provideNewsPresenter(MainActivity mainActivity,
//                                       ApiService apiService,
//                                       CompositeSubscription compositeSubscription,
//                                       ReservoirUtils reservoirUtils){
//    return new NewsPresenter(mainActivity,apiService,compositeSubscription,reservoirUtils);
//    }


}
