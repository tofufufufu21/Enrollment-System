package Enrollment;

import java.io.*;
import java.util.Scanner;

public class DeleteStudent {
    private static Scanner scanner = new Scanner(System.in);

    public void deleteStudent() {
        int idToDelete = getValidStudentId(); // Get valid student ID input

        // Construct the filename based on student ID
        String fileName = "student_" + idToDelete + ".csv"; // Ensure this file exists in the working directory
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Error: Student file '" + fileName + "' not found!");
            return;
        }

        // Ask for confirmation before deleting
        char confirm = getValidConfirmation(); // Get valid confirmation input
        if (confirm != 'y' && confirm != 'Y') {
            System.out.println("Deletion canceled.");
            return;
        }

        // Delete the file
        if (file.delete()) {
            System.out.println("Student with ID " + idToDelete + " has been deleted.");
            shiftStudentIds(idToDelete); // Shift IDs of subsequent students
            updateLastId(); // Update last_id.txt after deletion
        } else {
            System.out.println("Error deleting the student file.");
        }
    }

    private int getValidStudentId() {
        int idToDelete = -1;
        while (true) {
            try {
                System.out.print("Enter Student ID to delete: ");
                idToDelete = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (idToDelete < 0) {
                    System.out.println("Invalid input. Please enter a valid student ID.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid student ID.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        return idToDelete;
    }

    private char getValidConfirmation() {
        char confirm;
        while (true) {
            System.out.print("Do you really want to delete this student? (y/n): ");
            confirm = scanner.next().charAt(0);
            scanner.nextLine();  // Consume newline

            if (confirm == 'y' || confirm == 'Y' || confirm == 'n' || confirm == 'N') {
                break;  // Exit loop if valid input is entered
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
        return confirm;
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
                System.out.println("Renamed student file from ID " + (currentId + 1) + " to " + currentId);

                // Now, update the student ID inside the renamed file
                updateStudentIdInFile(newFile, currentId);
            } else {
                System.out.println("Error renaming file '" + currentFileName + "' to '" + newFileName + "'.");
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
            System.out.println("Error updating student ID: " + e.getMessage());
        }

        // Replace the original file with the updated file
        if (studentFile.delete() && tempFile.renameTo(studentFile)) {
            System.out.println("Student ID inside the file updated to " + newId);
        } else {
            System.out.println("Error updating the student file.");
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
                System.out.println("Error reading last_id.txt: " + e.getMessage());
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
            System.out.println("Error updating last_id.txt: " + e.getMessage());
        }
    }

    private void pressAnyKey() {
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in); // Create a new Scanner for user input
        scanner.nextLine();  // Wait for the user to press Enter
    }
}
