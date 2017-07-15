package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/25/16.
 * Android has the ability to toast, which is a
 * little text popup that displays messages to
 * the users. We implement a ToastInterface so
 * we can send a toast to it. If we want to make
 * a toast for another type of app other than
 * AndroidLauncher we will have to also make the
 * underlying graphics for it.
 */
public interface ToastInterface {
    public void toast(final String t);
}
