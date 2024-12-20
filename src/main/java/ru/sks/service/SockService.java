package ru.sks.service;

import ru.sks.exception.InsufficientSocksException;
import ru.sks.model.Sock;
import ru.sks.repository.SockRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sks.util.ComparisonOperator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SockService {

    private final Logger logger = LoggerFactory.getLogger(SockService.class);
    private final SockRepository sockRepository;

    @Transactional
    public void registerIncome(String color, int cottonPart, int quantity) {
        Optional<Sock> existingSock = findSock(color, cottonPart);

        if (existingSock.isPresent()) {
            Sock sock = existingSock.get();
            sock.increaseQuantity(quantity);
            sockRepository.save(sock);
        } else {
            Sock newSock = new Sock(null, color, cottonPart, quantity);
            sockRepository.save(newSock);
        }

        logger.info("Registered income for {} socks with color '{}' and cotton percentage {}", quantity, color, cottonPart);
    }

    @Transactional
    public void registerOutcome(String color, int cottonPart, int quantity) throws InsufficientSocksException {
        Optional<Sock> existingSock = findSock(color, cottonPart);

        if (existingSock.isPresent()) {
            Sock sock = existingSock.get();
            sock.decreaseQuantity(quantity);
            sockRepository.save(sock);

            logger.info("Registered outcome for {} socks with color '{}' and cotton percentage {}", quantity, color, cottonPart);
        } else {
            throw new InsufficientSocksException("Insufficient socks in stock");
        }
    }

    @Transactional
    public void updateSocks(Long id, String color, int cottonPart, int quantity) {
        logger.info("Updating socks with ID {}: color={}, cottonPart={}, quantity={}", id, color, cottonPart, quantity);
        Sock existingSocks = sockRepository.findById(id).orElseThrow(() -> new RuntimeException("Socks not found"));
        existingSocks.setColor(color);
        existingSocks.setCottonPart(cottonPart);
        existingSocks.setQuantity(quantity);
        sockRepository.save(existingSocks);
    }

    @Transactional(readOnly = true)
    public int countSocks(String color, ComparisonOperator operator, Integer cottonPart) {
        List<Sock> socks = sockRepository.findByColorAndCottonPart(color, cottonPart);

        if (operator != null && cottonPart != null) {
            switch (operator) {
                case MORE_THAN:
                    socks = socks.stream()
                            .filter(s -> s.getCottonPart() > cottonPart)
                            .collect(Collectors.toList());
                    break;
                case LESS_THAN:
                    socks = socks.stream()
                            .filter(s -> s.getCottonPart() < cottonPart)
                            .collect(Collectors.toList());
                    break;
                case EQUAL:
                    socks = socks.stream()
                            .filter(s -> s.getCottonPart() == cottonPart)
                            .collect(Collectors.toList());
                    break;
            }
        }

        return socks.stream().mapToInt(Sock::getQuantity).sum();
    }

    private Optional<Sock> findSock(String color, int cottonPart) {
        return sockRepository.findByColorAndCottonPart(color, cottonPart).stream().findFirst();
    }
}