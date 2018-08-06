package com.paddypal.azir.paddypal.register;

public class CreateData {
    String nic,firstName,lastName,userName,password,email,userType,province,district,city;
    float fieldSize;

    public CreateData(String nic, String firstName, String lastName, String userName, String password, String email, String userType, String province, String district, String city, float fieldSize) {
        this.nic = nic;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.province = province;
        this.district = district;
        this.city = city;
        this.fieldSize = fieldSize;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFieldSize(float fieldSize) {
        this.fieldSize = fieldSize;
    }
}
