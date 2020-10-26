package com.example.wedeliver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Request extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);



        BuyersModel listof = BuyersModel.getInstance();

        final ListView list = findViewById(R.id.list);

        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayList<Integer> id_list = new ArrayList<>();
        final ArrayList<String> firstname = new ArrayList<>();
        final ArrayList<String> lastname = new ArrayList<>();

        //cyklus ktory ide cez cely list nakupovatelov a od kazdeho
        // ulozi vsetky potrebne premenne ako meno a ID
        for(BuyersModel buyer : listof.listOfBuyers){
            arrayList.add(buyer.firstname + " " + buyer.lastname);
            id_list.add(buyer.id);
            firstname.add(buyer.firstname);
            lastname.add(buyer.lastname);
        }

        //zobrazenie zoznamu nakupovatelov

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arrayList
        );
        list.setAdapter(arrayAdapter);

        arrayAdapter.notifyDataSetChanged();

        //vyber jedneho konkretneho nakupovatela zo zoznamu
        final BuyersModel buyer = BuyersModel.getInstance();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
                UserModel user = UserModel.getInstance();
                buyer.id = (Integer) id_list.get(position);
                buyer.firstname = firstname.get(position);
                buyer.lastname = lastname.get(position);
                Intent intent = new Intent(Request.this,OrderFromUser.class);
                startActivity(intent);
            }
        });



    }
}
