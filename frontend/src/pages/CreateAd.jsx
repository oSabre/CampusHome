import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function CreateAd() {
  const navigate = useNavigate();
  const ownerId = localStorage.getItem('userId');

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    price: '',
    neighborhood: '', // Adicionado conforme sua entidade
    address: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Montando o payload para casar com a estrutura da sua entidade Java
    const payload = {
      title: formData.title,
      description: formData.description,
      price: parseFloat(formData.price), 
      neighborhood: formData.neighborhood,
      address: formData.address,
      userId: parseInt(ownerId),
      active: true
    };

    try {
      await api.post('/ads', payload);
      alert("Anúncio publicado com sucesso!");
      navigate('/owner-dashboard');
    } catch (error) {
      console.error("Erro ao salvar anúncio:", error);
      alert("Erro ao criar anúncio. Verifique se as informações estão corretas.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-10 px-4">
      <div className="max-w-xl mx-auto bg-white rounded-2xl shadow-lg p-8">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Novo Anúncio</h2>
        
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-semibold text-gray-600">Título</label>
            <input
              name="title"
              required
              className="w-full mt-1 p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Ex: Suíte individual próximo à reitoria"
              onChange={handleChange}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-semibold text-gray-600">Preço (R$)</label>
              <input
                name="price"
                type="number"
                step="0.01" // Permite decimais para o BigDecimal
                required
                className="w-full mt-1 p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                placeholder="0.00"
                onChange={handleChange}
              />
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-600">Bairro</label>
              <input
                name="neighborhood"
                required
                className="w-full mt-1 p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                placeholder="Ex: Trindade"
                onChange={handleChange}
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-600">Endereço Completo</label>
            <input
              name="address"
              required
              className="w-full mt-1 p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Rua, número, complemento..."
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-600">Descrição Detalhada</label>
            <textarea
              name="description"
              required
              rows="4"
              className="w-full mt-1 p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Fale sobre as regras, o que está incluso (água, luz, wi-fi)..."
              onChange={handleChange}
            ></textarea>
          </div>

          <div className="flex items-center justify-between pt-4 gap-4">
            <button
              type="button"
              onClick={() => navigate('/owner-dashboard')}
              className="text-gray-500 hover:underline"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-lg transition duration-200"
            >
              Criar Anúncio
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}