package Enrollment;

import java.util.ArrayList;
import java.util.List;

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
}