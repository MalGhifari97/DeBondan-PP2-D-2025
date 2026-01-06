/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AnggotaController;
import model.Anggota;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AnggotaView extends JFrame {
    private AnggotaController anggotaController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNim, txtNama, txtJurusan, txtTelp, txtSearch;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear, btnSearch;
    private int selectedId = -1;
    
    public AnggotaView() {
        anggotaController = new AnggotaController();
        initUI();
        loadData();
    }
    
    private void initUI() {
        setTitle("Manajemen Anggota");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Anggota"));
        
        inputPanel.add(new JLabel("NIM/NIS:"));
        txtNim = new JTextField();
        inputPanel.add(txtNim);
        
        inputPanel.add(new JLabel("Nama:"));
        txtNama = new JTextField();
        inputPanel.add(txtNama);
        
        inputPanel.add(new JLabel("Jurusan:"));
        txtJurusan = new JTextField();
        inputPanel.add(txtJurusan);
        
        inputPanel.add(new JLabel("No. Telp:"));
        txtTelp = new JTextField();
        inputPanel.add(txtTelp);
        
        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        JButton btnKembali = new JButton("Kembali ke Menu");
        
        btnTambah.addActionListener(e -> tambahAnggota());
        btnUpdate.addActionListener(e -> updateAnggota());
        btnHapus.addActionListener(e -> hapusAnggota());
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
        btnSearch.addActionListener(e -> searchAnggota());
        
        JPanel searchInputPanel = new JPanel(new BorderLayout());
        searchInputPanel.add(txtSearch, BorderLayout.CENTER);
        searchInputPanel.add(btnSearch, BorderLayout.EAST);
        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        
        // Tabel
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "NIM/NIS", "Nama", "Jurusan", "No. Telp"});
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                txtNim.setText(table.getValueAt(row, 1).toString());
                txtNama.setText(table.getValueAt(row, 2).toString());
                txtJurusan.setText(table.getValueAt(row, 3).toString());
                txtTelp.setText(table.getValueAt(row, 4).toString());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Anggota"));
        
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
        List<Anggota> anggotaList = anggotaController.getAllAnggota();
        for (Anggota anggota : anggotaList) {
            tableModel.addRow(new Object[]{
                anggota.getIdAnggota(),
                anggota.getNimNis(),
                anggota.getNama(),
                anggota.getJurusan(),
                anggota.getNoTelp()
            });
        }
    }
    
    private void tambahAnggota() {
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String jurusan = txtJurusan.getText().trim();
        String telp = txtTelp.getText().trim();
        
        // Validasi
        if (nim.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM dan Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = anggotaController.tambahAnggota(nim, nama, jurusan, telp);
        if (success) {
            JOptionPane.showMessageDialog(this, "Anggota berhasil ditambahkan!");
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambah anggota. NIM mungkin sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateAnggota() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang akan diupdate!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String jurusan = txtJurusan.getText().trim();
        String telp = txtTelp.getText().trim();
        
        if (nim.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM dan Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = anggotaController.updateAnggota(selectedId, nim, nama, jurusan, telp);
        if (success) {
            JOptionPane.showMessageDialog(this, "Anggota berhasil diupdate!");
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate anggota. NIM mungkin sudah digunakan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusAnggota() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus anggota ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = anggotaController.deleteAnggota(selectedId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Anggota berhasil dihapus!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus anggota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchAnggota() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Anggota> anggotaList = anggotaController.searchAnggota(keyword);
        for (Anggota anggota : anggotaList) {
            tableModel.addRow(new Object[]{
                anggota.getIdAnggota(),
                anggota.getNimNis(),
                anggota.getNama(),
                anggota.getJurusan(),
                anggota.getNoTelp()
            });
        }
    }
    
    private void clearForm() {
        selectedId = -1;
        txtNim.setText("");
        txtNama.setText("");
        txtJurusan.setText("");
        txtTelp.setText("");
        txtSearch.setText("");
        table.clearSelection();
        loadData();
    }
    
    private void kembaliKeMenu() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        this.dispose();
    }
}