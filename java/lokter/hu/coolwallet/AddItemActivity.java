package lokter.hu.coolwallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lokter.hu.coolwallet.adapter.AutoTextViewAdapter;
import lokter.hu.coolwallet.events.ItemSetChangedEvent;
import lokter.hu.coolwallet.model.Item;
import lokter.hu.coolwallet.model.Lab3l;
import lokter.hu.coolwallet.model.Repository;

import static java.lang.Math.abs;

public class AddItemActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {


    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    public static final String CREATE_TYPE = "create_type";
    public static final String REPEATING_ITEM = "repeating_item";
    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextAmount)
    EditText editTextAmount;
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.editTextDate)
    EditText editTextDate;
    @BindView(R.id.editTextTime)
    EditText editTextTime;
    @BindView(R.id.saveButton)
    Button saveButton;
    @BindView(R.id.textView_title_addItem)
    TextView tvTitleAddItem;
    @BindView(R.id.textView6)
    TextView textViewDate;
    @BindView(R.id.textView7)
    TextView textViewTime;
    private String type;

    private DateTime selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra(CREATE_TYPE);
        if (type.equals(REPEATING_ITEM)) {
            editTextDate.setVisibility(View.INVISIBLE);
            editTextTime.setVisibility(View.INVISIBLE);
            textViewDate.setVisibility(View.INVISIBLE);
            textViewTime.setVisibility(View.INVISIBLE);

        }
        tvTitleAddItem.setText(String.format(getString(R.string.add_item), typeToString()));
        selectedDate = DateTime.now();
        editTextDate.setText(DateTimeFormat.forPattern("yyyy.MM.dd").print(selectedDate));
        editTextTime.setText(DateTimeFormat.forPattern("HH:mm").print(selectedDate));

        AutoTextViewAdapter atvAdapter = new AutoTextViewAdapter(AddItemActivity.this, autoCompleteTextView);

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    autoCompleteTextView.showDropDown();
            }
        });

    }

    @OnClick({R.id.editTextDate, R.id.editTextTime, R.id.saveButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editTextDate:
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddItemActivity.this);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
                break;
            case R.id.editTextTime:
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(AddItemActivity.this);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
                break;
            case R.id.saveButton:
                saveClick();
                break;
        }
    }

    @OnClick(R.id.autoCompleteTextView)
    public void onViewClicked() {
        autoCompleteTextView.showDropDown();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        editTextDate.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth));
        selectedDate.year().setCopy(year);
        selectedDate.monthOfYear().setCopy(monthOfYear);
        selectedDate.dayOfMonth().setCopy(dayOfMonth);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        editTextTime.setText(getString(R.string.radial_time_picker_result_value, hourOfDay, minute));
        selectedDate.hourOfDay().setCopy(hourOfDay);
        selectedDate.minuteOfHour().setCopy(minute);
    }

    private void saveClick() {
        boolean ret = false;
        if (isEmpty(editTextTitle)) {
            editTextTitle.setError(getString(R.string.empty_editText));
            ret = true;
        }
        if (isEmpty(editTextAmount)) {
            editTextAmount.setError(getString(R.string.empty_editText));
            ret = true;
        }
        if (ret) return;
        //AutoCompleteTextView atv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        String dateString = editTextDate.getText().toString() + " " + editTextTime.getText().toString();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy.MM.dd HH:mm");
        int amount = Integer.parseInt(editTextAmount.getText().toString());
        if (!type.equals(REPEATING_ITEM))
            amount = abs(amount);
        if (type.equals("expense"))
            amount = amount * -1;
        if (type.equals(REPEATING_ITEM)) {
            boolean newLabel = true;
            String labelName = autoCompleteTextView.getText().toString();
            Lab3l label;
            label = new Lab3l(labelName);
            Intent returnIntent = new Intent();
            Item item = new Item(editTextTitle.getText().toString(), label, amount, formatter.parseDateTime(dateString));
            returnIntent.putExtra("REP_ITEM", item);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Repository.addItem(editTextTitle.getText().toString(), autoCompleteTextView.getText().toString(), amount, formatter.parseDateTime(dateString));
            EventBus.getDefault().post(new ItemSetChangedEvent(ItemSetChangedEvent.CREATED));
            finish();
        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    public String typeToString() {
        switch (type) {
            case "income":
                return getString(R.string.income);
            case "expense":
                return getString(R.string.expense);
            case REPEATING_ITEM:
                return getString(R.string.repeating_item);
        }
        return "";
    }
}
