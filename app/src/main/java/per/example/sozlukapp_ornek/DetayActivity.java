package per.example.sozlukapp_ornek;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import per.example.sozlukapp_ornek.databinding.ActivityDetayBinding;
import per.example.sozlukapp_ornek.volleydb_version.Volley_MainPage;

public class DetayActivity extends AppCompatActivity {
    Kelimeler kelime;
    DBConnection dbc;
    private ActivityDetayBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityDetayBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        dbc = new DBConnection(this);


        kelime = (Kelimeler) getIntent().getSerializableExtra("Nesne");

        bind.textIngilizce.setText(kelime.getKelime_ingilizce());
        bind.textIngilizce.setText(kelime.getKelime_ingilizce());
        bind.textTurkce.setText(kelime.getKelime_turkce());
        bind.textKelimeid.setText(String.valueOf(kelime.getKelime_id()));

        bind.btnKelimeSil.setOnClickListener(new View.OnClickListener() {
            //* Kelime Sil butonuna tıklandığında Kelimelerdao sınıfına yazılan komutlar çalıştırılarak ve
            // sonrasında snackbar'dan geri dönüş alarak kelimenin id'sine göre silme gerçekleştiriliyor.
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Seçili kelimeyi silmek istediğinizden emin misiniz?", Snackbar.LENGTH_LONG)
                        .setAction("Evet", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                               // new KelimelerDao().kelimeSil(dbc, kelime.getKelime_id());
                                kelimeSilVolley();
                                Toast.makeText(getApplicationContext(), "Kelime başarıyla silindi.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(DetayActivity.this, Volley_MainPage.class));
                                finish();
                            }
                        }).show();
            }
        });
    }

    void kelimeSilVolley(){

        String url = "https://restfuldb.onatsomer.com/kelime/delete_kelime.php";
        String wordID = bind.textKelimeid.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("KELİME SİLME BİLGİSİ", "Kelime başarıyla silindi. " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("VOLLEY İSTEK HATASI", error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Word_ID", wordID);
                return params;
            }
        };
        Volley.newRequestQueue(DetayActivity.this).add(request);

    }
}