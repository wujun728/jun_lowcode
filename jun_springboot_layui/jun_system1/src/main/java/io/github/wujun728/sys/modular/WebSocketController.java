
package io.github.wujun728.sys.modular;

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
