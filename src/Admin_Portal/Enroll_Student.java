package Admin_Portal;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Enroll_Student {
    private static final int MAX_SUBJECTS = 10; // Adjust as needed
    private static final int SUBJECT_COST = 1000; // Cost per subject
    private static final int LAB_FEE = 5000; // Example fee values
    private static final int PE_FEE = 5000;
    private static final int IMMERSION_FEE = 5000;
    private static final int LIBRARY_FEE = 5000;
    private static final int WATER_ENERGY_FEE = 5000;

    // Change access modifier to public
    public static class Subject {
        String name;

        public Subject(String name) {
            this.name = name;
        }
    }

    // Change access modifier to public
    public static class Strand {
        String name;
        Subject[] subjects;
        int numSubjects;

        public Strand(String name, Subject[] subjects) {
            this.name = name;
            this.subjects = subjects;
            this.numSubjects = subjects.length;
        }
    }

    // Change access modifier to public
    public static class Student {
        String name;
        String phoneNumber;
        String selectedStrand;
        String paymentStatus;
        int id;
        int balance;
        int numEnrolledSubjects;
        Subject[] enrolledSubjects;

        public Student() {
            this.enrolledSubjects = new Subject[MAX_SUBJECTS];
            this.numEnrolledSubjects = 0;
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
            case '1':
                selectedStrand = strands11[0]; // ABM 11
                grade = 11;
                break;
            case '2':
                selectedStrand = strands11[1]; // STEM 11
                grade = 11;
                break;
            case '3':
                selectedStrand = strands11[2]; // HUMSS 11
                grade = 11;
                break;
            case '4':
                selectedStrand = strands11[3]; // GAS 11
                grade = 11;
                break;
            case '5':
                selectedStrand = strands12[0]; // ABM 12
                grade = 12;
                break;
            case '6':
                selectedStrand = strands12[1]; // STEM 12
                grade = 12;
                break;
            case '7':
                selectedStrand = strands12[2]; // HUMSS 12
                grade = 12;
                break;
            case '8':
                selectedStrand = strands12[3]; // GAS 12
                grade = 12;
                break;
            case '0':
                // Handle cancellation if needed
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("\n1000 Pesos per subject");

        if (selectedStrand != null) {
            for (int i = 0; i < selectedStrand.numSubjects; i++) {
                System.out.printf("\nDo you want to enroll in %s? (y/n): ", selectedStrand.subjects[i].name);
                char enroll = scanner.next().charAt(0);
                scanner.nextLine(); // Consume newline
                if (enroll == 'y' || enroll == 'Y') {
                    addSubject(newStudent, selectedStrand.subjects[i]);
                }
            }
        } else {
            System.out.println("Invalid strand or grade.");
            return;
        }

        do {
            System.out.printf("\nTotal Subject cost: %d\n", newStudent.numEnrolledSubjects * SUBJECT_COST);
            System.out.print("\nAre you sure you want to enroll? (y/n): ");
            confirmation = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline

            switch (confirmation) {
                case 'y':
                case 'Y':
                    System.out.println("Enrolling you now...\n");
                    break;
                case 'n':
                case 'N':
                    System.out.println("Enrollment cancelled.\n");
                    return;
                default:
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        } while (confirmation != 'y' && confirmation != 'Y');

        newStudent.paymentStatus = "Unpaid";
        newStudent.id = lastId + 1; // Assign new student ID
        saveNewId(newStudent.id); // Save new ID to file

        // Calculate total balance
        newStudent.balance = SUBJECT_COST * newStudent.numEnrolledSubjects + LAB_FEE + PE_FEE + IMMERSION_FEE + LIBRARY_FEE + WATER_ENERGY_FEE;

        students[studentCount[0]] = newStudent; // Add the new student to the array
        studentCount[0]++;

        saveStudentToFile(newStudent); // Save student details to file

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
        // Implement your method to load last used ID
        return 0; // Placeholder
    }

    private void saveNewId(int id) {
        // Implement your method to save new ID
    }

    private void saveStudentToFile(Student student) {
        try (FileWriter fileWriter = new FileWriter("students.csv", true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.printf("%d,%s,%s,%s,%s,%d,",
                    student.id,
                    student.name,
                    student.phoneNumber,
                    student.selectedStrand,
                    student.paymentStatus,
                    student.balance);

            for (int i = 0; i < student.numEnrolledSubjects; i++) {
                printWriter.printf("%s,", student.enrolledSubjects[i].name);
            }

            printWriter.println();
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }
}
