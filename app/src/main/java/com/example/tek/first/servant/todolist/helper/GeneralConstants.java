package com.example.tek.first.servant.todolist.helper;

import com.example.tek.first.servant.R;

public abstract class GeneralConstants {

    public static final String COMPLETED_TODOLISTITEM_IDENTIFIER = "completedToDoListItemIdentifier";
    public static final String INCOMPLETED_TODOLISTITEM_IDENTIFIER = "incompleteToDoListItemIdentifier";
    public static final String TO_DO_ITEM_IDENTIFIER = "toDoItemIdentifier";

    public static final String HOUR_IDENTIFIER = "hourIdentifier";
    public static final String MINUTE_IDENTIFIER = "minuteIdentifier";
    public static final String DAY_IDENTIFIER = "dayIdentifier";
    public static final String YEAR_IDENTIFIER = "yearIdentifier";
    public static final String MONTH_IDENTIFIER = "monthIdentifier";

    public static final int TODOLISTITEM_COMPLETION_STATUS_INCOMPLETE = 1;
    public static final int TODOLISTITEM_COMPLETION_STATUS_COMPLETE = 2;

    public static final String SAVEINSTANCESTATE_ALL_TODOITEMS_ARRAYLIST_IDENTIFIER = "allToDoItemIdentifier";
    public static final String SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER = "incompleteToDoItemIdentifier";
    public static final String SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER = "completeToDoItemIdentifier";
    public static final String SAVEINSTANCESTATE_NOTSTARTED_TODOITEMS_ARRAYLIST_IDENTIFIER = "notStartedToDoItemIdentifier";

    // todo: how to getResources().getStringArray..... instead such self-created int Array
    public static final int[] PRIORITY_LEVEL_COLOR
            = {R.color.priority_level1, R.color.priority_level2, R.color.priority_level3,
            R.color.priority_level4, R.color.priority_level5, R.color.priority_level6,
            R.color.priority_level7, R.color.priority_level8, R.color.priority_level9,
            R.color.priority_level10};

    public static final String[] PRIORITY_LEVEL_COLOR_HEX_CODE
            = {"#FBE9E7", "#FFCCBC", "#FFAB91", "#FF7043", "#FF5722",
            "#F4511E", "#E64A19", "#D84315", "#BF360C", "#DD2C00"};


    public static final String TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER = "sortingTrendASCOrDESC";
    public static final String TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER = "sortingWayPriorityDeadlineTimeAddedOrTitle";

}


