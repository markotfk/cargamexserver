/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Entity
public class TrackRecord extends CarGameEntity implements Serializable {

    @XmlElement
    @OneToOne
    private Player player;
    
    @XmlElement
    private long recordTime;
    
    @OneToOne
    @XmlElement
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
