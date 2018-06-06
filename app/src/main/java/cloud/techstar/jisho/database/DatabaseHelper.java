package cloud.techstar.jisho.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

import cloud.techstar.jisho.AppMain;
@Deprecated
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION = 3;
    private static final String DATABASE_NAME    = "jisho.db";

    public DatabaseHelper() {
        super(AppMain.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(WordTable.create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Word.TABLE_WORDS);
        onCreate(db);
        Logger.e("Upgrade database version: "+DATABASE_VERSION);
    }
}