package User_Accounting;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import Enrollment.Student;
import Enrollment.InitializeStrands;
import Login.Login;
import User_Accounting.Accounting;

public class StudentAccounting {
    private static Scanner scanner = new Scanner(System.in);
    private static InitializeStrands initializeStrands = new InitializeStrands();

    public void loadStudentDashboard(int studentId) {
        Student student = getStudentDetails(studentId);
        while (true) {
            System.out.print("\n                                                                                        Do you want to make a payment? (y/n): ");
            char choice = scanner.next().charAt(0);
            scanner.nextLine();

            if (choice == 'n' || choice == 'N') {
                Student.promptToGoBackOrRestartAccounting(Login.currentUserType);
                return;
            } else if (choice == 'y' || choice == 'Y') {
                processPayment(student);
                break;
            } else {
                System.out.println("                                                                                        Invalid choice. Please enter 'y' or 'n'.");
            }
        }
    }

    private void processPayment(Student student) {
        double paymentAmount = 0;

        while (true) {
            try {
                System.out.print("                                                                                        Please enter the amount of payment: ");
                paymentAmount = scanner.nextDouble();
                scanner.nextLine(); // Clear the buffer

                if (paymentAmount <= 0) {
                    System.out.println("                                                                                        Invalid amount. Please enter a positive number.");
                    Student.pressAnyKey();
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("                                                                                        Invalid input! Please enter a numeric value.");
                Student.pressAnyKey();
                scanner.nextLine(); // Clear invalid input
            }
        }

        double currentBalance = student.getBalance();
        double newBalance = currentBalance - paymentAmount;

        if (newBalance < 0) {
            System.out.println("                                                                                        Payment exceeds the current balance. Please enter a valid amount.");
            Student.pressAnyKey();
            processPayment(student);
            return;
        }

        // Update student data
        student.setBalance(newBalance);
        if (newBalance == 0) {
            student.setPaymentStatus("                                                                                        Fully Paid");
        }

        // Save only payment status and balance, keeping other information unchanged
        updatePaymentInfoInFile(student);

        generateReceipt(student, paymentAmount, newBalance);

        System.out.println("                                                                                        Payment processed successfully.");
        Student.promptReturnBasedOnRole(scanner);
    }



    private Student getStudentDetails(int studentId) {
        String fileName = "student_" + studentId + ".csv";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("                                                                                        Student file not found.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read the header line
            reader.readLine();

            String line;
            if ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");
                int id = Integer.parseInt(studentData[0].trim());
                String name = studentData[1].trim();
                String phoneNumber = studentData[2].trim();
                String selectedStrandName = studentData[3].trim();
                String paymentStatus = studentData[4].trim();
                double balance = Double.parseDouble(studentData[5].trim());
                String enrolledSubjects = studentData.length > 6 ? studentData[6].trim() : "";
                String enrollmentStatus = studentData.length > 7 ? studentData[7].trim() : ""; // Enrollment Status from CSV
                String studentStatus = studentData.length > 8 ? studentData[8].trim() : ""; // Student Status from CSV

                // Load the strand with subjects
                Student.Strand selectedStrand = loadStrand(selectedStrandName);

                // Create the student object with the new parameters
                Student student = new Student(id, name, balance, phoneNumber, selectedStrand, paymentStatus, enrollmentStatus, studentStatus);

                // Load enrolled subjects if any
                if (!enrolledSubjects.isEmpty()) {
                    String[] subjects = enrolledSubjects.split(";");
                    for (String subjectName : subjects) {
                        student.addSubject(new Student.Subject(subjectName.trim()));
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

    private Student.Strand loadStrand(String strandName) {
        String fileName = strandName + ".csv";
        return initializeStrands.loadStrands(fileName);
    }

    private void updatePaymentInfoInFile(Student student) {
        String fileName = "student_" + student.getId() + ".csv";
        File file = new File(fileName);
        File tempFile = new File("temp_" + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            // Read and write the header
            String header = reader.readLine();
            if (header != null) {
                writer.println(header);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");
                studentData[4] = student.getPaymentStatus(); // Update Payment Status
                studentData[5] = String.valueOf(student.getBalance()); // Update Balance

                // Update EnrollmentStatus to "Enrolled" as soon as any payment is made
                studentData[7] = "Enrolled"; // Assuming EnrollmentStatus is the 8th column

                writer.println(String.join(",", studentData));
            }

        } catch (IOException e) {
            System.out.println("Error updating student file: " + e.getMessage());
        }

        // Replace the original file with the updated file
        if (!file.delete() || !tempFile.renameTo(file)) {
            System.out.println("Error replacing the file with updated information.");
        }
    }


    private void generateReceipt(Student student, double paymentAmount, double newBalance) {
        System.out.println("\n                                                                                        --- Payment Receipt ---");
        System.out.printf("                                                                                        Student ID: %d\n", student.getId());
        System.out.printf("                                                                                        Name: %s\n", student.getName());
        System.out.println("\nLAB_FEE = ₱5000\nPE_FEE = ₱5000\nIMMERSION_FEE = ₱5000\nLIBRARY_FEE = ₱5000\nWATER_ENERGY_FEE = ₱5000\n");
        System.out.printf("                                                                                        Amount Paid: %.2f\n", paymentAmount);
        System.out.printf("                                                                                        New Balance: %.2f\n", newBalance);
    }
}
