package org.maguz.cargamex.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
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
@Table(name="trackrecord", schema="carx")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class TrackRecord extends CarGameEntity implements Serializable {

    @XmlTransient
    @OneToOne
    private Player player;
    
    private long recordTime;
    
    @XmlTransient
    @OneToOne
    private Track track;
    
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Track getTrack() {
        return track;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }
    

    public void setTrack(Track track) {
        this.track = track;
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
        if (!(object instanceof TrackRecord)) {
            return false;
        }
        TrackRecord other = (TrackRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.maguz.cargamex.entities.TrackRecord[ id=" + id + " ]";
    }
    
}
