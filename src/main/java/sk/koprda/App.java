package sk.koprda;

import sk.koprda.utils.DirectoryNotFoundException;
import sk.koprda.utils.FileSearchUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String rootDirectory = "";
        int option;
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------File search app-----------");
        while(true){
            if(rootDirectory.equals("")){
                rootDirectory = setRootDirectory(sc);
            }
            printOptions();
            try{
                option = sc.nextInt();
                sc.nextLine();
                switch (option){
                    case 1:
                        handleAllFiles(rootDirectory);
                        break;
                    case 2:
                        handleAllDirectories(rootDirectory);
                        break;
                    case 3:
                        handlePatternMatchOption(rootDirectory, sc);
                        break;
                    case 4:
                        handleLastModifiedOption(rootDirectory, sc);
                        break;
                    case 5:
                        rootDirectory = "";
                        break;
                }
            } catch(InputMismatchException ime){
                System.out.println("Incorrect option, try again!");
                continue;
            } catch(DirectoryNotFoundException dnfe){
                System.out.println("Invalid directory pathname");
                rootDirectory = "";
                continue;
            } catch (DateTimeParseException dtpe){
                System.out.println("Invalid date and time!");
                continue;
            }
        }
    }

    /**
     * Prints user friendly output and asks for root directory
     * @param sc scanner used for getting input from user
     * @return root directory pathname
     */
    private static String setRootDirectory(Scanner sc){
        System.out.println("Please specify a root directory you want to examine (absolute path): ");
        System.out.println("Example: \"C:\\directory_1\\directory_2\"");
        return sc.nextLine();
    }

    /**
     * Prints options that can be done with application
     */
    private static void printOptions(){
        System.out.println("-(1) Print all files in root directory");
        System.out.println("-(2) Print all directories in root directory");
        System.out.println("-(3) Print files in root directory that match a pattern");
        System.out.println("-(4) Print files in root directory that were last modified after specified date and time");
        System.out.println("-(5) Change root directory");
        System.out.print("Your choice(1/2/3/4/5): ");
    }

    /**
     * Prettify the result of each method from FileSearchUtil
     * @param result List of Strings returned by FileSearchUtil methods
     */
    private static void prettify(List<String> result){
        for(int i = 0; i < result.size(); i++){
            System.out.println((i+1) + ". " + result.get(i));
        }
    }

    /**
     * Handle situation when user select to print all files
     * @param rootDirectory directory used as a parameter in findAllFilesMethod
     * @throws DirectoryNotFoundException when <b>rootDirectory</b> is not valid pathname
     */
    private static void handleAllFiles(String rootDirectory) throws DirectoryNotFoundException{
        List<String> result = FileSearchUtil.findAllFiles(rootDirectory);
        prettify(result);
    }

    /**
     * Handle situation when user select to print all directories
     * @param rootDirectory directory used as a parameter in findAllDirectories method
     * @throws DirectoryNotFoundException when <b>rootDirectory</b> is not valid pathname
     */
    private static void handleAllDirectories(String rootDirectory) throws DirectoryNotFoundException{
        List<String> result = FileSearchUtil.findAllDirectories(rootDirectory);
        prettify(result);
    }

    /**
     * Hanlde situation when user select to print files specified by some pattern
     * @param rootDirectory directory used as a parameter in findFilesByPattern method
     * @param sc Scanner object used for getting input (pattern) from user
     * @throws DirectoryNotFoundException when <b>rootDirectory</b> is not valid pathname
     */
    private static void handlePatternMatchOption(String rootDirectory, Scanner sc) throws DirectoryNotFoundException{
        String pattern = "";
        System.out.println("? - any single character");
        System.out.println("* - any character sequence");
        System.out.println("Specify pattern: ");
        pattern = sc.nextLine();
        List<String> result = FileSearchUtil.findFilesByPattern(pattern, rootDirectory);
        prettify(result);
    }

    /**
     * Handle situation when user select to print files specified by last modification date and time
     * @param rootDirectory directory used as a parameter in findFilesByLastChange method
     * @param sc Scanner object used for getting input (date and time) from user
     * @throws DirectoryNotFoundException when <b>rootDirectory</b> is not valid pathname
     * @throws DateTimeParseException when date and time format is not valid
     */
    private static void handleLastModifiedOption(String rootDirectory, Scanner sc)
            throws DirectoryNotFoundException, DateTimeParseException{
        String dateString ="";
        System.out.println("Format: MM/dd/yyyy HH:mm");
        System.out.println("Specify date and time:");
        dateString = sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        List<String> result = FileSearchUtil.findFilesByLastChange(dateTime, rootDirectory);
        prettify(result);
    }
}
