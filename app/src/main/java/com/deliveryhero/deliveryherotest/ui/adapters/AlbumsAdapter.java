package com.deliveryhero.deliveryherotest.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.beans.models.Album;
import com.deliveryhero.deliveryherotest.ui.adapters.viewholder.AlbumViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Adapter for displaying albums.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumViewHolder> implements Filterable {


    private final List<Album> mAlbums;
    private final Context mContext;
    private final List<Album> mFilteredAlbumList;

    private AlbumsFilter mAlbumFilter;


    public AlbumsAdapter(Context context, List<Album> albums) {
        super();

        mContext = context;
        mAlbums = albums;
        mFilteredAlbumList = new ArrayList<>(albums);
    }


    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_album, viewGroup, false);
        return new AlbumViewHolder(mContext, itemView);
    }

    @Override
    public int getItemCount() {
        return mFilteredAlbumList == null ? 0 : mFilteredAlbumList.size();
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder albumViewHolder, int position) {

        // Rebind
        albumViewHolder.bind();

        // Send current data to view model
        Album album = mFilteredAlbumList.get(position);
        albumViewHolder.getViewModel().setAlbumCommand.call(album);


    }

    @Override
    public Filter getFilter() {
        if (mAlbumFilter == null)
            mAlbumFilter = new AlbumsFilter(this, mAlbums);
        return mAlbumFilter;
    }

    private static class AlbumsFilter extends Filter {

        private final AlbumsAdapter adapter;
        private final List<Album> originalList;
        private final List<Album> filteredList;

        private AlbumsFilter(AlbumsAdapter adapter, List<Album> originalList) {
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

                for (final Album album : originalList) {
                    if (album.title.contains(filterPattern)) {
                        filteredList.add(album);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.mFilteredAlbumList.clear();
            adapter.mFilteredAlbumList.addAll((ArrayList<Album>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
