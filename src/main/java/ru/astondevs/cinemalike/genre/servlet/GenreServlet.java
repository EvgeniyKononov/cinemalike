package ru.astondevs.cinemalike.genre.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.service.FilmService;
import ru.astondevs.cinemalike.film.service.impl.FilmServiceImpl;
import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;
import ru.astondevs.cinemalike.film.servlet.mapper.FilmDtoMapper;
import ru.astondevs.cinemalike.film.servlet.mapper.impl.FilmDtoMapperImpl;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.service.GenreService;
import ru.astondevs.cinemalike.genre.service.impl.GenreServiceImpl;
import ru.astondevs.cinemalike.genre.servlet.dto.InGenreDto;
import ru.astondevs.cinemalike.genre.servlet.dto.OutGenreDto;
import ru.astondevs.cinemalike.genre.servlet.mapper.GenreDtoMapper;
import ru.astondevs.cinemalike.genre.servlet.mapper.impl.GenreDtoMapperImpl;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "GenreServlet", value = "/genre")
public class GenreServlet extends HttpServlet {
    private transient GenreService genreService;
    private transient FilmService filmService;
    private final transient GenreDtoMapper dtoMapper = new GenreDtoMapperImpl();
    private final transient FilmDtoMapper filmDtoMapper = new FilmDtoMapperImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        genreService = new GenreServiceImpl();
        filmService = new FilmServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Genre genre = genreService.findById(id);
        OutGenreDto genreDto = dtoMapper.map(genre, getOutFilmsDtoByGenreId(genre.getId()));
        String json = objectMapper.writeValueAsString(genreDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InGenreDto genreDto = objectMapper.readValue(req.getInputStream(), InGenreDto.class);
        Genre genre = dtoMapper.toNewEntity(genreDto);
        genre = genreService.save(genre);
        OutGenreDto outGenreDto = dtoMapper.map(genre, getOutFilmsDtoByGenreId(genre.getId()));
        String json = objectMapper.writeValueAsString(outGenreDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Genre genre = genreService.findById(id);
        InGenreDto genreDto = objectMapper.readValue(req.getInputStream(), InGenreDto.class);
        Genre updatedGenre = dtoMapper.toNewEntity(genreDto);
        genre = genreService.update(genre, updatedGenre);
        OutGenreDto outGenreDto = dtoMapper.map(genre, getOutFilmsDtoByGenreId(genre.getId()));
        String json = objectMapper.writeValueAsString(outGenreDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("id"));
        if (genreService.deleteById(id)) {
            resp.setStatus(202);
        } else {
            resp.setStatus(400);
        }
    }

    private Set<OutFilmDto> getOutFilmsDtoByGenreId(Long genreId) {
        Set<Film> films = filmService.findByGenreId(genreId);
        return filmDtoMapper.toOutDto(films);
    }
}
