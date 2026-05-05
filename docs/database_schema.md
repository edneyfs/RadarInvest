# Database Schema - RadarInvest

## Estratégia
- **Banco**: PostgreSQL 15+
- **Migrations**: Flyway (integrado ao Spring Boot)
- **Schema**: `public`

## Diagrama ER (Entity-Relationship)

```mermaid
erDiagram
    USUARIO {
        UUID id PK
        String nome
        String email UK
        String cpf
        String senha
        String role
        LocalDateTime last_seen
    }

    ITEM_WATCHLIST {
        UUID id PK
        String ticker UK
        String tipo_ativo
        LocalDateTime criado_em
    }

    EVENTO_PROVENTO {
        UUID id PK
        String ticker FK
        String tipo_evento
        Date data_com
        Date data_pagamento
        Decimal valor
    }

    EVENTO_NOTICIA {
        UUID id PK
        String ticker FK
        String titulo
        String url
        String severidade
        LocalDateTime publicado_em
    }

    %% Relacionamentos
    ITEM_WATCHLIST ||--o{ EVENTO_PROVENTO : "possui"
    ITEM_WATCHLIST ||--o{ EVENTO_NOTICIA : "possui"
    USUARIO }o--|{ ITEM_WATCHLIST : "monitora (futuro)"
```

## Detalhes das Tabelas

### `usuario`
Armazena os dados de autenticação e perfil dos usuários.
- `id`: Identificador único (UUID).
- `email`: Chave natural para login.
- `role`: Perfil de acesso (`USER`, `ADMIN`).

### `item_watchlist`
Ativos que estão sendo monitorados pelo sistema. Atualmente global (V1), mas preparado para ser vinculado a usuários na V2.
- `ticker`: Código do ativo na bolsa (ex: PETR4).

### `evento_provento` & `evento_noticia`
Tabelas de cache/histórico para eventos relacionados aos ativos.
- O campo `ticker` atua como chave estrangeira lógica para `item_watchlist`.

