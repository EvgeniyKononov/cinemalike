package ru.astondevs.cinemalike.film.servlet;

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
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.service.FilmService;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.service.GenreService;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilmServletTest {
    @Mock
    private GenreService genreService;
    @Mock
    private FilmService filmService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private FilmServlet filmServlet;
    private StringWriter writer;
    private ServletInputStream servletInputStream;
    private Film film;
    private Genre genre;
    private String outFilmDtoJson;

    @BeforeEach
    public void setUp() {
        writer = new StringWriter();
        film = new Film("best film", 1L);
        genre = new Genre(1L, "action");
        outFilmDtoJson = "{\"name\":\"best film\",\"genre\":\"action\",\"userLikes\":[]}";
        String inFilmDtoJson = "{\"name\":\"best film\",\"genreDto\":{\"name\":\"action\"}}";
        InputStream is = new ByteArrayInputStream(inFilmDtoJson.getBytes());
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
    void doGet_whenInvokeGet_thenReturnOutFilmDtoAsJson() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(filmService.findById(1L)).thenReturn(film);
        when(genreService.findById(film.getGenre())).thenReturn(genre);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        filmServlet.doGet(request, response);

        assertEquals(outFilmDtoJson, writer.toString());
    }

    @Test
    void doPost_whenInvokePost_thenReturnOutFilmDtoAsJson() throws IOException {
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(genreService.findByName(genre.getName())).thenReturn(genre);
        when(filmService.save(film)).thenReturn(film);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        filmServlet.doPost(request, response);

        assertEquals(outFilmDtoJson, writer.toString());
    }

    @Test
    void doPut_whenInvokePut_thenReturnOutFilmDtoAsJson() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(filmService.findById(1L)).thenReturn(film);
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(genreService.findByName(genre.getName())).thenReturn(genre);
        when(filmService.update(any(), any())).thenReturn(film);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        filmServlet.doPut(request, response);

        assertEquals(outFilmDtoJson, writer.toString());
    }

    @Test
    void doDelete_whenInvokeDelete_thenResponseStatusIsAccepted() {
        when(request.getParameter("id")).thenReturn("1");

        filmServlet.doDelete(request, response);

        verify(response).setStatus(202);
    }
}