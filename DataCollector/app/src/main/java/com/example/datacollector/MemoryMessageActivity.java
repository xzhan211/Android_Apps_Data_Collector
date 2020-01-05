package com.example.datacollector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class MemoryMessageActivity extends AppCompatActivity {

    private ListView listView;
    private volatile boolean exit;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> memoryList = Memory.getData();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MemoryMessageActivity.this, R.layout.mylistview, memoryList);
            listView.setAdapter(arrayAdapter);
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory);
        listView = findViewById(R.id.listViewMemory);

        exit = false;

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