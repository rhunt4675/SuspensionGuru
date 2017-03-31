package edu.mines.csci448.suspensionguru;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SetupActivity extends SingleFragmentActivity {
    private static final String VEHICLE_INTENT_EXTRA = "vehicle";
    private static final String SETUP_INTENT_EXTRA = "setup";

    @Override
    protected Fragment createFragment() {
        // Extract the Target Vehicle & Setup
        Intent intent = getIntent();
        String vehicleName = intent.getStringExtra(VEHICLE_INTENT_EXTRA);
        String setupName = intent.getStringExtra(SETUP_INTENT_EXTRA);

        // Create the Fragment
        return SetupFragment.newInstance(vehicleName, setupName);
    }

    public static Intent getIntent(Context context, String vehicleName, String setupName) {
        Intent intent =  new Intent(context, SetupActivity.class);
        intent.putExtra(VEHICLE_INTENT_EXTRA, vehicleName);
        intent.putExtra(SETUP_INTENT_EXTRA, setupName);
        return intent;
    }
}
