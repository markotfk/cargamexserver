package org.maguz.cargamex.entities;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract base class for all entity classes.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@MappedSuperclass

public abstract class CarGameEntity implements Serializable {
    
    protected static final Logger logger = Logger.getLogger(CarGameEntity.class.getName());
    
    protected static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    protected long created;

    /**
     * Constructor. 
     */
    protected CarGameEntity() {
        created = System.currentTimeMillis();
    }
    
    protected CarGameEntity(Long id, Long created) {
        this.id = id;
        this.created = created;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[ id=" + id + " ]";
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += new Long(id).hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        CarGameEntity other = (CarGameEntity) object;
        return this.id == other.id;
    }
}
