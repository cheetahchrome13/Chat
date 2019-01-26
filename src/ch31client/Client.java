
package ch31client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class Client creates a client chat box
 * @author Justin Mangan
 */
public class Client extends Application{
    
    //
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    
    //
    BorderPane tfPane = new BorderPane();
    Label lbl = new Label("Press ENTER to Send");
    TextField tf = new TextField();
    BorderPane mainPane = new BorderPane();
    TextArea ta = new TextArea();
    Scene scene = new Scene(mainPane, 450, 300);
    
    /**
     * Create a controls and put them in a scene
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage){
        
        tfPane.setPadding(new Insets(5,5,5,5));
        tfPane.setStyle("-fx-border-color: green");
        
        lbl.setAlignment(Pos.CENTER);
        tfPane.setBottom(lbl);
        BorderPane.setAlignment(lbl, Pos.CENTER);
        
        
        tf.setAlignment(Pos.CENTER);
        tfPane.setCenter(tf);
        
        
        mainPane.setTop(new ScrollPane(ta));
        mainPane.setCenter(tfPane);
        
        
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        tf.requestFocus();
        
        /**
         * Handler sends message on Enter
         */
        tf.setOnAction(e-> {
            try{
                // get msg in text field
                String msgOut = tf.getText();
                
                // print to client screen
                ta.appendText("\nClient: " + msgOut + "\n");
                
                // send to server
                toServer.writeUTF(msgOut);
                toServer.flush();
                
                // clear the text field
                Platform.runLater(()->{
                    tf.setText("");
                    });
                
            }
            catch(IOException ex){
                System.err.println(ex);
            }
        });
        
        /**
         * Creates a Thread and input/output streams to send messages back and forth
         */
        new Thread(()-> {
            try{
                // create socket
                Socket socket = new Socket("localhost", 5000);
                Platform.runLater(()->
                    ta.appendText("Chat started " + new Date() + "\n\n"));

                //create input stream to receive data from server
                fromServer = new DataInputStream(socket.getInputStream());
            
                // create an output stream
                toServer = new DataOutputStream(socket.getOutputStream());
                
                while(true){
                    
                    // receive msg from server
                    String msgIn = fromServer.readUTF();
                    
                    // print msg in txtarea
                    ta.appendText("\n\t\t\t\tServer: " + msgIn + "\n");
                       
                    //
                    Platform.runLater(()->{
//                        ta.appendText("\t\treceived " + new Date()+ "\n");
                    });
                }
            }
            catch(IOException ex){
            ta.appendText(ex.toString() + "\n");
            }
        }).start();
    }  
    
    /**
     * Main
     * @param args 
     */
    public static void main(String args[]){
        launch();
    }
}
