package d.dao.easylife.dagger2.modules;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.presenter.impl.QueryIpPresenter;
import d.dao.easylife.dagger2.qualifiers.ApiServiceIpQualifier;
import d.dao.easylife.dagger2.scopes.ActivityScope;
import d.dao.easylife.dagger2.ui.fragment.FragmentQueryIp;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/9/16.
 */
@Module
public class QueryIpFragmentModule {
    private FragmentQueryIp mFragmentQueryIp;

    public QueryIpFragmentModule(FragmentQueryIp fragmentQueryIp){
        this.mFragmentQueryIp = fragmentQueryIp;
    }

    @Provides
    @ActivityScope
    FragmentQueryIp provideFragmentQueryIp(){
        return mFragmentQueryIp;
    }

//    @Provides
//    @ActivityScope
//    QueryIpPresenter provideQueryIpPresenter(FragmentQueryIp fragmentQueryIp,
//                                          @ApiServiceIpQualifier ApiService apiService,
//                                          CompositeSubscription compositeSubscription,
//                                          ReservoirUtils reservoirUtils){
//    return new QueryIpPresenter(fragmentQueryIp,apiService,compositeSubscription,reservoirUtils);
//    }


}
