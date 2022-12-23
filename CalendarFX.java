import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// main application class, loads the fxml and calls for the controller's initialize().
public class CalendarFX extends Application{ 
	
	public void start(Stage stage) throws Exception{ 
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("calendar.fxml")); 
		Scene scene = new Scene(root); 
		stage.setTitle("Appointments"); 
		stage.setScene(scene); 
		stage.show();
	} 
	
	public static void main(String[] args) { 
		launch(args); 
		System.out.println();
	} 
}