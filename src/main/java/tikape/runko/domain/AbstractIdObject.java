package tikape.runko.domain;

import java.sql.Date;

public abstract class AbstractNamedObject {

    private Integer id;
    private String nimi;

    public AbstractNamedObject(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

}
