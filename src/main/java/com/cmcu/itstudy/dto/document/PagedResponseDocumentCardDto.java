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
public class PagedResponseDocumentCardDto {
    private List<DocumentCardResponseDto> content;
    private Integer page;
    private Integer size;
    private Integer totalElements;
    private Integer totalPages;
}
