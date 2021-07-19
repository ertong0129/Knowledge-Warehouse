# springboot自定义路由规则

场景：同一个功能的接口有两个版本（修改逻辑后旧版本接口不能删除），现在同一个url路径，需要根据请求数据（请求头），优雅区分路由

## 方法一：

springboot已经提供自定义接口mapping方式，只要指定header头版本信息即可：

```java
@GetMapping(value = "/item", headers = "api-version=1.1")
public String item() {
	return "1.0-1";
}
```

由于产品涉及应用比较多，一方面很多接口需要版本管理，另一方面，从代码优雅度来说，对于“api-version”常量，没有抽出来统一定义



## 方法二：在接口实现方法上增加版本注解

```java
@GetMapping(value = "/item")
@ApiVersion(2.0)
public String item() {
	return "2.0-1";
}
```

mvc执行过程图：

![](.\640.jpg)





### 1.实现注解

```
/**
* 版本控制声明版本号
*/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
   /**
    * 标识版本号
    * @return
    */
   double value();
}
```



### 2.实现自定义mapping的绑定，及绑定的条件

```
/**
 * 版本号匹配器
 */
public class VersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /**
     自定义类型注解匹配，即Controller接口类匹配
     **/
    @Override protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(apiVersion);
    }
    
    /**
     自定义方法注解匹配，即具体方法级别的注解匹配
     **/
    @Override protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }
    
    private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
    }
}
```



### 3.实现配条件ApiVersionCondition：

```
/**
 * 版本号匹配筛选器
 *
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private static final String HEADER_VERSION = "api-version";
    private static final double DEFAULT_VERSION = 1.0;
    private Double apiVersion;

    public ApiVersionCondition(double apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        if(this.apiVersion==other.getApiVersion())return this;
        return other;
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        String v = request.getHeader(HEADER_VERSION);
        Double version = DEFAULT_VERSION;
        if(StringUtil.isNotBlank(v)) {
            version = Double.valueOf(v);
        }
        // 如果请求的版本号等于配置版本号， 则满足
        if(version==this.apiVersion.doubleValue())return this;

        return null;
    }

    /**
     * 如果匹配到两个都符合版本需求的（理论上不应该有）,如果有，调用期会异常，还没解决启动期检查
     */
    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return 0;
        //return Double.compare(other.getApiVersion(), this.apiVersion);
    }

    public double getApiVersion() {
        return apiVersion;
    }
    
    /**
     * 用于对比条件是否已存在这样的条件，Spring容器存放requestMapping里的mapping用到方法各注解的条件hash值合作为mapping key
     */
    @Override
    public int hashCode() {
        return this.apiVersion.hashCode();
    }
    
    /**
     * 同hasCode用处
     */
    @Override
    public boolean equals(Object obj) {
        if(obj==null || !(obj instanceof ApiVersionCondition))return false;
        ApiVersionCondition avc = (ApiVersionCondition)obj;
        return this.apiVersion.doubleValue() ==avc.getApiVersion();
    }
}
```



### 4.最后把RequestMappingHandlerMapping注册到SpringMVC容器里：

```java
@Configuration
public class VersionConfiguration implements WebMvcRegistrations{
    
    @Bean
    protected RequestMappingHandlerMapping customRequestMappingHandlerMapping() {
        return new VersionRequestMappingHandlerMapping();
    }
    
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new VersionRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        return handlerMapping;
    }
}
```





## 参考地址：

https://mp.weixin.qq.com/s/m2HnUBXagKaLQjzww1s77g

https://mp.weixin.qq.com/s/E0PcbiH1l9S3S716x7FWAA