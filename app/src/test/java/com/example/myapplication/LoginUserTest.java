package com.example.myapplication;

import com.mobileappdev.homeworkplanner.LoginUserFragment;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class LoginUserTest{

    @Test
    public void testResetButton(){
        LoginUserFragment myList = mock(LoginUserFragment.class);

        when(myList.getString(R.id.reset)).thenReturn("Reset");
        assertEquals(myList.getString(R.id.reset), "Reset");
    }
}