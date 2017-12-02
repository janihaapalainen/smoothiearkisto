package tikape.runko.domain;

public class AnnosRaakaAine extends AbstractIdObject {

    public Integer jarjestys;
    public Double maara;
    public String mittayksikko;
    public String ohje;
    public Annos annos;
    public RaakaAine raakaaine;
        
    public AnnosRaakaAine(  Integer id, 
                            //Integer raaka_aine_id,
                            //Integer annos_id, 
                            Integer jarjestys, 
                            Double maara, 
                            String mittayksikko, 
                            String ohje,
                            Annos annos,
                            RaakaAine raakaaine) {
            super(id);
            //this.annos_id = annos_id;
            this.jarjestys = jarjestys;
            this.maara = maara;
            this.mittayksikko = mittayksikko;
            this.ohje = ohje;
            this.annos = annos;
            this.raakaaine = raakaaine;
        }

        public Integer getRaakaAineId() {
            return raakaaine.getId(); // raaka_aine_id;
        }

        public Integer getAnnosId() {
            return annos.getId(); //annos_id;
        }

        public Integer getJarjestys() {
            return jarjestys;
        }

        public Double getMaara() {
            return maara;
        }

        public String getMittayksikko() {
            return mittayksikko;
        }
        
        public String getOhje() {
            return ohje;
        }

        
}    



