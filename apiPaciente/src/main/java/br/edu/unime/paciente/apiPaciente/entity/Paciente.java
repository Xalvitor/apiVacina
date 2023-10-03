package br.edu.unime.paciente.apiPaciente.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    private String id;
    @NotBlank(message = "Nome não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 digitos")
    private String nome;
    @NotBlank(message = "Sobrenome não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 digitos")
    private String sobrenome;
    private String dataDeNascimento;
    private String genero;
}
