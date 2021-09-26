package com.hrushko.validate;

import com.hrushko.entity.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {
    private static final Logger logger = LogManager.getLogger(UserValidator.class);
    private static final String LOGIN = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
    private static final String PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*";

    private static final String ENTITY_IS_NULL = "entity is null";

    @Override
    public String isValid(User entity) {
        if (entity == null){
            return ENTITY_IS_NULL;
        }

        Pattern pattern = Pattern.compile(LOGIN);

        Matcher matcher = pattern.matcher(entity.getLogin());
        if (!matcher.find()){
            return "login is invalid!";
        }

        if (entity.getPassword() == null){
            return null;
        }
        pattern =  Pattern.compile(PASSWORD);

        matcher = pattern.matcher(entity.getPassword());

        if (!matcher.find()){
            return "password is invalid";
        }
        return null;
    }
}
