package com.anashidgames.gerdoo.pages.auth;

import com.anashidgames.gerdoo.view.validation.PsychoChangeable;
import com.anashidgames.gerdoo.view.validation.ValidatableInput;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by psycho on 3/21/16.
 */
public class AuthStateChangeListener implements PsychoChangeable.StateChangedListener, PsychoChangeable{

        public static final int STATE_NOT_SPECIFIED = -1;

        private Map<ValidatableInput, Integer> states = new HashMap<>();
        private int state = STATE_NOT_SPECIFIED;

        private PsychoChangeable.StateChangedListener listener;

        public AuthStateChangeListener(PsychoChangeable.StateChangedListener listener) {
            this.listener = listener;
        }

        @Override
        public void stateChanged(PsychoChangeable changeable, int newState) {
            states.put((ValidatableInput) changeable, newState);

            checkState();
        }

    private void checkState() {
        int newState = getState();
        if(state != newState) {
            state = newState;
            listener.stateChanged(this, newState);
        }
    }

    @Override
    public void setStateChangedListener(PsychoChangeable.StateChangedListener stateChangedListener) {
        listener = stateChangedListener;
    }

    public int getState(){
        int state = ValidatableInput.STATE_VALID;
        for(ValidatableInput key: states.keySet()){
            if(states.get(key) != ValidatableInput.STATE_VALID){
                state = ValidatableInput.STATE_INVALID;
            }
        }

        return state;
    }

    public boolean isValid(){
        return getState() == ValidatableInput.STATE_VALID;
    }

    protected void addEditText(PsychoChangeable editText){
        states.put((ValidatableInput) editText, STATE_NOT_SPECIFIED);
        editText.setStateChangedListener(this);
    }


}

