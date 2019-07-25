package me.jasonclement.c196.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import me.jasonclement.c196.entities.Assessment;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.models.AssessmentViewModel;

import static me.jasonclement.c196.ui.MainActivity.EXTRA_ID;

public class AssessmentActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat;

    private EditText titleEditor;
    private RadioGroup typeGroup;
    private RadioButton objectiveCheck;
    private RadioButton performanceCheck;
    private TextView dueDateEditor;
    private TextView goalDateEditor;
    private CheckBox dueDateAlertCheck;
    private CheckBox goalDateAlertCheck;
    private Spinner courseSpinner;
    private AssessmentViewModel viewModel;
    private ArrayAdapter<Course> courseAdapter;
    private Assessment assessment;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            if (assessment == null || assessment.getId() <= 0) {
                Toast.makeText(this, "This assessment does not yet exist.", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this assessment?");
                builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                    viewModel.delete(assessment);
                    finish();
                });
                builder.setNegativeButton(R.string.no, (dialog, id) -> {
                });
                builder.create().show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        titleEditor = findViewById(R.id.titleEditor);
        typeGroup = findViewById(R.id.typeGroup);
        objectiveCheck = findViewById(R.id.objectiveCheck);
        performanceCheck = findViewById(R.id.performanceCheck);
        dueDateEditor = findViewById(R.id.dueDateEditor);
        goalDateEditor = findViewById(R.id.goalDateEditor);
        dueDateAlertCheck = findViewById(R.id.dueDateAlertCheck);
        goalDateAlertCheck = findViewById(R.id.goalDateAlertCheck);
        courseSpinner = findViewById(R.id.courseSpinner);
        courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        courseSpinner.setAdapter(courseAdapter);

        viewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);

        int id = getIntent().getIntExtra(EXTRA_ID, 0);

        if (id == 0) {
            assessment = new Assessment();
            setupCourses();
        } else {
            viewModel.get(id).observe(this, assessment -> {
                if (assessment != null) {
                    this.assessment = assessment;
                    titleEditor.setText(assessment.getTitle());
                    if (assessment.getType().equals("Performance"))
                        performanceCheck.setChecked(true);
                    else
                        objectiveCheck.setChecked(true);
                    dueDateEditor.setText(dateFormat.format(assessment.getDueDate()));
                    goalDateEditor.setText(dateFormat.format(assessment.getGoalDate()));
                    dueDateAlertCheck.setChecked(assessment.getHasDueDateAlert());
                    goalDateAlertCheck.setChecked(assessment.getHasGoalDateAlert());
                }
                setupCourses();
            });
        }
    }

    private void setupCourses() {
        viewModel.getCourses().observe(this, courses -> {
            courseAdapter.clear();
            courseAdapter.add(new Course("<Unassigned>", null, null, null, null));
            courseAdapter.addAll(courses);
            for (int i = 0; i < courses.size(); i++) {
                if (assessment != null
                        && assessment.getCourseId() != null
                        && assessment.getCourseId() == courses.get(i).getId()) {
                    courseSpinner.setSelection(i + 1);
                    break;
                }
            }
        });
    }

    public void save(View view) {

        Date dueDate = null;
        Date goalDate = null;
        try {
            dueDate = dateFormat.parse(dueDateEditor.getText().toString());
            goalDate = dateFormat.parse(goalDateEditor.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String title = titleEditor.getText().toString();
        List<String> errors = new ArrayList<>();

        if (title.isEmpty()) errors.add("The title is required.");
        if (dueDate == null) errors.add("The due date is required.");
        if (goalDate == null) errors.add("The goal date is required.");
        if (dueDate != null && goalDate != null && goalDate.compareTo(dueDate) > 0)
            errors.add("The goal date must occur before or on the due date.");

        if (errors.size() > 0) {
            Toast.makeText(this, String.join(". ", errors), Toast.LENGTH_LONG).show();
            return;
        }

        assessment.setTitle(titleEditor.getText().toString());
        assessment.setType(performanceCheck.isChecked() ? "Performance" : "Objective");
        assessment.setDueDate(dueDate);
        assessment.setGoalDate(goalDate);
        assessment.setHasDueDateAlert(dueDateAlertCheck.isChecked());
        assessment.setHasGoalDateAlert(goalDateAlertCheck.isChecked());
        assessment.setCourseId(null);

        if (courseSpinner.getSelectedItemPosition() == 0)
            assessment.setCourseId(null);
        else
            assessment.setCourseId(((Course) courseSpinner.getSelectedItem()).getId());

        viewModel.save(assessment);

        setupAlerts();

        finish();
    }

    public void setupAlerts() {
        if (assessment.getHasDueDateAlert()) {
            AppReceiver.createAlarm(
                    getApplicationContext(),
                    "Assessment Due",
                    "Your " + assessment.getTitle() + " assessment is due today.",
                    assessment.getDueDate(),
                    AppReceiver.ASSESSMENT_DUE_ID
            );
        }
        if (assessment.getHasGoalDateAlert()) {
            AppReceiver.createAlarm(
                    getApplicationContext(),
                    "Assessment Goal",
                    "Your goal is today for your " + assessment.getTitle() + " assessment.",
                    assessment.getGoalDate(),
                    AppReceiver.ASSESSMENT_GOAL_ID
            );
        }
    }

    public void showDueDatePicker(View view) {
        showDatePicker(true, dueDateEditor.getText().toString());
    }

    public void showGoalDatePicker(View view) {
        showDatePicker(false, goalDateEditor.getText().toString());
    }

    public void showDatePicker(boolean forDueDate, String dateText) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AssessmentActivity.this,
                (datePicker, year1, month1, day1) -> {
                    String text = dateFormat.format(new GregorianCalendar(year1, month1, day1).getTime());
                    if (forDueDate)
                        dueDateEditor.setText(text);
                    else
                        goalDateEditor.setText(text);
                }, year, month, day);
        datePickerDialog.show();
    }
}
