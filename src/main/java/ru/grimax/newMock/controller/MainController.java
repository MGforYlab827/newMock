package ru.grimax.newMock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.grimax.newMock.model.RequestDTO;
import ru.grimax.newMock.model.ResponseDTO;

import java.math.BigDecimal;
import java.util.Random;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(MainController.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/info/postBalances")
    public ResponseEntity<ResponseDTO> postBalances(@RequestBody RequestDTO request) {
        try {
            char firstChar = request.getClientId().charAt(0);
            ResponseDTO response;
            switch (firstChar) {
                case '8' :
                    response = usBankAccount(request.getRqUID(), request.getClientId(), request.getAccount());
                    break;
                case '9' :
                    response = euBankAccount(request.getRqUID(), request.getClientId(), request.getAccount());
                    break;
                default :
                    response = ruBankAccount(request.getRqUID(), request.getClientId(), request.getAccount());
            }
            log.info("======RequestDTO=============\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
            log.info("======ResponseDTO=============\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/")
    public ResponseDTO responseDTO() {
        return new ResponseDTO();
    }

    private ResponseDTO usBankAccount(String rqUID, String clientId, String account) {
        double maxLimit = 2000;
        double balance = new Random().nextDouble(maxLimit);
        return new ResponseDTO(rqUID, clientId, account, "US", BigDecimal.valueOf(balance), BigDecimal.valueOf(maxLimit));
    }

    private ResponseDTO euBankAccount(String rqUID, String clientId, String account) {
        double maxLimit = 1000;
        double balance = new Random().nextDouble(maxLimit);
        return new ResponseDTO(rqUID, clientId, account, "EU", BigDecimal.valueOf(balance), BigDecimal.valueOf(maxLimit));
    }

    private ResponseDTO ruBankAccount(String rqUID, String clientId, String account) {
        double maxLimit = 50000;
        double balance = new Random().nextDouble(maxLimit);
        return  new ResponseDTO(rqUID, clientId, account, "RU", BigDecimal.valueOf(balance), BigDecimal.valueOf(maxLimit));
    }
}
