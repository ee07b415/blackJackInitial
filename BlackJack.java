import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface Round {
    public void round();
}

class AIRound implements Round {
    private BlackJack bj;

    public AIRound(BlackJack bj) {
        this.bj = bj;
    }

    @Override
    public void round() {
        int total = 0;
        for(int card: bj.player2.getCurrentCards()){
            total += card;
        }
        if(total < 17){
            bj.player2.currentCards.add(bj.deck.get(0));
            bj.deck.remove(0);
            round();
        }
    }
}

class PlayerRound implements Round {
    private BlackJack bj;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public PlayerRound(BlackJack bj) {
        this.bj = bj;
    }

    public String printCurrentCard(){
        String currentCard = "";
        for(int card : bj.player1.getCurrentCards()){
            currentCard += card + " ";
        }
        return currentCard;
    }

    @Override
    public void round() {
        int total = 0;
        for(int card: bj.player1.getCurrentCards()){
            total += card;
        }
        if (total > 21){
            System.out.println("Already more than 21, can't continue\n");
            return;
        }
        System.out.println("You already have: " + printCurrentCard() + "\n");
        System.out.println("Draw another card? Y/N \n");
        String input = "N";
        try {
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(input.equals("Y")){
            bj.player1.currentCards.add(bj.deck.get(0));
            bj.deck.remove(0);
            round();
        }
    }
}

public class BlackJack {
    public List<Integer> deck = new ArrayList<>();
    class Player {
        public String name;
        public Round round;
        public List<Integer> currentCards;

        public Player(String name, Round round) {
            this.name = name;
            this.round = round;
            currentCards = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public List<Integer> getCurrentCards() {
            return currentCards;
        }
    }

    public Player player1, player2;

    public BlackJack() {

        for(int i = 0; i < 4; i++){
            for(int j = 1; j <= 13; j++){
                deck.add(j);
            }
        }
        Collections.shuffle(deck);

        player1 = new Player("player", new PlayerRound(this));
        player2 = new Player("AI", new AIRound(this));
    }

    private enum Result {
        Win, Lose, Draw
    }

    public Result getResult(){
        int player1_total = 0;
        for(int card: player1.getCurrentCards()){
            player1_total += card;
        }

        int player2_total = 0;
        for(int card: player2.getCurrentCards()){
            player2_total += card;
        }

        if (player1_total <= 21 && player2_total <= 21 && player1_total > player2_total){
            return Result.Win;
        } else if (player1_total <= 21 && player2_total <= 21 && player1_total < player2_total){
            return Result.Lose;
        } else if(player1_total <= 21 && player2_total <= 21 && player1_total == player2_total) {
            return Result.Draw;
        } else if(player1_total > 21 && player2_total <= 21) {
            return Result.Lose;
        } else if(player1_total <= 21 && player2_total > 21) {
            return Result.Win;
        } else {
            return Result.Draw;
        }
    }

    public static void main(String[] args){
        System.out.println("Lets play BlackJACK!\n");

        BlackJack bj = new BlackJack();

        bj.player1.currentCards.add(bj.deck.get(0));
        bj.deck.remove(0);
        bj.player1.currentCards.add(bj.deck.get(0));
        bj.deck.remove(0);
        bj.player2.currentCards.add(bj.deck.get(0));
        bj.deck.remove(0);
        bj.player2.currentCards.add(bj.deck.get(0));
        bj.deck.remove(0);

        bj.player2.round.round();
        bj.player1.round.round();

        Result rs = bj.getResult();

        int player1_total = 0;
        for(int card: bj.player1.getCurrentCards()){
            player1_total += card;
        }

        int player2_total = 0;
        for(int card: bj.player2.getCurrentCards()){
            player2_total += card;
        }

        System.out.println("Your total is " + player1_total + " \n");
        System.out.println("AI total is " + player2_total + " \n");
        System.out.println("You " + rs + "!");

    }
}
