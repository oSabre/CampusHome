import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

export default function OwnerDashboard() {
  const [ads, setAds] = useState([]);
  const [housingGroups, setHousingGroups] = useState([]);
  const [pendingRequests, setPendingRequests] = useState([]);
  // Adicionei o estado que faltava para evitar o erro no finally
  const [loading, setLoading] = useState(false); 
  
  const ownerId = localStorage.getItem('userId');
  const navigate = useNavigate();

  useEffect(() => {
    fetchData();

    const interval = setInterval(() => {
        fetchData();
    }, 30000);

    return () => clearInterval(interval);
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) return;

      // 1. Busca Anúncios e Interesses em paralelo para ser mais rápido
      const [adsRes, interestsRes] = await Promise.all([
        api.get(`/ads/owner/${userId}`),
        api.get(`/interests/owner/${userId}/pending`)
      ]);

      setAds(adsRes.data);
      setPendingRequests(interestsRes.data);

      // 2. Busca os grupos apenas se houver anúncios
      if (adsRes.data.length > 0) {
        const groupPromises = adsRes.data.map(ad =>
          api.get(`/housing-groups/advertisement/${ad.id}`)
            .then(res => res.data)
            .catch(err => null) // Se der erro ou não existir, retorna null
        );

        const groupsResults = await Promise.all(groupPromises);
        // Filtra nulos e atualiza o estado
        setHousingGroups(groupsResults.filter(g => g !== null));
      }
    } catch (error) {
      console.error("Erro geral no Dashboard:", error);
    } finally {
      setLoading(false); // Agora não vai mais dar erro aqui
    }
  };

  const handleUpdateStatus = async (interestId, newStatus) => {
    try {
      // O seu backend já cria o HousingGroup quando recebe 'ACCEPTED'
      await api.patch(`/interests/${interestId}/status?status=${newStatus}`);
      setPendingRequests(prev => prev.filter(req => req.id !== interestId));
      alert(`Solicitação ${newStatus === 'ACCEPTED' ? 'aceita' : 'recusada'}!`);
      
      // Pequeno delay para o banco de dados processar a transação
      setTimeout(() => {
        fetchData();
      }, 800);
    } catch (error) {
      console.error(error);
      alert("Erro ao atualizar status");
    }
  };

  const handleToggleStatus = async (adId) => {
    try {
      await api.patch(`/ads/${adId}/toggle-status?ownerId=${ownerId}`);
      fetchData();
    } catch (error) {
      alert("Erro ao alterar visibilidade do anúncio.");
    }
  };

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div className="max-w-6xl mx-auto space-y-10">
        <section>
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Meus Anúncios</h2>
            <button 
              onClick={() => navigate('/create-ad')}
              className="bg-blue-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-blue-700 transition"
            >
              + Criar Novo Anúncio
            </button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {ads.length > 0 ? ads.map(ad => (
              <div key={ad.id} className={`p-6 rounded-xl shadow-md border ${ad.active ? 'bg-white' : 'bg-gray-100'}`}>
                <h3 className="font-bold text-lg mb-2">{ad.title}</h3>
                <p className="text-gray-600 text-sm mb-4 line-clamp-2">{ad.description}</p>
                <div className="flex justify-between items-center">
                  <span className={`text-xs font-bold px-2 py-1 rounded ${ad.active ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                    {ad.active ? 'ATIVO' : 'INATIVO'}
                  </span>
                  <button 
                    onClick={() => handleToggleStatus(ad.id)}
                    className="text-sm text-blue-600 hover:underline font-medium"
                  >
                    {ad.active ? 'Desativar' : 'Reativar'}
                  </button>
                </div>
              </div>
            )) : (
              <p className="text-gray-500 italic">Você ainda não possui anúncios cadastrados.</p>
            )}
          </div>
        </section>

        <section>
          <h2 className="text-2xl font-bold text-gray-800 mb-6">Minhas Moradias (Grupos)</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {housingGroups.length > 0 ? housingGroups.map(group => (
              <div key={group.id} className="bg-white p-6 rounded-xl shadow-md border-l-4 border-blue-500">
                <h3 className="font-bold text-lg">Moradia: {group.advertisementTitle}</h3>
                <p className="text-sm text-gray-500 mb-4">{group.residents?.length || 0} morador(es) aceito(s)</p>
                <div className="space-y-2 mb-4">
                  <p className="text-sm font-semibold text-gray-700">Regras da Casa:</p>
                  <p className="text-sm italic text-gray-600 bg-gray-50 p-2 rounded line-clamp-2">
                    {group.houseRules || "Nenhuma regra definida ainda."}
                  </p>
                </div>
                <button 
                  onClick={() => navigate(`/housing-group/${group.advertisementId}`)}
                  className="w-full py-2 bg-gray-800 text-white rounded-lg hover:bg-gray-900 transition"
                >
                  Gerenciar Convivência
                </button>
              </div>
            )) : (
              <p className="text-gray-500 italic">As moradias aparecerão aqui assim que você aceitar o primeiro estudante.</p>
            )}
          </div>
        </section>
        {pendingRequests.length > 0 && (
  <section className="bg-white p-6 rounded-2xl shadow-md border-l-8 border-yellow-400">
    <h2 className="text-2xl font-black text-gray-800 mb-6 flex items-center">
      <span className="mr-3 text-2xl">🔔</span> Solicitações de Interesse
    </h2>
    
    <div className="space-y-6">
      {pendingRequests.map(req => (
        <div key={req.id} className="flex flex-col bg-gray-50 rounded-2xl border border-gray-200 overflow-hidden hover:shadow-md transition">
          
          <div className="flex flex-col md:flex-row p-6 gap-6">
            
            {/* 1. PERFIL DO ESTUDANTE */}
            <div className="flex-1 space-y-4">
              <div className="flex items-center gap-4">
                <div className="w-14 h-14 bg-blue-600 rounded-full flex items-center justify-center text-white text-2xl font-bold">
                  {req.studentName?.charAt(0)}
                </div>
                <div>
                  <h3 className="text-xl font-bold text-gray-900">{req.studentName}</h3>
                  <span className="bg-blue-100 text-blue-700 text-[10px] font-black px-2 py-0.5 rounded-full uppercase">
                    {req.studentCourse}
                  </span>
                </div>
              </div>

              <div>
                <h4 className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Bio do Candidato</h4>
                <p className="text-sm text-gray-600 italic leading-relaxed">
                  {req.studentBio ? `"${req.studentBio}"` : "⚠️ O estudante ainda não preencheu o perfil ou a bio não foi carregada."}
                </p>
              </div>
            </div>

            {/* 2. ANÁLISE DE COMPATIBILIDADE (IA) */}
            <div className="flex-1 bg-white p-5 rounded-xl border border-blue-100 shadow-sm relative">
              <div className={`absolute top-0 right-0 px-3 py-1 text-[10px] font-black rounded-bl-xl text-white ${
                req.matchScore >= 70 ? 'bg-green-500' : req.matchScore >= 40 ? 'bg-orange-500' : 'bg-gray-500'
              }`}>
                {req.matchScore}% AFFINITY
              </div>

              <div className="flex items-center gap-2 mb-3">
                <span className="text-indigo-600">✨</span>
                <h4 className="text-[10px] font-black text-indigo-600 uppercase tracking-widest">Análise de Convivência IA</h4>
              </div>

              <p className="text-xs text-gray-700 leading-relaxed font-medium">
                {req.matchJustification || "A IA não pôde analisar este perfil no momento."}
              </p>

              {/* Barra de afinidade visual */}
              <div className="mt-4 w-full bg-gray-100 h-1.5 rounded-full overflow-hidden">
                <div 
                  className={`h-full transition-all duration-1000 ${
                    req.matchScore >= 70 ? 'bg-green-500' : 'bg-indigo-500'
                  }`}
                  style={{ width: `${req.matchScore}%` }}
                ></div>
              </div>
            </div>

            {/* 3. AÇÕES */}
            <div className="flex flex-row md:flex-col justify-center gap-3 min-w-[150px]">
              <button 
                onClick={() => handleUpdateStatus(req.id, 'ACCEPTED')}
                className="flex-1 bg-gray-900 text-white px-6 py-3 rounded-xl font-bold text-sm hover:bg-black transition transform active:scale-95 shadow-lg"
              >
                Aceitar
              </button>
              <button 
                onClick={() => handleUpdateStatus(req.id, 'REJECTED')}
                className="flex-1 bg-white border border-red-200 text-red-500 px-6 py-3 rounded-xl font-bold text-sm hover:bg-red-50 transition transform active:scale-95"
              >
                Recusar
              </button>
            </div>
          </div>

          {/* RODAPÉ DO CARD */}
          <div className="bg-gray-100/50 px-6 py-3 border-t border-gray-200 flex justify-between items-center">
            <span className="text-[10px] font-bold text-gray-400 uppercase">
              Interesse no anúncio: <span className="text-gray-700">{req.adTitle}</span>
            </span>
            <span className="text-[10px] text-gray-400">ID #{req.id}</span>
          </div>
        </div>
      ))}
    </div>
  </section>
)}

      </div>
    </div>
  );
}