package com.example.tek.first.servant.todolist.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tek.first.servant.todolist.model.ToDoItemModel;
import com.example.tek.first.servant.todolist.helper.GeneralHelper.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public static String TODOLIST_DATABASE_NAME = "todolist_database";
    public static String TODOLIST_TABLE_NAME = "todolist_table";

    public static String TODOLIST_ITEM_ID = "todolist_id";
    public static String TODOLIST_ITEM_TITLE = "todolist_title";
    public static String TODOLIST_ITEM_DESCRIPTION = "todolist_description";
    public static String TODOLIST_ITEM_PRIORITY = "todolist_priority";
    public static String TODOLIST_ITEM_DEADLINE = "todolist_time_date_set";
    public static String TODOLIST_ITEM_TIME_DATE_CREATED = "todolist_time_date_created";
    public static String TODOLIST_ITEM_CATEGORY = "todolist_category";
    public static String TODOLIST_ITEM_COMPLETE_STATUS = "todolist_complete_status";

    public static int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, TODOLIST_DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TODOLIST_TABLE_NAME + " (" +
                TODOLIST_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TODOLIST_ITEM_TITLE + " TEXT NOT NULL, " +
                TODOLIST_ITEM_DESCRIPTION + " TEXT, " +
                TODOLIST_ITEM_PRIORITY + " INTEGER DEFAULT 1, " +
                TODOLIST_ITEM_DEADLINE + " TEXT, " +
                TODOLIST_ITEM_TIME_DATE_CREATED + " TEXT, " +
                TODOLIST_ITEM_CATEGORY + " INTEGER DEFAULT 0, " +
                TODOLIST_ITEM_COMPLETE_STATUS + " INTEGER DEFAULT 0)";
        Log.v(LOG_TAG, "createQuery: " + createTableQuery);

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeTableQuery = "DROP TABLE IF EXISTS " + TODOLIST_TABLE_NAME;
        Log.v(LOG_TAG, "Upgrade database from " + oldVersion + " to " + newVersion);
        Log.v(LOG_TAG, "upgradeTableQuery: " + upgradeTableQuery);
        db.execSQL(upgradeTableQuery);
    }

    public boolean insertToDoListItem(ToDoItemModel toDoListItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_TITLE, toDoListItem.getTitle());
        contentValues.put(TODOLIST_ITEM_DESCRIPTION, toDoListItem.getDetailDescription());
        contentValues.put(TODOLIST_ITEM_PRIORITY, toDoListItem.getPriority());
        Long toDoItemDateAndTimeCreatedLongType = toDoListItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType inserted by DatabaseHelper: " + toDoItemDateAndTimeCreatedLongType);
        String toDoItemDateAndTimeCreated = Long.toString(toDoItemDateAndTimeCreatedLongType);
        contentValues.put(TODOLIST_ITEM_TIME_DATE_CREATED, toDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime inserted by DatabaseHelper: " + toDoItemDateAndTimeCreated);
        String deadline = Long.toString(toDoListItem.getToDoItemDeadline());
        contentValues.put(TODOLIST_ITEM_DEADLINE, deadline);
        Log.v(LOG_TAG, "deadline inserted by DatabaseHelper: " + deadline);
        contentValues.put(TODOLIST_ITEM_CATEGORY, toDoListItem.getCategory());
        contentValues.put(TODOLIST_ITEM_COMPLETE_STATUS, toDoListItem.getCompleteStatusCode());
        db.insert(TODOLIST_TABLE_NAME, null, contentValues);
        return true;
    }

//    public boolean insertToDoListItemSetDataAndTime(String title, )

    public ArrayList<ToDoItemModel> getTitlePriorityDeadlineOfAllToDoItemsAsArrayList() {
        ArrayList<ToDoItemModel> toDoListItemsTitlePriorityDeadlineArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(getAllQuery, null);
        // todo: test whether the 1st item was included
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, deadline);
            toDoListItemsTitlePriorityDeadlineArrayList.add(toDoListItem);
            cursor.moveToNext();
        }
        return toDoListItemsTitlePriorityDeadlineArrayList;
    }

    public ArrayList<ToDoItemModel> getAllToDoItemsAsArrayList() {
        ArrayList<ToDoItemModel> toDoListItemsTitlePriorityDeadlineArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(getAllQuery, null);
        // todo: test whether the 1st item was included
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            String description = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            long itemCreatedDateAndTime = Long.parseLong(cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED)));
            Log.v(LOG_TAG, "itemCreatedDateAndTime fetched the DatabaseHelper: " + itemCreatedDateAndTime);
            long deadline = Long.parseLong(cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE)));
            Log.v(LOG_TAG, "deadline fetched the DatabaseHelper: " + deadline);
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, description, itemCreatedDateAndTime, deadline, category, completionStatusCode);
            toDoListItemsTitlePriorityDeadlineArrayList.add(0, toDoListItem);
            cursor.moveToNext();
        }
        return toDoListItemsTitlePriorityDeadlineArrayList;
    }

    /**
     * Returns total rows of the table
     *
     * @return
     */
    public int numberOfTotalToDoItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TODOLIST_TABLE_NAME);
        return numRows;
    }

    /**
     * returns total rows of incomplete todolist items
     *
     * @return
     */
    // todo
//    public int numberOfNotStartedToDoItems(){
//        SQLiteDatabase db = this.getReadableDatabase();
//    }
//
//    public int numberOfIncompleteToDoItems() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//    }
//
//    public int numberOfCompletedToDoItem() {
//        SQLiteDatabase db = this.getReadableDatabase();
//    }
    public boolean deleteToDoItem(String title, Long dateAndTimeCreated) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODOLIST_TABLE_NAME,
                TODOLIST_ITEM_TIME_DATE_CREATED + " =? AND " +
                        TODOLIST_ITEM_TITLE + " =?",
                new String[]{dateAndTimeCreated.toString(), title});
        return true;
    }


}
