package com.hrushko.command.factory;

import java.util.stream.Stream;

public enum CommandType {
    LOGIN("login", RequestType.POST),
    LOGIN_PAGE("login_page",RequestType.GET),
    REGISTER_PAGE("register_page", RequestType.GET),
    LOGOUT("logout", RequestType.GET),
    REGISTER("register", RequestType.POST),
    ADD_EVENT("add_event", RequestType.POST),
    ADD_EVENT_PAGE("add_event_page", RequestType.GET),
    REMOVE_EVENT("remove_event", RequestType.GET),
    EDIT_EVENT("edit_event", RequestType.POST),
    EDIT_EVENT_PAGE("edit_event_page", RequestType.GET),
    PROFILE("profile", RequestType.GET),
    EDIT_USER("edit_user", RequestType.POST),
    EDIT_USER_INFO("edit_user_info", RequestType.POST),
    EDIT_USER_PHOTO("edit_user_photo", RequestType.POST),
    DELETE_USER("delete_user", RequestType.POST),
    REGISTER_TO_EVENT("register_to_event", RequestType.POST),
    LEAVE_EVENT("leave_event", RequestType.GET),
    USER_EVENT("user_event", RequestType.GET),
    GET_USER_ON_EVENT("get_user_on_event", RequestType.GET),
    HOME_PAGE("home_page", RequestType.GET),
    CHANGE_LANGUAGE("change_language", RequestType.GET),
    ALL_USERS("all_users", RequestType.GET),
    START_PAGE("start_page",RequestType.GET),
    ALL_EVENTS("all_events", RequestType.GET),
    ADD_ROLE("add_role", RequestType.POST),
    ADD_THEME("add_theme", RequestType.POST),
    ADD_PERMISSION("add_permission", RequestType.POST),
    ADD_ROLE_PAGE("add_role_page", RequestType.GET),
    ADD_THEME_PAGE("add_theme_page", RequestType.GET),
    ADD_PERMISSION_PAGE("add_permission_page", RequestType.GET),
    CHANGE_USER_PERMISSION("change_user_permission", RequestType.POST),
    MODIFY_ANY_EVENT("modify_any_event", RequestType.GET),
    DELETE_ANY_EVENT("delete_any_event", RequestType.GET);

    private String fieldName;
    private RequestType requestType;

    CommandType(String fieldName, RequestType requestType){
        this.fieldName = fieldName;
        this.requestType = requestType;
    }

    public RequestType getRequestType(){
        return requestType;
    }

    public static CommandType of(String command) {
        return Stream.of(CommandType.values())
                .filter(c -> c.fieldName.equalsIgnoreCase(command))
                .findFirst().orElse(HOME_PAGE);
    }

}
