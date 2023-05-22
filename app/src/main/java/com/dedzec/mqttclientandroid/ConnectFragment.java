package com.dedzec.mqttclientandroid;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.navigation.Navigation;
/* CONSTANTS */
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_CLIENT_ID;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_CLIENT_ID_KEY;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_PWD;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_PWD_KEY;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_SERVER_URI;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_SERVER_URI_KEY;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_USERNAME;
import static com.dedzec.mqttclientandroid.MQTTConstants.MQTT_USERNAME_KEY;

public class ConnectFragment extends Fragment {
    EditText edittext_server_uri, edittext_client_id, edittext_username, edittext_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edittext_server_uri = (EditText) view.findViewById(R.id.edittext_server_uri);
        edittext_client_id = (EditText) view.findViewById(R.id.edittext_client_id);
        edittext_username = (EditText) view.findViewById(R.id.edittext_username);
        edittext_password = (EditText) view.findViewById(R.id.edittext_password);

        view.findViewById(R.id.button_prefill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set default values in edit texts
                edittext_server_uri.setText(MQTT_SERVER_URI);
                edittext_client_id.setText(MQTT_CLIENT_ID);
                edittext_username.setText(MQTT_USERNAME);
                edittext_password.setText(MQTT_PWD);
            }
        });

        view.findViewById(R.id.button_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clean values in edit texts
                edittext_server_uri.setText("");
                edittext_client_id.setText("");
                edittext_username.setText("");
                edittext_password.setText("");
            }
        });

        view.findViewById(R.id.button_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverURIFromEditText = edittext_server_uri.getText().toString();
                String clientIDFromEditText  = edittext_client_id.getText().toString();
                String usernameFromEditText  = edittext_username.getText().toString();
                String pwdFromEditText       = edittext_password.getText().toString();

                Bundle mqttCredentialsBundle = new Bundle();
                mqttCredentialsBundle.putString(MQTT_SERVER_URI_KEY, serverURIFromEditText);
                mqttCredentialsBundle.putString(MQTT_CLIENT_ID_KEY, clientIDFromEditText);
                mqttCredentialsBundle.putString(MQTT_USERNAME_KEY, usernameFromEditText);
                mqttCredentialsBundle.putString(MQTT_PWD_KEY, pwdFromEditText);

                Navigation.findNavController(view).navigate(R.id.action_ConnectFragment_to_ClientFragment, mqttCredentialsBundle);
            }
        });
    }
}
