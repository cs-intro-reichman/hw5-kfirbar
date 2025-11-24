public class Wordle {

    // Reads all words from dictionary file into a String array.
    public static String[] readDictionary(String filename) {
        In in = new In(filename);
        String[] words = in.readAllLines();
        return words;
    }

    // Choose a random secret word from the dictionary.
    public static String chooseSecretWord(String[] dict) {
        int n = dict.length;
        int index = (int) (Math.random() * n);
        return dict[index].trim().toUpperCase();
    }

    // Simple helper: check if letter c appears anywhere in secret.
    public static boolean containsChar(String secret, char c) {
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    // Compute feedback for a single guess into resultRow (length WORD_LENGTH).
    // Simple version: G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        // secret = secret.toUpperCase();
        // guess = guess.toUpperCase();

        for (int i = 0; i < guess.length(); i++) {
            char gc = guess.charAt(i);
            char sc = secret.charAt(i);

            if (gc == sc) {
                resultRow[i] = 'G';
            } else if (containsChar(secret, gc)) {
                resultRow[i] = 'Y';
            } else {
                resultRow[i] = '_';
            }
        }
    }

    // Store guess string into the given row of guesses 2D array.
    public static void storeGuess(String guess, char[][] guesses, int row) {
        for (int col = 0; col < guess.length(); col++) {
            guesses[row][col] = guess.charAt(col);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        for (int i = 0; i < resultRow.length; i++) {
            if (resultRow[i] != 'G') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);
        System.out.println(secret);
        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH];
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH];

        // Reading from the standart input 
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readString().toUpperCase();
                if (guess.length() != WORD_LENGTH) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.println("Sorry, you did not guess the word.");
            System.out.println("The secret word was: " + secret);
        }

        inp.close();
    }
}
