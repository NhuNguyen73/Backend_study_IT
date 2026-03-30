package com.cmcu.itstudy.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminContributorCertificateDto {
    private String url;
    private String certificateName;
}
