package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;

import DataBase.AuthenticationManager;
import DataBase.Book;
import DataBase.User;

public class SearchBook extends JFrame {
    private JPanel SearchBookPanel;
    private JTextField kitapAdArama;
    private JTextField yazarArama;
    private JTextField konuArama;
    private JButton aramaYap;
    private JButton tumKitaplariGoster;
    private JLabel konuLabel;
    private JLabel yazarLabel;
    private JLabel adLabel;
    private JTable kitapAramaSonuclariTablosu;
    private JButton kitabiOduncAlButton;
    private JButton kitabiIadeEtButton;

    public SearchBook() {
        add(SearchBookPanel);
        setTitle("Kütüphane Uygulaması - Kitap Arama");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        User loggedInUser = AuthenticationManager.getLoggedInUser();

        aramaYap.addActionListener(new ActionListener() {
            String kitapDurumu;
            @Override
            public void actionPerformed(ActionEvent e) {
                String kitapAd = kitapAdArama.getText();
                String kitapYazar = yazarArama.getText();
                String kitapKonu = konuArama.getText();

                // İlgili metotları kullanarak aramayı gerçekleştir
                List<Book> searchResults = new ArrayList<>();

                if (!kitapAd.isEmpty()) {
                    searchResults = new Book().searchByTitle(kitapAd);
                } else if (!kitapYazar.isEmpty()) {
                    searchResults = new Book().searchByAuthor(kitapYazar);
                } else if (!kitapKonu.isEmpty()) {
                    searchResults = new Book().searchBySubject(kitapKonu);
                }

                DefaultTableModel tableModel = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // Tüm hücreleri düzenlenemez yapar
                    }
                };

                // Tablo başlıklarını belirt
                String[] columnNames = {"Id", "Ad", "Yazar", "Konu", "Durumu"};

                tableModel.setColumnIdentifiers(columnNames);

                // Kitap arama sonuçları veya tüm kitaplar tabloya eklendikten sonra
                kitapAramaSonuclariTablosu.setModel(tableModel);


                // Sonuçları tabloya ekle
                if (searchResults != null)
                    for (Book book : searchResults ) {
                        // Başlıkları bir kez ekleyin
                        tableModel.setColumnIdentifiers(columnNames);

                        if(book.getStatus()==1){
                            String kitapDurumu = "Mevcut";
                        }
                        else {
                            String kitapDurumu = "Mevcut değil";
                        }
                        Object[] rowData = {book.getId(), book.getTitle(), book.getAuthor(), book.getSubject(), kitapDurumu};
                        tableModel.addRow(rowData);
                    }
            }
        });


        tumKitaplariGoster.addActionListener(new ActionListener() {
            String kitapDurumu;

            @Override
            public void actionPerformed(ActionEvent e) {
                // Tüm kitapları getir
                List<Book> allBooks = new Book().getAll();

                DefaultTableModel tableModel = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // Tüm hücreleri düzenlenemez yapar
                    }
                };

                // Tablo başlıklarını belirt
                String[] columnNames = {"Id", "Ad", "Yazar", "Konu", "Durumu"};

                tableModel.setColumnIdentifiers(columnNames);

                // Kitap arama sonuçları veya tüm kitaplar tabloya eklendikten sonra
                kitapAramaSonuclariTablosu.setModel(tableModel);


                // Sonuçları tabloya ekle
                for (Book book : allBooks) {
                    tableModel.setColumnIdentifiers(columnNames);
                    if (book.getStatus() == 1) {
                        String kitapDurumu = "Mevcut";
                    } else {
                        String kitapDurumu = "Mevcut değil";
                    }

                    Object[] rowData = {book.getId(), book.getTitle(), book.getAuthor(), book.getSubject(), kitapDurumu};
                    tableModel.addRow(rowData);
                }
            }
        });


        kitabiOduncAlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = kitapAramaSonuclariTablosu.getSelectedRow();
                if (selectedRow != -1) {
                    // Seçilen satırdaki kitabın bilgilerini al

                    int userID = loggedInUser.getId();
                    int kitapId = (int) kitapAramaSonuclariTablosu.getValueAt(selectedRow, 0);
                    String kitapAdi = (String) kitapAramaSonuclariTablosu.getValueAt(selectedRow, 1);
                    String yazar = (String) kitapAramaSonuclariTablosu.getValueAt(selectedRow, 2);
                    String konu = (String) kitapAramaSonuclariTablosu.getValueAt(selectedRow, 3);
                    int durumu = (int) kitapAramaSonuclariTablosu.getValueAt(selectedRow, 4);
                    LocalTime anlikSaat = LocalTime.now();
                    LocalDate anlikTarih = LocalDate.now();
                    String anlikTarihString = anlikTarih.toString();

                    if (durumu == 1) {
                        // Burada ödünç alma işlemlerini gerçekleştir
                        // Örneğin, seçilen kitabın durumunu "Ödünç Alındı" olarak güncelleyebilirsiniz
                        // Book sınıfınızdaki bir metodu kullanarak bu işlemi gerçekleştirebilirsiniz
                        Book selectedBook = new Book(kitapId, kitapAdi, yazar, konu, durumu);
                        selectedBook.Borrow(userID,selectedBook.getId(),anlikTarihString);
                        Book.refreshBookList(kitapAramaSonuclariTablosu);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Kitap Mevcut Değil");
                    }

                    // Örneğin:


                    // Ardından güncellenmiş kitap listesini tabloya yeniden yükleyin
                } else {
                    JOptionPane.showMessageDialog(SearchBook.this, "Lütfen ödünç almak istediğiniz kitabı seçin.");
                }
            }
        });




    }
}
