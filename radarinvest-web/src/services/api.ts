import axios from 'axios';
import { AdicionarAtivoDTO, ItemWatchlistDTO, EventoNoticia, AssetDetails } from '../types';

export const api = axios.create({
    baseURL: 'http://localhost:8080/radarinvest',
});

export const AssetService = {
    obterDetalhes: async (ticker: string, tipoAtivo: string): Promise<AssetDetails> => {
        const response = await api.get<AssetDetails>(`/api/ativos/${ticker}/detalhes`, {
            params: { tipo: tipoAtivo }
        });
        return response.data;
    }
};

export const WatchlistService = {
    listar: async (): Promise<ItemWatchlistDTO[]> => {
        const response = await api.get<ItemWatchlistDTO[]>('/watchlist');
        return response.data;
    },

    adicionar: async (ticker: string, tipoAtivo: string): Promise<void> => {
        const dto: AdicionarAtivoDTO = { ticker, tipoAtivo };
        await api.post('/watchlist', dto);
    },

    remover: async (ticker: string): Promise<void> => {
        await api.delete(`/watchlist/${ticker}`);
    },

    listarNoticias: async (ticker: string, tipoAtivo: string): Promise<EventoNoticia[]> => {
        // Backend returns NewsDTO { descricao, data, link }
        // We map to EventoNoticia
        const response = await api.get<any[]>(`/news/${ticker}`, {
             params: { tipoAtivo }
        });
        return response.data.map((item: any, index: number) => ({
            id: String(index),
            identificadorAtivo: ticker,
            titulo: item.descricao,
            resumo: item.descricao,
            url: item.link,
            publicadoEm: item.data, // yyyy-MM-dd
            categoria: 'Fato Relevante',
            severidade: 'BAIXO',
            fonte: 'Investidor10'
        }));
    }
};

export const AdminService = {
    forcarAtualizacao: async (): Promise<void> => {
        await api.post('/atualizar');
    },

    getDashboard: async (): Promise<import('../types').AdminDashboardDTO> => {
        const response = await api.get<import('../types').AdminDashboardDTO>('/admin/dashboard');
        return response.data;
    }
};
