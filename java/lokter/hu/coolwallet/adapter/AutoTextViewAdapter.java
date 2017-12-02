package lokter.hu.coolwallet.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lokter.hu.coolwallet.model.Lab3l;
import lokter.hu.coolwallet.model.Repository;

/**
 * Created by Balint on 2017. 10. 04..
 */

public class AutoTextViewAdapter {
    private AutoCompleteTextView tv;
    private List<Lab3l> content;
    Map<Integer,Lab3l> map = new HashMap<>();
    private List<String> stringList = new ArrayList<>();
    public AutoTextViewAdapter(Context context,AutoCompleteTextView tv) {
        this.tv = tv;
        this.content = content;
        content = Repository.getLabels();
        loadData();
        fillSpinner(context);
    }

    private void fillSpinner(Context context) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, stringList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        tv.setAdapter(dataAdapter);
    }

    private void loadData() {
        int i = 0;
        for (Lab3l label :content) {
            stringList.add(label.getName());
            map.put(i,label);
            i++;
        }
    }

    public Lab3l getSelectedLabel(int index){
        return map.get(index);
    }


}
