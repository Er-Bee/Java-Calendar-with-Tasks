import java.util.Calendar;
import java.util.HashMap;


// the HashMapCalendar class is responsible for keeping track of tasks / assignment the user puts in in whatever day they do.
public class HashMapCalendar {
	private HashMap<Calendar, String> scheduleMap;
	
	public HashMapCalendar() {
		scheduleMap = new HashMap<>();
	}
	
	public void add(Calendar date, String appointment) {	
		// adding a new appointment (value) to a certain date (key)
		if (sameDay(date) == null)
			scheduleMap.put(date, appointment);
		else {
			// adding the appointment to the key of the hashmap, explained under the sameDay() function.
			Calendar cal = (Calendar)sameDay(date).clone();
			scheduleMap.put(cal, scheduleMap.get(cal) + "\n" + appointment);
		}
	}
	
	public Calendar sameDay(Calendar test) {
		// since the Calendar sees 2 objects in the same day as different objects, because of the time they were input in, we need to get the key (Calendar) of the same day that is in the hash map
		boolean exists = false;
		for (Calendar cal: scheduleMap.keySet()) {
			// for our purposes, we'd like the keys (Calendars) that are on the same day, month and year to be considered the same.
			if (cal.get(cal.DAY_OF_MONTH) == test.get(test.DAY_OF_MONTH) && cal.get(cal.MONTH) == test.get(test.MONTH) && cal.get(cal.YEAR) == test.get(test.YEAR)) {
				return cal;
			}
		}
		return null;
	}
	
	public int dayOfWeek(Calendar temp) {
		// used to start the month chosen on the right day.
		return temp.get(temp.DAY_OF_WEEK);
	}
	
	public String getSchedule(Calendar date) {
		// returns a string representation of available tasks / assignments on a given calendar's day
		if (sameDay(date) != null) {
			Calendar cal = (Calendar)sameDay(date).clone();
			return scheduleMap.get(cal);
		}
		return "No tasks.";
	}
	
	public void setSchedule(Calendar date, String old, String updated) {
		// used for replacing a value in a certain key (Calendar day). 
		if (sameDay(date) != null) {
			Calendar cal = (Calendar)sameDay(date).clone();
			scheduleMap.replace(cal, old, updated);
		}
	}
	
}
