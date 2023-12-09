//package com.app.cardfeature7;
//import org.junit.Test;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//// this is a unit testing for the mother creating an account.
//public class MotherSignupActivityTest{
//
//    @Test
//    public void testValidSignUp() {
//        MotherSignUpActivity activity = new MotherSignUpActivity();
//
//        String firstName = "Najmah";
//        String email = "najmah_142@gmail.com";
//        String password = "Aa12345*";
//
//        System.out.println("First Name: " + firstName);
//        System.out.println("Email: " + email);
//        System.out.println("Password: " + password);
//
//        boolean isEmailValid = activity.isEmailValid(email);
//        System.out.println("Is Email Valid: " + isEmailValid);
//
//        boolean isValid = activity.isSignUpValid(firstName, email, password);
//        assertTrue(isValid);
//    }
//
//    @Test
//    public void testEmptyName() {
//        MotherSignUpActivity activity = new MotherSignUpActivity();
//        assertFalse(activity.isSignUpValid("", "valid@email.com", "validPassword"));
//    }
//
//    @Test
//    public void testEmptyEmail() {
//        MotherSignUpActivity activity = new MotherSignUpActivity();
//        assertFalse(activity.isSignUpValid("ValidName", "", "validPassword"));
//    }
//
//    @Test
//    public void testInvalidEmail() {
//        MotherSignUpActivity activity = new MotherSignUpActivity();
//        assertFalse(activity.isSignUpValid("ValidName", "invalid_email", "validPassword"));
//    }
//
//    @Test
//    public void testEmptyPassword() {
//        MotherSignUpActivity activity = new MotherSignUpActivity();
//        assertFalse(activity.isSignUpValid("ValidName", "valid@email.com", ""));
//    }
//
//    @Test
//    public void testShortPassword() {
//        MotherSignUpActivity activity = new MotherSignUpActivity();
//        assertFalse(activity.isSignUpValid("ValidName", "valid@email.com", "short"));
//    }
//
//}
//
//
//
