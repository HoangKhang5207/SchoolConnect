package com.schoolconnect.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "daily_reports")
public class DailyReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "meal_status", nullable = false, length = 20)
    private String mealStatus;

    @Column(name = "meal_comment", nullable = false, columnDefinition = "TEXT")
    private String mealComment;

    @Column(name = "sleep_status", nullable = false, length = 20)
    private String sleepStatus;

    @Column(name = "sleep_duration_mins")
    private Integer sleepDurationMins;

    @Column(name = "activity_summary", nullable = false, columnDefinition = "TEXT")
    private String activitySummary;

    @Column(name = "mood_status", nullable = false, length = 20)
    private String moodStatus;

    @Column(name = "teacher_note", columnDefinition = "TEXT")
    private String teacherNote;

    @Column(name = "recorded_by", nullable = false, length = 120)
    private String recordedBy;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public DailyReportEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getMealStatus() {
        return mealStatus;
    }

    public void setMealStatus(String mealStatus) {
        this.mealStatus = mealStatus;
    }

    public String getMealComment() {
        return mealComment;
    }

    public void setMealComment(String mealComment) {
        this.mealComment = mealComment;
    }

    public String getSleepStatus() {
        return sleepStatus;
    }

    public void setSleepStatus(String sleepStatus) {
        this.sleepStatus = sleepStatus;
    }

    public Integer getSleepDurationMins() {
        return sleepDurationMins;
    }

    public void setSleepDurationMins(Integer sleepDurationMins) {
        this.sleepDurationMins = sleepDurationMins;
    }

    public String getActivitySummary() {
        return activitySummary;
    }

    public void setActivitySummary(String activitySummary) {
        this.activitySummary = activitySummary;
    }

    public String getMoodStatus() {
        return moodStatus;
    }

    public void setMoodStatus(String moodStatus) {
        this.moodStatus = moodStatus;
    }

    public String getTeacherNote() {
        return teacherNote;
    }

    public void setTeacherNote(String teacherNote) {
        this.teacherNote = teacherNote;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

