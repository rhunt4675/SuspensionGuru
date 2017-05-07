package edu.mines.csci448.suspensionguru;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;

import edu.mines.csci448.suspensionguru.data.Setup;
import edu.mines.csci448.suspensionguru.data.Vehicle;
import edu.mines.csci448.suspensionguru.ui.SetupExpandableListViewAdapter;
import edu.mines.csci448.suspensionguru.ui.VehicleExpandableListViewAdapter;

public class SetupFragment extends Fragment {
    private static final String VEHICLE_BUNDLE = "vehicle";
    private static final String SETUP_BUNDLE = "setup";

    private EditText _vParam1, _vParam2, _vParam3, _vParam4, _sParam1, _sParam2, _sParam3, _sParam4;
    private ExpandableListView _vehicleELV, _setupELV;
    private Vehicle _vehicle;
    private Setup _setup;

    private static int _expandedChild = 0;

    public static SetupFragment newInstance(String vehicleName, String setupName) {
        SetupFragment fragment = new SetupFragment();

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
        }

        // Enable Up Navigation
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        /* Wire up ExpandableListViews */
        _vehicleELV = (ExpandableListView) view.findViewById(R.id.fragment_setup_vehicleExpandableListView);
        _vehicleELV.setAdapter(new VehicleExpandableListViewAdapter(_vehicle, getContext()));
        _setupELV = (ExpandableListView) view.findViewById(R.id.fragment_setup_setupExpandableListView);
        _setupELV.setAdapter(new SetupExpandableListViewAdapter(_setup, getContext()));

        /* Mutual Exclusion in Child Expansion */
        _vehicleELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != _expandedChild)
                    _vehicleELV.collapseGroup(_expandedChild);
                _setupELV.collapseGroup(_expandedChild);

                _expandedChild = groupPosition;
            }
        });
        _setupELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                _vehicleELV.collapseGroup(_expandedChild);
                if (groupPosition != _expandedChild)
                    _setupELV.collapseGroup(_expandedChild);

                _expandedChild = groupPosition;
            }
        });

        return view;
    }

}
