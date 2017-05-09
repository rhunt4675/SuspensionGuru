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

import java.util.Locale;

import edu.mines.csci448.suspensionguru.data.Setup;
import edu.mines.csci448.suspensionguru.data.SuspensionDimension;
import edu.mines.csci448.suspensionguru.data.Vehicle;

public class SimulationFragment extends Fragment {
    private static final String VEHICLE_BUNDLE = "vehicle";
    private static final String SETUP_BUNDLE = "setup";

    private Vehicle _vehicle;
    private Setup _setup;
    private SuspensionDimension _suspensionDimension;
    private EditText _antiSquatEditText, _rollCenterEditText, _rollAxisEditText, _otherCalcEditText;

    // AntiSquat Calcs
    private double lowerLinkSlope = 0;
    private double lowerLinkIntercept = 0;
    private double upperLinkSlope = 0;
    private double upperLinkIntercept = 0;
    private double instantCenterX = 0;
    private double instantCenterZ = 0;
    private double antiSquatSlope = 0;
    private double antiSquatHeight = 0;      // Height of the 100% AS Line at x=instantCenterX
    private double antiSquatPercentage = 0;


    public static SimulationFragment newInstance(String vehicleName, String setupName) {
        SimulationFragment fragment = new SimulationFragment();

        // Pass arguments to Fragment
        Bundle arguments = new Bundle();
        arguments.putString(VEHICLE_BUNDLE, vehicleName);
        arguments.putString(SETUP_BUNDLE, setupName);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read Bundle
        if (getArguments() != null) {
            String vehicleName = getArguments().getString(VEHICLE_BUNDLE);
            String setupName = getArguments().getString(SETUP_BUNDLE);

            _vehicle = MainFragment._vehicles.get(vehicleName);
            _setup = MainFragment._setups.get(setupName);
            _suspensionDimension = SuspensionDimension.getSuspensionDimension(_vehicle, _setup, getContext());
        }

        // Enable Up Navigation
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simulation, container, false);

        /* Wire up Help Buttons */
        ImageButton antiSquatHelp = (ImageButton) view.findViewById(R.id.fragment_simulation_antiSquatHelp);
        antiSquatHelp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity(), R.string.fragment_help_text));
            }
        });
        ImageButton rollCenterHelp = (ImageButton) view.findViewById(R.id.fragment_simulation_rollCenterHelp);
        rollCenterHelp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity(), R.string.fragment_help_text));
            }
        });
        ImageButton rollAxisHelp = (ImageButton) view.findViewById(R.id.fragment_simulation_rollAxisHelp);
        rollAxisHelp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity(), R.string.fragment_help_text));
            }
        });
        ImageButton otherCalcHelp = (ImageButton) view.findViewById(R.id.fragment_simulation_otherCalcHelp);
        otherCalcHelp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity(), R.string.fragment_help_text));
            }
        });

        /* Wire up Input Parameter Fields */
        // TODO: Use Real Formulas, instead of these stubs.

        // CalculateAntiSquat
        lowerLinkSlope = ( (_suspensionDimension.getLowerFrameZ() - _suspensionDimension.getLowerAxleZ()) /
                           (_suspensionDimension.getLowerFrameX() - _suspensionDimension.getLowerAxleX()) );

        upperLinkSlope = ( (_suspensionDimension.getUpperFrameZ() - _suspensionDimension.getUpperAxleZ()) /
                           (_suspensionDimension.getUpperFrameX() - _suspensionDimension.getUpperAxleX()) );

        lowerLinkIntercept = _suspensionDimension.getLowerFrameZ() - (lowerLinkSlope * _suspensionDimension.getLowerFrameX());
        upperLinkIntercept = _suspensionDimension.getUpperFrameZ() - (upperLinkSlope * _suspensionDimension.getUpperFrameX());

        instantCenterX = (lowerLinkIntercept - upperLinkIntercept) / (upperLinkSlope - lowerLinkSlope);
        instantCenterZ = (lowerLinkSlope * instantCenterX) + lowerLinkIntercept;

        antiSquatSlope = _setup.getCenterOfGravityHeight() / _setup.getCenterOfGravityY(); // TODO Center of Gravity Y should be wheel base. Add variable later.
        antiSquatHeight = antiSquatSlope * instantCenterX;
        antiSquatPercentage = instantCenterZ / antiSquatHeight;

        // Display Results
        _antiSquatEditText = (EditText) view.findViewById(R.id.fragment_simulation_antiSquatText);
        _antiSquatEditText.setText(String.format(Locale.getDefault(), "%.1f", (antiSquatPercentage * 100)));
        _rollCenterEditText = (EditText) view.findViewById(R.id.fragment_simulation_rollCenterText);
        _rollCenterEditText.setText(String.format(Locale.getDefault(), "%.1f", Math.PI / (_setup.getCenterOfGravityHeight() == null ? 7.8 : _setup.getCenterOfGravityHeight())));
        _rollAxisEditText = (EditText) view.findViewById(R.id.fragment_simulation_rollAxisText);
        _rollAxisEditText.setText(String.format(Locale.getDefault(), "%.1f", Math.PI * (_setup.getMassVehicle() == null ? 85.2 : _setup.getMassVehicle())));
        _otherCalcEditText = (EditText) view.findViewById(R.id.fragment_simulation_otherCalcText);

        /* Wire up Refresh Button */
        Button refresh = (Button) view.findViewById(R.id.fragment_simulation_refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
