import java.util.Random;
import java.util.Scanner;

class Numbergame{

    public static void main(String[] args){

        Random random = new Random();
        int generatedNo = random.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);
        
        int userGuess;
        boolean correctGuess = false;

        System.out.println("Welcome to Number Game!!");
        System.out.println("I have generated a number between 1-100. Try to guess  it!!!");

        while(!correctGuess){
            System.out.println("Enter your Guess...");
            userGuess = scanner.nextInt();

            if(userGuess == generatedNo){
                System.out.println("Congratulations!! You have guessed the number!!");
                correctGuess = true;
            }
            else if(userGuess < generatedNo){
                System.out.println("oops! You've gussed low! Try again!");
            }
            else{
                System.out.println("oops! You've guessed high! Try again!");
            }
        }
        scanner.close();
    }
}

