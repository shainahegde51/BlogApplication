package com.springboot.blog.config;

import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity


@SecurityScheme(name = "Bearer Authentication",
type= SecuritySchemeType.HTTP,
bearerFormat = "JWT",
scheme = "bearer"
)


public class SecurityConfig {
private JwtAuthenticationEntryPoint authenticationEntryPoint;

private JwtAuthenticationFilter authenticationFilter;
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService,JwtAuthenticationEntryPoint authenticationEntryPoint,JwtAuthenticationFilter authenticationFilter){

        this.userDetailsService=userDetailsService;
        this.authenticationEntryPoint=authenticationEntryPoint;
        this.authenticationFilter=authenticationFilter;
    }



    @Bean
    //This will automatically get user details service and password encoder and does the authentication
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return  configuration.getAuthenticationManager();
    }


    @Bean
    public static PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //here we are going to configure security filter chain such way that we only enable basic authentication
        http.csrf((csrf) -> csrf.disable()).authorizeHttpRequests((authorize) ->
//                authorize.anyRequest().authenticated())
                authorize.requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()).httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    //commenting it since we are doing database authentication we dont need this
//    @Bean
    //here we will create few user and store it in inmemory object
    //authentication manager will use this userdetails service to get username and password
//    public UserDetailsService  userDetailsService(){
//        UserDetails shaina= User.builder().username("shaina")
//                .password(passwordEncoder().encode("shaina"))
//                .roles("USER")
//                .build();
//        UserDetails admin=User.builder().username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        return  new InMemoryUserDetailsManager(shaina,admin);
//    }

}
