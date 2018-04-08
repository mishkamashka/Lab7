package ru.ifmo.se;

public class Main {
    public static void main(String[] args) {
        //ServerEx a = new ServerEx();
        Server a = new Server();
        //Runtime.getRuntime().addShutdownHook(new Thread(Connection::saveOnQuit));
        a.start();
    }
}
