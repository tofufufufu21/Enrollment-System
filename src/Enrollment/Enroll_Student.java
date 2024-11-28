package Enrollment;

import java.sql.SQLOutput;
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
    private static final int LAB_FEE = 5000; // Example fee values
    private static final int PE_FEE = 5000;
    private static final int IMMERSION_FEE = 5000;
    private static final int LIBRARY_FEE = 5000;
    private static final int WATER_ENERGY_FEE = 5000;

    public void enrollStudent(List<Student.Strand> strands, Student[] students, int[] studentCount) {
        Scanner scanner = new Scanner(System.in);
        char confirmation;
        int lastId = loadLastUsedId();

        System.out.println("ENROLL A NEW STUDENT\n");

        String name;
        while (true) {
            System.out.print("Enter Name: ");
            name = scanner.nextLine();
            if (name.matches(".*\\d.*")) {
                System.out.println("Warning: Numbers are not allowed in the name. Press Enter to try again...");
                scanner.nextLine();
            } else {
                break;
            }
        }

        // Phone number validation loop
        String phoneNumber;
        while (true) {
            System.out.print("Phone Number: ");
            phoneNumber = scanner.nextLine();
            if (!phoneNumber.matches("\\d{11}")) {
                System.out.println("Warning: Phone number must be 11 digits and contain only numbers. Press Enter to try again...");
                scanner.nextLine();
            } else {
                break;
            }
        }

        // Set a default balance (adjust as needed)
        double balance = 0;

        System.out.println("\nChoose strand for enrollment:");
        System.out.println("""
                1. ABM 11
                2. STEM 11
                3. HUMSS 11
                4. GAS 11
                
                5. ABM 12
                6. STEM 12
                7. HUMSS 12
                8. GAS 12
                0. Cancel enrollment""");

        char choice;
        while (true) {
            System.out.print("\nEnter Choice (or '0' to cancel and return): ");
            String input = scanner.nextLine().trim(); // Read the entire input and trim whitespace

            if (input.equals("0")) {
                System.out.println("Enrollment canceled. Returning...\n");
                return; // Exit the method or loop
            }

            if (input.length() == 1 && Character.isDigit(input.charAt(0))) {
                choice = input.charAt(0); // Get the first character if it's a valid digit
                break; // Exit the loop if valid
            }

            // Print invalid message without re-prompting
            System.out.println("Invalid choice. Please enter a valid number or '0' to cancel.");
        }


        // Select the strand based on the choice
        String fileName = switch (choice) {
            case '1' -> "ABM_11.csv";
            case '2' -> "STEM_11.csv";
            case '3' -> "HUMSS_11.csv";
            case '4' -> "GAS_11.csv";
            case '5' -> "ABM_12.csv";
            case '6' -> "STEM_12.csv";
            case '7' -> "HUMSS_12.csv";
            case '8' -> "GAS_12.csv";
            case '0' -> {
                System.out.println("Enrollment canceled. Returning to Admin Portal...");
                yield null; // Yielding null for cancellation
            }
            default -> {
                System.out.println("Invalid choice.");
                yield null; // Yielding null for invalid choice
            }
        };

        if (fileName != null) {
            // Proceed with enrollment
            System.out.println("\n-----ENROLL TO AT LEAST ONE SUBJECT-----");
            System.out.println("1000 Pesos per subject");

            // Attempt to read the correct file for subjects
            List<Student.Subject> subjects = loadSubjectsFromCSV(fileName);

            // Check if subjects were loaded
            if (subjects.isEmpty()) {
                System.out.println("No subjects found for the selected strand. Enrollment cancelled.");
                return;
            }

            // Set the selected strand in the newStudent
            Student.Strand selectedStrand = strands.get(choice - '1'); // Adjusting to index (0-based)

            // Create the Student object with the collected data
            Student newStudent = new Student(lastId + 1, name, balance, phoneNumber, selectedStrand, "Unpaid");



            // Display available subjects for the selected strand and ask for enrollment
            for (Student.Subject subject : subjects) {
                boolean validInput = false;
                while (!validInput) {
                    System.out.printf("Do you want to enroll in \"%s\"? (y/n): ", subject.getSubjectName());
                    char enroll = scanner.next().charAt(0);
                    scanner.nextLine();  // Consume the newline

                    if (enroll == 'y' || enroll == 'Y') {
                        newStudent.addSubject(subject); // Make sure this method is defined in the Student class
                        validInput = true;
                    } else if (enroll == 'n' || enroll == 'N') {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
                    }
                }
            }


            if (newStudent.getNumEnrolledSubjects() == 0) {
                System.out.println("You didn't enroll in at least one subject. Enrollment cancelled.");
                return;
            }

            // Calculate the total cost including all applicable fees
            double totalCost = newStudent.getNumEnrolledSubjects() * SUBJECT_COST + LAB_FEE + PE_FEE + IMMERSION_FEE + LIBRARY_FEE + WATER_ENERGY_FEE;

            do {
                System.out.printf("\nTotal Subject cost (including additional fees): %.2f\n", totalCost);
                System.out.println("\nLAB_FEE = 5000₱\nPE_FEE = 5000₱\nIMMERSION_FEE = 5000₱\nLIBRARY_FEE = 5000₱\nWATER_ENERGY_FEE = 5000₱\n");
                // Display enrolled subjects
                System.out.println("Subjects you have enrolled in:");
                for (Student.Subject subject : newStudent.getEnrolledSubjects()) {
                    System.out.println(" - " + subject.getSubjectName());
                }

                System.out.print("\nAre you sure you want to enroll? (y/n): ");
                confirmation = scanner.next().charAt(0);
                scanner.nextLine();

                if (confirmation == 'y' || confirmation == 'Y') {
                    System.out.println("Enrolling you now...\n");
                    break;
                } else if (confirmation == 'n' || confirmation == 'N') {
                    System.out.println("Enrollment cancelled.\n");
                    return;
                } else {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            } while (true);

            newStudent.setBalance(totalCost); // Updated balance calculation

            students[studentCount[0]] = newStudent;
            studentCount[0]++;

            saveStudentToFile(newStudent, fileName);
            saveNewId(lastId + 1); // Save the updated last used ID here

        }
    }

    private int loadLastUsedId() {
        File file = new File("last_id.txt");

        if (!file.exists()) {
            return 0; // Start from ID 0 if file does not exist
        }

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }

        return 0; // Return 0 if unable to read the file
    }

    private void saveNewId(int newId) {
        try (FileWriter fileWriter = new FileWriter("last_id.txt", false)) { // false to overwrite the file
            fileWriter.write(String.valueOf(newId));
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
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
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip the header line
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    // Extract only the subject name
                    String subjectName = line.split(",")[0].trim(); // Get only the subject name
                    subjects.add(new Student.Subject(subjectName)); // Assuming Subject class takes a string for the name
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading subjects from file: " + fileName);
        }
        return subjects;
    }

}

