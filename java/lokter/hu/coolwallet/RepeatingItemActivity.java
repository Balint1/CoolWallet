package lokter.hu.coolwallet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lokter.hu.coolwallet.adapter.RepeatingItemsAdapter;
import lokter.hu.coolwallet.fragments.NewRepeatingItemDialogFragment;
import lokter.hu.coolwallet.model.Item;
import lokter.hu.coolwallet.model.RepeatingItem;
import lokter.hu.coolwallet.recievers.NotificationReciever;

public class RepeatingItemActivity extends AppCompatActivity
                implements NewRepeatingItemDialogFragment.INewRepeatingItemDialogListener {

    @BindView(R.id.repeating_recycler_view)
    RecyclerView repeatingRecyclerView;
    @BindView(R.id.add_repeating_item_fab)
    FloatingActionButton addRepeatingItemFab;
    RepeatingItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeating_item);
        ButterKnife.bind(this);
        mAdapter = new RepeatingItemsAdapter(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        repeatingRecyclerView.setLayoutManager(mLayoutManager);
        repeatingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        repeatingRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        repeatingRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.add_repeating_item_fab)
    public void onViewClicked() {
        new NewRepeatingItemDialogFragment().show(getSupportFragmentManager(), NewRepeatingItemDialogFragment.TAG);
    }

    @Override
    public void onRepeatingItemCreated(RepeatingItem repeatingItem, Item item) {
        if(item != null) {
            Intent intent = new Intent(getApplicationContext(),NotificationReciever.class);
            intent.putExtra("TITLE",item.getTitle());
            intent.putExtra("LABEL",item.getLabel().getName());
            intent.putExtra("AMOUNT",item.getAmount());
            intent.putExtra("ID",repeatingItem.getAlarmId());
            intent.putExtra("INTERVAL",repeatingItem.getInterval());
            intent.putExtra("FREQ",repeatingItem.getFrequency());
            intent.putExtra("TIME",repeatingItem.getStartingDate());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),repeatingItem.getAlarmId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, repeatingItem.getStartingDate(),pendingIntent);

            Toast.makeText(this, repeatingItem.getName() + " " + repeatingItem.getFrequency() + item.getTitle(), Toast.LENGTH_LONG).show();
            mAdapter.addItem(repeatingItem);
        }
        else{
            Toast.makeText(this,"Muszáj itemet megadni",Toast.LENGTH_LONG).show();
            Snackbar.make(repeatingRecyclerView,"Muszáj itemet megadni",Snackbar.LENGTH_SHORT);

        }
    }

}
