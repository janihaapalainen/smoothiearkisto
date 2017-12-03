package tikape.runko;

import java.util.Calendar;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiearkisto.db");
        RaakaAineDao raakaAineDao = new RaakaAineDao(database, "RaakaAine");        
        AnnosDao annosDao = new AnnosDao(database, "Annos");        
        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database, "AnnosRaakaAine");        

        // etusivu
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        // annoksien katselusivu
        get("/annokset", (req, res) -> {
            String msg = req.queryParams("msg");
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());
            map.put("viesti", msg); //mahollinen käyttäjälle näytettävä virhe-viesti, mikäli annosta ei voida poistaa

            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        // annoksen lisäys
        Spark.post("/annokset", (Request req, Response res) -> {
            Annos annos = new Annos(-1, req.queryParams("nimi"));
            annos = annosDao.saveOrUpdate(annos);
            res.redirect("/annokset");
            return "";
        });        
       
        // annoksen katselusivu
        get("/annos/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annos", annosDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("annosraakaaineet", annosRaakaAineDao.findByAnnosId(Integer.parseInt(req.params("id"))));
            map.put("raakaaineet", raakaAineDao.findAll());
            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        // annoksen poisto
        Spark.get("/annos/:id/delete", (req, res) -> {            
            Integer annosId = Integer.parseInt(req.params("id"));
            Integer result = annosDao.delete(annosId); // palautta 0:n mikäli annokseen liittyy annosraakaaineita
            String msg = "";
            if (result == 0) { // jos annosDao.delete palautta 0:n välitetään annokset-katselusivulle viesti käyttäjälle näytettäväksi
                msg = "?msg=" + "Annosta ei voi poistaa koska siihen liittyy Raaka-aineita";
            }
            res.redirect("/annokset"+msg);
            return "";
        });                

        // raaka-aineiden katselusivu
        get("/raakaaineet", (req, res) -> {
            String msg = req.queryParams("msg");            
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaAineDao.findAll());
            map.put("viesti", msg);            

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        // raaka-aineen lisäys
        Spark.post("/raakaaineet", (Request req, Response res) -> {
            RaakaAine raakaAine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAine = raakaAineDao.saveOrUpdate(raakaAine);
            res.redirect("/raakaaineet");
            return "";
        });        
        
        // raaka-aineen pisto
        Spark.get("/raakaaine/:id/delete", (req, res) -> {            
            Integer raakaAineId = Integer.parseInt(req.params("id"));
            Integer result = raakaAineDao.delete(raakaAineId); // palautta 0:n mikäli raaka-aineeseen liittyy annosraakaaineita
            String msg = "";
            if (result == 0) { // jos raakaAineDao.delete palautta 0:n välitetään raakaaineet-katselusivulle viesti käyttäjälle näytettäväksi
                msg = "?msg=" + "Raaka-ainetta ei voi poistaa koska siihen liittyy Annoksia";
            }
            res.redirect("/raakaaineet"+msg);

            return "";
        });                
        
        // raaka-aineen lisäys annokselle
        Spark.post("/annosraakaaineet", (Request req, Response res) -> {
            Annos annos = annosDao.findOne(Integer.parseInt(req.queryParams("annosId")));
            RaakaAine raakaAine = raakaAineDao.findOne(Integer.parseInt(req.queryParams("raakaAineId")));
            AnnosRaakaAine annosRaakaAine = 
                    new AnnosRaakaAine(-1, 
                        Integer.parseInt(req.queryParams("jarjestys")),
                        Double.parseDouble(req.queryParams("maara")),
                        req.queryParams("mittayksikko"),
                        req.queryParams("ohje"),
                        annos,
                        raakaAine
                    );
            annosRaakaAine = annosRaakaAineDao.saveOrUpdate(annosRaakaAine);
            res.redirect("/annos/" + req.queryParams("annosId"));
            return "";
        });      
        
        // raaka-aineen poisto annokselta
        Spark.get("/annosraakaaine/:id/delete/:annosid", (req, res) -> {            
            Integer annosRaakaAineId = Integer.parseInt(req.params("id"));
            String annosId = req.params("annosid");
            annosRaakaAineDao.delete(annosRaakaAineId);
            res.redirect("/annos/" + annosId);
            return "";
        });                
                
    }
}
