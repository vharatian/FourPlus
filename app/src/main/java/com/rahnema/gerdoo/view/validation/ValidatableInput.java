package com.rahnema.gerdoo.view.validation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.rahnema.gerdoo.R;
import com.rahnema.gerdoo.view.validation.validator.PsychoValidator;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 3/19/16.
 */
public class ValidatableInput extends LinearLayout implements PsychoChangeable {

    public static final int STATE_EMPTY = 1;
    public static final int STATE_VALID = 2;
    public static final int STATE_INVALID = 3;

    private EditText editText;
    private GifImageView statusIcon;

    private PsychoValidator<String> validator;
    private int state = -1;
    private PsychoChangeable.StateChangedListener stateChangedListener;

    public ValidatableInput(Context context) {
        super(context);
        init(context);
    }

    public ValidatableInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValidatableInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ValidatableInput(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.input_layout_with_status_icon, this);

        editText = (EditText) findViewById(R.id.input);
        statusIcon = (GifImageView) findViewById(R.id.statusIcon);

        editText.addTextChangedListener(new InnerTextWatcher());
        editText.addTextChangedListener(new PersianHintGravityTextWatcher(editText));
        checkState();
    }

    private void checkState(){
        int newState;
        if(getText().isEmpty()){
            newState = STATE_EMPTY;
        }else if(validator.validate(getText())){
            newState = STATE_VALID;
        }else {
            newState = STATE_INVALID;
        }

        if(state != newState) {
            state = newState;
            if(stateChangedListener != null){
                stateChangedListener.stateChanged(this, newState);
            }

            setBackground(state);
        }
    }

    private void setBackground(int state) {
        int backResource = -1;
        if(state == STATE_EMPTY){
            backResource = R.drawable.edit_text_background_normal;
        }else if(state == STATE_VALID){
            backResource = R.drawable.edit_text_background_valid;
        }else if(state == STATE_INVALID){
            backResource = R.drawable.edit_text_background_invalid;
        }

        editText.setBackgroundResource(backResource);
    }

    public void setValidator(PsychoValidator validator) {
        this.validator = validator;
    }

    @Override
    public void setStateChangedListener(StateChangedListener stateChangedListener) {
        this.stateChangedListener = stateChangedListener;
    }

    @Override
    public int getState() {
        return state;
    }

    public String getText(){
        return editText.getText().toString();
    }

    public void showErrorStatusIcon() {
        statusIcon.setVisibility(VISIBLE);
        statusIcon.setImageResource(R.drawable.zarbdar);
    }

    public void hidStatusIcon(){
        statusIcon.setVisibility(INVISIBLE);
    }

    public void setHintMessage(int messageResource) {
        editText.setHint(messageResource);
    }

    public void setRightIcon(int drawableResource) {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableResource, 0);
    }

    public void setInputType(int type){
        editText.setInputType(type);
    }


    private class InnerTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            checkState();
            hidStatusIcon();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkState();
            hidStatusIcon();
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkState();
            hidStatusIcon();
        }
    }
}
