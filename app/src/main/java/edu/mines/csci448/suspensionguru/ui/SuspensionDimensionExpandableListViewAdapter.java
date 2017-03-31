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
import android.widget.TextView;

import edu.mines.csci448.suspensionguru.HelpActivity;
import edu.mines.csci448.suspensionguru.R;
import edu.mines.csci448.suspensionguru.data.SuspensionDimension;

public class SuspensionDimensionExpandableListViewAdapter extends BaseExpandableListAdapter {
    private SuspensionDimension _suspension;
    private Boolean _upper;
    private LayoutInflater _inflater;

    public SuspensionDimensionExpandableListViewAdapter(SuspensionDimension suspension, boolean upper, Context context) {
        _suspension = suspension;
        _upper = upper;
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
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Create View (if null)
        if (convertView == null) {
            convertView = _inflater.inflate(R.layout.listview_parent_setup, null);
        }

        // Determine Group Title
        int groupTitleId = getGroupTitle(groupPosition);

        // Customize View
        TextView label = (TextView) convertView.findViewById(R.id.listview_parent_setup_groupText);
        label.setText(groupTitleId);

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
        if (groupPosition == 0) return R.string.suspensiondimensionexpandablelistviewadapter_group_frameMounts;
        else if (groupPosition == 1) return R.string.suspensiondiemnsionexpandablelistviewadapter_group_axleMounts;
        else return -1;
    }

    private int getChildTitle(int groupPosition, int childPosition) {
        if (childPosition == 0) return R.string.suspensiondimensionexpandablelistviewadapter_child_x;
        else if (childPosition == 1) return R.string.suspensiondimensionexpandablelistviewadapter_child_y;
        else if (childPosition == 2) return R.string.suspensiondimensionexpandablelistviewadapter_child_z;
        return -1;
    }

    private int getHelpMessage(int groupPosition, int childPosition) {
        if (childPosition == 0) return R.string.suspensiondimensionexpandablelistviewadapter_child_x_help;
        else if (childPosition == 1) return R.string.suspensiondimensionexpandablelistviewadapter_child_y_help;
        else if (childPosition == 2) return R.string.suspensiondimensionexpandablelistviewadapter_child_z_help;
        return -1;
    }

    private String getParameter(int groupPosition, int childPosition) {
        try {
            if (groupPosition == 0 && _upper) {
                if (childPosition == 0) return _suspension.getUpperFrameX().toString();
                else if (childPosition == 1) return _suspension.getUpperFrameY().toString();
                else if (childPosition == 2) return _suspension.getUpperFrameZ().toString();
            } else if (groupPosition == 0) {
                if (childPosition == 0) return _suspension.getLowerFrameX().toString();
                else if (childPosition == 1) return _suspension.getLowerFrameY().toString();
                else if (childPosition == 2) return _suspension.getLowerFrameZ().toString();
            } else if (groupPosition == 1 && _upper) {
                if (childPosition == 0) return _suspension.getUpperAxleX().toString();
                else if (childPosition == 1) return _suspension.getUpperAxleY().toString();
                else if (childPosition == 2) return _suspension.getUpperAxleZ().toString();
            } else if (groupPosition == 1) {
                if (childPosition == 0) return _suspension.getLowerAxleX().toString();
                else if (childPosition == 1) return _suspension.getLowerAxleY().toString();
                else if (childPosition == 2) return _suspension.getLowerAxleZ().toString();
            }
        } catch (NullPointerException e) {
            return "";
        }
        return "[missing?]";
    }

    private boolean setParameter(int groupPosition, int childPosition, String value) {
        try {
            if (groupPosition == 0 && _upper) {
                if (childPosition == 0) _suspension.setUpperFrameX(Double.parseDouble(value));
                else if (childPosition == 1) _suspension.setUpperFrameY(Double.parseDouble(value));
                else if (childPosition == 2) _suspension.setUpperFrameZ(Double.parseDouble(value));
            } else if (groupPosition == 0) {
                if (childPosition == 0) _suspension.setLowerFrameX(Double.parseDouble(value));
                else if (childPosition == 1) _suspension.setLowerFrameY(Double.parseDouble(value));
                else if (childPosition == 2) _suspension.setLowerFrameZ(Double.parseDouble(value));
            } else if (groupPosition == 1 && _upper) {
                if (childPosition == 0) _suspension.setUpperAxleX(Double.parseDouble(value));
                else if (childPosition == 1) _suspension.setUpperAxleY(Double.parseDouble(value));
                else if (childPosition == 2) _suspension.setUpperAxleZ(Double.parseDouble(value));
            } else if (groupPosition == 1) {
                if (childPosition == 0) _suspension.setLowerAxleX(Double.parseDouble(value));
                else if (childPosition == 1) _suspension.setLowerAxleY(Double.parseDouble(value));
                else if (childPosition == 2) _suspension.setLowerAxleZ(Double.parseDouble(value));
            }

            // Success
            _suspension.saveSuspensionDimension(_inflater.getContext());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
