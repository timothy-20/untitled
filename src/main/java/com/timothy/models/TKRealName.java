package com.timothy.models;

public class TKRealName {
    private final String firstName;
    private String lastName;

    public TKRealName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("이름은 null 값이거나 빈 문자열일 수 없습니다.");
        }

        this.firstName = firstName.replace(",", "");
        this.lastName = null;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName.replace(",", "");
    }

    @Override
    public String toString() {
        String lastName = this.lastName != null && !this.lastName.isEmpty() ? this.lastName + ", " : "";
        return lastName + this.firstName;
    }
}