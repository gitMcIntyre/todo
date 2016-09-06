package com.vf.test.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;

import com.vf.common.dao.impl.DAOImpl;

public class DAOImplTest extends EasyMockSupport{

	DAOImpl dao = new DAOImpl();
	SessionFactory mockSessionFactory;
	Session mockSession;
	Query mockQuery;
	
	
	private final String qName = "someQueryName";
	
	@Before
	public void setup(){
		mockSessionFactory = EasyMock.createNiceMock(SessionFactory.class);
		dao.setSessionFactory(mockSessionFactory);
		mockSession = EasyMock.createNiceMock(Session.class);
		mockQuery = EasyMock.createNiceMock(Query.class);
	}
	
	/**
	 * Simple test to demonstrate usage of JUnit and EasyMock.
	 */
	@Test
	public void executeNamedQueryTest(){
		List<List<String>> parameters = new ArrayList<List<String>>();
		List<Object> items = new ArrayList<Object>();
		
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.isOpen()).andReturn(true);
		EasyMock.expect(mockSession.isConnected()).andReturn(true);
		
		EasyMock.expect(mockSession.getNamedQuery(qName)).andReturn(mockQuery);
		EasyMock.expect(mockQuery.setCacheable(true)).andReturn(mockQuery);
		EasyMock.expect(mockQuery.list()).andReturn(items);
		
		EasyMock.replay(mockSession, mockSessionFactory, mockQuery);
		List<Object> fetchedItems = (List<Object>)dao.executeNamedQuery(qName, parameters);
		EasyMock.verify(mockSession, mockSessionFactory, mockQuery);
		
		assertEquals(items, fetchedItems);
	}
}
