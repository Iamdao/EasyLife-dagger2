package d.dao.easylife.dagger2.api;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import d.dao.easylife.dagger2.constants.BaseUrl;
import d.dao.easylife.dagger2.qualifiers.ApiServiceIpQualifier;
import d.dao.easylife.dagger2.qualifiers.ApiServiceRobotQualifier;
import d.dao.easylife.dagger2.qualifiers.RetrofitIpQualifier;
import d.dao.easylife.dagger2.qualifiers.RetrofitRobotQualifier;
import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by dao on 6/9/16.
 */
@Module
public class ApiServiceModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit provideNewsRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.NEWS)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    @RetrofitRobotQualifier
    Retrofit provideRobotRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.ROBOT)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }


    @Provides
    @Singleton
    @RetrofitIpQualifier
    Retrofit provideIpRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.IP)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    ApiService provideNewsApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    @ApiServiceRobotQualifier
    ApiService provideRobotApiService(@RetrofitRobotQualifier Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }
    @Provides
    @Singleton
    @ApiServiceIpQualifier
    ApiService provideIpApiService(@RetrofitIpQualifier Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }
}
