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
    private double lowerLinkSlopeXZ = 0;
    private double lowerLinkZIntercept = 0;
    private double upperLinkSlopeXZ = 0;
    private double upperLinkZIntercept = 0;
    private double instantCenterX = 0;
    private double instantCenterZ = 0;
    private double antiSquatSlope = 0;
    private double antiSquatHeight = 0;      // Height of the 100% AS Line at x=instantCenterX
    private double antiSquatPercentage = 0;

    // Roll Center Height
    private double lowerLinkSlopeXY = 0;
    private double lowerLinkYIntercept = 0;
    private double upperLinkSlopeXY = 0;
    private double upperLinkYIntercept = 0;
    private double lowerLinkXIntersect = 0;
    private double lowerLinkZIntersect = 0;
    private double upperLinkXIntersect = 0;
    private double upperLinkZIntersect = 0;
    private double rollCenterSlopeXZ = 0;
    private double rollCenterZIntercept = 0;
    private double rollCenterHeight = 0;



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

        // CalculateAntiSquat
        lowerLinkSlopeXZ = ( (_suspensionDimension.getLowerFrameZ() - _suspensionDimension.getLowerAxleZ()) /
                           (_suspensionDimension.getLowerFrameX() - _suspensionDimension.getLowerAxleX()) );

        upperLinkSlopeXZ = ( (_suspensionDimension.getUpperFrameZ() - _suspensionDimension.getUpperAxleZ()) /
                           (_suspensionDimension.getUpperFrameX() - _suspensionDimension.getUpperAxleX()) );

        lowerLinkZIntercept = _suspensionDimension.getLowerFrameZ() - (lowerLinkSlopeXZ * _suspensionDimension.getLowerFrameX());
        upperLinkZIntercept = _suspensionDimension.getUpperFrameZ() - (upperLinkSlopeXZ * _suspensionDimension.getUpperFrameX());

        instantCenterX = (lowerLinkZIntercept - upperLinkZIntercept) / (upperLinkSlopeXZ - lowerLinkSlopeXZ);
        instantCenterZ = (lowerLinkSlopeXZ * instantCenterX) + lowerLinkZIntercept;

        antiSquatSlope = _setup.getCenterOfGravityHeight() / _setup.getCenterOfGravityY(); // TODO Center of Gravity Y should be wheel base. Add variable later.
        antiSquatHeight = antiSquatSlope * instantCenterX;
        antiSquatPercentage = instantCenterZ / antiSquatHeight;

        // Calculate Roll Center Height
        lowerLinkSlopeXY = ( (_suspensionDimension.getLowerFrameY() - _suspensionDimension.getLowerAxleY()) /
                (_suspensionDimension.getLowerFrameX() - _suspensionDimension.getLowerAxleX()) );

        upperLinkSlopeXY = ( (_suspensionDimension.getUpperFrameY() - _suspensionDimension.getUpperAxleY()) /
                (_suspensionDimension.getUpperFrameX() - _suspensionDimension.getUpperAxleX()) );

        lowerLinkYIntercept = _suspensionDimension.getLowerFrameY() - (lowerLinkSlopeXY * _suspensionDimension.getLowerFrameX());
        upperLinkYIntercept = _suspensionDimension.getUpperFrameY() - (upperLinkSlopeXY * _suspensionDimension.getUpperFrameX());

        lowerLinkXIntersect = (-1 * lowerLinkYIntercept) / lowerLinkSlopeXY;
        upperLinkXIntersect = (-1 * upperLinkYIntercept) / upperLinkSlopeXY;

        lowerLinkZIntersect = (lowerLinkSlopeXZ * lowerLinkXIntersect) + lowerLinkZIntercept;
        upperLinkZIntersect = (upperLinkSlopeXZ * upperLinkXIntersect) + upperLinkZIntercept;

        rollCenterSlopeXZ = ( (lowerLinkZIntersect - upperLinkZIntersect) /
                              (lowerLinkXIntersect - upperLinkXIntersect));

        rollCenterZIntercept = lowerLinkZIntersect - (rollCenterSlopeXZ * lowerLinkXIntersect);
        rollCenterHeight = rollCenterZIntercept;



        // Display Results
        _antiSquatEditText = (EditText) view.findViewById(R.id.fragment_simulation_antiSquatText);
        _antiSquatEditText.setText(String.format(Locale.getDefault(), "%.1f", (antiSquatPercentage * 100)));
        _rollCenterEditText = (EditText) view.findViewById(R.id.fragment_simulation_rollCenterText);
        _rollCenterEditText.setText(String.format(Locale.getDefault(), "%.1f", rollCenterHeight));
        _rollAxisEditText = (EditText) view.findViewById(R.id.fragment_simulation_rollAxisText);
        _rollAxisEditText.setText(String.format(Locale.getDefault(), "%.1f", Math.toDegrees(Math.atan(rollCenterSlopeXZ))));
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
