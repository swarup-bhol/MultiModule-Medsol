package com.ms.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.dao.GradeDao;
import com.ms.dao.ProfessionDao;
import com.ms.dao.SpecializationDao;
import com.ms.dao.SubSpecializationDao;
import com.ms.dao.UserDao;
import com.ms.exception.UserNotFound;
import com.ms.utils.ApiResponse;
import com.ms.utils.Constants;
import com.sm.model.Grade;
import com.sm.model.Profession;
import com.sm.model.Specialization;
import com.sm.model.User;

@RestController
@RequestMapping("/api/medsol/profile")
public class ProfileController {

	@Autowired
	ProfessionDao professionDao;

	@Autowired
	GradeDao gradeDao;

	@Autowired
	SpecializationDao specDao;

	@Autowired
	SubSpecializationDao subSpecDao;
	
	@Autowired
	UserDao userDao;

	@GetMapping("/profession/all") 
	public ApiResponse<List<Profession>> getAllProfession() {
		return new ApiResponse<>(200, Constants.OK, professionDao.findAll());
	}

	@GetMapping("/grade/{professionId}")
	public ApiResponse<List<Grade>> getAllGradeOnProfession(@PathVariable long professionId) {
		Profession profession = professionDao.findByProfessionId(professionId);
		return new ApiResponse<>(200, Constants.OK, gradeDao.findByProfession(profession));
	}
	
	@GetMapping("/spec/all")
	public ApiResponse<List<Specialization>> getAllSpecialization(){
		List<Specialization> specializations = specDao.findAll();
		return new ApiResponse<>(200, Constants.OK, specializations);
	}

	@GetMapping("/subSpec/{specId}")
	   public ApiResponse<List<Specialization>> getAllSubSpecialisation(@PathVariable long specId){
		Specialization specialization = specDao.findBySpecializationId(specId);
		return new ApiResponse<>(200, Constants.OK, subSpecDao.findBySpecialization(specialization));
	   }
	
	@GetMapping("/spec/{userId}")
	public ApiResponse<List<Specialization>> getSpecializationByUserId(@PathVariable long userId){
		User user = userDao.findByUserId(userId);
		if(user == null) throw new UserNotFound(Constants.USER_NOT_FOUND);
		Specialization spec = specDao.findBySpecializationId(user.getSpecializationId());
		List<Specialization> specializations = new ArrayList<>();
		specializations.add(spec);
		return new ApiResponse<>(200, Constants.OK, specializations);
		
	}

}
