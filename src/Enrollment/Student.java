package Enrollment;

public class Student {
    String name;
    String phoneNumber;
    String selectedStrand;
    String paymentStatus;
    int id;
    double balance; // Change to double
    int numEnrolledSubjects;
    Enroll_Student.Subject[] enrolledSubjects; // Reference to Subject inside Enroll_Student

    public Student(int id, String name, double balance, String phoneNumber, String selectedStrand, String paymentStatus) {
        this.id = id;
        this.name = name;
        this.balance = balance; // Correctly assigning double
        this.phoneNumber = phoneNumber;
        this.selectedStrand = selectedStrand;
        this.paymentStatus = paymentStatus;
        this.enrolledSubjects = new Enroll_Student.Subject[Enroll_Student.MAX_SUBJECTS];
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

    public double getBalance() { // Change return type to double
        return balance;
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

    public void setBalance(double balance) { // Change parameter type to double
        this.balance = balance;
    }
}
