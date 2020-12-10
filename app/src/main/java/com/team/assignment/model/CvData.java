
package com.team.assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CvData {

    @SerializedName("tsync_id")
    @Expose
    private String tsyncId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("full_address")
    @Expose
    private String fullAddress;
    @SerializedName("name_of_university")
    @Expose
    private String nameOfUniversity;
    @SerializedName("graduation_year")
    @Expose
    private Integer graduationYear;
    @SerializedName("cgpa")
    @Expose
    private Float cgpa;
    @SerializedName("experience_in_months")
    @Expose
    private Integer experienceInMonths;
    @SerializedName("current_work_place_name")
    @Expose
    private String currentWorkPlaceName;
    @SerializedName("applying_in")
    @Expose
    private String applyingIn;
    @SerializedName("expected_salary")
    @Expose
    private Integer expectedSalary;
    @SerializedName("field_buzz_reference")
    @Expose
    private String fieldBuzzReference;
    @SerializedName("github_project_url")
    @Expose
    private String githubProjectUrl;
    @SerializedName("cv_file")
    @Expose
    private CvFile cvFile;
    @SerializedName("on_spot_update_time")
    @Expose
    private Integer onSpotUpdateTime;
    @SerializedName("on_spot_creation_time")
    @Expose
    private Integer onSpotCreationTime;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CvData() {
    }

    /**
     * 
     * @param tsyncId
     * @param applyingIn
     * @param onSpotCreationTime
     * @param experienceInMonths
     * @param message
     * @param nameOfUniversity
     * @param onSpotUpdateTime
     * @param fieldBuzzReference
     * @param phone
     * @param currentWorkPlaceName
     * @param githubProjectUrl
     * @param success
     * @param name
     * @param fullAddress
     * @param graduationYear
     * @param cgpa
     * @param expectedSalary
     * @param email
     * @param cvFile
     */
    public CvData(String tsyncId, String name, String email, String phone, String fullAddress, String nameOfUniversity, Integer graduationYear, Float cgpa, Integer experienceInMonths, String currentWorkPlaceName, String applyingIn, Integer expectedSalary, String fieldBuzzReference, String githubProjectUrl, CvFile cvFile, Integer onSpotUpdateTime, Integer onSpotCreationTime, Boolean success, String message) {
        super();
        this.tsyncId = tsyncId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.fullAddress = fullAddress;
        this.nameOfUniversity = nameOfUniversity;
        this.graduationYear = graduationYear;
        this.cgpa = cgpa;
        this.experienceInMonths = experienceInMonths;
        this.currentWorkPlaceName = currentWorkPlaceName;
        this.applyingIn = applyingIn;
        this.expectedSalary = expectedSalary;
        this.fieldBuzzReference = fieldBuzzReference;
        this.githubProjectUrl = githubProjectUrl;
        this.cvFile = cvFile;
        this.onSpotUpdateTime = onSpotUpdateTime;
        this.onSpotCreationTime = onSpotCreationTime;
        this.success = success;
        this.message = message;
    }

    public String getTsyncId() {
        return tsyncId;
    }

    public void setTsyncId(String tsyncId) {
        this.tsyncId = tsyncId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getNameOfUniversity() {
        return nameOfUniversity;
    }

    public void setNameOfUniversity(String nameOfUniversity) {
        this.nameOfUniversity = nameOfUniversity;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Float getCgpa() {
        return cgpa;
    }

    public void setCgpa(Float cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getExperienceInMonths() {
        return experienceInMonths;
    }

    public void setExperienceInMonths(Integer experienceInMonths) {
        this.experienceInMonths = experienceInMonths;
    }

    public String getCurrentWorkPlaceName() {
        return currentWorkPlaceName;
    }

    public void setCurrentWorkPlaceName(String currentWorkPlaceName) {
        this.currentWorkPlaceName = currentWorkPlaceName;
    }

    public String getApplyingIn() {
        return applyingIn;
    }

    public void setApplyingIn(String applyingIn) {
        this.applyingIn = applyingIn;
    }

    public Integer getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(Integer expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getFieldBuzzReference() {
        return fieldBuzzReference;
    }

    public void setFieldBuzzReference(String fieldBuzzReference) {
        this.fieldBuzzReference = fieldBuzzReference;
    }

    public String getGithubProjectUrl() {
        return githubProjectUrl;
    }

    public void setGithubProjectUrl(String githubProjectUrl) {
        this.githubProjectUrl = githubProjectUrl;
    }

    public CvFile getCvFile() {
        return cvFile;
    }

    public void setCvFile(CvFile cvFile) {
        this.cvFile = cvFile;
    }

    public Integer getOnSpotUpdateTime() {
        return onSpotUpdateTime;
    }

    public void setOnSpotUpdateTime(Integer onSpotUpdateTime) {
        this.onSpotUpdateTime = onSpotUpdateTime;
    }

    public Integer getOnSpotCreationTime() {
        return onSpotCreationTime;
    }

    public void setOnSpotCreationTime(Integer onSpotCreationTime) {
        this.onSpotCreationTime = onSpotCreationTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
