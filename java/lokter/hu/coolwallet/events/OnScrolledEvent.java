package lokter.hu.coolwallet.events;

/**
 * Created by Balint on 2017. 12. 02..
 */

public class OnScrolledEvent {
    private int page;

    public OnScrolledEvent(int page){
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
