import React, { useState } from 'react';
import { authService, LoginDTO } from '../services/authService';
import { useNavigate } from 'react-router-dom';
import { FaUserShield } from 'react-icons/fa';

const AdminLoginPage: React.FC = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        try {
            const dto: LoginDTO = { email, senha };
            await authService.login(dto);
            
            // Check if really admin
            if (authService.isAdmin()) {
                navigate('/admin');
            } else {
                setError('Acesso negado. Usuário sem privilégios administrativos.');
                authService.logout();
            }
        } catch (err) {
            setError('Falha no login. Verifique suas credenciais de administrador.');
            console.error(err);
        }
    };

    return (
        <div style={{ 
            display: 'flex', 
            justifyContent: 'center', 
            alignItems: 'center', 
            height: '100vh', 
            backgroundColor: '#0f172a' // Darker background for Admin
        }}>
            <div style={{ 
                backgroundColor: '#1e293b', 
                padding: '40px', 
                borderRadius: '8px', 
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
                width: '100%',
                maxWidth: '400px',
                textAlign: 'center'
            }}>
                <div style={{ color: '#38bdf8', marginBottom: '20px' }}>
                    <FaUserShield size={48} />
                </div>
                <h1 style={{ color: '#fff', marginBottom: '30px', fontSize: '1.5rem' }}>Área Administrativa</h1>
                
                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                    <div style={{ textAlign: 'left' }}>
                        <label htmlFor="admin-username" style={{ color: '#94a3b8', fontSize: '0.875rem', display: 'block', marginBottom: '5px' }}>Login Administrativo</label>
                        <input
                            id="admin-username"
                            name="admin-username"
                            type="text"
                            value={email}
                            onChange={e => setEmail(e.target.value)}
                            required
                            autoComplete="off"
                            style={{ 
                                width: '100%', 
                                padding: '10px', 
                                borderRadius: '4px', 
                                border: '1px solid #334155',
                                backgroundColor: '#0f172a',
                                color: '#fff'
                            }}
                        />
                    </div>
                    
                    <div style={{ textAlign: 'left' }}>
                        <label htmlFor="admin-password" style={{ color: '#94a3b8', fontSize: '0.875rem', display: 'block', marginBottom: '5px' }}>Senha</label>
                        <input
                            id="admin-password"
                            name="admin-password"
                            type="password"
                            value={senha}
                            onChange={e => setSenha(e.target.value)}
                            required
                            autoComplete="new-password"
                            style={{ 
                                width: '100%', 
                                padding: '10px', 
                                borderRadius: '4px', 
                                border: '1px solid #334155',
                                backgroundColor: '#0f172a',
                                color: '#fff'
                            }}
                        />
                    </div>

                    {error && <div style={{ color: '#ef4444', fontSize: '0.875rem', marginTop: '10px' }}>{error}</div>}

                    <button 
                        type="submit" 
                        style={{ 
                            padding: '12px', 
                            backgroundColor: '#38bdf8', 
                            color: '#0f172a', 
                            border: 'none', 
                            borderRadius: '4px', 
                            fontWeight: 'bold', 
                            cursor: 'pointer',
                            marginTop: '20px'
                        }}
                    >
                        Acessar Painel
                    </button>
                </form>
                
                <div style={{ marginTop: '20px' }}>
                    <a href="/radarinvest/login" style={{ color: '#64748b', fontSize: '0.875rem', textDecoration: 'none' }}>Voltar para área do investidor</a>
                </div>
            </div>
        </div>
    );
};

export default AdminLoginPage;
