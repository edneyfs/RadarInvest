*** Settings ***
Resource    ../resources/base.robot
Resource    ../resources/funcionalidades.robot

Test Setup       Abrir Navegador
Test Teardown    Fechar Navegador

*** Test Cases ***
Acesso Admin - Login Correto
    [Documentation]    Testa o login na área administrativa com credenciais válidas
    Acessar Área Administrativa    admin    2904@Arthur
    Wait Until Page Contains    Painel Administrativo    10s
    Wait Until Page Contains    Total de Usuários        10s

Acesso Admin - Senha Incorreta
    [Documentation]    Testa a segurança da área administrativa com senha errada
    [Tags]    exception
    Go To          ${BASE_URL}/admin/login
    Input Text     ${INPUT_ADMIN_USER}    admin
    Input Text     ${INPUT_ADMIN_PASS}    senhaerrada
    Click Element  ${BTN_ADMIN_ENTRAR}
    Wait Until Page Contains    Falha no login    10s
