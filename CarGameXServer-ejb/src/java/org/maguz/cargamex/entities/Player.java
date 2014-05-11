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
    
    private boolean isActive;
    
    private Long teamId;
    
    public Player() {
        email = "";
        login = "";
        password = "";
        sessionId = "";
        lastActivity = System.currentTimeMillis();
        points = 0;
        isActive = false;
        teamId = 0L;
    }
    
    public Player(Long id, Long created, String login, Long lastActivity, int points,
            boolean isActive, Team team) {
        super(id, created);
        this.login = login;
        this.lastActivity = lastActivity;
        this.points = points;
        this.email = "";
        this.password = "";
        this.sessionId = "";
        this.isActive = isActive;
        setTeam(team);
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

    public final void setTeam(Team team) {
        this.team = team;
        if (team != null) {
            this.teamId = team.getId();
        } else {
            this.teamId = 0L;
        }
    }
    
    public Long getTeamId() {
        return teamId;
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
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        if (sessionId == null) {
            setActive(false);
        } else {
            setActive(true);
        }
    }
    
    public String getSessionId() {
        return sessionId;
    }

    public boolean getActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
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
    public void setPasswordHash(String password) {
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
     */
    @Override
    public void initializeNew() {
        super.initializeNew();
        setPoints(0);
        // Assume that password is in clear text
        setPasswordHash(this.password);
        setLastActivity(System.currentTimeMillis());
    }
    
}
