package com.nnz.photoapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.client.http.HttpRequest;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;

import com.nnz.photoapp.api.users.service.UserService;
import com.nnz.photoapp.api.users.shared.UserDto;
import com.nnz.photoapp.api.users.ui.model.LoginRequestModel;

import ch.qos.logback.core.filter.Filter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private UserService usersService;
	private Environment environment;
	
	
	public AuthenticationFilter(UserService usersService, Environment environment,AuthenticationManager authenticationManager) {
		this.usersService = usersService;
		this.environment = environment;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
  
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginRequestModel.class);
            
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
	protected void successfulAuthentication(
			HttpServletRequest req,
			HttpServletResponse res,
			FilterChain chain,
			Authentication auth) throws IOException,ServletException{
		//I don't know because get to user name for user detail
		String userName = ((User)auth.getPrincipal()).getUsername();
		UserDto userDetails= usersService.getUserDetailsByEmail(userName);
		
		 String token = Jwts.builder()
	                .setSubject(userDetails.getUserId())
	                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
	                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret") )
	                .compact();
		
		res.addHeader("token", token);
		res.addHeader("userId", userDetails.getUserId());
				
	}
     

}
