package com.cmcu.itstudy.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentThreadPageResponseDto {

    private List<CommentResponse> content;
    private long totalComment;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
