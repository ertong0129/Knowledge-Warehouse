# springboot配置静态资源地址

## 第一种：在配置文件中配置

```
#静态资源访问路径
spring.mvc.static-path-pattern=/**
#静态资源映射路径
spring.resources.static-locations=classpath:/
```



## 第二种：在代码中进行设置

```
package com.isyscore.flow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticFileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/templates/");
        registry.addResourceHandler("/test/**").addResourceLocations("file:F:/知识点总结/java/代码/");
        //registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ baseUploadPath);
    }
}

```