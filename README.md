# RadarInvest V1.0 - Seus ativos, sempre no radar.

Sistema de monitoramento de ativos (FIIs e Ações) com foco em datas de corte (Data Com/Ex) e notícias relevantes agregadas por IA.

## 🚀 Como Rodar o Projeto

Pré-requisitos: Docker, Java 21, Node.js 18+.

### 1. Infraestrutura (Banco de Dados)
Suba o banco de dados PostgreSQL:
```bash
cd infra
docker-compose up -d
```

### 2. Backend (API)
Em um novo terminal:
```bash
cd radarinvest-api
# Linux/Mac
./mvnw spring-boot:run
# Windows
mvnw spring-boot:run

OU
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.8" && mvn spring-boot:run
```
*A API rodará em http://localhost:8080*

### 3. Frontend (Web)
Em outro terminal:
```bash
cd radarinvest-web
npm install
npm run dev
OU apenas:
npm run dev
```
*Acesse em http://localhost:3000*

## 🌟 Funcionalidades V1.0
- **Watchlist**: Adicione tickers (ex: HGLG11, ITSA4).
- **Melhor Dia de Compra**: O sistema calcula o último dia util para comprar e receber o provento.
- **Notícias Agregadas**: Painel lateral com notícias simuladas (Mock para V1) classificadas por severidade.
- **Modo Premium**: Interface Dark Mode polida.

## 🏗 Arquitetura
Consulte a pasta `docs/` para detalhes:
- [Arquitetura & Diagramas](docs/architecture_c4.md)
- [Design UX](docs/ux_design.md)
- [Schema de Banco](docs/database_schema.md)
- [Roadmap/Checklist V1.1+](docs/checklist_evolution.md)

## 🤖 Agentes Envolvidos
Este projeto foi coordenado e executado por 6 agentes especializados:
1. **UX Agent**: Design da interface.
2. **DBA Agent**: Schema PostgreSQL.
3. **Backend Agent**: Spring Boot API.
4. **Frontend Agent**: React Web.
5. **Mobile Agent**: Planejamento V2.
6. **Doc Agent**: Documentação.
