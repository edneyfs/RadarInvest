# Padrões de Projeto e Documentação

Este documento define as diretrizes técnicas, padrões de nomenclatura e boas práticas adotadas no desenvolvimento dos sistemas.

## 1. Backend (Java / Spring Boot)

### 1.1. Arquitetura e Camadas
O backend segue uma arquitetura em camadas padrão do Spring Boot:
- **API (`api/controller`)**: Exposição de endpoints REST. Deve conter apenas lógica de recebimento e resposta HTTP.
- **DTOs (`api/dto`)**: Objetos de transferência de dados. Use `record` do Java 21 sempre que possível.
- **Service (`application/service`)**: Regras de negócio, validações lógicas e orquestração.
- **Repository (`domain/repository`)**: Interfaces JPA para acesso a dados.
- **Entity (`domain/entity`)**: Classes persistentes mapeadas com Hibernate/JPA.

### 1.2. Nomenclatura
- **Classes**: `PascalCase` (ex: `UsuarioController`, `SecurityFilter`).
- **Métodos e Variáveis**: `camelCase` (ex: `buscarPorEmail`, `totalUsuarios`).
- **Interfaces Repository**: `[Entidade]Repository` (ex: `UsuarioRepository`).
- **DTOs**: `[Entidade]DTO` ou `[Ação]DTO` (ex: `UsuarioDTO`, `LoginDTO`).

### 1.3. Documentação e Logs
- **Swagger (OpenAPI)**: Obrigatório para todos os Controllers. Use `@Tag`, `@Operation` e `@ApiResponse` para descrever os endpoints. defina classes, atributos e exemplos.
  ```java
  @Operation(summary = "Resumo", description = "Detalhes", responses = { ... })
  ```
- **Logging**:
  - Use bibliotecas de log padrão (SLF4J/Logback).
  - Use log levels apropriados (INFO, DEBUG, ERROR).
  - Logar os json dos requests e responses.
    - **Dados Sensíveis**: Utilize a anotação customizada `@LogSensitive` para mascarar CPF, Senha e Email nos logs.
  ```java
  @LogSensitive(strategy = SensitiveType.MASK_CPF)
  String cpf;
  ```

### 1.4. Validação
- Use Bean Validation (`jakarta.validation`) nos DTOs de entrada (`@NotBlank`, `@Email`, `@CPF`).
- Use `@Valid` nos parâmetros dos Controllers.

---

## 2. Frontend (React / TypeScript)

### 2.1. Estrutura de Diretórios
- **`components/`**: Componentes reutilizáveis (botões, cards, inputs).
- **`pages/`**: Componentes que representam rotas completas.
- **`services/`**: Comunicação com API (Axios).
- **`types/`**: Interfaces TypeScript compartilhadas.

### 2.2. Nomenclatura
- **Componentes**: `PascalCase` e mesmo nome do arquivo (ex: `TickerInput.tsx`).
- **Hooks**: Prefixo `use` (ex: `useAuth`).
- **Interfaces**: Nome descritivo, sem prefixo `I` (ex: `Usuario`, não `IUsuario`).

### 2.3. Estilização
- **Variáveis CSS**: Use variáveis globais definidas em `index.css` para cores e fontes (ex: `var(--color-primary)`).
- **IDs para Testes**: Elementos interativos (inputs, botões) **DEVEM** possuir um atributo `id` único para facilitar testes automatizados (ex: `id="btn-login"`).

---

## 3. Testes Automatizados (Robot Framework)

### 3.1. Estrutura
- **`specs/`**: Arquivos de teste (`.robot`). Nomeados com prefixo numérico para ordem de execução (ex: `1_Cadastro.robot`, `2_Login.robot`).
- **`resources/`**: Keywords reutilizáveis e page objects (`funcionalidades.robot`, `elementos.robot`).

### 3.2. Boas Práticas
- **Locators**: Preferência absoluta por IDs (`id=...`). Evite XPaths complexos ou baseados em texto instável.
- **Dados de Teste**:
  - Dados estáticos devem ser evitados para cadastros. Use `FakerLibrary` para gerar dados únicos (CPF, Email).
  - Senhas devem atender aos requisitos de complexidade do sistema.
- **Independência**: Sempre que possível, o teste deve criar sua própria massa de dados ou limpar o ambiente após o uso (Teardown).

---

## 4. Versionamento e Git

- **Commits**: Mensagens claras e objetivas.
- **Branchs**: `main` para produção/estável. Features devem ser desenvolvidas em branches separadas se o time crescer.

---

## 5. Manutenção de Documentação

- **Manual do Usuário**: O arquivo `docs/manual_usuario.html` é um documento vivo.
  - **Regra**: A cada nova tela criada ou fluxo alterado no Frontend, deve-se:
    1. Capturar novo screenshot da tela.
    2. Atualizar o HTML com a nova imagem e descrição dos campos.
- **Banco de Dados**: O arquivo `docs/database_schema.md` deve refletir a estrutura atual.
  - **Regra**: Ao adicionar tabelas ou colunas, atualizar a lista de entidades e o diagrama Mermaid.
- **Padrões de Projeto**: Manter `docs/padroes_projeto.md` atualizado conforme novas convenções forem adotadas.
