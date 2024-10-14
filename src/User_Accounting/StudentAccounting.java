package User_Accounting;

import java.io.*;
import java.util.Scanner;
import Enrollment.Student;
import User_Types.UserType;

public class StudentAccounting {
    private static Scanner scanner = new Scanner(System.in);

    public void loadStudentDashboard(int studentId) {
        Student student = getStudentDetails(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Display student information
        System.out.printf("Student ID: %d\n", student.getId());
        System.out.printf("Name: %s\n", student.getName());
        System.out.printf("Phone Number: %s\n", student.getPhoneNumber());
        System.out.printf("Strand: %s\n", student.getSelectedStrand());
        System.out.printf("Payment Status: %s\n", student.getPaymentStatus());
        System.out.printf("Remaining Balance: %.2f\n", student.getBalance());

        // Check payment status
        if (student.getBalance() == 0) {
            System.out.println("You are already fully paid.");
            promptReturnToMenu(); // Ask if user wants to return to the main menu
            return; // End the method if fully paid
        }

        while (true) {
            System.out.print("\nDo you want to make a payment? (y/n): ");
            char choice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline

            if (choice == 'n' || choice == 'N') {
                promptReturnToMenu(); // Ask if user wants to return to the main menu
                continue; // Loop back to ask if they want to make a payment
            } else if (choice == 'y' || choice == 'Y') {
                processPayment(student); // Process the payment
                break; // Exit the loop after payment processing
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
            reader.readLine(); // Skip header

            String line;
            if ((line = reader.readLine()) != null) {
                String[] studentData = line.split(","); // Use comma as the delimiter
                int id = Integer.parseInt(studentData[0].trim());
                String name = studentData[1].trim();
                String phoneNumber = studentData[2].trim();
                String selectedStrand = studentData[3].trim();
                String paymentStatus = studentData[4].trim();
                double balance = Double.parseDouble(studentData[5].trim());

                return new Student(id, name, balance, phoneNumber, selectedStrand, paymentStatus);
            }
        } catch (IOException e) {
            System.out.println("Error reading student file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing balance data: " + e.getMessage());
        }
        return null; // Return null if no student is found
    }

    private void processPayment(Student student) {
        System.out.print("Please enter the amount of payment: ");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (paymentAmount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return; // Exit the method if the payment amount is invalid
        }

        double currentBalance = student.getBalance();
        double newBalance = currentBalance - paymentAmount;

        if (newBalance < 0) {
            System.out.println("Payment exceeds the current balance. Please enter a valid amount.");
            return; // Exit the method if payment exceeds balance
        }

        // Update student data
        student.setBalance(newBalance);
        if (newBalance == 0) {
            student.setPaymentStatus("Fully Paid"); // Update payment status to "Fully Paid"
        }

        // Save the updated student information
        saveStudentToFile(student);
        generateReceipt(student, paymentAmount, newBalance); // Generate receipt

        System.out.println("Payment processed successfully.");
        promptReturnToMenu(); // Ask if user wants to return to the main menu
    }

    private void saveStudentToFile(Student student) {
        String fileName = "student_" + student.getId() + ".csv"; // Create a unique filename for each student
        StringBuilder updatedData = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read the header line
            String header = reader.readLine();
            updatedData.append(header).append(System.lineSeparator()); // Append header

            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(","); // Use comma as the delimiter
                int id = Integer.parseInt(studentData[0].trim());

                if (id == student.getId()) {
                    // Update the record with new balance and payment status
                    updatedData.append(String.format("%d,%s,%s,%s,%s,%.2f",
                            student.getId(),
                            student.getName(),
                            student.getPhoneNumber(),
                            student.getSelectedStrand(),
                            student.getPaymentStatus(),
                            student.getBalance()));

                    // Add the enrolled subjects line
                    updatedData.append(",Enrolled Subjects: ").append(getEnrolledSubjects(line)).append(System.lineSeparator());
                    updated = true;
                } else {
                    updatedData.append(line).append(System.lineSeparator()); // Keep original line
                }
            }

            if (!updated) {
                System.out.println("No matching student record found to update.");
            } else {
                // Overwrite the original file with updated data
                try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                    writer.print(updatedData.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving student file: " + e.getMessage());
        }
    }

    private String getEnrolledSubjects(String line) {
        // Extract enrolled subjects from the original line
        String[] parts = line.split(",Enrolled Subjects: "); // Split based on enrolled subjects
        if (parts.length > 1) {
            return parts[1].trim(); // Return subjects part
        }
        return ""; // Return empty if no subjects found
    }

    private void generateReceipt(Student student, double paymentAmount, double remainingBalance) {
        String receiptFileName = "receipt_" + student.getId() + ".csv"; // Create a unique filename for each receipt
        try (FileWriter fileWriter = new FileWriter(receiptFileName, true); // true to append to the file
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            // Check if the receipt file is empty and write header if necessary
            File receiptFile = new File(receiptFileName);
            if (receiptFile.length() == 0) {
                printWriter.printf("Student ID,Name,Amount Paid,Remaining Balance\n"); // Write header
            }
            printWriter.printf("%d,%s,%.2f,%.2f\n",
                    student.getId(),
                    student.getName(),
                    paymentAmount,
                    remainingBalance); // Write receipt data
        } catch (IOException e) {
            System.out.println("Error saving receipt file: " + e.getMessage());
        }
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
