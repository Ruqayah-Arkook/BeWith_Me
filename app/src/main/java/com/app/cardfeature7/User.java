package com.app.cardfeature7;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String email;
    private String dateOfBirth;
    private String gender;
    private String name;
    private Map<String, Boolean> children;

    // Default (no-argument) constructor required for Firebase
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    // Constructor
    public User(String name,String userId, String email, String dateOfBirth, String gender) {
        this.name=name;
        this.userId = userId;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.children = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public Map<String, Boolean> getChildren() {
        return children;
    }

    public static class ChildInfo {
        private String childUID;
        private String childName;
        public DataSnapshot snapshot;
        public Context context;

        private String childAge;
        private String childUsername;
        private String key;
        private String socialAssistanceUID;
        private String password;

        // Default constructor
        public ChildInfo() {
            // Initialize default values if needed
        }
        public ChildInfo(String name) {
            this.childName=name;
            // Initialize default values if needed
        }

        public ChildInfo(String name,DataSnapshot snapshot) {
            this.childName=name;
            this.snapshot=snapshot;
            // Initialize default values if needed
        }

        public ChildInfo(String childUID, String childName, String childAge, String childUsername, String key, String socialAssistanceUID, String password) {
            this.childUID = childUID;
            this.childName = childName;
            this.childAge = childAge;
            this.childUsername = childUsername;
            this.key = key;
            this.socialAssistanceUID = socialAssistanceUID;
            this.password = password;
        }



        public DataSnapshot getSnapshot(){
            return  this.snapshot;
        }

        public String getChildUID() {
            return childUID;
            // implementation
        }

        public String getChildName() {
            return childName;
        }

        public String getChildUsername() {
            return childUsername;
        }

        public void setChildUsername(String childUsername) {
            this.childUsername = childUsername;
        }

        public String getChildAge() {
            return childAge;
        }

        public String getKey() {
            return key;
        }

        public String getSocialAssistanceUID() {
            return socialAssistanceUID;
        }
    }
    public static class SocialAssistance {
        private String name;
        private String email;
        private String socialAssistanceUID;

        public SocialAssistance() {
            // Default constructor required for calls to DataSnapshot.getValue(SocialAssistance.class)
        }

        public SocialAssistance(String name, String email, String socialAssistanceUID) {
            this.name = name;
            this.email = email;
            this.socialAssistanceUID = socialAssistanceUID;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getSocialAssistanceUID() {
            return socialAssistanceUID;
        }
    }
}