package com.mohamedsaeed555.ecommerce;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.Random;

public class testAcivity extends AppCompatActivity {
    Button btn , emit;
    String value="";
    int x=0;
    private static final String CHANNEL_ID = "Mohamed Saeed";
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://newaccsys.herokuapp.com");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_acivity);
        btn = findViewById(R.id.button10);
        emit = findViewById(R.id.button11);

        /*mSocket.on("yapa",onNewMessage);
        mSocket.connect();*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(testAcivity.this,SecondActivity.class);
                startActivity(i);
            }
        });

        emit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  dsd();
                mSocket.emit("dbchanged","Hallo Mohamed Saeed");
            }
        });

    }

    private void  dsd(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"ECommerce", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Abo Ayman");
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.cart2);
            builder.setContentTitle("ECommerce"+String.valueOf(++x));
            builder.setContentText("Abo Ayman");
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
            if (Build.VERSION.SDK_INT >= 21)
                builder.setVibrate(new long[0]);
            builder.setAutoCancel(true);
            builder.setChannelId(CHANNEL_ID);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(new Random().nextInt(), builder.build());

    }

/*
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Notification notification = new NotificationCompat.Builder(testAcivity.this,CHANNEL_ID)
                            .setContentTitle("Example Service")
                            .setContentText("Mohamed Saeed take break")
                            .setSmallIcon(R.drawable.cart2)
                            .setAutoCancel(true)
                            .build();
                }
            });
        }
    };
*/


}
