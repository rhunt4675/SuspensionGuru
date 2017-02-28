package edu.mines.csci448.suspensionguru;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SimulationFragment extends Fragment {
    private EditText _calc1, _calc2, _calc3, _calc4;

    public static SimulationFragment newInstance() {
        return new SimulationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simulation, container, false);

        /* Wire up Help Buttons */
        ImageButton calc1Help = (ImageButton) view.findViewById(R.id.fragment_simulation_calculation1Help);
        calc1Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton calc2Help = (ImageButton) view.findViewById(R.id.fragment_simulation_calculation2Help);
        calc2Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton calc3Help = (ImageButton) view.findViewById(R.id.fragment_simulation_calculation3Help);
        calc3Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });
        ImageButton calc4Help = (ImageButton) view.findViewById(R.id.fragment_simulation_calculation4Help);
        calc4Help.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(HelpActivity.getIntent(getActivity()));
            }
        });

        /* Wire up Input Parameter Fields */
        _calc1 = (EditText) view.findViewById(R.id.fragment_simulation_calculation1Text);
        _calc2 = (EditText) view.findViewById(R.id.fragment_simulation_calculation2Text);
        _calc3 = (EditText) view.findViewById(R.id.fragment_simulation_calculation3Text);
        _calc4 = (EditText) view.findViewById(R.id.fragment_simulation_calculation4Text);

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
