package ru.ifmo.se;

import com.google.gson.JsonSyntaxException;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class ClientApp {
    //Клиентский модуль должен запрашивать у сервера текущее состояние коллекции,
    //генерировать сюжет, выводить его на консоль и завершать работу.
    Set<Person> collec = new TreeSet<>();
    private InetSocketAddress clientSocketAddress;
    private DatagramChannel channel = null;
    private DatagramSocket socket;
    private DatagramPacket packetFromServer;
    private DatagramPacket packetToServer;
    private BufferedReader fromServer;
    private static final int sizeOfPacket = 100;
    private Scanner sc;

    public void main() {
        try {
            channel = DatagramChannel.open();
            clientSocketAddress = new InetSocketAddress(InetAddress.getByName("localhost"), 4718);
            //channel = DatagramChannel.open();
            socket = channel.socket();
            packetToServer = new DatagramPacket(new byte[65507], 65507, clientSocketAddress);
            //socket = new DatagramSocket();
            this.sendPacket("whatever, just work bitch");
            this.gettingResponse();


        } catch (IOException e) {
            e.printStackTrace();
        }
        packetFromServer = new DatagramPacket(new byte[65507], 65507);

        /*try {
            socket.receive(packetFromServer);
            System.out.println(new ByteArrayInputStream(packetFromServer.getData()));
        } catch (IOException e) {
            System.out.println("sdfghj");
        }*/
        this.sendPacket("data_request");
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
                    this.sendPacket("data_request");
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
                    this.sendPacket(command);
                    this.giveCollection();
                    //this.gettingResponse();
                    break;
                case "qw":
                    this.sendPacket(command);
                    this.giveCollection();
                    this.gettingResponse();
                    this.quit();
                    break;
                case "q":
                    this.sendPacket(command);
                    this.quit();
                    break;
                default:
                    this.sendPacket(command);
                    this.gettingResponse();
            }
        }
    }

    private void sendPacket(String string){
        try {
            ByteArrayOutputStream toServer = new ByteArrayOutputStream();
            toServer.write(string.getBytes());
            toServer.close();
            socket.send(packetToServer);
        } catch (IOException e){
            System.out.println("Can not create packet.");
        }
    }

    /*private DatagramPacket createPacket(String string){
        try {
            toClient.write(string.getBytes());
            packet.setData(toClient.toByteArray());
            packet.setLength(toClient.size());
            packet.setPort(packet.getPort());
        } catch (IOException e){
            System.out.println("Can not create packet.");
        }
        return packet;
    }*/

    /*private void connect(){
        try {
            //channel = DatagramChannel.open();
            socket = new DatagramSocket();
            //clientSocketAddress = new InetSocketAddress(4718);
            //channel.bind(clientSocketAddress);
            //socket = channel.socket();

        } catch (IOException e){
            e.printStackTrace();
        }
        int i = 0;
        while (channel == null) {
            try {
                Thread.sleep(1000);
                channel = DatagramChannel.open();
                clientSocketAddress = new InetSocketAddress(4718);
                DatagramSocket socket = channel.socket();
                socket.bind(clientSocketAddress);
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
            packet = new DatagramPacket(new byte[sizeOfPacket], sizeOfPacket);
            channel.socket().receive(packet);
            fromServer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(packet.getData())));
        } catch (IOException e){
            System.out.println("Can not create DataInput or DataOutput stream.");
            e.printStackTrace();
        }
        this.gettingResponse();
    }*/

    private void load(){
        final ObjectInputStream fromServer;
        try{
            fromServer = new ObjectInputStream(new ByteArrayInputStream(packetFromServer.getData()));
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
            // выход из цикла через исключение(да, я в курсе, что это нехоршо наверное, хз как по-другому)
            //e.printStackTrace();  StreamCorruptedException: invalid type code: 20
        } catch (ClassNotFoundException e){
            System.out.println("Class not found while deserializing.");
        }
    }

    private void giveCollection(){
        ObjectOutputStream toServer;
        try {
            toServer = new ObjectOutputStream(new ByteArrayOutputStream());
        } catch (IOException e){
            System.out.println("Can not create ObjectOutputStream.");
            return;
        }
        try {
            //Server.collec.forEach(person -> toClient.writeObject(person));
            for (Person person: this.collec){
                toServer.writeObject(person);
            }
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
        try {
            channel.close();
            fromServer.close();
        } catch (IOException e){
            System.out.println("Can not close channel.");
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void gettingResponse(){
        DatagramPacket fromServer = new DatagramPacket(new byte[65507], 65507);
        ByteArrayInputStream byteArrayInputStream;
        try{
            socket.receive(fromServer);
            byteArrayInputStream = new ByteArrayInputStream(fromServer.getData());
            Scanner sc = new Scanner(new InputStreamReader(byteArrayInputStream));
            sc.useDelimiter("\n");
            while (sc.hasNext()) {
                System.out.println(sc.next());
            }
            System.out.println("End of getting from server.");
        } catch (IOException e){
            System.out.println("The connection was lost.");
            System.out.println("Trying to reconnect...");
            //this.connect();
        }
    }

    private void removeGreater(String data) {
        Person a = JsonConverter.jsonToObject(data, Known.class);
        System.out.println(a.toString());
        this.collec.removeIf(person -> a.compareTo(person) > 0);
        System.out.println("Objects greater than given have been removed.\n");
    }

    private void addObject(String data) {
        try {
            if ((JsonConverter.jsonToObject(data, Known.class).getName() != null)) {
                this.collec.add(JsonConverter.jsonToObject(data, Known.class));
                System.out.println("Object " + JsonConverter.jsonToObject(data, Known.class).toString() + " has been added.\n");
            }
            else System.out.println("Object null can not be added.");
        } catch (NullPointerException | JsonSyntaxException e) {
            System.out.println("Something went wrong. Check your object and try again. For example of json format see \"help\" command.\n");
            System.out.println(e.toString());
        }
    }

    private void clear() {
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
