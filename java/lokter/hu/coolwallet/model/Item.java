package lokter.hu.coolwallet.model;

import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Balint on 2017. 10. 03..
 */
@Table(name = "Items")
public class Item extends Model implements Serializable,Comparable{
    @Column(name = "Title",notNull = true)
    private String title;
    @Column(name = "Label")
    private Lab3l label;
    @Column(name = "Amount", notNull = true)
    private int amount;
    @Column(name = "Desc")
    private String desc;
    @Column(name = "Date")
    public Long date;



    public Item() {
        super();
    }

    public Item(String title, Lab3l lab3l, int amount) {
        super();
        this.title = title;
        this.label = lab3l;
        this.amount = amount;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Lab3l getLabel() {
        return label;
    }

    public void setLabel(Lab3l label) {
        this.label = label;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public DateTime getDate() {return new DateTime(date);}

    public void setDate(DateTime date) {this.date = date.getMillis();}



    @Override
    public int compareTo(@NonNull Object o) {
        long dif = (this.date - ((Item) o).date);
        if(dif < 0) return 1;
        if(dif > 0) return -1;
        return 0;
    }
}