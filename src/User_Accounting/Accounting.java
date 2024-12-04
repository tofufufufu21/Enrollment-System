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
                System.out.println("\n\n\n\n\n");
                System.out.print("\n                                                                                        =======================================");
                System.out.print("\n                                                                                        Please enter your Student ID: ");
                studentId = Integer.parseInt(scanner.nextLine().trim()); // Parse input as integer
                validInput = true; // Exit loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                      Invalid input. Please enter a valid numeric Student ID.");
                System.out.println("                                                                                          =======================================");
                Student.pressAnyKey(); // Wait for user to press any key before retrying
            }
        }

        // Check if the student exists and get their details
        Student student = getStudentDetails(studentId);
        if (student == null) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                                  Student not found.");
            System.out.println("                                                                                        =======================================");
            Student.pressAnyKey();
            return;
        }

        // Check the payment status
        if (student.getBalance() == 0) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                             You are already fully paid.");
            System.out.println("                                                                                        =======================================");
            Student.promptReturnToMenu(scanner);
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
        System.out.println("\n                                                                                        =======================================");
        System.out.println("                                                                                        Student ID: " + student.getId());
        System.out.println("                                                                                        Name: " + student.getName());
        System.out.println("                                                                                        Phone Number: " + student.getPhoneNumber());
        System.out.println("                                                                                        Strand: " + student.getSelectedStrand().getName());
        System.out.println("                                                                                        Payment Status: " + student.getPaymentStatus());
        System.out.printf("                                                                                        Remaining Balance: %.2f%n", student.getBalance());
        System.out.println("                                                                                        =======================================");
    }

    private Student getStudentDetails(int studentId) {
        String fileName = "student_" + studentId + ".csv";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                                Student file not found.");
            System.out.println("                                                                                        =======================================");
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
            System.out.println("\n=======================================");
            System.out.println("Error reading student file: " + e.getMessage());
            System.out.println("=======================================");
        } catch (NumberFormatException e) {
            System.out.println("\n=======================================");
            System.out.println("Error parsing balance data: " + e.getMessage());
            System.out.println("=======================================");
        }
        return null;
    }

    private List<Student.Subject> loadSubjectsForStrand(String strandName) {
        List<Student.Subject> subjects = new ArrayList<>();
        String fileName = strandName + ".csv";

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("\n=======================================");
            System.out.println("Strand file not found: " + fileName);
            System.out.println("=======================================");
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
            System.out.println("\n=======================================");
            System.out.println("Error reading strand file: " + e.getMessage());
            System.out.println("=======================================");
        }
        return subjects;
    }
}
