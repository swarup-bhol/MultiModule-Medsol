package com.ms.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ms.dao.UserDao;
import com.ms.dto.PasswordDto;
import com.ms.exception.UserNotFound;
import com.ms.service.PasswordVerrificationService;
import com.ms.utils.Constants;
import com.ms.utils.EmailService;
import com.sm.model.User;



@Service
public class PasswordVerrificationServiceImpl implements PasswordVerrificationService{
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Autowired
	EmailService emailService;

	@Override
	public User verifyCode(String email,String code) {
		User user = userDao.findByUserEmail(email);
		if(user == null) throw new UserNotFound(Constants.USER_NOT_FOUND);
		String emailVerifficationCode = user.getEmailVerifficationCode();
		if(emailVerifficationCode.equals(code)) {
			return user;
		}
		return null;
		
	}

	@Override
	public User changePassword(User user, PasswordDto passwordDto) {
		user.setUserPassword(bcryptEncoder.encode(passwordDto.getPassword()));
		return userDao.save(user);
	}

	@Override
	public boolean sendMail(User userDetails) {
		Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    boolean sentMail = emailService.sentMail(userDetails.getUserEmail(), String.format("%06d", number));
	    if(sentMail) {
	    	 userDetails.setEmailVerifficationCode(String.format("%06d", number));
	    	 userDao.save(userDetails);
	    	 return true;
	    }
		return false;
	}
}
