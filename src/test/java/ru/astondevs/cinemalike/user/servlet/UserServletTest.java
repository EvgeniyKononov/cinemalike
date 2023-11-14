package ru.astondevs.cinemalike.user.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.service.UserService;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServletTest {
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private UserServlet userServlet;
    private StringWriter writer;
    private User user;
    private String outUserDtoJson;
    private ServletInputStream servletInputStream;

    @BeforeEach
    public void setUp() {
        writer = new StringWriter();
        user = new User(1L, "login1", "name1");
        outUserDtoJson = "{\"id\":1,\"name\":\"name1\",\"filmLikes\":[]}";
        String inUserDtoJson = "{\"login\": \"login1\",\"name\": \"name1\"}";
        InputStream is = new ByteArrayInputStream(inUserDtoJson.getBytes());
        servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return is.read();
            }
        };
    }

    @Test
    void doGet_whenInvokeGet_thenReturnOutUserDtoAsJson() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(userService.findById(1L)).thenReturn(user);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userServlet.doGet(request, response);

        assertEquals(outUserDtoJson, writer.toString());
    }

    @Test
    void doPost_whenInvokePost_thenReturnOutUserDtoAsJson() throws IOException {
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(userService.save(any())).thenReturn(user);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userServlet.doPost(request, response);

        assertEquals(outUserDtoJson, writer.toString());
    }

    @Test
    void doPut_whenInvokePut_thenReturnOutUserDtoAsJson() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(userService.findById(1L)).thenReturn(user);
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(userService.update(any(), any())).thenReturn(user);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userServlet.doPut(request, response);

        assertEquals(outUserDtoJson, writer.toString());
    }

    @Test
    void doDelete_whenInvokeDelete_thenResponseStatusIsAccepted() {
        when(request.getParameter("id")).thenReturn("1");

        userServlet.doDelete(request, response);

        verify(response).setStatus(202);
    }
}