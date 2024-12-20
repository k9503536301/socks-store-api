package ru.sks.repository;

import ru.sks.model.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SockRepository extends JpaRepository<Sock, Long> {
    List<Sock> findByColorAndCottonPart(String color, int cottonPart);
}