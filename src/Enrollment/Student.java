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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSelectedStrand() {
        return selectedStrand;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public int getBalance() {
        return balance;
    }

    public Enroll_Student.Strand getStrand() {
        return new Enroll_Student.Strand(selectedStrand, null); // Adjust as needed; currently returns a dummy Strand
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSelectedStrand(String selectedStrand) {
        this.selectedStrand = selectedStrand;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
