package com.stormstars.stellastone.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.stormstars.stellastone.service.user.UserService;

@SuppressWarnings("SecurityConfig")
@Deprecated
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  UserService userDetailsService;

  @Autowired
  SecurityHandler handler;

  /**
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/", "/index", "/css/**", "/js/**", "/img/**",
            "/img/icon/**", "/signIn", "Build/**", "/Build", "/Build/**", "/Build/**/**", "/Build/**/**/**",
            "/h2-console/**", "/modeTest", "/forgot_password", "/static/**", "/process-signIn",
            "/process-logIn", "/logIn", "/logIn/**", "/forgot_password",
            "/forgot_password_form", "/reset_password", "/reset_password_form")
        .permitAll()
        .antMatchers("/home", "/home/**", "/modeRealiste/**", "/modeAventure/**", "/modeTest").authenticated()
        .and()
        .formLogin()
        .loginPage("/logIn")
        .usernameParameter("email")
        .loginProcessingUrl("/process-logIn")
        .failureUrl("/logIn?error=true")
        .defaultSuccessUrl("/home")
        .permitAll();
    http.formLogin()
        .and().logout()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID");
    // * Custom success handler
    http.formLogin()
        .successHandler(handler);
    // ! Allow access to the h2-console
    http.authorizeRequests().antMatchers("/h2-console", "/h2-console/**").permitAll();
    http.csrf().ignoringAntMatchers("/h2-console", "/h2-console/**", "/modeTest/**", "/home/**");
    http.cors().and().csrf().disable();
    http.headers().frameOptions().sameOrigin();
  }

  /**
   * @return AuthenticationManager
   * @throws Exception
   */
  @Bean
  public AuthenticationManager authManager() throws Exception {
    return this.authenticationManager();
  }

  /**
   * @param auth : AuthenticationManagerBuilder
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(userDetailsService.encoder);
  }
}
