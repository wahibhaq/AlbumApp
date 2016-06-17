package com.deliveryhero.deliveryherotest.ui.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.ui.activities.GalleryActivity;
import com.deliveryhero.deliveryherotest.viewmodels.PhotosViewModel;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.RxLifecycle;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jakewharton.rxbinding.view.RxView.clicks;
import static com.trello.rxlifecycle.RxLifecycle.bindView;


/**
 * ViewHolder used in AlbumsAdapter.
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo_title)
    protected TextView mTitleTextView;

    @BindView(R.id.photo_thumbnail)
    protected ImageView mThumnailImageView;

    @BindView(R.id.photo_card_view)
    protected CardView mPhotosCardView;

    private final Context mContext;
    private final PhotosViewModel mViewModel;


    public PhotoViewHolder(Context context, View itemView) {
        super(itemView);

        mContext = context;

        mViewModel = new PhotosViewModel();

        ButterKnife.bind(this, itemView);

    }

    /**
     * Bind all subject to it's views.
     */
    public void bind() {

        // Bind text
        mViewModel.title().compose(RxLifecycle.bindView(itemView)).subscribe(mTitleTextView::setText);

        // Bind thumbnail
        mViewModel.thumbnail()
                .map(url -> Picasso.with(mContext).load(url))
                .compose(RxLifecycle.bindView(itemView))
                .subscribe(picasso -> {
                    picasso.into(mThumnailImageView);
                });

        // Bind clicks
        clicks(mPhotosCardView)
                .subscribe(x -> mViewModel.openPhotoDetailCommand.call(null));


        // Bind open details
        mViewModel.openPhotoDetailView()
                .compose(bindView(itemView))
                .subscribe(((GalleryActivity) mContext)::switchFragment);
    }

    public PhotosViewModel getViewModel() {
        return mViewModel;
    }
}
