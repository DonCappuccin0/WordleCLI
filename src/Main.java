import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
   public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
      Scanner sc = new Scanner(System.in);
      Wordle wordle = new Wordle(5, 5);

      String playerInput;
      do {
         wordle.display();
         System.out.println("Guess the word");
   }
}
