package com.ms.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ms.dto.LoginUser;
import com.ms.dto.PasswordDto;
import com.ms.dto.Profile;
import com.ms.dto.ProfileDetailsDto;
import com.ms.dto.SuggetionsDto;
import com.ms.dto.UpdateProfileDto;
import com.ms.dto.UserDto;
import com.sm.model.User;


public interface UserService {

	User save(UserDto user);

	User findOne(String username);

	boolean checkUser(UserDto user);

	public User findByuserId(long userId);
	
	public boolean loginUser(LoginUser loginUser);
	
	User uploadProfilePic(MultipartFile file, User user) throws IOException;

	ProfileDetailsDto getProfileDetails(User user);

	ProfileDetailsDto updateProfile(long userId, UpdateProfileDto profileDto);

	User updatePassWord(User user, PasswordDto passwordDto);

	User createProfile(Profile profile, User user);

	List<SuggetionsDto> searchUser(String name,long userId);

	User uploadDocument(MultipartFile file, User user) throws IOException;
	
	
}
