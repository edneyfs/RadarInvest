# Testes Automatizados - RadarInvest 🤖

Este diretório contém a estrutura de testes automatizados do projeto, divididos por plataforma.

## 📁 Estrutura

- **web/**: Testes de interface para a aplicação Web (React). Utiliza `SeleniumLibrary`.
- **mobile/**: (Futuro) Testes para Android/iOS. Utilizará `AppiumLibrary`.

---

## 🚀 Como Executar (Testes Web)

### 1. Pré-requisitos
Certifique-se de ter o **Python** instalado (versão 3.8 ou superior).
Verifique com:
```cmd
python --version
```

### 2. Instalação das Dependências
Abra o terminal na pasta raiz do projeto (`radar-invest`) e execute:

```cmd
pip install robotframework robotframework-seleniumlibrary
```

### 3. Rodando os Testes
Para rodar todos os testes WEB e salvar os resultados na pasta `logs`:

**Opção A (Executando da raiz do projeto):**
```cmd
robot -d logs tests/web
```

**Opção B (Executando de dentro da pasta tests):**
```cmd
cd tests
robot -d ../logs web
```

### 📊 Verificando Resultados
Após a execução, abra o arquivo `logs/report.html` no seu navegador para ver o relatório detalhado.
