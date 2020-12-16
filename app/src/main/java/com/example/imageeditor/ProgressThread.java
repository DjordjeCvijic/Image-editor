package com.example.imageeditor;

import android.content.Context;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;


public class ProgressThread extends Thread{
    private int ID;
    private ProgressBar progressBar;
    private Context context;

    public Object getLockObject() {
        return lockObject;
    }

    private Object lockObject;
    private boolean running=false;
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }



    public ProgressThread(int id,ProgressBar pb,Object lock,Context c){
        ID=id;
        progressBar=pb;
        lockObject=lock;
        context=c;
    }

    public void run(){

        progressBar.setMax(100);
        progressBar.setProgress(0);

        while(progressBar.getProgress()<=99){
            running=true;
            progressBar.setProgress(progressBar.getProgress()+5);
            try{
                sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            synchronized (lockObject){
                if(!running) {
                    try {
                        System.out.println("*****************STOP "+ID);
                        lockObject.wait();
                        System.out.println("***********START "+ID);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        Looper.prepare();
        Toast.makeText(context,"Poces "+ID+" je zavrsen",Toast.LENGTH_SHORT).show();
        Looper.loop();

    }
    public int getID(){return ID;}

}
