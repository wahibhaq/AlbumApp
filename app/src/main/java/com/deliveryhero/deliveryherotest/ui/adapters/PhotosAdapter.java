package com.deliveryhero.deliveryherotest.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.beans.models.Photo;
import com.deliveryhero.deliveryherotest.ui.adapters.viewholder.PhotoViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Adapter for displaying albums.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotoViewHolder> implements Filterable {


    private final List<Photo> mPhotos;
    private final Context mContext;
    private final List<Photo> mFilteredPhotoList;

    private PhotosFilter mPhotoFilter;

    public PhotosAdapter(Context context, List<Photo> photos) {
        super();

        mContext = context;
        mPhotos = photos;
        mFilteredPhotoList = new ArrayList<>(photos);
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_photo, viewGroup, false);
        return new PhotoViewHolder(mContext, itemView);
    }

    @Override
    public int getItemCount() {
        return mFilteredPhotoList == null ? 0 : mFilteredPhotoList.size();
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder photoViewHolder, int position) {

        // Rebind
        photoViewHolder.bind();

        // Send current data to view model
        Photo photo = mFilteredPhotoList.get(position);
        photoViewHolder.getViewModel().setPhotoItemCommand.call(photo);
    }

    @Override
    public Filter getFilter() {
        if (mPhotoFilter == null)
            mPhotoFilter = new PhotosFilter(this, mPhotos);
        return mPhotoFilter;
    }

    private static class PhotosFilter extends Filter {

        private final PhotosAdapter adapter;
        private final List<Photo> originalList;
        private final List<Photo> filteredList;

        private PhotosFilter(PhotosAdapter adapter, List<Photo> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Photo photo : originalList) {
                    if (photo.title.contains(filterPattern)) {
                        filteredList.add(photo);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.mFilteredPhotoList.clear();
            adapter.mFilteredPhotoList.addAll((ArrayList<Photo>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
