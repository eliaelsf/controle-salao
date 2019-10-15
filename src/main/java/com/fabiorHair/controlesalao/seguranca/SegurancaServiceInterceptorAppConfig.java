package com.fabiorHair.controlesalao.seguranca;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SegurancaServiceInterceptorAppConfig implements WebMvcConfigurer {
	 
	private SegurancaInterceptor segurancaInterceptor;
	 
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(segurancaInterceptor);
    }
}
