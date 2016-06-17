package com.deliveryhero.deliveryherotest.ui.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.ui.activities.GalleryActivity;
import com.deliveryhero.deliveryherotest.viewmodels.AlbumsViewModel;
import com.trello.rxlifecycle.RxLifecycle;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jakewharton.rxbinding.view.RxView.clicks;
import static com.trello.rxlifecycle.RxLifecycle.bindView;


/**
 * ViewHolder used in AlbumsAdapter.
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder {

    // Members

    @BindView(R.id.album_title)
    protected TextView mTitleTextView;

    @BindView(R.id.albums_card)
    protected CardView mAlbumsCardView;

    private final Context mContext;
    private final AlbumsViewModel mViewModel;

    public AlbumViewHolder(Context context, View itemView) {
        super(itemView);

        mContext = context;

        mViewModel = new AlbumsViewModel();

        ButterKnife.bind(this, itemView);

    }


    /**
     * Bind all subject to it's views.
     */
    public void bind() {

        // Bind text
        mViewModel.title().compose(RxLifecycle.bindView(itemView)).subscribe(mTitleTextView::setText);

        // Bind clicks
        clicks(mAlbumsCardView)
                .subscribe(x -> mViewModel.openPhotosCommand.call(null));


        // Bind open details
        mViewModel.openPhotosView()
                .compose(bindView(itemView))
                .subscribe(((GalleryActivity) mContext)::switchFragment);

    }


    public AlbumsViewModel getViewModel() {
        return mViewModel;
    }
}
