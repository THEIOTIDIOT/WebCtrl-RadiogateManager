package getValue;
import java.sql.SQLException;
import java.util.*;

public interface PostgresInterface<T>{
	
	//create
	void save(T t) throws SQLException;
	
	//read
	Optional<T> get(long id) throws SQLException;
	
	LinkedList<T> getAll() throws SQLException;
	
	//update
	void update(T t, String[] params) throws SQLException;
	
	//delete
	void delete(T t) throws SQLException;
	
}
