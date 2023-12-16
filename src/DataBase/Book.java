// Book sınıfı
package DataBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Book extends BaseBook implements CrudOperations<Book>, BookSearch {

    public Book(int id, String title, String author, String subject, int status) {
        super(id, title, author, subject, status);
    }
    public Book(){

    }

    @Override
    public boolean add(Book book) {
        if (isAdminLoggedIn()) {
            // Admin kullanıcısı doğrulandı, kitap ekleme işlemi yapabiliriz.
            String query = "INSERT INTO books (Title, Author, Subject, Status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setString(2, book.getAuthor());
                preparedStatement.setString(3, book.getSubject());
                preparedStatement.setInt(4, book.getStatus());

                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Admin girişi yapılmalıdır.");
            return false;
        }
    }

    @Override
    public boolean update(Book book) {
        if (isAdminLoggedIn()) {
            // Admin kullanıcısı doğrulandı, kitap güncelleme işlemi yapabiliriz.
            String query = "UPDATE books SET Title = ?, Author = ?, Subject = ?, Status = ? WHERE BookID = ?";
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setString(2, book.getAuthor());
                preparedStatement.setString(3, book.getSubject());
                preparedStatement.setInt(4, book.getStatus());
                preparedStatement.setInt(5, book.getId());

                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Admin kullanıcısı doğrulanamadı. Kitap güncellenemedi.");
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        if (isAdminLoggedIn()) {
            // Admin kullanıcısı doğrulandı, kitap silme işlemi yapabiliriz.
            String query = "DELETE FROM books WHERE BookID = ?";
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Admin kullanıcısı doğrulanamadı. Kitap silinemedi.");
            return false;
        }
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String subject = resultSet.getString("Subject");
                int status = resultSet.getInt("Status");

                Book retrievedBook = new Book(id, title, author, subject, status);
                books.add(retrievedBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> searchByTitle(String title) {
        List<Book> result = new ArrayList<>();
        String query = "SELECT * FROM books WHERE Title LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + title + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("BookID");
                    String bookTitle = resultSet.getString("Title");
                    String author = resultSet.getString("Author");
                    String subject = resultSet.getString("Subject");
                    int status = resultSet.getInt("Status");

                    Book book = new Book(id, bookTitle, author, subject, status);
                    result.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        String query = "SELECT * FROM books WHERE Author LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + author + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("BookID");
                    String bookTitle = resultSet.getString("Title");
                    String bookAuthor = resultSet.getString("Author");
                    String subject = resultSet.getString("Subject");
                    int status = resultSet.getInt("Status");

                    Book book = new Book(id, bookTitle, bookAuthor, subject, status);
                    result.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Book> searchBySubject(String subject) {
        List<Book> result = new ArrayList<>();
        String query = "SELECT * FROM books WHERE Subject LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + subject + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("BookID");
                    String bookTitle = resultSet.getString("Title");
                    String author = resultSet.getString("Author");
                    String bookSubject = resultSet.getString("Subject");
                    int status = resultSet.getInt("Status");

                    Book book = new Book(id, bookTitle, author, bookSubject, status);
                    result.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void Borrow(int userID, int bookID, String borrowDate) {
        Connection connection = getConnection();

        try {
            // Önce kitabın durumunu "Ödünç Alındı" olarak güncelle
            String updateStatusQuery = "UPDATE books SET Status = 0 WHERE BookID = ?";
            try (PreparedStatement updateStatusStatement = connection.prepareStatement(updateStatusQuery)) {
                updateStatusStatement.setInt(1, bookID);
                updateStatusStatement.executeUpdate();
            }

            // Ardından borrowings tablosuna yeni bir ödünç alma kaydı ekle
            String insertBorrowingQuery = "INSERT INTO borrowings (UserID, BookID, BorrowDate) VALUES (?, ?, ?)";
            try (PreparedStatement insertBorrowingStatement = connection.prepareStatement(insertBorrowingQuery)) {
                insertBorrowingStatement.setInt(1, userID);
                insertBorrowingStatement.setInt(2, bookID);
                insertBorrowingStatement.setDate(3, Date.valueOf(LocalDate.parse(borrowDate))); // Assuming date format is "yyyy-MM-dd"
                insertBorrowingStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Kitap ödünç alındı.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Bir hata oluştu.");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void refreshBookList(JTable table) {
        List<Book> allBooks = new Book().getAll();
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tüm hücreleri düzenlenemez yapar
            }
        };

        // Tablo başlıklarını belirt
        String[] columnNames = {"Ad", "Yazar", "Konu", "Durumu"};

        tableModel.setColumnIdentifiers(columnNames);

        // Kitap arama sonuçları veya tüm kitaplar tabloya eklendikten sonra
        table.setModel(tableModel);

        // Sonuçları tabloya ekle
        for (Book book : allBooks) {
            tableModel.setColumnIdentifiers(columnNames);
            Object[] rowData = {book.getTitle(), book.getAuthor(), book.getSubject(), book.getStatus()};
            tableModel.addRow(rowData);
        }
    }

    @Override
    public String toString() {
        return "ID: " + getId() +
                ", Title: " + getTitle() +
                ", Author: " + getAuthor() +
                ", Subject: " + getSubject() +
                ", Status: " + getStatus();
    }


    private Connection getConnection() {
        return DataBaseConnection.getConnection();
    }

    private boolean isAdminLoggedIn() {
        User loggedInUser = AuthenticationManager.getLoggedInUser();
        return loggedInUser != null && UserType.ADMIN.toString().equals(loggedInUser.getUserType());
    }
}
