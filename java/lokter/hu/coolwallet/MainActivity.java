package lokter.hu.coolwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import lokter.hu.coolwallet.adapter.MainPagerAdapter;
import lokter.hu.coolwallet.fragments.RepeatingItemsFragment;

import static lokter.hu.coolwallet.R.id.vpMain;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(vpMain)
    ViewPager viewPager;

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));


        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       switch (id){
           case R.id.settings:
               Intent intent = new Intent(this, OptionsActivity.class);
               startActivityForResult(intent,100);
               break;
           case R.id.planned_expenses:
               Fragment plannedFragment = new RepeatingItemsFragment();
               FragmentTransaction t = getSupportFragmentManager().beginTransaction();
               t.replace(R.id.drawer_layout,plannedFragment);
               t.addToBackStack(null);
               t.commit();
               break;

       }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Snackbar.make((ViewPager) findViewById(R.id.vpMain), R.string.settings_saved,Snackbar.LENGTH_SHORT).show();
        }
    }*/
}
