-- PROJETO BANCO DE DADOS I - LOJA DE SAPATOS
-- Fase 2
DROP TABLE IF EXISTS oferta CASCADE;
DROP TABLE IF EXISTS promocao CASCADE;
DROP TABLE IF EXISTS armazenamento CASCADE;
DROP TABLE IF EXISTS transferencia CASCADE;
DROP TABLE IF EXISTS composicao CASCADE;
DROP TABLE IF EXISTS compra CASCADE;
DROP TABLE IF EXISTS venda CASCADE;
DROP TABLE IF EXISTS solicitacao CASCADE;
DROP TABLE IF EXISTS fornecedor CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS modelo CASCADE;
DROP TABLE IF EXISTS funcionario CASCADE;
DROP TABLE IF EXISTS estoque CASCADE;
DROP TABLE IF EXISTS filial CASCADE;
DROP TABLE IF EXISTS sede CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;

-- SEDE
CREATE TABLE sede (
    id_sede     SERIAL PRIMARY KEY,
    nome_sede   VARCHAR(100) NOT NULL
);

-- ESTOQUE
-- (relacionamento "Controle" 1:1 obrigatório com Filial)
CREATE TABLE estoque (
    id_estoque          SERIAL PRIMARY KEY,
    quantidade_minima   INTEGER NOT NULL DEFAULT 0 CHECK (quantidade_minima >= 0)
);

-- FILIAL
CREATE TABLE filial (
    id_filial    SERIAL PRIMARY KEY,
    nome_filial  VARCHAR(100) NOT NULL,         
    id_sede      INTEGER NOT NULL REFERENCES sede(id_sede),
    id_estoque   INTEGER NOT NULL UNIQUE REFERENCES estoque(id_estoque)
);

-- FUNCIONARIO
CREATE TABLE funcionario (
    codigo          SERIAL PRIMARY KEY,
    cpf             VARCHAR(11) NOT NULL UNIQUE,
    funcao          VARCHAR(20) NOT NULL
                    CHECK (funcao IN ('Vendedor','Caixa','Estoquista','Gerente')),
    nome            VARCHAR(100) NOT NULL,
    data_admissao   DATE NOT NULL,
    id_filial       INTEGER NOT NULL REFERENCES filial(id_filial)
);

-- CLIENTE
CREATE TABLE cliente (
    cpf              VARCHAR(11) PRIMARY KEY,
    nome             VARCHAR(100) NOT NULL,
    data_nascimento  DATE,
    telefone         VARCHAR(20),
    email            VARCHAR(100),
    rua              VARCHAR(100),
    numero           VARCHAR(10),
    cep              VARCHAR(9),      
    pontos           INTEGER NOT NULL DEFAULT 0 CHECK (pontos >= 0)
);

-- MODELO - variações de número e cor de um produto
CREATE TABLE modelo (
    codigo            SERIAL PRIMARY KEY,
    cor               VARCHAR(30) NOT NULL,
    numero            INTEGER NOT NULL CHECK (numero BETWEEN 15 AND 50),
    categoria_modelo  VARCHAR(30)
);

-- PRODUTO
CREATE TABLE produto (
    codigo        SERIAL PRIMARY KEY,
    marca         VARCHAR(50) NOT NULL,
    categoria     VARCHAR(30) NOT NULL
                  CHECK (categoria IN ('Tênis','Sandália','Bota','Social','Sapatênis','Chinelo')),
    publico_alvo  VARCHAR(20) NOT NULL
                  CHECK (publico_alvo IN ('Masculino','Feminino','Infantil')),
    preco         NUMERIC(10,2) NOT NULL CHECK (preco >= 0),
    id_modelo     INTEGER NOT NULL REFERENCES modelo(codigo)
);

-- FORNECEDOR
CREATE TABLE fornecedor (
    razao_social  VARCHAR(150) PRIMARY KEY,
    cnpj          VARCHAR(14) NOT NULL UNIQUE,
    contato       VARCHAR(100)
);

-- SOLICITACAO - pedido de compra à sede junto ao fornecedor
CREATE TABLE solicitacao (
    id_solicitacao  SERIAL PRIMARY KEY,
    status          VARCHAR(25) NOT NULL DEFAULT 'Pendente'
                    CHECK (status IN ('Pendente','Entregue Parcialmente','Entregue','Cancelado')),
    quantidade      INTEGER NOT NULL CHECK (quantidade > 0),  
    custo           NUMERIC(10,2) NOT NULL CHECK (custo >= 0),
    data_entrega    DATE,
    razao_social    VARCHAR(150) NOT NULL REFERENCES fornecedor(razao_social),
    codigo          INTEGER NOT NULL REFERENCES produto(codigo),
    id_sede         INTEGER NOT NULL REFERENCES sede(id_sede)
);

-- VENDA
CREATE TABLE venda (
    id_venda         SERIAL PRIMARY KEY,
    data_venda       TIMESTAMP NOT NULL DEFAULT now(),      
    forma_pagamento  VARCHAR(20) NOT NULL
                     CHECK (forma_pagamento IN ('Dinheiro','PIX','Cartão de Débito','Cartão de Crédito')),
    num_parcelas     INTEGER CHECK (num_parcelas BETWEEN 1 AND 10),  
    id_filial        INTEGER NOT NULL REFERENCES filial(id_filial),
    id_funcionario   INTEGER NOT NULL REFERENCES funcionario(codigo)
);

-- COMPRA - associação opcional Cliente -> Venda, cardinalidade 0,1
CREATE TABLE compra (
    id_venda  INTEGER PRIMARY KEY REFERENCES venda(id_venda),
    cpf       VARCHAR(11) NOT NULL REFERENCES cliente(cpf)
);

-- COMPOSICAO - itens de uma venda
CREATE TABLE composicao (
    id_venda        INTEGER NOT NULL REFERENCES venda(id_venda),
    codigo          INTEGER NOT NULL REFERENCES produto(codigo),
    quantidade      INTEGER NOT NULL CHECK (quantidade > 0),    
    preco_unitario  NUMERIC(10,2) NOT NULL CHECK (preco_unitario >= 0), 
    PRIMARY KEY (id_venda, codigo)
);


-- TRANSFERENCIA - movimentação de produto para uma filial)
CREATE TABLE transferencia (
    id_filial           INTEGER NOT NULL REFERENCES filial(id_filial),
    codigo              INTEGER NOT NULL REFERENCES produto(codigo),
    data_transferencia  DATE NOT NULL DEFAULT CURRENT_DATE,
    PRIMARY KEY (id_filial, codigo, data_transferencia)
);

-- ARMAZENAMENTO - estoque de cada produto em cada filial
CREATE TABLE armazenamento (
    id_estoque  INTEGER NOT NULL REFERENCES estoque(id_estoque),
    codigo      INTEGER NOT NULL REFERENCES produto(codigo),
    quantidade  INTEGER NOT NULL DEFAULT 0 CHECK (quantidade >= 0),
    PRIMARY KEY (id_estoque, codigo)
);

-- PROMOCAO
CREATE TABLE promocao (
    id_promocao  SERIAL PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    data_inicio  DATE NOT NULL,           
    data_fim     DATE NOT NULL,         
    categorias   VARCHAR(200),
    CHECK (data_fim >= data_inicio)
);

-- OFERTA - produtos contemplados por uma promoção
CREATE TABLE oferta (
    id_promocao  INTEGER NOT NULL REFERENCES promocao(id_promocao),
    codigo       INTEGER NOT NULL REFERENCES produto(codigo),
    desconto     NUMERIC(5,2) CHECK (desconto BETWEEN 0 AND 100), 
    PRIMARY KEY (id_promocao, codigo)
);

-- ÍNDICES ADICIONAIS - uteis para desempenho de consultas frequentes
CREATE INDEX idx_venda_filial      ON venda(id_filial);
CREATE INDEX idx_venda_funcionario ON venda(id_funcionario);
CREATE INDEX idx_produto_modelo    ON produto(id_modelo);
CREATE INDEX idx_solicitacao_forn  ON solicitacao(razao_social);
CREATE INDEX idx_funcionario_fil   ON funcionario(id_filial);
