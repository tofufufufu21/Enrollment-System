package Admin_Portal;

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
        try (FileWriter fileWriter = new FileWriter("students.csv", true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.printf("%d,%s,%s,%s,%s,%d,",
                    student.id,
                    student.name,
                    student.phoneNumber,
                    student.selectedStrand.getName(), // Get the name of the selected strand
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

    public void enrollStudent(Strand[] strands11, Strand[] strands12, Student[] students, int[] studentCount) {
        Scanner scanner = new Scanner(System.in);
        Student newStudent = new Student();
       // int grade;
        //ito yung original grade variable bago ako nag lagay sa baba.
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

        Strand selectedStrand = null;
        //start of my major changes, stop when these comments are gone
        //changes
        boolean validInput = false; // Flag to check valid input
        int grade = 0; /* Declare grade, comment out yung nasa taas, yun ang original sa taaas,
        di ko alam bat pinalagay ni chatgpt dito, kaya naka comment out lang sa taas*/


        //my attempt
        //loop until a valid choice is selected
        while (!validInput) {
            System.out.println("\nChoose strand for enrollment:");
            System.out.println("1. ABM 11\n2. STEM 11\n3. HUMSS 11\n4. GAS 11\n" +
                    "\n5. ABM 12\n6. STEM 12\n7. HUMSS 12\n8. GAS 12\n\n0. Cancel enrollment");

            System.out.print("\nEnter Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline


            switch (choice) {
                case '1':
                    selectedStrand = strands11[0]; // ABM 11
                    grade = 11;
                    validInput = true; // ito yung function na nag che-check kung valid ba yung character base sa strand
                    break;
                case '2':
                    selectedStrand = strands11[1]; // STEM 11
                    grade = 11;
                    validInput = true;
                    break;
                case '3':
                    selectedStrand = strands11[2]; // HUMSS 11
                    grade = 11;
                    validInput = true;
                    break;
                case '4':
                    selectedStrand = strands11[3]; // GAS 11
                    grade = 11;
                    validInput = true;
                    break;
                case '5':
                    selectedStrand = strands12[0]; // ABM 12
                    grade = 12;
                    validInput = true;
                    break;
                case '6':
                    selectedStrand = strands12[1]; // STEM 12
                    grade = 12;
                    validInput = true;
                    break;
                case '7':
                    selectedStrand = strands12[2]; // HUMSS 12
                    grade = 12;
                    validInput = true;
                    break;
                case '8':
                    selectedStrand = strands12[3]; // GAS 12
                    grade = 12;
                    validInput = true;
                    break;
                case '0':
                    return; // Cancel enrollment
                default:
                    System.out.println("Invalid strand, select from the available strands to enroll!");
                    //return;
                    //comment out return para mag work yung while loop
                    //it fucking worked nigga!

            }// for switch
        }// for while loop
        // Assign the selected strand to the new student
        newStudent.selectedStrand = selectedStrand;

        System.out.println("\n-----ENROLL TO AT LEAST ONE SUBJECT-----");
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

        // Check if the student enrolled in at least one subject
        if (newStudent.numEnrolledSubjects == 0) {
            System.out.println("--------------------");
            System.out.println("You didn't enroll in at least one subject. Your enrollment is cancelled, kupal.\n");
            return; // Cancel enrollment if no subjects were selected and don't save anything
        }

        do {
            System.out.printf("\nTotal Subject cost: %d\n", newStudent.numEnrolledSubjects * SUBJECT_COST);
            System.out.print("\nAre you sure you want to enroll? (y/n): ");
            confirmation = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline

            switch (confirmation) {
                case 'y':
                case 'Y':
                    System.out.println("------------------------");
                    System.out.println("\nEnrolling you now...\n\n");
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
}
