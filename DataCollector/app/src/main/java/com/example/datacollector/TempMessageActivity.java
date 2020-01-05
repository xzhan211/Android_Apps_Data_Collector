package com.example.datacollector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class TempMessageActivity extends AppCompatActivity {

    private volatile boolean stopUpdate;

    private ListView listView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> temperatureList = Temperature.getTemp();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TempMessageActivity.this, R.layout.mylistview, temperatureList);
            listView.setAdapter(arrayAdapter);
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature);
        stopUpdate = false;
        listView = findViewById(R.id.listViewTemperature);

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        if (stopUpdate)
                            continue;
                        Message msg = new Message();
                        handler.sendMessage(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdate = true;
    }

    public void stopUpdate(View view) {
        stopUpdate = true;
    }

    public void startUpdate(View view) {
        stopUpdate = false;
    }
}
