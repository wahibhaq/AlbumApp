package com.deliveryhero.deliveryherotest.viewmodels;

import android.support.v4.app.Fragment;

import com.deliveryhero.deliveryherotest.beans.models.Photo;
import com.deliveryhero.deliveryherotest.controllers.PhotosController;
import com.deliveryhero.deliveryherotest.ui.fragments.extras.FragmentBuilder;
import com.deliveryhero.deliveryherotest.utils.BundleFactory;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by Hamza_MACB105 on 15/06/16.
 */
public final class PhotosViewModel {

    // Observable

    public final Observable<String> title() {
        return mTitleSubject.asObservable();
    }

    public final Observable<String> thumbnail() {
        return mThumbnailUrlSubject.asObservable();
    }

    public final Observable<List<Photo>> photos() {
        return mPhotos.asObservable();
    }

    public final Observable<Fragment> openPhotoDetailView() {
        return mOpenPhotoDetailViewSubject.asObservable();
    }


    // Commands

    public final PublishRelay<String> loadPhotos = PublishRelay.create();
    /**
     * Send item to this command to trigger changes to the title subject.
     */
    public final PublishRelay<Photo> setPhotoItemCommand = PublishRelay.create();
    public final PublishRelay<Void> openPhotoDetailCommand = PublishRelay.create();


    // Members

    private final BehaviorSubject<List<Photo>> mPhotos = BehaviorSubject.create();
    private final BehaviorSubject<String> mTitleSubject = BehaviorSubject.create();
    private final BehaviorSubject<String> mThumbnailUrlSubject = BehaviorSubject.create();
    private final PublishSubject<Fragment> mOpenPhotoDetailViewSubject = PublishSubject.create();


    public PhotosViewModel() {
        this(new PhotosController());
    }

    public PhotosViewModel(PhotosController controller) {
        super();


        loadPhotos
                .concatMap(controller::getPhotosByAlbumiD)
                .retry()
                .subscribe(mPhotos::onNext);


        setPhotoItemCommand
                .map(photo -> {
                    if (photo.title == null) {
                        return "";
                    }
                    return photo.title;
                })
                .retry()
                .subscribe(mTitleSubject::onNext);

        setPhotoItemCommand
                .map(photo -> {
                    if (photo.thumbnailUrl == null) {
                        return "";
                    }
                    return photo.thumbnailUrl;
                })
                .retry()
                .subscribe(mThumbnailUrlSubject::onNext);
        setPhotoItemCommand.map(photo -> photo).retry().subscribe(new Subscriber<Photo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Photo photo) {
                openPhotoDetailCommand.map(item -> FragmentBuilder.loadFragment(FragmentBuilder.FRAGMENT_DETAIL, BundleFactory.newBundle(photo))).subscribe(mOpenPhotoDetailViewSubject::onNext);
            }
        });

    }
}
