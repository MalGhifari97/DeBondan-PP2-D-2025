/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PeminjamanController;
import controller.BukuController;
import controller.AnggotaController;
import model.Peminjaman;
import model.Buku;
import model.Anggota;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class PeminjamanView extends JFrame {
    private PeminjamanController peminjamanController;
    private BukuController bukuController;
    private AnggotaController anggotaController;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboBuku, comboAnggota;
    private JTextField txtTanggalPinjam, txtTanggalKembali, txtSearch;
    private JButton btnPinjam, btnKembalikan, btnHapus, btnClear, btnSearch;
    private int selectedId = -1;
    
    public PeminjamanView() {
        peminjamanController = new PeminjamanController();
        bukuController = new BukuController();
        anggotaController = new AnggotaController();
        initUI();
        loadData();
        loadComboData();
    }
    
    private void initUI() {
        setTitle("Manajemen Peminjaman");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Peminjaman"));
        
        inputPanel.add(new JLabel("Buku:"));
        comboBuku = new JComboBox<>();
        inputPanel.add(comboBuku);
        
        inputPanel.add(new JLabel("Anggota:"));
        comboAnggota = new JComboBox<>();
        inputPanel.add(comboAnggota);
        
        inputPanel.add(new JLabel("Tanggal Pinjam (YYYY-MM-DD):"));
        txtTanggalPinjam = new JTextField();
        inputPanel.add(txtTanggalPinjam);
        
        inputPanel.add(new JLabel("Tanggal Kembali (YYYY-MM-DD):"));
        txtTanggalKembali = new JTextField();
        txtTanggalKembali.setEnabled(false); // Hanya untuk tampilan
        inputPanel.add(txtTanggalKembali);
        
        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnPinjam = new JButton("Pinjam");
        btnKembalikan = new JButton("Kembalikan");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        JButton btnKembali = new JButton("Kembali ke Menu");
        
        btnPinjam.addActionListener(e -> pinjamBuku());
        btnKembalikan.addActionListener(e -> kembalikanBuku());
        btnHapus.addActionListener(e -> hapusPeminjaman());
        btnClear.addActionListener(e -> clearForm());
        btnKembali.addActionListener(e -> kembaliKeMenu());
        
        buttonPanel.add(btnPinjam);
        buttonPanel.add(btnKembalikan);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnKembali);
        
        // Panel Pencarian
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Pencarian (Judul/Nama): "), BorderLayout.WEST);
        txtSearch = new JTextField();
        btnSearch = new JButton("Cari");
        btnSearch.addActionListener(e -> searchPeminjaman());
        
        JPanel searchInputPanel = new JPanel(new BorderLayout());
        searchInputPanel.add(txtSearch, BorderLayout.CENTER);
        searchInputPanel.add(btnSearch, BorderLayout.EAST);
        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        
        // Tabel
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
            "ID", "ID Buku", "Judul Buku", "ID Anggota", "Nama Anggota", 
            "Tanggal Pinjam", "Tanggal Kembali", "Status"
        });
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                
                // Set combo box berdasarkan ID
                int idBuku = Integer.parseInt(table.getValueAt(row, 1).toString());
                int idAnggota = Integer.parseInt(table.getValueAt(row, 3).toString());
                
                setComboSelection(comboBuku, idBuku);
                setComboSelection(comboAnggota, idAnggota);
                
                txtTanggalPinjam.setText(table.getValueAt(row, 5).toString());
                txtTanggalKembali.setText(table.getValueAt(row, 6) != null ? table.getValueAt(row, 6).toString() : "");
                
                // Enable/disable tombol berdasarkan status
                String status = table.getValueAt(row, 7).toString();
                btnKembalikan.setEnabled(status.equals("Dipinjam"));
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Peminjaman"));
        
        // Layout utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Panel tengah: search di atas tabel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        
        // Set tanggal pinjam otomatis ke hari ini
        txtTanggalPinjam.setText(new Date(System.currentTimeMillis()).toString());
    }
    
    private void loadComboData() {
        comboBuku.removeAllItems();
        comboAnggota.removeAllItems();
        
        // Tambah item kosong
        comboBuku.addItem("-- Pilih Buku --");
        comboAnggota.addItem("-- Pilih Anggota --");
        
        // Load buku
        List<Buku> bukuList = bukuController.getAllBuku();
        for (Buku buku : bukuList) {
            comboBuku.addItem(buku.getIdBuku() + " - " + buku.getJudul() + " (Stok: " + buku.getStok() + ")");
        }
        
        // Load anggota
        List<Anggota> anggotaList = anggotaController.getAllAnggota();
        for (Anggota anggota : anggotaList) {
            comboAnggota.addItem(anggota.getIdAnggota() + " - " + anggota.getNama());
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Peminjaman> peminjamanList = peminjamanController.getAllPeminjaman();
        for (Peminjaman p : peminjamanList) {
            tableModel.addRow(new Object[]{
                p.getIdPeminjaman(),
                p.getIdBuku(),
                p.getJudulBuku(),
                p.getIdAnggota(),
                p.getNamaAnggota(),
                p.getTanggalPinjam(),
                p.getTanggalKembali(),
                p.getStatus()
            });
        }
    }
    
    private void pinjamBuku() {
        try {
            // Ambil ID dari combo box
            if (comboBuku.getSelectedIndex() <= 0 || comboAnggota.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Pilih buku dan anggota!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String bukuItem = comboBuku.getSelectedItem().toString();
            String anggotaItem = comboAnggota.getSelectedItem().toString();
            
            int idBuku = Integer.parseInt(bukuItem.split(" - ")[0]);
            int idAnggota = Integer.parseInt(anggotaItem.split(" - ")[0]);
            
            String tanggalStr = txtTanggalPinjam.getText().trim();
            Date tanggalPinjam = Date.valueOf(tanggalStr); // Format harus YYYY-MM-DD
            
            boolean success = peminjamanController.tambahPeminjaman(idBuku, idAnggota, tanggalPinjam);
            if (success) {
                JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam!");
                clearForm();
                loadData();
                loadComboData(); // Refresh stok di combo
            } else {
                JOptionPane.showMessageDialog(this, "Gagal meminjam buku. Stok mungkin habis.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah! Gunakan YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void kembalikanBuku() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih peminjaman yang akan dikembalikan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String tanggalStr = JOptionPane.showInputDialog(this, "Masukkan tanggal pengembalian (YYYY-MM-DD):", new Date(System.currentTimeMillis()).toString());
            if (tanggalStr == null || tanggalStr.trim().isEmpty()) return;
            
            Date tanggalKembali = Date.valueOf(tanggalStr.trim());
            boolean success = peminjamanController.kembalikanBuku(selectedId, tanggalKembali);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan!");
                clearForm();
                loadData();
                loadComboData(); // Refresh stok
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengembalikan buku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusPeminjaman() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih peminjaman yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data peminjaman ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = peminjamanController.deletePeminjaman(selectedId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Data peminjaman berhasil dihapus!");
                clearForm();
                loadData();
                loadComboData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data peminjaman.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchPeminjaman() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Peminjaman> peminjamanList = peminjamanController.searchPeminjaman(keyword);
        for (Peminjaman p : peminjamanList) {
            tableModel.addRow(new Object[]{
                p.getIdPeminjaman(),
                p.getIdBuku(),
                p.getJudulBuku(),
                p.getIdAnggota(),
                p.getNamaAnggota(),
                p.getTanggalPinjam(),
                p.getTanggalKembali(),
                p.getStatus()
            });
        }
    }
    
    private void clearForm() {
        selectedId = -1;
        comboBuku.setSelectedIndex(0);
        comboAnggota.setSelectedIndex(0);
        txtTanggalPinjam.setText(new Date(System.currentTimeMillis()).toString());
        txtTanggalKembali.setText("");
        txtSearch.setText("");
        table.clearSelection();
        btnKembalikan.setEnabled(true);
        loadData();
    }
    
    // Helper untuk set combo box berdasarkan ID
    private void setComboSelection(JComboBox<String> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (item.startsWith(id + " - ")) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(0);
    }
    
    private void kembaliKeMenu() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        this.dispose();
    }
}