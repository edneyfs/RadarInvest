import React, { useEffect, useState } from 'react';
import { WatchlistService } from '../services/api';
import { ItemWatchlistDTO, EventoNoticia } from '../types';
import { Header } from '../components/Header';
import { TickerInput } from '../components/TickerInput';
import { AssetCard } from '../components/AssetCard';
import { NewsModal } from '../components/NewsModal';
import { AssetDetailsModal } from '../components/AssetDetailsModal';
import { FaSync } from 'react-icons/fa';
import { MdApps, MdShowChart, MdApartment, MdPublic } from 'react-icons/md';

export const Dashboard: React.FC = () => {
    const [watchlist, setWatchlist] = useState<ItemWatchlistDTO[]>([]);
    const [noticias, setNoticias] = useState<EventoNoticia[]>([]);
    const [ativoSelecionado, setAtivoSelecionado] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    // Asset Details Modal State
    const [detailsTicker, setDetailsTicker] = useState<string | null>(null);
    const [detailsType, setDetailsType] = useState<string>('');

    const [filterType, setFilterType] = useState('ALL');
    const [sortType, setSortType] = useState('TICKER');

    const filters = [
        { label: 'Todos', value: 'ALL', icon: MdApps },
        { label: 'Ações', value: 'B3_ACAO', icon: MdShowChart },
        { label: 'FIIs', value: 'B3_FII', icon: MdApartment },
        { label: 'BDRs', value: 'B3_BDR', icon: MdPublic },
    ];

    const loadWatchlist = async () => {
        setLoading(true);
        try {
            const data = await WatchlistService.listar();
            setWatchlist(data);
        } catch (error) {
            console.error('Erro ao listar', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadWatchlist();
    }, []);

    const handleRemove = async (ticker: string) => {
        if (!confirm(`Remover ${ticker} do radar?`)) return;
        try {
            await WatchlistService.remover(ticker);
            loadWatchlist();
            if (ativoSelecionado === ticker) {
                setAtivoSelecionado(null);
                setNoticias([]);
            }
        } catch (error) {
            alert('Erro ao remover.');
        }
    };

    const handleViewNews = (news: EventoNoticia[]) => {
        if (news.length > 0) {
            setAtivoSelecionado(news[0].identificadorAtivo);
            setNoticias(news);
        } else {
            alert('Sem notícias recentes para este ativo.');
        }
    };


    const handleOpenDetails = (ticker: string, type: string) => {
        setDetailsTicker(ticker);
        setDetailsType(type);
    };

    // Filter and Sort Logic
    const filteredAndSortedWatchlist = watchlist
        .filter(item => {
            if (filterType === 'ALL') return true;
            return item.tipoAtivo === filterType;
        })
        .sort((a, b) => {
            if (sortType === 'TYPE') {
                return a.tipoAtivo.localeCompare(b.tipoAtivo);
            }
            return a.ticker.localeCompare(b.ticker);
        });

    return (
        <div>
            <Header />
            <div className="container" style={{ paddingBottom: '2rem' }}>

                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                    <h2 style={{ margin: 0 }}>Meus Ativos ({watchlist.length})</h2>
                    <button onClick={loadWatchlist} className="btn-icon">
                        <FaSync className={loading ? 'spin' : ''} />
                    </button>
                </div>

                <div style={{ marginBottom: '1.5rem' }}>
                    <TickerInput onUpdate={loadWatchlist} />
                </div>

                <div style={{ display: 'flex', justifyContent: 'space-between', gap: '1rem', marginBottom: '1rem', flexWrap: 'wrap' }}>
                    {/* Chip Filters */}
                    <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                        {filters.map(filter => {
                            const Icon = filter.icon;
                            const isActive = filterType === filter.value;
                            return (
                                <button
                                    key={filter.value}
                                    onClick={() => setFilterType(filter.value)}
                                    style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: '0.5rem',
                                        padding: '0.5rem 1rem',
                                        borderRadius: '20px',
                                        border: isActive ? 'none' : '1px solid var(--color-border)',
                                        backgroundColor: isActive ? 'var(--color-accent)' : 'transparent',
                                        color: isActive ? '#0F172A' : 'var(--color-text-secondary)',
                                        fontWeight: isActive ? 600 : 400,
                                        fontSize: '0.9rem',
                                        transition: 'all 0.2s',
                                        cursor: 'pointer'
                                    }}
                                >
                                    <Icon size={18} />
                                    {filter.label}
                                </button>
                            );
                        })}
                    </div>

                    {/* Sort */}
                    <select
                        value={sortType}
                        onChange={(e) => setSortType(e.target.value)}
                        style={{ padding: '0.5rem', borderRadius: '6px', border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)', color: 'var(--color-text-primary)', height: 'fit-content' }}
                    >
                        <option style={{ backgroundColor: 'var(--color-bg-card)', color: 'var(--color-text-primary)' }} value="TICKER">Ordenar por Ticker</option>
                        <option style={{ backgroundColor: 'var(--color-bg-card)', color: 'var(--color-text-primary)' }} value="TYPE">Ordenar por Tipo</option>
                    </select>
                </div>

                {filteredAndSortedWatchlist.length === 0 ? (
                    <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
                        <h3 style={{ color: 'var(--color-text-secondary)' }}>
                            {watchlist.length === 0 ? 'Seu radar está vazio.' : 'Nenhum ativo encontrado com este filtro.'}
                        </h3>
                        {watchlist.length === 0 && <p>Adicione um fundo ou ação para começar a monitorar.</p>}
                    </div>
                ) : (
                    <div style={{
                        display: 'grid',
                        gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
                        gap: '1.5rem'
                    }}>
                        {filteredAndSortedWatchlist.map(item => (
                            <AssetCard
                                key={item.ticker}
                                item={item}
                                onRemove={() => handleRemove(item.ticker)}
                                onViewNews={handleViewNews}
                                onClick={() => handleOpenDetails(item.ticker, item.tipoAtivo)}
                            />
                        ))}
                    </div>
                )}
            </div>

            {ativoSelecionado && (
                <NewsModal
                    isOpen={!!ativoSelecionado}
                    onClose={() => setAtivoSelecionado(null)}
                    noticias={noticias}
                    ticker={ativoSelecionado}
                />
            )}

            <AssetDetailsModal
                isOpen={!!detailsTicker}
                onClose={() => setDetailsTicker(null)}
                ticker={detailsTicker || ''}
                tipoAtivo={detailsType}
            />

            <style>{`
                .spin { animation: spin 1s linear infinite; }
                @keyframes spin { 100% { transform: rotate(360deg); } }
            `}</style>
        </div>
    );
};
