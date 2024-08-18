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
        return httpSecurity
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/", "/users/login", "/users/register", "/users/login-error", "/error", "/api/v1/**").permitAll()
                                .requestMatchers("/workouts/all", "/workouts/history", "/workouts/**", "/users/profile").hasAnyRole(UserRoleEnum.ADMIN.name(), UserRoleEnum.CLIENT.name(), UserRoleEnum.COACH.name())
                                .requestMatchers(HttpMethod.GET, "/workout/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/clients").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/comments").permitAll()

                                .requestMatchers("/users/all").hasRole(UserRoleEnum.ADMIN.name())
                                .anyRequest().permitAll()

                ).formLogin(
                        formLogin -> {
                            formLogin
                                    .loginPage("/users/login")
                                    .usernameParameter("username")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/", true)
                                    .failureForwardUrl("/users/login-error");
                        }
                ).logout(
                        logout -> {
                            logout
                                    .logoutUrl("/users/logout")
                                    .logoutSuccessUrl("/")
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
        return new FitLaunchUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}
