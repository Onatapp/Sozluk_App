package per.example.sozlukapp_ornek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class DetayActivity extends AppCompatActivity {
    TextView textTurkce, textIngilizce, textKelimeid;
    Kelimeler kelime;
    DBConnection dbc;
    Button btnKelimeSil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);
        textTurkce = findViewById(R.id.textTurkce);
        textIngilizce = findViewById(R.id.textIngilizce);
        textKelimeid = findViewById(R.id.textKelimeid);
        btnKelimeSil = findViewById(R.id.btnKelimeSil);

        dbc = new DBConnection(this);


        kelime = (Kelimeler) getIntent().getSerializableExtra("Nesne");
        textIngilizce.setText(kelime.getKelime_ingilizce());
        textTurkce.setText(kelime.getKelime_turkce());
        textKelimeid.setText(String.valueOf(kelime.getKelime_id()));

        btnKelimeSil.setOnClickListener(new View.OnClickListener() {
            //* Kelime Sil butonuna tıklandığında Kelimelerdao sınıfına yazılan komutlar çalıştırılarak ve
            // sonrasında snackbar'dan geri dönüş alarak kelimenin id'sine göre silme gerçekleştiriliyor.
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Seçili kelimeyi silmek istediğinizden emin misiniz?", Snackbar.LENGTH_LONG)
                        .setAction("Evet", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new KelimelerDao().kelimeSil(dbc, kelime.getKelime_id());
                                Snackbar.make(v, "Seçili kelime başarıyla silindi.", Snackbar.LENGTH_LONG).show();
                                startActivity(new Intent(DetayActivity.this, MainActivity.class));
                                finish();
                            }
                        }).show();
            }
        });
    }
}