
package com.team.assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PdfUploadResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tsync_id")
    @Expose
    private String tsyncId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("extension")
    @Expose
    private Object extension;
    @SerializedName("description")
    @Expose
    private Object description;
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
    public PdfUploadResponse() {
    }

    /**
     * 
     * @param path
     * @param extension
     * @param code
     * @param tsyncId
     * @param success
     * @param name
     * @param description
     * @param id
     * @param message
     */
    public PdfUploadResponse(Integer id, String tsyncId, String code, String name, String path, Object extension, Object description, Boolean success, String message) {
        super();
        this.id = id;
        this.tsyncId = tsyncId;
        this.code = code;
        this.name = name;
        this.path = path;
        this.extension = extension;
        this.description = description;
        this.success = success;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTsyncId() {
        return tsyncId;
    }

    public void setTsyncId(String tsyncId) {
        this.tsyncId = tsyncId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getExtension() {
        return extension;
    }

    public void setExtension(Object extension) {
        this.extension = extension;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
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
