package com.ms.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ms.dao.CommentDao;
import com.ms.dao.PostDao;
import com.ms.dto.CommentDto;
import com.ms.dto.CommentListDto;
import com.ms.dto.ReCommentDto;
import com.ms.dto.ReplayListCommentDto;
import com.ms.exception.ResourceNotFoundException;
import com.ms.service.CommentService;
import com.ms.service.NotificationService;
import com.ms.service.UserService;
import com.ms.utils.Constants;
import com.sm.model.Comment;
import com.sm.model.Post;
import com.sm.model.User;


@Service
public class CommentServiceImpl implements CommentService{
	public static final String COMMENTED="commented";
	@Autowired
	UserService userService;
	
	@Autowired
	PostDao postDao;
	
	@Autowired
	CommentDao commentDao;
	

	@Autowired
	NotificationService notificationService;
	
	@Override
	public CommentListDto createNewComment(CommentDto commentDto) {
		Post post = postDao.findByPostId(commentDto.getPostId());
		if(post == null) throw new ResourceNotFoundException("Post Not found");
		
		User user = userService.findByuserId(commentDto.getUserId());
		if(user == null) throw new UsernameNotFoundException(Constants.USER_NOT_FOUND);
		
		Comment comment = new Comment();
		comment.setComment(commentDto.getMessage());
		comment.setUser(user);
		comment.setPost(post);
		Comment cmt = commentDao.save(comment);
		notificationService.createNotification(post, user, COMMENTED);
		CommentListDto dtos =new CommentListDto(); 
		dtos.setUserId(user.getUserId());
		dtos.setCommentedTime(cmt.getCreatedTime());
		dtos.setCommentedText(cmt.getComment());
		dtos.setCommentId(cmt.getCommentId());
		dtos.setUserName(user.getFullName());
		dtos.setReplays(new ArrayList<ReplayListCommentDto>());
		return dtos;
	}

	@Override
	public CommentListDto createNewReComment(ReCommentDto reCommentDto) {
		Comment comment= commentDao.findByCommentId(reCommentDto.getCommentId());
		if(comment == null) throw new ResourceNotFoundException(Constants.RESOURCE_NOT_FOUND);
		User user = userService.findByuserId(reCommentDto.getUserId());
		if(user == null) throw new UsernameNotFoundException(Constants.USER_NOT_FOUND);
		Comment reComment = new Comment();
		reComment.setUser(user);
		reComment.setPost(comment.getPost());
		reComment.setComment(reCommentDto.getCommentedText());
		reComment.setReCommentId(comment.getCommentId());
		Comment saveComment = commentDao.save(reComment);
		CommentListDto dtos =new CommentListDto(); 
		dtos.setUserId(user.getUserId());
		dtos.setCommentedTime(saveComment.getCreatedTime());
		dtos.setCommentedText(saveComment.getComment());
		dtos.setCommentId(saveComment.getCommentId());
		dtos.setUserName(user.getFullName());
		dtos.setReplays(new ArrayList<ReplayListCommentDto>());
		return dtos;
	}
//	@Override
//	public CommentListDto createNewReComment(ReCommentDto reCommentDto) {
//		Comment comment= commentDao.findByCommentId(reCommentDto.getCommentId());
//		if(comment == null) throw new ResourceNotFoundException(Constants.RESOURCE_NOT_FOUND);
//		User user = userService.findByuserId(reCommentDto.getUserId());
//		if(user == null) throw new UsernameNotFoundException(Constants.USER_NOT_FOUND);
//		ReComment reComment = new ReComment();
//		reComment.setUser(user);
//		reComment.setComment(comment);
//		reComment.setReComment(reCommentDto.getCommentedText());
//		ReComment saveComment = reCommentDao.save(reComment);
//		CommentListDto dtos =new CommentListDto(); 
//		dtos.setUserId(user.getUserId());
//		dtos.setCommentedTime(saveComment.getCreatedTime());
//		dtos.setCommentedText(saveComment.getReComment());
//		dtos.setCommentId(saveComment.getReCommentId());
//		dtos.setUserName(user.getFullName());
//		dtos.setReplays(new ArrayList<ReplayListCommentDto>());
//		return dtos;
//	}

  
	
	

}
