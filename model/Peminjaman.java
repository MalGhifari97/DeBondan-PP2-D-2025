/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class Peminjaman {
    private int idPeminjaman;
    private int idBuku;
    private int idAnggota;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String status;
    private String judulBuku;
    private String namaAnggota;
    
    public Peminjaman() {}
    
    public int getIdPeminjaman() { return idPeminjaman; }
    public void setIdPeminjaman(int idPeminjaman) { this.idPeminjaman = idPeminjaman; }
    
    public int getIdBuku() { return idBuku; }
    public void setIdBuku(int idBuku) { this.idBuku = idBuku; }
    
    public int getIdAnggota() { return idAnggota; }
    public void setIdAnggota(int idAnggota) { this.idAnggota = idAnggota; }
    
    public Date getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(Date tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }
    
    public Date getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(Date tanggalKembali) { this.tanggalKembali = tanggalKembali; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getJudulBuku() { return judulBuku; }
    public void setJudulBuku(String judulBuku) { this.judulBuku = judulBuku; }
    
    public String getNamaAnggota() { return namaAnggota; }
    public void setNamaAnggota(String namaAnggota) { this.namaAnggota = namaAnggota; }
}