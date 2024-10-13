package Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SearchStudent {

    public static class Student {
        int id;
        String name;
        String phoneNumber;
        String selectedStrand;
        String paymentStatus;
        int balance;
        String[] enrolledSubjects;
        int numEnrolledSubjects;

        public Student() {
            this.enrolledSubjects = new String[10]; // Adjust if needed
            this.numEnrolledSubjects = 0;
        }
    }

    public static void searchStudentById() {
        File file = new File("students.csv");
        if (!file.exists()) {
            System.out.println("Error: File 'students.csv' not found!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Student ID to search: ");
        int searchId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try (Scanner fileScanner = new Scanner(file)) {
            // Skip the header line if it exists
            boolean found = false;
            Student student = new Student();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");

                // Ensure we have at least 6 fields for the base student info
                if (data.length >= 6) {
                    student.id = Integer.parseInt(data[0]);
                    student.name = data[1];
                    student.phoneNumber = data[2];
                    student.selectedStrand = data[3];
                    student.paymentStatus = data[4];
                    student.balance = Integer.parseInt(data[5]);

                    // Check if the ID matches the search
                    if (student.id == searchId) {
                        // Parse enrolled subjects (from the 7th column onwards)
                        student.numEnrolledSubjects = 0;
                        for (int i = 6; i < data.length; i++) {
                            student.enrolledSubjects[student.numEnrolledSubjects] = data[i];
                            student.numEnrolledSubjects++;
                        }

                        // Display student information
                        found = true;
                        printStudentDetails(student);
                        break; // Exit once the student is found
                    }
                }
            }

            if (!found) {
                System.out.println("\nNo student found with ID: " + searchId);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void printStudentDetails(Student student) {
        System.out.println("Student ID: " + student.id);
        System.out.println("Name: " + student.name);
        System.out.println("Phone Number: " + student.phoneNumber);
        System.out.println("Strand: " + student.selectedStrand);
        System.out.println("Payment Status: " + student.paymentStatus);
        System.out.println("Balance: " + student.balance);
        System.out.println("Enrolled Subjects: ");
        for (int i = 0; i < student.numEnrolledSubjects; i++) {
            System.out.println("- " + student.enrolledSubjects[i]);
        }
    }
}
