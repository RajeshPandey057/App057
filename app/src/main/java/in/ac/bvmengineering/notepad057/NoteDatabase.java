package in.ac.bvmengineering.notepad057;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "NotesDB";
    private static final String DB_TABLE = "NotesTable";
    NoteDatabase(Context context){
        super(context, DB_NAME,null,DB_VERSION);
    }
    private  static final  String KEY_ID = "id";
    private  static final  String KEY_TITLE = "title";
    private  static final  String KEY_CONTENT = "content";
    private  static final  String KEY_DATE = "date";
    private  static final  String KEY_TIME = "time";
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ DB_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT,"+
                KEY_CONTENT + " TEXT,"+
                KEY_DATE + " TEXT,"+
                KEY_TIME + " TEXT"+ ")";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion>= newVersion){
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);

    } public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE,note.getTitle());
        c.put(KEY_CONTENT,note.getContent());
        c.put(KEY_DATE,note.getDate());
        c.put(KEY_TIME,note.getTime());

        long ID = db.insert(DB_TABLE,null,c);
        return ID;
    }
    public Note getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE,new String[] {KEY_ID,KEY_TITLE,KEY_CONTENT,KEY_DATE,KEY_TIME},KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
            return  new  Note(cursor.getLong(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4));
    }
    public List<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();
        String query = "SELECT * FROM " + DB_TABLE + " ORDER BY " + KEY_ID + " DESC";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                allNotes.add(note);
            }while (cursor.moveToNext());
        }
        return allNotes;
    }
    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c =new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());
        return db.update(DB_TABLE,c,KEY_ID + "=?",new String[]{String.valueOf(note.getID())});
    }
    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }

}
