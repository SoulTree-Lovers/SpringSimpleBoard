package com.example.simpleboard.post.controller;

import com.example.simpleboard.common.Api;
import com.example.simpleboard.crud.CRUDAbstractApiController;
import com.example.simpleboard.post.db.PostEntity;
import com.example.simpleboard.post.model.PostDTO;
import com.example.simpleboard.post.model.PostRequest;
import com.example.simpleboard.post.model.PostViewRequest;
import com.example.simpleboard.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostApiController extends CRUDAbstractApiController<PostDTO, PostEntity> {

    private final PostService postService;

//    @PostMapping("")
//    public PostEntity create(
//            @Valid @RequestBody PostRequest postRequest
//    ) {
//        return postService.create(postRequest);
//    }

    @PostMapping("/view")
    public PostEntity view(
            @Valid @RequestBody PostViewRequest postViewRequest
    ) {
        return postService.view(postViewRequest);
    }


//    @GetMapping("/all")
//    public Api<List<PostDTO>> list(
//            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC)
//            Pageable pageable
//    ) {
//        return postService.list(pageable);
//    }


//    @PostMapping("/delete")
//    public void delete(
//            @Valid @RequestBody PostViewRequest postViewRequest
//    ) {
//        postService.delete(postViewRequest);
//    }
}
