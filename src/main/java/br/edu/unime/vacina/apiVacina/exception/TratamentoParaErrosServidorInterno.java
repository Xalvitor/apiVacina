package br.edu.unime.vacina.apiVacina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TratamentoParaErrosServidorInterno {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    Map<String,String> showCustomMessage(Exception e){

        Map<String,String> response = new HashMap<>();
        response.put("Mensagem","Ocorreu um erro na aplicação. Nossa equipe de TI já foi notificada" +
                " e em breve nossos serviços estarão reestabelecidos. Para maiores informações" +
                " entre em contato pelo nosso WhatsApp 71 99999-9999. Lamentamos o ocorrido!");

        return response;
    }
}