package com.hyc;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hyc on 2017/4/13 12:17
 */

public interface ApiStores {
    static String welfareBaseUrl="http://gank.io/api/data/福利/";

    static String booksBaseUrl="https://api.douban.com";

    @GET("/v2/book/search")
    Observable<BookApi> loadBooksData(@Query("q") String q,@Query("tag") String tag, @Query("start") int start, @Query("count") int count);
    @GET("{count}/{page}")
    Observable<WelfareResult> loadWelfareData(@Path("count") int count,@Path("page") int page);
    @GET("/v2/movie/search")
    Observable<HotMovieBean> loadMovieTop250(@Query("q") String q,@Query("tag") String tag,@Query("start") int start, @Query("count") int count);
    @GET("/v2/movie/subject/{id}")
    Observable<MovieMoreData> loadMovieDetails(@Path("id") String id);

}
