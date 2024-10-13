package Enrollment;

public class Student {
    String name;
    String phoneNumber;
    String selectedStrand;
    String paymentStatus;
    int id;
    int balance;
    int numEnrolledSubjects;
    Enroll_Student.Subject[] enrolledSubjects; // Reference to Subject inside Enroll_Student

    public Student() {
        this.enrolledSubjects = new Enroll_Student.Subject[Enroll_Student.MAX_SUBJECTS]; // Access MAX_SUBJECTS
        this.numEnrolledSubjects = 0;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Enroll_Student.Strand getStrand() {
        return new Enroll_Student.Strand(selectedStrand, null); // Adjust as needed; currently returns a dummy Strand
    }
}
