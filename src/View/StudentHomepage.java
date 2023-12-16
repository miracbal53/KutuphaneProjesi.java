package View;

import javax.swing.*;
import DataBase.AuthenticationManager;
import DataBase.Book;
import DataBase.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

public class StudentHomepage extends JFrame {
    private JPanel studentHomepagePanel;
    private JButton kitapAraButton;
    private JButton cikisYapButton;
    private JButton bilgileriniGuncelleButton;
    private JButton oduncKitapAlButton;
    private JButton kitapIadeEtButton;
    private JLabel ad;
    private JLabel soyad;
    private JLabel tarih;
    private JLabel saat;


    public StudentHomepage(User loggedInUser) {
        add(studentHomepagePanel);
        setTitle("Kütüphane Uygulaması - Anasayfa");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DisplayUserInfo();
        kitapAraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchBook searchBook = new SearchBook();
                searchBook.setVisible(true);
            }
        });
        oduncKitapAlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = new Book();
                List<Book> tumKitaplar = book.getAll();
                SearchBook searchBook = new SearchBook();
                searchBook.setVisible(true);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        kitapIadeEtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void DisplayUserInfo() {

        if (loggedInUser != null) {
            String isim = loggedInUser.getName();
            String soyisim = loggedInUser.getSurname();
            LocalTime anlikSaat = LocalTime.now();
            LocalDate anlikTarih = LocalDate.now();
            ad.setText("Ad: " + isim);
            soyad.setText("Soyad: " + soyisim);
            saat.setText("Saat : " + anlikSaat);
            tarih.setText("Tarih : " + anlikTarih);
        } else {
            // Kullanıcı giriş yapmamışsa burada giriş yapılmasını sağlayacak bir işlem yapabilirsiniz.
            // Örneğin, bir giriş ekranını açabilirsiniz.
            JOptionPane.showMessageDialog(null, "Kullanıcı girişi yapılmalıdır.");
        }
    }
}