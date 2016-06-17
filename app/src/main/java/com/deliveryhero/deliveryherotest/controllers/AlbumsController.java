package com.deliveryhero.deliveryherotest.controllers;

import com.deliveryhero.deliveryherotest.beans.models.Album;
import com.deliveryhero.deliveryherotest.network.GalleryApiFactory;
import com.deliveryhero.deliveryherotest.network.GalleryApiService;
import com.deliveryhero.deliveryherotest.utils.ApiFailEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by MACB105 on 11/06/16.
 */
public class AlbumsController {


    private GalleryApiService mAlbumsApiService;


    public AlbumsController() {
        this(GalleryApiFactory.newApi(GalleryApiFactory.API_ENDPOINT_ALBUMS));
    }


    public AlbumsController(GalleryApiService apiService) {
        super();
        mAlbumsApiService = apiService;
    }

    /**
     * Fetches the albums.
     */
    public Observable<List<Album>> getAlbums(String justAstring) {

        return mAlbumsApiService
                .getAlbums()
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new ApiFailEvent("Something went wrong with network, please try again"));
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(albums -> albums == null ? new ArrayList<>() : albums);

    }
}
