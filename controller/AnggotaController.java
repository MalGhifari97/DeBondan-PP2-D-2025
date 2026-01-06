/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;

import config.DatabaseConnection;
import model.Anggota;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaController {
    private Connection connection;
    
    public AnggotaController() {
        connection = DatabaseConnection.getConnection();
    }
    
    // CREATE - Tambah anggota
    public boolean tambahAnggota(String nimNis, String nama, String jurusan, String noTelp) {
        if (nimNis.isEmpty() || nama.isEmpty()) {
            return false;
        }
        
        // Cek NIM/NIS unik
        if (getAnggotaByNimNis(nimNis) != null) {
            return false;
        }
        
        String sql = "INSERT INTO anggota (nim_nis, nama, jurusan, no_telp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nimNis);
            stmt.setString(2, nama);
            stmt.setString(3, jurusan);
            stmt.setString(4, noTelp);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ ALL - Ambil semua anggota
    public List<Anggota> getAllAnggota() {
        List<Anggota> listAnggota = new ArrayList<>();
        String sql = "SELECT * FROM anggota ORDER BY id_anggota DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Anggota anggota = new Anggota();
                anggota.setIdAnggota(rs.getInt("id_anggota"));
                anggota.setNimNis(rs.getString("nim_nis"));
                anggota.setNama(rs.getString("nama"));
                anggota.setJurusan(rs.getString("jurusan"));
                anggota.setNoTelp(rs.getString("no_telp"));
                listAnggota.add(anggota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listAnggota;
    }
    
    // UPDATE - Update anggota
    public boolean updateAnggota(int id, String nimNis, String nama, String jurusan, String noTelp) {
        if (nimNis.isEmpty() || nama.isEmpty()) {
            return false;
        }
        
        // Cek NIM/NIS unik (kecuali untuk anggota ini sendiri)
        Anggota anggotaExist = getAnggotaByNimNis(nimNis);
        if (anggotaExist != null && anggotaExist.getIdAnggota() != id) {
            return false;
        }
        
        String sql = "UPDATE anggota SET nim_nis=?, nama=?, jurusan=?, no_telp=? WHERE id_anggota=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nimNis);
            stmt.setString(2, nama);
            stmt.setString(3, jurusan);
            stmt.setString(4, noTelp);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Hapus anggota
    public boolean deleteAnggota(int id) {
        String sql = "DELETE FROM anggota WHERE id_anggota=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // SEARCH - Cari anggota
    public List<Anggota> searchAnggota(String keyword) {
        List<Anggota> listAnggota = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE nama LIKE ? OR nim_nis LIKE ? OR jurusan LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Anggota anggota = new Anggota();
                anggota.setIdAnggota(rs.getInt("id_anggota"));
                anggota.setNimNis(rs.getString("nim_nis"));
                anggota.setNama(rs.getString("nama"));
                anggota.setJurusan(rs.getString("jurusan"));
                anggota.setNoTelp(rs.getString("no_telp"));
                listAnggota.add(anggota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listAnggota;
    }
    
    // GET BY ID - Ambil anggota berdasarkan ID
    public Anggota getAnggotaById(int id) {
        Anggota anggota = null;
        String sql = "SELECT * FROM anggota WHERE id_anggota = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                anggota = new Anggota();
                anggota.setIdAnggota(rs.getInt("id_anggota"));
                anggota.setNimNis(rs.getString("nim_nis"));
                anggota.setNama(rs.getString("nama"));
                anggota.setJurusan(rs.getString("jurusan"));
                anggota.setNoTelp(rs.getString("no_telp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggota;
    }
    
    // GET BY NIM/NIS (private helper)
    private Anggota getAnggotaByNimNis(String nimNis) {
        Anggota anggota = null;
        String sql = "SELECT * FROM anggota WHERE nim_nis = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nimNis);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                anggota = new Anggota();
                anggota.setIdAnggota(rs.getInt("id_anggota"));
                anggota.setNimNis(rs.getString("nim_nis"));
                anggota.setNama(rs.getString("nama"));
                anggota.setJurusan(rs.getString("jurusan"));
                anggota.setNoTelp(rs.getString("no_telp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggota;
    }
}