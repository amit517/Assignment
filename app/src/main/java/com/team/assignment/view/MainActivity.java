package com.team.assignment.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.team.assignment.R;
import com.team.assignment.databinding.ActivityMainBinding;
import com.team.assignment.model.CvData;
import com.team.assignment.utils.ExperienceFilter;
import com.team.assignment.utils.MyApplication;
import com.team.assignment.utils.SessionManager;
import com.team.assignment.viewmodel.LoginActivityViewModel;
import com.team.assignment.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String graduationYear = "Select Graduation Year";
    private String jobNature = "Select Job Nature";
    private double cgpa;
    private int experience, salary;
    private SessionManager sessionManager;
    private MainActivityViewModel mainActivityViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
        binding.experienceET.setFilters(new InputFilter[]{new ExperienceFilter("0", "100")});

        mainActivityViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.gradYearSpinner.setAdapter(adapter);
        binding.gradYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                graduationYear = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.jobNature, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.jobNatureSpinner.setAdapter(adapter2);
        binding.jobNatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobNature = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.hasNetwork()) {
                    String nameET = binding.nameET.getText().toString();
                    String emailET = binding.emailET.getText().toString();
                    String phoneET = binding.phoneET.getText().toString();
                    String addressET = binding.addressET.getText().toString();
                    String universityET = binding.universityET.getText().toString();
                    String workPlaceET = binding.workPlaceET.getText().toString();
                    String referenceET = binding.referenceET.getText().toString();
                    String gitET = binding.gitET.getText().toString();
                    String salaryString = binding.salaryET.getText().toString();

                    if (checkField(nameET, emailET, phoneET, universityET, gitET, salaryString)) {

                        if (!binding.cgpaET.getText().toString().isEmpty()) {
                            if (checkCGPA(binding.cgpaET.getText().toString())) {
                                cgpa = Double.parseDouble(binding.cgpaET.getText().toString());
                            } else {
                                binding.cgpaET.setError("CGPA not in range");
                                return;
                            }
                        }
                        if (!binding.experienceET.getText().toString().isEmpty()) {
                            experience = Integer.parseInt(binding.experienceET.getText().toString());
                        }

                        try {
                            JsonObject paramObject = new JsonObject();
                            paramObject.addProperty("tsync_id", sessionManager.getToken());
                            paramObject.addProperty("name", nameET);
                            paramObject.addProperty("email", emailET);
                            paramObject.addProperty("phone", phoneET);
                            paramObject.addProperty("full_address", addressET);
                            paramObject.addProperty("name_of_university", universityET);
                            paramObject.addProperty("graduation_year", graduationYear);
                            paramObject.addProperty("cgpa", cgpa);
                            paramObject.addProperty("experience_in_months", experience);
                            paramObject.addProperty("current_work_place_name", workPlaceET);
                            paramObject.addProperty("applying_in", jobNature);
                            paramObject.addProperty("expected_salary", salary);
                            paramObject.addProperty("field_buzz_reference", referenceET);
                            paramObject.addProperty("github_project_url", gitET);
                            JsonObject innerObject = new JsonObject();
                            innerObject.addProperty("tsync_id", sessionManager.getToken());
                            paramObject.add("cv_file", innerObject);
                            paramObject.addProperty("on_spot_update_time", System.currentTimeMillis() / 1000L);

                            if (!sessionManager.getHasUpdated()) {
                                sessionManager.setHasUpdated(true);
                                long unixTime = System.currentTimeMillis() / 1000L;
                                sessionManager.setSpotCreationTime(unixTime);
                                paramObject.addProperty("on_spot_creation_time", sessionManager.getSpotCreationTime());
                            } else {
                                paramObject.addProperty("on_spot_creation_time", sessionManager.getSpotCreationTime());
                            }
                            mainActivityViewModel.sendPersonalData(paramObject).observe(MainActivity.this, new Observer<CvData>() {
                                @Override
                                public void onChanged(CvData cvData) {
                                    Toast.makeText(MainActivity.this, cvData.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (Exception e) {
                            //  Block of code to handle errors
                        }

                    } else {
                        Snackbar.make(binding.getRoot(), "Please check all the mandatory fields and try again!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        if (nameET.isEmpty()) {
                            binding.nameET.setError("Name Can't Be Empty");
                        }

                        if (emailET.isEmpty()) {
                            binding.emailET.setError("Email Can't Be Empty");
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailET).matches()) {
                            binding.emailET.setError("Invalid email address");
                        }

                        if (phoneET.isEmpty()) {
                            binding.phoneET.setError("Phone Number Can't Be Empty");
                        }

                        if (universityET.isEmpty()) {
                            binding.universityET.setError("University Can't Be Empty");
                        }

                        if (gitET.isEmpty()) {
                            binding.gitET.setError("Project URL Can't Be Empty");
                        }

                        /*if (getGraduationYear() == 0) {
                            binding.gradTV.setError("Select One");
                        } else {

                        }

                        if (jobNature.equals("Select Job Nature")) {
                            binding.jobNatureTV.setError("Select One");
                        }*/

                        if (checkSalary(salaryString)) {
                            Log.d("TAG", "onClick: " + checkSalary(salaryString));
                            binding.salaryET.setError("Invaild input");
                        }
                    }

                } else {
                    Snackbar.make(binding.getRoot(), "Please check you internet!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void init() {
        progressDialog = new ProgressDialog(this, R.style.ProgressDialogColour);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(this);
        sessionManager = new SessionManager(this);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private boolean checkField(String nameET, String emailET, String phoneET, String universityET, String gitET, String salaryString) {
        return !nameET.isEmpty()
                && !emailET.isEmpty()
                && !phoneET.isEmpty()
                && !universityET.isEmpty()
                && !gitET.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(emailET).matches()
                && getGraduationYear() != 0
                && !jobNature.equals("Select Job Nature")
                && !checkSalary(salaryString);
    }

    private int getGraduationYear() {
        if (!graduationYear.equals("Select Graduation Year")) {
            return Integer.parseInt(graduationYear);
        } else {
            return 0;
        }
    }

    private boolean checkCGPA(String s) {
        double num = Double.parseDouble(s);
        return (num >= 2 && num <= 4);
    }

    private boolean checkSalary(String salaryString) {

        if (salaryString.isEmpty()) {
            return true;
        } else {
            salary = Integer.parseInt(salaryString);
            return !checkSalaryRange(salary);
        }
    }

    private boolean checkSalaryRange(int salary) {
        return (salary >= 15000 && salary <= 60000);
    }

    private void showProgressBar() {
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }
}