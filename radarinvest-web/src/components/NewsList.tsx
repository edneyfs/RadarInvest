import React, { useState } from 'react';
import { EventoNoticia } from '../types';
import { FaExternalLinkAlt, FaChevronLeft, FaChevronRight } from 'react-icons/fa';

interface Props {
    noticias: EventoNoticia[];
    ativoSelecionado: string | null;
}

export const NewsList: React.FC<Props> = ({ noticias, ativoSelecionado }) => {
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 5;

    if (!ativoSelecionado) return null;

    // Sort by date (descending) just in case
    const sortedNews = [...noticias].sort((a, b) => new Date(b.publicadoEm).getTime() - new Date(a.publicadoEm).getTime());

    const totalPages = Math.ceil(sortedNews.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const currentItems = sortedNews.slice(startIndex, startIndex + itemsPerPage);

    const handlePageChange = (newPage: number) => {
        if (newPage >= 1 && newPage <= totalPages) {
            setCurrentPage(newPage);
        }
    };

    const formatDate = (dateStr: string) => {
        if (!dateStr) return '-';
        // Ensure dd/MM/yyyy
        const ymd = dateStr.substring(0, 10);
        const [year, month, day] = ymd.split('-');
        return `${day}/${month}/${year}`;
    };

    return (
        <div style={{ marginTop: '0' }}>
            {noticias.length === 0 ? (
                <div className="card" style={{ textAlign: 'center', padding: '2rem' }}>Nenhuma notícia encontrada nos últimos 3 meses.</div>
            ) : (
                <>
                    <div style={{ overflowX: 'auto' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.9rem' }}>
                            <thead>
                                <tr style={{ borderBottom: '1px solid var(--color-border)', textAlign: 'left' }}>
                                    <th style={{ padding: '0.75rem', color: 'var(--color-text-secondary)' }}>Descrição</th>
                                    <th style={{ padding: '0.75rem', color: 'var(--color-text-secondary)', width: '120px' }}>Data</th>
                                    <th style={{ padding: '0.75rem', color: 'var(--color-text-secondary)', width: '80px', textAlign: 'center' }}>Link</th>
                                </tr>
                            </thead>
                            <tbody>
                                {currentItems.map((news, idx) => (
                                    <tr key={idx} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                                        <td style={{ padding: '0.75rem' }}>{news.titulo}</td>
                                        <td style={{ padding: '0.75rem' }}>{formatDate(news.publicadoEm)}</td>
                                        <td style={{ padding: '0.75rem', textAlign: 'center' }}>
                                            <a
                                                href={news.url}
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                style={{ color: 'var(--color-accent)', display: 'flex', justifyContent: 'center' }}
                                                title="Abrir Notícia"
                                            >
                                                <FaExternalLinkAlt size={14} />
                                            </a>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {/* Pagination */}
                    {totalPages > 1 && (
                        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '1rem', marginTop: '1rem' }}>
                            <button
                                onClick={() => handlePageChange(currentPage - 1)}
                                disabled={currentPage === 1}
                                style={{
                                    background: 'none',
                                    border: '1px solid var(--color-border)',
                                    color: 'var(--color-text-primary)',
                                    borderRadius: '50%',
                                    width: '32px',
                                    height: '32px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    cursor: currentPage === 1 ? 'not-allowed' : 'pointer',
                                    opacity: currentPage === 1 ? 0.5 : 1
                                }}
                            >
                                <FaChevronLeft size={12} />
                            </button>

                            <span style={{ fontSize: '0.9rem', color: 'var(--color-text-secondary)' }}>
                                Página {currentPage} de {totalPages}
                            </span>

                            <button
                                onClick={() => handlePageChange(currentPage + 1)}
                                disabled={currentPage === totalPages}
                                style={{
                                    background: 'none',
                                    border: '1px solid var(--color-border)',
                                    color: 'var(--color-text-primary)',
                                    borderRadius: '50%',
                                    width: '32px',
                                    height: '32px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    cursor: currentPage === totalPages ? 'not-allowed' : 'pointer',
                                    opacity: currentPage === totalPages ? 0.5 : 1
                                }}
                            >
                                <FaChevronRight size={12} />
                            </button>
                        </div>
                    )}
                </>
            )}
        </div>
    );
};
