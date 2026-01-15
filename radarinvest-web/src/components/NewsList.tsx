import React from 'react';
import { EventoNoticia } from '../types';

interface Props {
    noticias: EventoNoticia[];
    ativoSelecionado: string | null;
}

export const NewsList: React.FC<Props> = ({ noticias, ativoSelecionado }) => {
    if (!ativoSelecionado) return null;

    return (
        <div style={{ marginTop: '2rem' }}>
            <h2 style={{ fontSize: '1.2rem', marginBottom: '1rem' }}>
                Notícias de {ativoSelecionado}
            </h2>

            {noticias.length === 0 ? (
                <div className="card">Nenhuma notícia recente encontrada.</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    {noticias.map(news => (
                        <div key={news.id} className="card" style={{ borderLeft: `4px solid ${news.severidade === 'ALTO' ? 'var(--color-danger)' : news.severidade === 'MEDIO' ? 'var(--color-warning)' : 'var(--color-success)'}` }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                                <span style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-secondary)' }}>
                                    {news.categoria}
                                </span>
                                <span style={{ fontSize: '0.8rem', color: 'var(--color-text-secondary)' }}>
                                    {new Date(news.publicadoEm).toLocaleDateString()}
                                </span>
                            </div>
                            <h4 style={{ margin: '0 0 0.5rem 0' }}>{news.titulo}</h4>
                            <p style={{ fontSize: '0.9rem', color: 'var(--color-text-secondary)', margin: 0 }}>
                                {news.resumo}
                            </p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};
