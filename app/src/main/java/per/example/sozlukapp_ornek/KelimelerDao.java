package per.example.sozlukapp_ornek;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.ArrayList;

public class KelimelerDao {

    public ArrayList<Kelimeler> tumKelimeler(DBConnection dbConnection) {

        ArrayList<Kelimeler> kelimelerArrayList = new ArrayList<>();

        SQLiteDatabase dbx = dbConnection.getWritableDatabase();

        Cursor c = dbx.rawQuery("SELECT * FROM kelimeler", null);

        while (c.moveToNext()) {
            Kelimeler kelime = new Kelimeler(c.getInt(c.getColumnIndexOrThrow("kelime_id"))
                    , c.getString(c.getColumnIndexOrThrow("turkce"))
                    , c.getString(c.getColumnIndexOrThrow("ingilizce")));
            kelimelerArrayList.add(kelime);
        }

        return kelimelerArrayList;
    }

    public ArrayList<Kelimeler> kelimeAra(DBConnection dbConnection, String arananKelime) {

        ArrayList<Kelimeler> kelimelerArrayList = new ArrayList<>();

        SQLiteDatabase dbx = dbConnection.getWritableDatabase();

        Cursor c = dbx.rawQuery("SELECT * FROM kelimeler WHERE ingilizce like '%" + arananKelime + "%'", null);

        while (c.moveToNext()) {
            Kelimeler kelime = new Kelimeler(c.getInt(c.getColumnIndexOrThrow("kelime_id"))
                    , c.getString(c.getColumnIndexOrThrow("turkce"))
                    , c.getString(c.getColumnIndexOrThrow("ingilizce")));
            kelimelerArrayList.add(kelime);
        }

        return kelimelerArrayList;
    }

    public void kelimeEkle(DBConnection dbConnection, String turkce, String ingilizce) {

        SQLiteDatabase dbx = dbConnection.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("turkce", turkce);
        values.put("ingilizce", ingilizce);

        dbx.insertOrThrow("kelimeler", null, values);
        dbx.close();
    }

    public void kelimeSil(DBConnection dbConnection, int kelime_id) {

        SQLiteDatabase dbx = dbConnection.getWritableDatabase();
        dbx.delete("kelimeler", "kelime_id=?", new String[]{String.valueOf(kelime_id)});
        dbx.close();
    }
}
