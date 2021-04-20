package sk.koprda.utils;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSearchUtil {

    // stateless, thus thread-safe

    private FileSearchUtil() {
        // intended to be empty
    }

    /**
     * Finds all files that are not directories in the file structure starting at root <b>dir</b>ectory
     *
     * @param dir pathname to root directory to be traversed
     * @return List of absolute pathnames of all files that are not directories in the file structure
     * @throws DirectoryNotFoundException when <b>dir</b> pathname is invalid
     */
    public static ArrayList<String> findAllfiles(String dir) throws DirectoryNotFoundException {
        return traverseDirFilterSuitable(dir, (file) -> !file.isDirectory());
    }

    /**
     * Finds directories in the file structure starting at root <b>dir</b>ectory
     *
     * @param dir pathname to root directory to be traversed
     * @return List of absolute pathnames of all directories found in the file structure
     * @throws DirectoryNotFoundException when <b>dir</b> pathname is invalid
     */
    public static ArrayList<String> findAllDirectories(String dir) throws DirectoryNotFoundException {
        return traverseDirFilterSuitable(dir, File::isDirectory);
    }

    /**
     * Finds files which pathnames matches a given <b>pattern</b>
     *
     * @param pattern pattern to be tested against file names where '?' represents '.' and '*' represents '.*'
     *                in Java regular expression notation
     * @param dir pathname to root directory to be traversed
     * @return List of absolute pathnames of files that names matches a given <b>pattern</b>
     * @throws DirectoryNotFoundException when <b>dir</b> pathname is invalid
     */
    public static ArrayList<String> findFilesByPattern(String pattern, String dir) throws DirectoryNotFoundException {
        return traverseDirFilterSuitable(dir, file -> fileMatchesPattern(file, pattern));
    }

    /**
     *Finds files that were last modified after a given <b>dateTime</b>
     *
     * @param dateTime Date to test against files last modification date
     * @param dir pathname to root directory to be traversed
     * @return List of absolute pathnames of files that were last modified after a given <b>dateTime</b>
     * @throws DirectoryNotFoundException when <b>dir</b> pathname is invalid
     */
    public static ArrayList<String> findFilesByLastChange(LocalDateTime dateTime, String dir)
            throws DirectoryNotFoundException {

        Predicate<File> isSuitable = file -> {
            LocalDateTime lastModified = Instant.ofEpochMilli(file.lastModified())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return lastModified.isAfter(dateTime);
        };
        return traverseDirFilterSuitable(dir, isSuitable);
    }

    /**
     * Traverses file structure starting at the root <b>dir</b>ectory and then applies a <b>isSuitable</b> Predicate
     * to filter files
     *
     * @param dir pathname to root directory to be traversed
     * @param isSuitable Predicate used to filter files
     * @return List of absolute pathnames of files that meets the <b>isSuitable</b> predicate
     * @throws DirectoryNotFoundException when <b>dir</b> pathname is invalid
     */
    private static ArrayList<String> traverseDirFilterSuitable(String dir, Predicate<File> isSuitable)
            throws DirectoryNotFoundException {
        File rootDir = new File(dir);
        if (!rootDir.exists() && !rootDir.isDirectory())
            throw new DirectoryNotFoundException("Given pathname doesn't match any directory!");
        ArrayList<String> suitableFiles = new ArrayList<>();
        Stack<File> unvisitedDirs = new Stack<>();
        unvisitedDirs.push(rootDir);
        File actualDir;
        while (!unvisitedDirs.isEmpty()) {
            actualDir = unvisitedDirs.pop();
            for (File actualDirFile : actualDir.listFiles()) {
                if (actualDirFile.isDirectory())
                    unvisitedDirs.push(actualDirFile);
                if (isSuitable.test(actualDirFile))
                    suitableFiles.add(actualDirFile.getAbsolutePath());
            }
        }
        return suitableFiles;
    }

    /**
     * Determine whether a <b>file</b>'s pahname matches a <b>patternString</b>
     * @param file file which name is to be tested against <b>patternString</b>
     * @param patternString pattern to be tested against file names where '?' represents '.' and '*' represents '.*'
     *                      in Java regular expression notation
     * @return true or false whether a given file's pathname matches a given <b>patternString</b>
     */
    private static boolean fileMatchesPattern(File file, String patternString) {
        patternString = patternString
                .replace("|", "\\|")
                .replace(".", "\\.")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("?", ".")
                .replace("*", ".*");

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(file.getName());
        return matcher.matches();
    }
}
