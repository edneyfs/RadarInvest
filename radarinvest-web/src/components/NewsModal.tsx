import React from 'react';
import { EventoNoticia } from '../types';
import { NewsList } from './NewsList';
import { FaTimes } from 'react-icons/fa';

interface NewsModalProps {
    isOpen: boolean;
    onClose: () => void;
    noticias: EventoNoticia[];
    ticker: string;
}

export const NewsModal: React.FC<NewsModalProps> = ({ isOpen, onClose, noticias, ticker }) => {
    if (!isOpen) return null;

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
                maxWidth: '600px',
                maxHeight: '80vh',
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
                    <h3 style={{ margin: 0 }}>Notícias: {ticker}</h3>
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
                        onMouseOver={(e) => e.currentTarget.style.backgroundColor = 'rgba(255,255,255,0.1)'}
                        onMouseOut={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                    >
                        <FaTimes size={20} />
                    </button>
                </div>

                <div style={{
                    overflowY: 'auto',
                    padding: '1.5rem'
                }}>
                    <NewsList noticias={noticias} ativoSelecionado={ticker} />
                </div>
            </div>
        </div>
    );
};
