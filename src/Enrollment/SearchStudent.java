package Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SearchStudent {

    public void searchStudentById() {
        Scanner scanner = new Scanner(System.in);  // Re-use scanner
        int studentId = 0;

        while (true) {  // Loop to continuously ask for student ID
            // Ensure the input is a valid integer
            while (true) {
                System.out.print("Enter Student ID to search: ");
                try {
                    studentId = scanner.nextInt();  // Get student ID here
                    break; // Exit the loop if input is valid
                } catch (InputMismatchException e) {
                    System.out.println("\nInvalid input! Please enter a valid integer for the Student ID.\n");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            String fileName = "student_" + studentId + ".csv"; // Construct the filename for the student
            File file = new File(fileName);

            if (!file.exists()) {
                System.out.println("Student with ID " + studentId + " not found.");
            } else {
                try (Scanner fileScanner = new Scanner(file)) {
                    if (fileScanner.hasNextLine()) {
                        // Skip the header line
                        fileScanner.nextLine(); // Read and ignore the header

                        if (fileScanner.hasNextLine()) {
                            String studentData = fileScanner.nextLine(); // Read the student data
                            String[] data = studentData.split(","); // Split the student data into fields
                            System.out.printf("Student ID %d Found\n", studentId);
                            System.out.println("Student Data:");
                            System.out.println("ID: " + data[0]);
                            System.out.println("Name: " + data[1]);
                            System.out.println("Phone Number: " + data[2]);
                            System.out.println("Strand: " + data[3]);
                            System.out.println("Payment Status: " + data[4]);
                            System.out.println("Balance: " + data[5]);
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error opening file: " + e.getMessage());
                }
            }

            pressAnyKey(); // Wait for user input after displaying student data or error
        }
    }

    // Method to simulate "Press any key to continue"
    private void pressAnyKey() {
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in); // Create a new Scanner for user input
        scanner.nextLine();  // Wait for the user to press Enter
    }
}
