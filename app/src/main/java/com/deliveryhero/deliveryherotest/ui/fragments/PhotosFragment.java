package com.deliveryhero.deliveryherotest.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.ui.adapters.PhotosAdapter;
import com.deliveryhero.deliveryherotest.utils.ApiFailEvent;
import com.deliveryhero.deliveryherotest.utils.AutoFitRecyclerView;
import com.deliveryhero.deliveryherotest.utils.InternetConnectivity;
import com.deliveryhero.deliveryherotest.utils.SearchTextEvent;
import com.deliveryhero.deliveryherotest.viewmodels.PhotosViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class PhotosFragment extends BaseFragment {

    public final static String ARG_SEL_ALBUMID = "selectedalbumid";

    private PhotosViewModel mPhotoViewModel;
    private String mSelectedAlbumId;

    @BindView(R.id.photos_recycler_view)
    protected AutoFitRecyclerView mRecyclerView;

    @BindView(R.id.infoTextView)
    protected TextView minfoView;

    private PhotosAdapter mPhotosAdapter;
    private OnFragmentInteractionListener mListener;

    public PhotosFragment() {
    }

    public static PhotosFragment newInstance() {
        PhotosFragment frag = new PhotosFragment();
        frag.setRetainInstance(true);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this, view);
        mSelectedAlbumId = (String) getArguments().get(ARG_SEL_ALBUMID);
        loadPhotosIfInternetAvailable();
        if (mListener != null) {
            mListener.onPhotosFragmentInteraction();
        }
        return view;
    }

    private void loadPhotosIfInternetAvailable() {
        if (InternetConnectivity.isNetworkAvailable(getActivity())) {
            initView();
            initViewModel();
            loadPhotos();
        } else {
            minfoView.setText(getString(R.string.nointenet));
        }
    }

    private void initView() {
        // Recycler
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

    private void initViewModel() {
        // View model
        mPhotoViewModel = new PhotosViewModel();
        // Bind search results

        mPhotoViewModel.photos()
                .map(photos -> mPhotosAdapter = new PhotosAdapter(getActivity(), photos))
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PhotosAdapter>() {
                    @Override
                    public void onCompleted() {
                        minfoView.setText("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        minfoView.setText(getString(R.string.fetchingerror));
                    }

                    @Override
                    public void onNext(PhotosAdapter photosAdapter) {
                        minfoView.setText("");
                        mRecyclerView.setAdapter(photosAdapter);
                    }
                });
    }

    private void loadPhotos() {
        minfoView.setText(getString(R.string.fetching));
        mPhotoViewModel.loadPhotos.call(mSelectedAlbumId);
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
        if (mPhotosAdapter != null)
            mPhotosAdapter.getFilter().filter(event.getSearchText());
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
        void onPhotosFragmentInteraction();
    }
}
