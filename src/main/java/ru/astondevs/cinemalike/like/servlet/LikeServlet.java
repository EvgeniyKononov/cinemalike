package ru.astondevs.cinemalike.like.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.cinemalike.like.service.LikeService;
import ru.astondevs.cinemalike.like.service.impl.LikeServiceImpl;

import java.io.IOException;

@WebServlet(name = "LikeServlet", value = "/like")
public class LikeServlet extends HttpServlet {
    private transient LikeService likeService;

    @Override
    public void init() {
    likeService = new LikeServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        Long filmId = Long.valueOf(req.getParameter("filmId"));
        if (likeService.setLike(userId, filmId)) {
            resp.setStatus(202);
        } else {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        Long filmId = Long.valueOf(req.getParameter("filmId"));
        if (likeService.deleteLike(userId, filmId)) {
            resp.setStatus(202);
        } else {
            resp.setStatus(400);
        }
    }
}
