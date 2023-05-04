package com.codunite.model;

public class ParamDataModel {
    private String fieldKey, paramName, datatype, minlength, maxlength, optional;

    public ParamDataModel(String fieldKey, String paramName, String datatype, String minlength, String maxlength,
                          String optional) {
        this.fieldKey = fieldKey;
        this.paramName = paramName;
        this.datatype = datatype;
        this.minlength = minlength;
        this.maxlength = maxlength;
        this.optional = optional;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getMinlength() {
        return minlength;
    }

    public void setMinlength(String minlength) {
        this.minlength = minlength;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }
}
