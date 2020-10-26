package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyOrderPreview extends AppCompatActivity {

    TextView name_customer, name_user;
    TextView store, address;
    Button generate_pdf_button;
    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<String> product_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_preview);

        name_user = (TextView) findViewById(R.id.user_name_on_deliver);
        name_customer = (TextView) findViewById(R.id.name_customer);
        store = (TextView) findViewById(R.id.store);
        address = (TextView) findViewById(R.id.address_text);
        listView = (ListView) findViewById(R.id.list_of_products);
        generate_pdf_button = (Button) findViewById(R.id.generate_pdf_button);

        final OrdersModel order = OrdersModel.getInstance();
        final UserModel user = UserModel.getInstance();

        product_list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, product_list);
        listView.setAdapter(adapter);


        String[] products = order.products.split(",");
        for (int i = 0; i < products.length; i++){
            product_list.add(products[i]);
        }
        adapter.notifyDataSetChanged();

        name_user.setText(user.name);
        name_customer.setText(order.name);
        store.setText(order.store);
        address.setText(order.address);


        generate_pdf_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create a new document
                PdfDocument document = new PdfDocument();

                // crate a page description
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1120, 2500, 1).create();

                // start a page
                PdfDocument.Page page = document.startPage(pageInfo);

                // draw something on the page
                View content = findViewById(R.id.scrollView2);
                content.draw(page.getCanvas());

                // finish the page
                document.finishPage(page);

                // write the document content

                File file = new File(Environment.getExternalStorageDirectory() + "/" + "order.pdf");
                // write the document content
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    if (file.exists()) {
                        document.writeTo(fos);
                        document.close();
                        fos.close();
                        Log.i("LOG", Environment.getExternalStorageDirectory() + "/" + "order.pdf");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    document.close();
                    Log.i("LOG", "fuk: " + e.getLocalizedMessage());
                }

                Toast.makeText(getApplicationContext(), "PDF bolo úspešne vygenerované.", Toast.LENGTH_LONG).show();

            }
        });

    }
}
