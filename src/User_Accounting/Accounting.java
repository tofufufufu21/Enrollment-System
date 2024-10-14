package User_Accounting;

import java.io.*;
import java.util.Scanner;
import Enrollment.Student;
import User_Types.UserType;

public class Accounting {
    private static Scanner scanner = new Scanner(System.in);

    public void studentLogin() {
        System.out.print("Please enter your Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Check if the student exists and get their details
        Student student = getStudentDetails(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Check the payment status
        if (student.getBalance() == 0) {
            System.out.println("You are already fully paid.");
            promptReturnToMenu();
            return; // Exit if fully paid
        } else {
            // Direct to StudentAccounting for payment processing
            StudentAccounting studentAccounting = new StudentAccounting();
            studentAccounting.loadStudentDashboard(studentId);
        }
    }

    private Student getStudentDetails(int studentId) {
        String fileName = "student_" + studentId + ".csv";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Student file not found.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read the header line
            reader.readLine(); // Skip header

            String line;
            if ((line = reader.readLine()) != null) {
                String[] studentData = line.split(","); // Use comma as the delimiter
                int id = Integer.parseInt(studentData[0].trim());
                String name = studentData[1].trim();
                String phoneNumber = studentData[2].trim();
                String selectedStrand = studentData[3].trim(); // Assuming selected strand is at index 3
                String paymentStatus = studentData[4].trim();
                double balance = Double.parseDouble(studentData[5].trim()); // Assuming balance is at index 5

                return new Student(id, name, balance, phoneNumber, selectedStrand, paymentStatus);
            }
        } catch (IOException e) {
            System.out.println("Error reading student file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing balance data: " + e.getMessage());
        }
        return null; // Return null if no student is found
    }

    private void promptReturnToMenu() {
        System.out.print("Do you want to go back to the main menu? (y/n): ");
        char backChoice = scanner.next().charAt(0);
        scanner.nextLine(); // Consume newline
        if (backChoice == 'y' || backChoice == 'Y') {
            UserType.user_type_menu(); // Call user_type_menu to return to the main menu
        }
    }
}
