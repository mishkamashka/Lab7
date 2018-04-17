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
    private static ServerSocket serverSocket;
    protected static SortedSet<Person> collec = Collections.synchronizedSortedSet(new TreeSet<Person>());

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(4718,1, InetAddress.getByName("localhost"));
            System.out.println(serverSocket.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server is now running.");
        try {
            while (true) {
                Socket client = serverSocket.accept();
                Connection connec = new Connection(client);
            }
        } catch (Exception e) {
            System.out.println("Server is not listening.");
            e.printStackTrace();
        }
    }
}

class Connection extends Thread {
    private Socket client;
    private BufferedReader fromClient;
    private PrintStream toClient;
    private final static String filename = System.getenv("FILENAME");
    private final static String currentdir = System.getProperty("user.dir");
    private static String filepath;
    private static File file;
    private ReentrantLock locker = new ReentrantLock();

    Connection(Socket client){
        Connection.filemaker();
        this.client = client;
        try {
            fromClient = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            toClient = new PrintStream(client.getOutputStream());
        } catch (IOException e){
            try{
                client.close();
            }catch (IOException ee){
                ee.printStackTrace();
            }
            e.printStackTrace();
        }
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
        System.out.println("Client " + client.toString() + " has connected to server.");
        toClient.println("You've connected to the server.\n");
        while(true) {
            try {
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
                            toClient.println();
                            break;
                        case "save_file":
                            this.save();
                            break;
                        default:
                            toClient.println("Not valid command. Try one of those:\nhelp - get help;\nclear - clear the collection;" +
                                    "\nload - load the collection again;\nadd {element} - add new element to collection;" +
                                    "\nremove_greater {element} - remove elements greater than given;\n" +
                                    "show - show the collection;\nquit - quit;\n");
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
                toClient.println("File is empty.");
            }
        } catch (FileNotFoundException e) {
            toClient.println("Collection can not be loaded.\nFile "+filename+" is not accessible: it does not exist or permission denied.");
            e.printStackTrace();
        }
        locker.unlock();
    }

    private void getCollection(){
        locker.lock();
        final ObjectInputStream fromClient;
        try{
            fromClient = new ObjectInputStream(client.getInputStream());
        } catch (IOException e){
            System.out.println("Can not create ObjectInputStream: "+e.toString());
            System.out.println("Just try again, that's pretty normal.");
            toClient.println("Can not create ObjectInputStream on server side: "+e.toString());
            toClient.println("Just try again, that's pretty normal.");
            return;
        }
        Person person;
        try{
            while ((person = (Person)fromClient.readObject()) != null){
                Server.collec.add(person);
            }
        } catch (IOException e) {
            System.out.println("Collection has been updated by client.");
            // выход из цикла через исключение(да, я в курсе, что это нехоршо наверное, хз как по-другому)
            //e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.out.println("Class not found while deserializing.");
        }
        locker.unlock();
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
            toClient.println("Collection has been saved to file.\n");
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
            this.toClient.println(" Collection copy has been loaded on client.\n");
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