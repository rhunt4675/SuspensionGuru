package edu.mines.csci448.suspensionguru;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class DimensionActivity extends SingleFragmentActivity {
    private static final String VEHICLE_INTENT_EXTRA = "vehicle_intent";
    private static final String SETUP_INTENT_EXTRA = "setup_intent";

    /**
     * Get fragment.
     *
     * @return DimensionFragment
     */
    @Override
    protected Fragment createFragment() {
        // Extract the Target Vehicle & Setup
        Intent intent = getIntent();
        String vehicleName = intent.getStringExtra(VEHICLE_INTENT_EXTRA);
        String setupName = intent.getStringExtra(SETUP_INTENT_EXTRA);

        // Create the Fragment
        return DimensionFragment.newInstance(vehicleName, setupName);
    }

    /**
     * Start this activity.
     *
     * @param context Context
     * @param vehicleName Vehicle
     * @param setupName Setup
     * @return Intent
     */
    public static Intent getIntent(Context context, String vehicleName, String setupName) {
        Intent intent =  new Intent(context, DimensionActivity.class);
        intent.putExtra(VEHICLE_INTENT_EXTRA, vehicleName);
        intent.putExtra(SETUP_INTENT_EXTRA, setupName);
        return intent;
    }
}
