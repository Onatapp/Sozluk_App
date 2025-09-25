package per.example.sozlukapp_ornek.volleydb_version;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import per.example.sozlukapp_ornek.Kelimeler;
import per.example.sozlukapp_ornek.KelimelerAdapter;
import per.example.sozlukapp_ornek.R;
import per.example.sozlukapp_ornek.databinding.MainpageVolleyBinding;

public class Volley_MainPage extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private MainpageVolleyBinding bind;
    private ArrayList<Kelimeler> kelimelerList = new ArrayList<>();
    private KelimelerAdapter adapter;
    private JSONObject jsonObject, kl;
    private JSONArray jArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        bind = MainpageVolleyBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(bind.toolbar2);
        bind.recyclerView2.setHasFixedSize(true);
        bind.recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        kelimeListele();

        bind.fabWordAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kelimeEkle();
            }
        });
    }

    public void kelimeListele(){
        //* VOLLEY İLE WEB SERVİS'TEN KAYITLI KELİMELERİ LİSTELEME İŞLEMİ.
        //!!! DİKKAT: VOLLEY İSTEĞİNDEN ÖNCE "SETADAPTER" KOMUTU YAZILIP ARDINDAN GELEN VERİLER -
        // ARRAYLİST'E YÜKLENDİKTEN SONRA NOTIFYDATASETCHANGED KOMUTU ÇALIŞTIRILIYOR.

        adapter = new KelimelerAdapter(Volley_MainPage.this, kelimelerList);
        bind.recyclerView2.setAdapter(adapter);

        String url = "https://restfuldb.onatsomer.com/kelime/tum_kelimeler.php";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("JSON YANITI KONTROL", response);
                kelimelerList.clear();

                try {
                    jsonObject = new JSONObject(response);
                    jArray = jsonObject.getJSONArray("kelimeler");

                    for (int i = 0; i < jArray.length(); i++) {

                        kl = jArray.getJSONObject(i);
                        int kelimeID = kl.getInt("Word_ID");
                        String kelimeTR = kl.getString("Word_Turkish");
                        String kelimeEN = kl.getString("Word_English");

                        Kelimeler kelime = new Kelimeler(kelimeID, kelimeTR, kelimeEN);
                        kelimelerList.add(kelime);
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON DÖNÜŞÜM HATASI", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY İSTEK HATASI", error.getMessage());
            }
        });

        Volley.newRequestQueue(Volley_MainPage.this).add(request);
    }

    public void kelimeArama(String kelimeAra){

        adapter = new KelimelerAdapter(Volley_MainPage.this, kelimelerList);
        bind.recyclerView2.setAdapter(adapter);
        String url = "https://restfuldb.onatsomer.com/kelime/kelime_arama.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                kelimelerList.clear();
                Log.e("JSON YANIT KONTROL", response);

                try {
                    jsonObject = new JSONObject(response);
                    jArray = jsonObject.getJSONArray("kelimeler");

                    for (int i = 0; i < jArray.length(); i++){

                        kl = jArray.getJSONObject(i);
                        int kelimeID = kl.getInt("Word_ID");
                        String kelimeTR = kl.getString("Word_Turkish");
                        String kelimeEN = kl.getString("Word_English");

                        Kelimeler kelime = new Kelimeler(kelimeID, kelimeTR, kelimeEN);
                        kelimelerList.add(kelime);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {

                    Log.e("JSON DÖNÜŞÜM HATASI", e.getMessage());
                }
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
                params.put("Word_Turkish", kelimeAra);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void kelimeEkle(){

        try {

            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(R.layout.alertview_tasarim, null);

            TextInputLayout txtLayoutWord_Turkish = v.findViewById(R.id.txtLayoutWord_Turkish);
            TextInputLayout txtLayoutWord_English = v.findViewById(R.id.txtLayoutWord_English);
            TextInputEditText editTxtWord_Turkish = v.findViewById(R.id.editTxtWord_Turkish);
            TextInputEditText editTxtWord_English = v.findViewById(R.id.editTxtWord_English);

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Yeni Kelime Ekle");
            ad.setView(v);
            ad.setIcon(R.drawable.outline_addicon_24);

            ad.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String url = "https://restfuldb.onatsomer.com/kelime/insert_kelime.php";
                    String kelimeTR = editTxtWord_Turkish.getText().toString().trim();
                    String kelimeEN = editTxtWord_English.getText().toString().trim();

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("KELİME KAYIT BİLGİSİ", "Kelime başarıyla kaydedildi. " + response);

                            adapter = new KelimelerAdapter(Volley_MainPage.this, kelimelerList);
                            bind.recyclerView2.setAdapter(adapter);
                            Toast.makeText(getApplicationContext(), "Kelime başarıyla kaydedildi.", Toast.LENGTH_LONG).show();
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
                            params.put("Word_Turkish", kelimeTR);
                            params.put("Word_English", kelimeEN);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(Volley_MainPage.this).add(request);

                }
            });

            ad.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }); ad.create().show();

        } catch (Exception e){

            Log.e("KELİME KAYDETME HATASI", e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //* TOOLBAR ÜZERİNDE ARAMA İŞLEVİ İÇİN ÖNCELİKLE TOOLBAR'A OLUŞTURULAN MENÜ TASARIMI İLE ARAMA BUTONU KOYULUYOR.
        //* ARDINDAN TASARIMIN İÇİNDEKİ UYGULAMA DA TIKLANACAK ACTION BUTONU ID'Sİ TANIMLANIYOR.

        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        MenuItem item = menu.findItem(R.id.action_searchMenu);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //* TOOLBAR ÜZERİNDE VOLLEY WEB SERVİSTEN GELEN KELİME ARAMA İŞLEVİ.
        if (kelimelerList != null){

            KelimelerAdapter adapter = new KelimelerAdapter(Volley_MainPage.this, kelimelerList);
            bind.recyclerView2.setAdapter(adapter);
            kelimeArama(newText);
        } else {
            Toast.makeText(this, "Kelime Bulunamadı!", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}