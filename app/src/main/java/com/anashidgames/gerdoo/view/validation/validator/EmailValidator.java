package com.anashidgames.gerdoo.view.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by psycho on 3/19/16.
 */
public class EmailValidator implements PsychoValidator<String>{

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean validate(String input) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(input);
        return matcher.find();
    }
}
