package com.example.datacollector;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CPUFreqencyActivity extends AppCompatActivity {

    private ListView listView;
    private volatile boolean exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpu);

        exit = false;

        listView = findViewById(R.id.listViewCPU);

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (exit)
                            break;
                        Thread.sleep(1000);
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
        exit = true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> frequencyList = CPUFreq.getFreq();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CPUFreqencyActivity.this, android.R.layout.simple_list_item_1, frequencyList);
            listView.setAdapter(arrayAdapter);
            super.handleMessage(msg);
        }
    };
}
