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

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/annokset", (req, res) -> {
            String msg = req.queryParams("msg");
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());
            map.put("viesti", msg);

            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/annokset", (Request req, Response res) -> {
            Annos annos = new Annos(-1, req.queryParams("nimi"));
            annos = annosDao.saveOrUpdate(annos);
            res.redirect("/annokset");
            return "";
        });        
       
        get("/annos/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annos", annosDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("annosraakaaineet", annosRaakaAineDao.findByAnnosId(Integer.parseInt(req.params("id"))));
            map.put("raakaaineet", raakaAineDao.findAll());

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        // delete
        Spark.get("/annos/:id/delete", (req, res) -> {            
            Integer annosId = Integer.parseInt(req.params("id"));
            Integer result = annosDao.delete(annosId);
            String msg = "";
            if (result == 0) {
                msg = "?msg=" + "Annosta ei voi poistaa koska siihen liittyy Raaka-aineita";
            }
            res.redirect("/annokset"+msg);
            return "";
        });                

        get("/raakaaineet", (req, res) -> {
            String msg = req.queryParams("msg");            
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaAineDao.findAll());
            map.put("viesti", msg);            

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        /*
        get("/raakaaine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaine", raakaAineDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "raakaaine");
        }, new ThymeleafTemplateEngine());
        */
        
        Spark.post("/raakaaineet", (Request req, Response res) -> {
            RaakaAine raakaAine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAine = raakaAineDao.saveOrUpdate(raakaAine);
            res.redirect("/raakaaineet");
            return "";
        });        
        
        // delete
        Spark.get("/raakaaine/:id/delete", (req, res) -> {            
            Integer raakaAineId = Integer.parseInt(req.params("id"));
            Integer result = raakaAineDao.delete(raakaAineId);

            String msg = "";
            if (result == 0) {
                msg = "?msg=" + "Raaka-ainetta ei voi poistaa koska siihen liittyy Annoksia";
            }
            res.redirect("/raakaaineet"+msg);

            return "";
        });                
        
        /////////
        Spark.post("/annosraakaaineet", (Request req, Response res) -> {
            Annos annos = annosDao.findOne(Integer.parseInt(req.queryParams("annosId")));
            RaakaAine raakaAine = raakaAineDao.findOne(Integer.parseInt(req.queryParams("raakaAineId")));
            AnnosRaakaAine annosRaakaAine = 
                    new AnnosRaakaAine(-1, 
                        //Integer.parseInt(req.queryParams("raakaAineId")),
                        //Integer.parseInt(req.queryParams("annosId")),
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
        // delete
        Spark.get("/annosraakaaine/:id/delete/:annosid", (req, res) -> {            
            Integer annosRaakaAineId = Integer.parseInt(req.params("id"));
            String annosId = req.params("annosid");
            annosRaakaAineDao.delete(annosRaakaAineId);
            res.redirect("/annos/" + annosId);
            return "";
        });                
        
        
    }
}
