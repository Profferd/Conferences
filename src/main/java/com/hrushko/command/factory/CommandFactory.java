package com.hrushko.command.factory;

import com.hrushko.command.action.*;
import com.hrushko.command.action.authorization.*;
import com.hrushko.command.action.event.*;
import com.hrushko.command.action.special.*;
import com.hrushko.command.action.userAction.*;

import java.util.EnumMap;
import java.util.Map;

import static com.hrushko.command.factory.CommandType.*;

public class CommandFactory {
    private static Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);

    static {
        commands.put(LOGIN, new LoginCommand());
        commands.put(LOGOUT, new LogoutCommand());
        commands.put(REGISTER, new RegisterCommand());
        commands.put(ADD_EVENT, new AddEventCommand());
        commands.put(REMOVE_EVENT, new RemoveEventCommand());
        commands.put(EDIT_EVENT, new EditEventCommand());
        commands.put(EDIT_EVENT_PAGE, new EditEventPageCommand());
        commands.put(PROFILE, new ProfileCommand());
        commands.put(EDIT_USER, new EditUserCommand());
        commands.put(EDIT_USER_INFO, new EditUserInfoCommand());
        commands.put(EDIT_USER_PHOTO, new EditUserPhotoCommand());
        commands.put(DELETE_USER, new DeleteUserCommand());
        commands.put(REGISTER_TO_EVENT, new RegisterToEventCommand());
        commands.put(LEAVE_EVENT, new LeaveEventCommand());
        commands.put(USER_EVENT, new UserEventsCommand());
        commands.put(HOME_PAGE, new HomePageCommand());
        commands.put(CHANGE_LANGUAGE, new ChangeLanguageCommand());
        commands.put(LOGIN_PAGE, new LoginPageCommand());
        commands.put(REGISTER_PAGE, new RegisterPageCommand());
        commands.put(ADD_EVENT_PAGE, new AddEventPageCommand());
        commands.put(ALL_USERS, new AllUsersCommand());
        commands.put(START_PAGE, new HomePageCommand());
        commands.put(ALL_EVENTS, new AllEventsCommand());
        commands.put(ADD_ROLE, new AddRoleCommand());
        commands.put(ADD_ROLE_PAGE, new AddRolePageCommand());
        commands.put(ADD_THEME, new AddThemeCommand());
        commands.put(ADD_THEME_PAGE, new AddThemePageCommand());
        commands.put(ADD_PERMISSION, new AddPermissionCommand());
        commands.put(ADD_PERMISSION_PAGE, new AddPermissionPageCommand());
        commands.put(CHANGE_USER_PERMISSION, new ChangeUserPermissionCommand());
        commands.put(GET_USER_ON_EVENT, new UsersOnEventCommand());
    }

    public static Command create(CommandType commandType) {
        return commands.get(commandType);
    }
}
