package com.ibm.gswt.campus.service;

import java.util.List;

import com.ibm.gswt.campus.bean.Event;

public interface EventService {

	public int saveEvent(Event event);
	
	public void deleteEvent(int id);
	
	public List<Event> getAllEvents();
	
	public Event getEventById(int id);
	
	public void saveEventUrl(Event event);
}
