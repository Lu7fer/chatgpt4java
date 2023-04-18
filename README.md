<h1 style="text-align: center; color: hotpink; -webkit-animation: rainbow 5s infinite; -moz-animation: rainbow 5s infinite; -o-animation: rainbow 5s infinite; animation: rainbow 5s infinite;">ChatGPT Java API</h1>

![stable](https://img.shields.io/badge/stability-stable-brightgreen.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.plexpt/chatgpt)](https://maven-badges.herokuapp.com/maven-central/com.github.plexpt/chatgpt)


OpenAI ChatGPT 的SDK。觉得不错请右上角Star

感谢 [revChatGPT](https://github.com/acheong08/ChatGPT).

# 问题表

[共约67万个问题，欢迎拿去炼丹](https://github.com/PlexPt/awesome-chatgpt-prompts-zh/blob/main/question/README.md)



点击👇🏻传送链接，购买云服务器炼丹：

- [**阿里云服务器**](https://reurl.cc/NqQXyx)
- [**【腾讯云】云服务器，低至4.2元/月**](https://url.cn/B7m0OYnG)

# 功能特性

|     功能      |  特性  |
|:-----------:|:----:|
|   GPT 3.5   |  支持  |
|   GPT 4.0   |  支持  |
| GPT 4.0-32k |  支持  |
|    流式对话     |  支持  |
|    阻塞式对话    |  支持  |
|     前端      |  无   |
|     上下文     |  支持  |
|   计算Token   | 即将支持 |
|   多KEY轮询    |  支持  |
|     代理      |  支持  |
|    反向代理     |  支持  |


## 使用指南

最新版本 [![Maven Central](https://img.shields.io/maven-central/v/com.github.plexpt/chatgpt)](https://maven-badges.herokuapp.com/maven-central/com.github.plexpt/chatgpt)

maven
```
<dependency>
    <groupId>com.github.plexpt</groupId>
    <artifactId>chatgpt</artifactId>
    <version>4.0.5</version>
</dependency>
```

gradle
```
implementation group: 'com.github.plexpt', name: 'chatgpt', version: '4.0.5'
```



### 最简使用

也可以使用这个类进行测试 [ConsoleChatGPT](src/test/java/cf/vbnm/chatgpt/StreamTest.java)

```java
import cf.vbnm.chatgpt.ChatGPT;import cf.vbnm.chatgpt.EnableChatGPTClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@EnableChatGPTClient(keys = {"your keys"})
@Component
class Test { //国内需要代理
    @Autowired
    private ChatGPT chatGPT;
    
    public void test() {
        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);
    }
}

```


### 进阶使用

```java
        //国内需要代理 国外不需要
    @Autowired
    private ChatGPT chatGPT;
    
    Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
    Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
    
    ChatCompletion chatCompletion = ChatCompletion.builder()
            .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
            .messages(Arrays.asList(system, message))
            .maxTokens(3000)
            .temperature(0.9)
            .build();
    ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
    Message res = response.getChoices().get(0).getMessage();
    System.out.println(res);

```

### 流式使用

```java
    @Autowired
    private ChatGPT chatGPT;
    ChatGPT chatGPTStream;
    
            
    ConsoleStreamListener listener = new ConsoleStreamListener();
    Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
    ChatCompletion chatCompletion = ChatCompletion.builder()
        .messages(Arrays.asList(message))
        .build();
    chatGPTStream.streamChatCompletion(chatCompletion, listener);

```

## 多KEY自动轮询

只需替换chatGPT构造部分

```java
import cf.vbnm.chatgpt.EnableChatGPTClient;
@EnableChatGPTClient(keys = {"key1", "key2", "..."})
```

## 上下文

参考  [ChatContextHolder.java](src/main/java/cf/vbnm/chatgpt/util/ChatContextHolder.java) 



# 常见问题

|                                           问                                            |                                       答                                       |
|:--------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------:|
|                                        KEY从哪来？                                         | 手动注册生成：ai.com(需要海外手机号)、或者成品独享帐号：[购买](https://fk.fq.mk/?code=YT0xJmI9Mg%3D%3D) |
|                                        哪些地区不能用                                         |            **以下国家IP不支持使用：中国(包含港澳台) 俄罗斯 乌克兰 阿富汗 白俄罗斯 委内瑞拉 伊朗 埃及!!**            |
|                                         有封号风险吗                                         |                               充值的没有。你免费白嫖不封你封谁。                               |
|                                    我是尊贵的Plus会员，能用吗                                     |                             能用，照封不误。PLUS调用API没啥区别                             |
|                                       GPT4.0 怎么用                                       |                   申请 https://openai.com/waitlist/gpt-4-api                    |
|                                 api.openai.com ping不通？                                 |                               禁ping，用curl测试连通性                                |
|                                         显示超时？                                          |                                   IP不好，换个IP                                   |
|           显示`Your access was terminated due to violation of our policies`...           |                                   你号没了，下一个                                    |
| 显示`That model is currently overloaded with other requests. You can retry your request` |                                 模型过载，官方炸了，重试                                  |
|                                       生成的图片不能用？                                        |                                 图片是它瞎编的，洗洗睡吧                                  |
|                                         如何充值？                                          |                                 用国外信用卡，国内的不行                                  |
|                                       返回http 401                                       |                                 API 密钥写错了/没写                                  |
|                                       返回http 429                                       |                              请求超速了，或者官方超载了。充钱可解决                              |
|                                       返回http 500                                       |                                     服务器炸了                                     |
|                                                                                        |                                                                               |

---
