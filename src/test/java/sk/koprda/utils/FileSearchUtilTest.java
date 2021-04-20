package sk.koprda.utils;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSearchUtilTest {

    String testDir = System.getProperty("user.dir") + "\\src\\test\\resources\\TestDirectory\\";

    @Test
    void testFindAllFiles_1() {
        List<String> expectedValues = Arrays.asList(
                testDir + "dir_a\\file_001",
                testDir + "dir_a\\file_002",
                testDir + "dir_a\\dir_aa\\file_003",
                testDir + "dir_b\\file_004",
                testDir + "dir_b\\file_005",
                testDir + "dir_b\\file_006"
        );
        try {
            List<String> actualValues = FileSearchUtil.findAllFiles("src/test/resources/TestDirectory");
            expectedValues.sort(String::compareTo);
            actualValues.sort(String::compareTo);
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindAllFiles_2() {
        try {
            FileSearchUtil.findAllFiles("non-existing_dir");
        } catch (DirectoryNotFoundException dnfe) {
            assertEquals("Given pathname doesn't match any directory!", dnfe.getMessage());
        }
    }

    @Test
    void testFindAllDirectories_1() {
        List<String> expectedValues = Arrays.asList(
                testDir + "dir_a",
                testDir + "dir_a\\dir_aa",
                testDir + "dir_b"
        );
        try {
            List<String> actualValues = FileSearchUtil.findAllDirectories("src/test/resources/TestDirectory");
            expectedValues.sort(String::compareTo);
            actualValues.sort(String::compareTo);
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindAllDirectories_2() {
        try {
            FileSearchUtil.findAllDirectories("non-existing_dir");
        } catch (DirectoryNotFoundException dnfe) {
            assertEquals("Given pathname doesn't match any directory!", dnfe.getMessage());
        }
    }

    @Test
    void testFindFilesByLastChange_1() {
        List<String> expectedValues = Arrays.asList(
                testDir + "dir_a\\file_001",
                testDir + "dir_a",
                testDir + "dir_a\\dir_aa\\file_003",
                testDir + "dir_a\\dir_aa"
        );
        try {
            LocalDateTime dateTime = LocalDateTime.of(
                    2021,
                    4,
                    20,
                    14,
                    45
            );
            List<String> actualValues = FileSearchUtil.findFilesByLastChange(dateTime, "src/test/resources/TestDirectory");
            expectedValues.sort(String::compareTo);
            actualValues.sort(String::compareTo);
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindFilesByLastChange_2() {
        List<String> expectedValues = Arrays.asList();
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            List<String> actualValues = FileSearchUtil.findFilesByLastChange(dateTime, "src/test/resources/TestDirectory");
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindFilesByLastChange_3() {
        List<String> expectedValues = Arrays.asList(
                testDir + "dir_a\\file_001",
                testDir + "dir_a\\file_002",
                testDir + "dir_a",
                testDir + "dir_a\\dir_aa\\file_003",
                testDir + "dir_a\\dir_aa",
                testDir + "dir_b\\file_004",
                testDir + "dir_b\\file_005",
                testDir + "dir_b\\file_006",
                testDir + "dir_b"
        );
        try {
            LocalDateTime dateTime = LocalDateTime.of(
                    2021,
                    4,
                    20,
                    14,
                    45
            ).minusHours(2);
            List<String> actualValues = FileSearchUtil.findFilesByLastChange(dateTime, "src/test/resources/TestDirectory");
            expectedValues.sort(String::compareTo);
            actualValues.sort(String::compareTo);
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindFilesByPattern_1() {
        try {
            String pattern = "file?001";
            List<String> expectedValues = Arrays.asList(testDir + "dir_a\\file_001");
            List<String> actualValues = FileSearchUtil.findFilesByPattern(pattern, "src/test/resources/TestDirectory");
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindFilesByPattern_2() {
        try {
            String pattern = "d*a";
            List<String> expectedValues = Arrays.asList(
                    testDir + "dir_a",
                    testDir + "dir_a\\dir_aa"
            );
            List<String> actualValues = FileSearchUtil.findFilesByPattern(pattern, "src/test/resources/TestDirectory");
            expectedValues.sort(String::compareTo);
            actualValues.sort(String::compareTo);
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindFilesByPattern_3() {
        try {
            String pattern = "file_*";
            List<String> expectedValues = Arrays.asList(
                    testDir + "dir_a\\file_001",
                    testDir + "dir_a\\file_002",
                    testDir + "dir_a\\dir_aa\\file_003",
                    testDir + "dir_b\\file_004",
                    testDir + "dir_b\\file_005",
                    testDir + "dir_b\\file_006"
            );
            List<String> actualValues = FileSearchUtil.findFilesByPattern(pattern, "src/test/resources/TestDirectory");
            expectedValues.sort(String::compareTo);
            actualValues.sort(String::compareTo);
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }

    @Test
    void testFindFilesByPattern_4() {
        try {
            String pattern = "*";
            List<String> expectedValues = Arrays.asList(
                    testDir + "dir_a\\file_001",
                    testDir + "dir_a\\file_002",
                    testDir + "dir_a",
                    testDir + "dir_a\\dir_aa\\file_003",
                    testDir + "dir_a\\dir_aa",
                    testDir + "dir_b\\file_004",
                    testDir + "dir_b\\file_005",
                    testDir + "dir_b\\file_006",
                    testDir + "dir_b"
            );
            List<String> actualValues = FileSearchUtil.findFilesByPattern(pattern, "src/test/resources/TestDirectory");
            expectedValues.sort(String::compareTo);
            actualValues.sort(String::compareTo);
            assertEquals(expectedValues, actualValues);
        } catch (DirectoryNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
    }


}
