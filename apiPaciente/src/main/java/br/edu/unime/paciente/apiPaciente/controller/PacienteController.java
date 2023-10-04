package br.edu.unime.paciente.apiPaciente.controller;

import br.edu.unime.paciente.apiPaciente.entity.Paciente;
import br.edu.unime.paciente.apiPaciente.service.PacienteService;
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
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    PacienteService pacienteService;
    @GetMapping
    public ResponseEntity<List<Paciente>> obterTodos(){
        return ResponseEntity.ok().body(pacienteService.obterTodos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> encontrarPaciente(@PathVariable String id) {
        try{
            Paciente paciente = pacienteService.encontrarPaciente(id);

            return ResponseEntity.ok().body(paciente);

        } catch (Exception e) {

            Map<String, String> resposta = new HashMap<>();

            resposta.put("mensagem", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }

    }
    @PostMapping

    public ResponseEntity<?> inserir(@RequestBody @Valid Paciente paciente, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){

            List<String> erros = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(erros.toArray());
        }
        pacienteService.inserir(paciente);

        return ResponseEntity.created(null).body(paciente);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable String id,
            @RequestBody Paciente paciente
    ) {
        try {
            Paciente pacienteAtualizado = pacienteService.atualizar(id, paciente);

            return ResponseEntity.ok().body(pacienteAtualizado);
        } catch (Exception e) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/id")
    public ResponseEntity<?> excluir(
            @PathVariable String id
    ) {
        try {
            pacienteService.deletar(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
