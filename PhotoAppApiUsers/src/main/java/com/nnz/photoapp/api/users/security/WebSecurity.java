package com.nnz.photoapp.api.users.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	private Environment env;
	
	@Autowired
	public WebSecurity(Environment env) {
		this.env=env;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		//http.authorizeRequests().antMatchers("/users/**").permitAll();
		http.authorizeRequests().antMatchers("/**").hasIpAddress(env.getProperty("gateway.ip"))
		.and().addFilter(getAuthenticationFilter());
		http.headers().frameOptions().disable();
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		// TODO Auto-generated method stub
		AuthenticationFilter authenticationFilter=new AuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManager());
		return authenticationFilter;
	}

}
