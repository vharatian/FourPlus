package com.anashidgames.gerdoo.view.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;

/**
 * Created by psycho on 3/21/16.
 */
public class PersianHintGravityTextWatcher implements TextWatcher {

    private EditText editText;

    public PersianHintGravityTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() <= 0){
            editText.setGravity(Gravity.RIGHT);
        }else {
            editText.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
