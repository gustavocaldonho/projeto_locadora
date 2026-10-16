package br.edu.ifes.col.locadorabackend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testa a API de ponta a ponta usando os dados de DadosIniciais.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LocadoraBackendApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void listaAtoresDosDadosIniciais() throws Exception {
		mvc.perform(get("/resources/atores"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(6));
	}

	@Test
	void incluiAlteraEExcluiAtor() throws Exception {
		mvc.perform(post("/resources/atores").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Tom Hanks\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(7));
		mvc.perform(put("/resources/atores/7").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Thomas Hanks\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Thomas Hanks"));
		mvc.perform(delete("/resources/atores/7")).andExpect(status().isNoContent());
		mvc.perform(get("/resources/atores/7"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.path").value("/resources/atores/7"));
	}

	@Test
	void naoIncluiAtorSemNome() throws Exception {
		mvc.perform(post("/resources/atores").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("O nome é obrigatório"));
	}

	@Test
	void naoExcluiAtorDiretorOuClasseLigadosATitulo() throws Exception {
		mvc.perform(delete("/resources/atores/1")).andExpect(status().isBadRequest());
		mvc.perform(delete("/resources/diretores/1")).andExpect(status().isBadRequest());
		mvc.perform(delete("/resources/classes/1")).andExpect(status().isBadRequest());
	}

	@Test
	void incluiTituloComAtoresDiretorEClasse() throws Exception {
		String json = """
				{"nome":"Tropa de Elite","ano":2007,"sinopse":"Capitão do BOPE.","categoria":"Ação",
				 "atores":[{"id":4},{"id":5}],"diretor":{"id":2},"classe":{"id":3}}
				""";
		mvc.perform(post("/resources/titulos").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.atores.length()").value(2))
				.andExpect(jsonPath("$.diretor.nome").value("Walter Salles"))
				.andExpect(jsonPath("$.classe.prazoDevolucao").value(5));
	}

	@Test
	void naoIncluiTituloComAtorInexistente() throws Exception {
		String json = """
				{"nome":"X","ano":2000,"categoria":"Drama","atores":[{"id":99}],"diretor":{"id":1},"classe":{"id":1}}
				""";
		mvc.perform(post("/resources/titulos").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound());
	}

	@Test
	void listaTitulosComAtores() throws Exception {
		mvc.perform(get("/resources/titulos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[2].nome").value("Matrix"))
				.andExpect(jsonPath("$[2].atores.length()").value(3));
		mvc.perform(get("/resources/itens"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(4));
	}

	@Test
	void naoExcluiTituloComItens() throws Exception {
		mvc.perform(delete("/resources/titulos/1")).andExpect(status().isBadRequest());
	}

	@Test
	void incluiItemENaoAceitaNumeroDeSerieRepetido() throws Exception {
		String json = """
				{"numSerie":"MTX-003","dtAquisicao":"2025-05-01","tipoItem":"DVD","titulo":{"id":1}}
				""";
		mvc.perform(post("/resources/itens").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.titulo.nome").value("Matrix"));
		mvc.perform(post("/resources/itens").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	void recusaTipoDeItemInvalido() throws Exception {
		String json = """
				{"numSerie":"X-1","dtAquisicao":"2025-05-01","tipoItem":"VHS","titulo":{"id":1}}
				""";
		mvc.perform(post("/resources/itens").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	void swaggerDisponivel() throws Exception {
		mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
	}

}
