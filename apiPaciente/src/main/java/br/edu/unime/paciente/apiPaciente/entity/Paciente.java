package br.edu.unime.paciente.apiPaciente.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import br.edu.unime.paciente.apiPaciente.Configuration.JacksonLocalDateConfiguration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

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
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate dataDeNascimento;
    private String genero;

    private List<String> contatos;

    private List<Endereco> enderecos;

}
