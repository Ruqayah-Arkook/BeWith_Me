//package com.app.cardfeature7;
//
//import org.junit.Test;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class MotherSignInActivityTest {
//
//    @Test
//    public void testValidSignIn() {
//        MotherSignIn activity = mock(MotherSignIn.class);
//        when(activity.isValidEmail("najmah@gmail.com")).thenReturn(true);
//        when(activity.isValidPassword("123456")).thenReturn(true);
//        when(activity.signInUser("najmah@gmail.com", "123456")).thenReturn(true);
//
//        assertTrue(activity.signInUser("najmah@gmail.com", "123456"));
//    }
//
//    @Test
//    public void testInvalidEmailAndPassword() {
//        MotherSignIn activity = mock(MotherSignIn.class);
//        when(activity.isValidEmail("123")).thenReturn(false);
//        when(activity.isValidPassword("123456")).thenReturn(true);
//        when(activity.signInUser("123", "123456")).thenReturn(false);
//
//        assertFalse(activity.signInUser("123", "123456"));
//    }
//
//    @Test
//    public void testInvalidEmail() {
//        MotherSignIn activity = mock(MotherSignIn.class);
//        when(activity.isValidEmail("najmah@gmail.com")).thenReturn(false);
//        when(activity.isValidPassword("1")).thenReturn(true);
//        when(activity.signInUser("najmah@gmail.com", "1")).thenReturn(false);
//
//        assertFalse(activity.signInUser("najmah@gmail.com", "1"));
//    }
//
//    @Test
//    public void testEmptyEmailAndPassword() {
//        MotherSignIn activity = mock(MotherSignIn.class);
//        when(activity.isValidEmail("")).thenReturn(false);
//        when(activity.isValidPassword("")).thenReturn(false);
//        when(activity.signInUser("", "")).thenReturn(false);
//
//        assertFalse(activity.signInUser("", ""));
//    }
//}
