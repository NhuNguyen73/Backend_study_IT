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
public class DocumentDetailResponseDto {
    private DocumentDetailInfoDto documentInfo;
    private DocumentDetailStatsDto stats;
    private DocumentPrimaryFileDto file;
    private DocumentDetailCommentsDto comments;
    private List<DocumentDetailQuizDto> quizzes;
    private List<DocumentRelatedDocumentDto> relatedDocuments;
}
