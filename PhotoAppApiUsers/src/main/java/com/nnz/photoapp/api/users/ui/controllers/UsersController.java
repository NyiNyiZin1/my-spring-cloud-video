package com.nnz.photoapp.api.users.ui.controllers;


import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nnz.photoapp.api.users.service.UserService;
import com.nnz.photoapp.api.users.shared.UserDto;
import com.nnz.photoapp.api.users.ui.model.CreateUserRequestModel;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	UserService usersService;
	
	@GetMapping("/status/check")
	public String status() {
		return "Working on port :"+env.getProperty("local.server.port");
	}
	
	
	@PostMapping(consumes= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			//return xml and json format
					produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
			)
	public ResponseEntity<CreateUserRequestModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
	
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		System.err.println("?1>>>>>>>>>>>>>>>>>"+userDto.getPassword());
		UserDto createdUser = usersService.createUser(userDto);
		System.err.println("?2>>>>>>>>>>>>>>>>>"+createdUser.getPassword());
		CreateUserRequestModel returnValue = modelMapper.map(createdUser,CreateUserRequestModel.class);
		System.err.println("?3>>>>>>>>>>>>>>>>>"+createdUser.getPassword());
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}

}
