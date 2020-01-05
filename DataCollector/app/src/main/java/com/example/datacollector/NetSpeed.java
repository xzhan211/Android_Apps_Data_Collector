package com.example.datacollector;

import java.io.BufferedReader;
import java.io.FileReader;

public class NetSpeed {
    private static long rxOld = 0;
    private static long txOld = 0;
    protected static long[] getNetSpeed(){
        long[] result = new long[2];
        String line;
        String[] segs;
        long rx = -1;
        long tx = -1;
        boolean isNum;

        try{
            FileReader fr = new FileReader("/proc/net/dev");
            BufferedReader in = new BufferedReader(fr, 500);
            while((line = in.readLine()) != null){
                line = line.trim();
                if(line.startsWith("wlan0")){
                    segs = line.split(":")[1].split("\\s+");
                    rx = Long.parseLong(segs[1]);
                    tx = Long.parseLong(segs[9]);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        result[0] = rx - rxOld;
        result[1] = tx - txOld;

        rxOld = rx;
        txOld = tx;

        return result;
    }
}
