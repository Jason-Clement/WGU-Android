package me.jasonclement.c196.entities;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@androidx.room.Entity(tableName = "courses")
public class Course implements Entity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer termId;
    private String title;
    private Date startDate;
    private Date endDate;
    private String status;
    private String notes;
    private boolean hasStartDateAlert;
    private boolean hasEndDateAlert;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean getHasStartDateAlert() {
        return hasStartDateAlert;
    }

    public void setHasStartDateAlert(boolean hasStartDateAlert) {
        this.hasStartDateAlert = hasStartDateAlert;
    }

    public boolean getHasEndDateAlert() {
        return hasEndDateAlert;
    }

    public void setHasEndDateAlert(boolean hasEndDateAlert) {
        this.hasEndDateAlert = hasEndDateAlert;
    }

    public Course() {

    }

    @Ignore
    public Course(String title, Date startDate, Date endDate, String status, String notes) {
        setTitle(title);
        setTermId(null);
        setStartDate(startDate);
        setEndDate(endDate);
        setStatus(status);
        setNotes(notes);
    }

    public String getSubtitle() {
        return String.format("%1$tY-%1$tm-%1$td - %2$tY-%2$tm-%2$td", getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
