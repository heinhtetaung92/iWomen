package com.parse.retrofit_api;

import com.parse.CommonConfig;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface TlgProfileService {

    @GET(CommonConfig.TLGPROFILE_URL)
    public void getTlgProfileList(Callback<String> callback);

    @GET(CommonConfig.TLGPROFILE_DETAILBYID_URL)
    public void getTlgProfileDetailById(@Path("id") String id, Callback<String> callback);



}
