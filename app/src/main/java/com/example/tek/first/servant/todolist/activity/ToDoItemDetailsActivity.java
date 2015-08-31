package com.example.tek.first.servant.todolist.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tek.first.servant.R;
import com.example.tek.first.servant.todolist.fragment.dialog.DatePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.DateModel;
import com.example.tek.first.servant.todolist.model.ToDoItemModel;

public class ToDoItemDetailsActivity extends Activity
        implements DetailedNewToDoItemDialogFragment.OnNewItemAddedListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialogFragment.DatePickerDialogListener {

    private static String LOG_TAG = ToDoItemDetailsActivity.class.getSimpleName();

    private TextView titleTextView;
    private TextView deadlineTextView;
    private TextView descriptionTextView;
    private TextView priorityTextView;
    private TextView dateAndTimeCreatedTextView;

    private Button btnEdit;
    private Button btnMarkAsComplete;
    private Button btnDelete;

    private int priority;

    private ToDoItemModel editedToDoItem;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_todoitem_details_activity);

        Intent intent = getIntent();
        final ToDoItemModel toDoItem
                = intent.getExtras().getParcelable(GeneralConstants.TO_DO_ITEM_IDENTIFIER);
        Log.v(LOG_TAG, "onCreate(), intent received, ToDoItemDetailsActivity: " + GeneralHelper.formatToString(toDoItem.getToDoItemDeadline()));
        priority = toDoItem.getPriority();

        dbHelper = new DatabaseHelper(ToDoItemDetailsActivity.this);

        titleTextView = (TextView) findViewById(R.id.textview_title_todoitem_detailactivity);
        deadlineTextView = (TextView) findViewById(R.id.textview_deadline_todoitem_detailactivity);
        descriptionTextView = (TextView) findViewById(R.id.textview_description_todoitem_detailactivity);
        priorityTextView = (TextView) findViewById(R.id.textview_priority_todoitem_detailactivity);
        dateAndTimeCreatedTextView = (TextView) findViewById(R.id.textview_dateandtimecreated_todoitem_detailactivity);

        btnEdit = (Button) findViewById(R.id.btn_edit_detailactivity);
        btnMarkAsComplete = (Button) findViewById(R.id.btn_markascomplete_detailactivity);
        btnDelete = (Button) findViewById(R.id.btn_delete_detailactivity);

        refreshToDoItemDetailsPage(toDoItem);

        /**
         * Styling the ActionBar
         */
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("ToDoItem Priority Level: " + toDoItem.getPriority());
        actionBar.setSubtitle("Details");

        String[] hexColorCode = getResources().getStringArray(R.array.priority_level_color);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor(hexColorCode[priority - 1]));
        getActionBar().setBackgroundDrawable(colorDrawable);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " will be edited");
                DetailedNewToDoItemDialogFragment detailedNewToDoListItemDialog = new DetailedNewToDoItemDialogFragment();
                detailedNewToDoListItemDialog.show(getFragmentManager(), "DetailedNewToDoItemDialogFragment");
            }
        });

        btnMarkAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_STATUS_COMPLETE);
                Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " is marked as complete");
                dbHelper.updateToDoListItem(toDoItem);
            }
        });

        btnMarkAsComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder markAsStatus = new AlertDialog.Builder(ToDoItemDetailsActivity.this);
                markAsStatus.setTitle("Please select a status to be marked")
                        .setItems(R.array.todoitem_complete_status, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 1:
                                        toDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_STATUS_INCOMPLETE);
                                        Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " is marked as incomplete");
                                        break;
                                    case 2:
                                        toDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_STATUS_NOT_STARTED);
                                        Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " is marked as not started");
                                        break;
                                    case 3:
                                        toDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_STATUS_COMPLETE);
                                        Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " is marked as complete");
                                        break;
                                }
                                dbHelper.updateToDoListItem(toDoItem);
                            }
                        });
                (markAsStatus.create()).show();
                return false;
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ToDoItemDetailsActivity.this);
                alertDialogBuilder.setMessage("Are you sure to delete ToDoItem: " + toDoItem.getTitle());
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteToDoItem(toDoItem);
                        Toast.makeText(ToDoItemDetailsActivity.this, "ToDoItem: " + toDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ToDoItemDetailsActivity.this, ToDoListMainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void refreshToDoItemDetailsPage(ToDoItemModel toDoItem) {
        titleTextView.setText(toDoItem.getTitle());
        String deadline = GeneralHelper.parseDateAndTimeToString(toDoItem.getToDoItemDeadline());
        deadlineTextView.setText("Deadline: " + deadline);
        Log.v(LOG_TAG, "deadline, refreshToDoItemDetailsPage(), ToDoItemDetailsActivity: " + deadline);
        descriptionTextView.setText(GeneralHelper.formatToString(toDoItem.getDetailDescription()));
        int priority = toDoItem.getPriority();
        priorityTextView.setText(GeneralHelper.formatToString(priority));
        priorityTextView.setBackgroundColor(GeneralConstants.PRIORITY_LEVEL_COLOR[priority]);
        String timeAdded = GeneralHelper.parseDateAndTimeToString(toDoItem.getItemCreatedDateAndTime());
        Log.v(LOG_TAG, "timeAdded, refreshToDoItemDetailsPage(), ToDoItemDetailsActivity: " + timeAdded);
        dateAndTimeCreatedTextView.setText("Time added: " + timeAdded);
    }


    @Override
    public void onDateSelected(DateModel dateSelected) {

    }

    @Override
    public void onNewItemAdded(ToDoItemModel todoItem) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
