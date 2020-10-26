package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    TextView name, email;
    Switch activity_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final UserModel user = UserModel.getInstance();

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        activity_switch = (Switch) findViewById(R.id.activity_switch);
        name.setText(user.name);
        email.setText(user.email);
        activity_switch.setChecked(user.active);

        //zobrazenie profilu prihlaseneho pouzivatela a zmena premennej 'Active' v databaze
        //ktora hovori o tom ci je pouzivatel, ktory si chce veci objednat alebo aj nakupuje pre inych

        activity_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                user.active = !user.active;
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://10.0.2.2:8080/rest/user/");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                            conn.setRequestProperty("Accept","application/json");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);

                            JSONObject jsonParam = new JSONObject();
                            jsonParam.put("id", user.id);
                            jsonParam.put("firstName", user.first_name);
                            jsonParam.put("lastName", user.last_name);
                            jsonParam.put("email", user.email);
                            jsonParam.put("active", user.active);
                            jsonParam.put("password", user.password);

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
            }
        });
    }

    public void onClickLogout(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
