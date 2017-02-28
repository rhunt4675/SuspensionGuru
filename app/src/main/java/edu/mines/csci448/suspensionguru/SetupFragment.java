package edu.mines.csci448.suspensionguru;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class SetupFragment extends Fragment {
    private EditText _vParam1, _vParam2, _vParam3, _vParam4, _sParam1, _sParam2, _sParam3, _sParam4;

    public static SetupFragment newInstance() {
        return new SetupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        /* Wire up Help Buttons */
        ImageButton vParam1Help = (ImageButton) view.findViewById(R.id.fragment_setup_vehicleParameter1Help);
        vParam1Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton vParam2Help = (ImageButton) view.findViewById(R.id.fragment_setup_vehicleParameter2Help);
        vParam2Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton vParam3Help = (ImageButton) view.findViewById(R.id.fragment_setup_vehicleParameter3Help);
        vParam3Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton vParam4Help = (ImageButton) view.findViewById(R.id.fragment_setup_vehicleParameter4Help);
        vParam4Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton sParam1Help = (ImageButton) view.findViewById(R.id.fragment_setup_setupParameter1Help);
        sParam1Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton sParam2Help = (ImageButton) view.findViewById(R.id.fragment_setup_setupParameter2Help);
        sParam2Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton sParam3Help = (ImageButton) view.findViewById(R.id.fragment_setup_setupParameter3Help);
        sParam3Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton sParam4Help = (ImageButton) view.findViewById(R.id.fragment_setup_setupParameter4Help);
        sParam4Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });

        /* Wire up Input Parameter Fields */
        _vParam1 = (EditText) view.findViewById(R.id.fragment_setup_vehicleParameter1Text);
        _vParam2 = (EditText) view.findViewById(R.id.fragment_setup_vehicleParameter2Text);
        _vParam3 = (EditText) view.findViewById(R.id.fragment_setup_vehicleParameter3Text);
        _vParam4 = (EditText) view.findViewById(R.id.fragment_setup_vehicleParameter4Text);
        _sParam1 = (EditText) view.findViewById(R.id.fragment_setup_setupParameter1Text);
        _sParam2 = (EditText) view.findViewById(R.id.fragment_setup_setupParameter2Text);
        _sParam3 = (EditText) view.findViewById(R.id.fragment_setup_setupParameter3Text);
        _sParam4 = (EditText) view.findViewById(R.id.fragment_setup_setupParameter4Text);

        return view;
    }

}
