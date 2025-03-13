package per.example.sozlukapp_ornek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView rv;
    private ArrayList<Kelimeler> kelimelerList;
    private DBConnection dbc;
    private FloatingActionButton fabAddBtn;
    private String turkishWord;
    private String englishWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        fabAddBtn = findViewById(R.id.fabAddBtn);


        DatabaseCopy();
        dbc = new DBConnection(this);

        toolbar.setTitle("Sözlük Uygulaması");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        fabAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //* Butona tıklandığında "Bottom Sheet" ekranın altından açılarak yeni kelime eklenmesi sağlanıyor.
                BottomSheetDialog bottomSheet = new BottomSheetDialog(MainActivity.this);
                bottomSheet.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottom_sheet, null);
                bottomSheet.setContentView(view);

                //* Bottom Sheet ekstra arayüz ayarları.
                bottomSheet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                bottomSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bottomSheet.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                bottomSheet.getWindow().setGravity(Gravity.BOTTOM);

                //* Erişilen bottom sheet tasarım dosyasında yer alan kullanılacak widget'lar id'leriyle tanımlanıyor.
                TextInputLayout inputLayout = view.findViewById(R.id.inputLayoutIngilizce);
                TextInputLayout inputLayout2 = view.findViewById(R.id.inputLayoutTurkce);
                TextInputEditText edittxtTurkce = view.findViewById(R.id.txtTurkce);
                TextInputEditText edittxtIngilizce = view.findViewById(R.id.txtIngilizce);
                Button btnKelimeKaydet = view.findViewById(R.id.btnKelimeKaydet);

                btnKelimeKaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        turkishWord = edittxtTurkce.getText().toString();
                        englishWord = edittxtIngilizce.getText().toString();

                        if (edittxtIngilizce.getText().toString().isEmpty() & edittxtTurkce.getText().toString().isEmpty()) {

                            inputLayout.setError("Lütfen bu alanı doldurunuz.");
                            inputLayout2.setError("Lütfen bu alanı doldurunuz.");
                        } else {

                            new KelimelerDao().kelimeEkle(dbc, englishWord, turkishWord);
                            Toast.makeText(MainActivity.this, edittxtIngilizce.getText() + " Kelimesi kaydedildi.", Toast.LENGTH_LONG).show();
                            bottomSheet.dismiss();
                            kelimelerList = new KelimelerDao().tumKelimeler(dbc);
                            KelimelerAdapter adapter = new KelimelerAdapter(getApplicationContext(), kelimelerList);
                            rv.setAdapter(adapter);
                        }
                    }
                });
                bottomSheet.show();

                bottomSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //Toast.makeText(MainActivity.this, "Bottom Sheet kapandı", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //* Uygulama açıldığında kelimeler listesini getiriyor.
        kelimelerList = new KelimelerDao().tumKelimeler(dbc);
        KelimelerAdapter adapter = new KelimelerAdapter(this, kelimelerList);
        rv.setAdapter(adapter);

    }


    public void DatabaseCopy() {
        //* Veritabanı kopyalama işlemi gerçekleştiriliyor.
        DatabaseCopyHelper helper = new DatabaseCopyHelper(this);

        try {
            helper.createDataBase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        helper.openDataBase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //* Menü Arama tasarımı, toolbar'da gösteriliyor.
        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        MenuItem mitem = menu.findItem(R.id.action_searchMenu);
        SearchView searchView = (SearchView) mitem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Log.e("Gönderilen Metin", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //* Kelime arama işlevi çalışıyor.
        if (kelimelerList != null) {
            kelimelerList = new KelimelerDao().kelimeAra(dbc, newText);
            KelimelerAdapter adapter = new KelimelerAdapter(this, kelimelerList);
            rv.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Kelime Bulunamadı!", Toast.LENGTH_LONG).show();
        }

        return false;
    }
}