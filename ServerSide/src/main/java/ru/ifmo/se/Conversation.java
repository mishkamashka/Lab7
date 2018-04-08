package ru.ifmo.se;

import ru.ifmo.se.enums.State;

import java.util.ArrayList;

public class Conversation implements TypeOfSpeech {
    private InteractionWithGroup people = new Group();

    public void sayHello(Person a, Person b) {
        if (b instanceof Unknown)
            System.out.println(Message.saying(a) + " Hello!");
        else
            System.out.println(Message.saying(a) + " Hello, " + b.toString() + "!");
    }

    public void sayBye(Person a, Person b) {
        if (b instanceof Unknown)
            System.out.println(Message.saying(a) + " Goodbye!");
        else
            System.out.println(Message.saying(a) + " Bye, " + b.toString() + "!");
    }

    public void tellStory(Person speaker) {
        if (this.people.isNotBored())
            System.out.println(Message.saying(speaker) + "I'm gonna tell you a story.\n" + Message.tellingStory(speaker));
        this.people.setStateForEach(Math.random());
    }

    public void tellJoke(Person speaker) {
        System.out.println(Message.saying(speaker) + "I'm gonna tell you a joke.\n" + Message.tellingJoke(speaker));
        this.people.setStateForEach(Math.random() + 0.1);
    }

    public void argue(Person a, Person b) {
        if (a.getState() == State.ANGRY && b.getState() == State.ANGRY) {
            System.out.println(Message.arguing(a, b));
        }
        if (a.getState() != State.ANGRY && b.getState() == State.ANGRY)
            System.out.println(Message.saying(b) + " Let's argue.\n"
                    + Message.saying(a) + " I don't wanna argue.");
        if (b.getState() != State.ANGRY && a.getState() == State.ANGRY)
            System.out.println(Message.saying(a) + " Let's argue.\n"
                    + Message.saying(b) + " I don't wanna argue.");
        a.setState(State.NEUTRAL);
        b.setState(State.NEUTRAL);
    }

    public void makeDo(Person a, Person b, String verb) {
        System.out.println(Message.makingDo(a, b) + verb + ".");
        if (Math.random() < 0.5)
            b.setState(State.ANGRY);
    }

    public void discuss(Person a, Person b, String thing) {
        System.out.println(Message.discussing(a, b) + thing + ".");
        this.people.setStateForEach(Math.random());
    }

    public void talk(Person a, Person b) {
        System.out.println(Message.talking(a, b));
        this.people.setStateForEach(Math.random());
    }

    public void sayPhrase(Person p, String s) {
        System.out.println(Message.saying(p) + s);
    }

    public void ask(Person asking, Person asked) {
        System.out.println(Message.asking(asking, asked));
        asking.setState(State.WAITING_FOR_ANSWER);
        this.answer(asked, asking);
    }

    public void answer(Person asked, Person asking) {
        System.out.println(Message.answering(asked));
        asking.setState(State.NEUTRAL);
    }

    public void addPerson(Person p) {
        if (p == null)
            return;
        this.people.addPerson(p);
        p.setState(State.NEUTRAL);
        ArrayList<Person> group = this.people.getList();
        if (!(group.size() - 1 == 0)) {
            for (Person g : group) {
                if (g.hashCode() != p.hashCode())
                    this.sayHello(g, p);
            }
        }
    }

    public void removePerson(Person p) {
        if (p == null)
            return;
        this.people.removePerson(p);
        p.setState(State.ABSENT);
        ArrayList<Person> group = this.people.getList();
        if (!group.isEmpty()) {
            for (Person g : group) {
                this.sayBye(g, p);
            }
        }
    }

    public void showParticipants() {
        ArrayList<Person> a = this.people.getList();
        System.out.println("At the moment taking part in conversation:");
        a.forEach(System.out::println);
    }

    public void countPeopleWithState(State state) {
        ArrayList<Person> a = this.people.getList();
        System.out.print("Amount of people who are " + state + ": ");
        System.out.println(a.stream().filter((person) -> person.getState().equals(state)).count());
    }

}

//a.forEach((person) -> System.out.println(person.toString()));