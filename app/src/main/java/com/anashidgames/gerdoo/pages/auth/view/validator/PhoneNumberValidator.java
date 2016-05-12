package com.anashidgames.gerdoo.pages.auth.view.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by psycho on 5/12/16.
 */
public class PhoneNumberValidator implements PsychoValidator<String> {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\d{10}", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean validate(String input) {
        input = input.replace("0098", "");
        input = input.replace("+98", "");
        if (input.charAt(0) == '0'){
            input = input.substring(1);
        }

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(input);
        return matcher.matches();
    }
}
