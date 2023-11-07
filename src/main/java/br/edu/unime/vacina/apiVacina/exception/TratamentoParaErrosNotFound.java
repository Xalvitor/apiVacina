package br.edu.unime.vacina.apiVacina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TratamentoParaErrosNotFound {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> lidarComProblemasInternoDeServidor(Exception ex){
        String mensagem = "O paciente não foi encontrado.";
        return new ResponseEntity<>(mensagem, HttpStatus.NOT_FOUND);

    }

}