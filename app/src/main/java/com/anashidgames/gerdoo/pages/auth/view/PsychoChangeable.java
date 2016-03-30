package com.anashidgames.gerdoo.pages.auth.view;

/**
 * Created by psycho on 3/19/16.
 */
public interface PsychoChangeable {

    int STATE_EMPTY = 1;
    int STATE_VALID = 2;
    int STATE_INVALID = 3;

    void setStateChangedListener(StateChangedListener stateChangedListener);
    int getState();
    boolean isValid();


    interface StateChangedListener {
        void stateChanged(PsychoChangeable editText, int newState);
    }
}
