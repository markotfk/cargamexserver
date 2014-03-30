/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Entity
@Table(name="team", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @XmlElement(required = true)
    private String name;
    
    @XmlElement
    private String logoPath;
    
    @XmlElement
    private int points;
    
    @XmlElement
    @OneToOne
    private Player owner;
    
    @OneToMany(mappedBy="team")
    private List<Player> admins;

    @OneToMany(mappedBy="team")
    private List<Player> players;
    
    @XmlElement
    private Long created;
    
    @XmlElement
    private int wins;

    @XmlElement
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    private void checkAdmins() {
        if (admins == null) {
            admins = new ArrayList<>();
        }
    }
    public List<Player> getAdmins() {
        checkAdmins();
        return admins;
    }

    public void addAdmin(Player player) {
        if (player == null) {
            throw new NullPointerException("Player is null");
        }
        checkAdmins();
        if (!admins.contains(player)) {
            admins.add(player);
        }
    }
    
    public boolean removeAdmin(Player player) {
        if (player == null) {
            throw new NullPointerException("Player is null");
        }
        checkAdmins();
        return admins.remove(player);
    }
    
    public boolean isAdmin(Player player) {
        if (player == null) {
            return false;
        }
        checkAdmins();
        return admins.contains(player);
    }
    
    public boolean isOwner(Player player) {
        if (player == null) {
            return false;
        }
        return owner.equals(player);
    }
    
    
    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }
    
    @XmlElement
    private int losses;

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public void addPlayer(Player player) {
        if (player == null) {
            throw new NullPointerException("Player is null");
        }
        players.add(player);
    }
    
    public boolean removePlayer(Player player) {
        if (player == null) {
            throw new NullPointerException("Player is null");
        }
        return players.remove(player);
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    
    public void setOwner(Player owner) {
        if (owner == null) {
            return;
        }
        this.owner = owner;
        addAdmin(owner);
    }
    
    public Player getOwner() {
        return owner;
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
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.maguz.cargamex.entities.Team[ id=" + id + " ]";
    }
    
}
