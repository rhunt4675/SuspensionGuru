package edu.mines.csci448.suspensionguru;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import edu.mines.csci448.suspensionguru.data.Setup;
import edu.mines.csci448.suspensionguru.data.SuspensionDimension;
import edu.mines.csci448.suspensionguru.data.Vehicle;
import edu.mines.csci448.suspensionguru.ui.SuspensionDimensionExpandableListViewAdapter;

public class DimensionFragment extends Fragment {
    private static final String VEHICLE_BUNDLE = "vehicle_bundle";
    private static final String SETUP_BUNDLE = "setup_bundle";
    private SuspensionDimension _suspensionDimension;

    private ExpandableListView _upperELV, _lowerELV;
    private static int _expandedChild = 0;

    /**
     * Create fragment.
     *
     * @param vehicleName Vehicle
     * @param setupName Setup
     * @return
     */
    public static DimensionFragment newInstance(String vehicleName, String setupName) {
        DimensionFragment fragment =  new DimensionFragment();

        // Pass arguments to Fragment
        Bundle arguments = new Bundle();
        arguments.putString(VEHICLE_BUNDLE, vehicleName);
        arguments.putString(SETUP_BUNDLE, setupName);
        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     * Lifecycle event.
     *
     * @param savedInstanceState Saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read Bundle
        if (getArguments() != null) {
            String vehicleName = getArguments().getString(VEHICLE_BUNDLE);
            String setupName = getArguments().getString(SETUP_BUNDLE);

            Vehicle vehicle = MainFragment._vehicles.get(vehicleName);
            Setup setup = MainFragment._setups.get(setupName);
            _suspensionDimension = SuspensionDimension.getSuspensionDimension(vehicle, setup, getContext());
        }

        // Enable Up Navigation
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Lifecycle event.
     *
     * @param inflater Inflater
     * @param container Container
     * @param savedInstanceState Saved state.
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_dimension, container, false);

        /* Wire up ExpandableListViews */
        _upperELV = (ExpandableListView) view.findViewById(R.id.fragment_dimension_upperLinkExpandableListView);
        _upperELV.setAdapter(new SuspensionDimensionExpandableListViewAdapter(_suspensionDimension, true, getContext()));
        _lowerELV = (ExpandableListView) view.findViewById(R.id.fragment_dimension_lowerLinkExpandableListView);
        _lowerELV.setAdapter(new SuspensionDimensionExpandableListViewAdapter(_suspensionDimension, false, getContext()));

        /* Mutual Exclusion in Child Expansion */
        _upperELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != _expandedChild)
                    _upperELV.collapseGroup(_expandedChild);
                _lowerELV.collapseGroup(_expandedChild);

                _expandedChild = groupPosition;
            }
        });
        _lowerELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                _upperELV.collapseGroup(_expandedChild);
                if (groupPosition != _expandedChild)
                    _lowerELV.collapseGroup(_expandedChild);

                _expandedChild = groupPosition;
            }
        });

        return view;
    }
}