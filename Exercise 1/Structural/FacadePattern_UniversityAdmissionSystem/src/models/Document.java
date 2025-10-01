package models;

import java.time.LocalDateTime;

/**
 * Document model class representing documents required for admission
 * Handles document verification status and metadata
 */
public class Document {
    private String documentId;
    private String studentId;
    private DocumentType documentType;
    private String fileName;
    private String filePath;
    private VerificationStatus verificationStatus;
    private LocalDateTime submittedDate;
    private LocalDateTime verifiedDate;
    private String verifierComments;
    private String verifierId;
    private boolean isMandatory;
    private long fileSize;
    private String fileFormat;
    
    /**
     * Enum for different types of documents
     */
    public enum DocumentType {
        BIRTH_CERTIFICATE("Birth Certificate"),
        ACADEMIC_TRANSCRIPT("Academic Transcript"),
        IDENTITY_PROOF("Identity Proof"),
        ADDRESS_PROOF("Address Proof"),
        PASSPORT_PHOTO("Passport Photo"),
        SIGNATURE("Signature"),
        CASTE_CERTIFICATE("Caste Certificate"),
        INCOME_CERTIFICATE("Income Certificate"),
        MIGRATION_CERTIFICATE("Migration Certificate"),
        CHARACTER_CERTIFICATE("Character Certificate"),
        MEDICAL_CERTIFICATE("Medical Certificate"),
        SPORTS_CERTIFICATE("Sports Certificate"),
        EXPERIENCE_CERTIFICATE("Experience Certificate"),
        RECOMMENDATION_LETTER("Recommendation Letter"),
        STATEMENT_OF_PURPOSE("Statement of Purpose");
        
        private final String displayName;
        
        DocumentType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum for document verification status
     */
    public enum VerificationStatus {
        SUBMITTED("Submitted"),
        UNDER_REVIEW("Under Review"),
        VERIFIED("Verified"),
        REJECTED("Rejected"),
        RESUBMISSION_REQUIRED("Resubmission Required"),
        PENDING("Pending");
        
        private final String displayName;
        
        VerificationStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Constructor to create a new Document
     * @param documentId Unique document identifier
     * @param studentId Student identifier
     * @param documentType Type of document
     * @param fileName Original file name
     * @param filePath Path where document is stored
     * @param isMandatory Whether document is mandatory for admission
     */
    public Document(String documentId, String studentId, DocumentType documentType,
                   String fileName, String filePath, boolean isMandatory) {
        this.documentId = documentId;
        this.studentId = studentId;
        this.documentType = documentType;
        this.fileName = fileName;
        this.filePath = filePath;
        this.isMandatory = isMandatory;
        this.submittedDate = LocalDateTime.now();
        this.verificationStatus = VerificationStatus.SUBMITTED;
    }
    
    // Getters and Setters
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(VerificationStatus verificationStatus) { 
        this.verificationStatus = verificationStatus;
        if (verificationStatus == VerificationStatus.VERIFIED) {
            this.verifiedDate = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDateTime submittedDate) { this.submittedDate = submittedDate; }
    
    public LocalDateTime getVerifiedDate() { return verifiedDate; }
    public void setVerifiedDate(LocalDateTime verifiedDate) { this.verifiedDate = verifiedDate; }
    
    public String getVerifierComments() { return verifierComments; }
    public void setVerifierComments(String verifierComments) { this.verifierComments = verifierComments; }
    
    public String getVerifierId() { return verifierId; }
    public void setVerifierId(String verifierId) { this.verifierId = verifierId; }
    
    public boolean isMandatory() { return isMandatory; }
    public void setMandatory(boolean mandatory) { isMandatory = mandatory; }
    
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    
    public String getFileFormat() { return fileFormat; }
    public void setFileFormat(String fileFormat) { this.fileFormat = fileFormat; }
    
    /**
     * Verifies the document
     * @param verifierId ID of the verifier
     * @param comments Verification comments
     * @param isApproved Whether document is approved
     */
    public void verifyDocument(String verifierId, String comments, boolean isApproved) {
        this.verifierId = verifierId;
        this.verifierComments = comments;
        this.verificationStatus = isApproved ? VerificationStatus.VERIFIED : VerificationStatus.REJECTED;
        this.verifiedDate = LocalDateTime.now();
    }
    
    /**
     * Checks if document is verified
     * @return true if verification status is VERIFIED
     */
    public boolean isVerified() {
        return verificationStatus == VerificationStatus.VERIFIED;
    }
    
    /**
     * Checks if document needs resubmission
     * @return true if status is REJECTED or RESUBMISSION_REQUIRED
     */
    public boolean needsResubmission() {
        return verificationStatus == VerificationStatus.REJECTED || 
               verificationStatus == VerificationStatus.RESUBMISSION_REQUIRED;
    }
    
    @Override
    public String toString() {
        return String.format("Document{id='%s', type=%s, status=%s, mandatory=%s, submitted=%s}",
                           documentId, documentType.getDisplayName(), 
                           verificationStatus.getDisplayName(), isMandatory, 
                           submittedDate.toLocalDate());
    }
}
