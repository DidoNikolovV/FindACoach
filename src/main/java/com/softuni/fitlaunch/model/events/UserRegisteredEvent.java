package com.softuni.fitlaunch.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {

    private final String userEmail;
    private final String usernames;

    public UserRegisteredEvent(Object source, String userEmail, String usernames) {
        super(source);
        this.userEmail = userEmail;
        this.usernames = usernames;
    }

}
