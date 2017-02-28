package edu.mines.csci448.suspensionguru;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class DimensionActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return DimensionFragment.newInstance();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, DimensionActivity.class);
    }
}
