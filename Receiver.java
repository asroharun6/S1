import javax.swing.*;
import java.awt.BorderLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends JFrame {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream dis;
    private JTextArea logArea;

    public Receiver() {
        createWindow();
        setupNetworking();
    }

    private void createWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void setupNetworking() {
        try {
            serverSocket = new ServerSocket(5000);
            logArea.append("Listening on port 5000...\n");

            while (true) {
                clientSocket = serverSocket.accept();
                dis = new DataInputStream(clientSocket.getInputStream());

                receiveFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile() {
        try {
            String fileName = dis.readUTF();
            File file = new File("received_files/" + fileName);
            file.getParentFile().mkdirs(); // Create directories if they don't exist
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];

            int read;
            while ((read = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }

            fos.close();
            logArea.append("Received file: " + fileName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Receiver();
    }
}
