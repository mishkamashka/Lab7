package ru.ifmo.se;

import com.google.gson.JsonSyntaxException;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

public class ClientApp {
    //Клиентский модуль должен запрашивать у сервера текущее состояние коллекции,
    //генерировать сюжет, выводить его на консоль и завершать работу.
    Set<Person> collec = new TreeSet<>();
    private static SocketAddress clientSocket;
    private static SocketChannel channel = null;
    private static DataInput fromServer;
    static PrintStream toServer;
    private Scanner sc;
    private ReentrantLock locker = new ReentrantLock();

    public void main() {
        this.connect();
        toServer.println("data_request");
        this.clear();
        this.load();
        this.gettingResponse();
        sc = new Scanner(System.in);
        String command;
        String input;
        String[] buf;
        String data = "";
        while (true) {
            input = sc.nextLine();
            buf = input.split(" ");
            command = buf[0];
            if (buf.length > 1)
                data = buf[1];
            switch (command) {
                case "load":
                    toServer.println("data_request");
                    this.clear();
                    this.load();
                    this.gettingResponse();
                    break;
                case "show":
                    this.show();
                    break;
                case "describe":
                    this.describe();
                    break;
                case "add":
                    this.addObject(data);
                    break;
                case "remove_greater":
                    this.removeGreater(data);
                    break;
                case "clear":
                    this.clear();
                    break;
                case "help":
                    this.help();
                    break;
                case "save":
                    toServer.println(command);
                    this.giveCollection();
                    this.gettingResponse();
                    break;
                case "qw":
                    toServer.println(command);
                    this.giveCollection();
                    this.gettingResponse();
                    this.quit();
                    break;
                case "q":
                    toServer.println(command);
                    this.quit();
                    break;
                default:
                    try{
                        toServer.println(command);
                        this.gettingResponse();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
            }
        }
    }

    public void connect(){
        locker.lock();
        try {
            clientSocket = new InetSocketAddress(InetAddress.getByName("localhost"), 4718);
            channel = SocketChannel.open(clientSocket);
        } catch (IOException e){
            //e.printStackTrace();
        }
        int i = 0;
        while (channel == null) {
            try {
                Thread.sleep(1000);
                channel = SocketChannel.open(clientSocket);
            } catch (IOException e) {
                if (i++ == 3){
                    System.out.println("Server is not responding for a long time...");
                }
                if (i == 10){
                    System.out.println("Server did not respond for too long. Try again later.");
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            fromServer = new DataInputStream(channel.socket().getInputStream());
            toServer = new PrintStream(new DataOutputStream(channel.socket().getOutputStream()));
        } catch (IOException e){
            System.out.println("Can not create DataInput or DataOutput stream.");
            e.printStackTrace();
        }
        locker.unlock();
        System.out.println(this.gettingResponse());
    }

    public void load(){
        final ObjectInputStream fromServer;
        try{
            DataInputStream dataInputStream = new DataInputStream(channel.socket().getInputStream());
            fromServer = new ObjectInputStream(channel.socket().getInputStream());
        } catch (IOException e){
            System.out.println("Can not create ObjectInputStream: "+e.toString());
            System.out.println("Just try again, that's pretty normal.");
            return;
        }
        Person person;
        try{
            while ((person = (Person)fromServer.readObject()) != null){
                this.collec.add(person);
            }
        } catch (IOException e) {
            System.out.println("Collection has been loaded.");
            // выход из цикла через исключение(да, я в курсе, что это нехоршо наверное, хз как по-другому)
            //e.printStackTrace();  StreamCorruptedException: invalid type code: 20
        } catch (ClassNotFoundException e){
            System.out.println("Class not found while deserializing.");
        }
    }

    public void giveCollection(){
        ObjectOutputStream toServer;
        OutputStream outputStream;
        try {
            outputStream = channel.socket().getOutputStream();
            toServer = new ObjectOutputStream(outputStream);
        } catch (IOException e){
            System.out.println("Can not create ObjectOutputStream.");
            return;
        }
        try {
            //Server.collec.forEach(person -> toClient.writeObject(person));
            for (Person person: this.collec){
                toServer.writeObject(person);
            }
            outputStream.write(64);
            System.out.println("Collection has been sent to server.");
        } catch (IOException e){
            System.out.println("Can not write collection into stream.");
        }
    }

    private void show() {
        if (this.collec.isEmpty())
            System.out.println("Collection is empty.");
        this.collec.forEach(person -> System.out.println(person.toString()));
        System.out.println();
    }

    private void describe(){
        this.collec.forEach(person -> person.describe());
        System.out.println();
    }

    private void quit(){
        sc.close();
        toServer.close();
        try {
            channel.close();
        } catch (IOException e){
            System.out.println("Can not close channel.");
            e.printStackTrace();
        }
        System.exit(0);
    }

    public String gettingResponse(){
        StringBuilder temp = new StringBuilder();
        try{
            Scanner sc = new Scanner(fromServer.readLine());
            sc.useDelimiter("\n");
            while (sc.hasNext()) {
                temp.append(sc.next());
                sc = new Scanner(fromServer.readLine());
            }
            System.out.println(temp.toString() + "\nEnd of getting from server.");
            return temp.toString();
        } catch (IOException e){
            temp.append("The connection was lost.\nTrying to reconnect...");
            this.connect();
            return temp.toString();
        }
    }

    public String removeGreater(String data) {
        Person a = JsonConverter.jsonToObject(data, Known.class);
        this.collec.removeIf(person -> a.compareTo(person) > 0);
        return ("Objects greater than given have been removed.");
    }

    public String addObject(String data) {
        try {
            if ((JsonConverter.jsonToObject(data, Known.class).getName() != null)) {
                if (this.collec.add(JsonConverter.jsonToObject(data, Known.class))) {
                    System.out.println("Current collection has been updated by server.");
                    return ("Object " + JsonConverter.jsonToObject(data, Known.class).toString() + " has been added.");
                } else{
                    return ("This object is already in the collection.");
                }
            } else {
                return ("Object null can not be added.");
            }
        } catch (NullPointerException | JsonSyntaxException e) {
            return ("Something went wrong. Check your object and try again. For example of json format see \"help\" command.");
        }
    }

    public void clear() {
        if (collec.isEmpty())
            System.out.println("There is nothing to remove, collection is empty.");
        else {
            collec.clear();
            System.out.println("Collection has been cleared.");
        }
    }

    private void help(){
        System.out.println("Commands:\nclear - clear the collection;\nload - load the collection again;" +
                "\nshow - show the collection;\ndescribe - show the collection with descriptions;" +
                "\nadd {element} - add new element to collection;\nremove_greater {element} - remove elements greater than given;" +
                "\nsave - save changes on server;\nq - quit without saving;\nqw - save on server and quit;\nhelp - get help;\n" +
                "save_file - save current server collection to file;\nload_file - load collection on server from file.");
        System.out.println("\nPattern for object Person input:\n{\"name\":\"Andy\",\"steps_from_door\":0}");
        System.out.println("\nHow objects are compared:\nObject A is greater than B if it stands further from the door B does. (That's weird but that's the task.)\n");
        System.out.println("Collection is saved to file when server shuts down or \"save_file\" command.");
    }
}
