package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.controller.validator.ImagesConstraint;
import wooteco.team.ittabi.legenoaroundhere.dto.PageRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PageableAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostImageResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.service.PostService;

@RestController
@AllArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    @PostMapping(UrlPathConstants.POSTS_PATH)
    public ResponseEntity<Void> createPost(@RequestBody PostCreateRequest postCreateRequest) {
        Long postId = postService.createPost(postCreateRequest).getId();

        return ResponseEntity
            .created(URI.create(UrlPathConstants.POSTS_PATH_WITH_SLASH + postId))
            .build();
    }

    @GetMapping(UrlPathConstants.POSTS_PATH_WITH_SLASH + "{postId}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long postId) {
        PostResponse post = postService.findPost(postId);

        return ResponseEntity
            .ok()
            .body(post);
    }

    @PostMapping(UrlPathConstants.POSTS_PATH + UrlPathConstants.IMAGES_PATH)
    public ResponseEntity<List<PostImageResponse>> uploadPostImages(
        @ImagesConstraint List<MultipartFile> images) {
        List<PostImageResponse> postImageResponses = postService.uploadPostImages(images);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postImageResponses);
    }

    @PutMapping(UrlPathConstants.POSTS_PATH_WITH_SLASH + "{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
        @RequestBody PostUpdateRequest postUpdateRequest) {
        PostResponse postResponse = postService.updatePost(postId, postUpdateRequest);

        return ResponseEntity
            .ok()
            .body(postResponse);
    }

    @GetMapping(UrlPathConstants.POSTS_PATH)
    public ResponseEntity<Page<PostWithCommentsCountResponse>> searchPosts(
        PageRequest pageRequest, PostSearchRequest postSearchRequest) {
        Page<PostWithCommentsCountResponse> posts
            = postService.searchPosts(PageableAssembler.assemble(pageRequest), postSearchRequest);

        return ResponseEntity
            .ok()
            .body(posts);
    }

    @GetMapping(UrlPathConstants.POSTS_PATH + UrlPathConstants.ME_PATH)
    public ResponseEntity<Page<PostWithCommentsCountResponse>> findMyPosts(
        PageRequest pageRequest) {
        Page<PostWithCommentsCountResponse> posts
            = postService.findMyPosts(PageableAssembler.assemble(pageRequest));

        return ResponseEntity
            .ok()
            .body(posts);
    }


    @GetMapping(UrlPathConstants.USERS_PATH_WITH_SLASH + "{userId}" + UrlPathConstants.POSTS_PATH)
    public ResponseEntity<Page<PostWithCommentsCountResponse>> findPosts(PageRequest pageRequest,
        @PathVariable Long userId) {
        Page<PostWithCommentsCountResponse> posts
            = postService.findPostsByUserId(PageableAssembler.assemble(pageRequest), userId);

        return ResponseEntity
            .ok()
            .body(posts);
    }

    @DeleteMapping(UrlPathConstants.POSTS_PATH_WITH_SLASH + "{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);

        return ResponseEntity
            .noContent()
            .build();
    }

    @PostMapping(UrlPathConstants.POSTS_PATH_WITH_SLASH + "{postId}" + UrlPathConstants.ZZANGS_PATH)
    public ResponseEntity<Void> pressPostZzang(@PathVariable Long postId) {
        postService.pressZzang(postId);

        return ResponseEntity
            .noContent()
            .build();
    }
}
