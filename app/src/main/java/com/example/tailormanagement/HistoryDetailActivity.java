package com.example.tailormanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class HistoryDetailActivity extends AppCompatActivity {

    Order order;
    TextView styleNameTxt,orderIdTxt,orderDateTxt,orderStatusTxt,tailorTxt,measurementTxt,paymentStatusTxt;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        order = new Order();
        order = (Order) getIntent().getSerializableExtra("orderObj");

        styleNameTxt = findViewById(R.id.historyDetailNameTxt);
        orderIdTxt = findViewById(R.id.historyDetailOrderIdTxt);
        orderDateTxt = findViewById(R.id.historyDetailOrderDateTxt);
        orderStatusTxt = findViewById(R.id.historyDetailStatusTxt);
        tailorTxt = findViewById(R.id.historyDetailTailorTxt);
        measurementTxt = findViewById(R.id.historyDetailMeasurementTxt);
        paymentStatusTxt = findViewById(R.id.historyDetailPaymentStatusTxt);
        imageView = findViewById(R.id.historyDetailImg);


        Date date = new Date(String.valueOf(order.getOrderDate()));
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        String measurments = "Height : "+order.getMesurment().getHeight()+"cm \n"
                +"Chest : "+order.getMesurment().getChest()+"cm \n"
                +"Waist Width : "+order.getMesurment().getWaist()+"cm \n"
                +"Hip : "+order.getMesurment().getHip()+"cm \n"
                +"Leg Length : "+order.getMesurment().getLeg()+"cm \n"
                +"Shoulder Width : "+order.getMesurment().getShoulder()+"cm \n";


        styleNameTxt.setText(order.getStyle().getStyleName());
        orderIdTxt.setText(String.valueOf(order.getOrderId()));
        orderDateTxt.setText(dateFormat.format(date));
        orderStatusTxt.setText(order.getStatus());
        tailorTxt.setText(order.getTailor().getName());
        measurementTxt.setText(measurments);
        paymentStatusTxt.setText(order.getPaymentStatus());
        imageView.setImageResource(order.getStyle().getImageResource());
    }
}
