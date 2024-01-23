package com.example.simpleboard.post.service;

import com.example.simpleboard.board.db.BoardRepository;
import com.example.simpleboard.common.Api;
import com.example.simpleboard.common.Pagination;
import com.example.simpleboard.post.db.PostEntity;
import com.example.simpleboard.post.db.PostRepository;
import com.example.simpleboard.post.model.PostRequest;
import com.example.simpleboard.post.model.PostViewRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
//    private final ReplyService replyService;
    private final BoardRepository boardRepository;


    public PostEntity create(PostRequest postRequest) {

        var boardEntity = boardRepository.findById(postRequest.getBoardId()).get();

        var entity = PostEntity.builder()
                .board(boardEntity)
                .userName(postRequest.getUserName())
                .password(postRequest.getPassword())
                .email(postRequest.getEmail())
                .status("REGISTERED")
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .postedAt(LocalDateTime.now())
                .build();

        return postRepository.save(entity);
    }

    /** 확인해야 할 사항
     * 1. 게시글이 있는가?
     * 2. 비밀번호가 맞는가?
    * */
    public PostEntity view(PostViewRequest postViewRequest) {

        return postRepository.findFirstByIdAndStatusOrderByIdDesc(postViewRequest.getPostId(), "REGISTERED")
                .map(it -> {
                    if (!it.getPassword().equals(postViewRequest.getPassword())) {
                        String format = "패스워드가 틀렸습니다. (입력받은값: %s, 실제비밀번호: %s)";
                        throw new RuntimeException(String.format(format, postViewRequest.getPassword(), it.getPassword()));
                    }

//                    // 답변글도 같이 적용
//                    var replyList = replyService.findAllByPostId(it.getId());
//                    it.setReplyList(replyList);

                    return it;
                })
                .orElseThrow(
                        () -> {
                            return new RuntimeException("해당 게시글이 존재하지 않습니다: " + postViewRequest.getPostId());
                        }
                );
    }

    public Api<List<PostEntity>> all(Pageable pageable) {
        var list = postRepository.findAll(pageable);

        var pagination = Pagination.builder()
                .page(list.getNumber()) // 현재 페이지 번호
                .size(list.getSize()) // 현재 사이즈를 몇으로 지정했나
                .currentElements(list.getNumberOfElements()) // 현재 페이지의 원소 개수
                .totalElements(list.getTotalElements()) // 전체 원소 수
                .totalPage(list.getTotalPages()) // 전체 페이지 수
                .build();

        var response = Api.<List<PostEntity>>builder()
                .body(list.toList())
                .pagination(pagination)
                .build();

        return response;
    }

    public void delete(PostViewRequest postViewRequest) {
        postRepository.findById(postViewRequest.getPostId())
                .map(it -> {
                    if (!it.getPassword().equals(postViewRequest.getPassword())) {
                        String format = "패스워드가 틀렸습니다. (입력받은값: %s, 실제비밀번호: %s)";
                        throw new RuntimeException(String.format(format, postViewRequest.getPassword(), it.getPassword()));
                    }

                    it.setStatus("UNREGISTERED");
                    postRepository.save(it);
                    return it;
                })
                .orElseThrow(
                        () -> {
                            return new RuntimeException("해당 게시글이 존재하지 않습니다: " + postViewRequest.getPostId());
                        }
                );
    }
}
