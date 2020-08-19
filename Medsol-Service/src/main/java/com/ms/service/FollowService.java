package com.ms.service;

import java.util.List;

import com.ms.dto.SuggetionsDto;
import com.sm.model.Follower;
import com.sm.model.User;

public interface FollowService {

	Follower followUser(long userId, long followerId);

	boolean isFollowing(long userId, long followId);

	List<SuggetionsDto> findAllFollowing(long userId);

	List<SuggetionsDto> findAllFollowers(long userId);

//	List<User> findPeople(int pageNo);

	List<SuggetionsDto> getAllUsersNotFollowedByCurrentUser(User user,int pageNo, int pageSize);
}



