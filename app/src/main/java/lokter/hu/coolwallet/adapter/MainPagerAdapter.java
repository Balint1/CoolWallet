package lokter.hu.coolwallet.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.joda.time.DateTime;

import lokter.hu.coolwallet.fragments.DiagramFragment;
import lokter.hu.coolwallet.fragments.RecentItemsFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        DateTime start = new DateTime(DateTime.now().year().get(),DateTime.now().monthOfYear().get(),1,0,0);
        DateTime end = start.plusMonths(1);
        switch (position) {
            case 0:
                return new RecentItemsFragment();
            case 1:
                DiagramFragment diagramFragment = new DiagramFragment();
                Bundle args = new Bundle();
                args.putSerializable(DiagramFragment.DIAGRAM_TYPE,DiagramFragment.DIAGRAM_MONTHLY);
                args.putInt(DiagramFragment.DIAGRAM_POS,1);

                diagramFragment.setArguments(args);
                return diagramFragment;
            case 2:
                DiagramFragment diagramFragment1 = new DiagramFragment();
                Bundle args1 = new Bundle();
                //args1.putString(DiagramFragment.STARTING_DATE,"YEAR");
                args1.putSerializable(DiagramFragment.DIAGRAM_TYPE,DiagramFragment.DIAGRAM_YEARLY);
                args1.putInt(DiagramFragment.DIAGRAM_POS,2);

                diagramFragment1.setArguments(args1);
                return diagramFragment1;
            default:
                return new RecentItemsFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}