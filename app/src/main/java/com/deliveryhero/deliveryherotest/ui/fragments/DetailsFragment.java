package com.deliveryhero.deliveryherotest.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.ui.fragments.extras.DetailFragmentExtras;
import com.deliveryhero.deliveryherotest.viewmodels.DetailsViewModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;


public class DetailsFragment extends BaseFragment {


    private DetailsViewModel mDetailViewModel;

    @BindView(R.id.details_title)
    protected TextView mTitleView;

    @BindView(R.id.details_image)
    protected ImageView mImageView;

    @BindView(R.id.details_photo_card_view)
    protected CardView mCardView;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        DetailsFragment frag = new DetailsFragment();
        frag.setRetainInstance(true);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailview, container, false);
        ButterKnife.bind(this, view);
        mDetailViewModel = new DetailsViewModel();
        loadPhotoDetail();
        if (mListener != null) {
            mListener.onPhotoDetailFragmentInteraction();
        }
        return view;
    }


    private void loadPhotoDetail() {
        // Send item to view model
        DetailFragmentExtras extras = new DetailFragmentExtras();
        Icepick.restoreInstanceState(extras, getArguments());
        mDetailViewModel.setItemCommand.call(extras.selectedPhoto);


        // Bind title
        mDetailViewModel.title()
                .compose(bindToLifecycle())
                .subscribe(mTitleView::setText);

        // Bind image
        mDetailViewModel.imageUrl()
                .map(url -> Picasso.with(getActivity()).load(url))
                .compose(bindToLifecycle())
                .subscribe(picasso -> {
                    picasso.into(mImageView);
                });
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

        void onPhotoDetailFragmentInteraction();
    }
}
