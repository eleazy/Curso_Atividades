/*Criação das tabelas*/
CREATE TABLE clientes (
  id INT PRIMARY KEY AUTO_INCREMENT,/*valor id é incrementado automaticamente*/
  nome VARCHAR(100),
  saldodevedor DECIMAL(10, 2)/*valor decimal com duas casas para representar dinheiro*/
);

CREATE TABLE contasreceber (
  id INT PRIMARY KEY AUTO_INCREMENT,
  idcliente INT,
  valor DECIMAL(10, 2),
  status CHAR(1),
  FOREIGN KEY (idcliente) REFERENCES clientes(id),
 CHECK (UPPER(status) IN ('S', 'N'))/*Valores permitidos para status são 'n' ou 's', case insensitive*/
);

DELIMITER //
/*Criação dos triggers*/
CREATE TRIGGER insert_contasreceber
AFTER INSERT ON contasreceber
FOR EACH ROW
BEGIN
    IF NEW.status = 'N' THEN
        UPDATE clientes
        SET saldodevedor = saldodevedor + NEW.valor
        WHERE id = NEW.idcliente;
    END IF;
END;

CREATE TRIGGER update_saldodevedor
AFTER UPDATE ON contasreceber
FOR EACH ROW
BEGIN
    IF NEW.status = 'N' AND OLD.status = 'S' THEN 
        UPDATE clientes
        SET saldodevedor = saldodevedor + NEW.valor
        WHERE id = NEW.idcliente;
    ELSEIF NEW.status = 'S' AND OLD.status = 'N' THEN
        UPDATE clientes
        SET saldodevedor = saldodevedor - NEW.valor
        WHERE id = NEW.idcliente;
    END IF;
END;

/* Inserindo dummy data para exemplificar o funcionamento */
INSERT INTO clientes (nome, saldodevedor) VALUES
  ('Fulano', 0.00),
  ('Beltrano', 0.00),
  ('Sicrano', 0.00);

INSERT INTO contasreceber (idcliente, valor, status) VALUES
  (1, 10.00, 'S'),
  (2, 20.00, 'N'),
  (3, 30.00, 'S');
  /* Neste ponto de execução o saldo devedor dos clientes será o seguinte:
  Fulano -> 0.00 -> saldo inicial de 0.00 mais conta de 10.00 paga.
  Beltrano -> 20.00 -> saldo inicial de 0.00 mais conta de 20.00 não paga.
  Sicrano -> 0.00 -> saldo inicial de 0.00 mais conta de 30.00 paga. */
   
  /* Atualizando as contas a receber existentes*/
UPDATE contasreceber
SET status = 'N' WHERE id = 1;
/* Supondo que houve um erro de pagamento, e a conta de id = 1 não foi realmente paga.
 Neste ponto o cliente Fulano possui saldo devedor de 10.00, saldo anterior de 0.00 mais conta não paga de 10.00 */
 
UPDATE contasreceber
SET status = 'S' WHERE id = 2;
/* Neste ponto a conta de id = 2 foi paga e o saldo devedor de Beltrano é 0.00*/

UPDATE contasreceber
SET status = 'N' WHERE id = 3;
/* Supondo outro erro de pagamento, neste ponto o cliente Sicrano possui saldo devedor de 30.00, saldo anterior de 0.00 mais conta não paga de 30.00 */
 
/* Consulta filtrando pelos clientes que possuem saldo devedor maior que 0.00 
 Os cliente encontrados serão Fulano e Sicrano */
SELECT * FROM clientes WHERE saldodevedor > 0.00;



