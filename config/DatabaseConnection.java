/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Register driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // URL database
                String url = "jdbc:mysql://localhost:3306/perpustakaan_db";
                String user = "root";
                String password = "";
                
                // Buat koneksi
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi database berhasil.");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver MySQL tidak ditemukan: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Koneksi database gagal: " + e.getMessage());
            }
        }
        return connection;
    }
    
    // Method untuk close connection (optional)
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Koneksi database ditutup.");
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}