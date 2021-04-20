package sk.koprda.utils;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Predicate;

public class FileSearchUtil {

    // stateless, thus thread-safe

    private FileSearchUtil(){
        // intended to be empty
    }

    private static ArrayList<String> traverseDirFilterSuitable(String dir, Predicate<File> isSuitable) throws DirectoryNotFoundException {
        File rootDir = new File(dir);
        if(!rootDir.exists() && !rootDir.isDirectory())
            throw new DirectoryNotFoundException("Given pathname doesn't match any directory!");
        ArrayList<String> suitableFiles = new ArrayList<>();
        Stack<File> unvisitedDirs = new Stack<>();
        unvisitedDirs.push(rootDir);
        File actualDir;
        while(!unvisitedDirs.isEmpty()){
            actualDir = unvisitedDirs.pop();
            for(File actualDirFile: actualDir.listFiles()){
                if(actualDirFile.isDirectory())
                    unvisitedDirs.push(actualDirFile);
                if(isSuitable.test(actualDirFile))
                    suitableFiles.add(actualDirFile.getAbsolutePath());
            }
        }
        return suitableFiles;
    }

    public static ArrayList<String> findAllfiles(String dir) throws DirectoryNotFoundException {
        return traverseDirFilterSuitable(dir, (file) -> !file.isDirectory());
    }

    public static ArrayList<String> findAllDirectories(String dir) throws DirectoryNotFoundException {
        return traverseDirFilterSuitable(dir, file -> file.isDirectory());
    }

    public static ArrayList<String> findFilesByPattern(String pattern, String dir){
        return null;
    }
    public static ArrayList<String> findFilesByLastChange(LocalDateTime dateTime, String dir)
            throws DirectoryNotFoundException{

        Predicate<File> isSuitable = file -> {
            LocalDateTime lastModified = Instant.ofEpochMilli(file.lastModified())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return  lastModified.isAfter(dateTime);
        };
        return traverseDirFilterSuitable(dir, isSuitable);
    }
}
