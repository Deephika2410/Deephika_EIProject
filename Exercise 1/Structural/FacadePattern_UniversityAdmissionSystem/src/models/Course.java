package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Course model class representing academic courses available for admission
 * Contains course details, requirements, and enrollment information
 */
public class Course {
    private String courseCode;
    private String courseName;
    private String department;
    private int duration; // in years
    private double fees;
    private int maxCapacity;
    private int currentEnrollment;
    private List<String> prerequisites;
    private String description;
    private CourseType courseType;
    private boolean isActive;
    
    /**
     * Enum for different types of courses
     */
    public enum CourseType {
        UNDERGRADUATE("Undergraduate"),
        POSTGRADUATE("Postgraduate"),
        DIPLOMA("Diploma"),
        CERTIFICATE("Certificate"),
        RESEARCH("Research");
        
        private final String displayName;
        
        CourseType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Constructor to create a new Course
     * @param courseCode Unique course identifier
     * @param courseName Name of the course
     * @param department Department offering the course
     * @param duration Course duration in years
     * @param fees Course fees
     * @param maxCapacity Maximum student capacity
     * @param courseType Type of course
     */
    public Course(String courseCode, String courseName, String department, 
                 int duration, double fees, int maxCapacity, CourseType courseType) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
        this.duration = duration;
        this.fees = fees;
        this.maxCapacity = maxCapacity;
        this.courseType = courseType;
        this.currentEnrollment = 0; // Always start with 0 enrollment
        this.prerequisites = new ArrayList<>();
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public double getFees() { return fees; }
    public void setFees(double fees) { this.fees = fees; }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public int getCurrentEnrollment() { return currentEnrollment; }
    public void setCurrentEnrollment(int currentEnrollment) { this.currentEnrollment = currentEnrollment; }
    
    public List<String> getPrerequisites() { return prerequisites; }
    public void addPrerequisite(String prerequisite) { this.prerequisites.add(prerequisite); }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { this.courseType = courseType; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    /**
     * Checks if the course has available seats
     * @return true if enrollment is less than capacity
     */
    public boolean hasAvailableSeats() {
        return currentEnrollment < maxCapacity;
    }
    
    /**
     * Enrolls a student in the course
     * @return true if enrollment successful
     */
    public boolean enrollStudent() {
        if (hasAvailableSeats() && isActive) {
            int previousEnrollment = currentEnrollment;
            currentEnrollment++;
            System.out.printf("ENROLLMENT UPDATE: %s capacity changed from %d/%d to %d/%d%n",
                            courseCode, previousEnrollment, maxCapacity, currentEnrollment, maxCapacity);
            return true;
        }
        return false;
    }
    
    /**
     * Gets remaining seats in the course
     * @return number of available seats
     */
    public int getRemainingSeats() {
        return maxCapacity - currentEnrollment;
    }
    
    /**
     * Withdraws a student from the course (decreases enrollment)
     * @return true if withdrawal successful
     */
    public boolean withdrawStudent() {
        if (currentEnrollment > 0) {
            int previousEnrollment = currentEnrollment;
            currentEnrollment--;
            System.out.printf("WITHDRAWAL UPDATE: %s capacity changed from %d/%d to %d/%d%n",
                            courseCode, previousEnrollment, maxCapacity, currentEnrollment, maxCapacity);
            return true;
        }
        return false;
    }
    
    /**
     * Gets the utilization percentage of the course
     * @return utilization percentage (0-100)
     */
    public double getUtilizationPercentage() {
        return (double) currentEnrollment / maxCapacity * 100.0;
    }
    
    /**
     * Checks if the course is nearly full (>90% capacity)
     * @return true if nearly full
     */
    public boolean isNearlyFull() {
        return getUtilizationPercentage() > 90.0;
    }
    
    /**
     * Checks if the course is full
     * @return true if at capacity
     */
    public boolean isFull() {
        return currentEnrollment >= maxCapacity;
    }
    
    /**
     * Gets a status message for the course capacity
     * @return capacity status message
     */
    public String getCapacityStatus() {
        if (isFull()) {
            return "FULL";
        } else if (isNearlyFull()) {
            return "NEARLY FULL";
        } else if (getRemainingSeats() <= 5) {
            return "LIMITED SEATS";
        } else {
            return "AVAILABLE";
        }
    }
    
    @Override
    public String toString() {
        return String.format("Course{code='%s', name='%s', department='%s', fees=%.2f, " +
                           "capacity=%d/%d (%.1f%%), status=%s, type=%s, active=%s}",
                           courseCode, courseName, department, fees, 
                           currentEnrollment, maxCapacity, getUtilizationPercentage(),
                           getCapacityStatus(), courseType.getDisplayName(), isActive);
    }
}
