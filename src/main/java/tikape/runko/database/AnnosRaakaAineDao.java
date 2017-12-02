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

public class AnnosRaakaAineDao extends AbstractIdObjectDao<AnnosRaakaAine> {

    public AnnosRaakaAineDao(Database database, String tableName) {
        super(database, tableName);
    }
    
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine object) throws SQLException {
        // simply support saving -- disallow saving if task with 
        // same name exists
        AnnosRaakaAine byId = findOne(object.getId());

        if (byId != null) {
            return byId;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " (raaka_aine_id, annos_id, jarjestys, maara, mittayksikko, ohje) VALUES (?,?,?,?,?,?)");
            stmt.setInt(1, object.getRaakaAineId());
            stmt.setInt(2, object.getAnnosId());
            stmt.setInt(3, object.getJarjestys());
            stmt.setDouble(4, object.getMaara());
            stmt.setString(5, object.getMittayksikko());
            stmt.setString(6, object.getOhje());
            stmt.executeUpdate();
        }

        return findOne(object.getId());

    }
    
    public List<AnnosRaakaAine>  findByAnnosId(Integer annosId) throws SQLException {
        List<AnnosRaakaAine> annosraakaaineet = new ArrayList<>();

        String query = "SELECT * FROM "+tableName+" WHERE annos_id = ? ORDER BY jarjestys";        
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, annosId);
            try (ResultSet result = stmt.executeQuery()) {

                while (result.next()) {
                    annosraakaaineet.add(createFromRow(result));
                }
            }
        }

        return annosraakaaineet;
    }
    
    public List<AnnosRaakaAine>  findByRaakaAineId(Integer raakaAineId) throws SQLException {
        List<AnnosRaakaAine> annosraakaaineet = new ArrayList<>();

        String query = "SELECT * FROM "+tableName+" WHERE raaka_aine_id = ? ORDER BY jarjestys";        
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, raakaAineId);
            try (ResultSet result = stmt.executeQuery()) {

                while (result.next()) {
                    annosraakaaineet.add(createFromRow(result));
                }
            }
        }

        return annosraakaaineet;
    }    

    
    @Override
    public AnnosRaakaAine createFromRow(ResultSet resultSet) throws SQLException {
        return new AnnosRaakaAine(
                resultSet.getInt("id"), 
                resultSet.getInt("jarjestys"), 
                resultSet.getDouble("maara"), 
                resultSet.getString("mittayksikko"), 
                resultSet.getString("ohje"),
                new AnnosDao(database, "Annos").findOne( resultSet.getInt("annos_id") ),               
                new RaakaAineDao(database, "RaakaAine").findOne( resultSet.getInt("raaka_aine_id") )               
                
        );
    }    
}
