package com.home.sample.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.home.sample.constants.AppUsers;

import org.springframework.security.core.userdetails.User;

public class UserUtils {

    public static Optional<User> getUser(final String userName) {
        return Stream.of(AppUsers.values())
                .map(user -> filterByName(user, userName))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private static User filterByName(final AppUsers user, final String userName) {
        return (user.getName().equals(userName)) 
            ? new User(user.getName(), user.getPassword(), user.getRoles()) 
            : null;
    }
}
