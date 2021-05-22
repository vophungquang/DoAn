package com.example.vophungquang.network.api;
/**
 * Created by vophungquang
 */
public class ApiUtils {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    public static MapService getMapService(){
        return RetrofitClient.getClient(BASE_URL).create(MapService.class);
    }
}
