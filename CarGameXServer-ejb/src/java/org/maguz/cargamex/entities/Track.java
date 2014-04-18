package org.maguz.cargamex.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Entity
@Table(name="track", schema="carx")
@XmlAccessorType(XmlAccessType.FIELD)
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Track)) {
            return false;
        }
        Track other = (Track) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.maguz.cargamex.entities.Track[ id=" + id + " ]";
    }
    
}
