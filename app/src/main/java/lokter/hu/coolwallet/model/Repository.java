package lokter.hu.coolwallet.model;

import android.util.Log;

import com.activeandroid.query.Select;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Balint on 2017. 10. 04..
 */

public class Repository {
    private int startingAmount = 100000;
    private static List<Lab3l> labels = null;

    private static void loadLabels() {
        Log.i("database", "Label-ek betöltése");
        labels = new Select()
                .from(Lab3l.class)
                .execute();

    }

    public static void AddLbel(Lab3l l) {
        if (l != null) {
            Log.i("database", "Label hozzáadása");
            labels.add(l);
            l.save();
        } else {
            throw new RuntimeException("A label értéke null");
        }
    }

    public static List<Lab3l> getLabels() {
        if(labels == null)
            loadLabels();
        return labels;
    }
    public static List<Item> getItems(){
        Log.i("database", "Item-ek betöltése");
        return new Select()
                .from(Item.class)
                .orderBy("Date")
                .execute();
    }
    public static List<Item> getItems(Lab3l filter){
        Log.i("database", "Item-ek betöltése");
        return new Select()
                .from(Item.class)
                .where("Label = ? ",filter.getId())
                .orderBy("Date")
                .execute();
    }
    public static void addItem(Item i){

        addItem(i.getTitle(),i.getLabel().getName(),i.getAmount(),i.getDate());
        //i.save();
    }



    public static void addItem(String title, String labelName, int amount,DateTime date){
        boolean newLabel = true;
        Lab3l label = new Lab3l();
        if(labelName.equals(""))
            labelName = "Other";
        if(labels == null)
        loadLabels();
        for (Lab3l l:labels) {
            if(l.getName().equals(labelName))
            {newLabel = false;
            label= l;
            }
        }

        if(newLabel)
        {
            label = new Lab3l(labelName);
            label.save();
            labels.add(label);
        }

        Item item = new Item(title,label,amount);
        item.setDate(date);
        item.save();
    }
    public static List<Item> getMonth(int year, int month)
    {
        DateTime start = new DateTime(year,month,1,0,0);
        DateTime end = start.plusMonths(1);


        return new Select()
                .from(Item.class).where("Date > ? AND Date < ?",start.getMillis(),end.getMillis())
                .execute();
    }
    public static List<Item> between(DateTime start, DateTime end)
    {

        return new Select()
                .from(Item.class).where("Date > ? AND Date < ?",start.getMillis(),end.getMillis())
                .execute();
    }
    public static List<Item> between(DateTime start, DateTime end,Lab3l filter)
    {

        return new Select()
                .from(Item.class).where("Date > ? AND Date < ? AND Label = ?",start.getMillis(),end.getMillis(),filter.getId())
                .execute();
    }
    public static int[] balance(DateTime start,DateTime end){
        int income = 0;
        int expense = 0;
        List<Item> incomeItems = new Select().from(Item.class).where("Amount > 0 AND Date > ? AND Date < ?",start.getMillis(),end.getMillis()).execute();
        List<Item> expenseItems = new Select().from(Item.class).where("Amount < 0 AND Date > ? AND Date < ?",start.getMillis(),end.getMillis()).execute();
        for (Item item:incomeItems) {
            income+= item.getAmount();
        }
        for (Item item:expenseItems) {
            expense+= item.getAmount();
        }

        return new int[]{income,expense};
    }
    public static int currentAmount(DateTime date){
        List<Item> items = new Select().from(Item.class).where("Date < ?",date.getMillis()).execute();
        int sum = 0;
        for(Item item:items)
            sum+=item.getAmount();
        return sum;
    }

    public static void addLabel(Lab3l label) {
        label.save();
    }

    public static List<RepeatingItem> getRepeatingItems(){
        Log.i("database", "Item-ek betöltése");
        return new Select()
                .from(RepeatingItem.class)
                .execute();
    }
}
