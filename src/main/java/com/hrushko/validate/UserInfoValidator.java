package com.hrushko.validate;

import com.hrushko.entity.UserInfo;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoValidator implements Validator<UserInfo> {
    private static final String ENTITY_IS_NULL = "entity is null";
    private static final String DATE_OF_BIRTH_IS_INCORRECT = "date of birth is incorrect";
    private static final String EMAIL = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$";
    private static final String EMAIL_IS_INCORRECT = "email is incorrect";

    @Override
    public String isValid(UserInfo entity) {
        if (entity == null) {
            return ENTITY_IS_NULL;
        }
        if (entity.getDateOfBirth() == null) return null;
        if (entity.getDateOfBirth().after(new Date()) ||
                entity.getDateOfBirth().before(new Date(1))) {
            return DATE_OF_BIRTH_IS_INCORRECT;
        }
        Pattern pattern = Pattern.compile(EMAIL);
        Matcher matcher = pattern.matcher(entity.getEmail());
        if (!matcher.find()){
            return EMAIL_IS_INCORRECT;
        }
        return null;
    }
}
