package controller;

import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.web.bind.annotation.*;
import service.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PublishMessage {
    private static String HOST = "tcp://47.107.97.120:1883";
    public static MqttClient client;

    public static final String SERVER_URL = "tcp://47.107.97.120:1883";

    //推送消息
    @RequestMapping(value = "mqtt/publishMessageOnTopic/{topic}", method = RequestMethod.POST)
    public Map<String, Object> publishMessageOnTopic(
            @PathVariable(value = "topic") String topic, @RequestBody String message) throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        JSONObject jsonObject = (JSONObject) JSONObject.parse(message);
        String mess = (String) jsonObject.get("message");
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(mess.getBytes());
        mqttMessage.setQos(2);
        mqttMessage.setRetained(true);
        if (this.findIndex(topic) == -1) {
            map.put("data", "no topic");
            return map;
        }
        int index = this.findIndex(topic);
        Server server = new Server();
        server.connect(topic);
        MqttTopic mqttTopic1 = server.mqttTopic1;
        Server.publish(mqttTopic1, mqttMessage);
        map.put("data", "已发送");
        return map;
    }

    public int findIndex(String topic) throws Exception {
        Server server = new Server();
        final List<String> topicList = server.getTOPIC();
        int Idnex = 0;
        for (String topic1 : topicList) {
            if (topic.equals(topic1)) {
                break;
            }
            Idnex++;
        }
        if (Idnex == topicList.size()) {
            return -1;
        } else {
            return Idnex;
        }
    }

    private MqttClient getMqttClient(String clientId) throws MqttException {
        if (client != null && clientId.equals(client.getClientId())) {
            return client;
        }
        return new MqttClient(HOST, clientId);
    }

    //创建主题
    @RequestMapping(value = "mqtt/createNewTopic/{topic}/{clientId}", method = RequestMethod.POST)
    public Map<String, Object> createNewTopic(@PathVariable(value = "topic") String topic,
                                              @PathVariable(value = "clientId") String clientId) {
        System.out.println("topic:" + topic + ",clientId" + clientId);
        try {
            Server server = new Server(topic, clientId);
            //server.connect(topic);
            MqttTopic topic1 = server.getMqttClient().getTopic("");
            System.out.println("topic1:" + topic1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return null;
    }
}
