package ru.ifmo.se;

import java.io.*;
import java.net.*;

public class ServerEx {
    private static int sizeOfPacket = 100;

    public void start(){
        try {
            DatagramSocket socket = new DatagramSocket(4718, InetAddress.getByName("localhost"));
            System.out.println("____________________________\n" +
                    "Welcome, dear %username%!\n" +
                    "\tThe server is ready...\n" +
                    "____________________________\n");
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[sizeOfPacket], sizeOfPacket);
                socket.receive(packet);
                Conection conection = new Conection(socket, packet);
            }
        } catch (UnknownHostException | SocketException e) {
            System.out.println("Error: can't create datagramSocket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: can't send datagramPacket: " + e.getMessage());
        }
    }
}

class Conection extends Thread {
    DatagramPacket packet;
    DatagramSocket socket;
    ByteArrayInputStream byteArrayInputStream;
    BufferedReader bufferedReader;
    ByteArrayOutputStream byteArrayOutputStream;
    BufferedWriter bufferedWriter;

    Conection (DatagramSocket socket, DatagramPacket packet){
        this.socket = socket;
        this.packet = packet;
        this.start();
    }

    @Override
    public void run() {

        byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        System.out.println(bufferedReader.lines());
        byteArrayOutputStream = new ByteArrayOutputStream();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
        try{
            bufferedWriter.write("Received at server");
            bufferedWriter.close();
            packet.setData(byteArrayOutputStream.toByteArray());
            packet.setLength(byteArrayOutputStream.size());
            packet.setPort(packet.getPort());
            socket.send(packet);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}