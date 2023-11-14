package ru.astondevs.cinemalike.genre.servlet;

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
import ru.astondevs.cinemalike.film.service.FilmService;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.service.GenreService;

import java.io.*;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServletTest {
    @Mock
    private GenreService genreService;
    @Mock
    private FilmService filmService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private GenreServlet genreServlet;
    private StringWriter writer;
    private ServletInputStream servletInputStream;
    private Genre genre;
    private String outGenreDtoJson;

    @BeforeEach
    public void setUp() {
        writer = new StringWriter();
        genre = new Genre(1L, "action");
        outGenreDtoJson = "{\"name\":\"action\",\"films\":[]}";
        String inGenreDtoJson = "{\"name\": \"action\"}";
        InputStream is = new ByteArrayInputStream(inGenreDtoJson.getBytes());
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
    void doGet_whenInvokeGet_thenReturnOutGenreDtoAsJson() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(genreService.findById(1L)).thenReturn(genre);
        when(filmService.findByGenreId(1L)).thenReturn(new HashSet<>());
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        genreServlet.doGet(request, response);

        assertEquals(outGenreDtoJson, writer.toString());
    }

    @Test
    void doPost_whenInvokePost_thenReturnOutGenreDtoAsJson() throws IOException {
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(genreService.save(any())).thenReturn(genre);
        when(filmService.findByGenreId(1L)).thenReturn(new HashSet<>());
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        genreServlet.doPost(request, response);

        assertEquals(outGenreDtoJson, writer.toString());
    }

    @Test
    void doPut_whenInvokePut_thenReturnOutGenreDtoAsJson() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(genreService.findById(1L)).thenReturn(genre);
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(genreService.update(any(), any())).thenReturn(genre);
        when(filmService.findByGenreId(1L)).thenReturn(new HashSet<>());
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        genreServlet.doPut(request, response);

        assertEquals(outGenreDtoJson, writer.toString());
    }

    @Test
    void doDelete_whenInvokeDelete_thenReturnTrueAndStatusAccepted() {
        when(request.getParameter("id")).thenReturn("1");
        when(genreService.deleteById(1L)).thenReturn(true);

        genreServlet.doDelete(request, response);

        verify(response).setStatus(202);
    }

    @Test
    void doDelete_whenInvokeDelete_thenReturnFalseAndStatusBadRequest() {
        when(request.getParameter("id")).thenReturn("1");
        when(genreService.deleteById(1L)).thenReturn(false);

        genreServlet.doDelete(request, response);

        verify(response).setStatus(400);
    }
}