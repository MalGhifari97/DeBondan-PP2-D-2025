/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.BukuController;
import model.Buku;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BukuView extends JFrame {
    private BukuController bukuController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtIsbn, txtJudul, txtPenulis, txtPenerbit, txtTahun, txtStok, txtSearch;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear, btnSearch;
    private int selectedId = -1;
    
    public BukuView() {
        bukuController = new BukuController();
        initUI();
        loadData();
    }
    
    private void initUI() {
        setTitle("Manajemen Buku");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Buku"));
        
        inputPanel.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        inputPanel.add(txtIsbn);
        
        inputPanel.add(new JLabel("Judul:"));
        txtJudul = new JTextField();
        inputPanel.add(txtJudul);
        
        inputPanel.add(new JLabel("Penulis:"));
        txtPenulis = new JTextField();
        inputPanel.add(txtPenulis);
        
        inputPanel.add(new JLabel("Penerbit:"));
        txtPenerbit = new JTextField();
        inputPanel.add(txtPenerbit);
        
        inputPanel.add(new JLabel("Tahun Terbit:"));
        txtTahun = new JTextField();
        inputPanel.add(txtTahun);
        
        inputPanel.add(new JLabel("Stok:"));
        txtStok = new JTextField();
        inputPanel.add(txtStok);
        
        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        JButton btnKembali = new JButton("Kembali ke Menu");
        
        btnTambah.addActionListener(e -> tambahBuku());
        btnUpdate.addActionListener(e -> updateBuku());
        btnHapus.addActionListener(e -> hapusBuku());
        btnClear.addActionListener(e -> clearForm());
        btnKembali.addActionListener(e -> kembaliKeMenu());
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnKembali);
        
        
        // Panel Pencarian
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Pencarian: "), BorderLayout.WEST);
        txtSearch = new JTextField();
        btnSearch = new JButton("Cari");
        btnSearch.addActionListener(e -> searchBuku());
        
        JPanel searchInputPanel = new JPanel(new BorderLayout());
        searchInputPanel.add(txtSearch, BorderLayout.CENTER);
        searchInputPanel.add(btnSearch, BorderLayout.EAST);
        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        
        // Tabel
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "ISBN", "Judul", "Penulis", "Penerbit", "Tahun", "Stok"});
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                txtIsbn.setText(table.getValueAt(row, 1).toString());
                txtJudul.setText(table.getValueAt(row, 2).toString());
                txtPenulis.setText(table.getValueAt(row, 3).toString());
                txtPenerbit.setText(table.getValueAt(row, 4).toString());
                txtTahun.setText(table.getValueAt(row, 5).toString());
                txtStok.setText(table.getValueAt(row, 6).toString());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Buku"));
        
        // Layout utama pada panel konten
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
       
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Buku> bukuList = bukuController.getAllBuku();
        for (Buku buku : bukuList) {
            tableModel.addRow(new Object[]{
                buku.getIdBuku(),
                buku.getIsbn(),
                buku.getJudul(),
                buku.getPenulis(),
                buku.getPenerbit(),
                buku.getTahunTerbit(),
                buku.getStok()
            });
        }
    }
    
    private void tambahBuku() {
        try {
            String isbn = txtIsbn.getText().trim();
            String judul = txtJudul.getText().trim();
            String penulis = txtPenulis.getText().trim();
            String penerbit = txtPenerbit.getText().trim();
            int tahun = Integer.parseInt(txtTahun.getText().trim());
            int stok = Integer.parseInt(txtStok.getText().trim());
            
            // Validasi
            if (isbn.isEmpty() || judul.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN dan Judul harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (tahun < 1000 || tahun > 2100) {
                JOptionPane.showMessageDialog(this, "Tahun terbit tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (stok < 0) {
                JOptionPane.showMessageDialog(this, "Stok tidak boleh negatif!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = bukuController.tambahBuku(isbn, judul, penulis, penerbit, tahun, stok);
            if (success) {
                JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah buku. ISBN mungkin sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tahun dan Stok harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBuku() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang akan diupdate!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String isbn = txtIsbn.getText().trim();
            String judul = txtJudul.getText().trim();
            String penulis = txtPenulis.getText().trim();
            String penerbit = txtPenerbit.getText().trim();
            int tahun = Integer.parseInt(txtTahun.getText().trim());
            int stok = Integer.parseInt(txtStok.getText().trim());
            
            // Validasi sama seperti tambah
            if (isbn.isEmpty() || judul.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN dan Judul harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = bukuController.updateBuku(selectedId, isbn, judul, penulis, penerbit, tahun, stok);
            if (success) {
                JOptionPane.showMessageDialog(this, "Buku berhasil diupdate!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate buku. ISBN mungkin sudah digunakan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tahun dan Stok harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusBuku() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus buku ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bukuController.deleteBuku(selectedId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus buku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchBuku() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Buku> bukuList = bukuController.searchBuku(keyword);
        for (Buku buku : bukuList) {
            tableModel.addRow(new Object[]{
                buku.getIdBuku(),
                buku.getIsbn(),
                buku.getJudul(),
                buku.getPenulis(),
                buku.getPenerbit(),
                buku.getTahunTerbit(),
                buku.getStok()
            });
        }
    }
    
    private void clearForm() {
        selectedId = -1;
        txtIsbn.setText("");
        txtJudul.setText("");
        txtPenulis.setText("");
        txtPenerbit.setText("");
        txtTahun.setText("");
        txtStok.setText("");
        txtSearch.setText("");
        table.clearSelection();
        loadData();
    }
    
    private void kembaliKeMenu() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        this.dispose(); // Tutup window BukuView
    }
}