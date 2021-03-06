package edu.mines.csci448.suspensionguru;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import edu.mines.csci448.suspensionguru.db.DBHelper;

public class MainActivity extends SingleFragmentActivity {
    /**
     * Lifecycle event.
     * @param savedInstanceState SavedInstance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper.initDB(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    /**
     * Get fragment.
     *
     * @return MainFragment
     */
    @Override
    protected Fragment createFragment() {
        return MainFragment.newInstance();
    }
}
