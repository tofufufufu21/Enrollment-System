package Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SearchStudent {

    public void searchStudentById(String adminName, Student[] students, int[] studentCount) {
        Scanner scanner = new Scanner(System.in);
        int studentId = 0;

        while (true) {  // Loop to allow continuous search until user chooses to return to the dashboard
            // Ensure the input is a valid integer
            while (true) {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                                   Search for a Student        ");
                System.out.println("                                                                                        =======================================");
                System.out.print("                                                                                        Enter the Student ID you want to search for: ");
                try {
                    studentId = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer after reading integer input
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("\n                                                                                        Invalid input! Please enter a valid integer for the Student ID.\n");
                    scanner.nextLine(); // Clear the invalid input
                    Student.pressAnyKey();
                    Student.PromptCancelToMenu(scanner, adminName, students, studentCount);
                }
            }

            String fileName = "student_" + studentId + ".csv";
            File file = new File(fileName);

            if (!file.exists()) {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                               Student with ID " + studentId + " not found. ");
                System.out.println("                                                                                        =======================================");
            } else {
                try (Scanner fileScanner = new Scanner(file)) {
                    if (fileScanner.hasNextLine()) {
                        fileScanner.nextLine(); // Skip the header

                        if (fileScanner.hasNextLine()) {
                            String studentData = fileScanner.nextLine();
                            String[] data = studentData.split(",");
                            System.out.println("\n                                                                                        =======================================");
                            System.out.printf("                                                                                                  Student ID %d Found\n", studentId);
                            System.out.println("                                                                                        =======================================");
                            System.out.println("                                                                                        Student Information:");
                            System.out.println("                                                                                        ----------------------");
                            System.out.println("                                                                                        ID: " + data[0]);
                            System.out.println("                                                                                        Name: " + data[1]);
                            System.out.println("                                                                                        Phone Number: " + data[2]);
                            System.out.println("                                                                                        Strand: " + data[3]);
                            System.out.println("                                                                                        Payment Status: " + data[4]);
                            System.out.println("                                                                                        Balance: " + data[5]);
                            System.out.println("\n                                                                                        =======================================");
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("                                                                                        Error opening file: " + e.getMessage());
                }
            }

            Student.pressAnyKey();

            // Ask the user if they want to go back to the dashboard or continue searching
            while (true) {
                System.out.print("\n                                                                                        Would you like to go back to the dashboard? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("y")) {
                    System.out.println("\n\n\n\n\n");
                    System.out.println("\n                                                                                        =======================================");
                    System.out.println("                                                                                           Returning to the Dashboard...\n");
                    System.out.println("                                                                                        =======================================");
                    return; // Exit the method to go back to the dashboard
                } else if (response.equals("n")) {
                    System.out.println("\n\n\n\n\n");
                    System.out.println("\n                                                                                        =======================================");
                    System.out.println("                                                                                             Continuing to search for another student...\n");
                    System.out.println("                                                                                        =======================================");
                    break; // Exit the inner loop to search another student
                } else {
                    System.out.println("\n                                                                                        Invalid input. Please enter 'y' or 'n'.\n");
                }
            }
        }
    }
}
