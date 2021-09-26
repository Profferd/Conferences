package com.hrushko.entity;

import com.hrushko.command.factory.CommandType;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

public enum Rule {
    MODIFY_ANY_EVENT("modify_any_event", EnumSet.of(CommandType.MODIFY_ANY_EVENT, CommandType.DELETE_ANY_EVENT)),
    CHANGE_USER_PERMISSION("change_user_permission", EnumSet.of(CommandType.CHANGE_USER_PERMISSION)),
    ALL_USERS("all_users", EnumSet.of(CommandType.ALL_USERS)),
    DELETE_USER("delete_user", EnumSet.of(CommandType.DELETE_USER)),
    ADD_THEME("add_theme", EnumSet.of(CommandType.ADD_THEME, CommandType.ADD_THEME_PAGE)),
    ADD_ROLE("add_role", EnumSet.of(CommandType.ADD_ROLE, CommandType.ADD_ROLE_PAGE)),
    ADD_PERMISSION("add_permission", EnumSet.of(CommandType.ADD_PERMISSION, CommandType.ADD_PERMISSION_PAGE));


    private final String value;
    private final EnumSet<CommandType> commands;

    private Rule(String value, EnumSet<CommandType> commandTypes) {
        this.value = value;
        this.commands = commandTypes;
    }

    public static Optional<Rule> of(String value) {
        return Stream.of(Rule.values())
                .filter(c -> c.value.equalsIgnoreCase(value))
                .findFirst();
    }

    public String getValue(){
        return value;
    }

    public EnumSet<CommandType> getCommands() {
        return commands;
    }
}
