# Mobile Service Architecture - RadarInvest Provider

## Proposta para V2 (Mobile)
Para a versão mobile (V1.1+), recomendamos **React Native** (Expo) ou **Flutter**.

### Justificativa: React Native
Como o time Web já utiliza React, o uso de React Native permite **compartilhamento de código** (DTOs, Hooks, Lógica de Negócio) e redução da curva de aprendizado.

### Arquitetura de API (BFF - Backend for Frontend)
O Backend atual expõe endpoints REST. Para mobile, sugerimos manter os mesmos endpoints, adicionando autenticação JWT (futuro).

### Contratos (DTOs)
O Mobile consumirá os mesmos JSONs:

**GET /api/watchlist**
```json
[
  {
    "ticker": "HGLG11",
    "proximoProvento": {
      "valor": 1.10,
      "dataPagamento": "2024-05-15"
    },
    "melhorDiaCompra": "2024-05-08"
  }
]
```

### Push Notifications
Para V1.1, implementar um serviço worker no Backend que verifica:
1. Se `hoje == melhorDiaCompra`, enviar Push: "Hoje é o último dia para comprar {ativo} e receber proventos!"
2. Integração com Firebase Cloud Messaging (FCM).
