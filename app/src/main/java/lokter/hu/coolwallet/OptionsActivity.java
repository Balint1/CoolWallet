package lokter.hu.coolwallet;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OptionsActivity extends AppCompatActivity {

    public static final String STARTING_AMOUNT = "starting_amount";
    public static final String ANIMATION = "animation";
    @BindView(R.id.saveButton)
    ImageButton saveButton;
    @BindView(R.id.startingEditText)
    EditText startingEditText;
    @BindView(R.id.animateSwitch)
    Switch animateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        int starting = 0;
        starting = settings.getInt(STARTING_AMOUNT,starting);
        startingEditText.setText("" + starting);
        boolean anim = true;
        anim = settings.getBoolean(ANIMATION,anim);
        animateSwitch.setChecked(anim);


    }

    @OnClick(R.id.saveButton)
    public void onViewClicked() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        if(!startingEditText.getText().toString().isEmpty())
        editor.putInt(STARTING_AMOUNT,Integer.parseInt(startingEditText.getText().toString()));
        editor.putBoolean(ANIMATION,animateSwitch.isChecked());
        editor.commit();
        finish();
    }

    @Override
    public void onBackPressed(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        onViewClicked();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.save_confirmation).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }

}
