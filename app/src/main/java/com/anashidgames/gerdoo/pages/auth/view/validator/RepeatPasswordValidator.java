package com.anashidgames.gerdoo.pages.auth.view.validator;

import com.anashidgames.gerdoo.pages.auth.view.ValidatableInput;

/**
 * Created by psycho on 3/19/16.
 */
public class RepeatPasswordValidator extends PasswordValidator {

    private ValidatableInput passwordEdit;

    public RepeatPasswordValidator(ValidatableInput passwordEdit) {
        this.passwordEdit = passwordEdit;
    }

    @Override
    public boolean validate(String input) {
        return super.validate(input) && passwordEdit.getText().toString().equals(input);
    }
}
