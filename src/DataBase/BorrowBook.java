package DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Ödünç Kitaplar Sınıfı
public class BorrowBook extends BaseEntity implements CrudOperations<BorrowBook> {

    private int userId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;

    // Constructor
    public BorrowBook(int id, int userId, int bookId, Date borrowDate, Date returnDate) {
        super(id);
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getter ve setter metotları
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    // CRUD operasyonları
    @Override
    public boolean add(BorrowBook borrowBook) {
        String query = "INSERT INTO borrowings (UserID, BookID, BorrowDate, ReturnDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, borrowBook.getUserId());
            preparedStatement.setInt(2, borrowBook.getBookId());
            preparedStatement.setDate(3, borrowBook.getBorrowDate());
            preparedStatement.setDate(4, borrowBook.getReturnDate());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(BorrowBook borrowBook) {
        String query = "UPDATE borrowings SET UserID = ?, BookID = ?, BorrowDate = ?, ReturnDate = ? WHERE BorrowingID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, borrowBook.getUserId());
            preparedStatement.setInt(2, borrowBook.getBookId());
            preparedStatement.setDate(3, borrowBook.getBorrowDate());
            preparedStatement.setDate(4, borrowBook.getReturnDate());
            preparedStatement.setInt(5, borrowBook.getId());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM borrowings WHERE BorrowingID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<BorrowBook> getAll() {
        List<BorrowBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT * FROM borrowings";
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("BorrowingID");
                int userId = resultSet.getInt("UserID");
                int bookId = resultSet.getInt("BookID");
                Date borrowDate = resultSet.getDate("BorrowDate");
                Date returnDate = resultSet.getDate("ReturnDate");

                BorrowBook borrowedBook = new BorrowBook(id, userId, bookId, borrowDate, returnDate);
                borrowedBooks.add(borrowedBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }

    private Connection getConnection() {
        return DataBaseConnection.getConnection();
    }
}
