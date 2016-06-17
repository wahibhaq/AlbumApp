package com.deliveryhero.deliveryherotest.utils;

import android.os.Bundle;

import icepick.Icepick;

/**
 * Base class for every intent extra that uses Icicle.
 */
public class IcepickIntentExtras {

    /**
     * Build a bundle for the underlying member variables.
     */
    public Bundle buildBundle() {
        Bundle bundle = new Bundle();
        Icepick.saveInstanceState(this, bundle);

        return bundle;
    }
}
