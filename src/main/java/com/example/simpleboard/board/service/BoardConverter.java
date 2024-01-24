package com.example.simpleboard.board.service;

import com.example.simpleboard.board.db.BoardEntity;
import com.example.simpleboard.board.model.BoardDTO;
import com.example.simpleboard.crud.Converter;
import com.example.simpleboard.post.service.PostConverter;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardConverter implements Converter<BoardDTO, BoardEntity> {

    private final PostConverter postConverter;

    @Override
    public BoardDTO toDto(BoardEntity boardEntity) {

        var postList = boardEntity.getPostList()
                .stream()
                .map(postConverter::toDto)
                .collect(Collectors.toList());

        return BoardDTO.builder()
                .id(boardEntity.getId())
                .boardName(boardEntity.getBoardName())
                .status(boardEntity.getStatus())
                .postList(postList)
                .build();
    }

    @Override
    public BoardEntity toEntity(BoardDTO boardDTO) {
        var postList = boardDTO.getPostList()
                .stream()
                .map(postConverter::toEntity)
                .collect(Collectors.toList());

        return BoardEntity.builder()
                .id(boardDTO.getId())
                .boardName(boardDTO.getBoardName())
                .status(boardDTO.getStatus())
                .postList(postList)
                .build();
    }
}
