package ru.ifmo.se;

import java.util.ArrayList;

public interface InteractionWithGroup {
    ArrayList<Person> getList();

    void addPerson(Person p);

    void removePerson(Person p);

    boolean isAnyone();

    boolean isNotBored();

    void setStateForEach(double chance);

}
