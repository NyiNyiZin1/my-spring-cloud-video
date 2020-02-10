package com.nnz.photoapp.api.users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import com.nnz.photoapp.api.users.data.UserEntity;
import com.nnz.photoapp.api.users.data.UserRepository;
import com.nnz.photoapp.api.users.shared.UserDto;

@Service
public class UserServiceImpl implements UserService{
	
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository usersRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
		// TODO Auto-generated constructor stub
		this.userRepository=usersRepository;
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		// TODO Auto-generated method stub
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		userEntity.setEncryptedPassword("test");
		
		UserEntity userEntity1 = userRepository.save(userEntity);
		UserDto returnValue = modelMapper.map(userEntity1, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity userEntity=userRepository.findByEmail(username);
		
		if(userEntity == null) throw new UsernameNotFoundException(username);
		
		return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),true,true,true,true,new ArrayList<>());
	}

}
