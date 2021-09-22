# spring优雅装配策略类

## 方法一：策略类上加自定义注解

自定义注解->自定义枚举>根据注解获取对象->根据注解获取对象

```
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface CustomerAnnotate {
    CustomerEnum value();
}

@CustomerAnnotate(CustomerEnum.A_CLASS)
@Component
public class AClass implement BaseInterface {
}

@Component
public class ClassFactory implements ApplicationContextAware {
    private HashMap<Enum,Class> wrapFilter = new HashMap<>();
	
		@Override
  	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
  		Map<String, Object> map = applicationContext.getBeansWithAnnotation(CustomerAnnotate.class);
      if (!CollectionUtils.isEmpty(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                CustomerAnnotate customerAnnotate = entry.getValue().getClass().getAnnotation(CustomerAnnotate.class);
                CustomerEnum customerEnum = customerAnnotate.value();
                wrapFilter.put(customerEnum, (BaseInterface) entry.getValue());
            }
        }
  }
}

```





## 方法二：抽象类bean初始化完成，自动往factory装值

```
public abstract class BaseClass implements InitializingBean {
    @Resource
    private CustomerClassFactory factory;
    
    @Override
    public void afterPropertiesSet() {
        schedulerTaskJobFactory.regitstBaseClass(this.getName(), this);
    }
}

@Component
public class CustomerClassFactory {
    private Map<String, BaseClass> baseClassMap = new ConcurrentHashMap<>();
    
    public void regitstBaseClass(String name, BaseClass baseClass) {
        baseClassMap.put(name, taskJob);
    }
}
```



## 方法三：根据类型获取对象

```
@Component
public class BaseClassFactory implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<String, BaseClass> map = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, BaseClass> beans = applicationContext.getBeansOfType(BaseClass.class);
        for (BaseClass baseClass : beans.values()) {
            map.put(baseClass.getTag(), baseClass);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```

