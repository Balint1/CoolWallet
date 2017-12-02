package lokter.hu.coolwallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lokter.hu.coolwallet.R;

/**
 * Created by Balint on 2017. 12. 02..
 */

public class MainFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.app_bar_main, container, false);

    }
}
