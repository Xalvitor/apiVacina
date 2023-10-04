package br.edu.unime.paciente.apiPaciente.service;

import br.edu.unime.paciente.apiPaciente.entity.Paciente;
import br.edu.unime.paciente.apiPaciente.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    @Autowired
    PacienteRepository pacienteRepository;
    public List<Paciente> obterTodos(){
        return pacienteRepository.findAll();
    }

    public Paciente obterPeloNomeESobrenome(String nome, String sobrenome) throws Exception {
        Paciente paciente = pacienteRepository.findFirstByNomeAndSobrenome(nome, sobrenome);

        if (paciente == null) {
            throw new Exception("Paciente não encontrado!");
        }

        return paciente;
    }

    public Paciente obterPeloId(String id) throws Exception {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);

        if (pacienteOptional.isEmpty()) {
            throw new Exception("Paciente não encontrado!");
        }

        return pacienteOptional.get();
    }

    public void inserir(Paciente paciente){
        pacienteRepository.insert(paciente);
    }

    public Paciente atualizar(String nome, String sobrenome, Paciente paciente) throws Exception {
        Paciente pacienteAntigo = obterPeloNomeESobrenome(nome, sobrenome);

        pacienteAntigo.setNome(paciente.getNome());
        pacienteAntigo.setSobrenome(paciente.getSobrenome());
        pacienteAntigo.setDataDeNascimento(paciente.getDataDeNascimento());
        pacienteAntigo.setGenero(paciente.getGenero());
        pacienteAntigo.setContatos(paciente.getContatos());
        pacienteAntigo.setEnderecos(paciente.getEnderecos());

        pacienteRepository.save(pacienteAntigo);

        return pacienteAntigo;
    }

    public void deletar(String nome, String sobrenome) throws Exception {
        Paciente paciente = obterPeloNomeESobrenome(nome, sobrenome);

        pacienteRepository.delete(paciente);

    }

}
