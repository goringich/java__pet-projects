
# File Reader

This Java application reads a text file, counts the frequency of each English letter, and writes the results to a specified output file.

## Features

- **Read File**: Prompts the user to input the name of a text file and reads its content.
- **Count Letter Frequency**: Counts the occurrences of each English letter (case-sensitive) in the file.
- **Write Results**: Writes the frequency count of each letter to a user-specified output file.

## Prerequisites

- **Java 11 or higher**: The application utilizes features available in Java 11 and above.

## Usage

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/goringich/java__pet-projects.git
   ```

2. **Navigate to the Project Directory**:

   ```bash
   cd java__pet-projects/file-reader
   ```

3. **Compile the Application**:

   ```bash
   javac FileReader.java
   ```

4. **Run the Application**:

   ```bash
   java FileReader
   ```

5. **Follow the Prompts**:

   - When prompted, enter the path to the input text file.
   - Enter the desired name for the output file where results will be saved.

## Error Handling

- **File Not Found**: If the specified input file does not exist or is not a valid file, the application will display an error message and terminate gracefully.
- **I/O Exceptions**: The application handles exceptions that may occur during file reading or writing, ensuring that appropriate error messages are displayed to the user.

## Notes

- **Character Case**: The frequency count distinguishes between uppercase and lowercase letters. For example, 'A' and 'a' are counted separately.
- **Non-Letter Characters**: Characters that are not English letters are ignored in the frequency count.

## References

- [Java - Count Letter Frequencies](https://www.dotnetperls.com/count-letter-frequencies-java)
- [Java Count Letter Frequencies](https://thedeveloperblog.com/java/count-letter-frequencies-java)

These resources provide additional insights into counting letter frequencies in Java.
