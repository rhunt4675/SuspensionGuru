package edu.mines.csci448.suspensionguru;


import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment extends Fragment {
    private static final String HELP_MESSAGE_BUNDLE = "helpMessageBundle";
    private int _helpMessageId = -1;

    /**
     * Create fragment.
     *
     * @param helpMessage Help message
     * @return Fragment
     */
    public static HelpFragment newInstance(@StringRes int helpMessage) {
        Bundle bundle = new Bundle();
        bundle.putInt(HELP_MESSAGE_BUNDLE, helpMessage);

        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Lifecycle event
     *
     * @param savedInstanceState Saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            _helpMessageId = getArguments().getInt(HELP_MESSAGE_BUNDLE);
        }
    }

    /**
     * Lifecycle event
     *
     * @param inflater Inflater
     * @param container Container
     * @param savedInstanceState Saved state
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        // Customize View
        TextView textView = (TextView) view.findViewById(R.id.fragment_help_helpText);
        textView.setText(_helpMessageId);

        return view;
    }

}
