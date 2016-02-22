package com.innovhaitian.guardtour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.innovhaitian.guardtour.network.VolleySingleton;

import java.util.Date;

public class MainActivity extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView myTextView;
    private QRCodeReaderView mydecoderview;
    private ImageButton flash;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    LocalTimeStore localTimeStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localTimeStore = new LocalTimeStore(this);
        if (System.currentTimeMillis() - localTimeStore.getStoreTime() < localTimeStore.getRemainingTime() && localTimeStore.getStoreTime() != -1){
            localTimeStore.setTimeStored(true);
        }else if (localTimeStore.getStoreTime() == -1)
            localTimeStore.setTimeStored(false);

        if (localTimeStore.getTimeStored()){
            startActivity(new Intent(MainActivity.this, Thanks.class));
            finish();
        }

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);

        mydecoderview.setOnQRCodeReadListener(this);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
        flash = (ImageButton)

                findViewById(R.id.light);

        //flashing();
    }

    public String sendQrRequest(int id, String date, String qrcode, String desprition) {
        String uid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String link = MyApplication.GUARD_TOUR + "uid=" + id + "&date_tour=" + date + "&qrcode=" + qrcode + "&description=" + desprition + "";
        return link;

    }


    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

//        Date date = new Date();
//        final String url = sendQrRequest(1, "2016-01-02", text, "description");


//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Log.i("REQUEST", "Registering " + response);
//                        Log.i("REQUEST", url);
//                        //Log.i("PEOPLE","Contact position "+cur.getPosition());
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("REQUEST", "Failed ");
//                Log.i("REQUEST", error.toString());
//                Log.i("REQUEST", url);
//                //Log.i("PEOPLE", error.toString());
//                // Log.i("PEOPLE", MyApplication.PEOPLE + "uid=1234&nom=" + name + "&prenom=Test" + "&phone=" + phoneNo);
//            }
//        });
//
//        requestQueue.add(stringRequest);
        Thread welcome = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);
                } catch (Exception e) {

                }

            }
        };
        welcome.start();
        Intent intent = new Intent(MainActivity.this, Incident.class);
        intent.putExtra("qrCodeValue", text);
        startActivity(intent);
        finish();
    }


    // Called when your device have no camera
    @Override
    public void cameraNotFound() {

    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
        //if(hasFlash)
        //turnOnFlash();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
        //turnOffFlash();
    }
}