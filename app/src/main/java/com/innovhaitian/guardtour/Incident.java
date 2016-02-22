package com.innovhaitian.guardtour;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.innovhaitian.guardtour.network.VolleySingleton;

public class Incident extends AppCompatActivity implements View.OnClickListener {
    MyCustomAdapter dataAdapter = null;
    LocalTimeStore localTimeStore;
    String qrCodeValue;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        Bundle bundle = getIntent().getExtras();
        qrCodeValue = bundle.getString("qrCodeValue");
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();

        displayListView();
        checkButtonClick();
        localTimeStore = new LocalTimeStore(this);

    }

    private void displayListView() {

        //Array list of countries
        ArrayList<ListAdapter> incidentList = new ArrayList<ListAdapter>();
        ListAdapter incident = new ListAdapter("Vitre casse",false);
        incidentList.add(incident);
        incident = new ListAdapter("Fenetre Brisee",false);
        incidentList.add(incident);
        incident = new ListAdapter("Pas d'electricite",false);
        incidentList.add(incident);
        incident = new ListAdapter("Generatrice ne marche pas",false);
        incidentList.add(incident);
        incident = new ListAdapter("Consierge present",false);
        incidentList.add(incident);
        incident = new ListAdapter("Employe present",false);
        incidentList.add(incident);
        incident = new ListAdapter("Lumieres defectueuses",false);
        incidentList.add(incident);

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.list_item, incidentList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                ListAdapter incident = (ListAdapter) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + incident.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.findSelected:
                StringBuffer responseText = new StringBuffer();
                //responseText.append("Ce que vous avez selectionne ...\n");

                ArrayList<ListAdapter> incidentList = dataAdapter.incidentList;
                for(int i=0;i<incidentList.size();i++){
                    ListAdapter incident = incidentList.get(i);
                    if(incident.isSelected()){
                        responseText.append(" " + incident.getName()+",");
                    }

                }

                final String qr=qrCodeValue;
                final String incid=responseText.toString();
                Date date=new Date();
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                final String dt=format.format(date);
                //Toast.makeText(getApplicationContext(),responseText, Toast.LENGTH_LONG).show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MyApplication.GUARD_TOUR,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                               Log.i("GUARD", "SUCCESSFUL");
                                localTimeStore.storeTime();
                                localTimeStore.setRemainingTime(60000);
                                startActivity(new Intent(Incident.this, Thanks.class));
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Incident.this,"Please check your network and try again",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Incident.this, MainActivity.class));
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("uid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        params.put("qrcode",qr);
                        params.put("description", incid);
                        params.put("date_tour",dt);
                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                );


                requestQueue.add(stringRequest);




                break;

            case R.id.nothingToReport:
                Toast.makeText(Incident.this, "Thanks", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Incident.this, Thanks.class));

                localTimeStore.storeTime();
                localTimeStore.setRemainingTime(60000);


                Toast.makeText(Incident.this, "Rien n'a ete reporte ...", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

    }

    private class MyCustomAdapter extends ArrayAdapter<ListAdapter> {

        private ArrayList<ListAdapter> incidentList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ListAdapter> incidentList) {
            super(context, textViewResourceId, incidentList);
            this.incidentList = new ArrayList<ListAdapter>();
            this.incidentList.addAll(incidentList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_item, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        ListAdapter incident = (ListAdapter) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        incident.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListAdapter incident = incidentList.get(position);
            holder.code.setText(incident.getName());
            holder.name.setChecked(incident.isSelected());
            holder.name.setTag(incident);

            return convertView;

        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.findSelected);
        Button myButton2 = (Button) findViewById(R.id.nothingToReport);

        myButton.setOnClickListener(this);
        myButton2.setOnClickListener(this);


    }

}

