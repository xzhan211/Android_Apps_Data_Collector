package com.example.datacollector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class NetMessageActivity extends AppCompatActivity {

    private TextView tx;
    private TextView rx;
    private volatile boolean exit;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long[] speed = NetSpeed.getNetSpeed();
            tx.setText(((float) speed[1]) / 1000 + "");
            rx.setText(((float) speed[0]) / 1000 + "");
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net);

        tx = findViewById(R.id.tx);
        rx = findViewById(R.id.rx);

        exit = false;

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if(exit)
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
    protected void onDestroy(){
        super.onDestroy();
        exit = true;
    }
}
