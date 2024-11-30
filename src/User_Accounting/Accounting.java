package User_Accounting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Enrollment.Student;
import User_Types.UserType;


public class Accounting {
    private static Scanner scanner = new Scanner(System.in);

    public void studentLogin() {
        int studentId = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Please enter your Student ID: ");
                studentId = Integer.parseInt(scanner.nextLine().trim()); // Parse input as integer
                validInput = true; // Exit loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid numeric Student ID.");
                Student.pressAnyKey(); // Wait for user to press any key before retrying
            }
        }

        // Check if the student exists and get their details
        Student student = getStudentDetails(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            Student.pressAnyKey();
            return;
        }

        // Check the payment status
        if (student.getBalance() == 0) {
            System.out.println("You are already fully paid.");
            promptReturnToMenu();
            return;
        } else {
            // Print student information
            displayStudentInfo(student);

            // Direct to StudentAccounting for payment processing
            StudentAccounting studentAccounting = new StudentAccounting();
            studentAccounting.loadStudentDashboard(studentId);
        }
    }

    private void displayStudentInfo(Student student) {
        System.out.println("Student ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Phone Number: " + student.getPhoneNumber());
        // Correctly display the strand name
        System.out.println("Strand: " + student.getSelectedStrand().getName());
        System.out.println("Payment Status: " + student.getPaymentStatus());
        System.out.printf("Remaining Balance: %.2f%n", student.getBalance());
    }

    private Student getStudentDetails(int studentId) {
        String fileName = "student_" + studentId + ".csv";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Student file not found.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip header

            String line;
            if ((line = reader.readLine()) != null) {
                String[] studentData = line.split(","); // Use comma as the delimiter
                int id = Integer.parseInt(studentData[0].trim());
                String name = studentData[1].trim();
                String phoneNumber = studentData[2].trim();
                String selectedStrandName = studentData[3].trim(); // Get strand name
                String paymentStatus = studentData[4].trim();
                double balance = Double.parseDouble(studentData[5].trim());

                // tinanggal ko na yung debugging statement dito


                // Load the subjects for the selected strand
                List<Student.Subject> subjects = loadSubjectsForStrand(selectedStrandName);

                // Create the Strand object
                Student.Strand strand = new Student.Strand(selectedStrandName, subjects);

                // Create the Student object
                Student student = new Student(id, name, balance, phoneNumber, strand, paymentStatus);

                // Get enrolled subjects as a list
                if (studentData.length > 6) { // Check if there are enrolled subjects
                    String[] enrolledSubjects = studentData[6].trim().split(";");
                    for (String subjectName : enrolledSubjects) {
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

    private List<Student.Subject> loadSubjectsForStrand(String strandName) {
        List<Student.Subject> subjects = new ArrayList<>();
        String fileName = strandName + ".csv";

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Strand file not found: " + fileName);
            return subjects;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip header

            String line;
            while ((line = reader.readLine()) != null) {
                String subjectName = line.trim();
                subjects.add(new Student.Subject(subjectName));
            }
        } catch (IOException e) {
            System.out.println("Error reading strand file: " + e.getMessage());
        }
        return subjects;
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