/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name="player", uniqueConstraints=@UniqueConstraint(columnNames={"email"}))
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement(required = true)
    @NotNull
    private String email;
    
    @Id
    @XmlElement(required = true)
    @NotNull
    private String login;
    
    @XmlElement(required = true)
    @NotNull
    private String password;
    
    @XmlElement
    private Date registered;

    @XmlElement
    private String sessionId;

    public boolean isLoggedIn() {
        return sessionId != null;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
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
            return sessionId == null;
        }
        if (sessionId != null) {
            return this.sessionId.equals(sessionId);
        }
        return false;
    }
    
}
