package com.ms.service;


import java.io.IOException;
import java.util.List;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ms.dto.PostDto;
import com.sm.model.Post;
import com.sm.model.User;

@Service
public interface PostService  {
	public Post uploadMedia(MultipartFile file, User user,String content, String specialization) throws IOException;
	public Post createPost(User user,String content, String specialization);
	public Post getPostById(long postId);
	public Post updateMedia(Post post, String content, MultipartFile file) throws IOException;
	public List<PostDto> getNewsFeedPosts(User user, int pageNo);
	public List<PostDto> getUploadedPost(User user, int pageNo);
	public PostDto findByPostId(Post post);
//	public ResponseEntity<byte[]> prepareContent(Post post, String httpRangeList);
	public List<PostDto> getPostSpecType(List<Long> specList, User user, int pageNo);
	public ResponseEntity<ResourceRegion> getVideoRegion(Post post, String rangeHeader) throws IOException;
	
}
