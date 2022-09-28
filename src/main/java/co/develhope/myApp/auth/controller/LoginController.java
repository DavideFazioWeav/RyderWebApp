package co.develhope.myApp.auth.controller;

import co.develhope.myApp.JwtTokenFilter;
import co.develhope.myApp.auth.entities.LoginDTO;
import co.develhope.myApp.auth.entities.LoginRTO;
import co.develhope.myApp.auth.services.LoginService;
import co.develhope.myApp.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @PostMapping("/login")
    public LoginRTO login(@RequestBody LoginDTO loginDTO) throws Exception {
        LoginRTO loginRTO=loginService.login(loginDTO);
        if (loginRTO == null) throw new Exception("Cannot Login");
        return loginRTO;
    }

}
