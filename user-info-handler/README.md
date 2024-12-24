# User Info Parser

This Java application processes user input to extract personal information, including initials, gender, and age. It utilizes the user's full name and date of birth to determine and display these details.

## Features

- **Initials Extraction**: Parses the user's full name to extract and display initials in the format "Surname I.O."
- **Gender Determination**: Determines the user's gender based on the patronymic (middle name) using specific Russian language rules.
- **Age Calculation**: Calculates the user's age in years, months, and days from the provided date of birth.

## Prerequisites

- **Java 11 or higher**: The application leverages features introduced in Java 11, such as the `java.time` package for date and time manipulation.

## Usage

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/goringich/java__pet-projects.git
   ```

2. **Navigate to the Project Directory**:

   ```bash
   cd java__pet-projects/user-info-parser
   ```

3. **Compile the Application**:

   ```bash
   javac UserInfoParser.java
   ```

4. **Run the Application**:

   ```bash
   java UserInfoParser
   ```

5. **Follow the Prompts**:

   - Enter your full name in the format "Surname Firstname Patronymic" when prompted.
   - Enter your date of birth in the format "dd.MM.yyyy" or "dd/MM/yyyy" when prompted.

## Input Validation

- **Full Name**: The application expects three components separated by spaces: surname, first name, and patronymic. It validates the input to ensure all three components are present and non-empty.
- **Date of Birth**: The application accepts dates in "dd.MM.yyyy" or "dd/MM/yyyy" formats. It validates the input to ensure it is a correct date and not in the future.

## Gender Determination Logic

The application determines gender based on the ending of the patronymic:

- If the patronymic ends with "ич", it is identified as male.
- If the patronymic ends with "на", it is identified as female.
- If the patronymic does not match these patterns, the gender is set to "Optimus Prime" as a default value.

## Age Calculation

The application calculates the user's age by computing the difference between the current date and the provided date of birth using the `Period` class from the `java.time` package. It accounts for years, months, and days to provide a detailed age breakdown.

## Error Handling

- **Unsupported Encoding**: The application sets the output encoding to UTF-8. If the system does not support UTF-8 encoding, it displays an error message.
- **Invalid Input**: The application prompts the user to re-enter information if the provided full name or date of birth is invalid, ensuring accurate data processing.

## Notes

- **Character Encoding**: The application sets the output stream to UTF-8 to correctly handle Cyrillic characters. Ensure your console or terminal supports UTF-8 encoding to display the output correctly.
- **Date Formats**: The application supports two date formats for flexibility: "dd.MM.yyyy" and "dd/MM/yyyy". Ensure to enter the date of birth in one of these formats.

## References

- [Determining Gender by Patronymic in Java](https://www.planetaexcel.ru/forum/?FID=1&PAGE_NAME=read&TID=49185)
- [Calculating Age from Date of Birth in Java](https://for-each.dev/lessons/b/-java-get-age)

These resources provide additional insights into the logic used for gender determination and age calculation in the application.
