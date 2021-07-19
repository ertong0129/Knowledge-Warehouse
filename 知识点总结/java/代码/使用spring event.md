# 使用spring内置的event，解耦代码块

默认情况下，事件在代码中的执行是同步的，如果需要异步，可以配合@Async使用



定义一个event类

```
@Getter
public class DemoEvent extends ApplicationEvent {

    private String exampleString;

    public DemoEvent(Object source, String exampleString) {
        super(source);
        this.exampleString = exampleString;
    }
}

```



定义一个listener

```
@Component
@Order(1)
public class DemoListener implements ApplicationListener<DemoEvent> {

    @SneakyThrows
    @Override
    @Async
    public void onApplicationEvent(DemoEvent demoEvent) {
        System.out.println("Run："+ DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")
            + ":" + demoEvent.getExampleString());
        Thread.sleep(1000);
    }

}
```



发布事件

```
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void test() {
        applicationEventPublisher.publishEvent(new DemoEvent(this, "测试"));
    }
```

