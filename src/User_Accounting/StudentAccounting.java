package User_Accounting;

import java.io.*;
import java.util.Scanner;
import Enrollment.Student;
import Enrollment.InitializeStrands;
import Enrollment.Strand;
import Enrollment.Subject;
import User_Types.UserType;

public class StudentAccounting {
    private static Scanner scanner = new Scanner(System.in);
    private static InitializeStrands initializeStrands = new InitializeStrands();

    public void loadStudentDashboard(int studentId) {
        Student student = getStudentDetails(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Check payment status
        if (student.getBalance() == 0) {
            System.out.println("You are already fully paid.");
            promptReturnToMenu();
            return;
        }

        while (true) {
            System.out.print("\nDo you want to make a payment? (y/n): ");
            char choice = scanner.next().charAt(0);
            scanner.nextLine();

            if (choice == 'n' || choice == 'N') {
                promptReturnToMenu();
                continue;
            } else if (choice == 'y' || choice == 'Y') {
                processPayment(student);
                break;
            } else {
                System.out.println("Invalid choice. Please enter 'y' or 'n'.");
            }
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
            reader.readLine();

            String line;
            if ((line = reader.readLine()) != null) {
                String[] studentData = line.split(","); // Ensure to use comma here
                int id = Integer.parseInt(studentData[0].trim());
                String name = studentData[1].trim();
                String phoneNumber = studentData[2].trim();
                String selectedStrandName = studentData[3].trim();
                String paymentStatus = studentData[4].trim();
                double balance = Double.parseDouble(studentData[5].trim());
                String enrolledSubjects = studentData.length > 6 ? studentData[6].trim() : "";

                // Load the strand with subjects
                Strand selectedStrand = loadStrand(selectedStrandName);
                Student student = new Student(id, name, balance, phoneNumber, selectedStrand, paymentStatus);

                // Load enrolled subjects if any
                if (!enrolledSubjects.isEmpty()) {
                    String[] subjects = enrolledSubjects.split(";");
                    for (String subjectName : subjects) {
                        student.addSubject(new Subject(subjectName.trim()));
                    }
                }

                return student;
            }
        } catch (IOException e) {
            System.out.println("Error reading student file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing balance data: " + e.getMessage());
        }
        return null;
    }

    private Strand loadStrand(String strandName) {
        String fileName = strandName + ".csv";
        return initializeStrands.loadStrands(fileName);
    }

    private void processPayment(Student student) {
        System.out.print("Please enter the amount of payment: ");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();

        if (paymentAmount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return;
        }

        double currentBalance = student.getBalance();
        double newBalance = currentBalance - paymentAmount;

        if (newBalance < 0) {
            System.out.println("Payment exceeds the current balance. Please enter a valid amount.");
            return;
        }

        // Update student data
        student.setBalance(newBalance);
        if (newBalance == 0) {
            student.setPaymentStatus("Fully Paid");
        }

        // Save only payment status and balance, keeping other information unchanged
        updatePaymentInfoInFile(student);

        generateReceipt(student, paymentAmount, newBalance);

        System.out.println("Payment processed successfully.");
        promptReturnToMenu();
    }

    private void updatePaymentInfoInFile(Student student) {
        String fileName = "student_" + student.getId() + ".csv";
        File file = new File(fileName);
        File tempFile = new File("temp_" + fileName); // Temporary file to hold the updated data

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            // Read the header line and write it to the temporary file
            if ((reader.readLine()) != null) {
                writer.println("ID,Name,Phone Number,Strand,Payment Status,Balance,Enrolled Subjects");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");

                // Only update payment status and balance, keeping other data unchanged
                studentData[4] = student.getPaymentStatus(); // Update Payment Status
                studentData[5] = String.valueOf(student.getBalance()); // Update Balance

                // Write the updated line to the temporary file
                writer.println(String.join(",", studentData));
            }

        } catch (IOException e) {
            System.out.println("Error updating student file: " + e.getMessage());
        }

        // Replace the original file with the updated temporary file
        if (!file.delete()) {
            System.out.println("Could not delete the original file");
        }
        if (!tempFile.renameTo(file)) {
            System.out.println("Could not rename the temporary file");
        }
    }

    private void generateReceipt(Student student, double paymentAmount, double newBalance) {
        System.out.println("\n--- Payment Receipt ---");
        System.out.printf("Student ID: %d\n", student.getId());
        System.out.printf("Name: %s\n", student.getName());
        System.out.printf("Amount Paid: %.2f\n", paymentAmount);
        System.out.printf("New Balance: %.2f\n", newBalance);
    }

    private void promptReturnToMenu() {
        System.out.print("Do you want to go back to the main menu? (y/n): ");
        char backChoice = scanner.next().charAt(0);
        scanner.nextLine();
        if (backChoice == 'y' || backChoice == 'Y') {
            UserType.user_type_menu();
        }
    }
}
