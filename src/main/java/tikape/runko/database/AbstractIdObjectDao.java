package tikape.runko.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.database.Database;
import tikape.runko.domain.AbstractIdObject;

public abstract class AbstractIdObjectDao<T extends AbstractIdObject>
        implements Dao<T, Integer> {

    protected Database database;
    protected String tableName;

    public AbstractIdObjectDao(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }
    
    @Override
    public T findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?");
            stmt.setInt(1, key);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return createFromRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error when looking for a row in " + tableName + " with id " + key);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll() throws SQLException {
        List<T> items = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement("SELECT * FROM " + tableName + " ").executeQuery()) {

            while (result.next()) {
                items.add(createFromRow(result));
            }
        }

        return items;
    }

    
    
    private T findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM " + tableName + " WHERE nimi = ?");
            stmt.setString(1, name);

            try (ResultSet result = stmt.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                return createFromRow(result);
            }
        }
    }       

    
    @Override
    public Integer delete(Integer key) throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }

        return null;

    }
    

    public abstract T createFromRow(ResultSet resultSet) throws SQLException;

}
