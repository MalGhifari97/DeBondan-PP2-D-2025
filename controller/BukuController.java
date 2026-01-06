/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Buku;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuController {
    private Connection connection;
    
    public BukuController() {
        connection = DatabaseConnection.getConnection();
    }
    
    // CREATE - Tambah Buku
    public boolean tambahBuku(String isbn, String judul, String penulis, 
                               String penerbit, int tahunTerbit, int stok) {
        if (isbn.isEmpty() || judul.isEmpty()) {
            return false;
        }
        
        // Cek ISBN unik
        if (getBukuByIsbn(isbn) != null) {
            return false; // ISBN sudah ada
        }
        
        String sql = "INSERT INTO buku (isbn, judul, penulis, penerbit, tahun_terbit, stok) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setString(2, judul);
            stmt.setString(3, penulis);
            stmt.setString(4, penerbit);
            stmt.setInt(5, tahunTerbit);
            stmt.setInt(6, stok);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ ALL - Ambil semua buku
    public List<Buku> getAllBuku() {
        List<Buku> listBuku = new ArrayList<>();
        String sql = "SELECT * FROM buku ORDER BY id_buku DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdBuku(rs.getInt("id_buku"));
                buku.setIsbn(rs.getString("isbn"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenulis(rs.getString("penulis"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setTahunTerbit(rs.getInt("tahun_terbit"));
                buku.setStok(rs.getInt("stok"));
                listBuku.add(buku);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listBuku;
    }
    
    // UPDATE - Update buku
    public boolean updateBuku(int id, String isbn, String judul, String penulis, 
                               String penerbit, int tahunTerbit, int stok) {
        if (isbn.isEmpty() || judul.isEmpty()) {
            return false;
        }
        
        // Cek ISBN unik (kecuali untuk buku ini sendiri)
        Buku bukuExist = getBukuByIsbn(isbn);
        if (bukuExist != null && bukuExist.getIdBuku() != id) {
            return false; // ISBN sudah dipakai buku lain
        }
        
        String sql = "UPDATE buku SET isbn=?, judul=?, penulis=?, penerbit=?, tahun_terbit=?, stok=? WHERE id_buku=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setString(2, judul);
            stmt.setString(3, penulis);
            stmt.setString(4, penerbit);
            stmt.setInt(5, tahunTerbit);
            stmt.setInt(6, stok);
            stmt.setInt(7, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Hapus buku
    public boolean deleteBuku(int id) {
        String sql = "DELETE FROM buku WHERE id_buku=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // SEARCH - Cari buku
    public List<Buku> searchBuku(String keyword) {
        List<Buku> listBuku = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE judul LIKE ? OR isbn LIKE ? OR penulis LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdBuku(rs.getInt("id_buku"));
                buku.setIsbn(rs.getString("isbn"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenulis(rs.getString("penulis"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setTahunTerbit(rs.getInt("tahun_terbit"));
                buku.setStok(rs.getInt("stok"));
                listBuku.add(buku);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listBuku;
    }
    
    // GET BY ID - Ambil buku berdasarkan ID
    public Buku getBukuById(int id) {
        Buku buku = null;
        String sql = "SELECT * FROM buku WHERE id_buku = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                buku = new Buku();
                buku.setIdBuku(rs.getInt("id_buku"));
                buku.setIsbn(rs.getString("isbn"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenulis(rs.getString("penulis"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setTahunTerbit(rs.getInt("tahun_terbit"));
                buku.setStok(rs.getInt("stok"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buku;
    }
    
    // GET BY ISBN (private helper)
    private Buku getBukuByIsbn(String isbn) {
        Buku buku = null;
        String sql = "SELECT * FROM buku WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                buku = new Buku();
                buku.setIdBuku(rs.getInt("id_buku"));
                buku.setIsbn(rs.getString("isbn"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenulis(rs.getString("penulis"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setTahunTerbit(rs.getInt("tahun_terbit"));
                buku.setStok(rs.getInt("stok"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buku;
    }
}