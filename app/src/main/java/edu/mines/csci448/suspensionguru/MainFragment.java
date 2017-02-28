package edu.mines.csci448.suspensionguru;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainFragment extends Fragment {
    private Spinner _vehicleSpinner, _setupSpinner;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        /* Wire up Spinners */
        _vehicleSpinner = (Spinner) view.findViewById(R.id.fragment_main_vehicleSpinner);
        _setupSpinner = (Spinner) view.findViewById(R.id.fragment_main_setupSpinner);

        /* Wire Up Add Vehicle/Setup Button */
        ImageButton addVehicle = (ImageButton) view.findViewById(R.id.fragment_main_addVehicleButton);
        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getActivity(), "Add Vehicle", Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton addSetup = (ImageButton) view.findViewById(R.id.fragment_main_addSetupButton);
        addSetup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getActivity(), "Add Setup", Toast.LENGTH_SHORT).show();
            }
        });

        /* Wire up Vehicle/Setup Details Button */
        Button setupDetails = (Button) view.findViewById(R.id.fragment_main_setupButton);
        setupDetails.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(SetupActivity.getIntent(getActivity()));
            }
        });

        /* Wire up Dimensions Button */
        Button dimension = (Button) view.findViewById(R.id.fragment_main_dimensionsButton);
        dimension.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(DimensionActivity.getIntent(getActivity()));
            }
        });

        /* Wire up Simulate Button */
        Button simulate = (Button) view.findViewById(R.id.fragment_main_simulateButton);
        simulate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(SimulationActivity.getIntent(getActivity()));
            }
        });

        return view;
    }

}
