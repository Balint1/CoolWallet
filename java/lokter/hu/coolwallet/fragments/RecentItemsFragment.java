package lokter.hu.coolwallet.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lokter.hu.coolwallet.AddItemActivity;
import lokter.hu.coolwallet.R;
import lokter.hu.coolwallet.RecyclerItemTouchHelper;
import lokter.hu.coolwallet.adapter.ItemsAdapter;
import lokter.hu.coolwallet.adapter.SpinnerAdapter;
import lokter.hu.coolwallet.events.ItemSetChangedEvent;

/**
 * Created by Balint on 2017. 10. 03..
 */

public class RecentItemsFragment extends Fragment implements AdapterView.OnItemSelectedListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    private static final int REQUEST_NEW_ITEM_CODE = 100;

    @BindView(R.id.label_spinner)
    Spinner labelSpinner;
    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.subFloatingMenuExpense)
    FloatingActionButton subFloatingMenuExpense;
    @BindView(R.id.subFloatingMenuIncome)
    FloatingActionButton subFloatingMenuIncome;
    @BindView(R.id.FloatingActionMenu1)
    FloatingActionMenu FloatingActionMenu1;
    private ItemsAdapter mAdapter;

    SpinnerAdapter spinnerAdapter;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recent_items, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mAdapter = new ItemsAdapter(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);



        spinnerAdapter = new SpinnerAdapter(getContext(), labelSpinner, getString(R.string.al_items));
        labelSpinner.setOnItemSelectedListener(this);
        spinnerAdapter.refresh();
        EventBus.getDefault().register(this);
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.subFloatingMenuExpense, R.id.subFloatingMenuIncome})
    public void onViewClicked(View view) {
                ActivityOptionsCompat options;
                Intent intent;
        switch (view.getId()) {
            case R.id.subFloatingMenuExpense:
                FloatingActionMenu1.close(true);
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        FloatingActionMenu1,
                        "create");
                intent = new Intent(getActivity(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.CREATE_TYPE, AddItemActivity.EXPENSE);

                startActivityForResult(intent, REQUEST_NEW_ITEM_CODE, options.toBundle());
                break;
            case R.id.subFloatingMenuIncome:
                FloatingActionMenu1.close(true);
                 options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                FloatingActionMenu1,
                                "create");
                 intent = new Intent(getActivity(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.CREATE_TYPE, AddItemActivity.INCOME);

                startActivity(intent, options.toBundle());
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mAdapter.setFilter(spinnerAdapter.getSelectedLabel(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ItemsAdapter.MyViewHolder) {
            mAdapter.removeItem( viewHolder.getAdapterPosition() );
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangedEvent(ItemSetChangedEvent event) {
        mAdapter.loadItems();
        spinnerAdapter.refresh();
        if(event.changeType.equals(ItemSetChangedEvent.CREATED))
        Snackbar.make(getView(), R.string.item_saved,Snackbar.LENGTH_SHORT).show();
    };
}