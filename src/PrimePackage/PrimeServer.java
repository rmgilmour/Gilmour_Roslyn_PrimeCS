package PrimePackage;

import java.io.*;
import java.net.*;
import java.util.Date;

import com.sun.security.ntlm.NTLMException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class PrimeServer extends Application {

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(7000);
                Platform.runLater(() ->
                        ta.appendText("Server started at " + new Date() + '\n'));

                // Listen for a connection request
                Socket socket = serverSocket.accept();

                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                while (true) {
                    // Receive number from the client
                    int number = inputFromClient.readInt();
                    boolean flag = true;
                    if (number <=1)
                        flag = false;
                    if (number == 2 || number == 3)
                        flag = true;
                    if (number % 2 == 0 || number % 3 == 0)
                        flag = false;

                    for (int i=5; i < Math.sqrt(number); i+=6)
                        if (number % i == 0 || number % (i+2) == 0)
                        flag = false;
                    if (flag)
                        outputToClient.writeInt(1);
                    else
                        outputToClient.writeInt(0);




                    Platform.runLater(() -> {
                        ta.appendText("Number received from client: "
                                + number + '\n');

                    });
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {

        launch(args);

    }
}
