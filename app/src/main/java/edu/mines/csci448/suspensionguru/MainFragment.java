package edu.mines.csci448.suspensionguru;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mines.csci448.suspensionguru.data.Setup;
import edu.mines.csci448.suspensionguru.data.Vehicle;
import edu.mines.csci448.suspensionguru.db.DBHelper;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {
    private static int REQUEST_IMAGE_CAPTURE = 1;

    public static Map<String /* Vehicle Name */, Vehicle> _vehicles = new HashMap<>();
    public static Map<String /* Setup Name */, Setup> _setups = new HashMap<>();

    private Spinner _vehicleSpinner, _setupSpinner;
    private ImageView _vehicleImageView;
    private CoordinatorLayout _vehicleImageLayout;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        /* Perform UI Lookups */
        final FloatingActionButton takePicture = (FloatingActionButton) view.findViewById(R.id.fragment_main_takePictureActionButton);
        final ImageButton addVehicle = (ImageButton) view.findViewById(R.id.fragment_main_addVehicleButton);
        final ImageButton addSetup = (ImageButton) view.findViewById(R.id.fragment_main_addSetupButton);
        final ImageButton deleteVehicle = (ImageButton) view.findViewById(R.id.fragment_main_deleteVehicleButton);
        final ImageButton deleteSetup = (ImageButton) view.findViewById(R.id.fragment_main_deleteSetupButton);
        final RelativeLayout setupSelectionRelativeLayout
                = (RelativeLayout) view.findViewById(R.id.fragment_main_setupSelectionRelativeLayout);
        final Button editVehicleSetupButton = (Button) view.findViewById(R.id.fragment_main_setupButton);
        final Button editDimensionsButton = (Button) view.findViewById(R.id.fragment_main_dimensionsButton);
        final Button simulateButton = (Button) view.findViewById(R.id.fragment_main_simulateButton);
        _vehicleSpinner = (Spinner) view.findViewById(R.id.fragment_main_vehicleSpinner);
        _setupSpinner = (Spinner) view.findViewById(R.id.fragment_main_setupSpinner);
        _vehicleImageView = (ImageView) view.findViewById(R.id.fragment_main_vehicleImageView);
        _vehicleImageLayout = (CoordinatorLayout) view.findViewById(R.id.fragment_main_vehicleImageLayout);

        /* Wire up Spinners */
        _vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupSelectionRelativeLayout.setVisibility(View.VISIBLE);
                updateVehiclePicture();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setupSelectionRelativeLayout.setVisibility(View.GONE);
            }
        });
        _setupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editVehicleSetupButton.setVisibility(View.VISIBLE);
                editDimensionsButton.setVisibility(View.VISIBLE);
                simulateButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editVehicleSetupButton.setVisibility(View.GONE);
                editDimensionsButton.setVisibility(View.GONE);
                simulateButton.setVisibility(View.GONE);
            }
        });

        /* Wire up Take Picture Floating Action Button */
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null
                        && _vehicleSpinner.getSelectedItemPosition() != AdapterView.INVALID_POSITION) {

                    try {
                        File image = getVehiclePicturePath();
                        Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.android.fileprovider", image);
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /* Wire Up Add Vehicle/Setup Button */
        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

            // Prompt User for Vehicle Name
            final EditText input = new EditText(getContext());
            new AlertDialog.Builder(getContext())
                .setTitle("Enter a Vehicle Name...")
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();
                        if (_vehicles.containsKey(text)) {

                            // Warn User of Duplicate
                            new AlertDialog.Builder(getContext())
                                .setTitle("Duplicate Vehicle")
                                .setMessage("A vehicle by that name already exists.")
                                .show();
                        } else if (!text.isEmpty()) {
                            // Add Vehicle
                            _vehicles.put(text, new Vehicle(text));
                            _vehicles.get(text).saveVehicle(getContext());

                            // Refresh Spinner (in a gross way)
                            refreshSpinners();
                        }
                    }
                }).show();
            }
        });
        addSetup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                // Prompt User for Setup Name
                final EditText input = new EditText(getContext());
                new AlertDialog.Builder(getContext())
                        .setTitle("Enter a Setup Name...")
                        .setView(input)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                String text = input.getText().toString();
                                if (_setups.containsKey(text)) {

                                    // Warn User of Duplicate
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Duplicate Setup")
                                            .setMessage("A setup by that name already exists.")
                                            .show();
                                } else if (!text.isEmpty()) {
                                    // Add Setup
                                    _setups.put(text, new Setup(text));
                                    _setups.get(text).saveSetup(getContext());

                                    // Refresh Spinner (in a gross way)
                                    refreshSpinners();
                                }
                            }
                        }).show();
            }
        });

        /* Wire Up Delete Vehicle/Setup Button */
        deleteVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_vehicleSpinner.getSelectedItem() == null) return;

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Vehicle")
                        .setMessage("Are you sure you want to delete '" + _vehicleSpinner.getSelectedItem() + "'?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove Vehicle from Map
                                String target = (String) _vehicleSpinner.getSelectedItem();
                                _vehicles.get(target).deleteVehicle(getContext());
                                _vehicles.remove(target);

                                // Refresh Spinner (in a gross way)
                                refreshSpinners();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        deleteSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_setupSpinner.getSelectedItem() == null) return;

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Setup")
                        .setMessage("Are you sure you want to delete '" + _setupSpinner.getSelectedItem() + "'?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove Setup from Map
                                String target = (String) _setupSpinner.getSelectedItem();
                                _setups.get(target).deleteSetup(getContext());
                                _setups.remove(target);

                                // Refresh Spinner (in a gross way)
                                refreshSpinners();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        /* Wire up Vehicle/Setup Details Button */
        editVehicleSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_vehicleSpinner.getSelectedItem() != null && _setupSpinner.getSelectedItem() != null)
                    startActivity(SetupActivity.getIntent(getActivity(),
                            (String) _vehicleSpinner.getSelectedItem(), (String) _setupSpinner.getSelectedItem()));
                else {
                    new AlertDialog.Builder(MainFragment.this.getContext())
                            .setTitle("Invalid Selection")
                            .setMessage("Select a vehicle and setup configuration from the list above.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setCancelable(true)
                            .show();
                }
            }
        });

        /* Wire up Dimensions Button */
        editDimensionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_vehicleSpinner.getSelectedItem() != null && _setupSpinner.getSelectedItem() != null)
                    startActivity(DimensionActivity.getIntent(getActivity(),
                            (String) _vehicleSpinner.getSelectedItem(), (String) _setupSpinner.getSelectedItem()));
                else {
                    new AlertDialog.Builder(MainFragment.this.getContext())
                            .setTitle("Invalid Selection")
                            .setMessage("Select a vehicle and setup configuration from the list above.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setCancelable(true)
                            .show();
                }
            }
        });

        /* Wire up Simulate Button */
        simulateButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (_vehicleSpinner.getSelectedItem() != null && _setupSpinner.getSelectedItem() != null)
                    startActivity(SimulationActivity.getIntent(getActivity(),
                            (String) _vehicleSpinner.getSelectedItem(), (String) _setupSpinner.getSelectedItem()));
                else {
                    new AlertDialog.Builder(MainFragment.this.getContext())
                            .setTitle("Invalid Selection")
                            .setMessage("Select a vehicle and setup configuration from the list above.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setCancelable(true)
                            .show();
                }
            }
        });

        updateVehiclePicture();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Backup Selected Items;
        int selectedVehiclePosition = _vehicleSpinner.getSelectedItemPosition();
        int selectedSetupPosition = _setupSpinner.getSelectedItemPosition();

        // Populate Vehicle/Setup Maps
        _vehicles.clear(); _setups.clear();
        List<Vehicle> vehicles = Vehicle.getVehicles(getContext());
        List<Setup> setups = Setup.getSetups(getContext());

        for (Vehicle vehicle : vehicles) _vehicles.put(vehicle.getName(), vehicle);
        for (Setup setup : setups) _setups.put(setup.getName(), setup);

        // Refresh all Spinners
        refreshSpinners();

        // Restore Selected Items
        if (selectedVehiclePosition != AdapterView.INVALID_POSITION)
            _vehicleSpinner.setSelection(selectedVehiclePosition);
        if (selectedSetupPosition != AdapterView.INVALID_POSITION)
            _setupSpinner.setSelection(selectedSetupPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBHelper.closeDB(getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            updateVehiclePicture();
        }
    }

    private void updateVehiclePicture() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(getVehiclePicturePath().getAbsolutePath(), options);

            options.inJustDecodeBounds = false;
            options.inSampleSize = 5;
            Bitmap img = BitmapFactory.decodeFile(getVehiclePicturePath().getAbsolutePath(), options);
            if (img == null) throw new IOException("File not found...");
            _vehicleImageView.setImageBitmap(img);

        } catch (IOException e) {
            _vehicleImageView.setImageDrawable(getResources().getDrawable(R.drawable.rockcrawler));
        }
    }

    private File getVehiclePicturePath() throws IOException {
        String filename = "JPEG_SuspensionDimension_" + _vehicleSpinner.getSelectedItem() + ".jpg";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, filename);
    }

    private void refreshSpinners() {
        _vehicleSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                R.layout.spinner_simple_dropdown_item, new ArrayList<>(_vehicles.keySet())));
        _setupSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                R.layout.spinner_simple_dropdown_item, new ArrayList<>(_setups.keySet())));
    }
}
