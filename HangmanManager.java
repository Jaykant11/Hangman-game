
import java.util.*;

public class HangmanManager {
   
   private SortedSet<String> wordBank;
   
   private SortedSet<Character> pickedLetters;
   
   private int guessesLeft;
   
   private char currentPattern[];
   
   private int length;
   
   private char patternArray[];
   
   private String biggestWordChoice;
   
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      this.length = length;
      
      if(length < 1 || max < 0) {
         throw new IllegalArgumentException();
      } else { //if we dont need to throw exceptions, we run the game.
         currentPattern = new char[length];
         guessesLeft = max; //sets max to the amount of guesses we have left
         wordBank = new TreeSet<String>(); //makes structure to store all possible words
         pickedLetters = new TreeSet<Character>(); //structure to keep track of all characters chosen.
         for (int i = 0; i < length; i++) {
            currentPattern[i] = '-';
         }
      }
      
      // this is to match the length of words, so we only 
      // use the words of specified length
      for (String word : dictionary) {
         if (word.length() == length) {
            wordBank.add(word);
         } else { //if the word is not of the same length
            //do nothing. skip word.
         }
      }
   }
   
   /*
   we call this method to see the entire
   set of words that the program is still
   considering as a possibility to be the
   final word.
   */
   public Set<String> words() {
      return wordBank;
   }
   
   /*
   this will return the number of guesses
   that the user has left in order to
   guess the word correctly.
   */
   public int guessesLeft() {
      return guessesLeft;
   }
   
   /*
   this will return all of the letters
   that have already been guessed by
   the user.
   */
   public Set<Character> guesses() {
      return pickedLetters;
   }
    
   /*
   this method will return the current
   pattern of the string that we are
   working with.
   */
   public String pattern() {
      StringBuilder builder = new StringBuilder("");
      for (int i = 0; i < currentPattern.length; i++) {
         builder.append(currentPattern[i]);
         builder.append(" ");
      }
      return builder.toString();
   }
   
   /*
   this method will take the users guess as a parameter
   and use that to update the pattern we are working with,
   as well as our wordBank. furthermore, this method will 
   throw an illegalStateException if there is no more guesses
   left, or if there are no more words in the wordBank. And 
   also it will throw an illegalArguementException if the 
   guess was already guessed.
   */
   public int record(char guess) {
      if (guessesLeft < 1 || wordBank.isEmpty()) {
         throw new IllegalStateException();
      } else if (pickedLetters.contains(guess)) {
         throw new IllegalArgumentException();
      }
      
      pickedLetters.add(guess);
      
      Map<String, List<String>> map = new TreeMap<>();
   
      for (String word : wordBank) {
         char[] charArray = word.toCharArray();
         patternArray = new char[length];
         for (int i = 0; i < length; i++) { //build pattern that will become our key.
            if (charArray[i] == guess) {
               patternArray[i] = guess;
            } else if (charArray[i] == currentPattern[i]) {
               patternArray[i] = currentPattern[i];
            } else {
               patternArray[i] = '-' ;
            }
         }
         String pString = new String(patternArray);
         String key = pString;
         if ( map.containsKey(key)) {
            map.get(key).add(word);
         } else {
            List<String> list = new ArrayList<String>();
            list.add(word);
            map.put(key, list);
         }
      }
      
      /*
      go through our map to find the biggest wordBank
      possible
      */
      int max = 0;
      List<String> biggestWordChoice;
      String biggestWordKey = null;
      for (String thisKey : map.keySet()) {
         int count = 0; 
         List<String> thisArray = map.get(thisKey);
         count = thisArray.size();
         if (count > max) {
            max = count;
            biggestWordChoice = thisArray;
            biggestWordKey = thisKey;
            
         }
      }
      /* 
      set the current pattern, with the 
      places where the current guess needs to go
      */
      for(int i = 0; i < length; i++) {
         if (biggestWordKey.charAt(i) == guess) {
            currentPattern[i] = biggestWordKey.charAt(i);
         }
      }
      /*
      get words from the map and add them into the 
      current wordBank
      /*/
      wordBank.clear();
      String keyString = new String(currentPattern);
      for (String validWord : map.get(keyString)) {
         wordBank.add(validWord); 
      }
      /*
      
      */
      int occurences = 0;
      for (int i = 0; i < currentPattern.length; i++) {
         if (currentPattern[i] == guess) {
            occurences++;
         } else {
            //do nothing
         }
      }
      if (occurences == 0) {
         guessesLeft--;
      }
      return occurences;
   }
}