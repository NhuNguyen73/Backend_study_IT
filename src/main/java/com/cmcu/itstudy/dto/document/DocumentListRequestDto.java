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
public class DocumentListRequestDto {
    private String keyword;
    private String categoryId;
    private List<String> tagIds;
    private String sort;
    private Integer page;
    private Integer size;
}
