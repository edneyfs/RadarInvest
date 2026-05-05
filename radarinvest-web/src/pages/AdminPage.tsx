import React, { useEffect, useState } from 'react';
import { AdminService } from '../services/api';
import { AdminDashboardDTO } from '../types';
import { FaUsers, FaUserClock, FaChartLine } from 'react-icons/fa';

export const AdminPage: React.FC = () => {
    const [stats, setStats] = useState<AdminDashboardDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        loadStats();
        // Atualiza a cada 60 segundos (1 minuto)
        const interval = setInterval(loadStats, 60000);
        return () => clearInterval(interval);
    }, []);

    const loadStats = async () => {
        try {
            const data = await AdminService.getDashboard();
            setStats(data);
        } catch (err) {
            setError('Erro ao carregar dados. Verifique se você tem permissão de Administrador.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div style={{ padding: '2rem', color: '#fff' }}>Carregando dados administrativos...</div>;
    if (error) return <div style={{ padding: '2rem', color: '#ef4444' }}>{error}</div>;

    return (
        <div className="container" style={{ padding: '20px' }}>
            <h1 style={{ color: '#fff', marginBottom: '20px' }}>Painel Administrativo</h1>
            
            <div style={{ 
                display: 'grid', 
                gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', 
                gap: '20px' 
            }}>
                {/* Card Total Usuários */}
                <div className="card" style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                    <div style={{ 
                        backgroundColor: 'rgba(56, 189, 248, 0.2)', 
                        padding: '15px', 
                        borderRadius: '50%', 
                        color: '#38bdf8' 
                    }}>
                        <FaUsers size={24} />
                    </div>
                    <div>
                        <div style={{ fontSize: '0.9rem', color: '#94a3b8' }}>Total de Usuários</div>
                        <div style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{stats?.totalUsers}</div>
                    </div>
                </div>

                {/* Card Usuários Online */}
                <div className="card" style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                    <div style={{ 
                        backgroundColor: 'rgba(34, 197, 94, 0.2)', 
                        padding: '15px', 
                        borderRadius: '50%', 
                        color: '#22c55e' 
                    }}>
                        <FaUserClock size={24} />
                    </div>
                    <div>
                        <div style={{ fontSize: '0.9rem', color: '#94a3b8' }}>Usuários Online (10min)</div>
                        <div style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{stats?.onlineUsers}</div>
                    </div>
                </div>

                {/* Card Ativos Monitorados */}
                <div className="card" style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                    <div style={{ 
                        backgroundColor: 'rgba(234, 179, 8, 0.2)', 
                        padding: '15px', 
                        borderRadius: '50%', 
                        color: '#eab308' 
                    }}>
                        <FaChartLine size={24} />
                    </div>
                    <div>
                        <div style={{ fontSize: '0.9rem', color: '#94a3b8' }}>Ativos Monitorados</div>
                        <div style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{stats?.monitoredTickers}</div>
                    </div>
                </div>
            </div>
        </div>
    );
};
