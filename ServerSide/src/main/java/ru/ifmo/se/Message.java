package ru.ifmo.se;

public class Message {
    static String saying(Person p) {
        return p.toString() + " says: ";
    }

    static String asking(Person a, Person b) {
        return a.toString() + " asks " + b.toString() + " about something.";
    }

    static String answering(Person p) {
        return p.toString() + " answers.";
    }

    static String tellingStory(Person p) {
        return p.toString() + " is telling a story.";
    }

    static String tellingJoke(Person p) {
        return p.toString() + " is telling a joke.";
    }

    static String talking(Person a, Person b) {
        return a.toString() + " and " + b.toString() + " are talking.";
    }

    static String arguing(Person a, Person b) {
        return a.toString() + " and " + b.toString() + " are arguing.";
    }

    static String makingDo(Person a, Person b) {
        return a.toString() + " wants " + b.toString() + " to ";
    }

    static String discussing(Person a, Person b) {
        return a.toString() + " and " + b.toString() + " are discussing ";
    }
}
