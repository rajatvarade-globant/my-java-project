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
    when(response.getWriter()).thenReturn(new TestResponse());

    servlet.doGet(request, response);

    assertEquals("Hello, world!", response.getWriter().toString());
  }

  private class TestResponse implements HttpServletResponse {
    private StringWriter writer = new StringWriter();

    @Override
    public PrintWriter getWriter() throws IOException {
      return new PrintWriter(writer);
    }

    @Override
    public String toString() {
      return writer.toString();
    }

    // Other methods of the HttpServletResponse interface
    // can be left unimplemented or implemented as no-ops.
  }
}
