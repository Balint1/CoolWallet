package lokter.hu.coolwallet.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;

import lokter.hu.coolwallet.recievers.NotificationReciever;

/**
 * Created by Balint on 2017. 12. 04..
 */
@Table(name = "RepeatingItems")
public class RepeatingItem extends Model implements Serializable{
    @Column(name = "StartingDate")
    private long startingDate;
    @Column(name = "Name")
    private String name;
    @Column(name = "Interval")
    private int interval;
    @Column(name = "Frequency")
    private int frequency;
    @Column(name = "ItemTitle")
    private String itemTitle;
    @Column(name = "ItemLabel")
    private String itemLabel;
    @Column(name = "ItemAmount")
    private int itemAmount;
    @Column(name = "AlarmId")
    private int alarmId;

    public static int maxAlarmId = -1;

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }
    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public RepeatingItem(long startingDate, String name, int interval, int frequency) {
        this.startingDate = startingDate;
        this.name = name;
        this.interval = interval;
        this.frequency = frequency;
        this.alarmId = nextAlarmId();
    }

    public void deleteAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,NotificationReciever.class);
        intent.putExtra("TITLE",itemTitle);
        intent.putExtra("LABEL",itemLabel);
        intent.putExtra("AMOUNT",itemAmount);
        intent.putExtra("ID",alarmId);
        intent.putExtra("INTERVAL",interval);
        intent.putExtra("FREQ",frequency);
        intent.putExtra("TIME",startingDate);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarmId,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            Log.e("NOTIFICATION", "AlarmManager update was not canceled. " + e.toString());
        }

    }

    public long getStartingDate() {
        return startingDate;
    }

    public String getName() {
        return name;
    }

    public int getInterval() {
        return interval;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public RepeatingItem() {super();}

    private static int nextAlarmId(){
            if(maxAlarmId <0)
            {
                List<RepeatingItem> repItems = Repository.getRepeatingItems();
                int maxId = -1;
                for (RepeatingItem repItem:repItems) {
                    if(repItem.getAlarmId() > maxId)
                        maxId = repItem.alarmId+1;
                }
                if(maxId == -1){
                    maxAlarmId = 0;
                return 0;}
                else {
                    maxAlarmId = maxId;
                return maxId;}
            }
            maxAlarmId+=1;
        return maxAlarmId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
