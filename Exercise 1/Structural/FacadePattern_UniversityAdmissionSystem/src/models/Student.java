package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Student model class representing a student in the university admission system
 * Contains personal information and application details
 */
public class Student {
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String emergencyContact;
    private List<String> submittedDocuments;
    private String studentId;
    private boolean isVerified;
    private boolean isEnrolled;
    
    /**
     * Constructor to create a new Student
     * @param name Student's full name
     * @param email Student's email address
     * @param phoneNumber Student's contact number
     * @param dateOfBirth Student's date of birth
     * @param address Student's address
     * @param emergencyContact Emergency contact information
     */
    public Student(String name, String email, String phoneNumber, 
                  LocalDate dateOfBirth, String address, String emergencyContact) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.submittedDocuments = new ArrayList<>();
        this.isVerified = false;
        this.isEnrolled = false;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public List<String> getSubmittedDocuments() { return submittedDocuments; }
    public void addDocument(String document) { this.submittedDocuments.add(document); }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    
    public boolean isEnrolled() { return isEnrolled; }
    public void setEnrolled(boolean enrolled) { isEnrolled = enrolled; }
    
    /**
     * Validates if student information is complete
     * @return true if all required fields are filled
     */
    public boolean isValidForAdmission() {
        return name != null && !name.trim().isEmpty() &&
               email != null && email.contains("@") &&
               phoneNumber != null && !phoneNumber.trim().isEmpty() &&
               dateOfBirth != null &&
               address != null && !address.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("Student{name='%s', email='%s', phone='%s', verified=%s, enrolled=%s, studentId='%s'}",
                           name, email, phoneNumber, isVerified, isEnrolled, studentId);
    }
}
