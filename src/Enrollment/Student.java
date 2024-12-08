package Enrollment;

//mga ibang class
import StudentDashboard.DashboardMenu;
import User_Accounting.Accounting;
import User_Types.UserType;
import Admin_Portal.RegistrarDashboard;
import User_Types.StudentMenu;
import Login.Login;
import Registrar.Register;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Student {
    // Constants
    public static final int MAX_SUBJECTS = 10; // Define MAX_SUBJECTS here

    // Fields
    private String name;
    private String phoneNumber;
    private Strand selectedStrand; // Assuming Strand is a separate class
    private String paymentStatus;
    private double balance;
    private int id;
    private List<Subject> enrolledSubjects = new ArrayList<>(); // Initialize the list

    // Constructor
    public Student(int id, String name, double balance, String phoneNumber, Strand selectedStrand, String paymentStatus) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.phoneNumber = phoneNumber;
        this.selectedStrand = selectedStrand;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Strand getSelectedStrand() {
        return selectedStrand;
    }

    public void setSelectedStrand(Strand selectedStrand) {
        this.selectedStrand = selectedStrand;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Add subject to enrolled subjects
    public void addSubject(Subject subject) {
        if (enrolledSubjects.size() < MAX_SUBJECTS) {
            enrolledSubjects.add(subject);
        } else {
            System.out.println("Cannot enroll in more than " + MAX_SUBJECTS + " subjects.");
        }
    }

    // Get the number of enrolled subjects
    public int getNumEnrolledSubjects() {
        return enrolledSubjects.size();
    }

    // Get the list of enrolled subjects
    public List<Subject> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public static class Strand {
        private final String name;
        private final List<Subject> subjects;

        public Strand(String name, List<Subject> subjects) {
            this.name = name;
            this.subjects = subjects;
        }

        public String getName() {
            return name;
        }

        public List<Subject> getSubjects() {
            return subjects;
        }

        public Strand(String name) {
            this.name = name;
            this.subjects = new ArrayList<>(); // Initialize with an empty list
        }
    }

    public static class Subject {
        private String subjectName;

        public Subject(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getSubjectName() {
            return subjectName;
        }
    }
    public static void pressAnyKey() {
        System.out.println("                                                                                        Press Enter to continue...");
        Scanner scanner = new Scanner(System.in); // Create a new Scanner for user input
        scanner.nextLine();  // Wait for the user to press Enter
    }
    public static void promptReturnToMenu(Scanner scanner) {
        while (true) {
            System.out.print("\n                                                                                        Do you want to go back to the main menu? (y/n): ");
            char backChoice = scanner.next().charAt(0);
            scanner.nextLine(); // Clear buffer

            if (backChoice == 'y' || backChoice == 'Y') {
                UserType.user_type_menu();
                return;
            } else if (backChoice == 'n' || backChoice == 'N') {
                System.out.println("\n                                                                                        Continuing...");
                return;
            } else {
                System.out.println("\n                                                                                        Invalid input! Please enter 'y' or 'n'.");
                pressAnyKey();
            }
        }
    }
    public static void PromptCancelToMenu(Scanner scanner, String adminName, Student[] students, int[] studentCount) {
        while (true) {
            System.out.print("                                                                                        Do you want to cancel and go back to the Admin Portal? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y")) {
                System.out.println("                                                                                        Returning to Admin Portal...\n");
                RegistrarDashboard.adminPortal(adminName, students, studentCount); // Return to Admin Portal
                return;
            } else if (response.equals("n")) {
                System.out.println("                                                                                        Continuing...\n");
                Accounting accounting = new Accounting(); // Create an instance of Accounting
                accounting.studentLogin();
            } else {
                System.out.println("\n                                                                                        Invalid input. Please enter 'y' or 'n'.\n");
            }
        }
    }


    public static void promptReturnToStudentMenu(Scanner scanner) {
        while (true) {
            System.out.print("\n                                                                                        Do you want to go back to the Student Menu? (y/n): ");
            char backChoice = scanner.next().charAt(0);
            scanner.nextLine(); // Clear buffer

            if (backChoice == 'y' || backChoice == 'Y') {
                StudentMenu.student_menu(); // Change this to the method that takes the user back to the Student Menu
                return;
            } else if (backChoice == 'n' || backChoice == 'N') {
                System.out.println("\n                                                                                        Continuing...");
                return;
            } else {
                System.out.println("\n                                                                                        Invalid input! Please enter 'y' or 'n'.");
                pressAnyKey();
            }
        }
    }
    public static void promptReturnBasedOnRole(Scanner scanner) {
        if ("Student".equals(Login.currentUserType)) {
            promptReturnToStudentMenu(scanner);
        } else if ("Admin".equals(Login.currentUserType)) {
            promptReturnToMenu(scanner);
        } else {
            System.out.println("\n⚠ User type not identified. Returning to the default menu.");
        }
    }

    public static void promptToGoBackOrRestartAccounting(String userType) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("                                                                                        Do you want to go back to the menu (y/n)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                // Check the current user type
                if ("Student".equals(userType)) {
                    StudentMenu.student_menu();
                } else if ("Admin".equals(userType)) {
                    UserType.user_type_menu();
                }
                return; // Exit the function after navigating back to the menu
            } else if (choice.equals("n")) {
                Accounting accounting = new Accounting();
                accounting.studentLogin(); // Restart the process
                return;
            } else {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                        Invalid input. Please enter 'y' or 'n'.");
                System.out.println("                                                                                        =======================================");
            }
        }
    }
    public static void promptToGoBackOrRestartDashboard(String userType, Student student) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("                                                                                        \nDo you want to go back to the menu (y/n)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                // Check the current user type
                if ("Student".equals(userType)) {
                    StudentMenu.student_menu();
                } else if ("Admin".equals(userType)) {
                    UserType.user_type_menu();
                }
                return; // Exit the function after navigating back to the menu
            } else if (choice.equals("n")) {
                DashboardMenu dashboardMenu = new DashboardMenu();
                dashboardMenu.showMenu(student);
                return;
            } else {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                        Invalid input. Please enter 'y' or 'n'.");
                System.out.println("                                                                                        =======================================");
            }
        }
    }

    public static void promptToGoBack(String userType) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("                                                                                        Do you want to go back to the menu (y/n)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                // Check the current user type
                  if ("Admin".equals(userType)) {
                    UserType.user_type_menu();
                }
                return; // Exit the function after navigating back to the menu
            } else if (choice.equals("n")) {
                Register register = new Register();
                register.showMaxAttemptsMenu(); // Restart the process
                return;
            } else {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                        Invalid input. Please enter 'y' or 'n'.");
                System.out.println("                                                                                        =======================================");
            }
        }
    }
}




