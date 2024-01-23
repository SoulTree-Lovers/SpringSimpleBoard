package com.example.simpleboard.post.service;

import com.example.simpleboard.post.db.PostEntity;
import com.example.simpleboard.post.db.PostRepository;
import com.example.simpleboard.post.model.PostRequest;
import com.example.simpleboard.post.model.PostViewRequest;
import com.example.simpleboard.reply.service.ReplyService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ReplyService replyService;

    public PostEntity create(PostRequest postRequest) {
        var entity = PostEntity.builder()
                .boardId(1L) // << 임시 고정
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

                    // 답변글도 같이 적용
                    var replyList = replyService.findAllByPostId(it.getId());
                    it.setReplyList(replyList);

                    return it;
                })
                .orElseThrow(
                        () -> {
                            return new RuntimeException("해당 게시글이 존재하지 않습니다: " + postViewRequest.getPostId());
                        }
                );
    }

    public List<PostEntity> all() {
        return postRepository.findAll();
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
