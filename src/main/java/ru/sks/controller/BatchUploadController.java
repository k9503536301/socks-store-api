package ru.sks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sks.exception.FileHandlingException;
import ru.sks.service.BatchUploadService;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "Batch Upload", description = "Operations related to batch upload of socks from CSV files")
@RequiredArgsConstructor
public class BatchUploadController {
    private final BatchUploadService batchUploadService;

    @Operation(
            summary = "Upload a batch of socks from a CSV file",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadBatch(@RequestPart MultipartFile file) throws FileHandlingException {
        batchUploadService.uploadBatch(file);
    }
}
