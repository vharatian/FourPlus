package com.rahnema.gerdoo.view.validation;

/**
 * Created by psycho on 3/19/16.
 */
public interface PsychoChangeable {
    void setStateChangedListener(StateChangedListener stateChangedListener);
    int getState();


    interface StateChangedListener {
        void stateChanged(PsychoChangeable editText, int newState);
    }
}
