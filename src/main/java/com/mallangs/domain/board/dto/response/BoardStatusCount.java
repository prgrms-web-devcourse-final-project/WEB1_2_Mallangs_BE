package com.mallangs.domain.board.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardStatusCount {
    private long total;
    private long published;
    private long hidden;
    private long draft;
}
