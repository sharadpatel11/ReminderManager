package com.example.glm.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ReminderData")
public class ReminderData extends ParseObject {

    public static final String KEY_UUID = "reminderID";
    public static final String KEY_REMINDERNAME = "reminderName";
    public static final String KEY_REMINDERTYPE = "reminderType";
    public static final String KEY_BELONGLIST = "reminderList";
    public static final Boolean KEY_CHECKOFF = false;
    public final static String KEY_USER = "user";
    public final static String KEY_DAY = "reminderDay";

    public ReminderData() {
        super();
    }

    public String getReminderID() {
        return getString(KEY_UUID);
    }

    public void setReminderID(String reminderID) {
        put(KEY_UUID, reminderID);
    }

    public String getReminderName() {
        return getString(KEY_REMINDERNAME);
    }

    public void setReminderName(String reminderName) {
        put(KEY_REMINDERNAME, reminderName);
    }

    public String getReminderType() {
        return getString(KEY_REMINDERTYPE);
    }

    public void setReminderType(String reminderType) {
        put(KEY_REMINDERTYPE, reminderType);
    }

    public String getBelongList() {
        return getString(KEY_BELONGLIST);
    }

    public void setBelongList(String belongList) {
        put(KEY_BELONGLIST, belongList);
    }

    public boolean getCheckOff() {
        return getBoolean(KEY_CHECKOFF.toString());
    }

    public void setCheckOff(boolean checkOff) {
        put(KEY_CHECKOFF.toString(), checkOff);
    }

    // Get the user for this item
    public ParseUser getOwner()  {
        return getParseUser(KEY_USER);
    }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getReminderDay(){
        return getString(KEY_DAY);
    }

    public void setReminderDay(String reminderDay){
        put(KEY_DAY, reminderDay);
    }
}
