package com.innovhaitian.guardtour.network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.innovhaitian.guardtour.MyApplication;


/**
 * Created by Castro Alhdo on 10/18/15.
 * Code writed by Castro Alhdo on 10/18/15
 * Don't use my code without my approuve
 */
public class VolleySingleton {
    private static VolleySingleton sInstance=null;

    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    private VolleySingleton(){
    mRequestQueue= Volley.newRequestQueue(MyApplication.getAppContext());
        imageLoader=new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private LruCache<String, Bitmap> cache=new LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024)/8);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }
    public static VolleySingleton getsInstance(){
        if (sInstance==null){
            sInstance=new VolleySingleton();
        }
        return sInstance;
    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
















