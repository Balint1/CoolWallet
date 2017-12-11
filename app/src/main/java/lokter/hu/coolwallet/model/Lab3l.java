package lokter.hu.coolwallet.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Balint on 2017. 10. 03..
 */
@Table(name = "Labels")
public class Lab3l extends Model implements Serializable{

    @Column(name = "Name")
    private String name;

    public Lab3l(){
        super();
    }

    public Lab3l(String name) {
        super();
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

}
