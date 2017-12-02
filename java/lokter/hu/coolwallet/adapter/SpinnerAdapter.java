package lokter.hu.coolwallet.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lokter.hu.coolwallet.model.Lab3l;
import lokter.hu.coolwallet.model.Repository;

import static android.R.layout.simple_spinner_item;

/**
 * Created by Balint on 2017. 10. 04..
 */

public class SpinnerAdapter {
    private Spinner spinner;
    private List<Lab3l> content;
    Map<Integer,Lab3l> map = new HashMap<>();
    private Context context;
    private String extra;
    private List<String> stringList = new ArrayList<>();
    public SpinnerAdapter(Context context,Spinner spinner, String extra_label) {
        this.spinner = spinner;
        this.content = content;
        extra = extra_label;
        this.context = context;
        content = Repository.getLabels();
    }

    private void fillSpinner(Context context) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, simple_spinner_item, stringList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void loadData() {
        stringList.clear();
        map.clear();
        int i = 1;
        stringList.add(extra);
        map.put(0,null);
        for (Lab3l label :content) {
            stringList.add(label.getName());
           map.put(i,label);
            i++;
        }
    }

    public Lab3l getSelectedLabel(int index){
        return map.get(index);
    }
    public void refresh()
    {
        loadData();
        fillSpinner(context);

    }

}