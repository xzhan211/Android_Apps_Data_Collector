package com.example.datacollector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class GPUMessageActivity extends AppCompatActivity {

    private ListView listView;
    private volatile boolean exit;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> GPUList = GPU.getGPU();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GPUMessageActivity.this, android.R.layout.simple_list_item_1, GPUList);
            listView.setAdapter(arrayAdapter);
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpu);
        exit = false;
        listView = findViewById(R.id.listViewGPU);

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
}
