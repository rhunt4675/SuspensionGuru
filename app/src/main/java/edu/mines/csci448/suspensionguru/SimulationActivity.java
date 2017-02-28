package edu.mines.csci448.suspensionguru;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SimulationActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SimulationFragment.newInstance();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SimulationActivity.class);
    }
}
