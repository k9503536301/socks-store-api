package ru.sks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.sks.exception.FileHandlingException;
import ru.sks.repository.SockRepository;
import ru.sks.service.BatchUploadService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class BatchUploadServiceTest {
    @Mock
    private SockRepository sockRepository;

    @InjectMocks
    private BatchUploadService batchUploadService;

    @Test
    void testUploadBatch_Successful() throws FileHandlingException {
        String data = "color,cotton_part,quantity\ngreen,60,20";
        MultipartFile multipartFile = new MockMultipartFile("mockFile", data.getBytes());
        batchUploadService.uploadBatch(multipartFile);

        verify(sockRepository).save(argThat(s ->
                s.getColor().equals("green") &&
                        s.getCottonPart() == 60 &&
                        s.getQuantity() == 20));
    }
}
