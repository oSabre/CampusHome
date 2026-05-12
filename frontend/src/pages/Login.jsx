import { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      // O endpoint deve ser apenas '/users/login' pois o /api já está no api.js
      const response = await api.post('/users/login', { email, password });
      
      // Armazenamos os dados essenciais (id e role) para gerenciar o acesso
      const { id, role, name } = response.data;
      localStorage.setItem('userId', id);
      localStorage.setItem('userRole', role);
      localStorage.setItem('userName', name);

      alert(`Bem-vindo, ${name}!`);

      // Redirecionamento baseado no perfil (Role)
      if (role === 'OWNER') {
        navigate('/owner-dashboard'); // Onde ele vê anúncios e o Match IA
      } else {
        navigate('/student-dashboard'); // Marketplace de anúncios para estudantes
      }
    } catch (err) {
      console.error(err);
      setError('E-mail ou senha incorretos. Tente novamente.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-xl p-8">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-2">CampusHome</h2>
        <p className="text-center text-gray-500 mb-8">Entre na sua conta</p>

        {error && <p className="text-red-500 text-sm text-center mb-4">{error}</p>}

        <form onSubmit={handleLogin} className="space-y-4">
          <input 
            type="email" 
            placeholder="E-mail" 
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" 
          />
          
          <input 
            type="password" 
            placeholder="Senha" 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" 
          />

          <button type="submit" 
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg transition duration-300 mt-4">
            Entrar
          </button>
        </form>

        <p className="text-center text-sm text-gray-600 mt-6">
          Não tem uma conta? <a href="/register" className="text-blue-600 font-semibold">Cadastre-se</a>
        </p>
      </div>
    </div>
  );
}