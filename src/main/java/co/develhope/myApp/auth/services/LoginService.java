package co.develhope.myApp.auth.services;

import co.develhope.myApp.auth.entities.LoginDTO;
import co.develhope.myApp.auth.entities.LoginRTO;
import co.develhope.myApp.users.entities.User;
import co.develhope.myApp.users.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LoginService {

    // stringa SECRET(andrebbe nello YAML) mi permette si "firmare" il JWT
    // https://www.uuidgenerator.net/ per generarlo
    public static final String JWT_SECRET = "b3f4db21-599c-41db-b531-77a951a3a67e";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * manages login system
     * @param loginDTO an entity that contains email and password
     * @return a LoginRTO that contains user and JWT Token
     */
    public LoginRTO login(LoginDTO loginDTO){
        if(loginDTO == null) return null;
        User userFromDB = userRepository.findByEmail(loginDTO.getEmail());//cerca userFromDB per email
        if(userFromDB == null || !userFromDB.isActive()) return null;// ritorna nullo se userFromDB è nullo o inattivo

        // chiama il metodo canUserLogin, se restituisce true l'utente può loggarsi
        boolean canLogin = this.canUserLogin(userFromDB, loginDTO.getPassword());
        if(!canLogin) return null; // se restituisce false l'utente non può loggarsi

        String JWT = generateJWT(userFromDB);

        //userFromDB.setJwtCreatedOn(LocalDateTime.now());
        //userRepository.save(userFromDB);

        //userFromDB.setPassword(null);
        LoginRTO out = new LoginRTO(); //istanzio un nuovo LoginRto che si chiama out
        out.setJWT(JWT); //setto il token JWT
        out.setUser(userFromDB); //setto l'utente

        return out; //restituisce un LoginRTO
    }

    /**
     * check if a user can log in
     * is an auxiliary function for login method
     * @param user represent the user
     * @param password is the user password
     * @return true if user can log in, otherwise returns false
     */
    public boolean canUserLogin(User user, String password){
        return passwordEncoder.matches(password, user.getPassword()); //(password in chiaro, password criptata)
    }

    /**
     * <a href="https://www.baeldung.com/java-date-to-localdate-and-localdatetime">...</a>
     * @param dateToConvert
     * @return
     */
    static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    /**
     * Create a JWT string from user data and add a signature via the JWT_SECRET
     * static method
     * @param user is the user
     * @return a string which is JWT Token
     */
    public static String getJWT(User user){
        Date expiresAt = convertToDateViaInstant(LocalDateTime.now().plusDays(15));

        //https://mkyong.com/java8/java-8-how-to-convert-a-stream-to-array/
        //prendiamo tutti i ruoli dell'utente e li convertiamo in array e li integriamo nel JWT Token
        String[] roles = user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new);
        //creiamo la nostra stringa JWT(JSON Web Tokens) che verrà poi assegnata all'utente
        return JWT.create()
                .withIssuer("develhope-demo")
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .withClaim("roles",String.join(",",roles)) //quì inseriamo i roles
                .withClaim("id", user.getId())
                .sign(Algorithm.HMAC512(JWT_SECRET));
    }

    /**
     *
     * @param user
     * @return
     */
    public String generateJWT(User user) {
        String JWT = getJWT(user);

        user.setJwtCreatedOn(LocalDateTime.now());
        userRepository.save(user);

        return JWT;
    }
}
