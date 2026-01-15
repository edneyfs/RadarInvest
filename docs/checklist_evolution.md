# RadarInvest - Roadmap de Evolução

## V1.1 - Refinamentos
- [ ] **SAAS**: transformar a aplicação em um SaaS, permitindo que os usuários paguem por um plano mensal para ter acesso a todos os recursos. Cado esta informação, teremos que criar um cadastro de usuários e uma página de login, possibilitar o cadastro de uma lista de tikets.
- [ ] **Segurança**: Adicionar Spring Security + JWT.

## V1.2 - Push Notifications
- [ ] **Push Notifications**: Alertas reais via Firebase qdo atingir o "melhor dia".

## V1.3 - Mobile Android
- [ ] **Mobile App - Android**: Implementar app React Native usando a API existente (ver docs/mobile_architecture.md).

## V1.4 - Mobile iOS
- [ ] **Mobile App - iOS**: Implementar app React Native usando a API existente (ver docs/mobile_architecture.md).

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
