package com.example.imageeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class ProgressThread extends Thread{
    private int ID;
    private ProgressBar progressBar;
    private Context context;
    private FilterService filterService;
    public Object getLockObject() {
        return lockObject;
    }

    private final Object lockObject;
    private boolean running=false;
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }



    public ProgressThread(int id, ProgressBar pb, Object lock, Context c, ImageView imageView){
        ID=id;
        progressBar=pb;
        lockObject=lock;
        context=c;
        filterService=new FilterService(imageView);
    }

    public void run(){

        progressBar.setMax(100);
        progressBar.setProgress(0);
        Bitmap finalImage=null;
        while(progressBar.getProgress()<=99){
            running=true;
            finalImage=filterService.filter(ID);
            try{
                sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }progressBar.setProgress(progressBar.getProgress()+5);
            synchronized (lockObject){
                if(!running) {
                    try {
                        lockObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        running=false;
        MainActivity.mapOfEditImage.put(ID,finalImage);

        Looper.prepare();
        Toast.makeText(context,"Process "+ID+" is done",Toast.LENGTH_SHORT).show();
        Looper.loop();

    }

}
