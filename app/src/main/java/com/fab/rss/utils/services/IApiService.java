package com.fab.rss.utils.services;

import com.fab.rss.utils.bundles.FeedBundle;
import com.fab.rss.utils.bundles.SignBundle;
import com.fab.rss.utils.models.AuthUser;
import com.fab.rss.utils.models.FeedRSS;
import com.fab.rss.utils.models.RSSResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/28/17
 */

@SuppressWarnings("UnusedDeclaration")
public interface IApiService {

    String BASE_URL_API = "https://rssfeedaggregator.herokuapp.com/";

    String SIGN_UP = "register";
    String SIGN_IN = "login";
    String USER = "api/user";
    String RSS = "api/rss";
    String RSS_ID = "api/rss/{id}";

    /**
     * LOGIN && REGISTRATION CALLS
     */

    @POST(SIGN_UP)
    Call<AuthUser> signUp(@Body SignBundle bundle);

    @POST(SIGN_IN)
    Call<AuthUser> signIn(@Body SignBundle bundle);

    /**
     * USER'S CALLS
     */

    @FormUrlEncoded
    @GET(USER)
    Call<AuthUser> getUser(@Header("token") String token);

    @FormUrlEncoded
    @PUT(USER)
    Call<AuthUser> updateUser(@Header("token") String token, @Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @DELETE(USER)
    Call<String> deleteUser(@Header("token") String token);

    /**
     * RSS CALLS
     */

    @POST(RSS)
    Call<RSSResponse> addRSS(@Header("token") String token, @Body FeedBundle bundle);

    @FormUrlEncoded
    @GET(RSS)
    Call<List<RSSResponse>> getListRSS(@Header("token") String token);

    @FormUrlEncoded
    @GET(RSS_ID)
    Call<FeedRSS> getRSS(@Header("token") String token, @Path("id") String id);

    @FormUrlEncoded
    @DELETE(RSS_ID)
    Call<String> deleteRSS(@Header("token") String token, @Path("id") String id);

    @FormUrlEncoded
    @PUT(RSS_ID)
    Call<String> updateRSS(@Header("token") String token, @Path("id") String id, @Field("title") String title,
                           @Field("url") String url, @Field("comment") String comment);

}