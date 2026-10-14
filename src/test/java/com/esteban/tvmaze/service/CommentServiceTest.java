package com.esteban.tvmaze.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.esteban.tvmaze.dto.CommentRequest;
import com.esteban.tvmaze.model.ShowComment;
import com.esteban.tvmaze.repository.CommentRepository;

class CommentServiceTest {

    private CommentRepository repository;
    private ShowService showService;
    private CommentService service;

    @BeforeEach
    void setUp() {
        repository = mock(CommentRepository.class);
        showService = mock(ShowService.class);
        service = new CommentService(repository, showService);
    }

    @Test
    void savesCommentForExistingShow() {
        when(showService.getShowById(975))
                .thenReturn(Map.of("id", 975));

        service.saveComment(
                new CommentRequest(975, "  Muy buena serie  ", 0)
        );

        ArgumentCaptor<ShowComment> captor =
                ArgumentCaptor.forClass(ShowComment.class);

        var order = inOrder(showService, repository);
        order.verify(showService).getShowById(975);
        order.verify(repository).save(captor.capture());

        ShowComment saved = captor.getValue();

        assertEquals(Integer.valueOf(975), saved.showId());
        assertEquals("Muy buena serie", saved.comment());
        assertEquals(Integer.valueOf(0), saved.rating());
        assertNotNull(saved.createdAt());
    }

    @Test
    void doesNotSaveCommentWhenShowDoesNotExist() {
        when(showService.getShowById(975))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Show no encontrado"
                ));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveComment(
                        new CommentRequest(975, "Comentario", 5)
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verifyNoInteractions(repository);
    }
}