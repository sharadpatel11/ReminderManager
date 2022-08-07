package com.example.glm.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ListData")
public class ListData extends ParseObject {

    public static final String KEY_LISTNAME = "ListName";
    public final static String KEY_USER = "User";

    //empty constructor needed by Parceler library
    public ListData() {

    }

    public String getListName() {
        return getString(KEY_LISTNAME);
    }

    public void setListName(String listName) {
        put(KEY_LISTNAME, listName);
    }

    // Get the user for this item
    public ParseUser getOwner()  {
        return getParseUser(KEY_USER);
    }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put(KEY_USER, user);
    }

}
