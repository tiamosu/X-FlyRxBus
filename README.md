# X-FlyRxBus
## 本项目fork自([Blankj/RxBus](https://github.com/Blankj/RxBus))，在此基础上进行了相关优化及实现项目中需求。

### fly-rxbus
[ ![Download](https://api.bintray.com/packages/weixia/maven/x-flyrxbus/images/download.svg) ](https://bintray.com/weixia/maven/x-flyrxbus/_latestVersion)
```groovy
implementation 'me.xia:x-flyrxbus:1.0.5'
```

## How to use

### 非粘性事件
1. 注册事件
```java
  public class YourActivity extends Activity {
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);

          // 注册 String 类型事件
          RxBusManager.subscribe(this, new RxBus.Callback<String>() {
              @Override
              public void onEvent(String s) {
                  Log.e("eventTag", s);
              }
          });

          // 注册带 tag 为 "my tag" 的 String 类型事件
          RxBusManager.subscribe(this, "my tag", new RxBus.Callback<String>() {
              @Override
              public void onEvent(String s) {
                  Log.e("eventTag", s);
              }
          });

          //注册多个带 tag 的类型事件
          RxBusManager.subscribeWithTags(this, new RxBus.Callback<RxBusMessage>() {
                      @Override
                      public void onEvent(String tag, RxBusMessage rxBusMessage) {
                          Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj);
                          if (rxBusMessage.mObj instanceof TestEvent) {
                              final TestEvent testEvent = (TestEvent) rxBusMessage.mObj;
                              Log.e("weixi", "onEvent: " + testEvent.mString);
                          }
                      }
                  }, "myTag1", "myTag2", "myTag3");
      }

      @Override
      protected void onDestroy() {
          super.onDestroy();
          // 注销
          RxBusManager.unregister(this);
      }
  }
```

2. 发送事件
```java
  // 发送 String 类型事件
  RxBusManager.post("without tag");

  // 发送带 tag 为 "my tag" 的 String 类型事件
  RxBusManager.post("with tag", "my tag");

  //发送带多个 tag 的类型事件
  RxBusManager.post(new RxBusMessage("1"), "myTag1");
  RxBusManager.post(new RxBusMessage(0.001), "myTag2");
  RxBusManager.post(new RxBusMessage(new TestEvent("hello")), "myTag3");
```

### 粘性事件（也就是先发送事件，在之后注册的时候便会收到之前发送的事件）
1. 注册事件
```java
  public class YourActivity extends Activity {
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);

          // 注册 String 类型事件
          RxBusManager.subscribeSticky(this, new RxBus.Callback<String>() {
              @Override
              public void onEvent(String s) {
                  Log.e("eventTag", s);
              }
          });

          // 注册带 tag 为 "my tag" 的 String 类型事件
          RxBusManager.subscribeSticky(this, "my tag", new RxBus.Callback<String>() {
              @Override
              public void onEvent(String s) {
                  Log.e("eventTag", s);
              }
          });

          //注册多个带 tag 的类型事件
          RxBusManager.subscribeStickyWithTags(this, new RxBus.Callback<RxBusMessage>() {
                      @Override
                      public void onEvent(String tag, RxBusMessage rxBusMessage) {
                          Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj);
                          if (rxBusMessage.mObj instanceof TestEvent) {
                              final TestEvent testEvent = (TestEvent) rxBusMessage.mObj;
                              Log.e("weixi", "onEvent: " + testEvent.mString);
                          }
                      }
                  }, "myTag4", "myTag5", "myTag6");
      }

      @Override
      protected void onDestroy() {
          super.onDestroy();
          // 注销
          RxBusManager.unregister(this);
      }
  }
```

2. 发送事件
```java
  // 发送 String 类型的粘性事件
  RxBusManager.postSticky("without tag");

  // 发送带 tag 为 "my tag" 的 String 类型的粘性事件
  RxBusManager.postSticky("with tag", "my tag");

  //发送带多个 tag 的类型的粘性事件
  RxBusManager.post(new RxBusMessage("1"), "myTag4");
  RxBusManager.post(new RxBusMessage(0.001), "myTag5");
  RxBusManager.post(new RxBusMessage(new TestEvent("hello")), "myTag6");
```

## Nice wrap
```java
public final class RxBusManager {

    public static void post(final Object event) {
        RxBus.post(event);
    }

    public static void post(final Object event, final String tag) {
        RxBus.post(event, tag);
    }

    public static void postSticky(final Object event) {
        RxBus.postSticky(event);
    }

    public static void postSticky(final Object event, final String tag) {
        RxBus.postSticky(event, tag);
    }

    public static void removeSticky(final Object event) {
        RxBus.removeSticky(event);
    }

    public static void removeSticky(final Object event,
                                    final String tag) {
        RxBus.removeSticky(event, tag);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final RxBus.Callback<T> callback) {
        RxBus.subscribe(subscriber, callback);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final Scheduler scheduler,
                                     final RxBus.Callback<T> callback) {
        RxBus.subscribe(subscriber, scheduler, callback);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final String tag,
                                     final RxBus.Callback<T> callback) {
        RxBus.subscribe(subscriber, tag, callback);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final String tag,
                                     final Scheduler scheduler,
                                     final RxBus.Callback<T> callback) {
        RxBus.subscribe(subscriber, tag, scheduler, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final RxBus.Callback<T> callback) {
        RxBus.subscribeSticky(subscriber, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final Scheduler scheduler,
                                           final RxBus.Callback<T> callback) {
        RxBus.subscribeSticky(subscriber, scheduler, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final String tag,
                                           final RxBus.Callback<T> callback) {
        RxBus.subscribeSticky(subscriber, tag, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final String tag,
                                           final Scheduler scheduler,
                                           final RxBus.Callback<T> callback) {
        RxBus.subscribeSticky(subscriber, tag, scheduler, callback);
    }

    public static <T> void subscribeWithTags(final Object subscriber,
                                             final RxBus.Callback<T> callback,
                                             final String... tags) {
        subscribeWithTags(subscriber, null, callback, tags);
    }

    public static <T> void subscribeWithTags(final Object subscriber,
                                             final Scheduler scheduler,
                                             final RxBus.Callback<T> callback,
                                             final String... tags) {
        if (tags == null || tags.length == 0) {
            throw new RuntimeException("Tags is empty,you should set the tags");
        }
        for (String tag : tags) {
            RxBus.subscribe(subscriber, tag, scheduler, callback);
        }
    }

    public static <T> void subscribeStickyWithTags(final Object subscriber,
                                                   final RxBus.Callback<T> callback,
                                                   final String... tags) {
        subscribeStickyWithTags(subscriber, null, callback, tags);
    }

    public static <T> void subscribeStickyWithTags(final Object subscriber,
                                                   final Scheduler scheduler,
                                                   final RxBus.Callback<T> callback,
                                                   final String... tags) {
        if (tags == null || tags.length == 0) {
            throw new RuntimeException("Tags is empty,you should set the tags");
        }
        for (String tag : tags) {
            RxBus.subscribeSticky(subscriber, tag, scheduler, callback);
        }
    }

    public static void unregister(final Object subscriber) {
        RxBus.unregister(subscriber);
    }
}
```

## *感谢原作者的贡献
