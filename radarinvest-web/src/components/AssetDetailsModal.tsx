import React, { useEffect, useState } from 'react';
import { AssetDetails } from '../types';
import { AssetService } from '../services/api';
import { FaTimes, FaChartLine, FaBuilding, FaMoneyBillWave, FaPercentage, FaUsers } from 'react-icons/fa';

interface AssetDetailsModalProps {
    isOpen: boolean;
    onClose: () => void;
    ticker: string;
    tipoAtivo: string;
}

export const AssetDetailsModal: React.FC<AssetDetailsModalProps> = ({ isOpen, onClose, ticker, tipoAtivo }) => {
    const [details, setDetails] = useState<AssetDetails | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (isOpen && ticker) {
            setLoading(true);
            AssetService.obterDetalhes(ticker, tipoAtivo)
                .then(data => setDetails(data))
                .catch(err => console.error(err))
                .finally(() => setLoading(false));
        } else {
            setDetails(null);
        }
    }, [isOpen, ticker, tipoAtivo]);

    if (!isOpen) return null;

    const formatCurrency = (val: number) => {
        return val ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val) : '-';
    };

    const formatPercent = (val: number) => {
        return val ? new Intl.NumberFormat('pt-BR', { style: 'percent', minimumFractionDigits: 2 }).format(val / 100) : '-';
    };

    const formatNumber = (val: number) => {
        return val ? new Intl.NumberFormat('pt-BR').format(val) : '-';
    };

    return (
        <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.7)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1000,
            backdropFilter: 'blur(5px)'
        }}>
            <div style={{
                backgroundColor: 'var(--color-surface)',
                borderRadius: '16px',
                width: '90%',
                maxWidth: '700px',
                maxHeight: '90vh',
                display: 'flex',
                flexDirection: 'column',
                boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.5)',
                position: 'relative',
                border: '1px solid var(--color-border)'
            }}>
                <div style={{
                    padding: '1.5rem',
                    borderBottom: '1px solid var(--color-border)',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                }}>
                    <div>
                        <h2 style={{ margin: 0, fontSize: '1.5rem' }}>{ticker}</h2>
                        <span style={{ color: 'var(--color-text-secondary)', fontSize: '0.9rem' }}>{details?.nome || 'Carregando...'}</span>
                    </div>
                    <button
                        onClick={onClose}
                        style={{
                            background: 'none',
                            border: 'none',
                            color: 'var(--color-text-secondary)',
                            cursor: 'pointer',
                            padding: '0.5rem',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            borderRadius: '50%',
                            transition: 'background 0.2s'
                        }}
                    >
                        <FaTimes size={20} />
                    </button>
                </div>

                <div style={{ padding: '2rem', overflowY: 'auto' }}>
                    {loading ? (
                        <div style={{ textAlign: 'center', padding: '2rem', color: 'var(--color-text-secondary)' }}>
                            Carregando detalhes...
                        </div>
                    ) : details ? (
                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '1.5rem' }}>
                            <IndicatorCard label="Cotação" value={formatCurrency(details.cotacao)} icon={<FaMoneyBillWave />} color="var(--color-success)" />
                            <IndicatorCard label="Dividend Yield (12M)" value={formatPercent(details.dy)} icon={<FaPercentage />} />
                            <IndicatorCard label="P/VP" value={details.pvp?.toFixed(2)} icon={<FaChartLine />} />
                            <IndicatorCard label="Variação (12M)" value={formatPercent(details.variacao12m)} icon={<FaChartLine />} color={details.variacao12m >= 0 ? 'var(--color-success)' : 'var(--color-error)'} />

                            <IndicatorCard label="Liquidez Diária" value={formatCurrency(details.liquidezDiaria)} icon={<FaMoneyBillWave />} />
                            <IndicatorCard label="Vacância" value={formatPercent(details.vacancia)} icon={<FaBuilding />} />
                            <IndicatorCard label="VP por Cota" value={formatCurrency(details.valorPatrimonialPorCota)} icon={<FaBuilding />} />
                            <IndicatorCard label="Nº Cotistas" value={formatNumber(details.numeroCotistas)} icon={<FaUsers />} />

                            <div style={{ gridColumn: '1 / -1', marginTop: '1rem', padding: '1rem', backgroundColor: 'rgba(255,255,255,0.03)', borderRadius: '8px' }}>
                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                                    <div>
                                        <div style={{ fontSize: '0.8rem', color: 'var(--color-text-secondary)' }}>Setor</div>
                                        <div style={{ fontWeight: 600 }}>{details.setor || '-'}</div>
                                    </div>
                                    <div>
                                        <div style={{ fontSize: '0.8rem', color: 'var(--color-text-secondary)' }}>Tipo de Fundo</div>
                                        <div style={{ fontWeight: 600 }}>{details.tipoFundo || '-'}</div>
                                    </div>
                                    <div>
                                        <div style={{ fontSize: '0.8rem', color: 'var(--color-text-secondary)' }}>CNPJ</div>
                                        <div style={{ fontWeight: 600 }}>{details.cnpj || '-'}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div style={{ textAlign: 'center', color: 'var(--color-text-secondary)' }}>
                            Não foi possível carregar os detalhes.
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

const IndicatorCard = ({ label, value, icon, color }: { label: string, value: string, icon: React.ReactNode, color?: string }) => (
    <div style={{
        backgroundColor: 'rgba(255, 255, 255, 0.05)',
        padding: '1rem',
        borderRadius: '12px',
        display: 'flex',
        flexDirection: 'column',
        gap: '0.5rem',
        border: '1px solid rgba(255, 255, 255, 0.1)'
    }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--color-text-secondary)', fontSize: '0.8rem' }}>
            {icon}
            <span>{label}</span>
        </div>
        <div style={{ fontSize: '1.2rem', fontWeight: 700, color: color || 'var(--color-text-primary)' }}>
            {value}
        </div>
    </div>
);
