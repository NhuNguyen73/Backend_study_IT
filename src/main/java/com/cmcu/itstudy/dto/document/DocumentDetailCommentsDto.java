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
public class DocumentDetailCommentsDto {
    private Long totalComments;
    private DocumentDetailCommentItemDto pinnedComment;
    private List<DocumentDetailCommentItemDto> latestComments;
}
