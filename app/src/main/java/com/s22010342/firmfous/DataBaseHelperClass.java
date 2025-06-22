package com.s22010342.firmfous;

public class DataBaseHelperClass {

    String name;
    String organization_code;
    String username;
    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization_code() {
        return organization_code;
    }

    public void setOrganization_code(String organization_code) {
        this.organization_code = organization_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DataBaseHelperClass(String name, String organization_code, String password, String username) {
        this.name = name;
        this.organization_code = organization_code;
        this.password = password;
        this.username = username;
    }


    public DataBaseHelperClass() {

    }
}
