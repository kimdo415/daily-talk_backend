package com.karainc.dailytalk.domain.board.repository;

import com.karainc.dailytalk.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAll();

    Board findByBoardIdx(long boardIdx);

}
