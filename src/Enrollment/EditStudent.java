package Enrollment;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EditStudent {
    private static Scanner scanner = new Scanner(System.in);

    public void editStudentDetails(String adminName, Student[] students, int[] studentCount) {
        int searchId = 0;
        boolean validInput = false;
        File file = null;

        // Loop until a valid student ID is entered and file exists
        while (true) {  // Infinite loop to prompt for student ID until the file is found
            try {
                System.out.print("Enter Student ID to edit: ");
                searchId = scanner.nextInt(); // Try to read an integer for the student ID
                scanner.nextLine(); // Consume newline

                // Construct the filename based on student ID
                String fileName = "student_" + searchId + ".csv";
                file = new File(fileName);

                if (!file.exists()) {
                    System.out.println("\nError: Student file '" + fileName + "' not found!");
                    // If the file is not found, prompt the user again for the student ID
                } else {
                    // If the file exists, break out of the loop
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Please enter a valid integer for the student ID.");
                scanner.nextLine();
                Student.pressAnyKey();
                Student.PromptCancelToMenu(scanner, adminName, students, studentCount);// Consume the invalid input and let the loop continue
            }
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

// Error handling: Ensure the user inputs only 'y' or 'n'
                    while (choice != 'y' && choice != 'Y' && choice != 'n' && choice != 'N') {
                        System.out.print("\nInvalid input. Please enter 'y' to edit or 'n' to skip: ");
                        choice = scanner.next().charAt(0);
                        scanner.nextLine(); // Consume newline
                    }

                    if (choice == 'y' || choice == 'Y') {
                        String newName;
                        while (true) {
                            System.out.print("\nEnter new name (letters only): ");
                            newName = scanner.nextLine();

                            // Check if the name contains any numbers
                            if (newName.matches(".*\\d.*")) {
                                System.out.println("\nWarning: Numbers are not allowed in the name. Please try again...");
                            } else {
                                studentData[1] = newName; // Update name if valid
                                break; // Exit loop if name is valid
                            }
                        }
                    }

                    System.out.print("\nDo you want to edit the student's phone number? (y/n): ");
                    choice = scanner.next().charAt(0);
                    scanner.nextLine(); // Consume newline

// Error handling: Ensure the user inputs only 'y' or 'n'
                    while (choice != 'y' && choice != 'Y' && choice != 'n' && choice != 'N') {
                        System.out.print("\nInvalid input. Please enter 'y' to edit or 'n' to skip: ");
                        choice = scanner.next().charAt(0);
                        scanner.nextLine(); // Consume newline
                    }

                    if (choice == 'y' || choice == 'Y') {
                        String phoneNumber;
                        while (true) {
                            System.out.print("Enter new phone number (11 digits): ");
                            phoneNumber = scanner.nextLine();

                            // Check if the phone number is exactly 11 digits and contains only numbers
                            if (!phoneNumber.matches("\\d{11}")) {
                                System.out.println("\nWarning: Phone number must be 11 digits and contain only numbers. Press Enter to try again...");
                            } else {
                                studentData[2] = phoneNumber;  // Update the phone number if valid
                                break;  // Exit loop when valid phone number is entered
                            }
                        }
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
