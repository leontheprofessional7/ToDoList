package com.example.tek.first.servant.todolist.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.tek.first.servant.todolist.activity.ToDoItemDetailsActivity;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.ToDoItemModel;

import java.util.ArrayList;

public class ToDoListDisplayFragment extends ListFragment {

    private static final String LOG_TAG = ToDoListDisplayFragment.class.getSimpleName();

    private DatabaseHelper dbHelper;

    @Override
    public void onStart() {
        super.onStart();
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArrayList<ToDoItemModel> toDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList();
        Intent intent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        intent.putExtra(GeneralConstants.TO_DO_ITEM_IDENTIFIER, toDoItemsArrayList.get(position));
        Log.v(LOG_TAG, "onListItemClick, ToDoListDisplayFragment: " +
                GeneralHelper.formatToString((toDoItemsArrayList.get(position)).getToDoItemDeadline()));
        startActivity(intent);
    }
}
