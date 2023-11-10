package ru.astondevs.cinemalike.user.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.service.UserService;
import ru.astondevs.cinemalike.user.service.impl.UserServiceImpl;
import ru.astondevs.cinemalike.user.servlet.dto.InUserDto;
import ru.astondevs.cinemalike.user.servlet.dto.OutUserDto;
import ru.astondevs.cinemalike.user.servlet.mapper.UserDtoMapper;
import ru.astondevs.cinemalike.user.servlet.mapper.impl.UserDtoMapperImpl;

import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    private transient UserService userService;
    private transient UserDtoMapper dtoMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        userService = new UserServiceImpl();
        dtoMapper = new UserDtoMapperImpl();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        User user = userService.findById(id);
        OutUserDto outUserDto = dtoMapper.map(user);
        String json = objectMapper.writeValueAsString(outUserDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InUserDto inUserDto = objectMapper.readValue(req.getInputStream(), InUserDto.class);
        User user = dtoMapper.map(inUserDto);
        user = userService.save(user);
        OutUserDto outUserDto = dtoMapper.map(user);
        String json = objectMapper.writeValueAsString(outUserDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        User user = userService.findById(id);
        InUserDto inUserDto = objectMapper.readValue(req.getInputStream(), InUserDto.class);
        User updatedUser = dtoMapper.map(inUserDto);
        user = userService.update(user, updatedUser);
        OutUserDto outUserDto = dtoMapper.map(user);
        String json = objectMapper.writeValueAsString(outUserDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("id"));
        userService.deleteById(id);
        resp.setStatus(202);
    }
}
