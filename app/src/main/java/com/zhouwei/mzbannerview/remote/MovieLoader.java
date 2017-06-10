package com.zhouwei.mzbannerview.remote;


import com.zhouwei.mzbannerview.http.ObjectLoader;
import com.zhouwei.mzbannerview.http.RetrofitServiceManager;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 *
 *
 * Created by zhouwei on 16/11/10.
 */

public class MovieLoader extends ObjectLoader {
    private MovieService mMovieService;

    public MovieLoader(){
        mMovieService = RetrofitServiceManager.getInstance().create(MovieService.class);
    }

    /**
     * 获取电影列表
      * @param start
     * @param count
     * @return
     */
    public Observable<List<Movie>> getMovie(int start, int count){
        return observe(mMovieService.getTop250(start,count))
                .map(new Func1<MovieSubject, List<Movie>>() {
            @Override
            public List<Movie> call(MovieSubject movieSubject) {
                return movieSubject.subjects;
            }
        });
    }

    public Observable<String> getWeatherList(String cityId, String key){
        return observe(mMovieService.getWeather(cityId,key)).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return null;
            }
        });
    }


    public interface MovieService{

        //获取豆瓣Top250 榜单
        @GET("top250")
        Observable<MovieSubject> getTop250(@Query("start") int start, @Query("count") int count);

        @FormUrlEncoded
        @POST("/x3/weather")
        Observable<String> getWeather(@Field("cityId") String cityId, @Field("key") String key);

    }
}
