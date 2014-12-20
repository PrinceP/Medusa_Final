/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package medusa;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Hp
 */
public class MedusaController implements Initializable {
    public MedusaController()
    {
        
    }
    Medusa medusaUI = new Medusa();
    File localselectionmessagefile;
    
    @FXML
    private Pane MainWriteMessageTabPane, MainLocalMessageTabPane,  MainListMessageTabPane, 
            MainCarrierTabPane, MainCarrierListTabPane, MessageTextAreaPane, EncryptedFileInfoPane,
            DecryptionFileSelectPane, DecryptionWebLoginPane, EncDecPasswordPane, DecEncPassowrdPane;
    
    @FXML
    private AnchorPane root_ancpane,CryptographyAncPane ;
    
    @FXML
    private  Text MessageNameText, MessageTypeText, MessageSizeText, MessageExtensionText,
                  MessagePathText, MessageHashcodeText, CarrierNameText, CarrierTypeText,
            CarrierSizeText, CarrierExtensionText, CarrierPathText, CarrierHashcodeText, WebMessageTabResultText,
           FTPMessageText, NowSelectEncryptionType, NowSelectEncryptionType2, FileTypeMessage, CarrierFileSizeText, 
            SelectCarrierText, CarrierFTPText,EncryptionMessageText, DecryptionLoginAlertText;
    
    @FXML
    private Label DecryptionWebDriveLabel;
    
    
    @FXML
    private GridPane MedusaLoginGPane,DecryptionLoginGridPane;
    
    @FXML
    private TextField WebMessageUsernmaeTextfield, hostname_textfield, port_textfield, ftp_username_textfield, 
            ftp_passwordfield, ftp_timeout_textfield, CarrierUsernameTextArea,OutputFileTextField, 
            DecyptionUsernameTextfield, SaveUsername, DecryptedFilePath, DirectoryRemoteTextfield, DirectoryTextfield;
    
    @FXML
    private PasswordField CarrierFTPPasswordField, WebMessagePasswordField, DecryptionPasswordField, SavePassword, 
            EncDecPasswordField, DecEncPasswordField;
    
    @FXML
    private ListView MessageWebFileListview, CarrierFTPListView, DecryptionListView;
    
    @FXML
    private TextArea MessageWriteTextarea, EncryptedStringText, TextAreaProcess;
    
    @FXML
    private TabPane MainTabPane;
    
    @FXML
    private Tab main_carrier_tab,main_message_tab,main_encrypted_tab;
    
    @FXML
    private void handleWriteMessageAction(ActionEvent event) {
        MainWriteMessageTabPane.setVisible(true);
        MainLocalMessageTabPane.setVisible(false);
        MainListMessageTabPane.setVisible(false);
        TextAreaProcess.setText("Clicked Write Message Radio Button \n Now Write Message To Hide in TextArea in Main.Message Tab \n");
        
        
    }
    
    @FXML 
    private void handleMessageLocalDriveAction(ActionEvent event) {
        FileChooser fileChooserMessage = new FileChooser();
        //Set extension filter
        List<String> filefilter = Arrays.asList("*.txt", "*.html", "*.php", "*.aspx", "*.png", "*.bmp", "*.doc", "*.rtf" );
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Message Files", filefilter);
        fileChooserMessage.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File messagefile = fileChooserMessage.showOpenDialog(root_ancpane.getScene().getWindow());
        
        medusaUI.messagefilename = messagefile.getName();
        medusaUI.messagefilepath = messagefile.getPath();
        //compute extension
        int i = medusaUI.messagefilename.lastIndexOf('.');
        if (i > 0) 
        {
              medusaUI.messagefileextension = medusaUI.messagefilename.substring(i+1);
        }
        if("txt".equalsIgnoreCase(medusaUI.messagefileextension) || "doc".equalsIgnoreCase(medusaUI.messagefileextension) || "rtf".equalsIgnoreCase(medusaUI.messagefileextension)
           || "php".equalsIgnoreCase(medusaUI.messagefileextension) || "html".equalsIgnoreCase(medusaUI.messagefileextension)
                || "asp".equalsIgnoreCase(medusaUI.messagefileextension))
        {
            medusaUI.messagefiletype = "Document";
            
        }
        else if("png".equalsIgnoreCase(medusaUI.messagefileextension)|| "bmp".equalsIgnoreCase(medusaUI.messagefileextension))
        {
            medusaUI.messagefiletype = "Image";
        }
        
        medusaUI.messagefilesize = messagefile.length();
        medusaUI.messagefilehashcode = messagefile.hashCode();
       
        MainWriteMessageTabPane.setVisible(false);
        MainLocalMessageTabPane.setVisible(true);
        MainListMessageTabPane.setVisible(false);
        MessageNameText.setText(medusaUI.messagefilename);
        MessageExtensionText.setText(medusaUI.messagefileextension);
        MessageTypeText.setText(medusaUI.messagefiletype);
        MessageSizeText.setText(String.valueOf(medusaUI.messagefilesize));
        MessagePathText.setText(medusaUI.messagefilepath);
        MessageHashcodeText.setText(String.valueOf(medusaUI.messagefilehashcode));
        localselectionmessagefile = messagefile;
        medusaUI.messageselected = true;
               
    }
    
    @FXML
    private void handleMessageWebLoginAction(ActionEvent event)
    {   
        if(medusaUI.loggedIn==false)
        { 
           medusaUI.username = WebMessageUsernmaeTextfield.getText();
            medusaUI.password = WebMessagePasswordField.getText();
            FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   WebMessageTabResultText.setText("Log In Error");
   
                }
                else
                {   WebMessageTabResultText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                FTPFile[] ftpFiles = client.listFiles("/home/"+medusaUI.username);  
                if(medusaUI.loggedIn == true)
                {
                   
                   List<String> filedetail = new ArrayList<>();
                   
                   DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   for (FTPFile file : ftpFiles) {
                      String filename = file.getName();
                      String filesize = Long.toString(file.getSize());
                      String filedate = dateFormater.format(file.getTimestamp().getTime());
                      String filetype = null;
                      int j = filename.lastIndexOf('.');
                      if (j > 0) 
                      {
                         filetype = filename.substring(j+1);
                      }
                      filedetail.add(filename+"\t\t"+filesize+"\t\t"+filetype+"\t\t"+filedate);
                      
                   }
                  ObservableList<String> filedetaildata = FXCollections.observableArrayList(filedetail);
                  MessageWebFileListview.setItems(filedetaildata);
                 
                  
             }
           client.logout();
        } catch (IOException e) {
            WebMessageTabResultText.setText("Oops! Something wrong happened");
        } finally {
           try {
                if (client.isConnected()) {
                    client.logout();
                    client.disconnect();}
               } catch (IOException e) {
            }
        }
     }
    }
    
    
        
    @FXML
    private void handleMessageWebDriveAction(ActionEvent event)
    {       
               MainWriteMessageTabPane.setVisible(false);
               MainLocalMessageTabPane.setVisible(false);
               MainListMessageTabPane.setVisible(true);
           if(medusaUI.loggedIn==true)
          { 
            medusaUI.username = WebMessageUsernmaeTextfield.getText();
            medusaUI.password = WebMessagePasswordField.getText();
            FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   WebMessageTabResultText.setText("Log In Error");
   
                }
                else
                {   WebMessageTabResultText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                FTPFile[] ftpFiles = client.listFiles("/home/"+medusaUI.username);  
                if(medusaUI.loggedIn == true)
                {
                   
                   List<String> filedetail = new ArrayList<>();
                   
                   DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   for (FTPFile file : ftpFiles) {
                      String filename = file.getName();
                      String filesize = Long.toString(file.getSize());
                      String filedate = dateFormater.format(file.getTimestamp().getTime());
                      String filetype = null;
                      int j = filename.lastIndexOf('.');
                      if (j > 0) 
                      {
                         filetype = filename.substring(j+1);
                      }
                      filedetail.add(filename+"\t\t"+filesize+"\t\t"+filetype+"\t\t"+filedate);
                      
                   }
                  ObservableList<String> filedetaildata = FXCollections.observableArrayList(filedetail);
                  MessageWebFileListview.setItems(filedetaildata);
                 
                  
             }
           client.logout();
        } catch (IOException e) {
            WebMessageTabResultText.setText("Oops! Something wrong happened!!!");
        } finally {
           try {
                if (client.isConnected()) {
                    client.logout();
                    client.disconnect();}
               } catch (IOException e) {
            }
        }
     }
        else
           {
               WebMessageTabResultText.setText("Log In First");
           }
    
                
            
    }
    
   
    
   @FXML
   private void handleMessageWebFileSelectAction(ActionEvent event)
   {
      String selected = MessageWebFileListview.getSelectionModel().getSelectedItem().toString(); 
      int k = selected.indexOf("\t");
      if (k > 0) 
      {
          selected = selected.substring(0, k);
      }               
      if(medusaUI.loggedIn==true)
          { 
            medusaUI.username = WebMessageUsernmaeTextfield.getText();
            medusaUI.password = WebMessagePasswordField.getText();
            FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                client.enterLocalPassiveMode();
                client.setFileType(FTP.BINARY_FILE_TYPE);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   WebMessageTabResultText.setText("Log In Error");
   
                }
                else
                {   WebMessageTabResultText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                 
                if(medusaUI.loggedIn == true)
                {  
                    String selectedfilepath = "/home/"+medusaUI.username+selected;
                    File downloadFile = new File("C:/Users/Default/AppData/Local/Temp"+selected);
                    boolean success;
                    try (OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
                        success = client.retrieveFile(selectedfilepath, outputStream1);
                    }
 
                    if (success) {
                     System.out.println("File #1 has been downloaded successfully.");
                    }
                    
                    medusaUI.messagefilename = downloadFile.getName();
                    medusaUI.messagefilepath = downloadFile.getPath();
                    //compute extension
                   int i = medusaUI.messagefilename.lastIndexOf('.');
                   if (i > 0) 
                   {
                     medusaUI.messagefileextension = medusaUI.messagefilename.substring(i+1);
                   }
                   if("txt".equalsIgnoreCase(medusaUI.messagefileextension) || "doc".equalsIgnoreCase(medusaUI.messagefileextension) || "rtf".equalsIgnoreCase(medusaUI.messagefileextension) || "html".equalsIgnoreCase(medusaUI.messagefileextension) || "aspx".equalsIgnoreCase(medusaUI.messagefileextension))
                   {
                     medusaUI.messagefiletype = "Document";
            
                   }
                   else if("png".equalsIgnoreCase(medusaUI.messagefileextension)|| "bmp".equalsIgnoreCase(medusaUI.messagefileextension))
                   {
                     medusaUI.messagefiletype = "Image";
                   }
                   
                   medusaUI.messagefilesize = downloadFile.length();
                   medusaUI.messagefilehashcode = downloadFile.hashCode();
       
                   MainWriteMessageTabPane.setVisible(false);
                   MainLocalMessageTabPane.setVisible(true);
                   MainListMessageTabPane.setVisible(false);
                   MessageNameText.setText(medusaUI.messagefilename);
                   MessageExtensionText.setText(medusaUI.messagefileextension);
                   MessageTypeText.setText(medusaUI.messagefiletype);
                   MessageSizeText.setText(String.valueOf(medusaUI.messagefilesize));
                   MessagePathText.setText(medusaUI.messagefilepath);
                   MessageHashcodeText.setText(String.valueOf(medusaUI.messagefilehashcode));
                   localselectionmessagefile =downloadFile; 
                    
                }client.logout();
               } catch (IOException e) {
                     WebMessageTabResultText.setText("Oops! Something wrong happened");
              } finally {
               try {
                   if (client.isConnected()) {
                       client.logout();
                       client.disconnect();}
                  } catch (IOException e) {
                }
            }
       }
   }
   
   @FXML
   private void handleMessageWriteOkAction(ActionEvent event) throws IOException
   {
       medusaUI.messagestring = MessageWriteTextarea.getText();
       medusaUI.messagetype = 0;        
       CryptographyAncPane.isFocused();
       medusaUI.messageselected = true;
       NowSelectEncryptionType.setText("Now Select Encryption Type");
       
   }
   
   @FXML
   private void handleMessageOkAction(ActionEvent event) throws IOException
   {   
       if("Document".equals(medusaUI.messagefiletype) || "Image".equals(medusaUI.messagefiletype))
       { 
       medusaUI.messagefile = localselectionmessagefile;
       medusaUI.messagestring = "." + medusaUI.messagefileextension + " ";
       medusaUI.messagestring = medusaUI.messagestring.concat(Files.toString(new File(medusaUI.messagefilepath), Charsets.UTF_8));    
       medusaUI.messagesize = medusaUI.messagestring.length();
       medusaUI.messageselected = true;
       medusaUI.messagetype = 0;
       CryptographyAncPane.isFocused();
       NowSelectEncryptionType2.setText("Now Select Encryption Type");
       medusaUI.messageselected = true;
       }
            
    }
   
   @FXML
    private void handleCarrierLocalDriveAction(ActionEvent event)
    {  
        if(medusaUI.encryptiontype != -1 || medusaUI.messageselected)
        {
         FileChooser fileChooserCarrier = new FileChooser();
         //Set extension filter
         List<String> filefilter = Arrays.asList("*.png", "*.bmp", "*.wav", "*.mp3");
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("carrier", filefilter);
         fileChooserCarrier.getExtensionFilters().add(extFilter);
         //Show save file dialog
         File carrierfile = fileChooserCarrier.showOpenDialog(root_ancpane.getScene().getWindow());
        
         medusaUI.carrierfilename = carrierfile.getName();
         medusaUI.carrierfilepath = carrierfile.getPath();
         //compute extension
         int i = medusaUI.carrierfilename.lastIndexOf('.');
         if (i > 0) 
         {
               medusaUI.carrierfileextension = medusaUI.carrierfilename.substring(i+1);
         }
         if("png".equalsIgnoreCase(medusaUI.carrierfileextension) || "bmp".equalsIgnoreCase(medusaUI.carrierfileextension) )
         {
             medusaUI.carrierfiletype = "Image";
            
         }
          else if("wav".equalsIgnoreCase(medusaUI.carrierfileextension)|| "mp3".equalsIgnoreCase(medusaUI.carrierfileextension))
         {
             medusaUI.carrierfiletype = "Audio";
         }
         
         medusaUI.carrierfilesize = carrierfile.length();
         medusaUI.carrierfilehashcode = carrierfile.hashCode();
       
         MainCarrierListTabPane.setVisible(false);
         MainCarrierTabPane.setVisible(true);
        
         CarrierNameText.setText(medusaUI.carrierfilename);
         CarrierExtensionText.setText(medusaUI.carrierfileextension);
         CarrierTypeText.setText(medusaUI.carrierfiletype);
         CarrierSizeText.setText(String.valueOf(medusaUI.carrierfilesize));
         CarrierPathText.setText(medusaUI.carrierfilepath);
         CarrierHashcodeText.setText(String.valueOf(medusaUI.carrierfilehashcode));
         SelectCarrierText.setText("");
         medusaUI.carrierselected = true;
        }
        else
        {
            
             if(medusaUI.messageselected == false)
            {
                MainWriteMessageTabPane.setVisible(true);
                MainLocalMessageTabPane.setVisible(false);
        
                MainListMessageTabPane.setVisible(false);
                NowSelectEncryptionType.setText("Select Message First");
                MainTabPane.getSelectionModel().select(main_message_tab);
            }
             else if(medusaUI.encryptiontype == -1)
            {
                MainWriteMessageTabPane.setVisible(true);
                MainLocalMessageTabPane.setVisible(false);
        
                MainListMessageTabPane.setVisible(false);
                NowSelectEncryptionType.setText("Select Encryption Type");
                MainTabPane.getSelectionModel().select(main_message_tab);
            }
        }
        
    }
   
    @FXML
    private void handleCarrierWebDriveAction(ActionEvent event)
    {    if(medusaUI.messageselected || medusaUI.encryptiontype != -1)
         {
              MainCarrierTabPane.setVisible(false);
               MainCarrierListTabPane.setVisible(true);
               
           if(medusaUI.loggedIn==true)
          { 
            medusaUI.username = CarrierUsernameTextArea.getText();
            medusaUI.password = CarrierFTPPasswordField.getText();
            FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   CarrierFTPText.setText("Log In Error");
   
                }
                else
                {   CarrierFTPText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                FTPFile[] ftpFiles = client.listFiles("/home/"+medusaUI.username);  
                if(medusaUI.loggedIn == true)
                {
                   
                   List<String> filedetail = new ArrayList<>();
                   
                   DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   for (FTPFile file : ftpFiles) {
                      String filename = file.getName();
                      String filesize = Long.toString(file.getSize());
                      String filedate = dateFormater.format(file.getTimestamp().getTime());
                      String filetype = null;
                      int j = filename.lastIndexOf('.');
                      if (j > 0) 
                      {
                         filetype = filename.substring(j+1);
                      }
                      filedetail.add(filename+"\t\t"+filesize+"\t\t"+filetype+"\t\t"+filedate);
                      
                   }
                  ObservableList<String> filedetaildata = FXCollections.observableArrayList(filedetail);
                  CarrierFTPListView.setItems(filedetaildata);
                    }
           client.logout();
        } catch (IOException e) {
            CarrierFTPText.setText("Oops! Something wrong happened!!!");
        } finally {
           try {
                if (client.isConnected()) {
                    client.logout();
                    client.disconnect();}
               } catch (IOException e) {
            }
        }
     }
        else
           {
              CarrierFTPText.setText("Log In First");
           }
         }
     else
        {
            
            if(medusaUI.messageselected == false)
            {
                MainWriteMessageTabPane.setVisible(true);
                MainLocalMessageTabPane.setVisible(false);
        
                MainListMessageTabPane.setVisible(false);
                NowSelectEncryptionType.setText("Select Message First");
                MainTabPane.getSelectionModel().select(main_message_tab);
            }
            else if(medusaUI.encryptiontype == -1)
            {
                MainWriteMessageTabPane.setVisible(true);
                MainLocalMessageTabPane.setVisible(false);
        
                MainListMessageTabPane.setVisible(false);
                NowSelectEncryptionType.setText("Select Encryption Type");
                MainTabPane.getSelectionModel().select(main_message_tab);
            }
        }
        
    }
    
    @FXML
    private void handleCarrierWebLoginAction(ActionEvent event)
    {
        if(medusaUI.loggedIn==false)
        { 
           medusaUI.username = CarrierUsernameTextArea.getText();
            medusaUI.password = CarrierFTPPasswordField.getText();
            FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   CarrierFTPText.setText("Log In Error");
   
                }
                else
                {   CarrierFTPText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                FTPFile[] ftpFiles = client.listFiles("/home/"+medusaUI.username);  
                if(medusaUI.loggedIn == true)
                {
                   
                   List<String> filedetail = new ArrayList<>();
                   
                   DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   for (FTPFile file : ftpFiles) {
                      String filename = file.getName();
                      String filesize = Long.toString(file.getSize());
                      String filedate = dateFormater.format(file.getTimestamp().getTime());
                      String filetype = null;
                      int j = filename.lastIndexOf('.');
                      if (j > 0) 
                      {
                         filetype = filename.substring(j+1);
                      }
                      filedetail.add(filename+"\t\t"+filesize+"\t\t"+filetype+"\t\t"+filedate);
                      
                   }
                  ObservableList<String> filedetaildata = FXCollections.observableArrayList(filedetail);
                  CarrierFTPListView.setItems(filedetaildata);
                 
                  
             }
           client.logout();
        } catch (IOException e) {
            CarrierFTPText.setText("Oops! Something wrong happened");
        } finally {
           try {
                if (client.isConnected()) {
                    client.logout();
                    client.disconnect();}
               } catch (IOException e) {
            }
        }
     }
    }
    
    @FXML
    private void handleCarrierWebFileSelectAction(ActionEvent event)
    {
        String selected = CarrierFTPListView.getSelectionModel().getSelectedItem().toString(); 
      int k = selected.indexOf("\t");
      if (k > 0) 
      {
          selected = selected.substring(0, k);
      }               
      if(medusaUI.loggedIn==true)
          { 
            medusaUI.username = CarrierUsernameTextArea.getText();
            medusaUI.password = CarrierFTPPasswordField.getText();
            FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                client.enterLocalPassiveMode();
                client.setFileType(FTP.BINARY_FILE_TYPE);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   CarrierFTPText.setText("Log In Error");
   
                }
                else
                {   CarrierFTPText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                 
                if(medusaUI.loggedIn == true)
                {  
                    String selectedfilepath = "/home/"+medusaUI.username+selected;
                    File downloadFile = new File("C:/Users/Default/AppData/Local/Temp/"+"Carrier"+selected);
                    boolean success;
                    try (OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
                        success = client.retrieveFile(selectedfilepath, outputStream1);
                    }
 
                    if (success) {
                     System.out.println("File #1 has been downloaded successfully.");
                    }
                    
                    medusaUI.carrierfilename = downloadFile.getName();
                    medusaUI.carrierfilepath = downloadFile.getPath();
                    //compute extension
                   int i = medusaUI.carrierfilename.lastIndexOf('.');
                   if (i > 0) 
                   {
                     medusaUI.carrierfileextension = medusaUI.carrierfilename.substring(i+1);
                   }
                   if("txt".equalsIgnoreCase(medusaUI.carrierfileextension) || "doc".equalsIgnoreCase(medusaUI.carrierfileextension) || "rtf".equalsIgnoreCase(medusaUI.carrierfileextension) || "html".equalsIgnoreCase(medusaUI.carrierfileextension) || "aspx".equalsIgnoreCase(medusaUI.carrierfileextension))
                   {
                     medusaUI.carrierfiletype = "Document";
            
                   }
                   else if("png".equalsIgnoreCase(medusaUI.carrierfileextension)|| "bmp".equalsIgnoreCase(medusaUI.carrierfileextension))
                   {
                     medusaUI.carrierfiletype = "Image";
                   }
                   
                   medusaUI.carrierfilesize = downloadFile.length();
                   medusaUI.carrierfilehashcode = downloadFile.hashCode();
       
                   MainCarrierListTabPane.setVisible(false);
                   MainCarrierTabPane.setVisible(true);
                   CarrierNameText.setText(medusaUI.messagefilename);
                   CarrierExtensionText.setText(medusaUI.messagefileextension);
                   CarrierTypeText.setText(medusaUI.messagefiletype);
                   CarrierSizeText.setText(String.valueOf(medusaUI.messagefilesize));
                   CarrierPathText.setText(medusaUI.messagefilepath);
                   CarrierHashcodeText.setText(String.valueOf(medusaUI.messagefilehashcode));
                   localselectionmessagefile =downloadFile; 
                    
                }client.logout();
               } catch (IOException e) {
                     CarrierFTPText.setText("Oops! Something wrong happened");
              } finally {
               try {
                   if (client.isConnected()) {
                       client.logout();
                       client.disconnect();}
                  } catch (IOException e) {
                }
            }
       }
    }
    
   
    
    @FXML
    private void handleCarrierFileOKButtonAction(ActionEvent event) throws FileNotFoundException, IOException
    {
        if(medusaUI.carrierselected == true || medusaUI.messageselected ==true ||medusaUI.encryptiontype != -1 )
        {
            MainTabPane.getSelectionModel().select(main_encrypted_tab);
    }
    }
    
    @FXML
    private void handleDoEncryptionAction(ActionEvent event)
    {
        
        
         
        if(medusaUI.carrierselected == true && medusaUI.messageselected == true && medusaUI.encryptiontype == 0 )
        {    
            if("png".equals(medusaUI.carrierfileextension) || "jpg".equals(medusaUI.carrierfileextension))
            {   
                if(medusaUI.messagetype == 0)
                {  medusasteago medusaSteago = new medusasteago();
                   String outputfile = OutputFileTextField.getText();
                   boolean encryption_message = false;
                   encryption_message = medusaSteago.encode(medusaUI.carrierfilepath , medusaUI.carrierfilename , medusaUI.carrierfileextension  , outputfile.toString() , medusaUI.messagestring);
                   EncryptionMessageText.setText(Boolean.toString(encryption_message));
                }
            }
            else if("wav".equals(medusaUI.carrierfileextension) || "mp3".equals(medusaUI.carrierfileextension))
            {
                if(medusaUI.messagetype == 0)
                {   Encoder encoder = new Encoder();
                    String encrypted_filepath = null;
                    
                    encrypted_filepath = encoder.realencode(medusaUI.messagestring, medusaUI.carrierfilepath);
                    
                    OutputFileTextField.setText(encrypted_filepath);
                    
                }
            }
    }
    }
    
    @FXML
    private void handleFileFromLocalDriveAction(ActionEvent event)
    {
        FileChooser fileChooserMessage = new FileChooser();
        //Set extension filter
        List<String> filefilter = Arrays.asList( "*.png", "*.bmp", "*.wav", "*.mp3" );
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Encrypted Files", filefilter);
        fileChooserMessage.getExtensionFilters().add(extFilter);
        //Show save file dialog
        
        File decryptedfile = fileChooserMessage.showOpenDialog(root_ancpane.getScene().getWindow());
         medusaUI.decryptedFile = decryptedfile;
         medusaUI.encryptedfilepath = decryptedfile.getPath();
         medusaUI.encryptedfilename = decryptedfile.getName();
       /*
        String encryptedfilename = decryptedfile.getName();
        String encryptedfilepath = decryptedfile.getPath();
        String encryptedfileextension = null;
        String encryptedfiletype = null;
        //compute extension
        int i = encryptedfilename.lastIndexOf('.');
        if (i > 0) 
        {
              encryptedfileextension = encryptedfilename.substring(i+1);
        }
        if("txt".equalsIgnoreCase(encryptedfileextension) || "doc".equalsIgnoreCase(encryptedfileextension) || "rtf".equalsIgnoreCase(encryptedfileextension))
        {
            encryptedfiletype = "Document";
            
        }
        else if("png".equalsIgnoreCase(encryptedfileextension)|| "bmp".equalsIgnoreCase(encryptedfileextension))
        {
            encryptedfiletype = "Image";
        }
        
        long encryptedfilesize = decryptedfile.length();
        long encryptedfilehashcode = decryptedfile.hashCode();
       
        MainWriteMessageTabPane.setVisible(false);
        MainLocalMessageTabPane.setVisible(true);
        MainListMessageTabPane.setVisible(false);
        MessageNameText.setText(encryptedfilename);
        MessageExtensionText.setText(encryptedfileextension);
        MessageTypeText.setText(encryptedfiletype);
        MessageSizeText.setText(String.valueOf(encryptedfilesize));
        MessagePathText.setText(encryptedfilepath);
        MessageHashcodeText.setText(String.valueOf(encryptedfilehashcode));
        medusaUI.decryptedFile = decryptedfile;
       // medusaUI.encryptedselected = true;
       */
        
    }
    
    @FXML
    private void handleFileFromWebDriveAction(ActionEvent event)
    {
       DecryptionFileSelectPane.setVisible(false);
          DecryptionWebLoginPane.setVisible(true); 
      if(medusaUI.loggedIn == true)
      { FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   DecryptionLoginAlertText.setText("Log In Error");
   
                }
                else
                {   DecryptionLoginAlertText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                FTPFile[] ftpFiles = client.listFiles("/home/"+medusaUI.username);  
                if(medusaUI.loggedIn == true)
                {
                   
                   List<String> filedetail = new ArrayList<>();
                   
                   DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   for (FTPFile file : ftpFiles) {
                      String filename = file.getName();
                      String filesize = Long.toString(file.getSize());
                      String filedate = dateFormater.format(file.getTimestamp().getTime());
                      String filetype = null;
                      int j = filename.lastIndexOf('.');
                      if (j > 0) 
                      {
                         filetype = filename.substring(j+1);
                      }
                      filedetail.add(filename+"\t\t"+filesize+"\t\t"+filetype+"\t\t"+filedate);
                      
                   }
                  ObservableList<String> filedetaildata = FXCollections.observableArrayList(filedetail);
                  DecryptionListView.setItems(filedetaildata);
                    }
           client.logout();
        } catch (IOException e) {
            DecryptionLoginAlertText.setText("Oops! Something wrong happened!!!");
        } finally {
           try {
                if (client.isConnected()) {
                    client.logout();
                    client.disconnect();}
               } catch (IOException e) {
            }
        }
     }
        else
           {
              DecryptionLoginAlertText.setText("Log In First");
           }
         
    }
    
    @FXML
    private void handleDecryptionWebLoginAction(ActionEvent event)
    {
        if(medusaUI.loggedIn==false)
        { 
           medusaUI.username = DecyptionUsernameTextfield.getText();
            medusaUI.password = DecryptionPasswordField.getText();
            FTPClient client = new FTPClient();
            try{
                client.connect("192.168.1.7",21);
                client.login(medusaUI.username,medusaUI.password);
                int replyCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                   DecryptionLoginAlertText.setText("Log In Error");
   
                }
                else
                {   DecryptionLoginAlertText.setText("Logged In");
                    medusaUI.loggedIn = true;
                }
                //  On Process Tab Print the server messages
                FTPFile[] ftpFiles = client.listFiles("/home/"+medusaUI.username);  
                if(medusaUI.loggedIn == true)
                {
                   
                   List<String> filedetail = new ArrayList<>();
                   
                   DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   for (FTPFile file : ftpFiles) {
                      String filename = file.getName();
                      String filesize = Long.toString(file.getSize());
                      String filedate = dateFormater.format(file.getTimestamp().getTime());
                      String filetype = null;
                      int j = filename.lastIndexOf('.');
                      if (j > 0) 
                      {
                         filetype = filename.substring(j+1);
                      }
                      filedetail.add(filename+"\t\t"+filesize+"\t\t"+filetype+"\t\t"+filedate);
                      
                   }
                  ObservableList<String> filedetaildata = FXCollections.observableArrayList(filedetail);
                  DecryptionListView.setItems(filedetaildata);
                 
                  
             }
           client.logout();
        } catch (IOException e) {
            DecryptionLoginAlertText.setText("Oops! Something wrong happened");
        } finally {
           try {
                if (client.isConnected()) {
                    client.logout();
                    client.disconnect();}
               } catch (IOException e) {
            }
        }
     }
    }
    
    @FXML
    private void handleDecryptionWebFileSelectAction(ActionEvent event)
    {
        
    }
    
    @FXML
    private void handleDecryptFileButtonAction(ActionEvent event) throws IOException
    {
        String messagetext = null;
      
      if(!medusaUI.encryptedfilepath.isEmpty())
      {   int j = medusaUI.encryptedfilename.lastIndexOf('.');
          String fileextension = null;
          if(j>0)
          {
              fileextension = medusaUI.encryptedfilename.substring(j+1);
          }
          if("png".equals(fileextension) || "jpg".equals(fileextension))
          {
              
          medusasteago medusaSteago = new medusasteago();
          
          messagetext = medusaSteago.decode(medusaUI.encryptedfilepath, medusaUI.encryptedfilename);
          
          
          
          }
          else if("wav".equals(fileextension) || "mp3".equals(fileextension))
          {
              Decoder decoder = new Decoder();
              messagetext = decoder.realdecode(medusaUI.encryptedfilepath);
          }
          int m = messagetext.indexOf("&AES#");
          System.out.println(m);
          if(m == 0)
          {
              medusaUI.decryptedstring = messagetext.substring(m+5);
              System.out.println(medusaUI.decryptedstring);
              EncDecPasswordField.setVisible(false);
              DecEncPassowrdPane.setVisible(true);
              
              
          }
          else
          {
          MessageTextAreaPane.setVisible(true);
          EncryptedFileInfoPane.setVisible(false);
          String extension = null;
          int k = messagetext.indexOf(".");
          int l = messagetext.indexOf(" ");
          System.out.println(k);
          System.out.println(l);
          if(k>=0 && l>0)
          {
              extension = messagetext.substring(k, l);
              System.out.println(extension);
              File newTextFile = new File(DecryptedFilePath.getText()+"decrypted"+extension);

               FileWriter fw = new FileWriter(newTextFile);
              
              System.out.println(newTextFile.getPath());
              messagetext = messagetext.substring(l+1);
              fw.write(messagetext);
              fw.close();
          }
          else
          {
          EncryptedStringText.setText(messagetext);
          }
          }
          
      }
    }
    
    @FXML
    private void handleDecryptedMessageOpenAction(ActionEvent event)
    {
        
    }
    
   
    
    @FXML
    private void handleWithoutEncyptionAction(ActionEvent event)
    {  if(medusaUI.messageselected == true)
      {
       medusaUI.encryptiontype = 0;
        
        if(medusaUI.messagetype == 1)
        {
        medusaUI.messagesize = medusaUI.messagefile.length();
        }
        else if(medusaUI.messagetype == 0)
        {
        medusaUI.messagesize = medusaUI.messagestring.length();
        }
        //System.out.println(medusaUI.messagesize);
        if(medusaUI.messagesize <50000)
        {
            medusaUI.carrierformat = 1;
            FileTypeMessage.setText("SELECT IMAGE CARRIER");
            float carriersize = (medusaUI.messagesize * 10)/ 1000 ;
            CarrierFileSizeText.setText("file size > " + Float.toString(carriersize) + "KB");
            
        }
        else if(medusaUI.messagesize < 1000000)
        {
            medusaUI.carrierformat = 2;
            FileTypeMessage.setText("SELECT AUDIO CARRIER");
            long carriersize = (medusaUI.messagesize * 10)/ 1000 ;
            CarrierFileSizeText.setText("file size > " + Long.toString(carriersize) + "KB");
            
        }
        MainTabPane.getSelectionModel().select(main_carrier_tab);
        SelectCarrierText.setText("Now Select Carrier Type");
      }
      else
    {   MainWriteMessageTabPane.setVisible(true);
        MainLocalMessageTabPane.setVisible(false);
        
        MainListMessageTabPane.setVisible(false);
        NowSelectEncryptionType.setText("Select Message First");
        MainTabPane.getSelectionModel().select(main_message_tab);
        
    }
    }
    
    @FXML
    private void handleAESEncryptioAction(ActionEvent event)
    {   if(medusaUI.messageselected)
       {
         medusaUI.encryptiontype = 1;
         EncDecPasswordPane.setVisible(true);
         DecEncPassowrdPane.setVisible(false);
         
         }
       
     else
     {
        MainWriteMessageTabPane.setVisible(true);
        MainLocalMessageTabPane.setVisible(false);
        
        MainListMessageTabPane.setVisible(false);
        NowSelectEncryptionType.setText("Select Message First");
        MainTabPane.getSelectionModel().select(main_message_tab);
     }
    }
    
    @FXML
    private void handleHashingAction(ActionEvent event)
    {   if(medusaUI.messageselected)
      {
           medusaUI.encryptiontype = 2;
      }
       
    else
    {
        MainWriteMessageTabPane.setVisible(true);
        MainLocalMessageTabPane.setVisible(false);
        
        MainListMessageTabPane.setVisible(false);
        NowSelectEncryptionType.setText("Select Message First");
        MainTabPane.getSelectionModel().select(main_message_tab);
    }
    }  
    
    @FXML
    private void handleEncDecAction(ActionEvent event)
    {  if(medusaUI.messageselected == true)
      {
        if(medusaUI.encryptiontype != -1)
        {
            if(medusaUI.encryptiontype == 1)
            {
                String password = EncDecPasswordField.getText();
                ChilkatExample chilkatexample = new ChilkatExample();
                medusaUI.messagestring = chilkatexample.doencryption(medusaUI.messagestring, password, 1);
                medusaUI.messagestring = "&AES#" + medusaUI.messagestring;
                if(medusaUI.messagesize <50000)
        {
            medusaUI.carrierformat = 1;
            FileTypeMessage.setText("SELECT IMAGE CARRIER");
            float carriersize = (medusaUI.messagesize * 10)/ 1000 ;
            CarrierFileSizeText.setText("file size > " + Float.toString(carriersize) + "KB");
            
        }
        else if(medusaUI.messagesize < 1000000)
        {
            medusaUI.carrierformat = 2;
            FileTypeMessage.setText("SELECT AUDIO CARRIER");
            long carriersize = (medusaUI.messagesize * 10)/ 1000 ;
            CarrierFileSizeText.setText("file size > " + Long.toString(carriersize) + "KB");
            
        }
        MainTabPane.getSelectionModel().select(main_carrier_tab);
        SelectCarrierText.setText("Now Select Carrier Type");
      }
            }                
            
        }
      else
    {   MainWriteMessageTabPane.setVisible(true);
        MainLocalMessageTabPane.setVisible(false);
        
        MainListMessageTabPane.setVisible(false);
        NowSelectEncryptionType.setText("Select Message First");
        MainTabPane.getSelectionModel().select(main_message_tab);
        
    }
        medusaUI.encryptiontype = 0;
    }
    
    @FXML
    private void handleDecEncAction(ActionEvent event) throws IOException
    { 
        MessageTextAreaPane.setVisible(true);
          EncryptedFileInfoPane.setVisible(false);
          System.out.println("lol");
        String password = DecEncPasswordField.getText();
        System.out.println("lol");
        ChilkatExample chilkatexample = new ChilkatExample();
        System.out.println("lol");
        String messagetext = chilkatexample.doencryption(medusaUI.decryptedstring, password, 2);
        
          String extension = null;
          int k = messagetext.indexOf(".");
          int l = messagetext.indexOf(" ");
          System.out.println(k);
          System.out.println(l);
          if(k>=0 && l>0)
          {
              extension = messagetext.substring(k, l);
              System.out.println(extension);
              File newTextFile = new File(DecryptedFilePath.getText()+"decrypted"+extension);
            try (FileWriter fw = new FileWriter(newTextFile)) {
                System.out.println(newTextFile.getPath());
                messagetext = messagetext.substring(l+1);
                fw.write(messagetext);
            }
          }
          else
          {
          EncryptedStringText.setText(messagetext);
          }
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event)
    {   
        DecryptionWebLoginPane.setVisible(false);
        DecryptionFileSelectPane.setVisible(true);
        
    }
    
    @FXML
    private void handleSaveWebdriveAction(ActionEvent event)
    {  
        medusaUI.username = SaveUsername.getText();
        medusaUI.password = SavePassword.getText();
        medusaUI.loggedIn = true;
        String server = "192.168.1.7";
        int port = 21;
        String user = medusaUI.username;
        String pass = medusaUI.password;
 
        FTPClient ftpClient = new FTPClient();
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
 
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(medusaUI.encryptedfilepath);
 
            String firstRemoteFile = "/home/"+medusaUI.username+medusaUI.encryptedfilename;
            boolean done;
            try (InputStream inputStream = new FileInputStream(firstLocalFile)) {
                System.out.println("Start uploading first file");
                done = ftpClient.storeFile(firstRemoteFile, inputStream);
            }
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
 
             } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
            }
        }
    }
    
   @FXML
   private void handleSendFTPAction(ActionEvent event)
   {
       String server = hostname_textfield.getText();
        int port = Integer.parseInt(port_textfield.getText());
        String user = ftp_username_textfield.getText() ;
        String pass = ftp_passwordfield.getText();
 
        FTPClient ftpClient = new FTPClient();
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
 
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
           
            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(DirectoryTextfield.getText());
 
            String firstRemoteFile = DirectoryRemoteTextfield.getText();
           boolean done;
           try (InputStream inputStream = new FileInputStream(firstLocalFile)) {
               System.out.println("Start uploading first file");
               System.out.println(firstRemoteFile);
               done = ftpClient.storeFile(firstRemoteFile, inputStream);
               System.out.println(done);
           }
            if (done) {
                System.out.println("The first file is uploaded successfully.");
                FTPMessageText.setText("File Uploaded");
            }

 
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            FTPMessageText.setText("Upload Unsucsessful");
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
            }
        }
   }
   
   @Override
    public void initialize(URL url, ResourceBundle rb) {
       
       TextAreaProcess.setText("Medusa UI Started \n for tutorial visit http://192.168.1.7/ \n"
              + medusaUI.dateFormat.format(medusaUI.date) + "\n\n\n\n\n");
        MainWriteMessageTabPane.setVisible(true);
        MainLocalMessageTabPane.setVisible(false);
        
        MainListMessageTabPane.setVisible(false);
        MainCarrierListTabPane.setVisible(false);
        MainCarrierTabPane.setVisible(true);
        
        MessageTextAreaPane.setVisible(false);
          EncryptedFileInfoPane.setVisible(false);
          
          
          DecryptionFileSelectPane.setVisible(true);
          DecryptionWebLoginPane.setVisible(false);
          
          EncDecPasswordPane.setVisible(false);
          DecEncPassowrdPane.setVisible(false);
        //To change body of generated methods, choose Tools | Templates.
        
   }
}