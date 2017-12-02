package tikape.runko.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.database.Database;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao extends AbstractNamedObjectDao<RaakaAine> {

    public RaakaAineDao(Database database, String tableName) {
        super(database, tableName);
    }
    
   @Override
   public Integer delete(Integer key) throws SQLException {
                
        List<AnnosRaakaAine> annosraakaaineet = new ArrayList<>();
        annosraakaaineet = new AnnosRaakaAineDao(database, "AnnosRaakaAine").findByRaakaAineId(key);
                
        if (annosraakaaineet.isEmpty()) {
            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?");
                stmt.setInt(1, key);
                stmt.executeUpdate();
            }
            return 1;
        }
        else {
            return 0;            
        }        

    }          

    
    @Override
    public RaakaAine createFromRow(ResultSet resultSet) throws SQLException {
        return new RaakaAine(
                resultSet.getInt("id"), 
                resultSet.getString("nimi")
        );
    }

}
