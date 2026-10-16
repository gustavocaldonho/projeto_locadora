package br.edu.ifes.col.locadorabackend;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes da Sprint 3, usando os dados de DadosIniciais:
 * sócios João (1, com dependentes Pedro 3 e Ana 4) e Maria (2);
 * itens MTX-001 (1), MTX-002 (2), CDB-001 (3) e ORG-001 (4);
 * locações: Maria com MTX-001 (vigente), João com CDB-001 (3 dias de atraso),
 * Pedro com ORG-001 (devolvida).
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "FUNCIONARIO")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AtendimentoClienteTests {

	@Autowired
	private MockMvc mvc;

	private void enviar(String metodo, String url, String json, int statusEsperado) throws Exception {
		var requisicao = switch (metodo) {
			case "POST" -> post(url);
			case "PUT" -> put(url);
			default -> throw new IllegalArgumentException(metodo);
		};
		mvc.perform(requisicao.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is(statusEsperado));
	}

	private String dependente(String nome, long socio) {
		return """
				{"nome":"%s","dtNascimento":"2010-01-01","sexo":"FEMININO","socio":{"numInscricao":%d}}
				""".formatted(nome, socio);
	}

	@Test
	void listaSociosEClientes() throws Exception {
		mvc.perform(get("/resources/socios")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
		mvc.perform(get("/resources/clientes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(4))
				.andExpect(jsonPath("$[?(@.nome == 'Pedro da Silva')].tipo").value("DEPENDENTE"));
		mvc.perform(get("/resources/socios/1/dependentes")).andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	void inscreveSocioAtivoENaoAceitaCpfRepetido() throws Exception {
		String json = """
				{"nome":"Carlos","dtNascimento":"1980-05-05","sexo":"MASCULINO","cpf":"111.222.333-44",
				 "endereco":"Rua A","tel":"2799999"}
				""";
		mvc.perform(post("/resources/socios").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.numInscricao").value(5))
				.andExpect(jsonPath("$.estahAtivo").value(true))
				.andExpect(jsonPath("$.tipo").value("SOCIO"));
		enviar("POST", "/resources/socios", json, 400);
	}

	@Test
	void socioTemNoMaximoTresDependentesAtivos() throws Exception {
		enviar("POST", "/resources/dependentes", dependente("Bia", 1), 201);
		mvc.perform(post("/resources/dependentes").contentType(MediaType.APPLICATION_JSON).content(dependente("Caio", 1)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("O sócio já possui 3 dependentes ativos"));
		// Desativando um, abre vaga
		mvc.perform(put("/resources/dependentes/4/desativar")).andExpect(jsonPath("$.estahAtivo").value(false));
		enviar("POST", "/resources/dependentes", dependente("Caio", 1), 201);
		// E agora a Ana não pode ser reativada
		mvc.perform(put("/resources/dependentes/4/reativar")).andExpect(status().isBadRequest());
	}

	@Test
	void desativarSocioDesativaDependentesEReativarVolta() throws Exception {
		mvc.perform(put("/resources/socios/1/desativar")).andExpect(jsonPath("$.estahAtivo").value(false));
		mvc.perform(get("/resources/socios/1/dependentes"))
				.andExpect(jsonPath("$[0].estahAtivo").value(false))
				.andExpect(jsonPath("$[1].estahAtivo").value(false));
		enviar("POST", "/resources/dependentes", dependente("Bia", 1), 400);
		mvc.perform(put("/resources/socios/1/reativar")).andExpect(jsonPath("$.estahAtivo").value(true));
		mvc.perform(get("/resources/socios/1/dependentes"))
				.andExpect(jsonPath("$[0].estahAtivo").value(true))
				.andExpect(jsonPath("$[1].estahAtivo").value(true));
	}

	@Test
	void naoExcluiSocioComLocacoesMasExcluiSocioSemLocacoesComDependentes() throws Exception {
		mvc.perform(delete("/resources/socios/1")).andExpect(status().isBadRequest());
		String json = """
				{"nome":"Carlos","dtNascimento":"1980-05-05","sexo":"MASCULINO","cpf":"11122233344",
				 "endereco":"Rua A","tel":"2799999"}
				""";
		enviar("POST", "/resources/socios", json, 201);
		enviar("POST", "/resources/dependentes", dependente("Filho do Carlos", 5), 201);
		mvc.perform(delete("/resources/socios/5")).andExpect(status().isNoContent());
		mvc.perform(get("/resources/dependentes/6")).andExpect(status().isNotFound());
	}

	@Test
	void efetuaLocacaoComValorEPrazoDaClasse() throws Exception {
		mvc.perform(post("/resources/locacoes").contentType(MediaType.APPLICATION_JSON)
				.content("{\"clienteId\":2,\"itemId\":2}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.valorCobrado").value(6.0))
				.andExpect(jsonPath("$.dtLocacao").value(LocalDate.now().toString()))
				.andExpect(jsonPath("$.dtDevolucaoPrevista").value(LocalDate.now().plusDays(3).toString()))
				.andExpect(jsonPath("$.situacao").value("VIGENTE"))
				.andExpect(jsonPath("$.cliente.nome").value("Maria Oliveira"));
		mvc.perform(get("/resources/locacoes/itens-disponiveis"))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].numSerie").value("ORG-001"));
	}

	@Test
	void funcionarioPodeInformarOutroValorEPrazo() throws Exception {
		String prevista = LocalDate.now().plusDays(7).toString();
		mvc.perform(post("/resources/locacoes").contentType(MediaType.APPLICATION_JSON)
				.content("{\"clienteId\":2,\"itemId\":2,\"valorCobrado\":3.5,\"dtDevolucaoPrevista\":\"" + prevista + "\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.valorCobrado").value(3.5))
				.andExpect(jsonPath("$.dtDevolucaoPrevista").value(prevista));
		String hoje = LocalDate.now().toString();
		mvc.perform(post("/resources/locacoes").contentType(MediaType.APPLICATION_JSON)
				.content("{\"clienteId\":2,\"itemId\":4,\"dtDevolucaoPrevista\":\"" + hoje + "\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void clienteEmDebitoNaoLoca() throws Exception {
		mvc.perform(post("/resources/locacoes").contentType(MediaType.APPLICATION_JSON)
				.content("{\"clienteId\":1,\"itemId\":2}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("está em débito")))
				.andExpect(jsonPath("$.message").value(containsString("Central do Brasil")));
	}

	@Test
	void itemJaLocadoNaoPodeSerLocadoDeNovo() throws Exception {
		mvc.perform(post("/resources/locacoes").contentType(MediaType.APPLICATION_JSON)
				.content("{\"clienteId\":3,\"itemId\":1}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("já está locado")));
	}

	@Test
	void clienteInativoNaoLoca() throws Exception {
		mvc.perform(put("/resources/socios/2/desativar")).andExpect(status().isOk());
		enviar("POST", "/resources/locacoes", "{\"clienteId\":2,\"itemId\":2}", 400);
	}

	@Test
	void soCancelaLocacaoNaoPaga() throws Exception {
		mvc.perform(delete("/resources/locacoes/3")).andExpect(status().isBadRequest());
		mvc.perform(delete("/resources/locacoes/1")).andExpect(status().isNoContent());
		mvc.perform(get("/resources/locacoes/1")).andExpect(status().isNotFound());
	}

	@Test
	void alteraLocacaoVigente() throws Exception {
		String prevista = LocalDate.now().plusDays(10).toString();
		mvc.perform(put("/resources/locacoes/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"valorCobrado\":5.0,\"dtDevolucaoPrevista\":\"" + prevista + "\",\"paga\":true}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.valorCobrado").value(5.0))
				.andExpect(jsonPath("$.paga").value(true));
		mvc.perform(put("/resources/locacoes/3").contentType(MediaType.APPLICATION_JSON).content("{\"valorCobrado\":1}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void devolucaoComAtrasoCobraMulta() throws Exception {
		// CDB-001: classe Catálogo (R$ 4,00), 3 dias de atraso => multa R$ 12,00 + locação R$ 4,00
		mvc.perform(get("/resources/devolucoes/CDB-001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.diasAtraso").value(3))
				.andExpect(jsonPath("$.multa").value(12.0))
				.andExpect(jsonPath("$.valorAPagar").value(16.0));
		mvc.perform(post("/resources/devolucoes/CDB-001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.locacao.situacao").value("DEVOLVIDA"))
				.andExpect(jsonPath("$.locacao.multaCobrada").value(12.0))
				.andExpect(jsonPath("$.locacao.dtDevolucaoEfetiva").value(LocalDate.now().toString()));
		// Devolvido, o João sai do débito e pode locar
		enviar("POST", "/resources/locacoes", "{\"clienteId\":1,\"itemId\":3}", 201);
	}

	@Test
	void devolucaoSemAtrasoNaoTemMulta() throws Exception {
		mvc.perform(post("/resources/devolucoes/MTX-001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.diasAtraso").value(0))
				.andExpect(jsonPath("$.multa").value(0.0))
				.andExpect(jsonPath("$.valorAPagar").value(6.0));
	}

	@Test
	void devolucaoDeItemNaoLocadoOuInexistente() throws Exception {
		mvc.perform(get("/resources/devolucoes/MTX-002"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("O item MTX-002 não está locado no momento"));
		mvc.perform(get("/resources/devolucoes/XYZ")).andExpect(status().isNotFound());
	}

	@Test
	void naoExcluiItemComLocacoes() throws Exception {
		mvc.perform(delete("/resources/itens/1")).andExpect(status().isBadRequest());
		mvc.perform(delete("/resources/itens/2")).andExpect(status().isNoContent());
	}

}
