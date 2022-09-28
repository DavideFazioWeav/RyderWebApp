package co.develhope.myApp.auth.services;

import co.develhope.myApp.auth.entities.SingUpActivationDTO;
import co.develhope.myApp.auth.entities.SingUpDTO;
import co.develhope.myApp.notification.services.MailNotificationService;
import co.develhope.myApp.users.entities.Role;
import co.develhope.myApp.users.entities.User;
import co.develhope.myApp.users.repositories.RoleRepository;
import co.develhope.myApp.users.repositories.UserRepository;
import co.develhope.myApp.users.utils.Roles;
import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class SignUpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public User signUp(@RequestBody SingUpDTO singUpDTO) throws Exception {
        return this.signUp(singUpDTO, Roles.REGISTERED);
    }

    public User signUp(@RequestBody SingUpDTO singUpDTO, String role) throws Exception {
        User userInDB = userRepository.findByEmail(singUpDTO.getEmail());
        if (userInDB != null) throw new Exception("User already exist");
        User user = new User();
        user.setName(singUpDTO.getName());
        user.setEmail(singUpDTO.getEmail());
        user.setSurname(singUpDTO.getSurname());
        user.setPassword(passwordEncoder.encode(singUpDTO.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(role);
        if (!userRole.isPresent()) throw new Exception("Cannot set role");
        roles.add(userRole.get());

        user.setRoles(roles);

        mailNotificationService.sendActivationMail(user);

        return userRepository.save(user);

    }

    public User activate(SingUpActivationDTO singUpActivationDTO)throws Exception {
        User user=userRepository.getByActivationCode(singUpActivationDTO.getActivationCode());
        if (user == null) throw new Exception("User not found");

        user.setActive(true);
        user.setActivationCode(null);
        return userRepository.save(user);
    }

}
