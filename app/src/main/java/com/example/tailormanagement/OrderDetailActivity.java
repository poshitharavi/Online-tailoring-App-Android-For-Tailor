package com.example.tailormanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    Order order;
    TextView styleNameTxt, orderIdTxt, orderDateTxt, orderStatusTxt, tailorTxt, measurementTxt, paymentStatusTxt, customerNameTxt,customerContactTxt;
    ImageView imageView;
    Spinner statusSpinner;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mRequestQueue = Volley.newRequestQueue(this);
        statusSpinner = findViewById(R.id.tailorStatusSpinner);

        final List<String> statusArray = new ArrayList<>();
        statusArray.add("Select ");
        statusArray.add("on Cutting Process");
        statusArray.add("on Sew process");
        statusArray.add("ready to deliver");
        statusArray.add("delivered");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, statusArray);
        statusSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        order = new Order();
        order = (Order) getIntent().getSerializableExtra("orderObj");

        styleNameTxt = findViewById(R.id.orderDetailNameTxt);
        orderIdTxt = findViewById(R.id.orderDetailOrderIdTxt);
        orderDateTxt = findViewById(R.id.orderDetailOrderDateTxt);
        orderStatusTxt = findViewById(R.id.orderDetailStatusTxt);
        tailorTxt = findViewById(R.id.orderDetailTailorTxt);
        measurementTxt = findViewById(R.id.orderDetailMeasurementTxt);
        paymentStatusTxt = findViewById(R.id.orderDetailPaymentStatusTxt);
        imageView = findViewById(R.id.orderDetailImg);
        statusSpinner = findViewById(R.id.tailorStatusSpinner);
        customerContactTxt = findViewById(R.id.orderDetailCustomerContactTxt);
        customerNameTxt = findViewById(R.id.orderDetailCustomerTxt);


        Date date = new Date(String.valueOf(order.getOrderDate()));
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        String measurments = "Height : " + order.getMesurment().getHeight() + "cm \n"
                + "Chest : " + order.getMesurment().getChest() + "cm \n"
                + "Waist Width : " + order.getMesurment().getWaist() + "cm \n"
                + "Hip : " + order.getMesurment().getHip() + "cm \n"
                + "Leg Length : " + order.getMesurment().getLeg() + "cm \n"
                + "Shoulder Width : " + order.getMesurment().getShoulder() + "cm \n";


        styleNameTxt.setText(order.getStyle().getStyleName());
        orderIdTxt.setText(String.valueOf(order.getOrderId()));
        orderDateTxt.setText(dateFormat.format(date));
        orderStatusTxt.setText(order.getStatus());
        tailorTxt.setText(order.getTailor().getName());
        measurementTxt.setText(measurments);
        paymentStatusTxt.setText(order.getPaymentStatus());
        customerNameTxt.setText(order.getUser().name);
        customerContactTxt.setText(order.getUser().email);
        imageView.setImageResource(order.getStyle().getImageResource());


        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    statusSpinner.setEnabled(false);
                    JSONObject reqObj = new JSONObject();
                    try {
                        reqObj.put("status", statusArray.get(position));

                        Log.d("tag", reqObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, getResources().getString(R.string.api_url) + "order/status-update/" + order.getOrderId(), reqObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Data: ", jsonObject.toString());
                            Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                            try {
                                orderStatusTxt.setText(jsonObject.getString("status"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            statusSpinner.setEnabled(true);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("Error: ", volleyError.getMessage());
                            Toast.makeText(getApplicationContext(), "Error in network : " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            statusSpinner.setEnabled(true);
                        }
                    });

                    mRequestQueue.add(req);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
