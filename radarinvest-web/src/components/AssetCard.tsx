import React, { useState } from 'react';
import { ItemWatchlistDTO, EventoNoticia } from '../types';
import { WatchlistService } from '../services/api';
import { FaTrash, FaNewspaper, FaMoneyBillWave, FaChevronDown, FaChevronUp } from 'react-icons/fa';

interface Props {
    item: ItemWatchlistDTO;
    onRemove: () => void;
    onViewNews: (noticias: EventoNoticia[]) => void;
    onClick?: () => void;
}

export const AssetCard: React.FC<Props> = ({ item, onRemove, onViewNews, onClick }) => {
    const [expanded, setExpanded] = useState(false);

    // Helper para formatar data sem sofrer com timezones (DD/MM/YYYY)
    const formatDateUTC = (dateStr: string | undefined): string => {
        if (!dateStr) return '-';
        // Se vier completo (ISO), pega só os 10 primeiros chars YYYY-MM-DD
        const ymd = dateStr.substring(0, 10);
        const [year, month, day] = ymd.split('-');
        return `${day}/${month}/${year}`;
    };

    const handleCheckNews = async () => {
        try {
            const noticias = await WatchlistService.listarNoticias(item.ticker);
            onViewNews(noticias);
        } catch (error) {
            console.error(error);
        }
    };

    const isUrgent = item.mensagemMelhorDia?.includes("Compre até");
    const hasProventos = item.proventos && item.proventos.length > 0;

    return (
        <div className="card" onClick={onClick} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', cursor: onClick ? 'pointer' : 'default' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div>
                    <h3 style={{ margin: 0, fontSize: '1.25rem' }}>{item.ticker}</h3>
                    <span style={{ fontSize: '0.8rem', color: 'var(--color-text-secondary)' }}>
                        {item.tipoAtivo === 'B3_FII' && 'FII'}
                        {item.tipoAtivo === 'B3_ACAO' && 'AÇÃO'}
                        {item.tipoAtivo === 'B3_BDR' && 'BDR'}
                    </span>
                </div>
                <button onClick={(e) => { e.stopPropagation(); onRemove(); }} className="btn-icon">
                    <FaTrash />
                </button>
            </div>

            {/* Lista Resumida de Proventos */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                {hasProventos ? (
                    <>
                        <div style={{
                            display: 'grid',
                            gridTemplateColumns: '0.8fr 1.2fr 1fr 1fr',
                            gap: '0.5rem',
                            fontSize: '0.75rem',
                            fontWeight: 600,
                            color: 'var(--color-text-secondary)',
                            borderBottom: '1px solid rgba(255,255,255,0.1)',
                            paddingBottom: '4px'
                        }}>
                            <span>Tipo</span>
                            <span>Valor</span>
                            <span>Com</span>
                            <span>Pag</span>
                        </div>

                        {(expanded ? item.proventos : item.proventos.slice(0, 2)).map((prov: any, idx: number) => (
                            <div key={idx} style={{ display: 'grid', gridTemplateColumns: '0.8fr 1.2fr 1fr 1fr', gap: '0.5rem', fontSize: '0.85rem', alignItems: 'center' }}>
                                <div style={{ fontSize: '0.8rem', color: 'var(--color-text-secondary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }} title={prov.tipo}>
                                    {prov.tipo}
                                </div>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--color-success)' }}>
                                    <FaMoneyBillWave size={12} />
                                    <span>
                                        {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL', minimumFractionDigits: 2, maximumFractionDigits: 4 }).format(prov.valor)}
                                    </span>
                                </div>
                                <div style={{ fontSize: '0.85rem' }}>{formatDateUTC(prov.dataCom)}</div>
                                <div style={{ fontSize: '0.85rem' }}>{formatDateUTC(prov.dataPagamento)}</div>
                            </div>
                        ))}

                        {item.proventos.length > 2 && (
                            <button
                                onClick={(e) => { e.stopPropagation(); setExpanded(!expanded); }}
                                style={{
                                    background: 'none',
                                    border: 'none',
                                    color: 'var(--color-accent)',
                                    fontSize: '0.8rem',
                                    cursor: 'pointer',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    marginTop: '4px'
                                }}>
                                {expanded ? <><FaChevronUp style={{ marginRight: 4 }} /> Ver menos</> : <><FaChevronDown style={{ marginRight: 4 }} /> Ver mais ({item.proventos.length - 2})</>}
                            </button>
                        )}
                    </>
                ) : (
                    <div style={{ fontSize: '0.9rem', color: 'var(--color-text-secondary)', fontStyle: 'italic' }}>
                        Nenhum provento futuro encontrado.
                    </div>
                )}
            </div>

            <div style={{
                marginTop: 'auto',
                padding: '0.75rem',
                backgroundColor: isUrgent ? 'rgba(56, 189, 248, 0.1)' : 'rgba(255,255,255,0.05)',
                borderRadius: '8px',
                border: isUrgent ? '1px solid var(--color-accent)' : '1px solid transparent'
            }} title="Sugestão de data limite para compra baseada na Data Com">
                <div style={{ fontSize: '0.8rem', fontWeight: 600, color: isUrgent ? 'var(--color-accent)' : 'var(--color-text-secondary)' }}>
                    Melhor dia de compra
                </div>
                <div style={{
                    fontSize: '1rem',
                    fontWeight: 700,
                    color: item.mensagemMelhorDia === 'Data limite expirada.' ? '#ef4444' : 'inherit'
                }}>
                    {item.mensagemMelhorDia}
                </div>
            </div>

            <button onClick={(e) => { e.stopPropagation(); handleCheckNews(); }} style={{ width: '100%', background: 'none', border: '1px solid var(--color-text-secondary)', color: 'var(--color-text-secondary)', padding: '0.5rem', borderRadius: '6px', fontSize: '0.9rem', marginTop: '0.5rem' }}>
                <FaNewspaper style={{ marginRight: '0.5rem' }} /> Ver Notícias
            </button>
        </div>
    );
};
