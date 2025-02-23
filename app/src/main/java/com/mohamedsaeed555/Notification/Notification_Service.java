package com.mohamedsaeed555.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.ecommerce.R;
import com.mohamedsaeed555.ecommerce.RecyclerAdapter;
import com.mohamedsaeed555.ecommerce.SecondActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;


public class Notification_Service extends Service {
    private static final String CHANNEL_ID = "Ecommerce";
    int x = 0;
    NotificationManagerCompat managerCompat;
    Gson gson = new Gson();
    Database db = new Database(this);
    private Socket mSocket;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Notification_Class notification_class = gson.fromJson(args[0].toString(),Notification_Class.class);

            if (notification_class.getAdmin()){
                if (notification_class.getGo_Activity().equals("orderdetails")) {
                    if (!(db.getAllusers().get(0).getAdmin())) {
                        if (notification_class.getSender_id().equals(db.getAllusers().get(0).get_id())) {
                            if (notification_class.getMsg().equals("Admin Contact with your order")) {
                                createNotificationchannel(notification_class);
                            }
                        }
                    }
                }else if (notification_class.getGo_Activity().equals("admin")){
                    if (notification_class.getSender_id().equals(db.getAllusers().get(0).get_id())){
                        createNotificationchannel(notification_class);
                    }
                } else {
                    if (!(db.getAllusers().get(0).get_id().equals(notification_class.getSender_id()))) {
                   /* if (notification_class.getGo_Activity().equals("allusers")){
                        createNotificationchannel(notification_class);
                    }else if (notification_class.getGo_Activity().equals("orderdetails")){
                        createNotificationchannel(notification_class);
                    }*/
                        if (db.getAllusers().get(0).getAdmin()) {
                            createNotificationchannel(notification_class);
                        }
                    }
                }
            }else {
                if (notification_class.getGo_Activity().equals("allusers")) {
                    if (db.getAllusers().get(0).getAdmin()){
                        createNotificationchannel(notification_class);
                    }
                }else {

                    if (!(db.getAllusers().get(0).get_id().equals(notification_class.getSender_id()))) {
                        if (!(notification_class.getGo_Activity().equals("allusers"))) {
                            createNotificationchannel(notification_class);
                        }
                    } /*else if (notification_class.getGo_Activity().equals("orderdetails")) {
                        if (notification_class.getSender_id().equals(db.getAllusers().get(0).get_id())) {
                            if (notification_class.getMsg().equals("Admin Contact with your order")) {
                                createNotificationchannel(notification_class);
                            }
                        }
                    }*/
                }

            }

        }
    };

    {
        try {
            mSocket = IO.socket("https://newaccsys.herokuapp.com");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSocket.connect();
        mSocket.on("yapa", onNewMessage);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSocket.connect();
        mSocket.on("yapa", onNewMessage);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.connect();
        mSocket.on("yapa", onNewMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }
    }

    public void createNotificationchannel(Notification_Class arg  ) {

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("c", "c");
        intent.putExtra("go", gson.toJson(arg));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(arg.getMsg());
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.get().load(arg.getImage()).placeholder(R.mipmap.ic_launcher_round).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.setLargeIcon(bitmap);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle(getResources().getString(R.string.app_name));
        String msg = arg.getMsg() +"\nMohamed Saeed And Mohamed Ayman Make this Simple APP";
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        builder.setContentText(msg);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= 21)
            builder.setVibrate(new long[0]);
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        builder.setChannelId(CHANNEL_ID);
        managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(arg.getNotification_id(), builder.build());
    }

    public void createNotificationchannelnotclick(Notification_Class arg) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(arg.getSender_id(), "ECommerce", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(arg.getMsg());
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, arg.getSender_id());
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.get().load(arg.getImage()).placeholder(R.drawable.haircode).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.setLargeIcon(bitmap);
        builder.setSmallIcon(R.drawable.cart2);
        builder.setContentTitle("ECommerce" + ++x );
        String msg = arg.getMsg() +"\nMohamed Saeed And Mohamed Ayman Make this Simple APP";
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        builder.setContentText(msg);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setOngoing(false);
        if (Build.VERSION.SDK_INT >= 21)
            builder.setVibrate(new long[0]);
        builder.setAutoCancel(true);
        builder.setChannelId(CHANNEL_ID);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(123456789,builder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        mSocket.connect();
        mSocket.on("yapa", onNewMessage);
    }
}
