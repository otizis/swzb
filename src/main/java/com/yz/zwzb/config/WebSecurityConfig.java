package com.yz.zwzb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning
        .InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().antMatchers("/index").permitAll().anyRequest().authenticated().and()

                .formLogin().loginPage("/login").permitAll().and()

                .logout().permitAll().and()

                .sessionManagement().maximumSessions(1).expiredUrl("/expired").maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry())

        ;
    }

    // Work around https://jira.spring.io/browse/SEC-2855
    @Bean
    public SessionRegistry sessionRegistry()
    {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authentication = auth
                .inMemoryAuthentication();
        for (int i = 0; i < 50; i++)
        {
            authentication.withUser("test" + i).password("111111").roles("USER");
        }

    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher()
    {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
}