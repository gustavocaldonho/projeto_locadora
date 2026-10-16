INSERT INTO ator (nome) VALUES ('Keanu Reeves'), ('Carrie-Anne Moss'), ('Laurence Fishburne'), ('Fernanda Montenegro'), ('Vinícius de Oliveira'), ('Leonardo DiCaprio');

INSERT INTO diretor (nome) VALUES ('Lana Wachowski'), ('Walter Salles'), ('Christopher Nolan');

INSERT INTO classe (nome, valor, prazo_devolucao) VALUES ('Lançamento', 8.50, 2), ('Ouro', 6.00, 3), ('Catálogo', 4.00, 5);

INSERT INTO titulo (nome, ano, sinopse, categoria, diretor_id, classe_id) VALUES
  ('Matrix', 1999, 'Um hacker descobre que a realidade é uma simulação.', 'Ficção científica', 1, 2),
  ('Central do Brasil', 1998, 'Uma ex-professora acompanha um menino em busca do pai.', 'Drama', 2, 3),
  ('A Origem', 2010, 'Um ladrão invade sonhos para roubar segredos.', 'Ficção científica', 3, 1);

INSERT INTO titulo_ator (titulo_id, ator_id) VALUES (1, 1), (1, 2), (1, 3), (2, 4), (2, 5), (3, 6);

INSERT INTO item (num_serie, dt_aquisicao, tipo_item, titulo_id) VALUES
  ('MTX-001', '2024-03-10', 'DVD', 1),
  ('MTX-002', '2024-03-10', 'BLURAY', 1),
  ('CDB-001', '2023-11-02', 'FITA', 2),
  ('ORG-001', '2025-01-15', 'BLURAY', 3);
