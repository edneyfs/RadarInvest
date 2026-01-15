CREATE TABLE item_watchlist (
    id UUID PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL UNIQUE,
    tipo_ativo VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evento_ativo (
    id UUID PRIMARY KEY,
    identificador_ativo VARCHAR(10) NOT NULL,
    tipo_ativo VARCHAR(20) NOT NULL,
    tipo_evento VARCHAR(20) NOT NULL,
    data_com DATE,
    data_ex DATE,
    data_pagamento DATE,
    valor DECIMAL(18,8),
    moeda VARCHAR(5),
    fonte VARCHAR(100),
    coletado_em TIMESTAMP,
    dados_extras_json TEXT, -- JSON structure
    CONSTRAINT fk_evento_ativo FOREIGN KEY (identificador_ativo) REFERENCES item_watchlist(ticker) ON DELETE CASCADE
);
CREATE INDEX idx_evento_identificador ON evento_ativo(identificador_ativo);

CREATE TABLE evento_noticia (
    id UUID PRIMARY KEY,
    identificador_ativo VARCHAR(10) NOT NULL,
    tipo_ativo VARCHAR(20) NOT NULL,
    titulo TEXT NOT NULL,
    resumo TEXT,
    url VARCHAR(500),
    publicado_em TIMESTAMP,
    categoria VARCHAR(50),
    severidade VARCHAR(10),
    fonte VARCHAR(100),
    coletado_em TIMESTAMP,
    hash_deduplicacao VARCHAR(64) UNIQUE,
    CONSTRAINT fk_noticia_ativo FOREIGN KEY (identificador_ativo) REFERENCES item_watchlist(ticker) ON DELETE CASCADE
);
CREATE INDEX idx_noticia_identificador ON evento_noticia(identificador_ativo);
