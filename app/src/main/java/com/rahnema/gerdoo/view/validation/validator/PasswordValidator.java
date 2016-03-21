package com.rahnema.gerdoo.view.validation.validator;

/**
 * Created by psycho on 3/19/16.
 */
public class PasswordValidator implements PsychoValidator<String>{

    public static final int MINIMUM_LENGTH = 4;

    @Override
    public boolean validate(String input) {
        return input.length() >= MINIMUM_LENGTH;
    }
}
