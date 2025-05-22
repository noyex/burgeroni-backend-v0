package com.noyex.data.dtos;

public class OrderOnSiteDto {
    private String firstName;
    private String lastName;
    private String userEmail;
    private String phone;
    private String address;
    private String addressSecondLine;
    private String city;
    private String zipCode;
    private String comment;
    private String orderType;

    public OrderOnSiteDto() {
    }

    public OrderOnSiteDto(String firstName, String lastName, String userEmail, String phone, String address, String addressSecondLine, String city, String zipCode, String comment, String orderType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.phone = phone;
        this.address = address;
        this.addressSecondLine = addressSecondLine;
        this.city = city;
        this.zipCode = zipCode;
        this.comment = comment;
        this.orderType = orderType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressSecondLine() {
        return addressSecondLine;
    }

    public void setAddressSecondLine(String addressSecondLine) {
        this.addressSecondLine = addressSecondLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
