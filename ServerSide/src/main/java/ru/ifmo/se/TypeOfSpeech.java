package ru.ifmo.se;

public interface TypeOfSpeech {
    void sayHello(Person a, Person b);

    void sayBye(Person a, Person b);

    void tellStory(Person speaker);

    void tellJoke(Person speaker);

    void argue(Person a, Person b);

    void makeDo(Person a, Person b, String thing);

    void discuss(Person a, Person b, String thing);

    void talk(Person a, Person b);

    void sayPhrase(Person p, String s);

    void ask(Person asking, Person asked);

    void answer(Person asked, Person asking);
}