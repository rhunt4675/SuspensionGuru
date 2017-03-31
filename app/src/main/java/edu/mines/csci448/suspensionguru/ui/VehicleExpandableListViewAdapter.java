package edu.mines.csci448.suspensionguru.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.mines.csci448.suspensionguru.HelpActivity;
import edu.mines.csci448.suspensionguru.R;
import edu.mines.csci448.suspensionguru.data.Vehicle;

public class VehicleExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Vehicle _vehicle;
    private LayoutInflater _inflater;

    public VehicleExpandableListViewAdapter(Vehicle vehicle, Context context) {
        _vehicle = vehicle;
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 2;
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
        int groupTitleId = getGroupTitle(groupPosition);
        int drawableId = getGroupDrawable(groupPosition);

        // Customize View
        TextView label = (TextView) convertView.findViewById(R.id.listview_parent_setup_groupText);
        label.setText(groupTitleId);
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

        // Determine Child Title & Help Text
        final int childTitleId = getChildTitle(groupPosition, childPosition);
        final int helpMessageId = getHelpMessage(groupPosition, childPosition);
        String currentParameter = getParameter(groupPosition, childPosition);

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

                // Generate Dialog
                AlertDialog alertDialog = new AlertDialog.Builder(_inflater.getContext())
                        .setTitle("Enter Parameter")
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = dialogEdit.getText().toString();
                                setParameter(groupPosition, childPosition, input);
                                parameter.setText(input);
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
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
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
        if (groupPosition == 0) return R.string.vehicleexpandablelistviewadapter_group_general;
        else return -1;
    }

    private int getGroupDrawable(int groupPosition) {
        if (groupPosition == 0) return R.drawable.redcar;
        else return -1;
    }

    private int getChildTitle(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            if (childPosition == 0) return R.string.vehicleexpandablelistviewadapter_child_name;
            else if (childPosition == 1) return R.string.vehicleexpandablelistviewadapter_child_description;
        }
        return -1;
    }

    private int getHelpMessage(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            if (childPosition == 0) return R.string.vehicleexpandablelistviewadapter_child_name_help;
            else if (childPosition == 1) return R.string.vehicleexpandablelistviewadapter_child_description_help;
        }
        return -1;
    }

    private String getParameter(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            if (childPosition == 0) return _vehicle.getName();
            else if (childPosition == 1) return _vehicle.getPurpose();
        }
        return "[missing?]";
    }

    private void setParameter(int groupPosition, int childPosition, String value) {
        if (groupPosition == 0) {
            if (childPosition == 0) _vehicle.setName(value);
            else if (childPosition == 1) _vehicle.setPurpose(value);
        }

        // Success
        _vehicle.saveVehicle(_inflater.getContext());
    }
}
