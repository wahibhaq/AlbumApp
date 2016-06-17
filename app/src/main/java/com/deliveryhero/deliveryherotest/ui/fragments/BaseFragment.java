package com.deliveryhero.deliveryherotest.ui.fragments;

/**
 * Created by Hamza_MACB105 on 13/06/16.
 */

import android.app.Activity;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Super class to be used by all fragments in order to have a activateSave to the
 * main activity
 * Created by Hamza_MACB105 on 13/06/16.
 */
public class BaseFragment extends RxFragment {

    //The activateSave on the GalleryActivity
    protected IActivityCallback mCallback;


    public static BaseFragment newInstance() {
        BaseFragment frag = new BaseFragment();
        frag.setRetainInstance(true);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (IActivityCallback) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
            //Toast.makeText(activity,"Failed to cast class, Wrong Activity",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets the title of the ActionBar present in the MainActivity
     *
     * @param title
     */
    public void setTitle(String title) {
        if (mCallback != null)
            mCallback.setActionBarTitle(title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    //The interface implemented by the main activity
    public interface IActivityCallback {
        void setActionBarTitle(String title);
    }
}

