package com.example.datacollector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static boolean firstFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        String testPath = "";
        String apkRoot = "";

        if (!firstFlag) {
            testPath = getPackageCodePath();
            apkRoot = "chmod 777 " + testPath;
            SystemManager.RootCommand(apkRoot);
            firstFlag = true;
        }
    }


    public void cpuButton(View view) {
        Intent intent = new Intent(this, CPUFreqencyActivity.class);
        startActivity(intent);
    }

    public void cpuTimeButton(View view) {
        Intent intent = new Intent(this, CPUTimeActivity.class);
        startActivity(intent);
    }

    public void batteryButton(View view) {
        Intent intent = new Intent(this, BatteryMessageActivity.class);
        startActivity(intent);
    }

    public void gpuButton(View view) {
        Intent intent = new Intent(this, GPUMessageActivity.class);
        startActivity(intent);
    }

    public void netButton(View view) {
        Intent intent = new Intent(this, NetMessageActivity.class);
        startActivity(intent);
    }

    public void tempButton(View view) {
        Intent intent = new Intent(this, TempMessageActivity.class);
        startActivity(intent);
    }

    public void memoryButton(View view) {
        Intent intent = new Intent(this, MemoryMessageActivity.class);
        startActivity(intent);
    }

    public void dataStorageButton(View view) {
        Intent intent = new Intent(this, DataStorageActivity.class);
        startActivity(intent);
    }
}

