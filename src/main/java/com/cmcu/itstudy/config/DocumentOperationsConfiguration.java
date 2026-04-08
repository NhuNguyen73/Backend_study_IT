package com.cmcu.itstudy.config;

import com.cmcu.itstudy.service.contract.DocumentOperationsService;
import com.cmcu.itstudy.service.impl.DocumentOperationsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DocumentOperationsConfiguration {

    @Bean
    @Primary
    public DocumentOperationsService documentOperationsService(DocumentOperationsServiceImpl impl) {
        return impl;
    }
}
