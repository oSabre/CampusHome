import { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const [role, setRole] = useState('STUDENT'); // Controla o tipo de usuário
  const [formData, setFormData] = useState({});
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Define o endpoint baseado na role (ajuste conforme seu Controller)
    const endpoint = role === 'STUDENT' ? '/api/users/register/student' : '/api/users/register/owner';
    
    try {
      await api.post(endpoint, formData);
      alert('Cadastro realizado com sucesso!');
      navigate('/login');
    } catch (error) {
      console.error(error);
      alert('Erro ao cadastrar. Verifique os dados ou o console.');
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-xl p-8">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-2">CampusHome</h2>
        <p className="text-center text-gray-500 mb-8">Crie sua conta na plataforma</p>

        {/* Toggle de Role */}
        <div className="flex bg-gray-100 p-1 rounded-xl mb-6">
          <button 
            onClick={() => { setRole('STUDENT'); setFormData({}); }}
            className={`flex-1 py-2 rounded-lg font-medium transition ${role === 'STUDENT' ? 'bg-white shadow text-blue-600' : 'text-gray-500'}`}
          >
            Sou Aluno
          </button>
          <button 
            onClick={() => { setRole('OWNER'); setFormData({}); }}
            className={`flex-1 py-2 rounded-lg font-medium transition ${role === 'OWNER' ? 'bg-white shadow text-blue-600' : 'text-gray-500'}`}
          >
            Sou Dono
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <input name="name" placeholder="Nome Completo" onChange={handleChange} required
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
          
          <input name="email" type="email" placeholder="E-mail" onChange={handleChange} required
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
          
          <input name="password" type="password" placeholder="Senha" onChange={handleChange} required
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />

          <input name="telefone" type="telefone" placeholder="Telefone" onChange={handleChange} required
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />

          {/* Campos Dinâmicos */}
          {role === 'STUDENT' ? (
            <input name="course" placeholder="Seu Curso Superior" onChange={handleChange}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
          ) : (
            <input name="cpfCnpj" placeholder="CPF/CNPJ" onChange={handleChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
          )}

          <textarea name="bio" placeholder="Conte um pouco sobre você..." onChange={handleChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none h-24 resize-none" />

          <button type="submit" 
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg transition duration-300 mt-4">
            Cadastrar como {role === 'STUDENT' ? 'Aluno' : 'Anunciante'}
          </button>
        </form>

        <p className="text-center text-sm text-gray-600 mt-6">
          Já tem conta? <a href="/login" className="text-blue-600 font-semibold">Faça Login</a>
        </p>
      </div>
    </div>
  );
}