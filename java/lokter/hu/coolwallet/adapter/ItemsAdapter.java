package lokter.hu.coolwallet.adapter;

/**
 * Created by barth on 2017. 10. 03..
 */


import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;

import org.joda.time.format.DateTimeFormat;

import java.util.Collections;
import java.util.List;

import lokter.hu.coolwallet.R;
import lokter.hu.coolwallet.fragments.RecentItemsFragment;
import lokter.hu.coolwallet.model.Item;
import lokter.hu.coolwallet.model.Lab3l;
import lokter.hu.coolwallet.model.Repository;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    public List<Item> itemsList;
    private RecentItemsFragment fragment;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, lab3l, amount,date;
        public View  viewBackground,viewForeground;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.itemTitle);
            lab3l = (TextView) view.findViewById(R.id.itemLabel);
            amount = (TextView) view.findViewById(R.id.itemAmount);
            date = (TextView) view.findViewById(R.id.itemDate);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public ItemsAdapter(RecentItemsFragment fragment) {
        this.fragment = fragment;
        loadItems();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Item item = itemsList.get(position);
        holder.title.setText(item.getTitle());
        holder.lab3l.setText(item.getLabel().getName());
        holder.amount.setText("" + item.getAmount());
        holder.date.setText(DateTimeFormat.forPattern("yyyy MMM dd").print(item.getDate()));
        if(item.getAmount() <0 )
            holder.amount.setTextColor(fragment.getResources().getColor(R.color.colorExpense));
        else
            holder.amount.setTextColor(fragment.getResources().getColor(R.color.colorIncome));



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.menu_item);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (R.id.delete == item.getItemId()) {
                            removeItem(position);
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }


    public void removeItem(int position) {
        final Item deletedItem = itemsList.get(position);
        final int deletedIndex = position;

        // remove the item from recycler view
        itemsList.get(position).delete();
        itemsList.remove(position);

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar
                .make(fragment.getView(), deletedItem.getTitle() + " " + fragment.getString(R.string.removed), Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                restoreItem(deletedItem, deletedIndex);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item, int position) {
        Repository.addItem(item.getTitle(),item.getLabel().getName(),item.getAmount(),item.getDate());
        itemsList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
    public void loadItems()
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
    }

    private void loadLabel(Lab3l label)
    {
        itemsList = new Select()
                .from(Item.class).where("Label = ? ",label.getId())
                .execute();
        Collections.sort(itemsList);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void addItem(Item item)
    {
        itemsList.add(item);
        Collections.sort(itemsList);
        notifyDataSetChanged();
    }
    public void setFilter(Lab3l label)
    {
        if(label == null)
           loadItems();
        else
            loadLabel(label);
    }


}
