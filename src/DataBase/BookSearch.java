// BookSearch arayüzü
package DataBase;

import java.util.List;

interface BookSearch {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> searchBySubject(String subject);
}