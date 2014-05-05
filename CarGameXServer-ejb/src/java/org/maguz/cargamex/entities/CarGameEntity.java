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
    protected Long id;

    protected Long created;

    /**
     * Constructor. 
     */
    protected CarGameEntity() {
        id = 0l;
        created = System.currentTimeMillis();
    }
    
    protected CarGameEntity(Long id, Long created) {
        this.id = id;
        this.created = created;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[ id=" + id + " ]";
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        CarGameEntity other = (CarGameEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public void initializeNew() {
        created = System.currentTimeMillis();
    }
}
