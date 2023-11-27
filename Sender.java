import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Sender extends JFrame {
    private JTextField ipField;
    private JTextField portField;
    private JButton chooseFileButton;
    private JButton sendButton;
    private File selectedFile;
    private Socket socket;
    private DataOutputStream dos;

    public Sender() {
        createWindow();
    }

    private void createWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new GridLayout(4, 2));

        chooseFileButton = new JButton("Choose File");
        ipField = new JTextField();
        portField = new JTextField("5000");
        sendButton = new JButton("Send File");

        add(chooseFileButton);
        add(new JLabel(""));  // Placeholder for layout
        add(new JLabel("Receiver IP:"));
        add(ipField);
        add(new JLabel("Port:"));
        add(portField);
        add(sendButton);

        chooseFileButton.addActionListener(e -> chooseFile());
        sendButton.addActionListener(e -> sendFile());

        setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            chooseFileButton.setText("Selected: " + selectedFile.getName());
        }
    }

    private void sendFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please choose a file first.");
            return;
        }

        try {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());

            socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());

            FileInputStream fis = new FileInputStream(selectedFile);
            byte[] buffer = new byte[4096];

            dos.writeUTF(selectedFile.getName());

            int read;
            while ((read = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, read);
            }

            fis.close();
            dos.close();
            socket.close();

            JOptionPane.showMessageDialog(this, "File sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending file.");
        }
    }

    public static void main(String[] args) {
        new Sender();
    }
}
