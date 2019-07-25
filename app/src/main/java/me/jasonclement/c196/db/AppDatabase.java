package me.jasonclement.c196.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import me.jasonclement.c196.entities.Assessment;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.entities.Mentor;
import me.jasonclement.c196.entities.Term;

@Database(entities = {Assessment.class, Course.class, Mentor.class, Term.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract AssessmentDao assessmentDao();
    public abstract CourseDao courseDao();
    public abstract MentorDao mentorDao();
    public abstract TermDao termDao();

    private static volatile AppDatabase instance;
    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "c196_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(dbInitCallback)
                        .build();
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback dbInitCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //new SampleDataGenerator(instance).execute();
        }
    };

    public static class SampleDataGenerator extends AsyncTask<Void, Void, Void> {

        private final TermDao termDao;
        private final CourseDao courseDao;
        private final AssessmentDao assessmentDao;
        private final MentorDao mentorDao;

        public SampleDataGenerator(AppDatabase db) {
            termDao = db.termDao();
            courseDao = db.courseDao();
            assessmentDao = db.assessmentDao();
            mentorDao = db.mentorDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            termDao.deleteAll();
            courseDao.deleteAll();
            assessmentDao.deleteAll();
            mentorDao.deleteAll();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            try {
                termDao.insert(new Term("Term 1", df.parse("2019-01-01"), df.parse("2019-06-30")));
                termDao.insert(new Term("Term 2", df.parse("2019-07-01"), df.parse("2019-12-31")));
                courseDao.insert(new Course("Course 1", df.parse("2019-01-01"), df.parse("2019-02-28"), "Completed", "This is course number 1."));
                courseDao.insert(new Course("Course 2", df.parse("2019-03-01"), df.parse("2019-04-30"), "Dropped", "I didn't like this one."));
                assessmentDao.insert(new Assessment("Objective", "Assessment 1", df.parse("2019-04-01"), df.parse("2019-02-01")));
                assessmentDao.insert(new Assessment("Performance", "Assessment 2", df.parse("2019-04-01"), df.parse("2019-02-01")));
                mentorDao.insert(new Mentor("Bob", "123-456-7894", "bob@wgu.edu"));
                mentorDao.insert(new Mentor("Alice", "123-456-7895", "alice@wgu.edu"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
