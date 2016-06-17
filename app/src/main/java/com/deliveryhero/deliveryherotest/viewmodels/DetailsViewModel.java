package com.deliveryhero.deliveryherotest.viewmodels;

import com.deliveryhero.deliveryherotest.beans.models.Photo;
import com.jakewharton.rxrelay.PublishRelay;

import rx.Observable;
import rx.subjects.BehaviorSubject;


public final class DetailsViewModel {


    public final Observable<String> title() {
        return mTitleSubject.asObservable();
    }

    public final Observable<String> imageUrl() {
        return mImageUrlSubject.asObservable();
    }

    // Commands
    public final PublishRelay<Photo> setItemCommand = PublishRelay.create();
    public final PublishRelay<Void> loadDetailsCommand = PublishRelay.create();

    // Members

    private BehaviorSubject<String> mTitleSubject = BehaviorSubject.create();
    private BehaviorSubject<String> mImageUrlSubject = BehaviorSubject.create();


    public DetailsViewModel() {


        setItemCommand
                .map(item -> item.title)
                .subscribe(mTitleSubject::onNext);

        setItemCommand
                .map(item -> item.url)
                .subscribe(mImageUrlSubject::onNext);
    }
}
