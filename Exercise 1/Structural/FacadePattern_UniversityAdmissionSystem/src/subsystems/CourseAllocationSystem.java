package subsystems;

import models.Course;
import models.Student;
import java.util.*;

/**
 * Course Allocation System - Handles course assignment and enrollment
 * This subsystem manages course availability, seat allocation, and enrollment
 * Part of the Facade Pattern implementation for University Admission System
 */
public class CourseAllocationSystem {
    private Map<String, Course> availableCourses;
    private Map<String, String> studentCourseAllocation;
    private Map<String, List<String>> courseWaitlist;
    
    /**
     * Constructor initializes the course allocation system
     */
    public CourseAllocationSystem() {
        this.availableCourses = new HashMap<>();
        this.studentCourseAllocation = new HashMap<>();
        this.courseWaitlist = new HashMap<>();
        initializeAvailableCourses();
    }
    
    /**
     * Initializes available courses for the academic year
     */
    private void initializeAvailableCourses() {
        // Undergraduate Engineering Courses
        availableCourses.put("CS101", new Course("CS101", "Computer Science & Engineering", 
            "Engineering", 4, 50000.0, 60, Course.CourseType.UNDERGRADUATE));
        
        availableCourses.put("EE101", new Course("EE101", "Electrical & Electronics Engineering", 
            "Engineering", 4, 45000.0, 50, Course.CourseType.UNDERGRADUATE));
        
        availableCourses.put("ME101", new Course("ME101", "Mechanical Engineering", 
            "Engineering", 4, 48000.0, 55, Course.CourseType.UNDERGRADUATE));
        
        availableCourses.put("CE101", new Course("CE101", "Civil Engineering", 
            "Engineering", 4, 46000.0, 45, Course.CourseType.UNDERGRADUATE));
        
        // Management Courses
        availableCourses.put("MBA101", new Course("MBA101", "Master of Business Administration", 
            "Management", 2, 75000.0, 40, Course.CourseType.POSTGRADUATE));
        
        availableCourses.put("BBA101", new Course("BBA101", "Bachelor of Business Administration", 
            "Management", 3, 40000.0, 35, Course.CourseType.UNDERGRADUATE));
        
        // Computer Applications
        availableCourses.put("MCA101", new Course("MCA101", "Master of Computer Applications", 
            "Computer Science", 3, 55000.0, 30, Course.CourseType.POSTGRADUATE));
        
        // Commerce
        availableCourses.put("BCom101", new Course("BCom101", "Bachelor of Commerce", 
            "Commerce", 3, 35000.0, 50, Course.CourseType.UNDERGRADUATE));
        
        // Initialize all courses with zero enrollment - capacity starts at 0/total
        // Enrollment will increase in real-time as students are admitted
        availableCourses.get("CS101").setCurrentEnrollment(0);
        availableCourses.get("MBA101").setCurrentEnrollment(0);
        availableCourses.get("EE101").setCurrentEnrollment(0);
        availableCourses.get("MCA101").setCurrentEnrollment(0);
        availableCourses.get("ME101").setCurrentEnrollment(0);
        availableCourses.get("CE101").setCurrentEnrollment(0);
        availableCourses.get("BBA101").setCurrentEnrollment(0);
        availableCourses.get("BCom101").setCurrentEnrollment(0);
        
        // Add course descriptions
        availableCourses.get("CS101").setDescription("Comprehensive program covering programming, algorithms, data structures, software engineering, and emerging technologies.");
        availableCourses.get("EE101").setDescription("Focus on electrical systems, electronics, power engineering, and communication technologies.");
        availableCourses.get("ME101").setDescription("Mechanical systems design, thermodynamics, manufacturing, and automotive engineering.");
        availableCourses.get("CE101").setDescription("Infrastructure development, structural engineering, construction management, and urban planning.");
        availableCourses.get("MBA101").setDescription("Advanced business management, leadership, strategy, and entrepreneurship.");
        availableCourses.get("BBA101").setDescription("Foundation in business principles, management, marketing, and finance.");
        availableCourses.get("MCA101").setDescription("Advanced computer applications, software development, and IT management.");
        availableCourses.get("BCom101").setDescription("Commerce fundamentals, accounting, business law, and financial management.");
    }
    
    /**
     * Displays all available courses with their details
     */
    public void displayAvailableCourses() {
        System.out.println("\nCOURSE ALLOCATION SYSTEM");
        System.out.println("===========================");
        System.out.println("Available Courses for Admission:");
        System.out.println();
        
        int index = 1;
        for (Course course : availableCourses.values()) {
            System.out.printf("%d. %s (%s)%n", index++, course.getCourseName(), course.getCourseCode());
            System.out.printf("   Department: %s%n", course.getDepartment());
            System.out.printf("   Type: %s%n", course.getCourseType().getDisplayName());
            System.out.printf("   Duration: %d years%n", course.getDuration());
            System.out.printf("   Fees: Rs.%,.2f per year%n", course.getFees());
            System.out.printf("   Capacity: %d/%d (Available: %d) - Utilization: %.1f%%%n", 
                             course.getCurrentEnrollment(), course.getMaxCapacity(), 
                             course.getRemainingSeats(), course.getUtilizationPercentage());
            System.out.printf("   Status: %s%n", course.getCapacityStatus());
            
            // Show capacity progression for demonstration
            if (course.getCurrentEnrollment() == 0) {
                System.out.println("   NOTE: Fresh capacity - ready for first enrollment!");
            } else {
                System.out.printf("   PROGRESS: %d student(s) enrolled so far%n", course.getCurrentEnrollment());
            }
            
            if (course.getDescription() != null) {
                System.out.printf("   Description: %s%n", course.getDescription());
            }
            
            // Real-time capacity warnings
            if (course.isFull()) {
                System.out.println("   WARNING: COURSE IS FULL - Waitlist available");
            } else if (course.isNearlyFull()) {
                System.out.println("   ALERT: Course is nearly full! Act quickly to secure seat");
            } else if (course.getRemainingSeats() <= 5) {
                System.out.println("   NOTE: Limited seats available - Register soon!");
            }
            
            // Show waitlist info if exists
            List<String> waitlist = courseWaitlist.get(course.getCourseCode());
            if (waitlist != null && !waitlist.isEmpty()) {
                System.out.printf("   Waitlist: %d students waiting%n", waitlist.size());
            }
            
            System.out.println();
        }
        
        // Display overall system statistics
        displaySystemCapacityStats();
    }
    
    /**
     * Allocates a course to a student
     * @param student Student requesting course allocation
     * @param courseCode Course code requested
     * @return AllocationResult containing allocation status
     */
    public AllocationResult allocateCourse(Student student, String courseCode) {
        System.out.println("\nProcessing Course Allocation...");
        System.out.println("=================================");
        
        String studentId = student.getEmail();
        
        // Check if course exists
        Course course = availableCourses.get(courseCode);
        if (course == null) {
            System.out.println("ERROR: Course not found: " + courseCode);
            return new AllocationResult(false, null, "Course not found", false);
        }
        
        System.out.println("Requested Course: " + course.getCourseName());
        System.out.println(" Student: " + student.getName());
        
        // Check if student already has a course allocated
        if (studentCourseAllocation.containsKey(studentId)) {
            String existingCourse = studentCourseAllocation.get(studentId);
            System.out.println("WARNING: Student already allocated to course: " + existingCourse);
            return new AllocationResult(false, course, "Student already enrolled in another course", false);
        }
        
        // Check course availability
        if (!course.isActive()) {
            System.out.println("ERROR: Course is currently inactive");
            return new AllocationResult(false, course, "Course is not active for admission", false);
        }
        
        // Try to allocate seat - BUT DON'T INCREASE CAPACITY YET
        if (course.hasAvailableSeats()) {
            // Tentative allocation - capacity will increase only after full admission success
            studentCourseAllocation.put(studentId, courseCode);
            System.out.println("SUCCESS: Course tentatively allocated!");
            System.out.printf("TENTATIVE ALLOCATION: Seat reserved for %s%n", course.getCourseName());
            System.out.printf("Current Capacity: %d/%d (Available: %d) - Capacity will increase only after full admission%n", 
                             course.getCurrentEnrollment(), course.getMaxCapacity(), course.getRemainingSeats());
            
            return new AllocationResult(true, course, "Course tentatively allocated - pending full admission", false);
        }
        
        // Course is full - add to waitlist
        System.out.println("WARNING: Course is full! Adding to waitlist...");
        addToWaitlist(studentId, courseCode);
        
        int waitlistPosition = getWaitlistPosition(studentId, courseCode);
        String message = String.format("Course is full. Added to waitlist (Position: %d)", waitlistPosition);
        
        System.out.println("Added to waitlist - Position: " + waitlistPosition);
        
        return new AllocationResult(false, course, message, true);
    }
    
    /**
     * Adds student to course waitlist
     * @param studentId Student identifier
     * @param courseCode Course code
     */
    private void addToWaitlist(String studentId, String courseCode) {
        courseWaitlist.computeIfAbsent(courseCode, k -> new ArrayList<>()).add(studentId);
    }
    
    /**
     * Gets waitlist position for a student
     * @param studentId Student identifier
     * @param courseCode Course code
     * @return Position in waitlist (1-based)
     */
    private int getWaitlistPosition(String studentId, String courseCode) {
        List<String> waitlist = courseWaitlist.get(courseCode);
        if (waitlist == null) return -1;
        
        return waitlist.indexOf(studentId) + 1;
    }
    
    /**
     * Gets allocated course for a student
     * @param studentId Student identifier
     * @return Course object if allocated, null otherwise
     */
    public Course getAllocatedCourse(String studentId) {
        String courseCode = studentCourseAllocation.get(studentId);
        return courseCode != null ? availableCourses.get(courseCode) : null;
    }
    
    /**
     * Checks if student has been allocated a course
     * @param studentId Student identifier
     * @return true if course is allocated
     */
    public boolean hasAllocatedCourse(String studentId) {
        return studentCourseAllocation.containsKey(studentId);
    }
    
    /**
     * Gets course by course code
     * @param courseCode Course code
     * @return Course object
     */
    public Course getCourse(String courseCode) {
        return availableCourses.get(courseCode);
    }
    
    /**
     * Gets all available courses
     * @return Map of course codes to Course objects
     */
    public Map<String, Course> getAllCourses() {
        return new HashMap<>(availableCourses);
    }
    
    /**
     * Gets enrollment statistics
     * @return EnrollmentStats containing system statistics
     */
    public EnrollmentStats getEnrollmentStats() {
        int totalCapacity = availableCourses.values().stream()
            .mapToInt(Course::getMaxCapacity)
            .sum();
        
        int totalEnrolled = availableCourses.values().stream()
            .mapToInt(Course::getCurrentEnrollment)
            .sum();
        
        int totalAvailable = totalCapacity - totalEnrolled;
        
        int totalWaitlisted = courseWaitlist.values().stream()
            .mapToInt(List::size)
            .sum();
        
        return new EnrollmentStats(totalCapacity, totalEnrolled, totalAvailable, totalWaitlisted);
    }
    
    /**
     * Displays system-wide capacity statistics
     */
    public void displaySystemCapacityStats() {
        System.out.println("SYSTEM CAPACITY OVERVIEW");
        System.out.println("=========================");
        
        EnrollmentStats stats = getEnrollmentStats();
        double systemUtilization = (double) stats.getTotalEnrolled() / stats.getTotalCapacity() * 100.0;
        
        System.out.printf("Total System Capacity: %d seats%n", stats.getTotalCapacity());
        System.out.printf("Currently Enrolled: %d students%n", stats.getTotalEnrolled());
        System.out.printf("Available Seats: %d%n", stats.getTotalAvailable());
        System.out.printf("Waitlisted Students: %d%n", stats.getTotalWaitlisted());
        System.out.printf("System Utilization: %.1f%%%n", systemUtilization);
        
        // Real-time alerts
        if (systemUtilization > 95.0) {
            System.out.println("CRITICAL: System near capacity!");
        } else if (systemUtilization > 85.0) {
            System.out.println("WARNING: System approaching high utilization");
        }
        
        System.out.println();
    }
    
    /**
     * Withdraws a student from their allocated course
     * @param studentId Student identifier
     * @return true if withdrawal successful
     */
    public boolean withdrawStudent(String studentId) {
        String courseCode = studentCourseAllocation.get(studentId);
        if (courseCode == null) {
            System.out.println("ERROR: Student not enrolled in any course");
            return false;
        }
        
        Course course = availableCourses.get(courseCode);
        if (course == null) {
            System.out.println("ERROR: Course not found");
            return false;
        }
        
        // Withdraw student from course (reduce enrollment)
        boolean withdrawn = course.withdrawStudent();
        if (withdrawn) {
            studentCourseAllocation.remove(studentId);
            System.out.printf("SUCCESS: Student withdrawn from %s%n", course.getCourseName());
            System.out.printf("Course capacity now: %d/%d (Available: %d)%n", 
                             course.getCurrentEnrollment(), course.getMaxCapacity(), course.getRemainingSeats());
            
            // Process waitlist if exists
            processWaitlist(courseCode);
            return true;
        }
        
        return false;
    }
    
    /**
     * Processes waitlist when a seat becomes available
     * @param courseCode Course code with available seat
     */
    private void processWaitlist(String courseCode) {
        List<String> waitlist = courseWaitlist.get(courseCode);
        if (waitlist != null && !waitlist.isEmpty()) {
            String nextStudentId = waitlist.remove(0);
            Course course = availableCourses.get(courseCode);
            
            if (course.enrollStudent()) {
                studentCourseAllocation.put(nextStudentId, courseCode);
                System.out.printf("AUTO-ENROLLMENT: Student from waitlist enrolled in %s%n", course.getCourseName());
                System.out.printf("Remaining waitlist: %d students%n", waitlist.size());
            }
        }
    }
    
    /**
     * Gets real-time course capacity information
     * @param courseCode Course code
     * @return String with current capacity info
     */
    public String getRealTimeCapacityInfo(String courseCode) {
        Course course = availableCourses.get(courseCode);
        if (course == null) {
            return "Course not found";
        }
        
        return String.format("%s: %d/%d seats (%.1f%% full) - Status: %s", 
                           course.getCourseName(), 
                           course.getCurrentEnrollment(), 
                           course.getMaxCapacity(), 
                           course.getUtilizationPercentage(),
                           course.getCapacityStatus());
    }
    
    /**
     * Confirms enrollment - actually increases capacity after successful admission
     * @param studentId Student identifier
     * @return true if confirmation successful
     */
    public boolean confirmEnrollment(String studentId) {
        String courseCode = studentCourseAllocation.get(studentId);
        if (courseCode == null) {
            System.out.println("ERROR: No tentative allocation found for student");
            return false;
        }
        
        Course course = availableCourses.get(courseCode);
        if (course == null) {
            System.out.println("ERROR: Course not found");
            return false;
        }
        
        // Now actually enroll the student and increase capacity
        boolean enrolled = course.enrollStudent();
        if (enrolled) {
            System.out.println("ENROLLMENT CONFIRMED: Capacity increased!");
            System.out.printf("FINAL CAPACITY UPDATE: %s now has %d/%d seats occupied%n", 
                             course.getCourseName(), course.getCurrentEnrollment(), course.getMaxCapacity());
            return true;
        }
        
        return false;
    }
    
    /**
     * Cancels tentative allocation if admission fails
     * @param studentId Student identifier
     * @return true if cancellation successful
     */
    public boolean cancelTentativeAllocation(String studentId) {
        String courseCode = studentCourseAllocation.get(studentId);
        if (courseCode == null) {
            System.out.println("No tentative allocation to cancel");
            return false;
        }
        
        // Remove the tentative allocation
        studentCourseAllocation.remove(studentId);
        Course course = availableCourses.get(courseCode);
        
        System.out.println("TENTATIVE ALLOCATION CANCELLED: Seat released without capacity change");
        System.out.printf("Course %s remains at %d/%d capacity%n", 
                         course.getCourseName(), course.getCurrentEnrollment(), course.getMaxCapacity());
        
        return true;
    }
    
    /**
     * Inner class for allocation result
     */
    public static class AllocationResult {
        private boolean success;
        private Course allocatedCourse;
        private String message;
        private boolean waitlisted;
        
        public AllocationResult(boolean success, Course allocatedCourse, String message, boolean waitlisted) {
            this.success = success;
            this.allocatedCourse = allocatedCourse;
            this.message = message;
            this.waitlisted = waitlisted;
        }
        
        public boolean isSuccess() { return success; }
        public Course getAllocatedCourse() { return allocatedCourse; }
        public String getMessage() { return message; }
        public boolean isWaitlisted() { return waitlisted; }
    }
    
    /**
     * Inner class for enrollment statistics
     */
    public static class EnrollmentStats {
        private int totalCapacity;
        private int totalEnrolled;
        private int totalAvailable;
        private int totalWaitlisted;
        
        public EnrollmentStats(int totalCapacity, int totalEnrolled, int totalAvailable, int totalWaitlisted) {
            this.totalCapacity = totalCapacity;
            this.totalEnrolled = totalEnrolled;
            this.totalAvailable = totalAvailable;
            this.totalWaitlisted = totalWaitlisted;
        }
        
        public int getTotalCapacity() { return totalCapacity; }
        public int getTotalEnrolled() { return totalEnrolled; }
        public int getTotalAvailable() { return totalAvailable; }
        public int getTotalWaitlisted() { return totalWaitlisted; }
    }
}
