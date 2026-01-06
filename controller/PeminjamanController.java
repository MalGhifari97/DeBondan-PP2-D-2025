/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Peminjaman;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanController {
    private Connection connection;
    
    public PeminjamanController() {
        connection = DatabaseConnection.getConnection();
    }
    
    // CREATE - Tambah peminjaman
    public boolean tambahPeminjaman(int idBuku, int idAnggota, Date tanggalPinjam) {
        // Validasi
        if (idBuku <= 0 || idAnggota <= 0) {
            return false;
        }
        
        // Cek stok buku
        if (!cekStokBuku(idBuku)) {
            return false; // Stok habis
        }
        
        String sql = "INSERT INTO peminjaman (id_buku, id_anggota, tanggal_pinjam, status) VALUES (?, ?, ?, 'Dipinjam')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            stmt.setInt(2, idAnggota);
            stmt.setDate(3, tanggalPinjam);
            stmt.executeUpdate();
            
            // Kurangi stok buku
            kurangiStokBuku(idBuku);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ ALL - Ambil semua peminjaman dengan join
    public List<Peminjaman> getAllPeminjaman() {
        List<Peminjaman> listPeminjaman = new ArrayList<>();
        String sql = "SELECT p.*, b.judul, a.nama " +
                     "FROM peminjaman p " +
                     "JOIN buku b ON p.id_buku = b.id_buku " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "ORDER BY p.id_peminjaman DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Peminjaman p = new Peminjaman();
                p.setIdPeminjaman(rs.getInt("id_peminjaman"));
                p.setIdBuku(rs.getInt("id_buku"));
                p.setIdAnggota(rs.getInt("id_anggota"));
                p.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                p.setTanggalKembali(rs.getDate("tanggal_kembali"));
                p.setStatus(rs.getString("status"));
                p.setJudulBuku(rs.getString("judul"));
                p.setNamaAnggota(rs.getString("nama"));
                listPeminjaman.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPeminjaman;
    }
    
    // UPDATE - Kembalikan buku
    public boolean kembalikanBuku(int idPeminjaman, Date tanggalKembali) {
        String sql = "UPDATE peminjaman SET tanggal_kembali = ?, status = 'Dikembalikan' WHERE id_peminjaman = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, tanggalKembali);
            stmt.setInt(2, idPeminjaman);
            stmt.executeUpdate();
            
            // Tambah stok buku
            tambahStokBuku(getIdBukuFromPeminjaman(idPeminjaman));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Hapus peminjaman
    public boolean deletePeminjaman(int id) {
        // Sebelum hapus, kembalikan stok jika masih dipinjam
        String status = getStatusPeminjaman(id);
        if (status != null && status.equals("Dipinjam")) {
            tambahStokBuku(getIdBukuFromPeminjaman(id));
        }
        
        String sql = "DELETE FROM peminjaman WHERE id_peminjaman = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // SEARCH - Cari peminjaman
    public List<Peminjaman> searchPeminjaman(String keyword) {
        List<Peminjaman> listPeminjaman = new ArrayList<>();
        String sql = "SELECT p.*, b.judul, a.nama " +
                     "FROM peminjaman p " +
                     "JOIN buku b ON p.id_buku = b.id_buku " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "WHERE b.judul LIKE ? OR a.nama LIKE ? OR p.status LIKE ? " +
                     "ORDER BY p.id_peminjaman DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Peminjaman p = new Peminjaman();
                p.setIdPeminjaman(rs.getInt("id_peminjaman"));
                p.setIdBuku(rs.getInt("id_buku"));
                p.setIdAnggota(rs.getInt("id_anggota"));
                p.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                p.setTanggalKembali(rs.getDate("tanggal_kembali"));
                p.setStatus(rs.getString("status"));
                p.setJudulBuku(rs.getString("judul"));
                p.setNamaAnggota(rs.getString("nama"));
                listPeminjaman.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPeminjaman;
    }
    
    // GET BY ID - Ambil peminjaman berdasarkan ID
    public Peminjaman getPeminjamanById(int id) {
        Peminjaman peminjaman = null;
        String sql = "SELECT * FROM peminjaman WHERE id_peminjaman = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                peminjaman = new Peminjaman();
                peminjaman.setIdPeminjaman(rs.getInt("id_peminjaman"));
                peminjaman.setIdBuku(rs.getInt("id_buku"));
                peminjaman.setIdAnggota(rs.getInt("id_anggota"));
                peminjaman.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                peminjaman.setTanggalKembali(rs.getDate("tanggal_kembali"));
                peminjaman.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peminjaman;
    }
    
    // ========== HELPER METHODS ==========
    
    private boolean cekStokBuku(int idBuku) {
        String sql = "SELECT stok FROM buku WHERE id_buku = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stok") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void kurangiStokBuku(int idBuku) {
        String sql = "UPDATE buku SET stok = stok - 1 WHERE id_buku = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void tambahStokBuku(int idBuku) {
        String sql = "UPDATE buku SET stok = stok + 1 WHERE id_buku = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int getIdBukuFromPeminjaman(int idPeminjaman) {
        String sql = "SELECT id_buku FROM peminjaman WHERE id_peminjaman = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPeminjaman);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_buku");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private String getStatusPeminjaman(int idPeminjaman) {
        String sql = "SELECT status FROM peminjaman WHERE id_peminjaman = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPeminjaman);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}