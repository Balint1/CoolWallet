package lokter.hu.coolwallet.events;


/**
 * Created by Balint on 2017. 12. 02..
 */

public class ItemSetChangedEvent {
   public static final String RESTORED = "restored";
   public static final String DELETED = "deleted";
   public static final String CREATED = "created";

    public String changeType;
    public ItemSetChangedEvent(String changeType){
        this.changeType = changeType;
    }
}
