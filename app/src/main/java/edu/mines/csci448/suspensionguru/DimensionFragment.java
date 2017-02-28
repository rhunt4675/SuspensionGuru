package edu.mines.csci448.suspensionguru;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class DimensionFragment extends Fragment {
    private EditText _param1, _param2, _param3, _param4;

    public static DimensionFragment newInstance() {
        return new DimensionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dimension, container, false);

        /* Wire up Help Buttons */
        ImageButton param1Help = (ImageButton) view.findViewById(R.id.fragment_dimension_parameter1Help);
        param1Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton param2Help = (ImageButton) view.findViewById(R.id.fragment_dimension_parameter2Help);
        param2Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton param3Help = (ImageButton) view.findViewById(R.id.fragment_dimension_parameter3Help);
        param3Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton param4Help = (ImageButton) view.findViewById(R.id.fragment_dimension_parameter4Help);
        param4Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });

        /* Wire up Input Parameter Fields */
        _param1 = (EditText) view.findViewById(R.id.fragment_dimension_parameter1Text);
        _param2 = (EditText) view.findViewById(R.id.fragment_dimension_parameter2Text);
        _param3 = (EditText) view.findViewById(R.id.fragment_dimension_parameter3Text);
        _param4 = (EditText) view.findViewById(R.id.fragment_dimension_parameter4Text);

        /* Wire up Refresh Button */
        Button refresh = (Button) view.findViewById(R.id.fragment_dimension_refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            }
        });

        /* Setup Up Navigation */
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        return view;
    }

}
