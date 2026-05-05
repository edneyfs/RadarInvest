import React, { useState } from 'react';
import { WatchlistService } from '../services/api';
import { FaPlus } from 'react-icons/fa';

interface Props {
    onUpdate: () => void;
}

export const TickerInput: React.FC<Props> = ({ onUpdate }) => {
    const [ticker, setTicker] = useState('');
    const [tipo, setTipo] = useState('B3_FII');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!ticker) return;

        setLoading(true);
        try {
            await WatchlistService.adicionar(ticker, tipo);
            setTicker('');
            onUpdate();
        } catch (error) {
            alert('Erro ao adicionar ativo. Verifique se já existe ou o ticker.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="card" style={{ display: 'flex', gap: '1rem', alignItems: 'center', marginBottom: '2rem' }}>
            <input
                id="novo-ativo-input"
                type="text"
                placeholder="Ex: HGLG11"
                value={ticker}
                onChange={(e) => setTicker(e.target.value.toUpperCase())}
                style={{ flex: 1 }}
                maxLength={6}
            />
            <select id="tipo-ativo-select" value={tipo} onChange={(e) => setTipo(e.target.value)} style={{ width: '150px' }}>
                <option value="B3_FII">Fundo Imob (FII)</option>
                <option value="B3_ACAO">Ação B3</option>
                <option value="B3_BDR">BDR</option>
            </select>
            <button id="adicionar-ativo-btn" type="submit" className="btn-primary" disabled={loading} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <FaPlus /> {loading ? 'Adicionando...' : 'Adicionar ao Radar'}
            </button>
        </form>
    );
};
