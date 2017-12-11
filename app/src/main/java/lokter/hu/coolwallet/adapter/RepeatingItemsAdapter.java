package lokter.hu.coolwallet.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lokter.hu.coolwallet.R;
import lokter.hu.coolwallet.model.RepeatingItem;
import lokter.hu.coolwallet.model.Repository;

/**
 * Created by Balint on 2017. 12. 04..
 */

public class RepeatingItemsAdapter extends RecyclerView.Adapter<RepeatingItemsAdapter.MyViewHolder> {

    public List<RepeatingItem> itemsList = new ArrayList<>();
    private AppCompatActivity activity;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public Button delBtn;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.rep_item_name_tv);
            delBtn = (Button) view.findViewById(R.id.DeleteButton);
        }
    }


    public RepeatingItemsAdapter(AppCompatActivity a) {
        itemsList = Repository.getRepeatingItems();
        activity = a;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repeating_item_row, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RepeatingItem item = itemsList.get(position);
        holder.title.setText(item.getName());

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                notifyItemRemoved(position);
            }
        });
    }


    public void removeItem(int position) {
        final RepeatingItem deletedItem = itemsList.get(position);
        deletedItem.deleteAlarm(activity.getApplicationContext());
        deletedItem.delete();
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


  /*  public void loadItems()
    {
        itemsList = new Select()
                .from(Item.class)
                .execute();
        Log.i("LIST","Items size :" + itemsList.size());
        for (int i = 0; i <itemsList.size();i++) {
            Log.i("LIST","" + itemsList.get(i).date);
        }
        Collections.sort(itemsList);

        notifyDataSetChanged();
    }*/



    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void addItem(RepeatingItem item)
    {
        item.save();
        itemsList.add(item);
        notifyDataSetChanged();
    }

}
