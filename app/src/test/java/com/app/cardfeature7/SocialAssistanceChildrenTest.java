//package com.app.cardfeature7;
//
//import org.junit.Test;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
////the unit testing for adding child under soacil
//public class SocialAssistanceChildrenTest {
//
//    @Test
//    public void testValidAssociation() {
//        SocialAssistance_Children activity = mock(SocialAssistance_Children.class);
//        when(activity.isValidChildInput()).thenReturn(true);
//        when(activity.associateChildWithSocialAssistance("childUID", "socialAssistanceUID")).thenReturn(true);
//
//        assertTrue(activity.associateChildWithSocialAssistance("childUID", "socialAssistanceUID"));
//    }
//
//    @Test
//    public void testInvalidChildCredentials() {
//        SocialAssistance_Children activity = mock(SocialAssistance_Children.class);
//        when(activity.isValidChildInput()).thenReturn(true);
//        when(activity.validateChildCredentials("Saud", "HGUI1892", "socialAssistanceUID")).thenReturn(false);
//
//        assertFalse(activity.validateChildCredentials("Saud", "HGUI1892", "socialAssistanceUID"));
//    }
//
//    @Test
//    public void testInvalidSharedKey() {
//        SocialAssistance_Children activity = mock(SocialAssistance_Children.class);
//        when(activity.isValidChildInput()).thenReturn(true);
//        when(activity.validateChildCredentials("Saud12", "QUNS1726", "socialAssistanceUID")).thenReturn(false);
//
//        assertFalse(activity.validateChildCredentials("Saud12", "QUNS1726", "socialAssistanceUID"));
//    }
//
//    @Test
//    public void testChildAlreadyAssociated() {
//        SocialAssistance_Children activity = mock(SocialAssistance_Children.class);
//        when(activity.isValidChildInput()).thenReturn(true);
//        when(activity.validateChildCredentials("Saud12", "HGUI1892", "socialAssistanceUID")).thenReturn(true);
//        when(activity.associateChildWithSocialAssistance("childUID", "socialAssistanceUID")).thenReturn(false);
//
//        assertFalse(activity.associateChildWithSocialAssistance("childUID", "socialAssistanceUID"));
//    }
//
//    @Test
//    public void testEmptyUsername() {
//        SocialAssistance_Children activity = mock(SocialAssistance_Children.class);
//        when(activity.isValidChildInput()).thenReturn(false);
//
//        assertFalse(activity.isValidChildInput());
//    }
//
//    @Test
//    public void testEmptySharedKey() {
//        SocialAssistance_Children activity = mock(SocialAssistance_Children.class);
//        when(activity.isValidChildInput()).thenReturn(false);
//
//        assertFalse(activity.isValidChildInput());
//    }
//
//    @Test
//    public void testEmptyInput() {
//        SocialAssistance_Children activity = mock(SocialAssistance_Children.class);
//        when(activity.isValidChildInput()).thenReturn(false);
//
//        assertFalse(activity.isValidChildInput());
//    }
//}
