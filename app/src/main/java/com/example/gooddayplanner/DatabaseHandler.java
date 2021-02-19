package com.example.gooddayplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    //Database info
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "GDP.sqlite3";
    private static com.example.gooddayplanner.DatabaseHandler sInstance;
    private static final String TAG = "GoodDayPlanner db";

    //Table names
    private static final String TABLE_EVENTS = "events";

    //Events table columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DESCRIPTION = "description";

    //SQL to create table events
    private static final String CREATE_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    TABLE_EVENTS +
                    "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT NOT NULL, " +
                    KEY_DATE + " INTEGER NOT NULL, " +
                    KEY_START + " INTEGER NOT NULL, " +
                    KEY_END + " INTEGER NOT NULL, " +
                    KEY_LOCATION + " TEXT, " +
                    KEY_DESCRIPTION + " TEXT)";

    //Event projection
    private String[] eventProjection = {
            KEY_ID,
            KEY_NAME,
            KEY_DATE,
            KEY_START,
            KEY_END,
            KEY_LOCATION,
            KEY_DESCRIPTION
    };

    /**
     * Constructor is private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creates events table
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            //Drop old table
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

            //Create new table
            onCreate(db);
        }
    }

    public static synchronized com.example.gooddayplanner.DatabaseHandler getInstance(Context context) {
        //If database don't exist
        if (sInstance == null) {
            //Create instance of it
            sInstance = new com.example.gooddayplanner.DatabaseHandler(context.getApplicationContext());
        }

        return sInstance;
    }

    public long addEvent(Event event) {
        //Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        long eventID = -1;

        //Begin new transaction
        db.beginTransaction();
        try {
            //Set the values to be inserted
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, event.getName());
            values.put(KEY_DATE, event.getDate());
            Log.i("DATE IN DB","i.e " + event.getDate());
            values.put(KEY_START, event.getStart());
            values.put(KEY_END, event.getEnd());
            values.put(KEY_LOCATION, event.getLocation());
            values.put(KEY_DESCRIPTION, event.getDescription());

            //Insert event
            eventID = db.insertOrThrow(TABLE_EVENTS, null, values);

            //Set transaction successful
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add event to database \n\n" + e.getMessage());
            return eventID;
        } finally {
            db.endTransaction();
        }

        return eventID;
    }

    public Event getEvent(long id) {
        //Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        //Search database
        Cursor cursor = db.query(
                TABLE_EVENTS,
                eventProjection,
                KEY_ID + " = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null
        );

        Event event = new Event();

        try {
            //Move cursor to first row
            cursor.moveToFirst();

            //Set values
            event.setID(Long.parseLong(cursor.getString(0)));
            event.setName(cursor.getString(1));
            event.setDate(Long.parseLong(cursor.getString(2)));
            event.setStart(Long.parseLong(cursor.getString(3)));
            event.setEnd(Long.parseLong(cursor.getString(4)));
            event.setLocation(cursor.getString(5));
            event.setDescription(cursor.getString(6));
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to select event in database \n\n" + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return event;
    }

    public Event[] getEventsForDay(long date) {
        //Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        //Search database
        Cursor cursor = db.query(
                TABLE_EVENTS,
                eventProjection,
                KEY_DATE + " == ?",
                new String[] { String.valueOf(date) },
                null,
                null,
                KEY_START + " ASC"
        );

        Event[] events;

        try {
            //Move cursor to first row
            cursor.moveToFirst();

            events = new Event[cursor.getCount()];

            //For every row in cursor
            for (int i = 0; i < cursor.getCount(); i++) {
                Event event = new Event(
                        Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        Long.parseLong(cursor.getString(2)),
                        Long.parseLong(cursor.getString(3)),
                        Long.parseLong(cursor.getString(4)),
                        cursor.getString(5),
                        cursor.getString(6)
                );

                events[i] = event;
                cursor.moveToNext();
            }

            return events;
        } catch (Exception e) {
            if (cursor != null) {
                Log.d(TAG, "Error while trying to select events for day in database \n\n" + e.getMessage());
            }
            events = new Event[0];
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return events;
    }

    public long updateEvent(Event event) {
        //Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        long eventID = -1;

        //Begin new transaction
        db.beginTransaction();
        try {
            //Set the values to be updated
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, event.getName());
            values.put(KEY_DATE, event.getDate());
            values.put(KEY_START, event.getStart());
            values.put(KEY_END, event.getEnd());
            values.put(KEY_LOCATION, event.getLocation());
            values.put(KEY_DESCRIPTION, event.getDescription());

            //Update event
            long rows = db.update(
                    TABLE_EVENTS,
                    values,
                    KEY_ID + " = ?",
                    new String[] { String.valueOf(event.getID()) }
            );

            //Check if update succeeded
            if (rows == 1) {
                eventID = event.getID();

                //Set transaction successful
                db.setTransactionSuccessful();
            } else {
                //Event do not exist so create it
                eventID = addEvent(event);

                if (eventID >= 0) {
                    //Set transaction successful
                    db.setTransactionSuccessful();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update event in database \n\n" + e.getMessage());
            return eventID;
        } finally {
            db.endTransaction();
        }

        return eventID;
    }

    public int deleteEvent(Event event) {
        //Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Begin new transaction
        db.beginTransaction();
        try {
            //Delete event
            db.delete(
                    TABLE_EVENTS,
                    KEY_ID + " = ?",
                    new String[] { String.valueOf(event.getID()) }
            );

            //Set transaction successful
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete event in database \n\n" + e.getMessage());
            return -1;
        } finally {
            db.endTransaction();
        }

        return 0;
    }

    public void deleteEvents() {
        //Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Begin new transaction
        db.beginTransaction();
        try {
            //Delete events
            db.delete(
                    TABLE_EVENTS,
                    null,
                    null
            );

            //Set transaction successful
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all events in database \n\n" + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}