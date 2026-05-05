import React, { useState } from 'react';
import { authService, UsuarioDTO } from '../services/authService';
import { useNavigate, Link } from 'react-router-dom';

const RegisterPage: React.FC = () => {
    const [formData, setFormData] = useState<UsuarioDTO>({
        nome: '',
        cpf: '',
        email: '',
        senha: ''
    });
    const [confirmSenha, setConfirmSenha] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const validatePassword = (password: string): boolean => {
        const minLength = 8;
        const hasUpperCase = /[A-Z]/.test(password);
        const hasLowerCase = /[a-z]/.test(password);
        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

        return password.length >= minLength && hasUpperCase && hasLowerCase && hasSpecialChar;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (formData.senha !== confirmSenha) {
            setError('As senhas não coincidem.');
            return;
        }

        if (formData.nome.length < 5) {
            setError('Nome deve ter no mínimo 5 caracteres.');
            return;
        }

        if (!validatePassword(formData.senha)) {
            setError('Senha deve ter no mínimo 8 caracteres, 1 maiúscula, 1 minúscula e 1 caractere especial.');
            return;
        }

        try {
            await authService.cadastro(formData);
            navigate('/login', { state: { message: 'Cadastro realizado com sucesso! Faça login.' } });
        } catch (err) {
            console.error(err);
            setError('Erro ao cadastrar. Verifique os dados e tente novamente.');
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '50px' }}>
            <h1>Cadastro</h1>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '300px' }}>
                <input
                    id="nome"
                    name="nome"
                    placeholder="Nome Completo"
                    value={formData.nome}
                    onChange={handleChange}
                    required
                    style={{ padding: '8px' }}
                />
                <input
                    id="cpf"
                    name="cpf"
                    placeholder="CPF (apenas números)"
                    value={formData.cpf}
                    onChange={handleChange}
                    required
                    style={{ padding: '8px' }}
                />
                <input
                    id="email"
                    name="email"
                    type="email"
                    placeholder="Email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                    style={{ padding: '8px' }}
                />

                <div style={{ position: 'relative' }}>
                    <input
                        id="senha"
                        name="senha"
                        type={showPassword ? "text" : "password"}
                        placeholder="Senha"
                        value={formData.senha}
                        onChange={handleChange}
                        required
                        style={{ padding: '8px', width: '100%', boxSizing: 'border-box' }}
                    />
                    <button
                        id="toggle-password"
                        type="button"
                        onClick={() => setShowPassword(!showPassword)}
                        style={{
                            position: 'absolute',
                            right: '5px',
                            top: '50%',
                            transform: 'translateY(-50%)',
                            background: 'none',
                            border: 'none',
                            cursor: 'pointer',
                            fontSize: '12px'
                        }}
                    >
                        {showPassword ? 'Ocultar' : 'Mostrar'}
                    </button>
                </div>

                <input
                    id="confirmarSenha"
                    type="password"
                    placeholder="Confirmar Senha"
                    value={confirmSenha}
                    onChange={e => setConfirmSenha(e.target.value)}
                    required
                    style={{ padding: '8px' }}
                />

                <button type="submit" style={{ padding: '10px', cursor: 'pointer' }}>Cadastrar</button>
            </form>
            {error && <p style={{ color: 'red', maxWidth: '300px', textAlign: 'center' }}>{error}</p>}
            <p>Já tem conta? <Link to="/login">Faça Login</Link></p>
        </div>
    );
};

export default RegisterPage;
