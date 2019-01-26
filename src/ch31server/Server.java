
package ch31server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class Client creates a chat box for client
 * @author Justin Mangan
 */
public class Server extends Application {
    
    //
    DataInputStream inputFromClient = null;
    DataOutputStream outputToClient = null;
    
    //
    BorderPane tfPane = new BorderPane();
    Label lbl = new Label("Press ENTER to Send");
    TextField tf = new TextField();
    BorderPane mainPane = new BorderPane();
//    VBox chatPane = new VBox();   
    TextArea ta = new TextArea();
    ScrollPane scroll = new ScrollPane(ta);
//    ScrollPane scroll = new ScrollPane(chatPane);
    Scene scene = new Scene(mainPane, 450, 300);
    
     @Override
    public void start(Stage primaryStage){
        
        tfPane.setPadding(new Insets(5,5,5,5));
        tfPane.setStyle("-fx-border-color: green");
        
        lbl.setAlignment(Pos.CENTER);
        tfPane.setBottom(lbl);
        BorderPane.setAlignment(lbl, Pos.CENTER);
        
//        chatPane.setSpacing(10);
//        chatPane.setPadding(new Insets(15,20, 10,10));
//        chatPane.setPrefHeight(150);
          
        tf.setAlignment(Pos.CENTER);
        tfPane.setCenter(tf);
        
        
        mainPane.setTop(scroll);
        mainPane.setCenter(tfPane);
        
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();
        tf.requestFocus();
        
        /**
         * Handler sends message on Enter
         */
        tf.setOnAction(e-> {
            try{
                
                // get message from textfield
                String msgOut = tf.getText().trim();
                
//                Label label = new Label();
//                label.setMaxWidth(150);
//                label.setWrapText(true);
//                label.setText(msgOut);
//                label.setStyle("-fx-border-color: black;");
//                chatPane.getChildren().add(label);
                
                // print to server text area
                ta.appendText("\nServer: " + msgOut + "\n");
                
                // send to server
                outputToClient.writeUTF(msgOut);
                outputToClient.flush();
                
                // clear text field
                Platform.runLater(()->{
                    tf.setText("");
                    });
                
            }
            catch(IOException ex){
                System.err.println(ex);
            }
        });
        
        /**
         * Creates Thread and input/output streams for sending data
         */
        new Thread(()-> {
            try{
                // create server socket
                ServerSocket serverSocket = new ServerSocket(5000);
                
                //this is the difference4
                Platform.runLater(()->{
                    ta.appendText("Chat started " + new Date() + "\n\n");
                            });
                
                // listen for conn request
                Socket socket = serverSocket.accept();
                
                // create data input / output streams
                inputFromClient = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                outputToClient = new DataOutputStream(socket.getOutputStream());
                
                while(true){
                    
                    //
                    String msgIn = inputFromClient.readUTF();
                 
                    //
                    ta.appendText("\n\t\t\t\tClient: " + msgIn + "\n");                    
                   
                    // 
                    Platform.runLater(()->{ 
                        
//                        HBox h = new HBox();
//                        h.setPrefWidth(450);
//                        Label label = new Label();
//                        label.setMaxWidth(150);
//                        label.setWrapText(true);
//                        label.setAlignment(Pos.CENTER);
//                        label.setText(msgIn);
//                        label.setStyle("-fx-border-color: black;");
//                        h.getChildren().add(label);
//                        h.setAlignment(Pos.CENTER_RIGHT);
//                        chatPane.getChildren().add(h);
          
                    });
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }).start();
    }
    
    /**
     * Main
     * @param args 
     */
    public static void main(String args[]){
        launch(args);
    }
}
