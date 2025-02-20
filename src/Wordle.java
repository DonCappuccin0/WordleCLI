import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

public class Wordle {
    private int tries;
    private final String wordToGuess;
    private final Set<Character> availableLetters;
    private final Set<Character> correctLetters;

    private static class GeneratedWord {
        private static final HttpClient httpClient = HttpClient.newHttpClient();
        public static String generate(int wordlength) throws URISyntaxException, IOException, InterruptedException {
            String API_URL = "https://random-word-api.vercel.app/api?words=1&length=";
            HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(new URI(API_URL+ wordlength)).build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return (httpResponse.body()).replaceAll("[\\[\\]\"]", "");
        }
    }

    public Wordle(int wordLength, int tries) throws URISyntaxException, IOException, InterruptedException {
        this.wordToGuess = Wordle.GeneratedWord.generate(wordLength);
        this.tries = tries;
        this.correctLetters = new HashSet<>();
        this.availableLetters = new HashSet<>();
        char character = 'a';

        for (int i = 0; i < 26; ++i) {
            this.availableLetters.add(character++);
        }
    }

    public void display() {
        System.out.println("Tries Remaining : " +  this.tries + "\nAvailable Letters : " + this.availableLetters.toString().toUpperCase());
    }

    public boolean guess(String playerInput) {
        if (playerInput.isEmpty()) {
            System.out.println("You're input can't be empty");
            return false;
        } else if (playerInput.length() != this.wordToGuess.length()) {
            System.out.println("You need to type exactly " + this.wordToGuess.length() + " characters.");
            return false;
        } else if (playerInput.equalsIgnoreCase(this.wordToGuess)) {
            System.out.println("GG you guessed the correct word --> " + this.wordToGuess);
            return true;
        } else {
            --this.tries;
            if (this.tries == 0) {
                System.out.println("All your tries have ended. You lost !\n The correct word was --> " + this.wordToGuess);
                return true;
            } else {
                this.refreshLetters(playerInput);
                this.positionChecker(playerInput);
                return false;
            }
        }
    }

    public void refreshLetters(String playerInput) {
        for (int i = 0; i < this.wordToGuess.length(); ++i) {
            if (!this.wordToGuess.contains(String.valueOf(playerInput.charAt(i)))) {
                this.availableLetters.remove(playerInput.charAt(i));
            } else {
                this.correctLetters.add(playerInput.charAt(i));
            }
        }
    }

    public void positionChecker(String playerInput) {
        for (int i = 0; i < playerInput.length(); ++i) {
            char playerInputChar = playerInput.charAt(i);
            if (!this.correctLetters.contains(playerInputChar)) {
                System.out.println("The letter " + playerInputChar + " doesn't exist in the word to guess");
            } else if (this.wordToGuess.indexOf(playerInputChar, i) == playerInput.indexOf(playerInputChar, i)) {
                System.out.println("The letter " + playerInputChar + " is in the correct place.");
            } else {
                System.out.println("The letter " + playerInputChar + " is not in the correct place.");
            }
        }
    }
}