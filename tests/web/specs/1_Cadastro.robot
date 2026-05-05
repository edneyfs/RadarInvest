*** Settings ***
Resource    ../resources/base.robot
Resource    ../resources/funcionalidades.robot

Test Setup       Abrir Navegador
Test Teardown    Fechar Navegador

*** Test Cases ***
Cadastro com Nome Curto
    [Documentation]    Tenta cadastrar com nome menor que 5 caracteres
    ${CPF_VALIDO}    FakerLibrary.cpf
    Registrar Novo Usuário    Ana    ana@teste.com    ${CPF_VALIDO}    Senha@123
    Wait Until Page Contains    Nome deve ter no mínimo 5 caracteres    5s

Cadastro com CPF Inválido
    [Documentation]    Tenta cadastrar com CPF inválido
    Registrar Novo Usuário    Fulano Teste    cpf@teste.com    12345678900    Senha@123
    Wait Until Page Contains    Erro ao cadastrar    5s

Cadastro com Email Inválido
    [Documentation]    Tenta cadastrar com email sem formato válido
    ${CPF_VALIDO}    FakerLibrary.cpf
    Registrar Novo Usuário    Fulano Teste    email_invalido    ${CPF_VALIDO}    Senha@123
    # O navegador geralmente impede o submit via HTML5 validation, mas se passar, o backend rejeita
    # Aqui verificamos se não saiu da página ou se deu erro
    Page Should Contain Button    xpath=//button[@type='submit']

Cadastro com Senhas Divergentes
    [Documentation]    Tenta cadastrar com senha e confirmação diferentes
    Go To          ${BASE_URL}/register
    Input Text     ${INPUT_REG_NOME}        Usuario Teste
    Input Text     ${INPUT_REG_EMAIL}       senhas@teste.com
    Input Text     ${INPUT_REG_CPF}         123.456.789-00
    Input Text     ${INPUT_REG_SENHA}       Senha@123
    Input Text     ${INPUT_REG_CONF_SENHA}  OutraSenha
    Click Element  ${BTN_CADASTRAR}
    Wait Until Page Contains    As senhas não coincidem    5s

Registrar Novo Usuário
    [Documentation]    Cria o usuário fulano@gmail.com para ser usado nos testes subsequentes
    ${CPF_VALIDO}    FakerLibrary.cpf
    Registrar Novo Usuário    Fulano da Silva    fulano@gmail.com    ${CPF_VALIDO}    Senha@123
    Wait Until Page Contains    Cadastro realizado com sucesso    15s

Adicionar Ativos na Carteira
    [Documentation]    Loga com o usuário criado e adiciona os ativos solicitados
    #login valido
    Acessar Aplicação    fulano@gmail.com    Senha@123
    Verificar Usuário Logado    Fulano da Silva
    
    # FIIs
    Adicionar Ativo ao Radar    XPML11    Fundo Imob (FII)
    Adicionar Ativo ao Radar    BTLG11    Fundo Imob (FII)
    Adicionar Ativo ao Radar    XPLG11    Fundo Imob (FII)

    # Ações
    Adicionar Ativo ao Radar    BBAS3     Ação B3
    Adicionar Ativo ao Radar    VALE3     Ação B3
    Adicionar Ativo ao Radar    ITSA4     Ação B3
    Adicionar Ativo ao Radar    PETR4     Ação B3

    # BDRs
    Adicionar Ativo ao Radar    CHVX34    BDR
    Adicionar Ativo ao Radar    COCA34    BDR
    Adicionar Ativo ao Radar    JPMC34    BDR

    Capture Page Screenshot

Verificar Persistencia dos Ativos
    [Documentation]    Verifica se os ativos adicionados anteriormente persistem após logout/login
    Acessar Aplicação    fulano@gmail.com    Senha@123
    Verificar Usuário Logado    Fulano da Silva
    
    # FIIs
    Verificar Ativo no Radar    XPML11
    Verificar Ativo no Radar    BTLG11
    Verificar Ativo no Radar    XPLG11

    # Ações
    Verificar Ativo no Radar    BBAS3
    Verificar Ativo no Radar    VALE3
    Verificar Ativo no Radar    ITSA4
    Verificar Ativo no Radar    PETR4

    # BDRs
    Verificar Ativo no Radar    CHVX34
    Verificar Ativo no Radar    COCA34
    Verificar Ativo no Radar    JPMC34