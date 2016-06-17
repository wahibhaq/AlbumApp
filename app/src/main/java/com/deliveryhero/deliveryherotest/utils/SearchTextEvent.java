package com.deliveryhero.deliveryherotest.utils;

/**
 * Created by Hamza_MACB105 on 16/06/16.
 */
public class SearchTextEvent {

    private final String searchText;

    public SearchTextEvent(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }
}
