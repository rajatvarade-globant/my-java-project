package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HelloWorldServletTest {
  private HelloWorldServlet servlet;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @BeforeEach
  public void setUp() {
    servlet = new HelloWorldServlet();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
  }

  @Test
  public void testDoGet() throws ServletException, IOException {
    when(response.getWriter()).thenReturn(new TestResponse
