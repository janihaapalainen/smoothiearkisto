package tikape.runko.domain;

public class AnnosRaakaAine extends AbstractIdObject {

    public Integer jarjestys;
    public Double maara;
    public String mittayksikko;
    public String ohje;
    public Annos annos;
    public RaakaAine raakaaine;
        
    public AnnosRaakaAine(  Integer id, 
                            Integer jarjestys, 
                            Double maara, 
                            String mittayksikko, 
                            String ohje,
                            Annos annos,
                            RaakaAine raakaaine) {
            super(id);
            this.jarjestys = jarjestys;
            this.maara = maara;
            this.mittayksikko = mittayksikko;
            this.ohje = ohje;
            this.annos = annos;
            this.raakaaine = raakaaine;
        }

        public Integer getRaakaAineId() {
            return raakaaine.getId(); 
        }

        public Integer getAnnosId() {
            return annos.getId(); 
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



