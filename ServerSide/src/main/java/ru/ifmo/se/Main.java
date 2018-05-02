package ru.ifmo.se;

public class Main {
    public static void main(String[] args) {
        Server a = new Server();
        new Thread(a).start();
        MainPanel mainPanel = new MainPanel();
    }
}
