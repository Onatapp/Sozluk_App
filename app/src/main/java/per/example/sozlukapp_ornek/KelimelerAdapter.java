package per.example.sozlukapp_ornek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KelimelerAdapter extends RecyclerView.Adapter<KelimelerAdapter.cardviewTasarimTutucu> {
    private Context c;
    private List<Kelimeler> kelimelerList;

    public KelimelerAdapter(Context context, List<Kelimeler> kelimelerList) {
        this.c = context;
        this.kelimelerList = kelimelerList;
    }

    public class cardviewTasarimTutucu extends RecyclerView.ViewHolder {
        private TextView txtTurkceKelime, txtİngilizceKelime;
        private CardView cardViewWord;

        public cardviewTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            txtTurkceKelime = itemView.findViewById(R.id.txtTurkceKelime);
            txtİngilizceKelime = itemView.findViewById(R.id.txtİngilizceKelime);
            cardViewWord = itemView.findViewById(R.id.cardViewWord);
        }
    }

    @NonNull
    @Override
    public cardviewTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //* Yukarıda tanımlanan CardView Nesnelerinin bulunduğu card_kelime Tasarım Dosyası burada inflate ediliyor yani tanımlanıyor.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_kelime, parent, false);
        return new cardviewTasarimTutucu(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cardviewTasarimTutucu holder, int position) {

        final Kelimeler kelime = kelimelerList.get(position);

        holder.txtTurkceKelime.setText(kelime.getKelime_turkce());
        holder.txtİngilizceKelime.setText(kelime.getKelime_ingilizce());

        holder.cardViewWord.setOnClickListener(view -> {

            Intent intentPage = new Intent(c, DetayActivity.class);
            intentPage.putExtra("Nesne", kelime);
            intentPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intentPage);
           // ((Activity)c).finish(); Bu sayfayı backstage'den silme kullanımı.
        });
    }

    @Override
    public int getItemCount() {
        //* kelimelerListesinin boyutunu dinamik olarak almayı sağlıyor.
        return kelimelerList.size();
    }

}
