package com.dedzec.mqttclientandroid;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.navigation.Navigation;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
/* CONSTANTS */
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_SERVER_URI_KEY;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_CLIENT_ID_KEY;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_USERNAME_KEY;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_PWD_KEY;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_TEST_TOPIC;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_TEST_MSG;

public class ClientFragment extends Fragment {
    private MQTTClient mqttClient;
    EditText edittext_pubtopic, edittext_pubmsg, edittext_subtopic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            private String LogText = "ClientFragment";

            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (mqttClient.isConnected()) {
                    // Disconnect from MQTT Broker
                    mqttClient.disconnect(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d(LogText, "Disconnected");

                            Toast.makeText(getActivity().getApplicationContext(), "MQTT Disconnection success", Toast.LENGTH_SHORT).show();

                            // Disconnection success, come back to Connect Fragment
                            Navigation.findNavController(getView()).navigate(R.id.action_ClientFragment_to_ConnectFragment);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.d(LogText, "Failed to disconnect");
                        }
                    });
                } else {
                    Log.d(LogText, "Impossible to disconnect, no server connected");
                }
            }

            private void closeFragment() {
                // Disable to close fragment
                this.setEnabled(false);
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String LogText = "ClientFragment";

        // Get EditText's
        edittext_pubtopic = (EditText) view.findViewById(R.id.edittext_pubtopic);
        edittext_pubmsg = (EditText) view.findViewById(R.id.edittext_pubmsg);
        edittext_subtopic = (EditText) view.findViewById(R.id.edittext_subtopic);

        // Get arguments passed by ConnectFragment
        String serverURI   = getArguments().getString(MQTT_SERVER_URI_KEY);
        String clientId    = getArguments().getString(MQTT_CLIENT_ID_KEY);
        String username    = getArguments().getString(MQTT_USERNAME_KEY);
        String pwd         = getArguments().getString(MQTT_PWD_KEY);

        // Check if passed arguments are valid
        if (serverURI != null
            && clientId != null
            && username != null
            && pwd != null        ) {
            // Open MQTT Broker communication
            mqttClient = new MQTTClient(getActivity().getApplicationContext(), serverURI, clientId);

            // Connect and login to MQTT Broker
            mqttClient.connect(username, pwd,
            new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d(LogText, "Connection success");

                            Toast.makeText(getActivity().getApplicationContext(), "MQTT Connection success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.d(LogText, "Connection failure: " + exception.toString());

                            Toast.makeText(getActivity().getApplicationContext(), "MQTT Connection fails: " + exception.toString(), Toast.LENGTH_SHORT).show();

                            // Come back to Connect Fragment
                            Navigation.findNavController(view).navigate(R.id.action_ClientFragment_to_ConnectFragment);
                        }
            },
            new MqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String msg = "Receive message: " + message.toString() + " from topic: " + topic;
                    Log.d(LogText, msg);

                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.d(LogText, "Connection lost " + cause);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(LogText, "Delivery complete");
                }
            });
        } else {
            // Arguments are not valid, come back to Connect Fragment
            Navigation.findNavController(view).navigate(R.id.action_ClientFragment_to_ConnectFragment);
        }

        view.findViewById(R.id.button_prefill_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set default values in edit texts
                edittext_pubtopic.setText(MQTT_TEST_TOPIC);
                edittext_pubmsg.setText(MQTT_TEST_MSG);
                edittext_subtopic.setText(MQTT_TEST_TOPIC);
            }
        });

        view.findViewById(R.id.button_clean_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clean values in edit texts
                edittext_pubtopic.setText("");
                edittext_pubmsg.setText("");
                edittext_subtopic.setText("");
            }
        });

        view.findViewById(R.id.button_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mqttClient.isConnected()) {
                    // Disconnect from MQTT Broker
                    mqttClient.disconnect(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d(LogText, "Disconnected");

                            Toast.makeText(getActivity().getApplicationContext(), "MQTT Disconnection success", Toast.LENGTH_SHORT).show();

                            // Disconnection success, come back to Connect Fragment
                            Navigation.findNavController(view).navigate(R.id.action_ClientFragment_to_ConnectFragment);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.d(LogText, "Failed to disconnect");
                        }
                    });
                } else {
                    Log.d(LogText, "Impossible to disconnect, no server connected");
                }
            }
        });

        view.findViewById(R.id.button_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic   = edittext_pubtopic.getText().toString();
                String message = edittext_pubmsg.getText().toString();

                if (mqttClient.isConnected()) {
                    mqttClient.publish(topic,
                            message,
                            1,
                            false,
                            new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    String msg ="Publish message: " + message + " to topic: " + topic;
                                    Log.d(LogText, msg);

                                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    Log.d(LogText, "Failed to publish message to topic");
                                }
                            });
                } else {
                    Log.d(LogText, "Impossible to publish, no server connected");
                }
            }
        });

        view.findViewById(R.id.button_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = edittext_subtopic.getText().toString();

                if (mqttClient.isConnected()) {
                    mqttClient.subscribe(topic,
                            1,
                            new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    String msg = "Subscribed to: "+ topic;
                                    Log.d(LogText, msg);

                                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    Log.d(LogText, "Failed to subscribe: $topic");
                                }
                            });
                } else {
                    Log.d(LogText, "Impossible to subscribe, no server connected");
                }
            }
        });

        view.findViewById(R.id.button_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = edittext_subtopic.getText().toString();

                if (mqttClient.isConnected()) {
                    mqttClient.unsubscribe( topic,
                            new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    String msg = "Unsubscribed to: " + topic;
                                    Log.d(LogText, msg);

                                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    Log.d(LogText, "Failed to unsubscribe: $topic");
                                }
                    });
                } else {
                    Log.d(LogText, "Impossible to unsubscribe, no server connected");
                }
            }
        });
    }
}
