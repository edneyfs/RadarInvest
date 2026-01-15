# RadarInvest - Arquitetura (V1.0)

## Visão Geral (C4 Context)

```mermaid
C4Context
    title Diagrama de Contexto - RadarInvest

    Person(user, "Investidor", "Usuário que monitora seus ativos.")
    System(radar, "RadarInvest System", "Monitora proventos, calcula datas e agrega notícias.")
    
    System_Ext(b3, "B3 / Proventos API", "Fonte de dados de proventos e datas com/ex.")
    System_Ext(news, "Fontes de Notícias", "Portais financeiros, CVM e RI.")

    Rel(user, radar, "Visualiza dashboard e recebe alertas")
    Rel(radar, b3, "Coleta dados de proventos")
    Rel(radar, news, "Coleta fatos relevantes")
```

## Containers (C4 Container)

```mermaid
C4Container
    title Diagrama de Container - RadarInvest

    Person(user, "Investidor", "Usa o navegador")

    Container_Boundary(c1, "RadarInvest") {
        Container(web, "Frontend Web", "React, Vite", "SPA para visualização de ativos e notícias.")
        Container(api, "Backend API", "Java 21, Spring Boot", "Gerencia Watchlist, Jobs de Coleta e Regras de Negócio.")
        ContainerDb(db, "Database", "PostgreSQL", "Armazena usuários, watchlist e histórico de eventos.")
    }

    Rel(user, web, "Acessa via HTTPS")
    Rel(web, api, "Chamadas REST (JSON)", "HTTPS")
    Rel(api, db, "Leitura/Escrita", "JDBC")
```

## Fluxo de Agentes Internos
O Backend é subdividido em responsabilidades de "Agentes" lógicos:
1. **AgendadorColeta**: Acorda diariamente.
2. **ColetaProventosService**: Itera sobre a Watchlist -> Chama `ProventosProvider`.
3. **MonitorNoticiasService**: Itera sobre a Watchlist -> Chama `NoticiasProvider` -> Deduplica Hash -> Salva DB.
