
INSERT INTO sede (nome_sede) VALUES ('Matriz')
ON CONFLICT (id_sede) DO NOTHING;

INSERT INTO estoque (quantidade_minima) VALUES (5)
ON CONFLICT (id_estoque) DO NOTHING;

INSERT INTO filial (nome_filial, id_sede, id_estoque)
VALUES ('Loja Centro', (SELECT id_sede FROM sede WHERE nome_sede = 'Matriz'), 
        (SELECT id_estoque FROM estoque WHERE quantidade_minima = 5))
ON CONFLICT (id_filial) DO NOTHING;

INSERT INTO modelo (cor, numero, categoria_modelo) VALUES
('Preto', 38, 'Tênis'),
('Preto', 39, 'Tênis'),
('Preto', 40, 'Tênis'),
('Preto', 41, 'Tênis'),
('Preto', 42, 'Tênis'),
('Branco', 38, 'Tênis'),
('Branco', 39, 'Tênis'),
('Branco', 40, 'Tênis'),
('Branco', 41, 'Tênis'),
('Branco', 42, 'Tênis'),
('Couro Marrom', 38, 'Social'),
('Couro Marrom', 39, 'Social'),
('Couro Marrom', 40, 'Social'),
('Couro Marrom', 41, 'Social'),
('Couro Marrom', 42, 'Social'),
('Couro Preto', 38, 'Social'),
('Couro Preto', 39, 'Social'),
('Couro Preto', 40, 'Social'),
('Couro Preto', 41, 'Social'),
('Couro Preto', 42, 'Social'),
('Azul', 38, 'Sapatênis'),
('Azul', 39, 'Sapatênis'),
('Azul', 40, 'Sapatênis'),
('Azul', 41, 'Sapatênis'),
('Azul', 42, 'Sapatênis'),
('Bege', 38, 'Sapatênis'),
('Bege', 39, 'Sapatênis'),
('Bege', 40, 'Sapatênis'),
('Bege', 41, 'Sapatênis'),
('Bege', 42, 'Sapatênis'),
('Camurça Marrom', 38, 'Bota'),
('Camurça Marrom', 39, 'Bota'),
('Camurça Marrom', 40, 'Bota'),
('Camurça Marrom', 41, 'Bota'),
('Camurça Marrom', 42, 'Bota');


INSERT INTO produto (marca, categoria, publico_alvo, preco, id_modelo) VALUES
('Nike', 'Tênis', 'Masculino', 189.90, (SELECT codigo FROM modelo WHERE cor = 'Preto' AND numero = 40 AND categoria_modelo = 'Tênis' LIMIT 1)),
('Nike', 'Tênis', 'Masculino', 189.90, (SELECT codigo FROM modelo WHERE cor = 'Preto' AND numero = 41 AND categoria_modelo = 'Tênis' LIMIT 1)),
('Nike', 'Tênis', 'Feminino', 179.90, (SELECT codigo FROM modelo WHERE cor = 'Branco' AND numero = 38 AND categoria_modelo = 'Tênis' LIMIT 1)),
('Nike', 'Tênis', 'Feminino', 179.90, (SELECT codigo FROM modelo WHERE cor = 'Branco' AND numero = 39 AND categoria_modelo = 'Tênis' LIMIT 1));

INSERT INTO produto (marca, categoria, publico_alvo, preco, id_modelo) VALUES
('Calçados Brasil', 'Social', 'Masculino', 259.90, (SELECT codigo FROM modelo WHERE cor = 'Couro Marrom' AND numero = 40 AND categoria_modelo = 'Social' LIMIT 1)),
('Calçados Brasil', 'Social', 'Masculino', 259.90, (SELECT codigo FROM modelo WHERE cor = 'Couro Marrom' AND numero = 41 AND categoria_modelo = 'Social' LIMIT 1)),
('Calçados Brasil', 'Social', 'Feminino', 239.90, (SELECT codigo FROM modelo WHERE cor = 'Couro Preto' AND numero = 38 AND categoria_modelo = 'Social' LIMIT 1)),
('Calçados Brasil', 'Social', 'Feminino', 239.90, (SELECT codigo FROM modelo WHERE cor = 'Couro Preto' AND numero = 39 AND categoria_modelo = 'Social' LIMIT 1));

INSERT INTO produto (marca, categoria, publico_alvo, preco, id_modelo) VALUES
('Comfort', 'Sapatênis', 'Masculino', 159.90, (SELECT codigo FROM modelo WHERE cor = 'Azul' AND numero = 40 AND categoria_modelo = 'Sapatênis' LIMIT 1)),
('Comfort', 'Sapatênis', 'Masculino', 159.90, (SELECT codigo FROM modelo WHERE cor = 'Azul' AND numero = 41 AND categoria_modelo = 'Sapatênis' LIMIT 1)),
('Comfort', 'Sapatênis', 'Feminino', 149.90, (SELECT codigo FROM modelo WHERE cor = 'Bege' AND numero = 38 AND categoria_modelo = 'Sapatênis' LIMIT 1)),
('Comfort', 'Sapatênis', 'Feminino', 149.90, (SELECT codigo FROM modelo WHERE cor = 'Bege' AND numero = 39 AND categoria_modelo = 'Sapatênis' LIMIT 1));

INSERT INTO produto (marca, categoria, publico_alvo, preco, id_modelo) VALUES
('Timber', 'Bota', 'Masculino', 299.90, (SELECT codigo FROM modelo WHERE cor = 'Camurça Marrom' AND numero = 40 AND categoria_modelo = 'Bota' LIMIT 1)),
('Timber', 'Bota', 'Masculino', 299.90, (SELECT codigo FROM modelo WHERE cor = 'Camurça Marrom' AND numero = 41 AND categoria_modelo = 'Bota' LIMIT 1)),
('Timber', 'Bota', 'Feminino', 279.90, (SELECT codigo FROM modelo WHERE cor = 'Camurça Marrom' AND numero = 38 AND categoria_modelo = 'Bota' LIMIT 1));

INSERT INTO armazenamento (id_estoque, codigo, quantidade)
SELECT 
    (SELECT id_estoque FROM filial WHERE nome_filial = 'Loja Centro'),
    p.codigo,
    CASE 
        WHEN p.marca = 'Nike' THEN 15
        WHEN p.marca = 'Calçados Brasil' THEN 10
        WHEN p.marca = 'Comfort' THEN 12
        WHEN p.marca = 'Timber' THEN 8
    END
FROM produto p
ON CONFLICT (id_estoque, codigo) DO NOTHING;

--opcional para testes
INSERT INTO cliente (cpf, nome, data_nascimento, telefone, email, rua, numero, cep, pontos) VALUES
('12345678901', 'João Silva', '1990-05-15', '11999999999', 'joao@email.com', 'Rua A', '100', '12345-678', 0),
('98765432100', 'Maria Oliveira', '1985-08-20', '11888888888', 'maria@email.com', 'Rua B', '200', '87654-321', 0)
ON CONFLICT (cpf) DO NOTHING;
