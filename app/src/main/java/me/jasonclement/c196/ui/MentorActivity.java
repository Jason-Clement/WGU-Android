package me.jasonclement.c196.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import me.jasonclement.c196.entities.Mentor;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.models.MentorViewModel;

import static me.jasonclement.c196.ui.MainActivity.EXTRA_ID;

public class MentorActivity extends AppCompatActivity {

    private EditText nameEditor;
    private EditText phoneEditor;
    private EditText emailEditor;
    private Spinner courseSpinner;
    private MentorViewModel viewModel;
    private ArrayAdapter<Course> courseAdapter;
    private Mentor mentor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            if (mentor == null || mentor.getId() <= 0) {
                Toast.makeText(this, "This mentor does not yet exist.", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this mentor?");
                builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                    viewModel.delete(mentor);
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
        setContentView(R.layout.activity_mentor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameEditor = findViewById(R.id.nameEditor);
        phoneEditor = findViewById(R.id.phoneEditor);
        emailEditor = findViewById(R.id.emailEditor);
        courseSpinner = findViewById(R.id.courseSpinner);
        courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        courseSpinner.setAdapter(courseAdapter);

        viewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        int id = getIntent().getIntExtra(EXTRA_ID, 0);

        if (id == 0) {
            mentor = new Mentor();
        } else {
            viewModel.get(id).observe(this, mentor -> {
                if (mentor != null) {
                    this.mentor = mentor;
                    nameEditor.setText(mentor.getTitle());
                    phoneEditor.setText(mentor.getPhoneNumber());
                    emailEditor.setText(mentor.getEmail());
                }
            });
        }
        viewModel.getCourses().observe(this, courses -> {
            courseAdapter.clear();
            courseAdapter.add(new Course("<Unassigned>", null, null, null, null));
            courseAdapter.addAll(courses);
            for (int i = 0; i < courses.size(); i++) {
                if (mentor != null
                        && mentor.getCourseId() != null
                        && mentor.getCourseId() == courses.get(i).getId()) {
                    courseSpinner.setSelection(i + 1);
                    break;
                }
            }
        });
    }

    public void save(View view) {

        String title = nameEditor.getText().toString();
        String phone = phoneEditor.getText().toString();
        String email = emailEditor.getText().toString();
        List<String> errors = new ArrayList<>();

        if (title.isEmpty()) errors.add("The title is required.");
        if (phone.isEmpty()) errors.add("The phone number is required.");
        if (email.isEmpty()) errors.add("The e-mail address is required.");

        if (errors.size() > 0) {
            Toast.makeText(this, String.join(". ", errors), Toast.LENGTH_LONG).show();
            return;
        }

        mentor.setName(nameEditor.getText().toString());
        mentor.setPhoneNumber(phone);
        mentor.setEmail(email);
        mentor.setCourseId(null);

        if (courseSpinner.getSelectedItemPosition() == 0)
            mentor.setCourseId(null);
        else
            mentor.setCourseId(((Course)courseSpinner.getSelectedItem()).getId());

        viewModel.save(mentor);

        finish();
    }
}
