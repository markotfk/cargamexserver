package org.maguz.cargamex.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Track entity.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Entity
@Table(name="track", schema="carx")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(
            name="Track.findTrackByName",
            query="SELECT t FROM Track t WHERE t.name LIKE :name"
    )
@XmlRootElement
public class Track extends CarGameEntity implements Serializable {

    private String name;
    
    @XmlTransient
    private String trackPath;
    
    private String description;

    public Track() {
        name = "";
        trackPath = "";
        description = "";
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrackPath() {
        return trackPath;
    }

    public void setTrackPath(String trackPath) {
        this.trackPath = trackPath;
    }
    
    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Track)) {
            return false;
        }
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.trackPath);
        hash = 37 * hash + Objects.hashCode(this.description);
        return hash;
    }
}
