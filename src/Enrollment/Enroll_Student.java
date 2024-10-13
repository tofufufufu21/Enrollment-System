package Enrollment;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Enroll_Student {
    public static final int MAX_SUBJECTS = 10; // Adjust as needed
    private static final int SUBJECT_COST = 1000; // Cost per subject
    private static final int LAB_FEE = 5000; // Example fee values
    private static final int PE_FEE = 5000;
    private static final int IMMERSION_FEE = 5000;
    private static final int LIBRARY_FEE = 5000;
    private static final int WATER_ENERGY_FEE = 5000;

    public static class Subject {
        String name;

        public Subject(String name) {
            this.name = name;
        }
    }

    public static class Strand {
        String name;
        Subject[] subjects;
        int numSubjects;

        public Strand(String name, Subject[] subjects) {
            this.name = name;
            this.subjects = subjects;
            this.numSubjects = subjects.length;
        }

        // Getter for name
        public String getName() {
            return name; // Return the name of the strand
        }
    }

    public static class Student {
        String name;
        String phoneNumber;
        Strand selectedStrand;
        String paymentStatus;
        int id;
        int balance;
        int numEnrolledSubjects;
        Subject[] enrolledSubjects;

        public Student() {
            this.enrolledSubjects = new Subject[MAX_SUBJECTS];
            this.numEnrolledSubjects = 0;
        }

        // Getter methods
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        // Return the selected Strand without creating a new one
        public Strand getStrand() {
            return selectedStrand; // Return the actual selected Strand object
        }
    }

    public void enrollStudent(Strand[] strands11, Strand[] strands12, Student[] students, int[] studentCount) {
        Scanner scanner = new Scanner(System.in);
        Student newStudent = new Student();
        int grade;
        char confirmation, choice;
        int lastId = loadLastUsedId();

        strands11 = new Strand[4]; // Assuming 4 strands for grade 11
        strands12 = new Strand[4]; // Assuming 4 strands for grade 12
        InitializeStrands.initializeStrands(strands11, strands12);

        System.out.println("ENROLL A NEW STUDENT\n");

        System.out.print("Enter Name: ");
        newStudent.name = scanner.nextLine();

        System.out.print("Phone Number: ");
        newStudent.phoneNumber = scanner.nextLine();

        System.out.println("\nChoose strand for enrollment:");
        System.out.println("1. ABM 11\n2. STEM 11\n3. HUMSS 11\n4. GAS 11\n" +
                "\n5. ABM 12\n6. STEM 12\n7. HUMSS 12\n8. GAS 12\n\n0. Cancel enrollment");

        System.out.print("\nEnter Choice: ");
        choice = scanner.next().charAt(0);
        scanner.nextLine(); // Consume newline

        Strand selectedStrand = null;

        switch (choice) {
            case '1': selectedStrand = strands11[0]; grade = 11; break; // ABM 11
            case '2': selectedStrand = strands11[1]; grade = 11; break; // STEM 11
            case '3': selectedStrand = strands11[2]; grade = 11; break; // HUMSS 11
            case '4': selectedStrand = strands11[3]; grade = 11; break; // GAS 11
            case '5': selectedStrand = strands12[0]; grade = 12; break; // ABM 12
            case '6': selectedStrand = strands12[1]; grade = 12; break; // STEM 12
            case '7': selectedStrand = strands12[2]; grade = 12; break; // HUMSS 12
            case '8': selectedStrand = strands12[3]; grade = 12; break; // GAS 12
            case '0':
                System.out.println("Enrollment canceled. Returning to Admin Portal...");
                return; // Cancel enrollment
            default:
                System.out.println("Invalid choice.");
                return;
        }

        // Assign the selected strand to the new student
        newStudent.selectedStrand = selectedStrand;
        System.out.println("\n-----ENROLL TO AT LEAST ONE SUBJECT-----");
        System.out.println("\n1000 Pesos per subject");

        if (selectedStrand != null) {
            for (int i = 0; i < selectedStrand.numSubjects; i++) {
                while (true) { // Loop until the user inputs 'y' or 'n'
                    System.out.printf("\nDo you want to enroll in %s? (y/n): ", selectedStrand.subjects[i].name);
                    char enroll = scanner.next().charAt(0);
                    scanner.nextLine(); // Consume newline

                    if (enroll == 'y' || enroll == 'Y') {
                        addSubject(newStudent, selectedStrand.subjects[i]);
                        break; // Valid input, break the loop
                    } else if (enroll == 'n' || enroll == 'N') {
                        break; // Valid input, break the loop
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
                }
            }
        } else {
            System.out.println("Invalid strand or grade.");
            return;
        }

        // Check if the student enrolled in at least one subject
        if (newStudent.numEnrolledSubjects == 0) {
            System.out.println("You didn't enroll in at least one subject. Enrollment cancelled.");
            return; // Cancel enrollment if no subjects were selected and don't save anything
        }

        do {
            System.out.printf("\nTotal Subject cost: %d\n", newStudent.numEnrolledSubjects * SUBJECT_COST);
            System.out.print("\nAre you sure you want to enroll? (y/n): ");
            confirmation = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline

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

        newStudent.paymentStatus = "Unpaid";
        newStudent.id = lastId + 1; // Assign new student ID
        saveNewId(newStudent.id); // Save new ID to file

        // Calculate total balance
        newStudent.balance = SUBJECT_COST * newStudent.numEnrolledSubjects + LAB_FEE + PE_FEE + IMMERSION_FEE + LIBRARY_FEE + WATER_ENERGY_FEE;

        students[studentCount[0]] = newStudent; // Add the new student to the array
        studentCount[0]++;

        saveStudentToFile(newStudent); // Save student details to a separate file

        System.out.printf("Welcome Student %s\nThis is your student ID: %d\n", newStudent.name, newStudent.id);
        System.out.printf("Total Subject cost: %d\n", newStudent.numEnrolledSubjects * SUBJECT_COST);
        System.out.printf("Total Cost: %d\nRemaining balance: %d\n", newStudent.balance, newStudent.balance);

        System.out.println("Please proceed to accounting");
    }

    private void addSubject(Student student, Subject subject) {
        if (student.numEnrolledSubjects < MAX_SUBJECTS) {
            student.enrolledSubjects[student.numEnrolledSubjects++] = subject;
        } else {
            System.out.println("Maximum subjects reached.");
        }
    }

    private int loadLastUsedId() {
        File file = new File("last_id.txt");

        if (!file.exists()) {
            return 0; // Start from ID 1 if file does not exist
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

    private void saveStudentToFile(Student student) {
        String fileName = "student_" + student.id + ".csv"; // Create a unique filename for each student
        try (FileWriter fileWriter = new FileWriter(fileName, false); // false to overwrite the file
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.printf("ID,Name,Phone Number,Strand,Payment Status,Balance\n"); // Write header
            printWriter.printf("%d,%s,%s,%s,%s,%d",
                    student.id,
                    student.name,
                    student.phoneNumber,
                    student.selectedStrand.getName(), // Get the name of the selected strand
                    student.paymentStatus,
                    student.balance);

            // Write enrolled subjects
            printWriter.printf(",Enrolled Subjects: ");
            for (int i = 0; i < student.numEnrolledSubjects; i++) {
                printWriter.printf("%s", student.enrolledSubjects[i].name);
                if (i < student.numEnrolledSubjects - 1) {
                    printWriter.printf(", "); // Separate subjects with a comma
                }
            }
            printWriter.println();
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }
}
