package com.softuni.fitlaunch.config;


import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.service.FitLaunchUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Configuration goes here
        return httpSecurity
                .authorizeHttpRequests(
                        // Define which urls are visible by which users
                        authorizeRequests -> authorizeRequests
                                // All static resources are situated in js, images, css are available for anyone
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                // Allow anyone to see the home page, the registration page and the login form
                                .requestMatchers("/", "/users/login", "/users/register", "/users/login-error", "/error","/api/v1/**").permitAll()
                                .requestMatchers("/workouts/all", "/workouts/history", "/workouts/**", "/users/profile").hasAnyRole(UserRoleEnum.ADMIN.name(), UserRoleEnum.CLIENT.name(), UserRoleEnum.COACH.name())
                                .requestMatchers(HttpMethod.GET, "/workout/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/comments").permitAll()
                                .requestMatchers("/users/all").hasRole(UserRoleEnum.ADMIN.name())
                                // all other requests are authenticated
                                .anyRequest().permitAll()

                ).formLogin(
                        formLogin -> {
                            formLogin
                                    // redirect here when we access something which is not allowed.
                                    // also this is the page where we perform login.
                                    .loginPage("/users/login")
                                    // Names of the input fields (in our case login.html)
                                    .usernameParameter("username")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/", true)
                                    .failureForwardUrl("/users/login-error");
                        }
                ).logout(
                        logout -> {
                            logout
                                    // the URL where we should POST something in order to perform the logout
                                    .logoutUrl("/users/logout")
                                    // where to go after logged out
                                    .logoutSuccessUrl("/")
                                    // invalidate the HTTP session
                                    .invalidateHttpSession(true);
                        }
                ).rememberMe(
                        rememberMe -> {
                            rememberMe
                                    .key("someUniqueKey")
                                    .tokenValiditySeconds(604800);
                        }
                ).httpBasic((Customizer.withDefaults()))
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers("/api/**");
                })
                .build();

    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // This service translates between the FitLaunch users and roles
        // to representation which spring security understands.
        return new FitLaunchUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}
