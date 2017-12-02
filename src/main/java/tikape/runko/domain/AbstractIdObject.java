package tikape.runko.domain;

import java.sql.Date;

public abstract class AbstractIdObject {

    private Integer id;

    public AbstractIdObject(Integer id) {
        this.id = id;
        
    }

    public Integer getId() {
        return id;
    }

}

