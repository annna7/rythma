package repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    List<T> findAll() throws SQLException;
    Optional<T> findById(int id) throws SQLException;
    boolean create(T item) throws SQLException;
    boolean update(T item) throws SQLException;
    boolean delete(int id) throws SQLException;
}
