package org.maguz.cargamex.entities;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.maguz.cargamex.util.PasswordUtils;

/**
 * Player entity.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Entity
@Table(name="player", schema="carx", uniqueConstraints=@UniqueConstraint(columnNames={"email", "login"}))
@Cacheable(false)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(
            name="Player.findByLogin",
            query="SELECT p FROM Player p WHERE p.login LIKE :playerLogin"
    ),
    @NamedQuery(
            name="Player.findByActiveSession",
            query="SELECT p FROM Player p WHERE p.sessionId IS NOT NULL"
    )
})
public class Player extends CarGameEntity implements Serializable {
    @XmlElement(required = true)
    @NotNull
    private String email;
    
    @XmlElement(required = true)
    @NotNull
    private String login;
    
    @NotNull
    private String password;
    
    private String sessionId;
    
    private Long lastActivity;

    private int points;
    
    public Player() {
    }
    
    public Player(Long id, Long created, String login, Long lastActivity, int points) {
        super(id, created);
        this.login = login;
        this.lastActivity = lastActivity;
        this.points = points;
        this.email = "";
        this.password = "";
        this.sessionId = "";
    }
    
    @XmlTransient
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    public Long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Long lastActivity) {
        this.lastActivity = lastActivity;
    }
    
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
    public boolean belongsTo(Team team) {
        if (team == null) {
            throw new NullPointerException("team");
        }
        if (this.team == null) {
            return false;
        }
        return this.team.equals(team);
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
    public boolean equals(Object object) {
        if (!(object instanceof Player)) {
            return false;
        }
        return super.equals(object);
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
        if (password == null) {
            throw new NullPointerException("password");
        }
        try {
            this.password = PasswordUtils.getSaltedHash(password);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            logger.log(Level.SEVERE, "SetPassword error:{0}", e.getMessage());
        }
        
    }
    
    public void setPasswordNoHash(String password) {
        if (password == null) {
            throw new NullPointerException("password");
        }
        this.password = password;
    }
    
    public boolean checkPassword(String password) {
        if (password == null) {
            return false;
        }
        
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
    
    /**
     * Initialize player object with default values.
     * @param password
     * @return true if initialization was success, false if password is invalid.
     */
    public boolean initializeWithPassword(String password) {
        if (password == null || password.trim().length() < 6) {
            return false;
        }
        setId((long)0);
        this.setPassword(password); // hashes plain-text password
        final long time = System.currentTimeMillis();
        setCreated(time);
        setLastActivity(time);
        setPoints(0);
        return true;
    }
    
}
