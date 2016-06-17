package com.deliveryhero.deliveryherotest.network;

import com.deliveryhero.deliveryherotest.beans.models.Album;
import com.deliveryhero.deliveryherotest.beans.models.Photo;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by MACB105 on 11/06/16.
 */
public interface GalleryApiService {

// GET

    /**
     * Returns the obeservable list of albums.
     */
    @GET("/albums")
    Observable<List<Album>> getAlbums();


    /**
     * Returns the obeservable list of photos.
     */
    @GET("/photos")
    Observable<List<Photo>> getPhotosByAlbumiD(@Query("albumId") String albumId);
}
