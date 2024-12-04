package Enrollment;

import java.io.*;
import java.util.Scanner;

public class DeleteStudent {
    private static Scanner scanner = new Scanner(System.in);

    public void deleteStudent(String adminName, Student[] students, int[] studentCount) {
        int idToDelete = getValidStudentId(adminName, students, studentCount); // Get valid student ID input

        // Construct the filename based on student ID
        String fileName = "student_" + idToDelete + ".csv"; // Ensure this file exists in the working directory
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error: Student file '" + fileName + "' not found!");
            System.out.println("                                                                                        =======================================");
            return;
        }

        // Ask for confirmation before deleting
        char confirm;
        while (true) {
            try {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                               Confirm Student Deletion       ");
                System.out.println("                                                                                        =======================================");
                System.out.print("                                                                                        Do you really want to delete this student? (y/n): ");
                confirm = scanner.next().charAt(0);
                scanner.nextLine();  // Consume newline

                if (confirm == 'y' || confirm == 'Y') {
                    break;  // Proceed with deletion if 'y' is entered
                } else if (confirm == 'n' || confirm == 'N') {
                    System.out.println("\n                                                                                        Deletion canceled.");
                    idToDelete = getValidStudentId(adminName, students, studentCount);
                } else {
                    // If the input is not 'y', 'Y', 'n', or 'N', print invalid input and loop again
                    System.out.println("\n                                                                                        Invalid input. Please enter 'y' or 'n'.");
                    Student.pressAnyKey();
                }
            } catch (Exception e) {
                // Catch any other unexpected input errors (e.g., input mismatch) and prompt for retry
                System.out.println("\n                                                                                        Invalid input. Please enter 'y' or 'n'.");
                Student.pressAnyKey();
                scanner.nextLine(); // Clear the buffer to prevent infinite loop
            }
        }

        // Delete the file
        if (file.delete()) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                          Student with ID " + idToDelete + " has been deleted.");
            System.out.println("                                                                                        =======================================");
            shiftStudentIds(idToDelete); // Shift IDs of subsequent students
            updateLastId(); // Update last_id.txt after deletion
        } else {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                             Error deleting the student file.");
            System.out.println("                                                                                        =======================================");
        }
    }

    private int getValidStudentId(String adminName, Student[] students, int[] studentCount) {
        int idToDelete = -1;
        while (true) {
            try {
                System.out.print("\n                                                                                        =======================================");
                System.out.print("\n                                                                                        Enter Student ID to delete: ");
                idToDelete = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (idToDelete < 0) {
                    System.out.println("\n                                                                                        =======================================");
                    System.out.println("                                                                                      Invalid input. Please enter a valid student ID.");
                    System.out.println("                                                                                        =======================================");
                    Student.pressAnyKey();
                    Student.PromptCancelToMenu(scanner, adminName, students, studentCount);
                    scanner.nextLine();
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                          Invalid input. Please enter a valid student ID.");
                System.out.println("                                                                                        =======================================");
                scanner.nextLine(); // Consume invalid input
                Student.pressAnyKey();
                Student.PromptCancelToMenu(scanner, adminName, students, studentCount);
            }
        }
        return idToDelete;
    }

    private void shiftStudentIds(int deletedId) {
        // Start from the deleted ID and shift every student file with a higher ID
        int currentId = deletedId;

        // Loop to check for subsequent student files that need their IDs decremented
        while (true) {
            String currentFileName = "student_" + (currentId + 1) + ".csv";
            File currentFile = new File(currentFileName);

            // If the file for the next student ID does not exist, stop shifting
            if (!currentFile.exists()) {
                break;
            }

            // Rename the current student file to the new ID (shifting down by 1)
            String newFileName = "student_" + currentId + ".csv";
            File newFile = new File(newFileName);

            if (currentFile.renameTo(newFile)) {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                         Renamed student file from ID " + (currentId + 1) + " to " + currentId);
                System.out.println("                                                                                        =======================================");

                // Now, update the student ID inside the renamed file
                updateStudentIdInFile(newFile, currentId);
            } else {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                         Error renaming file '" + currentFileName + "' to '" + newFileName + "'.");
                System.out.println("                                                                                        =======================================");
                break;
            }

            // Move to the next ID
            currentId++;
        }
    }

    private void updateStudentIdInFile(File studentFile, int newId) {
        File tempFile = new File("temp_update.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(studentFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line = reader.readLine(); // Read the header
            if (line != null) {
                writer.write(line + System.lineSeparator()); // Write header to temp file

                line = reader.readLine(); // Read the student data
                if (line != null) {
                    String[] studentData = line.split(","); // Split the student data by commas
                    studentData[0] = String.valueOf(newId); // Update the student ID

                    // Write the updated student data to temp file
                    writer.write(String.join(",", studentData) + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error updating student ID: " + e.getMessage());
            System.out.println("                                                                                         =======================================");
        }

        // Replace the original file with the updated file
        if (studentFile.delete() && tempFile.renameTo(studentFile)) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Student ID inside the file updated to " + newId);
            System.out.println("                                                                                        =======================================");
        } else {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error updating the student file.");
            System.out.println("                                                                                        =======================================");
        }
    }

    private void updateLastId() {
        // Read the last ID from last_id.txt
        int lastId = 0;
        File lastIdFile = new File("last_id.txt");

        // Check if last_id.txt exists
        if (lastIdFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(lastIdFile))) {
                String line = br.readLine();
                if (line != null) {
                    lastId = Integer.parseInt(line.trim());
                }
            } catch (IOException e) {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                           Error reading last_id.txt: " + e.getMessage());
                System.out.println("                                                                                        =======================================");
            }
        }

        // Decrement the last ID if necessary
        if (lastId > 0) {
            lastId--; // Decrement the ID after deletion
        }

        // Write the new last ID back to last_id.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(lastIdFile))) {
            bw.write(String.valueOf(lastId));
        } catch (IOException e) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error updating last_id.txt: " + e.getMessage());
            System.out.println("                                                                                        =======================================");
        }
    }
}
