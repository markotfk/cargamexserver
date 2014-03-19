/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.service;

import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public class PlayerFacadeRESTTest {
    
    public PlayerFacadeRESTTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of edit method, of class PlayerFacadeREST.
     */
    @Test
    public void testEdit_GenericType() throws Exception {
        System.out.println("edit");
        Player entity = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        instance.edit(entity);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class PlayerFacadeREST.
     */
    @Test
    public void testRemove_GenericType() throws Exception {
        System.out.println("remove");
        Player entity = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        instance.remove(entity);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of find method, of class PlayerFacadeREST.
     */
    @Test
    public void testFind_Object() throws Exception {
        System.out.println("find");
        Object id = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        Player expResult = null;
        Player result = instance.find(id);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findRange method, of class PlayerFacadeREST.
     */
    @Test
    public void testFindRange_intArr() throws Exception {
        System.out.println("findRange");
        int[] range = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        List<Player> expResult = null;
        List<Player> result = instance.findRange(range);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of count method, of class PlayerFacadeREST.
     */
    @Test
    public void testCount() throws Exception {
        System.out.println("count");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        int expResult = 0;
        int result = instance.count();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class PlayerFacadeREST.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Player entity = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        instance.create(entity);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class PlayerFacadeREST.
     */
    @Test
    public void testEdit_String_Player() throws Exception {
        System.out.println("edit");
        String id = "";
        Player entity = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        instance.edit(id, entity);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class PlayerFacadeREST.
     */
    @Test
    public void testRemove_String() throws Exception {
        System.out.println("remove");
        String id = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        instance.remove(id);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of find method, of class PlayerFacadeREST.
     */
    @Test
    public void testFind_String() throws Exception {
        System.out.println("find");
        String id = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        Player expResult = null;
        Player result = instance.find(id);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAll method, of class PlayerFacadeREST.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        List<Player> expResult = null;
        List<Player> result = instance.findAll();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findRange method, of class PlayerFacadeREST.
     */
    @Test
    public void testFindRange_Integer_Integer() throws Exception {
        System.out.println("findRange");
        Integer from = null;
        Integer to = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        List<Player> expResult = null;
        List<Player> result = instance.findRange(from, to);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of countREST method, of class PlayerFacadeREST.
     */
    @Test
    public void testCountREST() throws Exception {
        System.out.println("countREST");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PlayerFacadeREST instance = (PlayerFacadeREST)container.getContext().lookup("java:global/classes/PlayerFacadeREST");
        String expResult = "";
        String result = instance.countREST();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
