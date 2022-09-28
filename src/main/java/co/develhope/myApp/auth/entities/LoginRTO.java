package co.develhope.myApp.auth.entities;

import co.develhope.myApp.users.entities.User;
import lombok.Data;

@Data
public class LoginRTO {

    private User user;

    private String JWT;
}
