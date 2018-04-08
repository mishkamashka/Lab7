package ru.ifmo.se;

import java.io.*;
import java.net.*;

public class Client {
    private DatagramSocket socket;
    private int sizeOfPackage = 100;

    public void main() {
        try {
            socket = new DatagramSocket();
            byte[] b = new byte[2];
            b[0]=5;
            DatagramPacket packet1 = new DatagramPacket(b, b.length, new InetSocketAddress(InetAddress.getByName("localhost"), 4718));
            socket.send(packet1);
            DatagramPacket packet = new DatagramPacket(new byte[sizeOfPackage], sizeOfPackage);
            socket.receive(packet);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));

            System.out.println("Received from " + packet.getAddress());
            System.out.println(bufferedReader.lines());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
            try {
                bufferedWriter.write("Whatever");
                bufferedWriter.close();
                byte[] bytes = byteArrayOutputStream.toByteArray();

                packet = new DatagramPacket(bytes, bytes.length);
                packet.setAddress(socket.getInetAddress());
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

