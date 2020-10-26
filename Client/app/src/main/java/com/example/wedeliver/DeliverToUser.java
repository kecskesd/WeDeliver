package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DeliverToUser extends AppCompatActivity {

    TextView name_customer, name_user;
    TextView store, address;
    Button accept_order_button;
    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<String> product_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_to_user);

        // inicializacia vsetkych elementov z layoutu
        name_customer = (TextView) findViewById(R.id.name_customer);
        name_user = (TextView) findViewById(R.id.user_name_on_deliver);
        store = (TextView) findViewById(R.id.store);
        address = (TextView) findViewById(R.id.address_text);
        listView = (ListView) findViewById(R.id.list_of_products);
        accept_order_button = (Button) findViewById(R.id.accept_order_button);

        final OrdersModel order = OrdersModel.getInstance();
        final UserModel user = UserModel.getInstance();

        // vytvorenie a naplnenie listu s produktami, ktore sa postupne
        // zo stringu povytahuju (oddelene ciarkou)
        product_list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, product_list);
        listView.setAdapter(adapter);

        String[] products = order.products.split(",");
        for (int i = 0; i < products.length; i++){
            product_list.add(products[i]);
        }
        adapter.notifyDataSetChanged();

        // nastavenie textu pre elementy v layoute
        name_customer.setText(order.name);
        name_user.setText(user.name);
        store.setText(order.store);
        address.setText(order.address);


        accept_order_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final UserModel user = UserModel.getInstance();
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://10.0.2.2:8080/rest/order");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                            conn.setRequestProperty("Accept","application/json");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);

                            JSONObject jsonParam = new JSONObject();

                            jsonParam.put("id", order.id);
                            jsonParam.put("buyerId", user.id);
                            jsonParam.put("buyerName", user.name);
                            jsonParam.put("customerId", order.customerId);
                            jsonParam.put("customerName", order.name);
                            jsonParam.put("status", 1);
                            jsonParam.put("products", order.products);
                            jsonParam.put("shop", order.store);
                            jsonParam.put("address", order.address);

                            Log.i("JSON", jsonParam.toString());
                            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                            os.writeBytes(jsonParam.toString());

                            os.flush();
                            os.close();

                            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                            Log.i("MSG" , conn.getResponseMessage());

                            conn.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                t.start();
                // presmerovanie na hlavnu stranu a zobrazenie spravy o uspešnej aktualizacii
                Intent intent = new Intent(DeliverToUser.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(com.example.wedeliver.DeliverToUser.this,"Objednávka bola akceptovaná.",Toast.LENGTH_LONG).show();

            }



        });



    }
}
