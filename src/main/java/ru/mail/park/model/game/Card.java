package ru.mail.park.model.game;

//больше для лога
public class Card {
    final String name;
    final int inQueue;
    final String suit;

    public Card(int inQueue, int suit) {
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

    static public int stringToPar(String card){
        switch (card.charAt(0)){
            case 'T':
                return 0;
            case 'A':
                return 1;
            case 'X':
                return 10;
            case 'J':
                return 11;
            case 'Q':
                return 12;
            case 'K':
                return 13;
            default:
                return (int)card.charAt(0);
        }
    }

    static public int stringToSuit(String card){
        return (int) card.charAt(1);
    }
}
