# RadarInvest - Roadmap de Evolução

## V1.1 - Refinamentos
- [X] **SAAS**: transformar a aplicação em um SaaS, permitindo que os usuários paguem por um plano mensal para ter acesso a todos os recursos. Cado esta informação, teremos que criar um cadastro de usuários e uma página de login, possibilitar o cadastro de uma lista de tikets.
- [X] **Segurança**: Adicionar Spring Security + JWT.

## V1.2 - I18N
- [ ] **I18N**: internacionalização da aplicação. Na tela dashborar ter alguma forma de escolher o idioma ( um combobox ou icones com bandeiras da nacionalidade - o que o UX achar mais apropriado). As mensagens de erro do projeto api que são retornado para o client solicitado (via request) devem ser traduzidas.

## V1.3 - Push Notifications
- [ ] **Push Notifications**: Alertas reais via Firebase. Alerta de dia de pagamento.

## V1.4 - Mobile Android
- [ ] **Segurança**: Implementar padrão de mercado para evitar Man-in-the-Middle, usar HTTPS (cadeado verde). O HTTPS criptografa TUDO (incluindo a senha) antes de sair do navegador do usuário.
- [ ] **Mobile App - Android**: Implementar app React Native usando a API existente (ver docs/mobile_architecture.md). controle de log radarinvest-android.log.

## V1.5 - Mobile iOS
- [ ] **Mobile App - iOS**: Implementar app React Native usando a API existente (ver docs/mobile_architecture.md). controle de log radarinvest-ios.log.

## V2.0 - Multi-Ativos
- [ ] **Renda Fixa**: 
  - Novo `TipoAtivoProvider` para Tesouro Direto / CDBs.
  - Tela de curvas de juros.
- [ ] **Cripto**:
  - Integração com CoinGecko/Binance API.
  - Monitoramento 24/7 (Scheduler mais frequente).

## V3.0 - Inteligência Avançada
- [ ] **LLM Real**: Integrar OpenAI/Claude via LangChain4j para resumir PDFs de relatórios gerenciais dos FIIs.
- [ ] **Sentiment Analysis**: Classificar notícias baseadas em sentimento de mercado (Bullish/Bearish).
