package com.example.imageeditor;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.widget.ImageView;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;


public class FilterService {
    private ImageView originalImage;

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    public FilterService(ImageView iw){
        originalImage=iw;
    }



    public Bitmap filter(int i){
        Filter myFilter=null;
        switch (i){
            case 1: myFilter=SampleFilters.getBlueMessFilter();
                break;
            case 2: myFilter=SampleFilters.getLimeStutterFilter();
                break;
            case 3: myFilter=SampleFilters.getNightWhisperFilter();
                break;
        }


        BitmapDrawable drawable=(BitmapDrawable)originalImage.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        Bitmap inputImage=bitmap.copy(Bitmap.Config.ARGB_8888,true);

        return myFilter.processFilter(inputImage);
    }

}
