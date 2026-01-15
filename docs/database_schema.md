# Database Schema - RadarInvest V1.0

## Estratégia
- **Banco**: PostgreSQL 15+
- **Migrations**: Flyway (integrado ao Spring Boot)
- **Schema**: `public`

## ERD (Entidades)

### `item_watchlist`
Tabela principal para armazenar os ativos monitorados pelo usuário (versão monousuário inicialmente, ou global).
- `id` (UUID, PK)
- `ticker` (VARCHAR 10) - Indexado, Unique
- `tipo_ativo` (VARCHAR 20) - 'B3_FII', 'B3_ACAO'
- `criado_em` (TIMESTAMP)

### `evento_provento`
Cache de proventos para não consultar API externa toda hora.
- `id` (UUID, PK)
- `ticker` (VARCHAR 10) - FK (Lógica) para item_watchlist.ticker
- `tipo_evento` (VARCHAR 20) - 'DIVIDENDO', 'JCP', 'RENDIMENTO'
- `data_com` (DATE)
- `data_ex` (DATE)
- `data_pagamento` (DATE)
- `valor` (DECIMAL 18,8)
- `coletado_em` (TIMESTAMP)

### `evento_noticia`
Notícias coletadas e processadas pela IA.
- `id` (UUID, PK)
- `ticker` (VARCHAR 10)
- `titulo` (TEXT)
- `resumo` (TEXT)
- `url` (TEXT)
- `publicado_em` (TIMESTAMP)
- `coletado_em` (TIMESTAMP)
- `severidade` (VARCHAR 10) - 'BAIXO', 'MEDIO', 'ALTO'
- `hash_deduplicacao` (VARCHAR 64) - Unique para evitar duplicatas

## Scripts SQL Iniciais (Draft)

```sql
CREATE TABLE item_watchlist (
    id UUID PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL UNIQUE,
    tipo_ativo VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evento_provento (
    id UUID PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL,
    tipo_evento VARCHAR(20),
    data_com DATE,
    data_ex DATE,
    data_pagamento DATE,
    valor DECIMAL(18,8),
    coletado_em TIMESTAMP,
    CONSTRAINT fk_provento_ticker FOREIGN KEY (ticker) REFERENCES item_watchlist(ticker) -- Simplificação V1
);
-- Index para busca rápida
CREATE INDEX idx_provento_ticker ON evento_provento(ticker);
```
