package com.deliveryhero.deliveryherotest.ui.fragments.extras;

import android.os.Bundle;

import com.deliveryhero.deliveryherotest.ui.fragments.AlbumsFragment;
import com.deliveryhero.deliveryherotest.ui.fragments.BaseFragment;
import com.deliveryhero.deliveryherotest.ui.fragments.DetailsFragment;
import com.deliveryhero.deliveryherotest.ui.fragments.PhotosFragment;

/**
 * Created by Hamza_MACB105 on 13/06/16.
 */
public class FragmentBuilder {

    public final static int FRAGMENT_ALBUMS = 1;
    public final static int FRAGMENT_PHOTOS = 2;
    public final static int FRAGMENT_DETAIL = 3;


    public static BaseFragment loadFragment(int fragmentId, Bundle arguments) {
        BaseFragment fragment = BaseFragment.newInstance();
        switch (fragmentId) {
            case FRAGMENT_ALBUMS:
                fragment = AlbumsFragment.newInstance();
                fragment.setArguments(arguments);
                break;
            case FRAGMENT_PHOTOS:
                fragment = PhotosFragment.newInstance();
                fragment.setArguments(arguments);
                break;
            case FRAGMENT_DETAIL:
                fragment = DetailsFragment.newInstance();
                fragment.setArguments(arguments);
                break;
            default:
                fragment = BaseFragment.newInstance();
        }
        return fragment;
    }

}

