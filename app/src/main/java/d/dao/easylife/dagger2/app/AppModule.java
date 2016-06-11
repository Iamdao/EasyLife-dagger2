package d.dao.easylife.dagger2.app;

import android.content.Context;

import javax.inject.Singleton;

import d.dao.easylife.dagger2.manager.CacheManager;
import d.dao.easylife.dagger2.utils.ACache;
import d.dao.easylife.dagger2.utils.ToastUtil;
import dagger.Module;
import dagger.Provides;

/**
 * Created by dao on 6/9/16.
 */
@Module
public class AppModule {
//    private EasyLifeApplication mApplication;
//
//    public AppModule(EasyLifeApplication easyLifeApplication) {
//        this.mApplication = easyLifeApplication;
//    }
//
//    @Provides
//    @Singleton
//    Application provideApplication() {
//        return this.mApplication;
//    }


    Context context;

    public AppModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    ToastUtil provideToastUtil(){
        return new ToastUtil(context);
    }

    @Provides
    @Singleton
    ACache provideACache(){
        return ACache.get(context);
    }

    @Provides
    @Singleton
    CacheManager provideCacheManager(){
        return new CacheManager(context);
    }


}
