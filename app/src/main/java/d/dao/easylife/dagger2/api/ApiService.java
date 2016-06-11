package d.dao.easylife.dagger2.api;

import d.dao.easylife.dagger2.model.bean.ip.Ip;
import d.dao.easylife.dagger2.model.bean.joke.JokeFirst;
import d.dao.easylife.dagger2.model.bean.news.News;
import d.dao.easylife.dagger2.model.bean.robot.RobotResponseMsg;
import d.dao.easylife.dagger2.model.bean.weather.Weather;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by dao on 5/29/16.
 * 管理API
 */
public interface ApiService {

    //新闻接口:
    //http://api.1-blog.com/biz/bizserver/news/list.do?max_behot_time=1464583244994&size=10
    @GET("/biz/bizserver/news/list.do")
    Observable<News> loadNews(@Query("max_behot_time") long max_behot_time, @Query("size") int size);

    //笑话1
    @GET("/biz/bizserver/xiaohua/list.do")
    Observable<JokeFirst> loadFirstJoke(@Query("maxXhid") int maxXhid,
                                        @Query("minXhid") int minXhid,
                                        @Query("size") int size);
    //天气
    @GET("/biz/bizserver/weather/list.do")
    Observable<Weather> loadWeather(@Query("city") String city, @Query("more") int more);


//    http://op.juhe.cn/robot/index?info=你好&key=您申请到的APPKEY
    //机器人
    @GET("/robot/index")
    Observable<RobotResponseMsg> loadRobot(@Query("info") String info, @Query("key") String key);

    //http://apis.juhe.cn/ip/ip2addr?ip=www.juhe.cn&key=appkey
    //ip查询
    @GET("/ip/ip2addr")
    Observable<Ip> queryIp(@Query("ip") String ip,@Query("key") String key);

}
