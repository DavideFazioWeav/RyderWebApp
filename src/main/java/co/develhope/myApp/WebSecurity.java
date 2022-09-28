package co.develhope.myApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true )
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception{
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/sign/**").permitAll();
                /*.antMatchers("/admin/total-erase").hasRole(Roles.SUPER_ADMIN)
                .antMatchers("/admin/**").hasAnyRole(Roles.ADMIN,Roles.OWNER,Roles.SUPER_ADMIN)
                .antMatchers("/blog/**").hasRole(Roles.EDITOR)
                .anyRequest().authenticated();
                /**
                .antMatchers("/dev-tools/**").hasAnyAuthority("DEV_READ")
                .antMatchers("/dev-tools-bis/**").hasAuthority("DEV_CRUD")
                .antMatchers("/h2-console/**").permitAll() ;**/

        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
