package com.rahnema.gerdoo.view.validation.validator;

import com.rahnema.gerdoo.view.validation.ValidatableInput;

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
