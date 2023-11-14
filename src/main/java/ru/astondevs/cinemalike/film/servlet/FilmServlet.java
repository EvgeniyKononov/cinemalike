package ru.astondevs.cinemalike.film.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.service.FilmService;
import ru.astondevs.cinemalike.film.service.impl.FilmServiceImpl;
import ru.astondevs.cinemalike.film.servlet.dto.InFilmDto;
import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;
import ru.astondevs.cinemalike.film.servlet.mapper.FilmDtoMapper;
import ru.astondevs.cinemalike.film.servlet.mapper.impl.FilmDtoMapperImpl;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.service.GenreService;
import ru.astondevs.cinemalike.genre.service.impl.GenreServiceImpl;

import java.io.IOException;

@WebServlet(name = "FilmServlet", value = "/film")
public class FilmServlet extends HttpServlet {
    private transient FilmService filmService;
    private transient GenreService genreService;
    private final transient FilmDtoMapper dtoMapper = new FilmDtoMapperImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        filmService = new FilmServiceImpl();
        genreService = new GenreServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Film film = filmService.findById(id);
        Genre genre = genreService.findById(film.getGenre());
        OutFilmDto outFilmDto = dtoMapper.map(film, genre);
        String json = objectMapper.writeValueAsString(outFilmDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InFilmDto inFilmDto = objectMapper.readValue(req.getInputStream(), InFilmDto.class);
        Genre genre = genreService.findByName(inFilmDto.getGenreDto().getName());
        Film film = dtoMapper.toNewEntity(inFilmDto, genre);
        film = filmService.save(film);
        OutFilmDto outFilmDto = dtoMapper.map(film, genre);
        String json = objectMapper.writeValueAsString(outFilmDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Film film = filmService.findById(id);
        InFilmDto inFilmDto = objectMapper.readValue(req.getInputStream(), InFilmDto.class);
        Genre genre = genreService.findByName(inFilmDto.getGenreDto().getName());
        Film updatedFilm = dtoMapper.toNewEntity(inFilmDto, genre);
        film = filmService.update(film, updatedFilm);
        OutFilmDto outFilmDto = dtoMapper.map(film, genre);
        String json = objectMapper.writeValueAsString(outFilmDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("id"));
        filmService.deleteById(id);
        resp.setStatus(202);
    }
}
