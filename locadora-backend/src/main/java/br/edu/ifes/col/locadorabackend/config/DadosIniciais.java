package br.edu.ifes.col.locadorabackend.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifes.col.locadorabackend.domain.Ator;
import br.edu.ifes.col.locadorabackend.domain.Classe;
import br.edu.ifes.col.locadorabackend.domain.Diretor;
import br.edu.ifes.col.locadorabackend.domain.Item;
import br.edu.ifes.col.locadorabackend.domain.TipoItem;
import br.edu.ifes.col.locadorabackend.domain.Titulo;
import br.edu.ifes.col.locadorabackend.repositories.AtorRepository;
import br.edu.ifes.col.locadorabackend.repositories.ClasseRepository;
import br.edu.ifes.col.locadorabackend.repositories.DiretorRepository;
import br.edu.ifes.col.locadorabackend.repositories.ItemRepository;
import br.edu.ifes.col.locadorabackend.repositories.TituloRepository;

/**
 * Cadastra alguns dados de exemplo quando o banco está vazio.
 * Se já existir algum ator cadastrado, não faz nada.
 */
@Component
public class DadosIniciais implements CommandLineRunner {

	private final AtorRepository atorRepository;
	private final DiretorRepository diretorRepository;
	private final ClasseRepository classeRepository;
	private final TituloRepository tituloRepository;
	private final ItemRepository itemRepository;

	public DadosIniciais(AtorRepository atorRepository, DiretorRepository diretorRepository,
			ClasseRepository classeRepository, TituloRepository tituloRepository, ItemRepository itemRepository) {
		this.atorRepository = atorRepository;
		this.diretorRepository = diretorRepository;
		this.classeRepository = classeRepository;
		this.tituloRepository = tituloRepository;
		this.itemRepository = itemRepository;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (atorRepository.count() > 0) {
			return;
		}

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
