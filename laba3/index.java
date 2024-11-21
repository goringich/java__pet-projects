import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;



public class index{

  private static String determineGender(char[] patronymic) {
    int length = patronymic.length;

  
    // Проверка на окончания для мужских отчеств
    if (
      (length >= 4 && 
        patronymic[length - 1] == 'ч' && 
        patronymic[length - 2] == 'и' &&
        patronymic[length - 3] == 'в' && 
        patronymic[length - 4] == 'о') ||
      (length >= 4 && 
        patronymic[length - 1] == 'ч' && 
        patronymic[length - 2] == 'и' &&
        patronymic[length - 3] == 'в' && 
        patronymic[length - 4] == 'е')) 
    {
      return "М";
    }
  
    // Проверка на окончания для женских отчеств
    else if (
        (length >= 4 && patronymic[length - 1] == 'а' && patronymic[length - 2] == 'н' &&
         patronymic[length - 3] == 'в' && patronymic[length - 4] == 'о') ||
        (length >= 4 && patronymic[length - 1] == 'а' && patronymic[length - 2] == 'н' &&
         patronymic[length - 3] == 'в' && patronymic[length - 4] == 'е')) {
      return "Ж";
    }

    return "Optimus Prime";
  }


  private static LocalDate parseDate(String dateStr){
    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d.M.yyyy");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d/M/yyyy");
    try {
      return LocalDate.parse(dateStr, formatter1);
    } catch (DateTimeParseException e) {
      try {
        return LocalDate.parse(dateStr, formatter2);
      } catch (DateTimeParseException ex) {
        return null;
      }
    }
  }

  private static String getEnding(int number, String one, String few, String many){
    int lastDigit = number % 10;
    int lastTwoDigits = number % 100;

    if (lastTwoDigits >= 11 && lastTwoDigits <= 14){
      return many;
    }

    switch (lastDigit){
      case 1:
        return one;
      case 2:
      case 3:
      case 4:
        return few;
      default:
        return many;
    }
  }


  public static void main(String[] args) {
    try {
      System.setOut(new PrintStream(System.out, true, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      System.err.println("UTF-8 encoding is not supported on this system.");
    }

    Scanner scanner = new Scanner(System.in, "UTF-8");
    String[] nameParts;

    // Ввод ФИО, пока не буде корректным
    while (true){
      System.out.print("Введите ФИО: ");
      String fullName = scanner.nextLine();

      nameParts = fullName.split(" ");

      // System.out.println("Количество частей после разделения: " + nameParts.length);
      // for (String part : nameParts) {
      //   System.out.println("Часть: " + part.);
      // }

      if (nameParts.length == 3) {
        if (!nameParts[0].isEmpty() && !nameParts[1].isEmpty() && !nameParts[2].isEmpty()) {
            break;
        }
      }

      System.out.print("Неверный ввод ФИО. Повторите попытку.");
    }

    String lastName = nameParts[0];
    char[] firstName = nameParts[1].toCharArray();
    char[] patronymic = nameParts[2].toCharArray();
    
    // System.out.println("Длина patronymic: " + patronymic.length);
    // System.out.println("Содержимое nameParts[2]: '" + nameParts[2] + "'");

    // System.out.print("Отчество: ");
    // for (char c : patronymic) {
    //   System.out.print(c); // Вывод каждого символа
    // }


    String initialsString = lastName + " " + firstName[0] + "." + patronymic[0];
    
    String gender = determineGender(patronymic);

    LocalDate birthDate;

    // Ввод даты, пока не буде корректным
    while (true) { 
      System.out.print("Введите дату рождения (дд.мм.гггг или дд/мм/гггг): ");
      String dobInput = scanner.nextLine().trim();
      birthDate = parseDate(dobInput);
      
      if (birthDate != null) {
        // Проверяем, что дата рождения меньше текущей даты
        if (birthDate.isBefore(LocalDate.now())) {
          break;
        } else {
          System.out.println("Дата рождения не может быть в будущем. Повторите попытку.");
        }
      } else {
        System.out.println("Некорректный ввод даты рождения. Повторите попытку.");
      }
    }
    

    LocalDate currentDate = LocalDate.now();
    Period agePeriod = Period.between(birthDate, currentDate);
    int years = agePeriod.getYears();
    int months = agePeriod.getMonths();
    int days= agePeriod.getDays();

    String ageString = years + " " + getEnding(years, "год", "года", "лет");
    String monthString = months + " " + getEnding(months, "месяц", "месяца", "месяцев");
    String dayString = days + " " + getEnding(days, "день", "дня", "дней");

    // Вывод результатов
    System.out.println("Инициалы: " + initialsString);
    System.out.println("Пол: " + gender);
    System.out.println("Возраст: " + ageString + ", " + monthString + ", " + dayString);

 
    scanner.close();
  }
}