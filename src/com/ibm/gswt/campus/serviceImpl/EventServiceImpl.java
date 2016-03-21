package com.ibm.gswt.campus.serviceImpl;

import java.util.List;

import com.ibm.gswt.campus.bean.Event;
import com.ibm.gswt.campus.daoImpl.EventDaoImpl;
import com.ibm.gswt.campus.service.EventService;

public class EventServiceImpl implements EventService {
	
	private EventDaoImpl eventDao = new EventDaoImpl();

	@Override
	public int saveEvent(Event event) {
		return eventDao.saveEvent(event);
		
	}

	@Override
	public void deleteEvent(int id) {
		eventDao.deleteEvent(id);
		
	}

	@Override
	public List<Event> getAllEvents() {
		return eventDao.getAllEvents();
	}

	@Override
	public Event getEventById(int id) {
		return eventDao.getEventById(id);
	}

	@Override
	public void saveEventUrl(Event event) {
		eventDao.saveEventUrl(event);
	}
}
