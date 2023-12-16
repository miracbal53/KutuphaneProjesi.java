// CrudOperations arayüzü
package DataBase;

import java.util.List;

interface CrudOperations<T> {
    boolean add(T item);
    boolean update(T item);
    boolean delete(int id);
    List<T> getAll();
}