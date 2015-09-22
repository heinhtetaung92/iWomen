package org.undp_iwomen.iwomen.model.retrofit_api;

import org.undp_iwomen.iwomen.CommonConfig;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface TlgProfileService {

    @GET(CommonConfig.TLGPROFILE_URL)
    public void getTlgProfile( Callback<String> callback);



}
