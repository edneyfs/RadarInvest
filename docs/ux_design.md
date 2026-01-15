# UX Design - RadarInvest V1.0

## Mapa de Telas (V1.0)
A aplicação será **Single Page Application (SPA)** com foco em simplicidade.

### 1. Dashboard Principal (`/`)
- **Header**: Logo "RadarInvest", Botão de Tema (Dark/Light).
- **Hero Section**: "Seus ativos, sempre no radar."
- **Input Area**: Campo de texto "Ticker" + Select "Tipo" (Ação/FII) + Botão "Adicionar".
- **Grid de Ativos (Cards ou Tabela)**:
  - Ticker (ex: HGLG11)
  - Próximo Provento (Valor e Data Pagamento)
  - Data Com / Data Ex
  - **Destaque**: "Melhor dia de compra" (ex: "Até 14/05").
  - Botão "Remover".
- **Painel Lateral/Inferior**: "Notícias & Fatos"
  - Lista de cards compactos com título, resumo e severidade.

## Wireframe Textual

```
+-------------------------------------------------------------+
| [Logo] RadarInvest                                   (Theme)|
+-------------------------------------------------------------+
|                                                             |
|           SEUS ATIVOS, SEMPRE NO RADAR                      |
|                                                             |
|  [ HGLG11 ] [ FII v ] [ ADICIONAR AO RADAR ]                |
|                                                             |
+-------------------------------------------------------------+
| MEUS ATIVOS (3)                                             |
|                                                             |
| +-------------------------+   +-------------------------+   |
| | HGLG11 (FII)            |   | ITSA4 (Ação)            |   |
| | R$ 1,10 em 15/05        |   | R$ 0,02 em 30/06        |   |
| | Data Com: 08/05         |   | Data Com: ...           |   |
| | Data Ex:  09/05         |   | Data Ex: ...            |   |
| | [!] Compre até 08/05    |   | [!] Compre até ...      |   |
| +-------------------------+   +-------------------------+   |
|                                                             |
+-------------------------------------------------------------+
| NOTÍCIAS RELEVANTES (IA)                                    |
| - [ALTO] HGLG11: Fato Relevante sobre emissão de cotas...   |
| - [BAIXO] ITSA4: Anúncio de resultados trimestre...         |
+-------------------------------------------------------------+
```

## Microcopy & Mensagens
- **Empty State**: "Seu radar está vazio. Adicione um ativo acima para começar."
- **Loading**: "Sintonizando o radar..."
- **Erro**: "Interferência no sinal. Tente novamente."
- **Recomendação**: "Para receber o próximo provento, compre até {melhorDia}." (Neutro, informativo).
