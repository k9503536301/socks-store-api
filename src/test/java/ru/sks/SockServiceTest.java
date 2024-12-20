package ru.sks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import ru.sks.model.Sock;
import ru.sks.repository.SockRepository;
import ru.sks.exception.InsufficientSocksException;
import ru.sks.util.ComparisonOperator;
import ru.sks.service.SockService;

@ExtendWith(MockitoExtension.class)
class SockServiceTest {

    @Mock
    private SockRepository sockRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private SockService sockService;

    private Sock sock;

    @BeforeEach
    void setUp() {
        sock = new Sock(1L, "blue", 50, 10);
    }

    @Test
    void testRegisterIncome_NewSock() {
        when(sockRepository.findByColorAndCottonPart(any(), anyInt())).thenReturn(List.of());

        sockService.registerIncome("green", 60, 20);

        verify(sockRepository).save(argThat(s ->
                s.getColor().equals("green") &&
                        s.getCottonPart() == 60 &&
                        s.getQuantity() == 20));
    }

    @Test
    void testRegisterIncome_ExistingSock() {
        when(sockRepository.findByColorAndCottonPart(eq("blue"), eq(50))).thenReturn(List.of(sock));

        sockService.registerIncome("blue", 50, 15);

        assertEquals(25, sock.getQuantity());

        verify(sockRepository).save(sock);
    }

    @Test
    void testRegisterOutcome_Successful() throws Exception {
        when(sockRepository.findByColorAndCottonPart(eq("blue"), eq(50))).thenReturn(List.of(sock));

        sockService.registerOutcome("blue", 50, 8);

        assertEquals(2, sock.getQuantity());

        verify(sockRepository).save(sock);
    }

    @Test
    void testRegisterOutcome_Failure() {
        when(sockRepository.findByColorAndCottonPart(eq("red"), eq(70))).thenReturn(List.of());

        assertThrows(InsufficientSocksException.class,
                () -> sockService.registerOutcome("red", 70, 12),
                "Insufficient socks in stock");
    }

    @Test
    void testUpdateSocks_Successful() {
        when(sockRepository.findById(eq(1L))).thenReturn(Optional.of(sock));

        sockService.updateSocks(1L, "yellow", 80, 30);

        assertEquals("yellow", sock.getColor());
        assertEquals(80, sock.getCottonPart());
        assertEquals(30, sock.getQuantity());

        verify(sockRepository).save(sock);
    }

    @Test
    void testUpdateSocks_SocksNotFound() {
        when(sockRepository.findById(eq(100L))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> sockService.updateSocks(100L, "black", 90, 40),
                "Socks not found");
    }

    @Test
    void testCountSocks_MoreThan() {
        when(sockRepository.findByColorAndCottonPart(eq("brown"), eq(75)))
                .thenReturn(List.of(
                        new Sock(3L, "brown", 85, 45),
                        new Sock(4L, "brown", 65, 55)));

        int result = sockService.countSocks("brown", ComparisonOperator.MORE_THAN, 75);

        assertEquals(45, result);
    }

    @Test
    void testCountSocks_LessThan() {
        when(sockRepository.findByColorAndCottonPart(eq("purple"), eq(60)))
                .thenReturn(List.of(
                        new Sock(5L, "purple", 50, 32),
                        new Sock(6L, "purple", 72, 43)));

        int result = sockService.countSocks("purple", ComparisonOperator.LESS_THAN, 60);

        assertEquals(32, result); // Только носки с содержанием хлопка меньше 60%
    }

    @Test
    void testCountSocks_Equal() {
        when(sockRepository.findByColorAndCottonPart(eq("orange"), eq(82)))
                .thenReturn(List.of(
                        new Sock(7L, "orange", 82, 22),
                        new Sock(8L, "orange", 92, 33)));

        int result = sockService.countSocks("orange", ComparisonOperator.EQUAL, 82);

        assertEquals(22, result); // Только носки с точным содержанием хлопка 82%
    }
}

