package com.ms.service;

import com.ms.dto.CommentDto;
import com.ms.dto.CommentListDto;
import com.ms.dto.ReCommentDto;

public interface CommentService {
	CommentListDto createNewComment(CommentDto commentDto);

	CommentListDto createNewReComment(ReCommentDto reCommentDto);

//	Comment createNewComment(User user, Post post, String comments);

}
