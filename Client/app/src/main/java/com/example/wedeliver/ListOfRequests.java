package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListOfRequests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_requests);


        OrdersModel ordersList = OrdersModel.getInstance();

        final ListView list = findViewById(R.id.list);

        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayList<Integer> id_list = new ArrayList<>();
        final ArrayList<String> address_list = new ArrayList<>();
        final ArrayList<String> store_list = new ArrayList<>();
        final ArrayList<String> products_list = new ArrayList<>();
        final ArrayList<Integer> customer_ids = new ArrayList<>();

        // nacitanie vsetkych objednavok, pre pouzivatela, ktory je pre niekoho kupujuci
        for(OrdersModel order : ordersList.listOfOrders){
            arrayList.add(order.name);
            id_list.add(order.id);
            address_list.add(order.address);
            store_list.add(order.store);
            products_list.add(order.products);
            customer_ids.add(order.customerId);
        }

        // vytvorenie listView s tymito polozkami
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arrayList
        );
        list.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        // listener na kliknutie na polozku v zozname ziasosti
        // nacitanie udajov o ziadosti do vytvorenej instancie order,
        // ktora sa bude volat pri zobrazeni stranky so samotnou ziadostou
        final OrdersModel order = OrdersModel.getInstance();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
                UserModel user = UserModel.getInstance();
                order.id = (Integer) id_list.get(position);
                order.customerId  = (Integer) customer_ids.get(position);
                order.name = (String) arrayList.get(position);
                order.address = (String) address_list.get(position);
                order.store = (String) store_list.get(position);
                order.products = (String) products_list.get(position);
                Intent intent = new Intent(ListOfRequests.this,DeliverToUser.class);
                startActivity(intent);
            }
        });

    }
}
