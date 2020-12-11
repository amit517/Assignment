package com.team.assignment.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.TextUtils;
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
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.team.assignment.R;
import com.team.assignment.apicom.RetrofitClient;
import com.team.assignment.databinding.ActivityMainBinding;
import com.team.assignment.model.CvData;
import com.team.assignment.model.PdfUploadResponse;
import com.team.assignment.utils.ExperienceFilter;
import com.team.assignment.utils.FileUtil;
import com.team.assignment.utils.MyApplication;
import com.team.assignment.utils.SessionManager;
import com.team.assignment.viewmodel.LoginActivityViewModel;
import com.team.assignment.viewmodel.MainActivityViewModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String graduationYear = "Select Graduation Year";
    private String jobNature = "Select Job Nature";
    private double cgpa;
    private int experience, salary;
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
                    if (isSizeOk){
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
                                        mainActivityViewModel.sendUploadPdf(file,cvData.getCvFile().getId()).observe(MainActivity.this, new Observer<PdfUploadResponse>() {
                                            @Override
                                            public void onChanged(PdfUploadResponse pdfUploadResponse) {
                                                Snackbar.make(binding.getRoot(), pdfUploadResponse.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .setAction("Action", null).show();
                                            }
                                        });
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

                            if (checkSalary(salaryString)) {
                                Log.d("TAG", "onClick: " + checkSalary(salaryString));
                                binding.salaryET.setError("Invaild input");
                            }
                        }
                    }else {
                        Snackbar.make(binding.getRoot(), "Please select a valid pdf to proceed", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }

                } else {
                    Snackbar.make(binding.getRoot(), "Please check you internet!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }
        });

        binding.selectPDFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();
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

}