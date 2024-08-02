package per.example.sozlukapp_ornek;

import java.io.Serializable;

public class Kelimeler implements Serializable {
    private int kelime_id;
    private String kelime_turkce;
    private String kelime_ingilizce;

    public Kelimeler() {
    }

    public Kelimeler(int kelime_id, String kelime_turkce, String kelime_ingilizce) {
        this.kelime_id = kelime_id;
        this.kelime_turkce = kelime_turkce;
        this.kelime_ingilizce = kelime_ingilizce;
    }

    public int getKelime_id() {
        return kelime_id;
    }

    public void setKelime_id(int kelime_id) {
        this.kelime_id = kelime_id;
    }

    public String getKelime_turkce() {
        return kelime_turkce;
    }

    public void setKelime_turkce(String kelime_turkce) {
        this.kelime_turkce = kelime_turkce;
    }

    public String getKelime_ingilizce() {
        return kelime_ingilizce;
    }

    public void setKelime_ingilizce(String kelime_ingilizce) {
        this.kelime_ingilizce = kelime_ingilizce;
    }
}
