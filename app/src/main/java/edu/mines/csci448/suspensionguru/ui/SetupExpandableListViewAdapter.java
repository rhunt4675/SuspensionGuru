package edu.mines.csci448.suspensionguru.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.mines.csci448.suspensionguru.HelpActivity;
import edu.mines.csci448.suspensionguru.R;
import edu.mines.csci448.suspensionguru.SetupFragment;
import edu.mines.csci448.suspensionguru.data.Setup;

public class SetupExpandableListViewAdapter implements ExpandableListAdapter {
    private Setup _setup;
    private LayoutInflater _inflater;
    private Fragment _parentFragment;

    private EditText _geolocation = null;

    public SetupExpandableListViewAdapter(Setup setup, Context context, Fragment fragment) {
        _setup = setup;
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _parentFragment = fragment;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return 5;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        switch (groupPosition) {
            case 0:
            case 1:
            case 3:
            case 4: return 3;
            case 2: return 4;
            default: return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override @SuppressWarnings("deprecation")
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Create View (if null)
        if (convertView == null) {
            convertView = _inflater.inflate(R.layout.listview_parent_setup, null);
        }

        // Determine Group Title
        final int titleId = getGroupTitle(groupPosition);
        int drawableId = getGroupDrawable(groupPosition);

        // Customize View
        TextView label = (TextView) convertView.findViewById(R.id.listview_parent_setup_groupText);
        label.setText(titleId);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.listview_parent_setup_groupImage);
        imageView.setImageDrawable(_inflater.getContext().getResources().getDrawable(drawableId));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // Create View (if null)
        if (convertView == null) {
            convertView = _inflater.inflate(R.layout.listview_child_setup, null);
        }

        // Determine Child Title & Help Message
        final int childTitleId = getChildTitle(groupPosition, childPosition);
        final int helpMessageId = getHelpMessage(groupPosition, childPosition);
        final String currentParameter = getParameter(groupPosition, childPosition);

        // Customize View
        TextView label = (TextView) convertView.findViewById(R.id.listview_child_setup_childLabel);
        label.setText(childTitleId);

        final EditText parameter = (EditText) convertView.findViewById(R.id.listview_child_setup_childParameterText);
        parameter.setText(currentParameter);
        parameter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_UP) return false;

                // Customize Dialog View
                View view = _inflater.inflate(R.layout.dialog_string_edit, null);
                final TextView dialogLabel = (TextView) view.findViewById(R.id.dialog_string_edit_parameterLabel);
                dialogLabel.setText(childTitleId);
                final EditText dialogEdit = (EditText) view.findViewById(R.id.dialog_string_edit_parameterValue);
                dialogEdit.setText(parameter.getText().toString());
                dialogEdit.setRawInputType(!isNumeric(groupPosition, childPosition) ? InputType.TYPE_CLASS_TEXT
                        : InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                // Generate Dialog
                AlertDialog alertDialog = new AlertDialog.Builder(_inflater.getContext())
                        .setTitle("Enter Parameter")
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = dialogEdit.getText().toString();
                                boolean success = setParameter(groupPosition, childPosition, input);
                                if (success) parameter.setText(input);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();

                // Show Soft Keyboard
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    alertDialog.show();
                }

                // Event consumed
                return true;
            }
        });

        // Special Case -- Geolocation
        final ImageButton geolocation = (ImageButton) convertView.findViewById(R.id.listview_child_setup_locationButton);
        if (groupPosition == 1 && childPosition == 2) {
            _geolocation = parameter;

            geolocation.setVisibility(View.VISIBLE);
            geolocation.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ((SetupFragment) _parentFragment).getLocation();
                }
            });
        } else {
            geolocation.setVisibility(View.GONE);
        }

        ImageButton help = (ImageButton) convertView.findViewById(R.id.listview_child_setup_vehicleParameterHelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = _inflater.getContext();
                context.startActivity(HelpActivity.getIntent(context, helpMessageId));
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    private int getGroupTitle(int groupPosition) {
        if (groupPosition == 0) return R.string.setupexpandablelistviewadapter_group_general;
        else if (groupPosition == 1) return R.string.setupexpandablelistviewadapter_group_location;
        else if (groupPosition == 2) return R.string.setupexpandablelistviewadapter_group_tires;
        else if (groupPosition == 3) return  R.string.setupexpandablelistviewadapter_group_mass;
        else if (groupPosition == 4) return R.string.setupexpandablelistviewadapter_group_cog;
        else return -1;
    }

    private int getGroupDrawable(int groupPosition) {
        if (groupPosition == 0) return R.drawable.gear;
        else if (groupPosition == 1) return R.drawable.map;
        else if (groupPosition == 2) return R.drawable.tire;
        else if (groupPosition == 3) return R.drawable.scale;
        else if (groupPosition == 4) return R.drawable.ruler;
        else return -1;
    }

    private int getChildTitle(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_name;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_driver;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_speed;
        } else if (groupPosition == 1) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_locationname;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_locationdesc;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_geolocation;
        } else if (groupPosition == 2) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_tirepressure;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_tirediameter;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_tirerollingdiameter;
            else if (childPosition == 3) return R.string.setupexpandablelistviewadapter_child_tirewidth;
        } else if (groupPosition == 3) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_massvehicle;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_massfront;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_massback;
        } else if (groupPosition == 4) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_cogx;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_cogy;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_cogz;
        }
        return -1;
    }

    private int getHelpMessage(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_name_help;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_driver_help;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_speed_help;
        } else if (groupPosition == 1) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_locationname_help;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_locationdesc_help;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_geolocation_help;
        } else if (groupPosition == 2) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_tirepressure_help;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_tirediameter_help;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_tirerollingdiameter_help;
            else if (childPosition == 3) return R.string.setupexpandablelistviewadapter_child_tirewidth_help;
        } else if (groupPosition == 3) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_massvehicle_help;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_massfront_help;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_massback_help;
        } else if (groupPosition == 4) {
            if (childPosition == 0) return R.string.setupexpandablelistviewadapter_child_cogx_help;
            else if (childPosition == 1) return R.string.setupexpandablelistviewadapter_child_cogy_help;
            else if (childPosition == 2) return R.string.setupexpandablelistviewadapter_child_cogz_help;
        }
        return -1;
    }

    private String getParameter(int groupPosition, int childPosition) {
        try {
            if (groupPosition == 0) {
                if (childPosition == 0) return _setup.getName();
                else if (childPosition == 1) return _setup.getDriver();
                else if (childPosition == 2) return _setup.getTargetSpeed().toString();
            } else if (groupPosition == 1) {
                if (childPosition == 0) return _setup.getLocationName();
                else if (childPosition == 1) return _setup.getLocationDescription();
                else if (childPosition == 2) return _setup.getLocationLatitude().toString()
                        + "," + _setup.getLocationLongitude().toString();
            } else if (groupPosition == 2) {
                if (childPosition == 0) return _setup.getTirePressure().toString();
                else if (childPosition == 1) return _setup.getTireDiameter().toString();
                else if (childPosition == 2) return _setup.getTireRollingDiameter().toString();
                else if (childPosition == 3) return _setup.getTireWidth().toString();
            } else if (groupPosition == 3) {
                if (childPosition == 0) return _setup.getMassVehicle().toString();
                else if (childPosition == 1) return _setup.getMassFrontUnsprung().toString();
                else if (childPosition == 2) return _setup.getMassBackUnsprung().toString();
            } else if (groupPosition == 4) {
                if (childPosition == 0) return _setup.getCenterOfGravityX().toString();
                else if (childPosition == 1) return _setup.getCenterOfGravityY().toString();
                else if (childPosition == 2) return _setup.getCenterOfGravityHeight().toString();
            }
        } catch (NullPointerException e) {
            return "";
        }
        return "[missing?]";
    }

    private boolean setParameter(int groupPosition, int childPosition, String value) {
        try {
            if (groupPosition == 0) {
                if (childPosition == 0) _setup.setName(value);
                else if (childPosition == 1) _setup.setDriver(value);
                else if (childPosition == 2) _setup.setTargetSpeed(Double.parseDouble(value));
            } else if (groupPosition == 1) {
                if (childPosition == 0) _setup.setLocationName(value);
                else if (childPosition == 1) _setup.setLocationDescription(value);
                else if (childPosition == 2) {
                    String[] parts = value.split(",");
                    _setup.setLocationLatitude(Double.parseDouble(parts[0]));
                    _setup.setLocationLongitude(Double.parseDouble(parts[1]));
                }
            } else if (groupPosition == 2) {
                if (childPosition == 0) _setup.setTirePressure(Double.parseDouble(value));
                else if (childPosition == 1) _setup.setTireDiameter(Double.parseDouble(value));
                else if (childPosition == 2) _setup.setTireRollingDiameter(Double.parseDouble(value));
                else if (childPosition == 3) _setup.setTireWidth(Double.parseDouble(value));
            } else if (groupPosition == 3) {
                if (childPosition == 0) _setup.setMassVehicle(Double.parseDouble(value));
                else if (childPosition == 1) _setup.setMassFrontUnsprung(Double.parseDouble(value));
                else if (childPosition == 2) _setup.setMassBackUnsprung(Double.parseDouble(value));
            } else if (groupPosition == 4) {
                if (childPosition == 0) _setup.setCenterOfGravityX(Double.parseDouble(value));
                else if (childPosition == 1) _setup.setCenterOfGravityY(Double.parseDouble(value));
                else if (childPosition == 2) _setup.setCenterOfGravityHeight(Double.parseDouble(value));
            }

            // Success
            _setup.saveSetup(_inflater.getContext());
            return true;
        } catch (NumberFormatException e) {
            new AlertDialog.Builder(_inflater.getContext())
                    .setTitle("Invalid Number Format")
                    .setMessage("Enter a decimal number.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            new AlertDialog.Builder(_inflater.getContext())
                    .setTitle("Invalid Geolocation Format")
                    .setMessage("Enter a lattitude/longitude pair separated by a comma.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return false;
        }
    }

    private boolean isNumeric(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            if (childPosition == 0) return false;
            else if (childPosition == 1) return false;
            else if (childPosition == 2) return true;
        } else if (groupPosition == 1) {
            if (childPosition == 0) return false;
            else if (childPosition == 1) return false;
            else if (childPosition == 2) return false;
        } else if (groupPosition == 2) {
            if (childPosition == 0) return true;
            else if (childPosition == 1) return true;
            else if (childPosition == 2) return true;
            else if (childPosition == 3) return true;
        } else if (groupPosition == 3) {
            if (childPosition == 0) return true;
            else if (childPosition == 1) return true;
            else if (childPosition == 2) return true;
        } else if (groupPosition == 4) {
            if (childPosition == 0) return true;
            else if (childPosition == 1) return true;
            else if (childPosition == 2) return true;
        }

        return false;
    }

    public void setLocation(double latitude, double longitude) {
        _setup.setLocationLatitude(latitude);
        _setup.setLocationLongitude(longitude);

        if (_geolocation != null) {
            _geolocation.setText(latitude + "," + longitude);
            _setup.saveSetup(_inflater.getContext());
        }
    }
}
