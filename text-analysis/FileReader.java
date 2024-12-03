import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class FileReader {
  // print File
  public static void printFile(Scanner FileReader){
    System.out.println("File content: ");
    while (FileReader.hasNextLine()){
      System.out.println(FileReader.nextLine());
    }
  }

  // count the number of letters
  private static Map<Character, Integer> numberOfDifferentEnglishLetters(Scanner FileReader){
    Map<Character, Integer> frequencyMap = new HashMap<>(26);

    while (FileReader.hasNextLine()){
      String line = FileReader.nextLine();
      for (char ch : line.toCharArray()){
        if (Character.isLetter(ch) && ((ch <= 'Z' && ch >= 'A') || (ch <= 'z' && ch >= 'a'))){
          frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }
      }
    }

    return frequencyMap;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Input the data: ");
    String fileName = scanner.nextLine();

    File file = new File(fileName);

    if (!file.exists() || !file.isFile()) {
      System.out.println("The file doesnt exist or is not a file");
      scanner.close();
      return;
    }

    Map<Character, Integer> frequencyMap;
    try (Scanner FileReader = new Scanner(file)){
      // printFile(FileReader);
      frequencyMap = numberOfDifferentEnglishLetters(FileReader);
    } catch (FileNotFoundException e) { // subclass of IOException
      System.out.println("The file doesnt exist or is not a file");
      scanner.close();
      return;
    } 

    System.out.println("Input output file name: ");
    String OutputFileName = scanner.nextLine();

    // write results
    try (FileWriter writer = new FileWriter(OutputFileName)){
      for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()){
        writer.write(entry.getKey() + ": " + entry.getValue() + System.lineSeparator());
      }
      System.out.println("The results were written up successfully");
    } catch (IOException e){
      System.out.println("Error when writing to a file: " + e.getMessage());
    }

    scanner.close();
  }
}