package com.medicare.prohealthymedicare.model;

public class DataDokterModels {

    private String nama, jenispraktik,haripraktik,jampraktik,foto,cp;

    public DataDokterModels(String nama, String jenispraktik, String haripraktik, String jampraktik, String foto, String cp) {
        this.nama = nama;
        this.jenispraktik = jenispraktik;
        this.haripraktik = haripraktik;
        this.jampraktik = jampraktik;
        this.foto = foto;
        this.cp = cp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenispraktik() {
        return jenispraktik;
    }

    public void setJenispraktik(String jenispraktik) {
        this.jenispraktik = jenispraktik;
    }

    public String getHaripraktik() {
        return haripraktik;
    }

    public void setHaripraktik(String haripraktik) {
        this.haripraktik = haripraktik;
    }

    public String getJampraktik() {
        return jampraktik;
    }

    public void setJampraktik(String jampraktik) {
        this.jampraktik = jampraktik;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }
}