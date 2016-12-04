package ru.mail.park.model.game;

import java.util.*;

public class Deck {
    private final Stack<Card> cardStack = new Stack<>();
    private final int maxCards = 105;
    private final int maxDignity = 14;
    private final int maxSuit = 10;

    public Deck() {
        Set<Card> cardsSet;
        cardsSet = new HashSet<>();
        final Random random = new Random();
        while (cardsSet.size()<maxCards){
            cardsSet.add(new Card(random.nextInt(maxDignity),random.nextInt(maxSuit)));
        }
        cardsSet.forEach(cardStack::push);
    }

    public Set<Card> popCards(int count){
        Set<Card> result = new HashSet<>();
        Card card = null;
        for (int i=0; i<count; i++){
            card = cardStack.pop();
            result.add(card);
        }
        return result;
    }

    public int pushCard(Set<Card> cards){
        cards.forEach(cardStack::push);
        return cardStack.size();
    }
}
