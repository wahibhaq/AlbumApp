package com.deliveryhero.deliveryherotest.utils;

import android.os.Bundle;

import com.deliveryhero.deliveryherotest.beans.models.Photo;
import com.deliveryhero.deliveryherotest.ui.fragments.extras.DetailFragmentExtras;

/**
 * Created by Hamza_MACB105 on 15/06/16.
 */
public class BundleFactory {

    public static Bundle newBundle(String key, String value) {

        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    public static Bundle newBundle(Photo photo) {

        DetailFragmentExtras detailFragmentExtras = new DetailFragmentExtras();
        detailFragmentExtras.selectedPhoto = photo;
        return detailFragmentExtras.buildBundle();
    }

}
