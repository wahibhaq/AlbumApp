package com.deliveryhero.deliveryherotest.controllers;

import com.deliveryhero.deliveryherotest.beans.models.Photo;
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
public class PhotosController {

    private GalleryApiService mPhotosApiService;


    public PhotosController() {
        this(GalleryApiFactory.newApi(GalleryApiFactory.API_ENDPOINT_PHOTOS));
    }


    public PhotosController(GalleryApiService apiService) {
        super();
        mPhotosApiService = apiService;
    }

    /**
     * Fetches the photos by the relevant albumid.
     */
    public Observable<List<Photo>> getPhotosByAlbumiD(String albumId) {

        return mPhotosApiService
                .getPhotosByAlbumiD(albumId)
                .subscribeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new ApiFailEvent("Something went wrong with network, please try again"));
                    }
                })
                .map(photos -> photos == null ? new ArrayList<>() : photos);

    }
}
