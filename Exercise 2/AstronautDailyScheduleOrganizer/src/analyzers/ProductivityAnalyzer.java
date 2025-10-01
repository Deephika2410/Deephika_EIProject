package analyzers;

import models.Task;
import models.PriorityLevel;
import utils.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Interface for productivity analysis strategies
 * Implements Strategy Design Pattern
 * Follows SOLID principles with open-closed principle
 */
public interface ProductivityAnalyzer {
    
    /**
     * Analyzes the given tasks and provides recommendations
     * @param tasks List of tasks to analyze
     * @return Analysis result with recommendations
     */
    String analyze(List<Task> tasks);
    
    /**
     * Gets the name of this analyzer
     * @return Analyzer name
     */
    String getAnalyzerName();
}

/**
 * Comprehensive productivity analyzer implementation
 * Analyzes workload distribution, priority balance, and time management
 */
class ComprehensiveProductivityAnalyzer implements ProductivityAnalyzer {
    
    private static final int RECOMMENDED_BREAK_MINUTES = 15;
    private static final int MAX_CONTINUOUS_WORK_MINUTES = 120; // 2 hours
    private static final double HIGH_PRIORITY_THRESHOLD = 0.4; // 40% of tasks
    private static final int MIN_TASK_DURATION = 30; // 30 minutes
    private static final int MAX_DAILY_WORK_HOURS = 10;
    
    @Override
    public String analyze(List<Task> tasks) {
        Logger.logMethodEntry("ComprehensiveProductivityAnalyzer", "analyze");
        
        if (tasks == null || tasks.isEmpty()) {
            return "No tasks scheduled for analysis.";
        }
        
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== PRODUCTIVITY ANALYSIS REPORT ===\n\n");
        
        // Basic statistics
        analysis.append("SCHEDULE OVERVIEW:\n");
        analysis.append(String.format("- Total tasks: %d\n", tasks.size()));
        analysis.append(String.format("- Completed tasks: %d\n", getCompletedTaskCount(tasks)));
        analysis.append(String.format("- Pending tasks: %d\n", tasks.size() - getCompletedTaskCount(tasks)));
        analysis.append(String.format("- Total scheduled time: %.1f hours\n\n", getTotalScheduledHours(tasks)));
        
        // Priority distribution analysis
        analysis.append(analyzePriorityDistribution(tasks));
        
        // Time management analysis
        analysis.append(analyzeTimeManagement(tasks));
        
        // Workload analysis
        analysis.append(analyzeWorkload(tasks));
        
        // Break analysis
        analysis.append(analyzeBreaks(tasks));
        
        // Recommendations
        analysis.append(generateRecommendations(tasks));
        
        Logger.logMethodExit("ComprehensiveProductivityAnalyzer", "analyze");
        return analysis.toString();
    }
    
    @Override
    public String getAnalyzerName() {
        return "Comprehensive Productivity Analyzer";
    }
    
    private int getCompletedTaskCount(List<Task> tasks) {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }
    
    private double getTotalScheduledHours(List<Task> tasks) {
        return tasks.stream()
            .mapToLong(Task::getDurationMinutes)
            .sum() / 60.0;
    }
    
    private String analyzePriorityDistribution(List<Task> tasks) {
        StringBuilder result = new StringBuilder();
        result.append("PRIORITY DISTRIBUTION ANALYSIS:\n");
        
        Map<PriorityLevel, Long> priorityCount = tasks.stream()
            .collect(Collectors.groupingBy(Task::getPriorityLevel, Collectors.counting()));
        
        for (PriorityLevel priority : PriorityLevel.values()) {
            long count = priorityCount.getOrDefault(priority, 0L);
            double percentage = (double) count / tasks.size() * 100;
            result.append(String.format("- %s priority: %d tasks (%.1f%%)\n", 
                        priority.getDisplayName(), count, percentage));
        }
        
        // Analysis
        long highPriorityCount = priorityCount.getOrDefault(PriorityLevel.HIGH, 0L) + 
                               priorityCount.getOrDefault(PriorityLevel.CRITICAL, 0L);
        double highPriorityPercentage = (double) highPriorityCount / tasks.size();
        
        if (highPriorityPercentage > HIGH_PRIORITY_THRESHOLD) {
            result.append("\nWARNING: High percentage of high-priority tasks detected!\n");
        }
        
        result.append("\n");
        return result.toString();
    }
    
    private String analyzeTimeManagement(List<Task> tasks) {
        StringBuilder result = new StringBuilder();
        result.append("TIME MANAGEMENT ANALYSIS:\n");
        
        // Sort tasks by start time
        List<Task> sortedTasks = tasks.stream()
            .sorted((t1, t2) -> t1.getStartTime().compareTo(t2.getStartTime()))
            .collect(Collectors.toList());
        
        // Check for optimal task durations
        long shortTasks = tasks.stream()
            .filter(task -> task.getDurationMinutes() < MIN_TASK_DURATION)
            .count();
        
        if (shortTasks > 0) {
            result.append(String.format("- %d tasks are shorter than %d minutes (may be too fragmented)\n", 
                        shortTasks, MIN_TASK_DURATION));
        }
        
        // Check time distribution throughout the day
        analyzeTimeDistribution(sortedTasks, result);
        
        result.append("\n");
        return result.toString();
    }
    
    private void analyzeTimeDistribution(List<Task> sortedTasks, StringBuilder result) {
        if (sortedTasks.isEmpty()) return;
        
        LocalTime earliestStart = sortedTasks.get(0).getStartTime();
        LocalTime latestEnd = sortedTasks.stream()
            .map(Task::getEndTime)
            .max(LocalTime::compareTo)
            .orElse(LocalTime.MIDNIGHT);
        
        long totalDaySpan = Duration.between(earliestStart, latestEnd).toMinutes();
        double workHours = totalDaySpan / 60.0;
        
        result.append(String.format("- Schedule spans from %s to %s (%.1f hours)\n", 
                    earliestStart, latestEnd, workHours));
        
        if (workHours > MAX_DAILY_WORK_HOURS) {
            result.append("WARNING: Schedule spans more than recommended daily work hours!\n");
        }
    }
    
    private String analyzeWorkload(List<Task> tasks) {
        StringBuilder result = new StringBuilder();
        result.append("WORKLOAD ANALYSIS:\n");
        
        // Analyze priority vs time allocation
        Map<PriorityLevel, Double> priorityTimeMap = tasks.stream()
            .collect(Collectors.groupingBy(
                Task::getPriorityLevel,
                Collectors.summingDouble(Task::getDurationMinutes)
            ));
        
        double totalMinutes = tasks.stream().mapToLong(Task::getDurationMinutes).sum();
        
        for (PriorityLevel priority : PriorityLevel.values()) {
            double minutes = priorityTimeMap.getOrDefault(priority, 0.0);
            double percentage = totalMinutes > 0 ? (minutes / totalMinutes) * 100 : 0;
            
            if (minutes > 0) {
                result.append(String.format("- %s priority tasks: %.1f hours (%.1f%% of total time)\n", 
                            priority.getDisplayName(), minutes / 60, percentage));
            }
        }
        
        result.append("\n");
        return result.toString();
    }
    
    private String analyzeBreaks(List<Task> tasks) {
        StringBuilder result = new StringBuilder();
        result.append("BREAK ANALYSIS:\n");
        
        List<Task> sortedTasks = tasks.stream()
            .sorted((t1, t2) -> t1.getStartTime().compareTo(t2.getStartTime()))
            .collect(Collectors.toList());
        
        List<Duration> gaps = new ArrayList<>();
        List<Duration> continuousWork = new ArrayList<>();
        
        for (int i = 0; i < sortedTasks.size() - 1; i++) {
            Task current = sortedTasks.get(i);
            Task next = sortedTasks.get(i + 1);
            
            Duration gap = Duration.between(current.getEndTime(), next.getStartTime());
            if (gap.toMinutes() > 0) {
                gaps.add(gap);
            }
            
            // Check for continuous work periods
            if (gap.toMinutes() < RECOMMENDED_BREAK_MINUTES) {
                Duration workPeriod = Duration.between(current.getStartTime(), next.getEndTime());
                continuousWork.add(workPeriod);
            }
        }
        
        long avgBreakMinutes = gaps.stream()
            .mapToLong(Duration::toMinutes)
            .filter(minutes -> minutes > 0)
            .sum() / Math.max(gaps.size(), 1);
        
        result.append(String.format("- Number of breaks: %d\n", gaps.size()));
        result.append(String.format("- Average break duration: %d minutes\n", avgBreakMinutes));
        
        long longWorkPeriods = continuousWork.stream()
            .filter(duration -> duration.toMinutes() > MAX_CONTINUOUS_WORK_MINUTES)
            .count();
        
        if (longWorkPeriods > 0) {
            result.append(String.format("WARNING: %d continuous work periods exceed %d minutes!\n", 
                        longWorkPeriods, MAX_CONTINUOUS_WORK_MINUTES));
        }
        
        result.append("\n");
        return result.toString();
    }
    
    private String generateRecommendations(List<Task> tasks) {
        StringBuilder result = new StringBuilder();
        result.append("RECOMMENDATIONS:\n");
        
        List<String> recommendations = new ArrayList<>();
        
        // Priority-based recommendations
        Map<PriorityLevel, Long> priorityCount = tasks.stream()
            .collect(Collectors.groupingBy(Task::getPriorityLevel, Collectors.counting()));
        
        long highPriorityCount = priorityCount.getOrDefault(PriorityLevel.HIGH, 0L) + 
                               priorityCount.getOrDefault(PriorityLevel.CRITICAL, 0L);
        double highPriorityPercentage = (double) highPriorityCount / tasks.size();
        
        if (highPriorityPercentage > HIGH_PRIORITY_THRESHOLD) {
            recommendations.add("Consider redistributing high-priority tasks across multiple days");
            recommendations.add("Evaluate if all high-priority tasks are truly urgent");
        }
        
        // Time management recommendations
        long shortTasks = tasks.stream()
            .filter(task -> task.getDurationMinutes() < MIN_TASK_DURATION)
            .count();
        
        if (shortTasks > 2) {
            recommendations.add("Consider batching short tasks together for better efficiency");
        }
        
        // Break recommendations
        List<Task> sortedTasks = tasks.stream()
            .sorted((t1, t2) -> t1.getStartTime().compareTo(t2.getStartTime()))
            .collect(Collectors.toList());
        
        for (int i = 0; i < sortedTasks.size() - 1; i++) {
            Task current = sortedTasks.get(i);
            Task next = sortedTasks.get(i + 1);
            
            Duration gap = Duration.between(current.getEndTime(), next.getStartTime());
            if (gap.toMinutes() == 0) {
                recommendations.add("Add breaks between consecutive tasks for better productivity");
                break;
            }
        }
        
        // Workload recommendations
        double totalHours = getTotalScheduledHours(tasks);
        if (totalHours > MAX_DAILY_WORK_HOURS) {
            recommendations.add("Consider spreading tasks across multiple days - current schedule exceeds recommended daily limit");
        }
        
        // Generic recommendations
        if (recommendations.isEmpty()) {
            recommendations.add("Schedule looks well-balanced! Continue maintaining good task distribution");
            recommendations.add("Consider adding buffer time between tasks for unexpected delays");
        }
        
        for (int i = 0; i < recommendations.size(); i++) {
            result.append(String.format("%d. %s\n", i + 1, recommendations.get(i)));
        }
        
        result.append("\n=== END OF ANALYSIS ===");
        
        return result.toString();
    }
}
