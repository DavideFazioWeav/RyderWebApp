package co.develhope.myApp.auth.entities;

import lombok.Data;


@Data
public class SingUpDTO {


    private String name;
    private String surname;
    private String email;
    private String password;

}
