package ru.sks.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.sks.exception.InsufficientSocksException;
import ru.sks.model.Sock;
import ru.sks.service.SockService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sks.util.ComparisonOperator;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "Socks", description = "Operations related to socks management")
@RequiredArgsConstructor
public class SockController {

    private final SockService sockService;

    @Operation(
            summary = "Register income of socks",
            description = "Increases the number of socks on the warehouse.",
            parameters = {
                    @Parameter(name = "color", description = "Color of the socks"),
                    @Parameter(name = "cottonPart", description = "Percentage of cotton"),
                    @Parameter(name = "quantity", description = "Quantity of the socks")
            }
    )
    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerIncome(@Valid @RequestBody Sock sock) {
        sockService.registerIncome(sock.getColor(), sock.getCottonPart(), sock.getQuantity());
    }

    @Operation(
            summary = "Register outcome of socks",
            description = "Decreases the number of socks on the warehouse.",
            parameters = {
                    @Parameter(name = "color", description = "Color of the socks"),
                    @Parameter(name = "cottonPart", description = "Percentage of cotton"),
                    @Parameter(name = "quantity", description = "Quantity of the socks")
            }
    )
    @PostMapping("/outcome")
    @ResponseStatus(HttpStatus.OK)
    public void registerOutcome(@Valid @RequestBody Sock sock) throws InsufficientSocksException{
        sockService.registerOutcome(sock.getColor(), sock.getCottonPart(), sock.getQuantity());
    }

    @Operation(
            summary = "Get total number of socks matching criteria",
            description = "Returns the total number of socks that match the given color and cotton percentage comparison.",
            parameters = {
                    @Parameter(name = "color", description = "Color of the socks"),
                    @Parameter(name = "operator", description = "Comparison operator - one of moreThan, lessThan, equal"),
                    @Parameter(name = "cotton_part", description = "Percentage of cotton")
            }
    )
    @GetMapping
    public Map<String, Object> getTotalSocks(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) ComparisonOperator operator,
            @RequestParam(required = false) Integer cotton_part) {
        int totalSocks = sockService.countSocks(color, operator, cotton_part);

        return Map.of("totalSocks", totalSocks);
    }

    @Operation(
            summary = "Update socks by ID",
            description = "Updates the properties of sock record",
            parameters = {
                    @Parameter(name = "id", description = "Postgresql record ID"),
                    @Parameter(name = "color", description = "Color of the socks"),
                    @Parameter(name = "cottonPart", description = "Percentage of cotton"),
                    @Parameter(name = "quantity", description = "Quantity of the socks")
            }
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSocks(@PathVariable Long id, @Valid @RequestBody Sock sock) {
        sockService.updateSocks(id, sock.getColor(), sock.getCottonPart(), sock.getQuantity());
    }
}