package Enrollment;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Enroll_Student {

    private static final int SUBJECT_COST = 1000; // Cost per subject
    private static final int LAB_FEE = 5000;
    private static final int PE_FEE = 5000;
    private static final int IMMERSION_FEE = 5000;
    private static final int LIBRARY_FEE = 5000;
    private static final int WATER_ENERGY_FEE = 5000;

    public void enrollStudent(List<Student.Strand> strands, Student[] students, int[] studentCount) {
        Scanner scanner = new Scanner(System.in);
        char confirmation;
        int lastId = loadLastUsedId();

        System.out.println("\n\n                                                                                        ==================================");
        System.out.println("                                                                                               ENROLL A NEW STUDENT       ");
        System.out.println("                                                                                        ==================================\n\n");

        String name;
        while (true) {
            System.out.println("                                                                                        -----------------------------------");
            System.out.println("                                                                                         Please Enter the Student's Name   ");
            System.out.println("                                                                                        -----------------------------------");
            System.out.print("                                                                                               -> ");
            name = scanner.nextLine();
            if (name.matches(".*\\d.*")) {
                System.out.println("\n                                                                                      ----------------------------------------");
                System.out.println("                                                                                         WARNING: Names cannot contain numbers! ");
                System.out.println("                                                                                                Please try again.               ");
                System.out.println("                                                                                      ----------------------------------------\n");
            } else {
                break;
            }
        }

        String phoneNumber;
        while (true) {
            System.out.println("\n                                                                                         -------------------------------------");
            System.out.println("                                                                                        Please Enter the Student's Phone Number ");
            System.out.println("                                                                                                (11 digits only)                ");
            System.out.println("                                                                                        -------------------------------------");
            System.out.print("                                                                                                -> ");
            phoneNumber = scanner.nextLine();
            if (!phoneNumber.matches("\\d{11}")) {
                System.out.println("\n                                                                                       ----------------------------------------");
                System.out.println("                                                                                         WARNING: Phone number must be 11 digits ");
                System.out.println("                                                                                                and contain only numbers.        ");
                System.out.println("                                                                                       ---------------------------------------\n");
            } else {
                break;
            }
        }

        double balance = 0;

        System.out.println("                                                                                          ====================================");
        System.out.println("                                                                                                    SELECT A STRAND           ");
        System.out.println("                                                                                          ==================================\n");
        System.out.println("                                                                                                   1. ABM 11");
        System.out.println("                                                                                                   2. STEM 11");
        System.out.println("                                                                                                   3. HUMSS 11");
        System.out.println("                                                                                                   4. GAS 11\n");
        System.out.println("                                                                                                   5. ABM 12");
        System.out.println("                                                                                                   6. STEM 12");
        System.out.println("                                                                                                   7. HUMSS 12");
        System.out.println("                                                                                                   8. GAS 12\n");
        System.out.println("                                                                                                   0. Cancel enrollment");
        System.out.println("                                                                                           ==================================\n");

        char choice;
        while (true) {
            System.out.print("                                                                                               Enter Choice (0-8): ");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                System.out.println("                                                                                        \n----------------------------------------");
                System.out.println("                                                                                          Enrollment canceled. Returning to menu. ");
                System.out.println("                                                                                        ----------------------------------------\n");
                return;
            }
            if (input.length() == 1 && Character.isDigit(input.charAt(0))) {
                choice = input.charAt(0);
                break;
            }
            System.out.println("\n                                                                                        ----------------------------------------");
            System.out.println("                                                                                         Invalid choice! Please enter a valid number.");
            System.out.println("                                                                                         ----------------------------------------\n");
        }

        String fileName = switch (choice) {
            case '1' -> "ABM_11.csv";
            case '2' -> "STEM_11.csv";
            case '3' -> "HUMSS_11.csv";
            case '4' -> "GAS_11.csv";
            case '5' -> "ABM_12.csv";
            case '6' -> "STEM_12.csv";
            case '7' -> "HUMSS_12.csv";
            case '8' -> "GAS_12.csv";
            default -> null;
        };

        if (fileName != null) {
            System.out.println("\n\n                                                                                        -----ENROLL TO AT LEAST ONE SUBJECT-----");
            System.out.println("                                                                                              1000 Pesos per subject\n");

            List<Student.Subject> subjects = loadSubjectsFromCSV(fileName);
            if (subjects.isEmpty()) {
                System.out.println("                                                                                        No subjects found for the selected strand. Enrollment cancelled.");
                return;
            }

            Student.Strand selectedStrand = strands.get(choice - '1');
            Student newStudent = new Student(lastId + 1, name, balance, phoneNumber, selectedStrand, "Unpaid");

            for (Student.Subject subject : subjects) {
                boolean validInput = false;
                while (!validInput) {
                    System.out.printf("                                                                                Do you want to enroll in \"%s\"? (y/n): ", subject.getSubjectName());
                    char enroll = scanner.next().charAt(0);
                    scanner.nextLine();
                    if (enroll == 'y' || enroll == 'Y') {
                        newStudent.addSubject(subject);
                        validInput = true;
                    } else if (enroll == 'n' || enroll == 'N') {
                        validInput = true;
                    } else {
                        System.out.println("                                                                                   Invalid input. Please enter 'y' for yes or 'n' for no.");
                    }
                }
            }

            if (newStudent.getNumEnrolledSubjects() == 0) {
                System.out.println("                                                                                        You didn't enroll in at least one subject. Enrollment cancelled.");
                return;
            }

            double totalCost = newStudent.getNumEnrolledSubjects() * SUBJECT_COST + LAB_FEE + PE_FEE + IMMERSION_FEE + LIBRARY_FEE + WATER_ENERGY_FEE;

            do {
                System.out.printf(" \nTotal Subject cost (including additional fees): %.2f\n", totalCost);
                System.out.println("\nLAB_FEE = ₱5000\nPE_FEE = ₱5000\nIMMERSION_FEE = ₱5000\nLIBRARY_FEE = ₱5000\nWATER_ENERGY_FEE = ₱5000\n");
                System.out.println("Subjects you have enrolled in:");
                for (Student.Subject subject : newStudent.getEnrolledSubjects()) {
                    System.out.println(" - " + subject.getSubjectName());
                }

                System.out.print("\n                                                                                        Are you sure you want to enroll? (y/n): ");
                confirmation = scanner.next().charAt(0);
                scanner.nextLine();

                if (confirmation == 'y' || confirmation == 'Y') {
                    System.out.println("                                                                                        Enrolling you now...\n");
                    break;
                } else if (confirmation == 'n' || confirmation == 'N') {
                    System.out.println("                                                                                        Enrollment cancelled.\n");
                    return;
                } else {
                    System.out.println("                                                                                        Invalid input. Please enter 'y' or 'n'.");
                }
            } while (true);

            newStudent.setBalance(totalCost);
            students[studentCount[0]] = newStudent;
            studentCount[0]++;
            saveStudentToFile(newStudent, fileName);
            saveNewId(lastId + 1);
        }
    }

    private int loadLastUsedId() {
        File file = new File("last_id.txt");
        if (!file.exists()) {
            return 0;
        }
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.println("                                                                                        Error opening file: " + e.getMessage());
        }
        return 0;
    }

    private void saveNewId(int newId) {
        try (FileWriter fileWriter = new FileWriter("last_id.txt", false)) {
            fileWriter.write(String.valueOf(newId));
        } catch (IOException e) {
            System.out.println("                                                                                        Error opening file: " + e.getMessage());
        }
    }

    private void saveStudentToFile(Student student, String selectedStrandFileName) {
        String fileName = "student_" + student.getId() + ".csv"; // Create a unique filename for each student
        try (FileWriter fileWriter = new FileWriter(fileName, false); // false to overwrite the file
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Write header
            printWriter.printf("ID,Name,Phone Number,Strand,Payment Status,Balance,Enrolled Subjects\n");

            // Prepare the enrolled subjects string
            StringBuilder subjectsString = new StringBuilder();
            for (Student.Subject subject : student.getEnrolledSubjects()) {
                if (subject != null) { // Check for null to avoid NullPointerException
                    subjectsString.append(subject.getSubjectName()).append("; "); // Separate subjects with a semicolon
                }
            }

            // Remove the last semicolon and space if any subjects were added
            if (!subjectsString.isEmpty()) {
                subjectsString.setLength(subjectsString.length() - 2); // Remove the last "; "
            }

            // Remove the ".csv" extension from the selected strand file name
            String strandNameOnly = selectedStrandFileName.replace(".csv", ""); // Exclude the CSV extension

            // Write student information along with enrolled subjects
            printWriter.printf("%d,%s,%s,%s,%s,%.2f,%s\n",
                    student.getId(),
                    student.getName(),
                    student.getPhoneNumber(),
                    strandNameOnly, // Use the strand name without the .csv extension
                    student.getPaymentStatus(),
                    student.getBalance(),
                    subjectsString.toString()); // Add enrolled subjects here

        } catch (IOException e) {
            System.out.println("Error saving student to file: " + e.getMessage());
        }
    }

    private List<Student.Subject> loadSubjectsFromCSV(String fileName) {
        List<Student.Subject> subjects = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                subjects.add(new Student.Subject(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("                                                                                        Error loading file " + fileName + ": " + e.getMessage());
        }
        return subjects;
    }
}
