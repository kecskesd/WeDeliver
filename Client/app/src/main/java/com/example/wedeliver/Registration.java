package com.example.wedeliver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

public class Registration extends AppCompatActivity {

    //trieda s funkcionalitou registracie

    CardView btn_button;
    EditText firstname, lastname, email, password, password_confirm;
    boolean valid = false;

    //priradenie vsetkych itemov z GUI do premennych

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn_button = (CardView) findViewById(R.id.button_register);

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password_confirm = (EditText) findViewById(R.id.password_confirm);

    }

    public void onClickRegister(View v){

        //kontrola validity registracneho formularu, najprv sa kontroluje ci sa v databaze
        //nenachadza zadany email pokial sa nenachadza prejde formular dalsou validaciou

        String URL = "http://10.0.2.2:8080/rest/user/email/" + email.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.i("Email validation:", email.getText().toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MAIL EXISTUJE", response.toString());
                        Toast.makeText(getApplicationContext(), "Tento E-Mail sa uz používa!", Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {

            //pokial mail este nieje pouzivany kontroluje sa dlzka heska a korekrnost zadaneho emailu
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MAIL NEEXISTUJE", error.toString());
                       // Toast.makeText(getApplicationContext(),"Nesprávny E-Mail alebo heslo",Toast.LENGTH_LONG).show();
                        if (password.getText().toString().length() < 6) {
                            Toast.makeText(getApplicationContext(), "Minimálna dĺžka hesla je 6!", Toast.LENGTH_LONG).show();
                        } else {
                            if (!isValidEmail(email.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Neplatný E-mail!", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    String x = password.getText().toString();
                                    String y = password_confirm.getText().toString();

                                    if (x.equals(y)) {

                                        //POST request, ktory prida noveho usera do databazy po uspesnej validacii

                                        Thread t = new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                try {
                                                    URL url = new URL("http://10.0.2.2:8080/rest/user");
                                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                    conn.setRequestMethod("POST");
                                                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                                                    conn.setRequestProperty("Accept", "application/json");
                                                    conn.setDoOutput(true);
                                                    conn.setDoInput(true);

                                                    JSONObject jsonParam = new JSONObject();
                                                    int hashpass = password.getText().toString().hashCode();
                                                    String hp = Integer.toString(hashpass);

                                                    //tvorba JSON do POST requestu

                                                    jsonParam.put("firstName", firstname.getText().toString());
                                                    jsonParam.put("lastName", lastname.getText().toString());
                                                    jsonParam.put("email", email.getText().toString());
                                                    jsonParam.put("password", hp);
                                                    jsonParam.put("active", true);


                                                    Log.i("JSON", jsonParam.toString());
                                                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                                                    os.writeBytes(jsonParam.toString());

                                                    os.flush();
                                                    os.close();

                                                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                                                    Log.i("MSG", conn.getResponseMessage());

                                                    conn.disconnect();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        t.start();
                                        valid = true;

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Heslá sa musia zhodovať!", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (valid) {
                                    Intent intent = new Intent(Registration.this, Login.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                }
        );
        requestQueue.add(objectRequest);




    }

    //kontrola validity emailu

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
