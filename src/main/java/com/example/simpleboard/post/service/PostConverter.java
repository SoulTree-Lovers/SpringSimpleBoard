package com.example.simpleboard.post.service;

import com.example.simpleboard.board.db.BoardRepository;
import com.example.simpleboard.crud.Converter;
import com.example.simpleboard.post.db.PostEntity;
import com.example.simpleboard.post.model.PostDTO;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostConverter implements Converter<PostDTO, PostEntity> {

    private final BoardRepository boardRepository;

    @Override
    public PostDTO toDto(PostEntity postEntity) {
        return PostDTO.builder()
                .id(postEntity.getId())
                .userName(postEntity.getUserName())
                .status(postEntity.getStatus())
                .email(postEntity.getEmail())
                .password(postEntity.getPassword())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .postedAt(LocalDateTime.now())
                .boardId(postEntity.getBoard().getId())
                .build();
    }

    @Override
    public PostEntity toEntity(PostDTO postDTO) {
        var boardEntity = boardRepository.findById(postDTO.getBoardId());

        return PostEntity.builder()
                .id(postDTO.getId())
                .userName(postDTO.getUserName())
                .password(postDTO.getPassword())
                .email(postDTO.getEmail())
                .status(postDTO.getStatus())
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .postedAt(postDTO.getPostedAt())
                .board(boardEntity.get())
                .build();

    }
}
