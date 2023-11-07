package br.edu.unime.vacina.apiVacina;

import br.edu.unime.vacina.apiVacina.entity.Vacina;
import br.edu.unime.vacina.apiVacina.service.VacinaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class VacinaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VacinaService vacinaService;


	private static final String VACINA_ID_PFIZER = "1";
	private static final String VACINA_ID_CORONAVAC = "2";
	private static final String VACINA_ID_JHONSON = "3";

	@Test
	@DisplayName("Deve ser possível obter todas as vacinas cadastradas")
	public void testObterTodasAsVacinas() throws Exception {
		// Arrange
		Vacina pfizer = new Vacina(VACINA_ID_PFIZER, "PFIZER", "123ABC", LocalDate.of(2025,3,23), 2, 21);
		Vacina coronavac = new Vacina(VACINA_ID_CORONAVAC, "CORONAVAC", "456DEF", LocalDate.now(), 2, 28);
		Vacina jhonson = new Vacina(VACINA_ID_JHONSON, "JHONSON & JHONSON", "789GHI", LocalDate.now(), 1, null);

		List<Vacina> vacinas = Arrays.asList(pfizer, coronavac, jhonson);

		// Mock
		when(vacinaService.obterTodos()).thenReturn(vacinas);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/vacina"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(VACINA_ID_PFIZER))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].fabricante").value("PFIZER"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].lote").value("123ABC"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].dataDeValidade").value(String.valueOf(pfizer.getDataDeValidade())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroDeDoses").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].intervaloDeDoses").value(21))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(VACINA_ID_CORONAVAC))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].fabricante").value("CORONAVAC"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].lote").value("456DEF"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(VACINA_ID_JHONSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].fabricante").value("JHONSON & JHONSON"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].lote").value("789GHI"));

		// Verify
		verify(vacinaService, times(2)).obterTodos();
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia quando não há vacinas cadastradas")
	public void testObterListaVazia() throws Exception {
		// Arrange
		List<Vacina> vacinas = new ArrayList<>();

		// Mock
		when(vacinaService.obterTodos()).thenReturn(vacinas);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/vacina"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));

		// Verify
		verify(vacinaService, times(2)).obterTodos();
	}

	@Test
	@DisplayName("Deve ser possível obter uma vacina pelo ID")
	public void testObterVacinaPeloId() throws Exception {
		// Arrange
		Vacina pfizer = new Vacina(VACINA_ID_PFIZER, "PFIZER", "123ABC", LocalDate.of(2024,5,11), 2, 21);

		// Mock
		when(vacinaService.encontrarVacina(VACINA_ID_PFIZER)).thenReturn(pfizer);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/vacina/" + VACINA_ID_PFIZER))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(VACINA_ID_PFIZER))
				.andExpect(MockMvcResultMatchers.jsonPath("$.fabricante").value("PFIZER"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lote").value("123ABC"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dataDeValidade").value(String.valueOf(pfizer.getDataDeValidade())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.numeroDeDoses").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$.intervaloDeDoses").value(21));

		// Verify
		verify(vacinaService, times(1)).encontrarVacina(VACINA_ID_PFIZER);
	}

	@Test
	@DisplayName("Deve lançar uma ResourceNotFoundException ao buscar um ID inexistente")
	public void testObterVacinaPorIdInexistente() throws Exception {
		// Arrange
		String idInexistente = "999";

		// Mock
		when(vacinaService.encontrarVacina(idInexistente)).thenThrow(ResourceNotFoundException.class);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/vacina/" + idInexistente))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

		// Verify
		verify(vacinaService, times(1)).encontrarVacina(idInexistente);
	}

	@Test
	@DisplayName("Deve adicionar uma vacina no banco de dados")
	public void testAdicionarVacina() throws Exception {
		// Arrange
		Vacina novaVacina = new Vacina("4", "NOVA VACINA", "XYZ123", LocalDate.now(), 2, 28);

		// Mock
		doNothing().when(vacinaService).inserir(novaVacina);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		String novaVacinaJson;
		novaVacinaJson = objectMapper.writeValueAsString(novaVacina);

		// Opcional: Desabilita pretty-print

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/vacina")
						.contentType(MediaType.APPLICATION_JSON)
						.content(novaVacinaJson))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(novaVacina.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.fabricante").value(novaVacina.getFabricante()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lote").value(novaVacina.getLote()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dataDeValidade").value(String.valueOf(novaVacina.getDataDeValidade())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.numeroDeDoses").value(novaVacina.getNumeroDeDoses()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.intervaloDeDoses").value(novaVacina.getIntervaloDeDoses()));

		// Verify
		verify(vacinaService, times(1)).inserir(novaVacina);
	}

	@Test
	@DisplayName("Deve atualizar uma vacina no banco de dados")
	public void testAtualizarVacina() throws Exception {
		// Arrange
		String vacinaId = "4"; // Substitua pelo ID da vacina que deseja atualizar
		Vacina vacinaAtualizada = new Vacina("4", "VACINA ATUALIZADA", "ABC456", LocalDate.parse("2023-12-02"), 3, 21);

		// Mock
		when(vacinaService.atualizar(eq(vacinaId), any(Vacina.class))).thenReturn(vacinaAtualizada);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		String vacinaAtualizadaJson = objectMapper.writeValueAsString(vacinaAtualizada);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/vacina/{id}", vacinaId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(vacinaAtualizadaJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(vacinaAtualizada.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.fabricante").value(vacinaAtualizada.getFabricante()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lote").value(vacinaAtualizada.getLote()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dataDeValidade").value(String.valueOf(vacinaAtualizada.getDataDeValidade())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.numeroDeDoses").value(vacinaAtualizada.getNumeroDeDoses()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.intervaloDeDoses").value(vacinaAtualizada.getIntervaloDeDoses()));

		// Verify
		verify(vacinaService, times(1)).atualizar(eq(vacinaId), any(Vacina.class));
	}


	@Test
	@DisplayName("Deve excluir uma vacina do banco de dados")
	public void testExcluirVacina() throws Exception {
		// Arrange
		String vacinaId = "4"; // Substitua pelo ID da vacina que deseja excluir

		// Mock
		doNothing().when(vacinaService).deletar(vacinaId);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.delete("/vacina/{id}", vacinaId))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		// Verify
		verify(vacinaService, times(1)).deletar(vacinaId);
	}
	@Test
	@DisplayName("Deve retornar erro ao tentar deletar um paciente utilizando um ID que não consta no banco de dados. ")
	public void testeDeletarVacinaComIdInvalido() throws Exception {

		//Arrange
		String vacinaId = "1010101010";

		//Mock
		doThrow(new ResourceNotFoundException("Vacina não encontrada")).when(vacinaService).deletar(vacinaId);

		//Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.delete("/vacina/{id}", vacinaId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
