package com.example.datacollector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class CPUTimeActivity extends AppCompatActivity {

    private ListView listView;
    private volatile boolean exit;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> timeList = CPUTime.getCPUTimes();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CPUTimeActivity.this, android.R.layout.simple_list_item_1, timeList);
            listView.setAdapter(arrayAdapter);
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cputime);
        exit = false;

        listView = findViewById(R.id.listViewCPUTime);

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
