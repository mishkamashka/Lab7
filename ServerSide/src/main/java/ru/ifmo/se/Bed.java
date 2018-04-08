package ru.ifmo.se;

import ru.ifmo.se.exceptions.NotEnoughMoneyException;
import ru.ifmo.se.exceptions.NothingToRemoveException;
import ru.ifmo.se.exceptions.TooMuchMoneyException;

public class Bed {
    private boolean isReady = false;
    final double price = 1;

    public void checkMoney(double paid, double price) throws NotEnoughMoneyException, TooMuchMoneyException {
        if (paid < this.price)
            throw new NotEnoughMoneyException();
        if (paid > this.price)
            throw new TooMuchMoneyException();
    }

    class Pillow {
        final double price = 2;

        public void shakeUp(Person a) {
            System.out.println("Pillow is shaken up by " + a.toString());
        }

        public void putAway(Person a) {
            System.out.println(new GettingBedding() {
                @Override
                public String takeSomething() {
                    return "Pillow is put away by " + a.toString() + ".";
                }
            }.takeSomething());
        }
    }

    class Blanket {
        final double price = 1;

        public void sleepUnder(Person a) {
            System.out.println(a.toString() + " is sleeping unger the Blanket.");
        }

        public void putAway(Person a) {
            System.out.println(new GettingBedding() {
                @Override
                public String takeSomething() {
                    return "Blanket is put away by " + a.toString() + ".";
                }
            }.takeSomething());
        }
    }

    class Bedsheet {
        final double price = 1;

        public void crumple(Person a) {
            System.out.println("Bedsheet is crumpled by " + a.toString() + ".");
        }

        public void turnIntoAGhost(Person a) {
            System.out.println(a.toString() + " has made a ghost out of the bedsheet.");
        }

        public void putAway(Person a) {
            System.out.println(new GettingBedding() {
                @Override
                public String takeSomething() {
                    return "Bedsheet is put away by " + a.toString() + ".";
                }
            }.takeSomething());
        }
    }

    public Pillow getPillow(double paid) {
        System.out.println("Pillow is requested, paid in amount of santics: " + paid);
        Pillow pillow = new Pillow();
        System.out.print("The wall says: ");
        if (!(this.isReady))
            return pillow;
        try {
            checkMoney(paid, pillow.price);
            System.out.println(new GettingBedding() {
                @Override
                public String giveSomething() {
                    return "Pillow is given.";
                }
            }.giveSomething());
        } catch (NotEnoughMoneyException e) {
            System.out.println("Not enough money for a pillow.");
            System.out.println(this.price - paid + " more needed.");
        } catch (TooMuchMoneyException e) {
            System.out.println("Take the pillow and change.");
        } finally {
            return pillow;
        }
    }

    public Blanket getBlanket(double paid) {
        System.out.println("Blanket is requested, paid in amount of santics: " + paid);
        Blanket blanket = new Blanket();
        System.out.print("The wall says: ");
        if (!(this.isReady))
            return blanket;
        try {
            checkMoney(paid, blanket.price);
            System.out.println(new GettingBedding() {
                @Override
                public String giveSomething() {
                    return "Blanket is given.";
                }
            }.giveSomething());
        } catch (NotEnoughMoneyException e) {
            System.out.println("Not enough money for a blanket.");
            System.out.println(this.price - paid + " more needed.");
        } catch (TooMuchMoneyException e) {
            System.out.println("Take the blanket and change.");
        } finally {
            return blanket;
        }
    }

    public Bedsheet getBedsheet(double paid) {
        System.out.println("Bedsheet is requested, paid in amount of santics: " + paid);
        Bedsheet bedsheet = new Bedsheet();
        System.out.print("The wall says: ");
        if (!(this.isReady))
            return bedsheet;
        try {
            checkMoney(paid, bedsheet.price);
            System.out.println(new GettingBedding() {
                @Override
                public String giveSomething() {
                    return "Bedsheet is given.";
                }
            }.giveSomething());
        } catch (NotEnoughMoneyException e) {
            System.out.println("Not enough money for a bedsheet.");
            System.out.println(this.price - paid + " more needed.");
        } catch (TooMuchMoneyException e) {
            System.out.println("Take the bedsheet and change.");
        } finally {
            return bedsheet;
        }
    }

    public boolean isReady() {
        return isReady;
    }

    public void makeReady() {
        isReady = true;
    }

    public void makeUnready() {
        if (this.isReady())
            isReady = false;
        else
            throw new NothingToRemoveException();
    }
}