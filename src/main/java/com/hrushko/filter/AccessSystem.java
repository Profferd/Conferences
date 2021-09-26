package com.hrushko.filter;

import com.hrushko.command.factory.CommandType;
import com.hrushko.entity.Permission;
import com.hrushko.entity.Rule;
import com.hrushko.entity.UserDto;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.EnumSet;

public class AccessSystem {
    private static final Logger LOGGER = LogManager.getLogger(AccessSystem.class);
    private static EnumSet<CommandType> commandTypes = EnumSet.noneOf(CommandType.class);

    public static void updateRules(UserDto userDto) {
        commandTypes.clear();
        if (userDto == null) {
            makeRulesForGuest();
            return;
        } else {
            addMinimalRules();
        }
        Permission specialRules = userDto.getPermission();
        specialRules.getRules().stream()
                .map(Rule::getCommands)
                .forEach(commandTypes::addAll);
    }

    public static boolean checkAccess(CommandType commandType) {
        return commandTypes.contains(commandType);
    }

    public static boolean checkAccess(String command) {
        return checkAccess(CommandType.of(command));
    }

    private static void addMinimalRules() {
        commandTypes = EnumSet.of(CommandType.LOGOUT, CommandType.ADD_EVENT_PAGE,
                CommandType.ADD_EVENT, CommandType.EDIT_USER,
                CommandType.EDIT_USER_INFO, CommandType.PROFILE, CommandType.REGISTER_TO_EVENT,
                CommandType.LEAVE_EVENT, CommandType.USER_EVENT, CommandType.EDIT_USER_PHOTO,
                CommandType.EDIT_EVENT, CommandType.EDIT_EVENT_PAGE, CommandType.REMOVE_EVENT,
                CommandType.GET_USER_ON_EVENT);
        commandTypes.addAll(getCommonRules());
    }

    private static void makeRulesForGuest() {
        commandTypes = EnumSet.of(CommandType.LOGIN_PAGE, CommandType.LOGIN, CommandType.REGISTER,
                CommandType.REGISTER_PAGE);
        commandTypes.addAll(getCommonRules());
    }

    private static EnumSet<CommandType> getCommonRules() {
        return EnumSet.of(CommandType.HOME_PAGE, CommandType.CHANGE_LANGUAGE, CommandType.START_PAGE,
                CommandType.ALL_EVENTS);
    }
}
