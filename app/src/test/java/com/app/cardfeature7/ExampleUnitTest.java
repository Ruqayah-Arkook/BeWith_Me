//package com.app.cardfeature7;
//
//import android.content.Context;
//import android.widget.Toast;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import static org.mockito.Mockito.*;
//
//public class ExampleUnitTest {
//    @Mock
//    private Context mockedContext;
//
//    @Mock
//    private Toast mockedToast;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testValidate_AllFieldsNotEmpty_ReturnsTrue() {
//        String image = "sample image";
//        String name = "sample name";
//        String time = "sample time";
//        boolean result = ClickableImageActivity.validate(image, name, time);
//
//        // Assert that the method returns true
//        assert (result);
//        // Verify that no Toast messages are shown
//        verify(mockedContext, never()).getApplicationContext();
//    }
//
//    @Test
//    public void testValidate_EmptyImage_ReturnsFalse_AndShowsToast() {
//        String image = "";
//        String name = "sample name";
//        String time = "sample time";
//
//        boolean result = ClickableImageActivity.validate(image, name, time);
//
//        // Assert that the method returns false
//        assert (!result);
//        // Verify that the Toast message is shown with the expected text
//        verify(mockedContext).getApplicationContext();
//        verify(mockedToast).show();
//    }
//
//    @Test
//    public void testValidate_EmptyName_ReturnsFalse_AndShowsToast() {
//        String image = "sample image";
//        String name = "";
//        String time = "sample time";
//
//        boolean result = ClickableImageActivity.validate(image, name, time);
//
//        // Assert that the method returns false
//        assert (!result);
//        // Verify that the Toast message is shown with the expected text
//        verify(mockedContext).getApplicationContext();
//        verify(mockedToast).show();
//    }
//
//    @Test
//    public void testValidate_EmptyTime_ReturnsFalse_AndShowsToast() {
//        String image = "sample image";
//        String name = "sample name";
//        String time = "";
//
//        boolean result = ClickableImageActivity.validate(image, name, time);
//
//        // Assert that the method returns false
//        assert (!result);
//        // Verify that the Toast message is shown with the expected text
//        verify(mockedContext).getApplicationContext();
//        verify(mockedToast).show();
//    }
//}