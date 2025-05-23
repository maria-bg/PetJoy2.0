CREATE TABLE cliente (
  cpf VARCHAR(14) PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  telefone VARCHAR(20)
);

CREATE TABLE pets (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  idade INT,
  dono VARCHAR(14),
  data_entrada DATE,
  data_saida DATE,
  FOREIGN KEY (dono) REFERENCES cliente(cpf) ON DELETE CASCADE
);

CREATE TABLE materiais (
  id INT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL
);

CREATE TABLE atividade (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo VARCHAR(50),
  data DATE,
  id_pet INT,
  FOREIGN KEY (id_pet) REFERENCES pets(id) ON DELETE CASCADE
);

CREATE TABLE material_utilizado (
  id_atividade INT,
  id_material INT,
  quantidade INT,
  PRIMARY KEY (id_atividade, id_material),
  FOREIGN KEY (id_atividade) REFERENCES atividade(id) ON DELETE CASCADE,
  FOREIGN KEY (id_material) REFERENCES materiais(id)
);

INSERT INTO cliente (cpf, nome, telefone) VALUES
('111.111.111-11', 'Joana', '99999-1111'),
('222.222.222-22', 'Carlos', '98888-2222'),
('333.333.333-33', 'Ana', '97777-3333'),
('444.444.444-44', 'Maria', '12345-6789'),
('555.555.555-55', 'Bruno', '98765-4321'),
('666.666.666-66', 'Fernanda', '91234-5678');

INSERT INTO pets (nome, idade, dono, data_entrada, data_saida) VALUES
('Rex', 4, '111.111.111-11', '2025-05-10', '2025-05-20'),
('Luna', 2, '222.222.222-22', '2025-05-12', '2025-05-17'),
('Mel', 3, '333.333.333-33', '2025-05-14', '2025-05-19'),
('Toby', 1, '444.444.444-44', '2025-05-18', '2025-05-22'),
('Mia', 5, '555.555.555-55', '2025-05-19', '2025-05-24'),
('Thor', 2, '666.666.666-66', '2025-05-20', '2025-05-25');

INSERT INTO materiais (id, nome) VALUES
(1, 'Shampoo'),
(2, 'Toalha'),
(3, 'Condicionador'),
(4, 'Escova'),
(5, 'Sabonete'),
(6, 'Tesoura');

INSERT INTO atividade (tipo, data, id_pet) VALUES
('Banho', '2025-05-12', 1),
('Tosa', '2025-05-14', 2),
('Banho', '2025-05-15', 3),
('Vacina', '2025-05-18', 4),
('Consulta', '2025-05-19', 5),
('Banho', '2025-05-20', 6);

INSERT INTO material_utilizado (id_atividade, id_material, quantidade) VALUES
(1, 1, 1),
(1, 2, 1),
(2, 4, 1),
(2, 6, 1),
(3, 1, 1),
(3, 3, 1);