package com.deliveryhero.deliveryherotest.viewmodels;

import android.support.v4.app.Fragment;

import com.deliveryhero.deliveryherotest.beans.models.Album;
import com.deliveryhero.deliveryherotest.controllers.AlbumsController;
import com.deliveryhero.deliveryherotest.ui.fragments.PhotosFragment;
import com.deliveryhero.deliveryherotest.ui.fragments.extras.FragmentBuilder;
import com.deliveryhero.deliveryherotest.utils.BundleFactory;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by MACB105 on 11/06/16.
 */
public final class AlbumsViewModel {

    // Observable


    public final Observable<String> title() {
        return mTitleSubject.asObservable();
    }

    public final Observable<List<Album>> albums() {
        return mAlbums.asObservable();
    }

    public final Observable<Fragment> openPhotosView() {
        return mOpenPhotosViewSubject.asObservable();
    }


    // Commands

    public final PublishRelay<String> loadAlbums = PublishRelay.create();
    /**
     * Send item to this command to trigger changes to the title subject.
     */
    public final PublishRelay<Album> setAlbumCommand = PublishRelay.create();
    /**
     * Send null to this command to request a photos fragment.
     */
    public final PublishRelay<Void> openPhotosCommand = PublishRelay.create();


    // Members

    private final BehaviorSubject<List<Album>> mAlbums = BehaviorSubject.create();
    private final BehaviorSubject<String> mTitleSubject = BehaviorSubject.create();
    private final PublishSubject<Fragment> mOpenPhotosViewSubject = PublishSubject.create();


    public AlbumsViewModel() {
        this(new AlbumsController());
    }

    public AlbumsViewModel(AlbumsController controller) {
        super();

        loadAlbums
                .concatMap(controller::getAlbums)
                .retry(2)
                .subscribe(mAlbums::onNext);


        setAlbumCommand
                .map(album -> {
                    if (album.title == null) {
                        return "";
                    }
                    return album.title;
                })
                .retry()
                .subscribe(mTitleSubject::onNext);

        setAlbumCommand.map(album -> album.id).retry().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                openPhotosCommand.map(item -> FragmentBuilder.loadFragment(FragmentBuilder.FRAGMENT_PHOTOS, BundleFactory.newBundle(PhotosFragment.ARG_SEL_ALBUMID, integer.toString()))).subscribe(mOpenPhotosViewSubject::onNext);
            }
        });
    }

}
