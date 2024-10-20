package Enrollment;

import java.io.*;
import java.util.Scanner;

public class EditStudent {
    private static Scanner scanner = new Scanner(System.in);

    public void editStudentDetails() {
        System.out.print("Enter Student ID to edit: ");
        int searchId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Construct the filename based on student ID
        String fileName = "student_" + searchId + ".csv"; // Ensure this file exists in the working directory
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Error: Student file '" + fileName + "' not found!");
            return;
        }

        File tempFile = new File("temp.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean found = false;

            // Read the header
            line = reader.readLine();
            writer.write(line + System.lineSeparator()); // Write the header to temp file

            // Read student data
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(","); // Split the data into fields
                int studentId = Integer.parseInt(studentData[0]);

                if (studentId == searchId) {
                    found = true;
                    // Display current information
                    System.out.println("\nStudent Found:");
                    System.out.println("ID: " + studentId);
                    System.out.println("Name: " + studentData[1]);
                    System.out.println("Phone Number: " + studentData[2]);

                    // Edit student details (only name and phone number)
                    System.out.print("\nDo you want to edit the student's name? (y/n): ");
                    char choice = scanner.next().charAt(0);
                    scanner.nextLine(); // Consume newline
                    if (choice == 'y' || choice == 'Y') {
                        System.out.print("Enter new name: ");
                        studentData[1] = scanner.nextLine();
                    }

                    System.out.print("\nDo you want to edit the student's phone number? (y/n): ");
                    choice = scanner.next().charAt(0);
                    scanner.nextLine(); // Consume newline
                    if (choice == 'y' || choice == 'Y') {
                        System.out.print("Enter new phone number: ");
                        studentData[2] = scanner.nextLine();
                    }

                    // Write the updated student data to the temp file
                    writer.write(String.join(",", studentData) + System.lineSeparator());
                } else {
                    // Write the original data if not found
                    writer.write(line + System.lineSeparator());
                }
            }

            if (!found) {
                System.out.println("\nNo student found with ID: " + searchId);
            } else {
                System.out.println("\nStudent details updated successfully!");
            }

        } catch (IOException e) {
            System.out.println("Error processing the file: " + e.getMessage());
        }

        // Replace the original file with the temporary file
        if (file.delete() && tempFile.renameTo(file)) {
            System.out.println("File updated successfully.");
        } else {
            System.out.println("Error replacing the original file.");
        }
    }
}
