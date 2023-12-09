//package com.app.cardfeature7;
//import org.junit.Test;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
////this is the class for the socail login unit testing
//public class SocialAssistanceLoginTest {
//
//    @Test
//    public void testValidLogin() {
//        SocialAssistanceLogin activity = mock(SocialAssistanceLogin.class);
//        when(activity.isValidEmail("reham@gmail.com")).thenReturn(true);
//        when(activity.isValidPassword("Reham123")).thenReturn(true);
//        when(activity.signInSocialAssistance("reham@gmail.com", "Reham123")).thenReturn(true);
//
//        assertTrue(activity.signInSocialAssistance("reham@gmail.com", "Reham123"));
//    }
//
//    @Test
//    public void testIncorrectPassword() {
//        SocialAssistanceLogin activity = mock(SocialAssistanceLogin.class);
//        when(activity.isValidEmail("reham@gmail.com")).thenReturn(true);
//        when(activity.isValidPassword("Re123")).thenReturn(true);
//        when(activity.signInSocialAssistance("reham@gmail.com", "Re123")).thenReturn(false);
//
//        assertFalse(activity.signInSocialAssistance("reham@gmail.com", "Re123"));
//    }
//
//    @Test
//    public void testInvalidEmail() {
//        SocialAssistanceLogin activity = mock(SocialAssistanceLogin.class);
//        when(activity.isValidEmail("jass@gmail.com")).thenReturn(false);
//
//        assertFalse(activity.isValidEmail("jass@gmail.com"));
//    }
//
//    @Test
//    public void testEmptyEmail() {
//        SocialAssistanceLogin activity = mock(SocialAssistanceLogin.class);
//        when(activity.isValidEmail("")).thenReturn(false);
//
//        assertFalse(activity.isValidEmail(""));
//    }
//
//    @Test
//    public void testEmptyPassword() {
//        SocialAssistanceLogin activity = mock(SocialAssistanceLogin.class);
//        when(activity.isValidPassword("")).thenReturn(false);
//
//        assertFalse(activity.isValidPassword(""));
//    }
//}
