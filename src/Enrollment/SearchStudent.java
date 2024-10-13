package Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SearchStudent {

    public void searchStudentById(int studentId) {
        String fileName = "student_" + studentId + ".csv"; // Construct the filename for the student
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Student with ID " + studentId + " not found.");
            pressAnyKey();
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                // Skip the header line
                scanner.nextLine(); // Read and ignore the header

                if (scanner.hasNextLine()) {
                    String studentData = scanner.nextLine(); // Read the student data
                    String[] data = studentData.split(","); // Split the student data into fields
                    System.out.printf("Student ID %d Found\n", studentId);
                    System.out.println("Student Data:");
                    System.out.println("ID: " + data[0]);
                    System.out.println("Name: " + data[1]);
                    System.out.println("Phone Number: " + data[2]);
                    System.out.println("Strand: " + data[3]);
                    System.out.println("Payment Status: " + data[4]);
                    System.out.println("Balance: " + data[5]);
                    // Add any other relevant fields here
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
        pressAnyKey(); // Wait for user input after displaying student data
    }

    public void searchStudentByName(String studentName) {
        File folder = new File("."); // Current directory
        File[] files = folder.listFiles((dir, name) -> name.startsWith("student_") && name.endsWith(".csv"));

        if (files == null || files.length == 0) {
            System.out.println("No student records found.");
            pressAnyKey();
            return;
        }

        boolean found = false;

        for (File file : files) {
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextLine()) {
                    // Skip the header line
                    scanner.nextLine(); // Read and ignore the header

                    if (scanner.hasNextLine()) {
                        String studentData = scanner.nextLine(); // Read student data
                        String[] data = studentData.split(","); // Split the student data into fields

                        if (data[1].equalsIgnoreCase(studentName)) {
                            System.out.printf("Student ID %s Found\n", data[0]); // Using ID from data
                            System.out.println("Student Data:");
                            System.out.println("ID: " + data[0]);
                            System.out.println("Name: " + data[1]);
                            System.out.println("Phone Number: " + data[2]);
                            System.out.println("Strand: " + data[3]);
                            System.out.println("Payment Status: " + data[4]);
                            System.out.println("Balance: " + data[5]);
                            // Add any other relevant fields here
                            found = true;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error opening file: " + e.getMessage());
            }
        }

        if (!found) {
            System.out.println("No student found with the name: " + studentName);
        }
        pressAnyKey(); // Wait for user input after search
    }

    // Method to simulate "Press any key to continue"
    private void pressAnyKey() {
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in); // Create a new Scanner for user input
        scanner.nextLine();
    }
}
