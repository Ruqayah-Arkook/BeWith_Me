//package com.app.cardfeature7;
//import org.junit.Test;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
////this is the unit testing for socail create an account.
//public class SocialAssistanceSignupTest {
//
//    @Test
//    public void testValidSignUp() {
//        SocialAssistanceSignup activity = mock(SocialAssistanceSignup.class);
//        when(activity.isEmailValid("sara2@gmail.com")).thenReturn(true);
//        when(activity.registerSocialAssistanceUser("Sara", "sara2@gmail.com", "sar092")).thenReturn(true);
//
//        assertTrue(activity.registerSocialAssistanceUser("Sara", "sara2@gmail.com", "sar092"));
//    }
//
//    @Test
//    public void testInvalidEmailFormat() {
//        SocialAssistanceSignup activity = mock(SocialAssistanceSignup.class);
//        when(activity.isEmailValid("sara2@gmail.copk")).thenReturn(false);
//
//        assertFalse(activity.isEmailValid("sara2@gmail.copk"));
//    }
//
//    @Test
//    public void testEmailAlreadyInUse() {
//        SocialAssistanceSignup activity = mock(SocialAssistanceSignup.class);
//        when(activity.isEmailValid("sara2@gmail.com")).thenReturn(true);
//        when(activity.registerSocialAssistanceUser("Deema", "sara2@gmail.com", "pl12321")).thenReturn(false);
//
//        assertFalse(activity.registerSocialAssistanceUser("Deema", "sara2@gmail.com", "pl12321"));
//    }
//
//    @Test
//    public void testShortPassword() {
//        SocialAssistanceSignup activity = mock(SocialAssistanceSignup.class);
//        when(activity.isEmailValid("reham@gmail.com")).thenReturn(true);
//        when(activity.registerSocialAssistanceUser("Reham", "reham@gmail.com", "Re1")).thenReturn(false);
//
//        assertFalse(activity.registerSocialAssistanceUser("Reham", "reham@gmail.com", "Re1"));
//    }
//
//    @Test
//    public void testEmptyName() {
//        SocialAssistanceSignup activity = mock(SocialAssistanceSignup.class);
//        when(activity.isEmailValid("")).thenReturn(true);
//        when(activity.registerSocialAssistanceUser("", "reham@gmail.com", "Re1")).thenReturn(false);
//
//        assertFalse(activity.registerSocialAssistanceUser("", "reham@gmail.com", "Re1"));
//    }
//}
//
