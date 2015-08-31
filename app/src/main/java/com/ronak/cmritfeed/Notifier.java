package com.ronak.cmritfeed;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.TextView;

/**
 * Created by ronak on 15/8/15.
 */
public class Notifier extends BroadcastReceiver {
    public static int REDIRECT_EVENT = 1;
    public static int REDIRECT_LATEST = 2;
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("type",1);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if(id == REDIRECT_EVENT){
            Event event = (Event)intent.getSerializableExtra("event");
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder notification = new Notification.Builder(context);
            notification.setContentTitle("CMRITFeed");
            String month,day;
            month = (String) android.text.format.DateFormat.format("MMM",event.getDate());
            day = (String) android.text.format.DateFormat.format("dd",event.getDate());
            notification.setContentText(event.getTitle()+"event is at"+event.getLocation()+" on "+day+" "+month).setSmallIcon(R.drawable.contact_drawer);
            Intent intent1 = new Intent(context,DescriptionActivity.class);
            intent1.putExtra("event", event);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,0);
            notification.setContentIntent(pendingIntent);
            notification.setAutoCancel(true);
            notification.setSound(alarmSound);
            manager.notify(1, notification.build());
        }else{
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder notification = new Notification.Builder(context);
            notification.setContentTitle("CMRITFeed");
            notification.setContentText("We have got new events, click here");
            Intent intent1 = new Intent(context,Home.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,0);
            notification.setContentIntent(pendingIntent);
            notification.setSound(alarmSound);
            manager.notify(0,notification.build());
        }
    }
}
