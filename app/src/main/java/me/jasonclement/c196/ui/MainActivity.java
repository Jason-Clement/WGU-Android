package me.jasonclement.c196.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import me.jasonclement.c196.R;
import me.jasonclement.c196.db.AppDatabase;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "me.jasonclement.c196.id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showTerms(View view) {
        Intent intent = new Intent(this, TermListActivity.class);
        startActivity(intent);
    }

    public void showCourses(View view) {
        Intent intent = new Intent(this, CourseListActivity.class);
        startActivity(intent);
    }

    public void showAssessments(View view) {
        Intent intent = new Intent(this, AssessmentListActivity.class);
        startActivity(intent);
    }

    public void showMentors(View view) {
        Intent intent = new Intent(this, MentorListActivity.class);
        startActivity(intent);
    }

    public void generateSampleData(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This will also clear all existing data. Are you sure about this?");
        builder.setPositiveButton(R.string.yes, (dialog, id) -> {
            AppDatabase db = AppDatabase.getInstance(this);
            AppDatabase.SampleDataGenerator generator = new AppDatabase.SampleDataGenerator(db);
            generator.execute();
        });
        builder.setNegativeButton(R.string.no, (dialog, id) -> { });
        builder.create().show();
    }
}
