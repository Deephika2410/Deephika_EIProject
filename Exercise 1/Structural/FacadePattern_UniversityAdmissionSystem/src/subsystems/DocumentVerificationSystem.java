package subsystems;

import models.Document;
import models.Student;
import java.util.*;
import java.time.LocalDateTime;

/**
 * Document Verification System - Handles verification of student documents
 * This subsystem is responsible for validating and verifying all submitted documents
 * Part of the Facade Pattern implementation for University Admission System
 */
public class DocumentVerificationSystem {
    private Map<String, List<Document>> studentDocuments;
    private Set<Document.DocumentType> mandatoryDocuments;
    private int verificationCounter;
    
    /**
     * Constructor initializes the document verification system
     */
    public DocumentVerificationSystem() {
        this.studentDocuments = new HashMap<>();
        this.mandatoryDocuments = new HashSet<>();
        this.verificationCounter = 0;
        initializeMandatoryDocuments();
    }
    
    /**
     * Initializes the list of mandatory documents required for admission
     */
    private void initializeMandatoryDocuments() {
        mandatoryDocuments.add(Document.DocumentType.BIRTH_CERTIFICATE);
        mandatoryDocuments.add(Document.DocumentType.ACADEMIC_TRANSCRIPT);
        mandatoryDocuments.add(Document.DocumentType.IDENTITY_PROOF);
        mandatoryDocuments.add(Document.DocumentType.ADDRESS_PROOF);
        mandatoryDocuments.add(Document.DocumentType.PASSPORT_PHOTO);
        mandatoryDocuments.add(Document.DocumentType.CHARACTER_CERTIFICATE);
    }
    
    /**
     * Submits documents for a student
     * @param student Student submitting documents
     * @param documentTypes List of document types being submitted
     * @return true if submission successful
     */
    public boolean submitDocuments(Student student, List<Document.DocumentType> documentTypes) {
        System.out.println("\nDOCUMENT VERIFICATION SYSTEM");
        System.out.println("================================");
        System.out.println("Processing document submission for: " + student.getName());
        
        String studentId = student.getEmail(); // Using email as temporary ID
        List<Document> documents = new ArrayList<>();
        
        for (Document.DocumentType docType : documentTypes) {
            String docId = generateDocumentId();
            String fileName = docType.getDisplayName().replaceAll(" ", "_") + "_" + studentId + ".pdf";
            String filePath = "/documents/" + studentId + "/" + fileName;
            boolean isMandatory = mandatoryDocuments.contains(docType);
            
            Document doc = new Document(docId, studentId, docType, fileName, filePath, isMandatory);
            documents.add(doc);
            
            System.out.println("Submitted: " + docType.getDisplayName() + 
                             (isMandatory ? " (Mandatory)" : " (Optional)"));
        }
        
        studentDocuments.put(studentId, documents);
        System.out.println("Document submission completed successfully!");
        return true;
    }
    
    /**
     * Verifies all submitted documents for a student
     * @param student Student whose documents need verification
     * @return VerificationResult containing verification status and details
     */
    public VerificationResult verifyDocuments(Student student) {
        System.out.println("\n🔎 Starting Document Verification Process...");
        
        String studentId = student.getEmail();
        List<Document> documents = studentDocuments.get(studentId);
        
        if (documents == null || documents.isEmpty()) {
            System.out.println("ERROR: No documents found for verification!");
            return new VerificationResult(false, "No documents submitted", new ArrayList<>());
        }
        
        List<String> verificationDetails = new ArrayList<>();
        List<Document> missingMandatory = new ArrayList<>();
        int verifiedCount = 0;
        
        // Check for mandatory documents
        for (Document.DocumentType mandatoryType : mandatoryDocuments) {
            boolean found = documents.stream()
                .anyMatch(doc -> doc.getDocumentType() == mandatoryType);
            
            if (!found) {
                Document missingDoc = new Document("MISSING", studentId, mandatoryType, 
                                                 "NOT_SUBMITTED", "", true);
                missingMandatory.add(missingDoc);
                verificationDetails.add("ERROR: Missing mandatory document: " + mandatoryType.getDisplayName());
            }
        }
        
        if (!missingMandatory.isEmpty()) {
            System.out.println("ERROR: Document verification failed - Missing mandatory documents");
            return new VerificationResult(false, "Missing mandatory documents", verificationDetails);
        }
        
        // Simulate document verification process
        System.out.println("Verifying submitted documents...");
        for (Document doc : documents) {
            // Simulate verification time
            try {
                Thread.sleep(200); // Small delay for realistic simulation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Random verification logic (90% success rate for demonstration)
            boolean verified = Math.random() > 0.1;
            String verifierId = "VERIFIER_" + (verificationCounter++ % 5 + 1);
            
            if (verified) {
                doc.verifyDocument(verifierId, "Document verified successfully", true);
                verifiedCount++;
                verificationDetails.add("SUCCESS: " + doc.getDocumentType().getDisplayName() + " - VERIFIED");
                System.out.println("   SUCCESS: " + doc.getDocumentType().getDisplayName() + " verified");
            } else {
                doc.verifyDocument(verifierId, "Document quality issues - resubmission required", false);
                verificationDetails.add("ERROR: " + doc.getDocumentType().getDisplayName() + " - REJECTED");
                System.out.println("   ERROR: " + doc.getDocumentType().getDisplayName() + " rejected");
            }
        }
        
        boolean allVerified = verifiedCount == documents.size();
        String message = allVerified ? 
            "All documents verified successfully" : 
            String.format("%d/%d documents verified", verifiedCount, documents.size());
        
        if (allVerified) {
            student.setVerified(true);
            System.out.println("SUCCESS: Document verification completed successfully!");
        } else {
            System.out.println("WARNING: Document verification completed with issues");
        }
        
        return new VerificationResult(allVerified, message, verificationDetails);
    }
    
    /**
     * Gets verification status for a student
     * @param studentId Student identifier
     * @return Current verification status
     */
    public boolean isStudentVerified(String studentId) {
        List<Document> documents = studentDocuments.get(studentId);
        if (documents == null || documents.isEmpty()) {
            return false;
        }
        
        // Check if all mandatory documents are verified
        for (Document.DocumentType mandatoryType : mandatoryDocuments) {
            boolean verified = documents.stream()
                .anyMatch(doc -> doc.getDocumentType() == mandatoryType && doc.isVerified());
            
            if (!verified) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets list of documents for a student
     * @param studentId Student identifier
     * @return List of documents
     */
    public List<Document> getStudentDocuments(String studentId) {
        return studentDocuments.getOrDefault(studentId, new ArrayList<>());
    }
    
    /**
     * Generates unique document ID
     * @return Document ID
     */
    private String generateDocumentId() {
        return "DOC_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Inner class to represent verification result
     */
    public static class VerificationResult {
        private boolean success;
        private String message;
        private List<String> details;
        
        public VerificationResult(boolean success, String message, List<String> details) {
            this.success = success;
            this.message = message;
            this.details = details;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public List<String> getDetails() { return details; }
    }
}
