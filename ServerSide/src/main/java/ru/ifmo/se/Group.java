package ru.ifmo.se;

import ru.ifmo.se.enums.State;

import java.util.ArrayList;

public class Group implements InteractionWithGroup {
    private ArrayList<Person> people = new ArrayList<>();

    public ArrayList<Person> getList() {
        ArrayList<Person> a = new ArrayList<>(this.people);
        return a;
    }

    public void addPerson(Person p) {
        System.out.println(p.toString() + " enters.");
        p.describe();
        this.people.add(p);
    }

    public void removePerson(Person p) {
        System.out.println(p.toString() + " leaves.");
        this.people.remove(p);
    }

    public boolean isAnyone() {
        return !this.people.isEmpty();
    }

    public boolean isNotBored() {
        boolean f = false;
        int i = 0;
        while (i < this.people.size() && !f) {
            Person a = this.people.get(i);
            f = a.getState() != State.BORED;
        }
        return f;
    }

    public void setStateForEach(double chance) {
        for (Person g : this.people) {
            double chanceC = chance + Math.random();
            if (chanceC < 0.5) {
                g.setState(State.BORED);
                System.out.println(g.toString() + " is bored.");
            } else if (chanceC > 0.5 && chanceC < 1) {
                g.setState(State.ANGRY);
                System.out.println(g.toString() + " is angry.");
            } else if (chanceC > 1 && chanceC < 1.5) {
                g.setState(State.INTERESTED);
                System.out.println(g.toString() + " is interested.");
            } else
                System.out.println(g.toString() + " doesn't care.");
        }
    }
}