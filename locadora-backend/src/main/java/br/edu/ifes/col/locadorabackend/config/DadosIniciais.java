package br.edu.ifes.col.locadorabackend.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifes.col.locadorabackend.domain.Ator;
import br.edu.ifes.col.locadorabackend.domain.Classe;
import br.edu.ifes.col.locadorabackend.domain.Cliente;
import br.edu.ifes.col.locadorabackend.domain.Dependente;
import br.edu.ifes.col.locadorabackend.domain.Diretor;
import br.edu.ifes.col.locadorabackend.domain.Item;
import br.edu.ifes.col.locadorabackend.domain.Locacao;
import br.edu.ifes.col.locadorabackend.domain.Perfil;
import br.edu.ifes.col.locadorabackend.domain.Sexo;
import br.edu.ifes.col.locadorabackend.domain.Socio;
import br.edu.ifes.col.locadorabackend.domain.TipoItem;
import br.edu.ifes.col.locadorabackend.domain.Titulo;
import br.edu.ifes.col.locadorabackend.domain.Usuario;
import br.edu.ifes.col.locadorabackend.repositories.AtorRepository;
import br.edu.ifes.col.locadorabackend.repositories.ClasseRepository;
import br.edu.ifes.col.locadorabackend.repositories.ClienteRepository;
import br.edu.ifes.col.locadorabackend.repositories.DependenteRepository;
import br.edu.ifes.col.locadorabackend.repositories.DiretorRepository;
import br.edu.ifes.col.locadorabackend.repositories.ItemRepository;
import br.edu.ifes.col.locadorabackend.repositories.LocacaoRepository;
import br.edu.ifes.col.locadorabackend.repositories.SocioRepository;
import br.edu.ifes.col.locadorabackend.repositories.TituloRepository;
import br.edu.ifes.col.locadorabackend.repositories.UsuarioRepository;

/**
 * Cadastra alguns dados de exemplo quando o banco está vazio.
 * O acervo só é cadastrado se não houver nenhum ator, os clientes
 * e locações só se não houver nenhum cliente, e os usuários de login
 * (admin/admin123 e cliente/cliente123) só se não houver nenhum usuário.
 */
@Component
public class DadosIniciais implements CommandLineRunner {

	private final AtorRepository atorRepository;
	private final DiretorRepository diretorRepository;
	private final ClasseRepository classeRepository;
	private final TituloRepository tituloRepository;
	private final ItemRepository itemRepository;
	private final ClienteRepository clienteRepository;
	private final SocioRepository socioRepository;
	private final DependenteRepository dependenteRepository;
	private final LocacaoRepository locacaoRepository;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public DadosIniciais(AtorRepository atorRepository, DiretorRepository diretorRepository,
			ClasseRepository classeRepository, TituloRepository tituloRepository, ItemRepository itemRepository,
			ClienteRepository clienteRepository, SocioRepository socioRepository,
			DependenteRepository dependenteRepository, LocacaoRepository locacaoRepository,
			UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.atorRepository = atorRepository;
		this.diretorRepository = diretorRepository;
		this.classeRepository = classeRepository;
		this.tituloRepository = tituloRepository;
		this.itemRepository = itemRepository;
		this.clienteRepository = clienteRepository;
		this.socioRepository = socioRepository;
		this.dependenteRepository = dependenteRepository;
		this.locacaoRepository = locacaoRepository;
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (atorRepository.count() == 0) {
			cadastrarAcervo();
		}
		if (clienteRepository.count() == 0) {
			cadastrarClientesELocacoes();
		}
		if (usuarioRepository.count() == 0) {
			usuarioRepository.save(new Usuario("admin", passwordEncoder.encode("admin123"), "Funcionário", Perfil.FUNCIONARIO));
			usuarioRepository.save(new Usuario("cliente", passwordEncoder.encode("cliente123"), "Cliente", Perfil.CLIENTE));
		}
	}

	private void cadastrarAcervo() {

		Ator keanu = atorRepository.save(new Ator(null, "Keanu Reeves"));
		Ator carrie = atorRepository.save(new Ator(null, "Carrie-Anne Moss"));
		Ator laurence = atorRepository.save(new Ator(null, "Laurence Fishburne"));
		Ator fernanda = atorRepository.save(new Ator(null, "Fernanda Montenegro"));
		Ator vinicius = atorRepository.save(new Ator(null, "Vinícius de Oliveira"));
		Ator leonardo = atorRepository.save(new Ator(null, "Leonardo DiCaprio"));

		Diretor lana = diretorRepository.save(new Diretor(null, "Lana Wachowski"));
		Diretor walter = diretorRepository.save(new Diretor(null, "Walter Salles"));
		Diretor nolan = diretorRepository.save(new Diretor(null, "Christopher Nolan"));

		Classe lancamento = classeRepository.save(new Classe(null, "Lançamento", 8.50, 2));
		Classe ouro = classeRepository.save(new Classe(null, "Ouro", 6.00, 3));
		Classe catalogo = classeRepository.save(new Classe(null, "Catálogo", 4.00, 5));

		Titulo matrix = tituloRepository.save(titulo("Matrix", 1999,
				"Um hacker descobre que a realidade é uma simulação.", "Ficção científica",
				List.of(keanu, carrie, laurence), lana, ouro));
		Titulo central = tituloRepository.save(titulo("Central do Brasil", 1998,
				"Uma ex-professora acompanha um menino em busca do pai.", "Drama",
				List.of(fernanda, vinicius), walter, catalogo));
		Titulo origem = tituloRepository.save(titulo("A Origem", 2010,
				"Um ladrão invade sonhos para roubar segredos.", "Ficção científica",
				List.of(leonardo), nolan, lancamento));

		itemRepository.save(item("MTX-001", LocalDate.of(2024, 3, 10), TipoItem.DVD, matrix));
		itemRepository.save(item("MTX-002", LocalDate.of(2024, 3, 10), TipoItem.BLURAY, matrix));
		itemRepository.save(item("CDB-001", LocalDate.of(2023, 11, 2), TipoItem.FITA, central));
		itemRepository.save(item("ORG-001", LocalDate.of(2025, 1, 15), TipoItem.BLURAY, origem));
	}

	private void cadastrarClientesELocacoes() {
		Socio joao = socioRepository.save(socio("João da Silva", LocalDate.of(1985, 4, 12), Sexo.MASCULINO,
				"123.456.789-00", "Rua das Flores, 100 - Colatina/ES", "(27) 99999-1111"));
		Socio maria = socioRepository.save(socio("Maria Oliveira", LocalDate.of(1990, 9, 3), Sexo.FEMININO,
				"987.654.321-00", "Av. Getúlio Vargas, 500 - Colatina/ES", "(27) 98888-2222"));
		Dependente pedro = dependenteRepository.save(dependente("Pedro da Silva", LocalDate.of(2012, 1, 20),
				Sexo.MASCULINO, joao));
		dependenteRepository.save(dependente("Ana da Silva", LocalDate.of(2015, 6, 8), Sexo.FEMININO, joao));

		LocalDate hoje = LocalDate.now();
		// Locação vigente, dentro do prazo
		itemRepository.findByNumSerie("MTX-001").ifPresent(item ->
				locacaoRepository.save(locacao(item, maria, hoje.minusDays(1), hoje.plusDays(2), false)));
		// Locação em atraso: João fica em débito e não consegue locar até devolver
		itemRepository.findByNumSerie("CDB-001").ifPresent(item ->
				locacaoRepository.save(locacao(item, joao, hoje.minusDays(8), hoje.minusDays(3), false)));
		// Locação já devolvida
		itemRepository.findByNumSerie("ORG-001").ifPresent(item -> {
			Locacao devolvida = locacao(item, pedro, hoje.minusDays(20), hoje.minusDays(18), true);
			devolvida.setDtDevolucaoEfetiva(hoje.minusDays(18));
			devolvida.setMultaCobrada(0.0);
			locacaoRepository.save(devolvida);
		});
	}

	private Socio socio(String nome, LocalDate nascimento, Sexo sexo, String cpf, String endereco, String tel) {
		Socio socio = new Socio();
		socio.setNome(nome);
		socio.setDtNascimento(nascimento);
		socio.setSexo(sexo);
		socio.setCpf(cpf);
		socio.setEndereco(endereco);
		socio.setTel(tel);
		return socio;
	}

	private Dependente dependente(String nome, LocalDate nascimento, Sexo sexo, Socio socio) {
		Dependente dependente = new Dependente();
		dependente.setNome(nome);
		dependente.setDtNascimento(nascimento);
		dependente.setSexo(sexo);
		dependente.setSocio(socio);
		return dependente;
	}

	private Locacao locacao(Item item, Cliente cliente, LocalDate dtLocacao, LocalDate dtPrevista, boolean paga) {
		Locacao locacao = new Locacao();
		locacao.setItem(item);
		locacao.setCliente(cliente);
		locacao.setDtLocacao(dtLocacao);
		locacao.setDtDevolucaoPrevista(dtPrevista);
		locacao.setValorCobrado(item.getTitulo().getClasse().getValor());
		locacao.setPaga(paga);
		return locacao;
	}

	private Titulo titulo(String nome, int ano, String sinopse, String categoria, List<Ator> atores,
			Diretor diretor, Classe classe) {
		Titulo titulo = new Titulo();
		titulo.setNome(nome);
		titulo.setAno(ano);
		titulo.setSinopse(sinopse);
		titulo.setCategoria(categoria);
		titulo.setAtores(new ArrayList<>(atores));
		titulo.setDiretor(diretor);
		titulo.setClasse(classe);
		return titulo;
	}

	private Item item(String numSerie, LocalDate dtAquisicao, TipoItem tipo, Titulo titulo) {
		Item item = new Item();
		item.setNumSerie(numSerie);
		item.setDtAquisicao(dtAquisicao);
		item.setTipoItem(tipo);
		item.setTitulo(titulo);
		return item;
	}

}
