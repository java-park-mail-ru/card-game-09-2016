package ru.mail.park.model.game;

//больше для лога
public class Card {
    final String name;
    final int inQueue;
    final String suit;

    Card(int inQueue, int suit) {
        this.inQueue = inQueue;
        if (inQueue > 0) {
            switch (inQueue) {
                case 1:
                    this.name = "A";
                    break;
                case 10:
                    this.name = "X";
                    break;
                case 11:
                    this.name = "J";
                    break;
                case 12:
                    this.name = "Q";
                    break;
                case 13:
                    this.name = "K";
                    break;
                default:
                    this.name = String.valueOf(inQueue);
            }
                    this.suit = String.valueOf(suit);
        }else{
            this.name = "T";    //от слова Trump
            this.suit = "T";
        }

    }
    @Override
    public String toString(){
        return name + suit;
    }
}
