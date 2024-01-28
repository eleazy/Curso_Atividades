 CREATE TABLE produtos (
	  id INT PRIMARY KEY AUTO_INCREMENT,
	  nome VARCHAR(100),
	  estoque integer,
	  estoqueminimo integer   
);  

/* 1. Criar, na tabela de produtos, um campo para armazenar o usuário que realizou 
a inclusão do registro e outro para armazenar o usuário que realizou a última modificação. */
ALTER TABLE produtos 
	ADD user_inclusao VARCHAR(100),
	ADD user_modificacao VARCHAR(100);

/* 2. Criar uma tabela “auditoriaproduto” com os campos:
 id, data/hora, ação realizada (inclusão ou alteração) e usuário. */
CREATE TABLE auditoriaproduto (
	id INT PRIMARY KEY AUTO_INCREMENT,
    data_hora DATETIME,
    acao_realizada VARCHAR(10),
    usuario VARCHAR(100),
    nome_produto VARCHAR(100),
    id_produto INTEGER,
    FOREIGN KEY (id_produto) REFERENCES produtos(id)
);

DELIMITER //
/* 3. Trigger na tabela de produtos que inclua o registro na tabela
 “auditoriaproduto” sempre que um novo produto for cadastrado. */
CREATE TRIGGER produto_novo
AFTER INSERT ON produtos
FOR EACH ROW
BEGIN
	INSERT INTO auditoriaproduto (data_hora, acao_realizada,  usuario, nome_produto, id_produto) VALUES 
    (NOW(), 'inclusão',NEW.user_inclusao, NEW.nome, NEW.id);
END;

/* 4. Trigger na tabela de produtos que inclua o registro na tabela “auditoriaproduto”
 sempre que um produto for modificado. */
CREATE TRIGGER produto_modificacao
AFTER UPDATE ON produtos
FOR EACH ROW
BEGIN    
	INSERT INTO auditoriaproduto (data_hora, acao_realizada, usuario, nome_produto, id_produto) VALUES 
    (NOW(), 'alteração', NEW.user_modificacao, NEW.nome , NEW.id);
END;

/* Inserindo dummy data para exemplificar o funcionamento */
INSERT INTO produtos (nome, estoque, estoqueminimo, user_inclusao)
VALUES
  ('Fender Stratocaster', 15, 5, 'UserIN_0'),
  ('BOSS DS-2', 34, 7, 'UserIN_1'),
  ('Dunlop Cry Baby', 56, 9, 'UserIN_3'),
  ('Mxr Micro Amp', 75, 15, 'UserIN_4');
    
UPDATE produtos SET user_modificacao = 'UserModFinal', estoque = 4 WHERE id = 2;
/* Supondo que houvesse modificações anteriores, UserModFinal seria o único achado na 
busca, pois foi o último a modificar o produto */

/* 5. Consulta que nos retorne as seguintes informações: id, nome, data/hora e usuário
 que fez a inclusão do produto, data/hora e usuário que realizou a última modificação do produto. */
SELECT 
    p.id AS id_produto,
    p.nome AS nome_produto,
    a1.data_hora AS inclusao_data_hora,
    a1.usuario AS inclusao_usuario,
    COALESCE(a2.data_hora, 'Nunca modificado') AS alteracao_data_hora,
    COALESCE(a2.usuario, 'Nunca modificado') AS alteracao_usuario
FROM produtos p
LEFT JOIN auditoriaproduto a1 ON p.id = a1.id_produto AND a1.acao_realizada = 'inclusão'
LEFT JOIN (
    SELECT id_produto, MAX(data_hora) AS max_data_hora
    FROM auditoriaproduto
    WHERE acao_realizada = 'alteração'
    GROUP BY id_produto
) a2_max ON p.id = a2_max.id_produto
LEFT JOIN auditoriaproduto a2 ON a2_max.id_produto = a2.id_produto AND a2_max.max_data_hora = a2.data_hora;
/* WHERE p.id = 2; */
/* linha adicional caso seja necessário buscar um produto específico pelo seu id. */
