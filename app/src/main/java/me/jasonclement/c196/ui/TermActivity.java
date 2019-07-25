package me.jasonclement.c196.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.jasonclement.c196.R;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.entities.Term;
import me.jasonclement.c196.models.TermViewModel;

import static me.jasonclement.c196.ui.MainActivity.EXTRA_ID;

public class TermActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat;

    private EditText titleEditor;
    private TextView startDateEditor;
    private TextView endDateEditor;
    private LinearLayout coursesLayout;
    private TermViewModel viewModel;
    private Term term;
    private List<Course> courses;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            boolean hasCourses = false;
            for (int i = 0; i < courses.size(); i++) {
                AppCompatCheckBox checkBox = (AppCompatCheckBox) coursesLayout.getChildAt(i);
                if (checkBox.isChecked()) {
                    hasCourses = true;
                }
            }
            if (term == null || term.getId() <= 0) {
                Toast.makeText(this, "This term does not yet exist.", Toast.LENGTH_LONG).show();
            } else if (hasCourses) {
                Toast.makeText(this, "This term has courses assigned to it. "
                        + "Please uncheck them from the courses list before deleting this term.", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this term?");
                builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                    viewModel.delete(term, courses);
                    finish();
                });
                builder.setNegativeButton(R.string.no, (dialog, id) -> {});
                builder.create().show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        titleEditor = findViewById(R.id.titleEditor);
        startDateEditor = findViewById(R.id.startDateEditor);
        endDateEditor = findViewById(R.id.endDateEditor);
        coursesLayout = findViewById(R.id.courseListLayout);

        viewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        int termId = getIntent().getIntExtra(EXTRA_ID, 0);

        if (termId == 0) {
            term = new Term();
            setupCourseList();
        } else {
            viewModel.getTerm(termId).observe(this, term -> {
                if (term != null) {
                    this.term = term;
                    titleEditor.setText(term.getTitle());
                    startDateEditor.setText(dateFormat.format(term.getStartDate()));
                    endDateEditor.setText(dateFormat.format(term.getEndDate()));
                }
                setupCourseList();
            });
        }
    }

    public void setupCourseList() {
        viewModel.getCourses().observe(this, courses -> {
            this.courses = courses;
            coursesLayout.removeAllViews();
            for (Course course : courses) {
                AppCompatCheckBox checkBox = new AppCompatCheckBox(this);
                checkBox.setTag(course.getId());
                checkBox.setChecked(course.getTermId() != null && term != null && course.getTermId() == term.getId());
                if (course.getTermId() != null && (term == null || course.getTermId() != term.getId()))
                    checkBox.setTextColor(Color.BLUE);
                checkBox.setText(course.getTitle());
                coursesLayout.addView(checkBox);
            }
        });
    }

    public void save(View view) {

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(startDateEditor.getText().toString());
            endDate = dateFormat.parse(endDateEditor.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String title = titleEditor.getText().toString();
        List<String> errors = new ArrayList<>();

        if (title.isEmpty()) errors.add("The title is required.");
        if (startDate == null) errors.add("The start date is required.");
        if (endDate == null) errors.add("The end date is required.");
        if (startDate != null && endDate != null && startDate.compareTo(endDate) > 0)
            errors.add("The end date cannot occur before the start date.");

         if (errors.size() > 0) {
             Toast.makeText(this, String.join(". ", errors), Toast.LENGTH_LONG).show();
             return;
         }

        term.setTitle(titleEditor.getText().toString());
        term.setStartDate(startDate);
        term.setEndDate(endDate);

        for (int i = 0; i < courses.size(); i++) {
            AppCompatCheckBox checkBox = (AppCompatCheckBox)coursesLayout.getChildAt(i);
            Course course = courses.get(i);
            if (checkBox.isChecked())
                course.setTermId(term.getId());
            else if (course.getTermId() != null && course.getTermId() == term.getId())
                course.setTermId(null);
        }

        viewModel.save(term, courses);

        finish();
    }

    public void showStartDatePicker(View view) {
        showDatePicker(true, startDateEditor.getText().toString());
    }

    public void showEndDatePicker(View view) {
        showDatePicker(false, endDateEditor.getText().toString());
    }

    public void showDatePicker(boolean forStart, String dateText) {
        Calendar calendar = Calendar.getInstance();
        if (!dateText.isEmpty()) {
            try {
                Date date = dateFormat.parse(dateText);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(TermActivity.this,
                (datePicker, year1, month1, day1) -> {
                    String text = dateFormat.format(new GregorianCalendar(year1, month1, day1).getTime());
                    if (forStart)
                        startDateEditor.setText(text);
                    else
                        endDateEditor.setText(text);
                }, year, month, day);
        datePickerDialog.show();
    }
}
