package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderFromUser extends AppCompatActivity {

    TextView name_buyer, name_user;
    EditText product_name_text;
    Button add_product_button, create_order_button;
    ListView listView;
    Spinner stores;

    EditText street_text, city_text, street_num_text, zip_text;

    ArrayAdapter<String> store_adapter;
    ArrayAdapter<String> adapter;
    ArrayList<String> product_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_from_user);

        name_user = (TextView) findViewById(R.id.user_name_on_create);
        name_buyer = (TextView) findViewById(R.id.name_buyer);
        final UserModel user = UserModel.getInstance();
        final BuyersModel buyer = BuyersModel.getInstance();

        name_buyer.setText(buyer.firstname + " " + buyer.lastname);
        name_user.setText(user.name);


        add_product_button = (Button) findViewById(R.id.add_product_button);
        listView = (ListView) findViewById(R.id.list_of_products);
        stores = (Spinner) findViewById(R.id.spinner_store);

        // vytvorenie spinnera s prednastavenymi hodnotami
        String[] store_array = new String[] {
                "Tesco", "Lidl", "Kaufland", "Billa"
        };
        store_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, store_array);
        store_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stores.setAdapter(store_adapter);

        product_list = new ArrayList<>();
        product_name_text = (EditText) findViewById(R.id.product_name_text);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, product_list);
        listView.setAdapter(adapter);

        street_text = (EditText) findViewById(R.id.street_text);
        city_text = (EditText) findViewById(R.id.city_text);
        street_num_text = (EditText) findViewById(R.id.street_num_text);
        zip_text = (EditText) findViewById(R.id.zip_text);


        // vytvorenie noveho produktu po stlaceni tlacidla
        add_product_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!product_name_text.getText().toString().isEmpty()){
                    product_list.add(product_name_text.getText().toString());
                }
                product_name_text.setText("");
                adapter.notifyDataSetChanged();

            }
        });
        // vymazanie produktu po kliknuti na neho
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                product_list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        create_order_button = (Button) findViewById(R.id.create_order_button);


        // vytvorenie novej ziadosti o nakup pre konkretneho pouzivatela
        create_order_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // zluci prvky z adresy do jedneho strinug
                final String address =
                        street_text.getText().toString() + "," +
                        street_num_text.getText().toString() + "," +
                        city_text.getText().toString() + "," +
                        zip_text.getText().toString();

                // zluci vsetky produkty do jedneho stringu
                String products = "";
                for (String product : product_list){
                    products = products.concat(product + ",");
                }
                final String all_products = products;

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

                            jsonParam.put("buyerId", buyer.id);
                            jsonParam.put("buyerName", buyer.firstname + " " + buyer.lastname);
                            jsonParam.put("customerId", user.id);
                            jsonParam.put("customerName", user.name);
                            jsonParam.put("status", 0);
                            jsonParam.put("products", all_products);
                            jsonParam.put("shop", stores.getSelectedItem().toString());
                            jsonParam.put("address", address);

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
                // presmerovanie na hlavnu stranu a oznam o uspesnom vytvoreni
                Intent intent = new Intent(OrderFromUser.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(com.example.wedeliver.OrderFromUser.this,"Objednávka bola úspešne vytvorená.",Toast.LENGTH_LONG).show();

            }

        });

    }


}
