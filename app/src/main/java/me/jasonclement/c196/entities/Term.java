package me.jasonclement.c196.entities;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@androidx.room.Entity(tableName = "terms")
public class Term implements Entity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Date startDate;
    private Date endDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Term() {

    }

    @Ignore
    public Term(String title, Date startDate, Date endDate) {
        setTitle(title);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public String getSubtitle() {
        return String.format("%1$tY-%1$tm-%1$td - %2$tY-%2$tm-%2$td", getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
