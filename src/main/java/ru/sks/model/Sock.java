package ru.sks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sks.exception.InsufficientSocksException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int cottonPart;

    @Column(nullable = false)
    private int quantity;

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) throws InsufficientSocksException {
        if (this.quantity < amount) {
            throw new InsufficientSocksException("Insufficient socks in stock");
        }
        this.quantity -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sock)) return false;
        Sock sock = (Sock) o;
        return getCottonPart() == sock.getCottonPart() &&
                Objects.equals(getColor(), sock.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getCottonPart());
    }
}