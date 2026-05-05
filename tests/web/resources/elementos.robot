*** Variables ***
# --- LOGIN USUÁRIO ---
${INPUT_EMAIL}          id=email
${INPUT_SENHA}          id=password
${BTN_ENTRAR}           xpath=//button[@type='submit']
${MSG_ERRO_LOGIN}       xpath=//*[contains(text(), 'Falha no login')]

# --- LOGIN ADMIN ---
${INPUT_ADMIN_USER}     id=admin-username
${INPUT_ADMIN_PASS}     id=admin-password
${BTN_ADMIN_ENTRAR}     xpath=//button[@type='submit']

# --- CADASTRO ---
${INPUT_REG_NOME}       id=nome
${INPUT_REG_EMAIL}      id=email
${INPUT_REG_CPF}        id=cpf
${INPUT_REG_SENHA}      id=senha
${INPUT_REG_CONF_SENHA}     id=confirmarSenha
${BTN_CADASTRAR}        xpath=//button[@type='submit']

# --- DASHBOARD / HOME ---
${LBL_SAUDACAO}         xpath=//div[contains(@class, 'user-greeting')] | //*[contains(text(), 'Olá,')]
${MSG_DASH_ADMIN}       xpath=//*[contains(text(), 'Painel Administrativo')]
