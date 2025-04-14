package com.example.myapplication.Models;

import androidx.annotation.NonNull;

public class Absen {
    private String id;
    private String nama;
    private String tanggal;
    private String waktu;
    private String status;

    // Wajib: Constructor kosong untuk Firebase
    public Absen() {
    }

    // Constructor untuk input manual
    public Absen(String nama, String tanggal, String waktu, String status) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.status = status;
    }

    // Getter & Setter dengan validasi dasar
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id != null && !id.isEmpty()) {
            this.id = id;
        }
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if(nama != null && !nama.trim().isEmpty()) {
            this.nama = nama.trim();
        }
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        if(tanggal != null && !tanggal.trim().isEmpty()) {
            this.tanggal = tanggal.trim();
        }
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        if(waktu != null && !waktu.trim().isEmpty()) {
            this.waktu = waktu.trim();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status != null && (status.equalsIgnoreCase("Hadir") ||
                status.equalsIgnoreCase("Tidak Hadir"))) {
            this.status = status;
        }
    }

    // Untuk debugging
    @NonNull
    @Override
    public String toString() {
        return "Absen{" +
                "id='" + id + '\'' +
                ", nama='" + nama + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", waktu='" + waktu + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}