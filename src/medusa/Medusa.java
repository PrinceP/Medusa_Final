/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package medusa;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Hp
 */
public class Medusa extends Application {
    public  boolean loggedIn = false;
    public  String messagefilename;
    public  String messagefileextension = null;
    public  String messagefiletype = null;
    public  String messagefilepath;
    public  String messagestring = null;
    public  long messagefilesize;
    public  int messagefilehashcode;
    public boolean messageselected = false;
    public String carrierfilename;
    public String carrierfileextension = null;
    public String carrierfiletype = null;
    public String carrierfilepath;
    public long carrierfilesize;
    public int carrierfilehashcode;
    public boolean carrierselected;
    public String username = null;
    public String password = null;
    public File messagefile ;
    public int encryptiontype = -1;
    public int messagetype = -1;
    public long messagesize = 0;
    public int carrierformat = 0;
    public File decryptedFile;                 
    public String encryptedfilepath = null;
    public String encryptedfilename = null;
    public String sendfilepath = null;
    public String decryptedstring = null;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public Date date = new Date();
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Medusa.fxml"));
        stage.setResizable(false);
        Scene scene = new Scene(root);
        MedusaController mc = new MedusaController();
        
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        File dir = new File("C:\\Users\\Default\\AppData\\Local\\Temp");  dir.mkdir();
    }
}