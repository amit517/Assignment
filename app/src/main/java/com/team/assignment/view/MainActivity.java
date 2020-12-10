package com.team.assignment.view;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.team.assignment.R;
import com.team.assignment.databinding.ActivityMainBinding;
import com.team.assignment.utils.MyApplication;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String graduationYear = "Graduation Year";
    private String jobNature = "Job Nature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
        binding.jobNatureSpinner.setAdapter(adapter);
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

                    if (checkField(nameET, emailET, phoneET, addressET, universityET, workPlaceET, gitET)) {

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

                        if (addressET.isEmpty()) {
                            binding.addressET.setError("Address Can't Be Empty");
                        }

                        if (universityET.isEmpty()) {
                            binding.universityET.setError("University Can't Be Empty");
                        }

                        if (workPlaceET.isEmpty()) {
                            binding.workPlaceET.setError("Work Place Can't Be Empty");
                        }

                        if (gitET.isEmpty()) {
                            binding.gitET.setError("Project URL Can't Be Empty");
                        }

                    }

                } else {
                    Snackbar.make(binding.getRoot(), "Please check you internet!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        sendDataToServer();
    }

    private boolean checkField(String nameET, String emailET, String phoneET, String addressET, String universityET, String workPlaceET, String gitET) {
        return !nameET.isEmpty()
                && !emailET.isEmpty()
                && !phoneET.isEmpty()
                && !addressET.isEmpty()
                && !universityET.isEmpty()
                && !workPlaceET.isEmpty()
                && !gitET.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(emailET).matches()
                && getGraduationYear() != 0
                && !jobNature.equals("Job Nature");
    }

    private int getGraduationYear() {
        if (!graduationYear.equals("Graduation Year")) {
            return Integer.parseInt(graduationYear);
        } else {
            return 0;
        }
    }

    private void sendDataToServer() {

    }
}