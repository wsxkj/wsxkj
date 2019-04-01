package com.vue.util;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;  
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2; 

@Configuration  
@EnableWebMvc  
@EnableSwagger2
//@ComponentScan(basePackages = "com.vue")
public class SwaggerConfig {
	 @Bean
    public Docket api() {
		 System.out.println("----调用swaggerconfig----");
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vue.controller"))
                .build()
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("")
                .description("")
                .version("1.0.0")
                .termsOfServiceUrl("")
                .license("")
                .licenseUrl("")
                .build();
    }

//	private SpringSwaggerConfig springSwaggerConfig;  
//	  
//    
//	  
//    
//    @Autowired  
//    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {  
//        this.springSwaggerConfig = springSwaggerConfig;  
//    }  
  
    /** 
     * 链式编程 来定制API样式 
     * 后续会加上分组信息 
     * 
     * @return 
     */  
//    @Bean  
//    public SwaggerSpringMvcPlugin customImplementation() {  //   .*?
//    	return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*?");  
//    }  
//  
//    private ApiInfo apiInfo() {
//    	 ApiInfo apiInfo = new ApiInfo(  
//                 "蓝智慧接口文档",  
//                 "这里是所有的接口，里边含有说明，请自行测试",  
//                 "My Apps API terms of service",  
//                 "My Apps API Contact Email",  
//                 "My Apps API Licence Type",  
//                 "My Apps API License URL");  
//         return apiInfo;  
//    }  
	
}
