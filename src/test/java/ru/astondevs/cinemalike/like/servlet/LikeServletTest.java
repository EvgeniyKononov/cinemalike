package ru.astondevs.cinemalike.like.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.cinemalike.like.service.LikeService;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServletTest {
    @Mock
    private LikeService likeService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private LikeServlet likeServlet;

    @Test
    void doPost_whenLikeSaved_thenResponseHasStatusAccepted() throws ServletException, IOException {
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("filmId")).thenReturn("1");
        when(likeService.setLike(1L, 1L)).thenReturn(true);

        likeServlet.doPost(request, response);

        verify(response).setStatus(202);
    }

    @Test
    void doPost_whenLikeNotSaved_thenResponseHasStatusBadRequest() throws ServletException, IOException {
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("filmId")).thenReturn("1");
        when(likeService.setLike(1L, 1L)).thenReturn(false);

        likeServlet.doPost(request, response);

        verify(response).setStatus(400);
    }

    @Test
    void doDelete_whenLikeDeleted_thenResponseHasStatusAccepted() throws ServletException, IOException {
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("filmId")).thenReturn("1");
        when(likeService.deleteLike(1L, 1L)).thenReturn(true);

        likeServlet.doDelete(request, response);

        verify(response).setStatus(202);
    }

    @Test
    void doDelete_whenLikeNotDeleted_thenResponseHasStatusBadRequest() throws ServletException, IOException {
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("filmId")).thenReturn("1");
        when(likeService.deleteLike(1L, 1L)).thenReturn(false);

        likeServlet.doDelete(request, response);

        verify(response).setStatus(400);
    }
}