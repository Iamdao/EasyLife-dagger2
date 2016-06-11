package d.dao.easylife.dagger2.manager;

import android.content.Context;

import javax.inject.Inject;

import d.dao.easylife.dagger2.app.EasyLifeApplication;
import d.dao.easylife.dagger2.utils.ACache;

/**
 * Created by dao on 5/23/16.
 * 管理缓存
 */
public class CacheManager {


    private ACache mACache;

    public CacheManager(ACache cache) {
        this.mACache = cache;
    }

    public CacheManager(Context context) {
        this.mACache = ACache.get(context);
    }


    /**
     * 保存WeatherActivity默认的城市
     *
     * @param city
     */
    public void saveDefaultCity(String city) {
        mACache.put("city", city);
    }

    /**
     * 获取WeatherActivity默认的城市
     */
    public String getDefaultCity() {
        return mACache.getAsString("city");
    }


}
