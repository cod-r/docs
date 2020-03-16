package com.example.docs.config;

import com.example.docs.filter.APIKeyAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Configuration
@EnableWebSecurity(debug = true)
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${principal.hashed.key}")
    private String principalHashedKey;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        APIKeyAuthFilter filter = new APIKeyAuthFilter();

        filter.setAuthenticationManager(authentication -> {
            String urlApiKey = (String) authentication.getPrincipal();
            if (!BCrypt.checkpw(urlApiKey, principalHashedKey)) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/documentation.html")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(filter)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
