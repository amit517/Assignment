package com.team.assignment.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.team.assignment.R;
import com.team.assignment.databinding.ActivityMainBinding;
import com.team.assignment.model.CvData;
import com.team.assignment.model.PdfUploadResponse;
import com.team.assignment.utils.ExperienceFilter;
import com.team.assignment.utils.FileUtil;
import com.team.assignment.utils.MyApplication;
import com.team.assignment.utils.SessionManager;
import com.team.assignment.viewmodel.MainActivityViewModel;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String graduationYear = "Select Graduation Year";
    private String jobNature = "Select Job Nature";
    private int  salary;
    private SessionManager sessionManager;
    private MainActivityViewModel mainActivityViewModel;
    private ProgressDialog progressDialog;
    private final int PDF_REQ_CODE = 101;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int MAX_FILE_SIZE = 4000000;
    private boolean isSizeOk = false;
    private File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
        setSpinnerValue();
        binding.experienceET.setFilters(new InputFilter[]{new ExperienceFilter("0", "100")});

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClicked();
            }
        });

        binding.selectPDFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();
            }
        });

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
    }

    private void onNextButtonClicked() {
        if (MyApplication.hasNetwork()) {
            if (isSizeOk) {
                String nameET = binding.nameET.getText().toString();
                String emailET = binding.emailET.getText().toString();
                String phoneET = binding.phoneET.getText().toString();
                String universityET = binding.universityET.getText().toString();
                String gitET = binding.gitET.getText().toString();
                String salaryString = binding.salaryET.getText().toString();

                if (checkField(nameET, emailET, phoneET, universityET, gitET, salaryString)) {


                    try {
                        JsonObject paramObject = new JsonObject();
                        paramObject.addProperty("tsync_id", sessionManager.getToken());
                        paramObject.addProperty("name", nameET);
                        paramObject.addProperty("email", emailET);
                        paramObject.addProperty("phone", phoneET);
                        if (!binding.addressET.getText().toString().isEmpty()){
                            paramObject.addProperty("full_address", binding.addressET.getText().toString());
                        }
                        paramObject.addProperty("name_of_university", universityET);
                        paramObject.addProperty("graduation_year", Integer.parseInt(graduationYear));
                        if (!binding.cgpaET.getText().toString().isEmpty()) {
                            if (checkCGPA(binding.cgpaET.getText().toString())) {
                                paramObject.addProperty("cgpa", Double.parseDouble(binding.cgpaET.getText().toString()));
                            } else {
                                binding.cgpaET.setError("CGPA not in range");
                                return;
                            }
                        }
                        if (!binding.experienceET.getText().toString().isEmpty()) {
                            paramObject.addProperty("experience_in_months", Integer.parseInt(binding.experienceET.getText().toString()));
                        }
                        if (!binding.workPlaceET.getText().toString().isEmpty()){
                            paramObject.addProperty("current_work_place_name", binding.workPlaceET.getText().toString());
                        }
                        paramObject.addProperty("applying_in", jobNature);
                        paramObject.addProperty("expected_salary", salary);
                        if (!binding.referenceET.getText().toString().isEmpty()){
                            paramObject.addProperty("field_buzz_reference", binding.referenceET.getText().toString());
                        }
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
                        Log.d("TAG", "onNextButtonClicked: " + paramObject);
                        mainActivityViewModel.sendPersonalData(paramObject).observe(MainActivity.this, new Observer<CvData>() {
                            @Override
                            public void onChanged(CvData cvData) {
                                if (cvData.getSuccess()) {
                                    mainActivityViewModel.sendUploadPdf(file, cvData.getCvFile().getId()).observe(MainActivity.this, new Observer<PdfUploadResponse>() {
                                        @Override
                                        public void onChanged(PdfUploadResponse pdfUploadResponse) {
                                            if (pdfUploadResponse.getSuccess()) {
                                                Snackbar.make(binding.getRoot(), "Successfully submitted", Snackbar.LENGTH_SHORT)
                                                        .setAction("Action", null).show();
                                                clearFields();
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    } catch (Exception e) {
                        //  Block of code to handle errors
                    }

                } else {
                    Snackbar.make(binding.getRoot(), "Please check all the mandatory fields and try again!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    if (nameET.isEmpty()) {
                        binding.nameET.setError("Name can't be empty");
                    }

                    if (emailET.isEmpty()) {
                        binding.emailET.setError("Email can't be empty");
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(emailET).matches()) {
                        binding.emailET.setError("Invalid email address");
                    }

                    if (phoneET.isEmpty()) {
                        binding.phoneET.setError("Phone number can't be empty");
                    }

                    if (universityET.isEmpty()) {
                        binding.universityET.setError("University can't be empty");
                    }

                    if (gitET.isEmpty()) {
                        binding.gitET.setError("Project url can't be empty");
                    }

                    if (checkSalary(salaryString)) {
                        Log.d("TAG", "onClick: " + checkSalary(salaryString));
                        binding.salaryET.setError("Invalid input");
                    }

                    if (getGraduationYear() == 0) {
                        binding.gradYearSpinner.setError("Must select one");
                    }

                    if (jobNature.equals("Select Job Nature")) {
                        binding.jobNatureSpinner.setError("Select a type");
                    }
                }
            } else {
                Snackbar.make(binding.getRoot(), "Please select a valid pdf to proceed", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        } else {
            Snackbar.make(binding.getRoot(), "Please check you internet!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    private void clearFields() {
        binding.nameET.setText("");
        binding.emailET.setText("");
        binding.phoneET.setText("");
        binding.addressET.setText("");
        binding.universityET.setText("");
        binding.cgpaET.setText("");
        binding.experienceET.setText("");
        binding.workPlaceET.setText("");
        binding.salaryET.setText("");
        binding.referenceET.setText("");
        binding.gitET.setText("");
        binding.gradYearSpinner.setSelection(0);
        binding.jobNatureSpinner.setSelection(0);
        binding.selectedFileNameTV.setText("Not Selected");
        file = null;
        isSizeOk = false;
    }

    private void setSpinnerValue() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.gradYearSpinner.setAdapter(adapter);
        binding.gradYearSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                graduationYear = getResources().getStringArray(R.array.options)[i];
                binding.gradYearSpinner.setError(null);
                Log.d("TAG", "onItemClick: " + graduationYear);
            }
        });
        adapter = ArrayAdapter.createFromResource(this,
                R.array.jobNature, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.jobNatureSpinner.setAdapter(adapter);
        binding.jobNatureSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jobNature = getResources().getStringArray(R.array.jobNature)[i];
                binding.jobNatureSpinner.setError(null);
                Log.d("TAG", "onItemClick: " + jobNature);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                file = FileUtil.from(this, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) {
                long fileSize = file.length();
                if (fileSize > MAX_FILE_SIZE) {
                    isSizeOk = false;
                    Toast.makeText(this, "Maximum file size 4MB", Toast.LENGTH_SHORT).show();
                } else {
                    binding.selectedFileNameTV.setText(file.getName());
                    isSizeOk = true;
                }
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            requestMultiplePermissions();
        }
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

    private void requestMultiplePermissions() {
        Dexter.withContext(this)
                .withPermissions(Arrays.asList(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ))
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseFile.setType("application/pdf");
                            chooseFile = Intent.createChooser(chooseFile, "Select CV");
                            startActivityForResult(chooseFile, PDF_REQ_CODE);

                        } else if (!report.areAllPermissionsGranted()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Need Permission")
                .setMessage("Need storage permission to select pdf. You can grant them in app settings")
                .setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openSettings();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}