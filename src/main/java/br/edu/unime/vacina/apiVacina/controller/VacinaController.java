package br.edu.unime.vacina.apiVacina.controller;

import br.edu.unime.vacina.apiVacina.entity.Vacina;
import br.edu.unime.vacina.apiVacina.service.VacinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import lombok.Data;

import javax.validation.Valid;

@Data

@RestController
@RequestMapping("/vacina")
public class VacinaController {

    @Autowired
    VacinaService vacinaService;
    @GetMapping
    public ResponseEntity<List<Vacina>> obterTodos(){
        return ResponseEntity.ok().body(vacinaService.obterTodos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> encontrarPaciente(@PathVariable String id) {
        try{
            Vacina paciente = vacinaService.encontrarVacina(id);

            return ResponseEntity.ok().body(paciente);

        } catch (Exception e) {

            Map<String, String> resposta = new HashMap<>();

            resposta.put("mensagem", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }

    }
    @PostMapping

    public ResponseEntity<?> inserir(@RequestBody @Valid Vacina vacina, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){

            List<String> erros = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(erros.toArray());
        }
        vacinaService.inserir(vacina);

        return ResponseEntity.created(null).body(vacina);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable String id,
            @RequestBody Vacina vacina
    ) {
        try {
            Vacina vacinaAtualizado = vacinaService.atualizar(id, vacina);

            return ResponseEntity.ok().body(vacinaAtualizado);
        } catch (Exception e) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(
            @PathVariable String id
    ) {
        try {
            vacinaService.deletar(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
