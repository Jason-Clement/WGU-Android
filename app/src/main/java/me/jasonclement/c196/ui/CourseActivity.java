package me.jasonclement.c196.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.jasonclement.c196.R;
import me.jasonclement.c196.entities.Assessment;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.entities.Mentor;
import me.jasonclement.c196.entities.Term;
import me.jasonclement.c196.models.CourseViewModel;

import static me.jasonclement.c196.ui.MainActivity.EXTRA_ID;

public class CourseActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat;

    private EditText titleEditor;
    private TextView startDateEditor;
    private TextView endDateEditor;
    private CheckBox startDateAlertCheck;
    private CheckBox endDateAlertCheck;
    private Spinner statusSpinner;
    private EditText notesEditor;
    private Spinner termSpinner;
    private LinearLayout assessmentLayout;
    private LinearLayout mentorLayout;
    private ArrayAdapter<Term> termAdapter;
    private CourseViewModel viewModel;
    private Course course;
    private List<Term> terms;
    private List<Assessment> assessments;
    private List<Mentor> mentors;
    private List<String> statusTypes;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            boolean hasCourses = false;
            if (course == null || course.getId() <= 0) {
                Toast.makeText(this, "This course does not yet exist.", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this course?");
                builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                    viewModel.delete(course);
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
        setContentView(R.layout.activity_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusTypes = Arrays.asList(getResources().getStringArray(R.array.course_statuses));

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        titleEditor = findViewById(R.id.titleEditor);
        startDateEditor = findViewById(R.id.startDateEditor);
        endDateEditor = findViewById(R.id.endDateEditor);
        startDateAlertCheck = findViewById(R.id.startDateAlertCheck);
        endDateAlertCheck = findViewById(R.id.endDateAlertCheck);
        statusSpinner = findViewById(R.id.statusSpinner);
        notesEditor = findViewById(R.id.notesEditor);
        termSpinner = findViewById(R.id.termSpinner);
        assessmentLayout = findViewById(R.id.assessmentListLayout);
        mentorLayout = findViewById(R.id.mentorListLayout);
        termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        termSpinner.setAdapter(termAdapter);

        viewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        int courseId = getIntent().getIntExtra(EXTRA_ID, 0);

        if (courseId == 0) {
            course = new Course();
            setupLists();
        } else {
            viewModel.getCourse(courseId).observe(this, course -> {
                if (course != null) {
                    this.course = course;
                    titleEditor.setText(course.getTitle());
                    startDateEditor.setText(dateFormat.format(course.getStartDate()));
                    endDateEditor.setText(dateFormat.format(course.getEndDate()));
                    startDateAlertCheck.setChecked(course.getHasStartDateAlert());
                    endDateAlertCheck.setChecked(course.getHasEndDateAlert());
                    notesEditor.setText(course.getNotes());

                    for (int i = 0; i < statusTypes.size(); i++) {
                        if (statusTypes.get(i).equals(course.getStatus())) {
                            statusSpinner.setSelection(i);
                            break;
                        }
                    }
                }
                setupLists();
            });
        }
    }

    public void setupLists() {
        viewModel.getTerms().observe(this, terms -> {
            termAdapter.clear();
            termAdapter.add(new Term("<Unassigned>", null, null));
            termAdapter.addAll(terms);
            for (int i = 0; i < terms.size(); i++) {
                if (course != null
                        && course.getTermId() != null
                        && course.getTermId() == terms.get(i).getId()) {
                    termSpinner.setSelection(i + 1);
                    break;
                }
            }
        });
        viewModel.getAssessments().observe(this, assessments -> {
            this.assessments = assessments;
            assessmentLayout.removeAllViews();
            for (Assessment assessment : assessments) {
                AppCompatCheckBox checkBox = new AppCompatCheckBox(this);
                checkBox.setTag(assessment.getId());
                checkBox.setChecked(assessment.getCourseId() != null && course != null && assessment.getCourseId() == course.getId());
                if (assessment.getCourseId() != null && (course  == null || assessment.getCourseId() != course.getId()))
                    checkBox.setTextColor(Color.BLUE);
                checkBox.setText(assessment.getTitle());
                assessmentLayout.addView(checkBox);
            }
        });
        viewModel.getMentors().observe(this, mentors -> {
            this.mentors = mentors;
            mentorLayout.removeAllViews();
            for (Mentor mentor : mentors) {
                AppCompatCheckBox checkBox = new AppCompatCheckBox(this);
                checkBox.setTag(mentor.getId());
                checkBox.setChecked(mentor.getCourseId() != null && course != null && mentor.getCourseId() == course.getId());
                if (mentor.getCourseId() != null && (course  == null || mentor.getCourseId() != course.getId()))
                    checkBox.setTextColor(Color.BLUE);
                checkBox.setText(mentor.getTitle());
                mentorLayout.addView(checkBox);
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

        course.setTitle(titleEditor.getText().toString());
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setHasStartDateAlert(startDateAlertCheck.isChecked());
        course.setHasEndDateAlert(endDateAlertCheck.isChecked());
        course.setStatus(statusTypes.get(statusSpinner.getSelectedItemPosition()));
        course.setNotes(notesEditor.getText().toString());

        if (termSpinner.getSelectedItemPosition() == 0)
            course.setTermId(null);
        else
            course.setTermId(((Term) termSpinner.getSelectedItem()).getId());

        for (int i = 0; i < assessments.size(); i++) {
            AppCompatCheckBox checkBox = (AppCompatCheckBox)assessmentLayout.getChildAt(i);
            Assessment assessment = assessments.get(i);
            if (checkBox.isChecked())
                assessment.setCourseId(course.getId());
            else if (assessment.getCourseId() != null && assessment.getCourseId() == course.getId())
                assessment.setCourseId(null);
        }

        for (int i = 0; i < mentors.size(); i++) {
            AppCompatCheckBox checkBox = (AppCompatCheckBox)mentorLayout.getChildAt(i);
            Mentor mentor = mentors.get(i);
            if (checkBox.isChecked())
                mentor.setCourseId(course.getId());
            else if (mentor.getCourseId() != null && mentor.getCourseId() == course.getId())
                mentor.setCourseId(null);
        }

        viewModel.save(course, assessments, mentors);

        setupAlerts();

        finish();
    }

    public void setupAlerts() {
        if (course.getHasStartDateAlert()) {
            AppReceiver.createAlarm(
                    getApplicationContext(),
                    "Course Start",
                    "Your " + course.getTitle() + " course starts today.",
                    course.getStartDate(),
                    AppReceiver.COURSE_START_ID
            );
        }
        if (course.getHasEndDateAlert()) {
            AppReceiver.createAlarm(
                    getApplicationContext(),
                    "Course End",
                    "Your " + course.getTitle() + " course ends today.",
                    course.getEndDate(),
                    AppReceiver.COURSE_END_ID
            );
        }
    }

    public void shareNotes(View view) {
        if (notesEditor.getText().toString().isEmpty())
            return;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, notesEditor.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseActivity.this,
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
