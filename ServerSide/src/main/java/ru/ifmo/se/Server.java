package ru.ifmo.se;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {
    //Серверный модуль должен реализовывать все функции управления коллекцией
    //в интерактивном режиме, кроме отображения текста в соответствии с сюжетом предметной области.
    private static DatagramSocket serverSocket;
    private static DatagramPacket packetFromClient;
    private static BufferedReader fromClient;
    private static final int sizeOfPacket = 256;
    protected static SortedSet<Person> collec = Collections.synchronizedSortedSet(new TreeSet<Person>());

    @Override
    public void run() {
        try {
            serverSocket = new DatagramSocket(4718, InetAddress.getByName("localhost"));
            System.out.println(serverSocket.toString());
            System.out.println(serverSocket.getLocalPort());
            System.out.println("Server is now running.");
            while (true) {
                DatagramPacket fromClient = new DatagramPacket(new byte[sizeOfPacket], sizeOfPacket);
                serverSocket.receive(fromClient);
                Connection connec = new Connection(serverSocket, fromClient);
            }
        } catch (UnknownHostException | SocketException e){
            System.out.println("Server is not listening.");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Can not receive datagramPacket.");
            e.printStackTrace();
        }
    }
}

class Connection extends Thread {
    private DatagramSocket client;
    private DatagramPacket packet;
    private BufferedReader fromClient;
    private ByteArrayOutputStream toClient;
    private final static String filename = System.getenv("FILENAME");
    private final static String currentdir = System.getProperty("user.dir");
    private static String filepath;
    private static File file;
    private ReentrantLock locker = new ReentrantLock();

    Connection(DatagramSocket serverSocket, DatagramPacket packetFromClient){
        Connection.filemaker();
        this.packet = packetFromClient;
        this.client = serverSocket;
        fromClient = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.packet.getData())));
        toClient = new ByteArrayOutputStream();
        this.start();
    }

    private static void filemaker(){
        if (currentdir.startsWith("/")) {
            filepath = currentdir + "/" + filename;
        } else
            filepath = currentdir + "\\" + filename;
        file = new File(filepath);
    }

    public void run(){
        try {
            this.load();
        } catch (IOException e) {
            System.out.println("Exception while trying to load collection.\n" + e.toString());
        }
        System.out.println("Client " + packet.getSocketAddress()+ " " + packet.getPort()+ " has connected to server.");
        try {
            client.send(this.createPacket("You've connected to the server.\n"));
        } catch (IOException e){
            System.out.println("Can not send packet.");
        }
        DatagramPacket packetFromClient = new DatagramPacket(new byte[65507], 65507);
        while(true) {
            try {
                client.receive(packetFromClient);
                String command = fromClient.readLine();
                System.out.println("Command from client: " + command);
                try {
                    switch (command) {
                        case "data_request":
                            this.giveCollection();
                            break;
                        case "save":
                            this.clear();
                            this.getCollection();
                            break;
                        case "qw":
                            this.getCollection();
                        case "q":
                            this.quit();
                            break;
                        case "load_file":
                            this.load();
                            client.send(this.createPacket("\n"));
                            break;
                        case "save_file":
                            this.save();
                            break;
                        default:
                            client.send(this.createPacket("Not valid command. Try one of those:\nhelp - get help;\nclear - clear the collection;" +
                                    "\nload - load the collection again;\nadd {element} - add new element to collection;" +
                                    "\nremove_greater {element} - remove elements greater than given;\n" +
                                    "show - show the collection;\nquit - quit;\n"));
                    }
                }catch (NullPointerException e){
                    System.out.println("Null command received.");
                }
            } catch (IOException e) {
                System.out.println("Connection with the client is lost.");
                System.out.println(e.toString());
                try {
                    fromClient.close();
                    toClient.close();
                    client.close();
                } catch (IOException ee){
                    System.out.println("Exception while trying to close client's streams.");
                }
                return;
            }
        }
    }

    private DatagramPacket createPacket(String string){
        try {
            toClient.write(string.getBytes());
            packet.setData(toClient.toByteArray());
            packet.setLength(toClient.size());
            packet.setPort(packet.getPort());
        } catch (IOException e){
            System.out.println("Can not create packet.");
        }
        return packet;
    }

    private void load() throws IOException {
        locker.lock();
        try (Scanner sc = new Scanner(file)) {
            StringBuilder tempString = new StringBuilder();
            tempString.append('[');
            sc.useDelimiter("}\\{");
            while (sc.hasNext()) {
                tempString.append(sc.next());
                if (sc.hasNext())
                    tempString.append("},{");
            }
            sc.close();
            JSONArray jsonArray = new JSONArray(tempString.append(']').toString());
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jsonObjectAsString = jsonObject.toString();
                    Server.collec.add(JsonConverter.jsonToObject(jsonObjectAsString, Known.class));
                }
                System.out.println("Connection has been loaded.");
            } catch (NullPointerException e) {
                try {
                    client.send(this.createPacket("File is empty.\n"));
                } catch (IOException ee){
                    System.out.println("Can not send packet.");
                }
            }
        } catch (FileNotFoundException e) {
            try {
                client.send(this.createPacket("Collection can not be loaded.\nFile "+filename+" is not accessible: it does not exist or permission denied.\n"));
            } catch (IOException ee){
                System.out.println("Can not send packet.");
            }
            e.printStackTrace();
        }
        locker.unlock();
    }

    private void getCollection() throws IOException{
        locker.lock();
        final ObjectInputStream fromClient;
        try{
            fromClient = new ObjectInputStream(new ByteArrayInputStream(this.packet.getData()));
        } catch (IOException e){
            System.out.println("Can not create ObjectInputStream: "+e.toString());
            System.out.println("Just try again, that's pretty normal.");
            client.send(this.createPacket("Can not create ObjectInputStream on server side: "+e.toString()));
            client.send(this.createPacket("Just try again, that's pretty normal."));
            return;
        }
        Person person;
        try{
            while ((person = (Person)fromClient.readObject()) != null){
                Server.collec.add(person);
            }
            client.send(this.createPacket("Collection has been saved on server.\n"));
        } catch (IOException e) {
            client.send(this.createPacket("Collection has been saved on server.\n"));
            // выход из цикла через исключение(да, я в курсе, что это нехоршо наверное, хз как по-другому)
            //e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.out.println("Class not found while deserializing.");
        } finally {
            System.out.println("Collection has been updated by client.");
            locker.unlock();
        }
    }

    private void quit() throws IOException {
        fromClient.close();
        toClient.close();
        client.close();
        System.out.println("Client has disconnected.");
    }

    private void save(){
        locker.lock();
        try {
            Writer writer = new FileWriter(file);
            //
            //Server.collec.forEach(person -> writer.write(Connection.objectToJson(person)));
            for (Person person: Server.collec){
                writer.write(JsonConverter.objectToJson(person));
            }
            writer.close();
            System.out.println("Collection has been saved.");
            client.send(this.createPacket("Collection has been saved to file.\n"));
        } catch (IOException e) {
            System.out.println("Collection can not be saved.\nFile "+filename+" is not accessible: it does not exist or permission denied.");
            e.printStackTrace();
        }
        locker.unlock();
    }

    public static void saveOnQuit(){
        try {
            Writer writer = new FileWriter(file);
            //
            //Server.collec.forEach(person -> writer.write(Connection.objectToJson(person)));
            for (Person person: Server.collec){
                writer.write(JsonConverter.objectToJson(person));
            }
            writer.close();
            System.out.println("Collection has been saved.");
        } catch (IOException e) {
            System.out.println("Collection can not be saved.\nFile "+filename+" is not accessible: it does not exist or permission denied.");
            e.printStackTrace();
        }
    }

    private void giveCollection(){
        locker.lock();
        ObjectOutputStream toClient;
        try {
            toClient = new ObjectOutputStream(this.toClient);
        } catch (IOException e){
            System.out.println("Can not create ObjectOutputStream.");
            return;
        }
        try {
            //Server.collec.forEach(person -> toClient.writeObject(person));
            for (Person person: Server.collec){
                toClient.writeObject(person);
            }
            client.send(this.createPacket(" Collection copy has been loaded on client.\n"));
        } catch (IOException e){
            System.out.println("Can not write collection into stream.");
        }
        locker.unlock();
    }

    private void showCollection() {
        if (Server.collec.isEmpty())
            System.out.println("Collection is empty.");
        for (Person person : Server.collec) {
            System.out.println(person.toString());
        }
    }

    private void clear() {
        Server.collec.clear();
    }
}