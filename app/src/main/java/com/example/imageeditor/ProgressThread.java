package com.example.imageeditor;

import android.widget.ProgressBar;

public class ProgressThread extends Thread{
    private int id;
    private ProgressBar progressBar;

    public ProgressThread(int id,ProgressBar pb){
        this.id=id;
        progressBar=pb;
    }

    public void run(){
        progressBar.setMax(100);
        progressBar.setProgress(0);

        while(progressBar.getProgress()<=99){
            try{
                sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
            progressBar.setProgress(progressBar.getProgress()+10);
        }
        
    }
}
