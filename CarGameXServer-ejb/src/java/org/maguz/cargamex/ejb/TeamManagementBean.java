/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.ejb;

import java.util.List;
import javax.ejb.Stateless;
import org.maguz.cargamex.entities.Team;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Stateless
public class TeamManagementBean extends ManagementBean implements TeamManagementBeanLocal {

    
    @Override
    public StatusCode add(Team entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        if (find(entity.getId()) == null) {
            return StatusCode.NotFound;
        }
        em.persist(entity);
        return StatusCode.OK;
    }

    @Override
    public StatusCode remove(Team entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        Team existing = find(entity.getId());
        if (existing != null) {
            em.remove(em.merge(existing));
            return StatusCode.OK;
        }
        return StatusCode.NotFound;
    }

    @Override
    public StatusCode edit(Team entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        em.merge(entity);
        return StatusCode.OK;
    }

    @Override
    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    @Override
    public List<Team> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Team.class));
        return em.createQuery(cq).getResultList();
    }
}
