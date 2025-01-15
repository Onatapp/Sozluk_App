package per.example.sozlukapp_ornek;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {


    public DBConnection(Context context) {
        super(context, "sozluk.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS \"kelimeler\" (\n" +
                "\t\"kelime_id\"\tINTEGER,\n" +
                "\t\"ingilizce\"\tTEXT,\n" +
                "\t\"turkce\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"kelime_id\" AUTOINCREMENT)\n" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kelimeler");
        onCreate(db);
    }
}
