/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import view.BukuView;
import view.AnggotaView;
import view.PeminjamanView;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Sistem Perpustakaan");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        // Judul
        JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Perpustakaan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        // Panel tombol di tengah
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton bukuButton = new JButton("Data Buku");
        JButton anggotaButton = new JButton("Data Anggota");
        JButton peminjamanButton = new JButton("Data Peminjaman");
        JButton exitButton = new JButton("Keluar");

        Dimension buttonSize = new Dimension(200, 42); // sedikit lebih besar, tapi tidak terlalu besar
        Font buttonFont = new Font("Arial", Font.BOLD, 16); // tulisan diperbesar
        bukuButton.setPreferredSize(buttonSize);
        anggotaButton.setPreferredSize(buttonSize);
        peminjamanButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        bukuButton.setFont(buttonFont);
        anggotaButton.setFont(buttonFont);
        peminjamanButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        bukuButton.addActionListener(e -> openBukuView());
        anggotaButton.addActionListener(e -> openAnggotaView());
        peminjamanButton.addActionListener(e -> openPeminjamanView());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(bukuButton);
        buttonPanel.add(anggotaButton);
        buttonPanel.add(peminjamanButton);
        buttonPanel.add(exitButton);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(buttonPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }
    
    private void openBukuView() {
        BukuView bukuView = new BukuView();
        bukuView.setVisible(true);
        this.dispose();
    }
    
    private void openAnggotaView() {
        AnggotaView anggotaView = new AnggotaView();
        anggotaView.setVisible(true);
        this.dispose();
    }
    
    private void openPeminjamanView() {
        PeminjamanView peminjamanView = new PeminjamanView();
        peminjamanView.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}