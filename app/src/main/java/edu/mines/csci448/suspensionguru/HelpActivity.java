package edu.mines.csci448.suspensionguru;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

public class HelpActivity extends SingleFragmentActivity {
    private static final String INTENT_HELP_MESSAGE = "helpMessage";

    /**
     * Get the fragment.
     *
     * @return HelpFragment
     */
    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        int resId = intent.getIntExtra(INTENT_HELP_MESSAGE, -1);
        return HelpFragment.newInstance(resId);
    }

    /**
     * Start this activity.
     *
     * @param context Context
     * @param helpMessage HelpMessage
     * @return Intent
     */
    public static Intent getIntent(Context context, @StringRes int helpMessage) {
        Intent intent = new Intent(context, HelpActivity.class);
        intent.putExtra(INTENT_HELP_MESSAGE, helpMessage);
        return intent;
    }
}
