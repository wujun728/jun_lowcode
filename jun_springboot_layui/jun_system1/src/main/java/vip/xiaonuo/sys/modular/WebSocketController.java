/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.sys.modular;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket控制器
 * 参考地址：https://blog.csdn.net/moshowgame/article/details/80275084
 *
 * @author xuyuxiang
 * @date 2021/1/21 11:19
 */
@ServerEndpoint("/webSocket/{userId}")
@RestController
public class WebSocketController {

    private static final Log log = Log.get();

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketController对象。
     */
    private static ConcurrentHashMap<Long, WebSocketController> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private Long userId;

    /**
     * 当建立连接时
     *
     * @author xuyuxiang
     * @date 2021/1/21 11:23
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        this.session = session;
        this.userId = userId;
        //如果已经包含该用户id，则移除后重新加入
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        }else{
            //否则直接加入
            webSocketMap.put(userId,this);
        }
        log.info(">>> 用户：{}已建立连接", userId);
    }

    /**
     * 当关闭连接时
     *
     * @author xuyuxiang
     * @date 2021/1/21 11:23
     */
    @OnClose
    public void onClose(Session session) {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            log.info(">>> 用户：{}已关闭连接", userId);
        } else {
            log.info(">>> 连接已关闭...");
        }
    }

    /**
     * 当接收到消息时，暂时用不到
     *
     * @author xuyuxiang
     * @date 2021/1/21 11:23
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info(">>> 接收到消息：{}", message);
    }

    /**
     * 当发生错误时
     *
     * @author xuyuxiang
     * @date 2021/1/21 11:23
     */
    @OnError
    public void onError(Session session, Throwable e) {
        log.error(">>> WebSocket出现未知错误: ");
        e.printStackTrace();
    }

    /**
     * 批量给用户发送消息
     *
     * @author xuyuxiang
     * @date 2021/1/21 11:23
     */
    public static void sendMessageBatch(String message, List<Long> userIdList) {
        userIdList.forEach(userId -> {
            sendMessage(message, userId);
        });
    }

    /**
     * 给用户发送消息
     *
     * @author xuyuxiang
     * @date 2021/1/21 11:23
     */
    public static void sendMessage(String message, Long userId) {
        if(ObjectUtil.isNotEmpty(userId) && webSocketMap.containsKey(userId)) {
            log.info(">>> 发送消息给：{}，内容：{}", userId, message);
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error(">>> 用户：{}，不在线，不发送消息", userId);
        }
    }

    /**
     * 消息发送方法
     *
     * @author xuyuxiang
     * @date 2021/1/21 11:23
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error(">>> WebSocket消息发送出现错误: ");
            e.printStackTrace();
        }
    }
}
