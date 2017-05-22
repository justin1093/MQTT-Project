package com.example.viji.myapplication;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.EditText;
//https://developer.zebra.com/docs/DOC-2315


public class MainActivity extends AppCompatActivity {

    Button button;
    Button button1;
    EditText eText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        String server = "tcp://m11.cloudmqtt.com:11091";
        MQTTUtils.connect(server);

    }

    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.switch1);
        button1 = (Button) findViewById(R.id.button01);
        eText = (EditText) findViewById(R.id.editText);
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //String payload =" testttte" ;

                //Toast.makeText(MainActivity.this, payload, Toast.LENGTH_LONG).show();
                try {
                    String topic = "Set point";
                    String str = eText.getText().toString();
                    String payload =String.format(str);
                    MQTTUtils.pub(topic, payload);
                    Toast.makeText(MainActivity.this, payload, Toast.LENGTH_LONG).show();

                }catch(Exception ex){

                    String test ="msg";
                    Toast.makeText(MainActivity.this, test, Toast.LENGTH_LONG).show();
                    ex.printStackTrace();

                }

            }

        });


        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //String payload =" testttte" ;
                startService(new Intent(MainActivity.this, SilentBeaconService.class));
                //Toast.makeText(MainActivity.this, payload, Toast.LENGTH_LONG).show();
                try {

                    String topic = "Blinds";
                    String status ="true";
                    String payload =String.format("{switch: %s}", status);
                    MQTTUtils.pub(topic, payload);
                    Toast.makeText(MainActivity.this, payload, Toast.LENGTH_LONG).show();

                }catch(Exception ex){

                    String test ="msg";
                    Toast.makeText(MainActivity.this, test, Toast.LENGTH_LONG).show();
                    ex.printStackTrace();

                }

            }

        });
    }
}
