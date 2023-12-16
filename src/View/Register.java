package View;

import DataBase.AuthenticationManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private JTextField kullaniciAdiField;
    private JPasswordField registerSifre;
    private JComboBox<String> registerKullaniciTuru;
    private JButton kayitOlButton;
    private JPanel registerPanel;
    private JTextField registerAd;
    private JTextField registerSoyad;
    private JTextField registerKullaniciAdi;

    public Register() {
        add(registerPanel);
        setTitle("Kütüphane Uygulaması - Kayıt Ol");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        kayitOlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ad = registerAd.getText();
                String soyad = registerSoyad.getText();
                String kullaniciAdi = registerKullaniciAdi.getText();
                String sifre = String.valueOf(registerSifre.getPassword());
                String kullaniciTuru = (String) registerKullaniciTuru.getSelectedItem();

                if (AuthenticationManager.registerUser(kullaniciAdi, sifre, kullaniciTuru,ad,soyad)) {
                    JOptionPane.showMessageDialog(null, "Kayıt başarılı!");
                    dispose(); // Kayıt işlemi başarılıysa pencereyi kapat
                    // Burada giriş ekranına yönlendirme işlemini gerçekleştirin
                    Login login = new Login();
                    login.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Kayıt başarısız! Kullanıcı adı zaten mevcut.");
                }
            }
        });

    }


}
