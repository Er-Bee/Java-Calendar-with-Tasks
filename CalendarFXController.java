import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

// using HashMapCalendar and JavaFX to implement the wanted features.
public class CalendarFXController {
	private HashMapCalendar map;
	
	HashMap<String, Integer> monthMap = new HashMap<>();
	HashMap<Integer, Integer> daysInMonth = new HashMap<>();
	
	ArrayList<Button> buttons;
	
    @FXML
    private ChoiceBox<String> monthChoice;
    
    
    @FXML
    private ChoiceBox<String> yearChoice;
    
    
    @FXML
    private GridPane grid;

    
    @FXML
    void updateBoard(ActionEvent event) {
    	// if no month / year were chosen in the choice boxes, do nothing.
    	if (monthChoice.getValue() == null || yearChoice.getValue() == null)
    		return;
    	
    	// resetting all the grid pane's children that are buttons to represent nothing so we don't get overlapping days when the user changes month / year.
    	ObservableList<Node> children = grid.getChildren();
    	for (Node child : children) {
    		if(child != null && child instanceof Button)
    			((Button) child).setText("");
    	}
    	
    	int m = monthMap.get(monthChoice.getValue());
    	int y = Integer.parseInt(yearChoice.getValue());
    	
    	// check if given year is a leap year, change february's day amount accordingly
    	if (isLeapYear(y))
    		daysInMonth.replace(1, 29);
    	else
    		daysInMonth.replace(1, 28);
    	
    	// making a Calendar object for current year and month, finding out which day it starts in.
    	Calendar cal = Calendar.getInstance();
    	cal.set(y, m, 1);
    	int d = map.dayOfWeek(cal);
    	
    	// setting the text of the appropriate buttons according to the months days.
    	for(int i = 1; i <= daysInMonth.get(m); i++) {
    		cal.set(y, m, i);
    		buttons.get(d+i-2).setText(cal.get(cal.DAY_OF_MONTH) + "");	// -2 because we transition from 'd' and 'i' (start their count at 1) to a list that starts its count at 0
    	}
    	
    	
    	
    }
    
    private String[] possibleValues = {"Edit", "Add", "Nothing"};
    
    public void check(Button butt) {
    	// upon clicking a button, check if it's empty (not a day in the current month, in that case, do nothing and exit the function)
    	if(butt.getText() == "")
    		return;
    	
    	String year = yearChoice.getValue();
    	String month = monthChoice.getValue();
    	String day = butt.getText();
    	
    	// the current chosen date that will be checked
    	Calendar date = Calendar.getInstance();
    	int y = Integer.parseInt(year);
    	int m = monthMap.get(month);
    	int d = Integer.parseInt(day);
    	date.set(y, m, d);
    	
    	// upon clicking a button with a day in the given month, check if there's anything in it, display it to the user and let them choose what they want to do.
    	//i know these are kinda repeating, but i wanted different wording for each case, the default choice to be different, as well as different default lines for editting
    	if (map.sameDay(date) == null) {
    		String selectedValue = (String) JOptionPane.showInputDialog(null,"No tasks at this date.\n-----------\nWhat would you like to do?", "Input",JOptionPane.INFORMATION_MESSAGE, null,possibleValues, possibleValues[1]);
    		switch(selectedValue){
    		case "Nothing":
    			// if user chose nothing, close the message, do nothing.
    			return;
    		case "Add":
    			// if user chose add, we let them input the value they'd like to add
    			String appointmentValue = JOptionPane.showInputDialog(null, "Please enter value:", "");
    			if (isEmpty(appointmentValue))
    				break;
    			map.add(date, appointmentValue);
    			JOptionPane.showMessageDialog(null, "Tasks you have on " + day + "/" + (m+1) + "/" +  year + "\n are:\n" + map.getSchedule(date));
    			break;
    		case "Edit":
    			// if user chose to edit, we show them an input dialog, where the current tasks are already given in the input line, so they can change it however they'd like.
    			String updated = JOptionPane.showInputDialog(null, "Please edit your tasks:", "");
    			map.add(date, updated);
    			break;
    		}
    	}
    	else {
    		String selectedValue = (String) JOptionPane.showInputDialog(null, "Current tasks for " + day + "/" + (m+1) + "/" +  year + ":\n" + map.getSchedule(date) + "\n-----------\nWhat would you like to do?", "Input", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[2]);
    		switch(selectedValue){
    		case "Nothing":
    			// if user chose nothing, close the message, do nothing.
    			return;
    		case "Add":
    			// if user chose add, we let them input the value they'd like to add
    			String appointmentValue = JOptionPane.showInputDialog(null, "Please enter value:", "");
    			if (isEmpty(appointmentValue))
    				break;
    			map.add(date, appointmentValue);
    			JOptionPane.showMessageDialog(null, "Tasks you have on " + day + "/" + (m+1) + "/" +  year + "\n are:\n" + map.getSchedule(date));
    			break;
    		case "Edit":
    			// if user chose to edit, we show them an input dialog, where the current tasks are already given in the input line, so they can change it however they'd like.
    			String updated = JOptionPane.showInputDialog(null, "Please edit your tasks:", map.getSchedule(date));
    			map.setSchedule(date, map.getSchedule(date), updated);
    			break;
    		}
    	}
    }
    
    public boolean isEmpty(String input) {
    	// checking input string for any characters that aren't whitespace
    	return (input.trim().length() <= 0);
    }
    
    public boolean isLeapYear(int year) {
    	// returning true if given year is a leap year
    	return (year%4 == 0 && year%100 == 0 && year%400 == 0);
    }
    
    public void initialize() {
    	map = new HashMapCalendar();
    	
    	// an array of the 12 months to be shown in the months' choice box.
    	String[] monthArr = {"January","February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    	for(int i = 0; i < 12; i++) {
    		monthMap.put(monthArr[i], i);
    	}
    	ObservableList<String> months = FXCollections.observableArrayList(monthArr);
    	monthChoice.setItems(months);
    	
    	// an array for next 5 years to be shown in the years' choice box. 
    	String[] yearArr = new String[5];
    	for (int i = 0; i < yearArr.length; i++) {
			yearArr[i] = (2022 + i) + "";
		}
    	ObservableList<String> years = FXCollections.observableArrayList(yearArr);
    	yearChoice.setItems(years);
    	
    	// saving each month's number of days in a hashmap object.
    	daysInMonth.put(0, 31);
    	daysInMonth.put(1, 28);
    	daysInMonth.put(2, 31);
    	daysInMonth.put(3, 30);
    	daysInMonth.put(4, 31);
    	daysInMonth.put(5, 30);
    	daysInMonth.put(6, 31);
    	daysInMonth.put(7, 31);
    	daysInMonth.put(8, 30);
    	daysInMonth.put(9, 31);
    	daysInMonth.put(10, 30);
    	daysInMonth.put(11, 31);
    	
    	// creating buttons for all the day slots
    	buttons = new ArrayList<>();
    	int index = 0;
    	for (int i = 0; i < 6; i++) {	// 7 days in a week
    		for (int j = 0; j < 7; j++) {	// 6 rows in a page
    			buttons.add(new Button(""));
        		grid.add(buttons.get(index), j, i);
        		index++;
    		}
    	}
    	
    	// setting the on click action.
    	for (Button butt : buttons) {
    		butt.setOnAction(e -> check(butt));
    	}
    }
}
