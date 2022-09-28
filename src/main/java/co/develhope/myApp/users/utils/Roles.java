package co.develhope.myApp.users.utils;

import co.develhope.myApp.users.entities.User;

import javax.persistence.Table;

public class Roles {

    public final static String RESTAURANT="RESTAURANT";
    public final static String REGISTERED="REGISTERED";
    public final static String RIDER="RIDER";

    public static boolean hasRole(User user, String roleInput){
        return user.getRoles().stream().filter(role -> role.getName().equals(roleInput)).count()!=0;
    }
}
