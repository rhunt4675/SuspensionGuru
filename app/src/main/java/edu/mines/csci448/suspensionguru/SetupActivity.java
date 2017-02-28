package edu.mines.csci448.suspensionguru;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SetupActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SetupFragment.newInstance();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SetupActivity.class);
    }
}
