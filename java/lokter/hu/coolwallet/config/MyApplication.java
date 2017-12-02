package lokter.hu.coolwallet.config;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Balint on 2017. 10. 03..
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}