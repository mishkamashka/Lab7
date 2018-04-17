package ru.ifmo.se;

public class Main {
    public static void main(String[] args) {
        Server a = new Server();
        Runtime.getRuntime().addShutdownHook(new Thread(Connection::saveOnQuit));
        a.start();
    }
}
