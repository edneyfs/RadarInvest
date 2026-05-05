*** Settings ***
Library     SeleniumLibrary
Library     FakerLibrary    locale=pt_BR

*** Variables ***
${BASE_URL}     http://localhost:3000/radarinvest
${BROWSER}      chrome
${TIMEOUT}      10s

*** Keywords ***
Abrir Navegador
    Open Browser    ${BASE_URL}    ${BROWSER}
    Maximize Browser Window
    Set Selenium Implicit Wait    ${TIMEOUT}

Fechar Navegador
    Capture Page Screenshot
    Close Browser
