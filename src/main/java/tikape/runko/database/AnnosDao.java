package tikape.runko.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.database.Database;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;

public class AnnosDao extends AbstractNamedObjectDao<Annos> {

    public AnnosDao(Database database, String tableName) {
        super(database, tableName);
    }
     
   @Override
   public Integer delete(Integer key) throws SQLException {
                
        List<AnnosRaakaAine> annosraakaaineet = new ArrayList<>();
        annosraakaaineet = new AnnosRaakaAineDao(database, "AnnosRaakaAine").findByAnnosId(key);
                
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
    public Annos createFromRow(ResultSet resultSet) throws SQLException {
        return new Annos(
                resultSet.getInt("id"), 
                resultSet.getString("nimi")
        );
    }

}
