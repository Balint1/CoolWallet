package lokter.hu.coolwallet.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lokter.hu.coolwallet.AddItemActivity;
import lokter.hu.coolwallet.R;
import lokter.hu.coolwallet.model.Item;
import lokter.hu.coolwallet.model.RepeatingItem;
import lokter.hu.coolwallet.recievers.NotificationReciever;

import static android.app.Activity.RESULT_OK;

public class NewRepeatingItemDialogFragment extends AppCompatDialogFragment implements CalendarDatePickerDialogFragment.OnDateSetListener {

    public static final String TAG = "NewRepeatingItemDialogFragment";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    @BindView(R.id.dialog_title)
    EditText dialogTitle;
    @BindView(R.id.date_edit_text)
    EditText dateEditText;
    @BindView(R.id.interval_spinner)
    Spinner intervalSpinner;
    @BindView(R.id.freq_edit_text)
    EditText freqEditText;
    @BindView(R.id.time_spinner)
    Spinner timeSpinner;
    Unbinder unbinder;

    @BindView(R.id.rep_save)
    Button repSave;

    private DateTime selectedDate = DateTime.now();
    private Item item = null;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.date_edit_text)
    public void onViewClicked() {
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this);
        cdp.show(getActivity().getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }


    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        dateEditText.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth));
        selectedDate.year().setCopy(year);
        selectedDate.monthOfYear().setCopy(monthOfYear);
        selectedDate.dayOfMonth().setCopy(dayOfMonth);
        if(selectedDate.getMillis() <DateTime.now().getMillis()){
            selectedDate = DateTime.now();
            dateEditText.setText(getString(R.string.calendar_date_picker_result_values, selectedDate.getYear(), selectedDate.getMonthOfYear(), selectedDate.getDayOfMonth()));
        }
    }

    @OnClick(R.id.rep_save)
    public void onSaveClicked() {
        Intent intent;
        intent = new Intent(getActivity(), AddItemActivity.class);
        intent.putExtra(AddItemActivity.CREATE_TYPE, AddItemActivity.REPEATING_ITEM);

        startActivityForResult(intent,101);
    }


    public interface INewRepeatingItemDialogListener {
        void onRepeatingItemCreated(RepeatingItem newItem, Item item);
    }

    private INewRepeatingItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof INewRepeatingItemDialogListener) {
            listener = (INewRepeatingItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the INewShoppingItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_repeating)
                .setView(getContentView())
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        listener.onRepeatingItemCreated(createRepItem(), item);


                    }
                })
                .setNegativeButton(R.string.str_cancel, null)
                .create();
    }

    private RepeatingItem createRepItem() {


        if (freqEditText.getText().length() == 0)
            freqEditText.setText("1");
        RepeatingItem rep = null;
        if(item != null){
            int interval = getInterval();
            rep = new RepeatingItem(getStartingTime(interval), dialogTitle.getText().toString(), interval, Math.abs(Integer.parseInt(freqEditText.getText().toString())));
            if (rep.getName().length() == 0)
                rep.setName("Repeating item" + " " + rep.getAlarmId());
            rep.setItemAmount(item.getAmount());
            rep.setItemLabel(item.getLabel().getName());
            rep.setItemTitle(item.getTitle());

        }
        return rep;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_repeating_item, null);
        unbinder = ButterKnife.bind(this, contentView);
        List<String> times;
        times = new ArrayList<>();
        times.add(getString(R.string.morning));
        times.add(getString(R.string.south));
        times.add(getString(R.string.afternoon));
        times.add(getString(R.string.evening));
        times.add(getString(R.string.now));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, times);
        timeSpinner.setAdapter(adapter);
        List<String> intervals;
        intervals = new ArrayList<>();
        intervals.add(getString(R.string.daily));
        intervals.add(getString(R.string.weekly));
        intervals.add(getString(R.string.time_monthly));
        intervals.add(getString(R.string.second));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, intervals);
        intervalSpinner.setAdapter(adapter1);
        intervalSpinner.setSelection(2);
        freqEditText.setText("1");
        dateEditText.setText(DateTimeFormat.forPattern("yyyy.MM.dd").print(selectedDate));

        return contentView;
    }

    private boolean isValid() {
        boolean ret = true;
        if (dialogTitle.getText().length() == 0) {
            dialogTitle.setError(getString(R.string.empty_editText));
            ret = false;
        }
        return ret;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("REPEATING_ITEM","actres");

        if(resultCode == RESULT_OK){
            item = (Item) data.getSerializableExtra("REP_ITEM");
            Log.i("REPEATING_ITEM",item.getLabel().getName());
        }

    }

    private int getInterval(){
        switch (intervalSpinner.getSelectedItemPosition()){
            case 0:
                return NotificationReciever.DAILY;
            case 1:
                return NotificationReciever.WEEKLY;
            case 2:
                return NotificationReciever.MONTHLY;
            case 3:
                return NotificationReciever.SECOND;
        }
        return NotificationReciever.MONTHLY;
    }

    private long getStartingTime(int interval){
        int hour = 0;
        switch (timeSpinner.getSelectedItemPosition()){
            case 0:
                hour = 8;
                break;
            case 1:
                hour = 12;
                break;
            case 2:
                hour = 16;
                break;
            case 3:
                hour = 20;
                break;
            case 4:
                selectedDate = DateTime.now().plus(1000 * 8);
                break;
        }
        if(hour != 0) {

            selectedDate = selectedDate.hourOfDay().setCopy(hour);
            selectedDate = selectedDate.minuteOfHour().setCopy(0);
            selectedDate = selectedDate.secondOfMinute().setCopy(0);
            if(selectedDate.getMillis()  < DateTime.now().getMillis() ){
                switch (interval) {
                    case NotificationReciever.DAILY:
                        selectedDate = selectedDate.plusDays(1);
                        break;
                    case NotificationReciever.WEEKLY:
                        selectedDate = selectedDate.plusWeeks(1);
                        break;
                    case NotificationReciever.MONTHLY:
                        selectedDate = selectedDate.plusMonths(1);
                        break;
                }
            }
        }
        return selectedDate.getMillis();
    }

}