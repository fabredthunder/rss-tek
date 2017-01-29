package com.fab.rss.utils.services;

import com.fab.rss.utils.bundles.FeedBundle;
import com.fab.rss.utils.bundles.SignBundle;
import com.fab.rss.utils.models.AuthUser;
import com.fab.rss.utils.models.BaseResponse;
import com.fab.rss.utils.models.FeedRSS;
import com.fab.rss.utils.models.RSSResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET(USER)
    Call<AuthUser> getUser(@Header("token") String token);

    @PUT(USER)
    Call<AuthUser> updateUser(@Header("token") String token, @Body SignBundle bundle);

    @DELETE(USER)
    Call<BaseResponse> deleteUser(@Header("token") String token);

    /**
     * RSS CALLS
     */

    @POST(RSS)
    Call<RSSResponse> addRSS(@Header("token") String token, @Body FeedBundle bundle);

    @GET(RSS)
    Call<List<RSSResponse>> getListRSS(@Header("token") String token);

    @GET(RSS_ID)
    Call<FeedRSS> getRSS(@Header("token") String token, @Path("id") String id);

    @DELETE(RSS_ID)
    Call<BaseResponse> deleteRSS(@Header("token") String token, @Path("id") String id);

    @PUT(RSS_ID)
    Call<BaseResponse> updateRSS(@Header("token") String token, @Path("id") String id, @Body FeedBundle bundle);

}