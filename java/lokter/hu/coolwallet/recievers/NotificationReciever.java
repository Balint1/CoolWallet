package lokter.hu.coolwallet.recievers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import lokter.hu.coolwallet.RepeatingItemNotification;
import lokter.hu.coolwallet.events.ItemSetChangedEvent;
import lokter.hu.coolwallet.model.Item;
import lokter.hu.coolwallet.model.Lab3l;
import lokter.hu.coolwallet.model.Repository;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Balint on 2017. 12. 05..
 */

public class NotificationReciever extends BroadcastReceiver {
    private static final int NotificationID = 3456;
    public static final int MONTHLY = 101;
    public static final int WEEKLY= 102;
    public static final int DAILY = 103;
    public static final int SECOND = 104;


    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("TITLE");
        String label = intent.getStringExtra("LABEL");
        int amount = intent.getIntExtra("AMOUNT",1);
        int id = intent.getIntExtra("ID",1);
        int interval = intent.getIntExtra("INTERVAL",1);
        int freq = intent.getIntExtra("FREQ",1);
        DateTime startingTime = new DateTime(intent.getLongExtra("TIME",1));


        switch (interval){
            case DAILY:
                startingTime = startingTime.plusDays(freq);
                break;
            case WEEKLY:
                startingTime = startingTime.plusWeeks(freq);
                break;
            case MONTHLY:
                startingTime = startingTime.plusMonths(freq);
                break;
            case SECOND:
                startingTime = startingTime.plusSeconds(freq);
                break;

        }


        intent.putExtra("TIME",startingTime.getMillis());
       PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
       AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
       alarmManager.set(AlarmManager.RTC_WAKEUP, startingTime.getMillis(),pendingIntent);

        Repository.addItem(title,label,amount, DateTime.now());
        Item item = new Item(title,new Lab3l(label),amount, DateTime.now());

        EventBus.getDefault().post(new ItemSetChangedEvent(ItemSetChangedEvent.CREATED));

        RepeatingItemNotification.notify(context,item);
    }
}
