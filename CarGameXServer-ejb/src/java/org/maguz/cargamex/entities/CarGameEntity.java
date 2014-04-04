/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@MappedSuperclass
@XmlRootElement
public abstract class CarGameEntity {
    
    protected static final long serialVersionUID = 1L;
    
    @XmlElement
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @XmlElement
    protected Long created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }
}
