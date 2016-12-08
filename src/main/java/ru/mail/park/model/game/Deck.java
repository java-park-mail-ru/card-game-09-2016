package ru.mail.park.model.game;

import java.util.*;

class Deck {
    private final Stack<String> cardStack = new Stack<>();
    private final int maxCards = 105;
    private final int maxDignity = 14;
    private final int maxSuit = 10;

    Deck() {
        Set<String> cardsSet;
        cardsSet = new HashSet<>();
        final Random random = new Random();
        while (cardsSet.size()<maxCards){
            cardsSet.add((new Card(random.nextInt(maxDignity),random.nextInt(maxSuit))).toString());
        }
        cardsSet.forEach(cardStack::push);
    }

    public Set<String> popCards(int count){
        Set<String> result = new HashSet<>();
        for (int i=0; i<count; i++){
            result.add(cardStack.pop());
        }
        return result;
    }

    public int pushCard(Set<String> cards){
        cards.forEach(cardStack::push);
        return cardStack.size();
    }

    public int getCount(){
        return cardStack.size();
    }
}
