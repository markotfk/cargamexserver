package org.maguz.cargamex.entities;

import java.io.Serializable;
import java.util.logging.Level;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Track record entity.
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
    
    private Long recordTime;
    
    @XmlTransient
    @OneToOne
    private Track track;
    
    public TrackRecord() {
        recordTime = 0l;
    }
    
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Track getTrack() {
        return track;
    }

    public Long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Long recordTime) {
        if (recordTime <= 0) {
            logger.log(Level.WARNING, "setRecordTime invalid time: {0}", recordTime);
            return;
        }
        this.recordTime = recordTime;
    }
    

    public void setTrack(Track track) {
        this.track = track;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TrackRecord)) {
            return false;
        }
        return super.equals(object);
    }
}
