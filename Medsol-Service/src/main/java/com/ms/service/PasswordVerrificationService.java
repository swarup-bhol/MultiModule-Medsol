package com.ms.service;

import com.ms.dto.PasswordDto;
import com.sm.model.User;

public interface PasswordVerrificationService {

	public User verifyCode(String email, String code);

	public User changePassword(User user, PasswordDto passwordDto);

	public boolean sendMail(User userDetails);
}
