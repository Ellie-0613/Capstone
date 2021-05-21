package com.example.capstone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }
    private void sendRegistrationToServer(String token) {
        Log.d("test", token);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("test", "1");
        if (remoteMessage.getData().size() > 0) {
            Log.d("test", "2");
            sendNotification(remoteMessage.getData());
        }
    }
    private void sendNotification(Map<String, String> messageBody) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d("test", "3");
            String channelID="channel_01"; //알림채널 식별자
            String channelName="MyChannel01"; //알림채널의 이름(별명)

            //알림채널 객체 만들기
            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

            //알림매니저에게 채널 객체의 생성을 요청
            notificationManager.createNotificationChannel(channel);

            //알림건축가 객체 생성
            builder=new NotificationCompat.Builder(this, channelID);

        }else{
            Log.d("test", "4");
            //알림 건축가 객체 생성
            builder= new NotificationCompat.Builder(this, null);
        }
        Log.d("test", "5");
        //건축가에게 원하는 알림의 설정작업
        builder.setSmallIcon(android.R.drawable.ic_menu_view);

        //상태바를 드래그하여 아래로 내리면 보이는
        //알림창(확장 상태바)의 설정
        builder.setContentTitle(messageBody.get("title"));//알림창 제목
        builder.setContentText(messageBody.get("body"));//알림창 내용

        //건축가에게 알림 객체 생성하도록
        Notification notification=builder.build();

        //알림매니저에게 알림(Notify) 요청
        notificationManager.notify(1, notification);
        Log.d("test", "6");
    }
}
