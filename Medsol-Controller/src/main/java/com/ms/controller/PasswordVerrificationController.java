package com.ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.dao.UserDao;
import com.ms.dto.PasswordDto;
import com.ms.exception.UserNotFound;
import com.ms.service.PasswordVerrificationService;
import com.ms.utils.ApiResponse;
import com.ms.utils.Constants;
import com.ms.utils.EmailService;
import com.sm.model.User;


@RestController
@RequestMapping("api/medsol/password")
public class PasswordVerrificationController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
    EmailService emailService;
	
	@Autowired
	PasswordVerrificationService passwordService;

	@GetMapping("/{email}")
	public ApiResponse<String> checkEmailOrPassword(@PathVariable String email) {
		
		User userDetails = userDao.findByUserEmail(email);
		if(userDetails == null)
			return new ApiResponse<>(200, Constants.OK, Constants.USER_NOT_FOUND);
		boolean messageStatus = passwordService.sendMail(userDetails);
		return new ApiResponse<>(200, Constants.OK, messageStatus);
		
	}
	
	@GetMapping("/verify/{email}/{code}")
	public ApiResponse<User> verifyPassword(@PathVariable String email ,@PathVariable String code){
		User user = passwordService.verifyCode(email, code);
		if(user == null)
			return new ApiResponse<>(200, Constants.BAD_REQUEST, code);
		return new ApiResponse<>(200, Constants.OK, user);
	}
	
	@PutMapping("/{userId}")
	public ApiResponse<User> updatePassword(@RequestBody PasswordDto passwordDto, @PathVariable long userId){
		User user = userDao.findByUserId(userId);
		if(user == null) throw new UserNotFound(Constants.USER_NOT_FOUND);
		if(! passwordDto.getPassword().equals(passwordDto.getCnfPassword()))
			return new ApiResponse<>(402, Constants.BAD_REQUEST, passwordDto);
		User updatedUser = passwordService.changePassword(user,passwordDto);
	    return new ApiResponse<>(200, Constants.OK, updatedUser);
	}
}
