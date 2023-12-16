package View;

import DataBase.AuthenticationManager;
import DataBase.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JPanel loginPanel;
    private JTextField loginKullaniciAdi;
    private JPasswordField loginSifre;
    private JButton girisYapButton;
    private JButton kayitOlButton;
    private JButton sifremiUnuttumButton;
    private JPanel loginForm;

    public Login() {
        add(loginPanel);
        setSize(400, 400);
        setTitle("Kütüphane Uygulaması");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        girisYapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kullaniciAdi, sifre;
                kullaniciAdi = "mirac"; //loginKullaniciAdi.getText();
                sifre = "1234"; //String.valueOf(loginSifre.getPassword());
                if (AuthenticationManager.authenticateUser(kullaniciAdi, sifre)) {
                    User loggedInUser = AuthenticationManager.getLoggedInUser();
                    // Giriş başarılı olduğunda StudentHomepage penceresini oluştur
                    StudentHomepage studentHomepage = new StudentHomepage(loggedInUser);
                    // Mevcut pencereyi gizle
                    setVisible(false);
                    // Yeni pencereyi görünür kıl
                    studentHomepage.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Giriş başarısız!");
                }
            }
        });

        kayitOlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kayıt ol butonuna tıklandığında Register penceresini oluştur
                Register register = new Register();

                // Yeni pencereyi görünür kıl
                register.setVisible(true);
            }
        });

    }
}
