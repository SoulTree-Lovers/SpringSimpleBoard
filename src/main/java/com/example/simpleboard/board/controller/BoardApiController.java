package com.example.simpleboard.board.controller;

import com.example.simpleboard.board.db.BoardEntity;
import com.example.simpleboard.board.model.BoardDTO;
import com.example.simpleboard.board.model.BoardRequest;
import com.example.simpleboard.board.service.BoardService;
import com.example.simpleboard.crud.CRUDAbstractApiController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController extends CRUDAbstractApiController<BoardDTO, BoardEntity> {

    private final BoardService boardService;

    /*@PostMapping("")
    public BoardDTO create(
            @Valid @RequestBody BoardRequest boardRequest
    ) {
        return boardService.create(boardRequest);
    }*/

    /*@GetMapping("/id/{id}")
    public BoardDTO view(
            @PathVariable Long id
    ) {
        var entity = boardService.view(id);
        log.info("result : {}", entity);
        return boardService.view(id);
    }*/
}
