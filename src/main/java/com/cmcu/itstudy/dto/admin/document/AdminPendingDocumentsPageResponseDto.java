package com.cmcu.itstudy.dto.admin.document;

import com.cmcu.itstudy.dto.document.DocumentCardDto;
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
public class AdminPendingDocumentsPageResponseDto {

    private List<DocumentCardDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
