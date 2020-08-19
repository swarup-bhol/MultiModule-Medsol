package com.ms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ms.dao.PostDao;
import com.ms.dao.UserDao;
import com.ms.dto.PostDto;
import com.ms.exception.PostNotFoundException;
import com.ms.exception.ResourceNotFoundException;
import com.ms.exception.UserNotFound;
import com.ms.service.PostService;
import com.ms.service.UserService;
import com.ms.utils.ApiResponse;
import com.ms.utils.Constants;
import com.sm.model.Post;
import com.sm.model.User;


@RestController
@RequestMapping("/api/medsol/posts")
public class PostController {

	@Autowired
	UserService userService;

	@Autowired
	PostService postService;

	@Autowired
	PostDao postDao;

	@Autowired
	UserDao userDao;

	// Upload post only text or file like image or video
	@PostMapping("/{userId}")
	public ApiResponse<Post> uploadPost(@PathVariable Long userId, @RequestParam("content") String content,
			@RequestParam(required = false) MultipartFile file, @RequestParam("type") String type) throws IOException {
		if (content.isEmpty())
			return new ApiResponse<>(400, Constants.BAD_REQUEST, content);
		User user = userService.findByuserId(userId);

		if (user == null)
			throw new UserNotFound(Constants.USER_NOT_FOUND);
		if (file == null) { // Condition for only uploading text Post
			Post post = postService.createPost(user, content, type);
			return new ApiResponse<>(200, Constants.CREATED, post);
		}
		Post post = postService.uploadMedia(file, user, content, type);
		return new ApiResponse<>(200, Constants.CREATED, post);
	}

	// Update post
	@PutMapping("post/{postId}")
	public ApiResponse<Post> updateUploadedPost(@PathVariable Long postId, @RequestParam("content") String content,
			@RequestParam(required = false) MultipartFile file) throws IOException {
		if (content.isEmpty())
			return new ApiResponse<>(400, Constants.BAD_REQUEST, content);
		Post post = postService.getPostById(postId);
		if (post == null)
			return new ApiResponse<Post>(204, Constants.FILE_NOT_FOUND, post);
		if (file == null) {
			post.setPostContent(content);
			return new ApiResponse<>(200, Constants.CREATED, postDao.save(post));
		}
		Post updatePost = postService.updateMedia(post, content, file);
		return new ApiResponse<>(200, Constants.CREATED, updatePost);
	}

	// Get each media file in format of jpeg or png
	@GetMapping(value = "/img/{postId}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public @ResponseBody byte[] getClassPath(@PathVariable Long postId) throws IOException {
		Post post = postService.getPostById(postId);
		if (post == null || post.getPostImgPath() == null)
			return null;
		Path uploadPath = Paths.get(post.getPostImgPath());
		byte[] test = Files.readAllBytes(uploadPath);
		return test;
	}

	@GetMapping(value = "/video/{postId}",produces = "application/octet-stream")
	public ResponseEntity<ResourceRegion> getVideo(HttpServletResponse resp, @PathVariable long postId,
			@RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
		Post post = postDao.findByPostId(postId);
		if (post == null || post.getPostVideoPath() == null)
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		resp.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		ResponseEntity<ResourceRegion> stream = postService.getVideoRegion(post, rangeHeader);
		return stream;
	}

	// Delete post by postId
	@DeleteMapping("/{postId}")
	public ApiResponse<Post> deletePost(@PathVariable long postId) throws PostNotFoundException {
		Post post = postDao.findByPostId(postId);
		if (post == null)
			throw new PostNotFoundException(Constants.RESOURCE_NOT_FOUND);
		post.setRecordStatus(false);
		postDao.save(post);
		return new ApiResponse<>(200, Constants.DELETED, postId);
	}

	// Get all post By postId
	@GetMapping("/{userId}/post/{pageNo}")
	public ApiResponse<List<PostDto>> getAllPostByUser(@PathVariable long userId, @PathVariable int pageNo) {
		User user = userService.findByuserId(userId);
		if (user == null)
			throw new UserNotFound(Constants.USER_NOT_FOUND);
		return new ApiResponse<>(200, Constants.OK, postService.getUploadedPost(user, pageNo));

	}

	@GetMapping("/{userId}/feeds/{pageNo}")
	public ApiResponse<List<PostDto>> getAllPost(@PathVariable long userId, @PathVariable int pageNo) {
		User user = userDao.findByUserId(userId);
		if (user == null)
			throw new UserNotFound(Constants.USER_NOT_FOUND);
		List<PostDto> allPost = postService.getNewsFeedPosts(user, pageNo);
		return new ApiResponse<>(200, Constants.OK, allPost);
	}

	@GetMapping("feeds/{userId}/bySpec/{pageNo}")
	public ApiResponse<PostDto> getPostBySpecifications(@RequestParam List<Long> specList, @PathVariable long userId,
			@PathVariable int pageNo) {
		User user = userDao.findByUserId(userId);
		List<PostDto> postDtos = postService.getPostSpecType(specList, user, pageNo);
		return new ApiResponse<PostDto>(200, Constants.OK, postDtos);
	}

	@GetMapping("/{postId}")
	public ApiResponse<PostDto> getPostByPostId(@PathVariable long postId) {
		Post post = postDao.findByPostId(postId);
		if (post == null)
			throw new ResourceNotFoundException(Constants.FILE_NOT_FOUND);
		PostDto postDto = postService.findByPostId(post);
		return new ApiResponse<>(200, Constants.OK, postDto);
	}

}
