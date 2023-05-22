package com.dedzec.mqttclientandroid;

import android.content.Context;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

public class MQTTClient {
    private MqttAndroidClient mqttClient;
    private final int qos = 1;
    private final Boolean retained = false;

    public MQTTClient(Context context, String serverURI, String clientId) {
        mqttClient = new MqttAndroidClient(context, serverURI, clientId);
    }

    private final IMqttActionListener defaultCbConnect = new IMqttActionListener() {
        private final String javaClass = "defaultCbConnect";

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(this.javaClass, "(Default) Connection success");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(this.javaClass, "Connection failure: ${exception.toString()}");
        }
    };

    private final MqttCallback defaultCbClient = new MqttCallback() {
        private final String javaClass = "defaultCbClient";

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Log.d(this.javaClass, "Receive message: ${message.toString()} from topic: $topic");
        }

        @Override
        public void connectionLost(Throwable cause) {
            Log.d(this.javaClass, "Connection lost ${cause.toString()}");
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(this.javaClass, "Delivery completed");
        }
    };

    private final IMqttActionListener defaultCbSubscribe = new IMqttActionListener() {
        private final String javaClass = "defaultCbSubscribe";

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(this.javaClass, "Subscribed to topic");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(this.javaClass, "Failed to subscribe topic");
        }
    };

    private final IMqttActionListener defaultCbUnsubscribe = new IMqttActionListener() {
        private final String javaClass = "defaultCbUnsubscribe";

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(this.javaClass, "Unsubscribed to topic");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(this.javaClass, "Failed to unsubscribe topic");
        }
    };

    private final IMqttActionListener defaultCbPublish = new IMqttActionListener() {
        private final String javaClass = "defaultCbPublish";

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(this.javaClass, "Message published to topic");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(this.javaClass, "Failed to publish message to topic");
        }
    };

    private final IMqttActionListener defaultCbDisconnect = new IMqttActionListener() {
        private final String javaClass = "defaultCbDisconnect";

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(this.javaClass, "Disconnected");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(this.javaClass, "Failed to disconnect");
        }
    };

    public void connect(String username, String password, IMqttActionListener cbConnect, MqttCallback cbClient) {
        mqttClient.setCallback(cbClient);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        try {
            mqttClient.connect(options, null, cbConnect);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    public void subscribe(String topic, int qos, IMqttActionListener defaultCbSubscribe) {
        try {
            mqttClient.subscribe(topic, qos, null, defaultCbSubscribe);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    };

    public void unsubscribe(String topic, IMqttActionListener defaultCbUnsubscribe) {
        try {
            mqttClient.unsubscribe(topic, null, defaultCbUnsubscribe);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String msg, int qos, Boolean retained, IMqttActionListener defaultCbPublish) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());
            message.setQos(qos);
            message.setRetained(retained);
            mqttClient.publish(topic, message, null, defaultCbPublish);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(IMqttActionListener defaultCbDisconnect) {
        try {
            mqttClient.disconnect(null, defaultCbDisconnect);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
