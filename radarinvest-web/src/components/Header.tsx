import React, { useEffect, useState } from 'react';
import { FaSatelliteDish, FaSignOutAlt } from 'react-icons/fa';
import { authService } from '../services/authService';

export const Header: React.FC = () => {
    const [userName, setUserName] = useState<string | null>(null);

    useEffect(() => {
        const storedName = localStorage.getItem('usuario_nome');
        if (storedName) {
            setUserName(storedName);
        }
    }, []);

    const handleLogout = () => {
        authService.logout();
    };

    return (
        <header style={{ padding: '1rem 0', borderBottom: '1px solid rgba(255,255,255,0.1)', marginBottom: '2rem' }}>
            <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: 0 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    <FaSatelliteDish size={24} color="var(--color-accent)" />
                    <h1 style={{ margin: 0, fontSize: '1.5rem', fontWeight: 700, letterSpacing: '-0.5px' }}>
                        Radar<span style={{ color: 'var(--color-accent)' }}>Invest</span>
                    </h1>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '1.5rem' }}>
                    {userName && (
                        <span style={{ color: 'var(--color-text-secondary)', fontSize: '0.95rem' }}>
                            Olá, <strong style={{ color: 'var(--color-text-primary)' }}>{userName}</strong>
                        </span>
                    )}
                    <button
                        onClick={handleLogout}
                        style={{
                            display: 'flex',
                            alignItems: 'center',
                            gap: '0.5rem',
                            background: 'rgba(255, 255, 255, 0.05)',
                            border: '1px solid var(--color-border)',
                            color: 'var(--color-text-primary)',
                            padding: '0.5rem 1rem',
                            borderRadius: '6px',
                            cursor: 'pointer',
                            fontSize: '0.9rem',
                            transition: 'all 0.2s'
                        }}
                        onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'rgba(255, 255, 255, 0.1)'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'rgba(255, 255, 255, 0.05)'}
                    >
                        <FaSignOutAlt />
                        Sair
                    </button>
                </div>
            </div>
        </header>
    );
};
