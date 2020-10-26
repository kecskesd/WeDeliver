package com.example.wedeliver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.common.api.Response;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;


import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    CardView login;
    EditText email, password;
    TextView name_loggedin;
    boolean validation = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (CardView) findViewById(R.id.login_button);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    //otvorene novej karty/viewu

    public void onClickSignup(View v){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void onClickLogin(View v){

        final UserModel user = UserModel.getInstance();
        final MainActivity help = new MainActivity();

        //po kliknuti na login sa posle GET request, ktory nam da z databazy
        //data pre zadany email, po zahashovani zadane heslo sa hashe porovnaju
        //pokial heslo sedi pouzivatel sa prihlasi do appky

        String URL = "http://10.0.2.2:8080/rest/user/email/" + email.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest response", response.toString());
                       // response = (JSONObject) JSONSerializer.toJSON(response.toString());

                        try {
                            user.id = (Integer) response.get("id");
                            user.first_name = (String) response.get("firstName");
                            user.last_name = (String) response.get("lastName");
                            user.email = (String) response.get("email");
                            user.active = (Boolean) response.get("active");
                            user.password = (String) response.get("password");
                            user.name = user.first_name + " " + user.last_name;

                            //hashovanie hesiel

                            String inputPassowrd = password.getText().toString();
                            int hash = inputPassowrd.hashCode();
                            inputPassowrd = Integer.toString(hash);


                            //porovnanie zahashovaneho hesla z inputu s hashom z databazy

                            if(response.get("password").equals(inputPassowrd)){
                                validation = true;
                                System.out.println("OKOK");
                            } else {
                                System.out.println("BADBAD");
                                Toast.makeText(com.example.wedeliver.Login.this,"Nesprávne meno alebo heslo",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(validation){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                        Toast.makeText(com.example.wedeliver.Login.this,"Nesprávny E-Mail alebo heslo",Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(objectRequest);





    }
}
