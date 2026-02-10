package com.pios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                // 允许匿名访问所有 API
                .antMatchers("/api/**").permitAll()
                // 允许所有其他请求
                .anyRequest().permitAll()
            .and()
            .httpBasic().disable()
            .formLogin().disable();
        
        return http.build();
    }
}
