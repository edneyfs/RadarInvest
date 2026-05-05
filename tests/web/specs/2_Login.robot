*** Settings ***
Resource    ../resources/base.robot
Resource    ../resources/funcionalidades.robot

Test Setup       Abrir Navegador
Test Teardown    Fechar Navegador

*** Test Cases ***
Verificar Persistência dos Ativos
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


Login Inválido
    [Documentation]    Testa a tentativa de login com credenciais erradas
    [Tags]    exception
    Go To          ${BASE_URL}/login
    Input Text     ${INPUT_EMAIL}       errado@teste.com
    Input Text     ${INPUT_SENHA}       senhaerrada
    Click Element  ${BTN_ENTRAR}
    Wait Until Page Contains    Falha no login    10s
