package com.seas.usuario.grouponandroidstudiov2.tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils extends Thread{
	
	private boolean ejecucion = true;
	private String imageHttpAddress = "";
	private Bitmap loadedImage = null;
	
	public Bitmap getLoadedImage() {
		return loadedImage;
	}
	public void setLoadedImage(Bitmap loadedImage) {
		this.loadedImage = loadedImage;
	}
	public Utils(String imageHttpAddress) {
		super();
		this.imageHttpAddress = imageHttpAddress;
	}
	public boolean isEjecucion() {
		return ejecucion;
	}
	public void setEjecucion(boolean ejecucion) {
		this.ejecucion = ejecucion;
	}
	
	@Override
	public void run() {
		ejecucion = true;
		URL imageUrl = null;
        HttpURLConnection conn = null; 
        try {
            imageUrl = new URL(imageHttpAddress);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (IOException e) {
        }finally{
        	if(conn!=null){conn.disconnect();}
        }
        ejecucion = false;
	}

//	public synchronized static Bitmap downloadFile(String imageHttpAddress) {
//		        
//		    }
}

