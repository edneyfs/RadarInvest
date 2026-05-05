*** Settings ***
Resource    base.robot
Resource    elementos.robot

*** Keywords ***
Acessar Aplicação
    [Arguments]    ${EMAIL}    ${SENHA}
    Go To          ${BASE_URL}/login
    Input Text     ${INPUT_EMAIL}       ${EMAIL}
    Input Text     ${INPUT_SENHA}       ${SENHA}
    Click Element  ${BTN_ENTRAR}

Acessar Área Administrativa
    [Arguments]    ${USER}    ${SENHA}
    Go To          ${BASE_URL}/admin/login
    Input Text     ${INPUT_ADMIN_USER}    ${USER}
    Input Text     ${INPUT_ADMIN_PASS}    ${SENHA}
    Click Element  ${BTN_ADMIN_ENTRAR}

Registrar Novo Usuário
    [Arguments]    ${NOME}    ${EMAIL}    ${CPF}    ${SENHA}
    Go To          ${BASE_URL}/register
    Input Text     ${INPUT_REG_NOME}        ${NOME}
    Input Text     ${INPUT_REG_EMAIL}       ${EMAIL}
    Input Text     ${INPUT_REG_CPF}         ${CPF}
    Input Text     ${INPUT_REG_SENHA}       ${SENHA}
    Input Text     ${INPUT_REG_CONF_SENHA}  ${SENHA}
    Click Element  ${BTN_CADASTRAR}

Verificar Usuário Logado
    [Arguments]    ${NOME_ESPERADO}
    Wait Until Page Contains    Meus Ativos    10s
    Wait Until Page Contains    Olá, ${NOME_ESPERADO}    10s
    # Alternativa se tivermos um ID fixo para a saudação
    # Element Should Contain    ${LBL_SAUDACAO}    ${NOME_ESPERADO}

Adicionar Ativo ao Radar
    [Arguments]    ${TICKER}    ${TIPO_LABEL}
    Input Text     id=novo-ativo-input    ${TICKER}
    Select From List By Label    id=tipo-ativo-select    ${TIPO_LABEL}
    Click Element  id=adicionar-ativo-btn
    Wait Until Page Contains Element    xpath=//h3[contains(text(), '${TICKER}')] | //div[contains(text(), '${TICKER}')]    10s
    
Verificar Ativo no Radar
    [Arguments]    ${TICKER}
    Wait Until Page Contains Element    xpath=//h3[contains(text(), '${TICKER}')] | //div[contains(text(), '${TICKER}')]    10s
