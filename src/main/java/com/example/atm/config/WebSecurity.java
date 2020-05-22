package com.example.atm.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LogManager.getLogger(WebSecurity.class);
    private final UserDetailsService userDetailsService;

    public WebSecurity(@Qualifier("customUserDetailService")
                               UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) {
        try {
            builder.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
        } catch (Exception e) {
            logger.error("WebSecurity -> configureglobal()" + e);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/register", "/login").permitAll()
                .antMatchers("/atm/push-cash-to-atm", "/atm/get-atms").hasRole("ADMIN")
                .antMatchers("/atm/withdraw-money",
                        "/atm/deposit-money",
                        "/atm/transfer-money").hasRole("USER")
                .anyRequest().authenticated()
                .and().httpBasic();

        http.headers().frameOptions().disable();

        http.cors();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
