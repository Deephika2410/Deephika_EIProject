package analyzers;

import models.Task;
import utils.Logger;
import java.util.List;

/**
 * Context class for productivity analysis using Strategy pattern
 * Allows dynamic selection of analysis strategies at runtime
 * Follows SOLID principles with strategy pattern implementation
 */
public class ProductivityAnalysisContext {
    
    private ProductivityAnalyzer analyzer;
    
    /**
     * Constructor with default analyzer
     */
    public ProductivityAnalysisContext() {
        this.analyzer = new ComprehensiveProductivityAnalyzer();
        Logger.info("ProductivityAnalysisContext created with default analyzer");
    }
    
    /**
     * Constructor with specific analyzer
     * @param analyzer ProductivityAnalyzer to use
     */
    public ProductivityAnalysisContext(ProductivityAnalyzer analyzer) {
        if (analyzer == null) {
            throw new IllegalArgumentException("ProductivityAnalyzer cannot be null");
        }
        this.analyzer = analyzer;
        Logger.info("ProductivityAnalysisContext created with analyzer: " + analyzer.getAnalyzerName());
    }
    
    /**
     * Sets the analysis strategy
     * @param analyzer ProductivityAnalyzer to use
     */
    public void setAnalyzer(ProductivityAnalyzer analyzer) {
        if (analyzer == null) {
            throw new IllegalArgumentException("ProductivityAnalyzer cannot be null");
        }
        
        Logger.info("Switching analyzer from " + this.analyzer.getAnalyzerName() + 
                   " to " + analyzer.getAnalyzerName());
        this.analyzer = analyzer;
    }
    
    /**
     * Gets the current analyzer
     * @return Current ProductivityAnalyzer
     */
    public ProductivityAnalyzer getAnalyzer() {
        return analyzer;
    }
    
    /**
     * Performs productivity analysis on the given tasks
     * @param tasks List of tasks to analyze
     * @return Analysis result
     */
    public String performAnalysis(List<Task> tasks) {
        Logger.logMethodEntry("ProductivityAnalysisContext", "performAnalysis");
        
        try {
            if (tasks == null) {
                Logger.warn("Null task list provided for analysis");
                return "No tasks provided for analysis.";
            }
            
            Logger.info("Performing productivity analysis with " + analyzer.getAnalyzerName() + 
                       " on " + tasks.size() + " tasks");
            
            String result = analyzer.analyze(tasks);
            
            Logger.info("Productivity analysis completed successfully");
            Logger.logMethodExit("ProductivityAnalysisContext", "performAnalysis");
            
            return result;
            
        } catch (Exception e) {
            String errorMsg = "Error during productivity analysis: " + e.getMessage();
            Logger.error(errorMsg, e);
            return "Analysis failed: " + errorMsg;
        }
    }
    
    /**
     * Gets the name of the current analyzer
     * @return Analyzer name
     */
    public String getCurrentAnalyzerName() {
        return analyzer.getAnalyzerName();
    }
}
