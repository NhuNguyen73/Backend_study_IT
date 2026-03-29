package com.cmcu.itstudy.dto.contributor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorRegistrationRequestDto {

    private String portfolioLink;

    @NotBlank(message = "Kinh nghiệm không được để trống")
    private String experience;

    @NotEmpty(message = "Vui lòng tải lên ít nhất một chứng chỉ")
    @Valid
    private List<CertificateDto> certificates;
}
