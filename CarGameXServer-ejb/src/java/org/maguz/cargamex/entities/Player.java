/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.entities;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.maguz.cargamex.util.PasswordUtils;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Entity
@XmlRootElement
@Table(name="player", uniqueConstraints=@UniqueConstraint(columnNames={"email", "login"}))
@Cacheable(false)
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @XmlElement(required = true)
    @NotNull
    private String email;
    
    @XmlElement(required = true)
    @NotNull
    private String login;
    
    @XmlElement(required = true)
    @NotNull
    private String password;
    
    @XmlElement
    private Long registered;

    @XmlElement
    private String sessionId;
    
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
    public boolean isLoggedIn() {
        return sessionId != null;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public Long getRegistered() {
        return registered;
    }

    public void setRegistered(Long registered) {
        this.registered = registered;
    }
    
    @XmlElement
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public void addPoints(int points) {
        this.points += points;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Player)) {
            return false;
        }
        Player other = (Player) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.maguz.cargamex.entities.User[ login=" + login + " ]";
    }

    /**
     * @return the name
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = PasswordUtils.getSaltedHash(password);
    }
    
    public void setPasswordNoHash(String password) {
        this.password = password;
    }
    
    public boolean checkPassword(String password) {
        try {
            return PasswordUtils.check(password, this.password);
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean checkSessionId(String sessionId) {
        if (this.sessionId == null) {
            return false;
        }
        if (sessionId != null) {
            return this.sessionId.equals(sessionId);
        }
        return false;
    }
    
}
