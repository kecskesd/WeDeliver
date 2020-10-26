package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView name_loggedin;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        name_loggedin = (TextView) findViewById(R.id.name_loggedin);
        user = UserModel.getInstance();
        name_loggedin.setText(user.name);

        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};

        int permsRequestCode = 200;

        requestPermissions(perms, permsRequestCode);


        final BuyersModel listOfBuyers = BuyersModel.getInstance();
        final UserModel user = UserModel.getInstance();


        listOfBuyers.drop();

        // nacitavainie vsetkych pouzivatelov, ktory su ochotni pre niekoho nakupit
        String URL = "http://10.0.2.2:8080/rest/user/active/true";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest objectRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest response", response.toString());
                        int x = 0;
                        for (int i = 0 - x ; i < response.length(); i++) {
                            JSONObject obj = null;
                            try {
                                BuyersModel model = new BuyersModel();

                                obj = response.getJSONObject(i);
                                String first_name = (String) obj.get("firstName");
                                String last_name = (String) obj.get("lastName");
                                String email = (String) obj.get("email");
                                int id = (Integer) obj.get("id");
                                if(id != user.id){
                                    listOfBuyers.fill(i - x,first_name,last_name,email, id);
                                } else {
                                    x++;
                                }

                                System.out.println(first_name + " " + last_name + " " + email);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);



        final OrdersModel listOfOrders = OrdersModel.getInstance();

        listOfOrders.drop();

        // nacitanie vsetkych objednavok o ktore ho niekto poziadal,
        // pre aktualne prihlaseneho pouzivatela
        URL = "http://10.0.2.2:8080/rest/order/buyerId/" + user.id;
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        JsonArrayRequest objectRequest1 = new JsonArrayRequest(com.android.volley.Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest response", response.toString());
                        int x = 0;
                        for (int i = 0 - x; i < response.length(); i++) {
                            JSONObject obj = null;
                            try {
                                obj = response.getJSONObject(i);
                                String name = (String) obj.get("customerName");
                                String store = (String) obj.get("shop");
                                String address = (String) obj.get("address");
                                String products = (String) obj.get("products");
                                int status = (Integer) obj.get("status");
                                int id = (Integer) obj.get("id");
                                int customerId = (Integer) obj.get("customerId");
                                if(status != 1){
                                    listOfOrders.fill(i - x,name, address, store, products, status,customerId, id);
                                } else {
                                    x++;
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );
        requestQueue1.add(objectRequest1);


        final OrdersModel listOfRequests = OrdersModel.getInstance();

        listOfRequests.drop_requests();

        // nacitanie vsetkych obiednavok pre aktualne prihlaseneho pouzivatela,
        // ktore sam vytvoril
        URL = "http://10.0.2.2:8080/rest/order/customerId/" + user.id;
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        JsonArrayRequest objectRequest2 = new JsonArrayRequest(com.android.volley.Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest response", response.toString());
                        for (int i = 0 ; i < response.length(); i++) {
                            JSONObject obj = null;
                            try {
                                obj = response.getJSONObject(i);
                                String name = (String) obj.get("customerName");
                                String store = (String) obj.get("shop");
                                String address = (String) obj.get("address");
                                String products = (String) obj.get("products");
                                int id = (Integer) obj.get("id");
                                int customerId = (Integer) obj.get("customerId");

                                listOfRequests.fill_request(i,name, address, store, products, customerId, id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );
        requestQueue2.add(objectRequest2);


    }

    public void showMap(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void showLogin(View view) {
        user = UserModel.getInstance();
        if (user.id == 0) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    public void showList(View view) {

        Intent intent = new Intent(this, Request.class);
        startActivity(intent);
    }

    public void showRequestList(View view) {

        Intent intent = new Intent(this, ListOfRequests.class);
        startActivity(intent);
    }

    public void showOrderList(View view) {
        Intent intent = new Intent(this, AllMyOrders.class);
        startActivity(intent);
    }
}
