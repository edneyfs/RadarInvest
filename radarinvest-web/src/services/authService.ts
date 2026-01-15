import axios from 'axios';
import { api } from './api';

export interface UsuarioDTO {
    nome: string;
    cpf: string;
    email: string;
    senha: string;
}

export interface LoginDTO {
    email: string;
    senha: string;
}

export interface TokenDTO {
    token: string;
    tipo: string;
    nome: string;
}

export const authService = {
    login: async (dados: LoginDTO): Promise<TokenDTO> => {
        const response = await api.post<TokenDTO>('/auth/login', dados);
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('usuario_nome', response.data.nome);
        }
        return response.data;
    },

    cadastro: async (dados: UsuarioDTO): Promise<void> => {
        await api.post('/usuarios', dados);
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('usuario_nome');
        // Redireciona considerando o base path definido no Vite/Router
        window.location.href = '/radarinvest/login';
    },

    isAuthenticated: () => {
        const token = localStorage.getItem('token');
        // Adicionar verificação de expiração se desejar (jwt-decode)
        return !!token;
    },

    getToken: () => {
        return localStorage.getItem('token');
    }
};

// Interceptor para adicionar token
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});
