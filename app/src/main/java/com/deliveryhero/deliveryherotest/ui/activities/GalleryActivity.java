package com.deliveryhero.deliveryherotest.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.deliveryhero.deliveryherotest.R;
import com.deliveryhero.deliveryherotest.ui.fragments.AlbumsFragment;
import com.deliveryhero.deliveryherotest.ui.fragments.DetailsFragment;
import com.deliveryhero.deliveryherotest.ui.fragments.PhotosFragment;
import com.deliveryhero.deliveryherotest.ui.fragments.extras.FragmentBuilder;
import com.deliveryhero.deliveryherotest.utils.SearchTextEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

public class GalleryActivity extends RxAppCompatActivity implements AlbumsFragment.OnFragmentInteractionListener, PhotosFragment.OnFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    private Menu mMenu;
    private MenuItem mSearchMenuItem;
    private Boolean mEnableBackButton = false;
    private Fragment mLoadedFragment;
    private final static String ARG_FRAG_TAG = "loadedfragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        mLoadedFragment = FragmentBuilder.loadFragment(FragmentBuilder.FRAGMENT_ALBUMS, new Bundle());
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mLoadedFragment = getSupportFragmentManager().getFragment(savedInstanceState, ARG_FRAG_TAG);

        }
        switchFragment(mLoadedFragment);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        if(mLoadedFragment!=null)
        getSupportFragmentManager().putFragment(outState, ARG_FRAG_TAG, mLoadedFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLoadedFragment = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mLoadedFragment = null;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void switchFragment(Fragment fragment) {

        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container, fragment, fragmentTag);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
        mLoadedFragment = fragment;
        hideSearchViewIfVisibile();
    }

    public void changeActionBarTitle(String text) {
        getSupportActionBar().setTitle(text);
    }

    public void backButtonVisibility(boolean visibility) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(visibility);
    }

    public void searchButtonVisibility(boolean visibility) {
        if (mMenu == null)
            return;
        mMenu.setGroupVisible(R.id.main_menu_group, visibility);

    }

    public void hideSearchViewIfVisibile() {
        if (mSearchMenuItem != null && mSearchMenuItem.getActionView().isShown())
        {
            mSearchMenuItem.getActionView().clearFocus();
            mSearchMenuItem.collapseActionView();
        }
    }


    /**
     * Fragments Interaction Listeners.
     */

    @Override
    public void onAlbumsFragmentInteraction() {
        changeActionBarTitle(getString(R.string.albums));
        backButtonVisibility(false);
        searchButtonVisibility(true);
        mEnableBackButton = false;
    }

    @Override
    public void onPhotosFragmentInteraction() {
        changeActionBarTitle(getString(R.string.photos));
        backButtonVisibility(true);
        searchButtonVisibility(true);
        mEnableBackButton = true;

    }

    @Override
    public void onPhotoDetailFragmentInteraction() {
        changeActionBarTitle(getString(R.string.photodetail));
        backButtonVisibility(true);
        searchButtonVisibility(false);
        mEnableBackButton = true;

    }


    /**
     * Action Bar Search Listeners.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("send info via bus", newText);
        EventBus.getDefault().post(new SearchTextEvent(newText));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mEnableBackButton) {
            super.onBackPressed();
        }
    }

}
