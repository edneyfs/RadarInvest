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

    listarNoticias: async (ticker: string): Promise<EventoNoticia[]> => {
        const response = await api.get<EventoNoticia[]>(`/watchlist/${ticker}/noticias`);
        return response.data;
    }
};

export const AdminService = {
    forcarAtualizacao: async (): Promise<void> => {
        await api.post('/atualizar');
    }
};
