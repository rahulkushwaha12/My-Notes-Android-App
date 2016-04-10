package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import model.MyNotes;

/**
 * Created by Rahul Kushwaha on 03-Apr-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper{
    private final ArrayList<MyNotes> notesList= new  ArrayList<>();


    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE "+ Constants.TABLE_NAME + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.TITLE_NAME + " TEXT, " + Constants.CONTENT_NAME + " TEXT, " + Constants.DATE_NAME + " LONG);";
        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);

    }
    //add content to TABLE
    public void addNotes(MyNotes note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TITLE_NAME, note.getTitle());
        values.put(Constants.CONTENT_NAME, note.getContent());
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());
        db.insert(Constants.TABLE_NAME, null, values);

        //Log.v("Note successfully", "yeah!!");
        db.close();

    }
    //update content to TABLE
    public void updateNotes(MyNotes note,int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TITLE_NAME, note.getTitle());
        values.put(Constants.CONTENT_NAME, note.getContent());
        //values.put(Constants.DATE_NAME, note.getRecordDate());

        String where = Constants.KEY_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(id)};

        db.update(Constants.TABLE_NAME, values, where, whereArgs);
        //db.insert(Constants.TABLE_NAME, null, values);

        Log.v("updated successfully", "yeah!!");
        db.close();

    }
    //fetch a note
    public MyNotes fetchNote(int id) {
        String fetchQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.KEY_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(fetchQuery, new String[]{String.valueOf(id)});
        MyNotes note;
        note = null;
        if (cursor.moveToFirst()) {
            note = new MyNotes();
            note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME)));
            note.setItemId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
            note.setContent(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)));
            note.setRecordDate(cursor.getString(cursor.getColumnIndex(Constants.DATE_NAME)));

        }
        cursor.close();
        return note;
    }
    //delete notes
    public void deleteNote(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ",
                new String[]{ String.valueOf(id)});

        db.close();

    }
    //get notes
    public ArrayList<MyNotes> getNotes(){

        String selectQuery = "SELECT * FROM "+ Constants.TITLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID, Constants.TITLE_NAME, Constants.CONTENT_NAME, Constants.DATE_NAME},null,null, null,null,Constants.DATE_NAME+ " DESC");
        //loop through cursor
        if (cursor.moveToFirst()) {
            do {
                MyNotes note = new MyNotes();
                note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)));
                note.setItemId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String dataData = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());
                note.setRecordDate(dataData);
                notesList.add(note);

            } while (cursor.moveToNext());
        }

        return notesList;

    }
}
