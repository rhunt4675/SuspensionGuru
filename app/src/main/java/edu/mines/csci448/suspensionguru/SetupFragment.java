package edu.mines.csci448.suspensionguru;


import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import edu.mines.csci448.suspensionguru.data.Setup;
import edu.mines.csci448.suspensionguru.data.Vehicle;
import edu.mines.csci448.suspensionguru.ui.SetupExpandableListViewAdapter;
import edu.mines.csci448.suspensionguru.ui.VehicleExpandableListViewAdapter;

public class SetupFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int PERM_REQUEST = 1;
    private static final String VEHICLE_BUNDLE = "vehicle";
    private static final String SETUP_BUNDLE = "setup";

    private EditText _vParam1, _vParam2, _vParam3, _vParam4, _sParam1, _sParam2, _sParam3, _sParam4;
    private ExpandableListView _vehicleELV, _setupELV;
    private Vehicle _vehicle;
    private Setup _setup;

    private GoogleApiClient _googleApiClient = null;

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

        // Google API Client
        if (_googleApiClient == null)
            _googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
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
        _setupELV.setAdapter(new SetupExpandableListViewAdapter(_setup, getContext(), this));

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

    @Override
    public void onStart() {
        _googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        _googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERM_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                processLocation();
            } else {
                Toast.makeText(getContext(), "Location permission denied...", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void getLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERM_REQUEST);
        } else {
            processLocation();
        }
    }

    public void processLocation() {
        try {
            if (_googleApiClient.isConnected()) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(_googleApiClient);
                if (lastLocation != null) {
                    ((SetupExpandableListViewAdapter) _setupELV.getExpandableListAdapter()).setLocation(
                            lastLocation.getLatitude(), lastLocation.getLongitude()
                    );
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
