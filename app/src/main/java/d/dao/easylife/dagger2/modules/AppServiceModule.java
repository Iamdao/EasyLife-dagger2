package d.dao.easylife.dagger2.modules;


import javax.inject.Singleton;

import d.dao.easylife.dagger2.manager.NavigationManager;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/7/16.
 */
@Module
public class AppServiceModule {


    @Provides
    @Singleton
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @Singleton
    ReservoirUtils provideReservoirUtils() {
        return new ReservoirUtils();
    }

    @Provides
    @Singleton
    NavigationManager provideNavigationManager(){
        return new NavigationManager();
    }
}
