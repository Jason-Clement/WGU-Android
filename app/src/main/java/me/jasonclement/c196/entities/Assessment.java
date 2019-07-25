package me.jasonclement.c196.entities;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@androidx.room.Entity(tableName = "assessments")
public class Assessment implements Entity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer courseId;
    private String type;
    private String title;
    private Date dueDate;
    private boolean hasDueDateAlert;
    private Date goalDate;
    private boolean hasGoalDateAlert;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(Date goalDate) {
        this.goalDate = goalDate;
    }

    public boolean getHasGoalDateAlert() {
        return hasGoalDateAlert;
    }

    public void setHasGoalDateAlert(boolean hasGoalDateAlert) {
        this.hasGoalDateAlert = hasGoalDateAlert;
    }

    public boolean getHasDueDateAlert() {
        return hasDueDateAlert;
    }

    public void setHasDueDateAlert(boolean hasDueDateAlert) {
        this.hasDueDateAlert = hasDueDateAlert;
    }

    public Assessment() {

    }

    @Ignore
    public Assessment(String type, String title, Date dueDate, Date goalDate) {
        setType(type);
        setTitle(title);
        setDueDate(dueDate);
        setGoalDate(goalDate);
    }

    public String getSubtitle() {
        return String.format("Goal: %1$tY-%1$tm-%1$td, Due: %2$tY-%2$tm-%2$td", getGoalDate(), getDueDate());
    }

    @Override
    public String toString() { return getTitle(); }
}
