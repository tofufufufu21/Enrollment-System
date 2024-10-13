package Enrollment;

public class InitializeStrands {

    public static void initializeStrands(Enroll_Student.Strand[] strands11, Enroll_Student.Strand[] strands12) {
        // Initialize Grade 11 strands and subjects
        Enroll_Student.Subject[] stem11Subjects = {
                new Enroll_Student.Subject("General Mathematics"),
                new Enroll_Student.Subject("Earth Science"),
                new Enroll_Student.Subject("Physical Education"),
                new Enroll_Student.Subject("ICT"),
                new Enroll_Student.Subject("Disaster Readiness and Risk"),
                new Enroll_Student.Subject("Introduction to Philosophy 1"),
                new Enroll_Student.Subject("Personality Development")
        };
        strands11[1] = new Enroll_Student.Strand("STEM", stem11Subjects);

        Enroll_Student.Subject[] abm11Subjects = {
                new Enroll_Student.Subject("Oral Communication"),
                new Enroll_Student.Subject("Komunikasyon at Pananaliksik"),
                new Enroll_Student.Subject("General Mathematics"),
                new Enroll_Student.Subject("Personality Development"),
                new Enroll_Student.Subject("Disaster Readiness and Risk"),
                new Enroll_Student.Subject("Entrepreneurship"),
                new Enroll_Student.Subject("Organization Management")
        };
        strands11[0] = new Enroll_Student.Strand("ABM", abm11Subjects);

        Enroll_Student.Subject[] humss11Subjects = {
                new Enroll_Student.Subject("Information Technology"),
                new Enroll_Student.Subject("Komunikasyon at Pananaliksik"),
                new Enroll_Student.Subject("Physical Education"),
                new Enroll_Student.Subject("Physical Science"),
                new Enroll_Student.Subject("Reading and Writing"),
                new Enroll_Student.Subject("Earth and Science"),
                new Enroll_Student.Subject("Personality and Development")
        };
        strands11[2] = new Enroll_Student.Strand("HUMSS", humss11Subjects);

        Enroll_Student.Subject[] gas11Subjects = {
                new Enroll_Student.Subject("Oral Communication"),
                new Enroll_Student.Subject("General Mathematics"),
                new Enroll_Student.Subject("Earth and Life Science"),
                new Enroll_Student.Subject("Understanding Culture"),
                new Enroll_Student.Subject("Disaster Readiness and Risk"),
                new Enroll_Student.Subject("Introduction to Philosophy 1"),
                new Enroll_Student.Subject("Personality Development")
        };
        strands11[3] = new Enroll_Student.Strand("GAS", gas11Subjects);

        // Initialize Grade 12 strands and subjects
        Enroll_Student.Subject[] stem12Subjects = {
                new Enroll_Student.Subject("21st Century Literature"),
                new Enroll_Student.Subject("Basic Calculus"),
                new Enroll_Student.Subject("Entrepreneurship"),
                new Enroll_Student.Subject("General Biology"),
                new Enroll_Student.Subject("Research"),
                new Enroll_Student.Subject("Pagsulat sa Filipino"),
                new Enroll_Student.Subject("General Physics 1")
        };
        strands12[1] = new Enroll_Student.Strand("STEM", stem12Subjects);

        Enroll_Student.Subject[] abm12Subjects = {
                new Enroll_Student.Subject("Introduction to Philosophy"),
                new Enroll_Student.Subject("Contemporary Philippine Arts"),
                new Enroll_Student.Subject("Physical Education"),
                new Enroll_Student.Subject("Empowerment Technologies"),
                new Enroll_Student.Subject("Disaster Readiness and Risk"),
                new Enroll_Student.Subject("Fundamentals of Accountancy"),
                new Enroll_Student.Subject("Principles of Marketing")
        };
        strands12[0] = new Enroll_Student.Strand("ABM", abm12Subjects);

        Enroll_Student.Subject[] humss12Subjects = {
                new Enroll_Student.Subject("General Mathematics"),
                new Enroll_Student.Subject("Earth Science"),
                new Enroll_Student.Subject("Physical Education"),
                new Enroll_Student.Subject("Philippine Arts"),
                new Enroll_Student.Subject("Disaster Readiness and Risk"),
                new Enroll_Student.Subject("Introduction to Philosophy 1"),
                new Enroll_Student.Subject("Personality Development")
        };
        strands12[2] = new Enroll_Student.Strand("HUMSS", humss12Subjects);

        Enroll_Student.Subject[] gas12Subjects = {
                new Enroll_Student.Subject("General Mathematics"),
                new Enroll_Student.Subject("Earth Science"),
                new Enroll_Student.Subject("Physical Education"),
                new Enroll_Student.Subject("Empowerment Technologies"),
                new Enroll_Student.Subject("Practical Research 2"),
                new Enroll_Student.Subject("English for Academic"),
                new Enroll_Student.Subject("Organization Management")
        };
        strands12[3] = new Enroll_Student.Strand("GAS", gas12Subjects);
    }
}
