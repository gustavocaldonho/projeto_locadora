package br.edu.ifes.col.locadorabackend;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
 * Testes da Sprint 4: login, controle de acesso e Consultar Título.
 * Usa os usuários de DadosIniciais: admin/admin123 (funcionário) e cliente/cliente123 (cliente).
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SegurancaEConsultaTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void semLoginDevolve401SemAbrirJanelaDoNavegador() throws Exception {
		mvc.perform(get("/resources/atores"))
				.andExpect(status().isUnauthorized())
				.andExpect(header().doesNotExist("WWW-Authenticate"))
				.andExpect(jsonPath("$.status").value(401));
		mvc.perform(get("/resources/titulos/consulta")).andExpect(status().isUnauthorized());
	}

	@Test
	void loginComSenhaErradaDevolve401() throws Exception {
		mvc.perform(get("/resources/auth/usuario").with(httpBasic("admin", "errada")))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void loginDevolveOUsuarioSemASenha() throws Exception {
		mvc.perform(get("/resources/auth/usuario").with(httpBasic("admin", "admin123")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.perfil").value("FUNCIONARIO"))
				.andExpect(jsonPath("$.senha").doesNotExist());
		mvc.perform(get("/resources/auth/usuario").with(httpBasic("cliente", "cliente123")))
				.andExpect(jsonPath("$.perfil").value("CLIENTE"));
	}

	@Test
	void funcionarioAcessaTudo() throws Exception {
		mvc.perform(get("/resources/locacoes").with(httpBasic("admin", "admin123"))).andExpect(status().isOk());
		mvc.perform(get("/resources/titulos/consulta").with(httpBasic("admin", "admin123"))).andExpect(status().isOk());
	}

	@Test
	void clienteSoConsultaTitulos() throws Exception {
		mvc.perform(get("/resources/titulos/consulta").with(httpBasic("cliente", "cliente123")))
				.andExpect(status().isOk());
		mvc.perform(get("/resources/titulos/consulta/categorias").with(httpBasic("cliente", "cliente123")))
				.andExpect(status().isOk());
		mvc.perform(get("/resources/atores").with(httpBasic("cliente", "cliente123")))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.error").value("Acesso negado"));
		mvc.perform(get("/resources/locacoes").with(httpBasic("cliente", "cliente123")))
				.andExpect(status().isForbidden());
		mvc.perform(delete("/resources/titulos/3").with(httpBasic("cliente", "cliente123")))
				.andExpect(status().isForbidden());
	}

	@Test
	void consultaPorNomeMostraCopiasDisponiveis() throws Exception {
		// Matrix tem 2 cópias e a MTX-001 está locada
		mvc.perform(get("/resources/titulos/consulta").param("nome", "matr").with(httpBasic("cliente", "cliente123")))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].nome").value("Matrix"))
				.andExpect(jsonPath("$[0].totalItens").value(2))
				.andExpect(jsonPath("$[0].itensDisponiveis").value(1))
				.andExpect(jsonPath("$[0].atores.length()").value(3))
				.andExpect(jsonPath("$[0].valorLocacao").value(6.0))
				.andExpect(jsonPath("$[0].previsaoDisponibilidade").doesNotExist());
	}

	@Test
	void consultaPorCategoriaEAtor() throws Exception {
		mvc.perform(get("/resources/titulos/consulta").param("categoria", "Ficção científica").with(httpBasic("cliente", "cliente123")))
				.andExpect(jsonPath("$.length()").value(2));
		// Ator 4 = Fernanda Montenegro, em Central do Brasil (única cópia locada, em atraso)
		mvc.perform(get("/resources/titulos/consulta").param("atorId", "4").with(httpBasic("cliente", "cliente123")))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].nome").value("Central do Brasil"))
				.andExpect(jsonPath("$[0].itensDisponiveis").value(0))
				.andExpect(jsonPath("$[0].previsaoDisponibilidade").exists());
		mvc.perform(get("/resources/titulos/consulta/categorias").with(httpBasic("cliente", "cliente123")))
				.andExpect(jsonPath("$[0]").value("Drama"));
	}

	@Test
	void funcionarioCadastraUsuarioENaoPodeFicarSemFuncionario() throws Exception {
		String json = """
				{"login":"maria","senha":"segredo1","nome":"Maria Atendente","perfil":"FUNCIONARIO"}
				""";
		mvc.perform(post("/resources/usuarios").with(httpBasic("admin", "admin123"))
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.senha").doesNotExist());
		mvc.perform(get("/resources/auth/usuario").with(httpBasic("maria", "segredo1"))).andExpect(status().isOk());
		// Com dois funcionários dá para excluir um, mas não o último
		mvc.perform(delete("/resources/usuarios/3").with(httpBasic("admin", "admin123"))).andExpect(status().isNoContent());
		mvc.perform(delete("/resources/usuarios/1").with(httpBasic("admin", "admin123")))
				.andExpect(status().isBadRequest());
	}

	@Test
	void swaggerContinuaLiberado() throws Exception {
		mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
	}

}
