export interface ProventoDTO {
    tipo: string;
    valor: number;
    dataCom?: string;
    dataPagamento?: string;
}

export interface ItemWatchlistDTO {
    ticker: string;
    tipoAtivo: 'B3_FII' | 'B3_ACAO' | 'B3_BDR';
    proventos: ProventoDTO[];
    melhorDiaCompra?: string;
    mensagemMelhorDia?: string;
}

export interface AdicionarAtivoDTO {
    ticker: string;
    tipoAtivo: string;
}

export interface EventoNoticia {
    id: string;
    identificadorAtivo: string;
    titulo: string;
    resumo: string;
    url: string;
    publicadoEm: string;
    categoria: string;
    severidade: 'BAIXO' | 'MEDIO' | 'ALTO';
    fonte: string;
}

export interface AssetDetails {
    ticker: string;
    nome: string;
    setor: string;
    cotacao: number;
    dy: number;
    pvp: number;
    variacao12m: number;
    vacancia: number;
    valorPatrimonialPorCota: number;
    tipoFundo: string;
    cnpj: string;
    numeroCotistas: number;
    liquidezDiaria: number;
}
