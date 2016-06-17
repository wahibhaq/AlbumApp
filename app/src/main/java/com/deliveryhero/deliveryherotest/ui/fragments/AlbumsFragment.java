package com.deliveryhero.deliveryherotest.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.ui.adapters.AlbumsAdapter;
import com.deliveryhero.deliveryherotest.utils.ApiFailEvent;
import com.deliveryhero.deliveryherotest.utils.InternetConnectivity;
import com.deliveryhero.deliveryherotest.utils.SearchTextEvent;
import com.deliveryhero.deliveryherotest.viewmodels.AlbumsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class AlbumsFragment extends BaseFragment {

    private AlbumsViewModel mAlbumViewModel;
    private Bundle mSavedState;

    @BindView(R.id.albums_recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.infoTextView)
    protected TextView minfoView;

    private OnFragmentInteractionListener mListener;
    private AlbumsAdapter mAlbumAdapter;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    public static AlbumsFragment newInstance() {
        AlbumsFragment frag = new AlbumsFragment();
        frag.setRetainInstance(true);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ButterKnife.bind(this, view);
        loadAlbumsIfInternetAvailable();

        if (mListener != null) {
            mListener.onAlbumsFragmentInteraction();
        }
        return view;
    }

    private void loadAlbumsIfInternetAvailable() {
        if (InternetConnectivity.isNetworkAvailable(getActivity())) {
            initView();
            initViewModel();
            loadalbums();
        } else {
            minfoView.setText(getString(R.string.nointenet));
        }
    }

    private void initView() {
        // Recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

    private void initViewModel() {
        // View model
        mAlbumViewModel = new AlbumsViewModel();
        mAlbumViewModel.albums()
                .map(albums -> mAlbumAdapter = new AlbumsAdapter(getActivity(), albums))
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AlbumsAdapter>() {
                    @Override
                    public void onCompleted() {
                        minfoView.setText("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        minfoView.setText(getString(R.string.fetchingerror));
                    }

                    @Override
                    public void onNext(AlbumsAdapter albumsAdapter) {
                        minfoView.setText("");
                        mRecyclerView.setAdapter(albumsAdapter);
                    }
                });
    }


    private void loadalbums() {
        minfoView.setText(getString(R.string.fetching));
        mAlbumViewModel.loadAlbums.call("Albums");
        // Bind Albums
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchTextEvent event) {
        Log.d("receing info via bus", event.getSearchText());
        if (mAlbumAdapter != null)
            mAlbumAdapter.getFilter().filter(event.getSearchText());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ApiFailEvent event) {
        if (minfoView != null)
            minfoView.setText(event.getFailText());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onAlbumsFragmentInteraction();
    }
}
