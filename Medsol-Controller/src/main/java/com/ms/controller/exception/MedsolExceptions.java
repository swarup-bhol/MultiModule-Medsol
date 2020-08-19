package com.ms.controller.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ms.exception.InvalidUserNamePasswordException;
import com.ms.exception.UnautorizedAccess;
import com.ms.exception.UserNotFound;
import com.ms.utils.ApiResponse;
import com.ms.utils.Constants;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class MedsolExceptions extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidUserNamePasswordException.class)
	public final ResponseEntity<Object> handleInvalidUserNameOrPAsswordException(InvalidUserNamePasswordException ex,
			WebRequest request) {
		ApiResponse<Object> error = new ApiResponse<>(400, Constants.INVALID_CREDENTIALS, ex.getMessage());
		return new ResponseEntity<Object>(error, HttpStatus.OK);
	}
	@ExceptionHandler(UnautorizedAccess.class)
	public final ResponseEntity<Object> handleUnauthorizedAccessException(UnautorizedAccess ex,
			WebRequest request) {
		ApiResponse<Object> error = new ApiResponse<>(401, Constants.UNAUTHORIZED, Constants.UNAUTHORIZED);
		return new ResponseEntity<Object>(error, HttpStatus.OK);
	}
	@ExceptionHandler(UserNotFound.class)
	public final ResponseEntity<Object> handleUserNotFoundException(UserNotFound ex,
			WebRequest request) {
		ApiResponse<Object> error = new ApiResponse<>(404, Constants.USER_NOT_FOUND, Constants.USER_NOT_FOUND);
		return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
	}

	public final ResponseEntity<Object> handelJwtTokenExpireException(ExpiredJwtException ex, WebRequest webRequest){
		ApiResponse<Object> error = new ApiResponse<>(404, Constants.TOKEN_EXPIRE, Constants.TOKEN_EXPIRE);
		return new ResponseEntity<Object>(error, HttpStatus.UNAUTHORIZED);
	}
	
	// Parent Exceptions 
//	@ExceptionHandler(Exception.class)
//	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//		return new ResponseEntity<Object>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
} 
