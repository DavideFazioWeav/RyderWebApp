package co.develhope.myApp.auth.controller;

import co.develhope.myApp.auth.entities.SingUpActivationDTO;
import co.develhope.myApp.auth.entities.SingUpDTO;
import co.develhope.myApp.auth.services.SignUpService;
import co.develhope.myApp.users.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @PostMapping("/up")
    public User signUp(@RequestBody SingUpDTO singUpDTO, String role) throws Exception {
        return signUpService.signUp(singUpDTO, role);
    }

    @PostMapping("/activation")
    public User  activation(@RequestBody SingUpActivationDTO singUpActivationDTO) throws Exception {
        return signUpService.activate(singUpActivationDTO);
    }

}
