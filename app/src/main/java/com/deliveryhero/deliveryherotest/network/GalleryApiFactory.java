package com.deliveryhero.deliveryherotest.network;

import com.deliveryhero.deliveryherotest.network.deserializers.AlbumDeserializer;
import com.deliveryhero.deliveryherotest.network.deserializers.PhotoDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by MACB105 on 11/06/16.
 */
public class GalleryApiFactory {

    // Constants
    public static final int API_ENDPOINT_ALBUMS = 1;
    public static final int API_ENDPOINT_PHOTOS = 2;
    private static final String API_BASEURL = "http://jsonplaceholder.typicode.com";

    /**
     * Creates an service instance.
     */
    public static GalleryApiService newApi(int endPoint) {
        return newApi(new OkHttpClient(), endPoint);
    }

    /**
     * Creates an service instance with the given client.
     */
    public static GalleryApiService newApi(OkHttpClient client, int apiEndPoint) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        switch (apiEndPoint) {
            case API_ENDPOINT_ALBUMS:
                gsonBuilder.registerTypeAdapter(AlbumDeserializer.TYPE, new AlbumDeserializer());
                break;
            case API_ENDPOINT_PHOTOS:
                gsonBuilder.registerTypeAdapter(AlbumDeserializer.TYPE, new PhotoDeserializer());
                break;
            default:

        }

        Gson gson = gsonBuilder.create();

        // Builds API service
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(API_BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(GalleryApiService.class);
    }
}
