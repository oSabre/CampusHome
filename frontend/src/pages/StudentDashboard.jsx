import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

export default function StudentDashboard() {
    const [studentData, setStudentData] = useState(null);
    const [ads, setAds] = useState([]);
    const [myGroup, setMyGroup] = useState(null);
    const [bio, setBio] = useState("");
    const [isEditingBio, setIsEditingBio] = useState(false);
    const [loadingMatch, setLoadingMatch] = useState(null);
    const [matchResults, setMatchResults] = useState({});
    
    const userId = localStorage.getItem('userId');
    const navigate = useNavigate();

    useEffect(() => {
        fetchData();
        const interval = setInterval(fetchData, 10000); 
        return () => clearInterval(interval);
    }, []);

    const fetchData = async () => {
        if (!userId) return;
        // 1. Busca Perfil (Prioridade para a Bio)
        try {
            const [userRes, groupRes] = await Promise.all([
                api.get(`/users/profile/${userId}`),
                api.get(`/housing-groups/student/${userId}`).catch(() => ({ data: null }))
            ]);
            setStudentData(userRes.data);
            if (userRes.data.bio) {
                setBio(userRes.data.bio);
                console.log("Bio carregada:", userRes.data.bio);
            }

            if (groupRes && groupRes.data) {
                setMyGroup(groupRes.data);
            } else {
                setMyGroup(null);
            }

        } catch (err) {
            console.error("Erro ao carregar perfil:", err);
        }

        // 2. Busca Anúncios (Independente)
        if (!myGroup) {
            try {
                const adsRes = await api.get('/ads/search'); 
                setAds(adsRes.data);
            } catch (err) {
                console.error("Erro ao carregar anúncios:", err);
            }
        }
    };

    const getBadge = (xp = 0) => {
        if (xp >= 100) return { label: "Lenda", color: "bg-purple-600", icon: "🏆" };
        if (xp >= 50) return { label: "Veterano", color: "bg-blue-500", icon: "⭐" };
        if (xp >= 10) return { label: "Engajado", color: "bg-green-500", icon: "🌱" };
        return { label: "Calouro", color: "bg-slate-400", icon: "🥚" };
    };

    const handleUpdateBio = async () => {
        try {
            await api.patch(`/users/${userId}/bio`, { bio });
            setIsEditingBio(false);
            alert("Bio atualizada com sucesso!");
        } catch (error) {
            alert("Erro ao atualizar bio.");
        }
    };

    const handleInterest = async (adId) => {
        try {
        await api.post('/interests', { adId, studentId: userId });
        alert("Interesse enviado! Aguarde o retorno do proprietário.");
        } catch (error) {
        alert("Você já demonstrou interesse ou houve um erro.");
        }
    };

    const handleCheckMatch = async (adId) => {
        setLoadingMatch(adId);
        try {
            const res = await api.get(`/match/preview?studentId=${userId}&adId=${adId}`);
            // Salva o resultado indexado pelo ID do anúncio
            setMatchResults(prev => ({ ...prev, [adId]: res.data }));
        } catch (error) {
            alert("Erro ao calcular afinidade com os moradores.");
        } finally {
            setLoadingMatch(null);
        }
    };

    // --- RENDERIZAÇÃO CONDICIONAL ---
    
    // SE O ESTUDANTE JÁ ESTIVER EM UM GRUPO (MORADOR)
    if (myGroup) {
        return (
        <div className="p-8 bg-blue-50 min-h-screen flex flex-col items-center">
            <div className="max-w-4xl w-full bg-white p-8 rounded-2xl shadow-xl border-t-8 border-blue-600">
            <h1 className="text-3xl font-black text-gray-800 mb-2">Minha Moradia Atual</h1>
            <p className="text-blue-600 font-bold mb-6 italic">Você faz parte do grupo: {myGroup.advertisementTitle}</p>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <section className="bg-gray-50 p-6 rounded-xl">
                <h2 className="font-bold text-xl mb-4">🏠 Regras da Casa</h2>
                <p className="text-gray-700 whitespace-pre-line">{myGroup.houseRules}</p>
                </section>

                <section className="bg-gray-50 p-6 rounded-xl">
                <h2 className="font-bold text-xl mb-4">👥 Meus Colegas</h2>
                <ul className="space-y-3">
                    {myGroup.residents.map(res => (
                    <li key={res.id} className="flex items-center space-x-3 bg-white p-2 rounded shadow-sm">
                        <div className="w-8 h-8 bg-blue-200 rounded-full flex items-center justify-center text-blue-700 font-bold text-xs">
                        {res.name.charAt(0)}
                        </div>
                        <span className="text-gray-800 font-medium">{res.name} ({res.course})</span>
                    </li>
                    ))}
                </ul>
                </section>
            </div>

            <button 
                onClick={() => navigate(`/housing-group/${myGroup.advertisementId}`)}
                className="mt-10 w-full py-4 bg-blue-600 text-white rounded-xl font-black text-lg hover:bg-blue-700 transition shadow-lg"
            >
                ENTRAR NA ÁREA DE CONVIVÊNCIA
            </button>
            </div>
        </div>
        );
    }

    // SE O ESTUDANTE AINDA ESTIVER BUSCANDO (DASHBOARD NORMAL)
    return (
        <div className="p-8 bg-gray-50 min-h-screen">
        <div className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-10">
            
            {/* COLUNA ESQUERDA: PERFIL */}
            <aside className="lg:col-span-1 space-y-6">
            <div className="bg-white p-6 rounded-2xl shadow-md border-t-4 border-green-500">
                <div className="text-center mb-4">
                    <div className="w-24 h-24 bg-green-100 rounded-full mx-auto mb-3 flex items-center justify-center text-3xl">🎓</div>
                    <h2 className="text-xl font-bold text-gray-800">{studentData?.name}</h2>
                    <p className="text-sm text-gray-500 mb-2">{studentData?.course}</p>
                    
                    {/* BADGE DE GAMIFICAÇÃO */}
                    <div className="flex flex-col items-center gap-1">
                        <div className={`${getBadge(studentData?.xp).color} text-white text-[10px] font-black px-3 py-1 rounded-full flex items-center gap-1 shadow-sm`}>
                            <span>{getBadge(studentData?.xp).icon}</span>
                            {getBadge(studentData?.xp).label}
                        </div>
                        <span className="text-[9px] font-bold text-gray-400">{studentData?.xp || 0} XP ACUMULADO</span>
                    </div>
                </div>

                <div className="space-y-3">
                <label className="text-xs font-black text-gray-400 uppercase tracking-wider">Sua Bio</label>
                {isEditingBio ? (
                    <>
                    <textarea 
                        value={bio}
                        onChange={(e) => setBio(e.target.value)}
                        className="w-full p-3 border rounded-lg text-sm bg-gray-50 focus:ring-2 focus:ring-green-500 outline-none"
                        rows="4"
                    />
                    <button 
                        onClick={handleUpdateBio}
                        className="w-full bg-green-600 text-white py-2 rounded-lg font-bold text-sm"
                    > Salvar Bio </button>
                    </>
                ) : (
                    <>
                    <p className="text-gray-600 text-sm italic">{bio ? `"${bio}"` : "Conte um pouco sobre seus hábitos de convivência..."}"</p>
                    <button 
                        onClick={() => setIsEditingBio(true)}
                        className="text-green-600 text-xs font-bold hover:underline"
                    > Editar Perfil </button>
                    </>
                )}
                </div>
            </div>
            </aside>

            {/* COLUNA DIREITA: CENTRAL DE ANÚNCIOS */}
            <main className="lg:col-span-2 space-y-6">
            <h2 className="text-2xl font-black text-gray-800 flex items-center">
                <span className="mr-2">🔍</span> Moradias Disponíveis
            </h2>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {ads.map(ad => (
                <div key={ad.id} className="bg-white p-6 rounded-2xl shadow-md border border-gray-100 hover:shadow-lg transition">
                    <div className="flex justify-between items-start mb-4">
                    <h3 className="font-bold text-lg text-gray-800">{ad.title}</h3>
                    <span className="bg-green-100 text-green-700 font-bold px-2 py-1 rounded text-sm">
                        R$ {ad.price}
                    </span>
                    </div>
                    <p className="text-gray-500 text-sm mb-4 line-clamp-3">{ad.description}</p>
                    <div className="flex items-center text-xs text-gray-400 mb-6">
                    <span className="mr-3">📍 {ad.neighborhood}</span>
                    <span>👤 Proprietário: {ad.ownerName}</span>
                    </div>
                    {matchResults[ad.id] ? (
                    // RESULTADO DO MATCH (Exibido após o clique)
                    <div className="mb-4 bg-indigo-50 border border-indigo-100 p-4 rounded-xl animate-in fade-in duration-500">
                        <div className="flex justify-between items-center mb-2">
                            <span className="text-[10px] font-black text-indigo-600 uppercase tracking-widest flex items-center">
                                <span className="mr-1">✨</span> Afinidade IA
                            </span>
                            <span className="text-sm font-black text-indigo-700">{matchResults[ad.id].score}%</span>
                        </div>
                        <p className="text-[11px] text-indigo-900 leading-relaxed italic">
                            {matchResults[ad.id].justification}
                        </p>
                    </div>
                ) : (
                    // BOTÃO PARA CALCULAR (Exibido inicialmente)
                    <button 
                        onClick={() => handleCheckMatch(ad.id)}
                        disabled={loadingMatch === ad.id}
                        className="w-full mb-4 py-2 bg-indigo-50 text-indigo-600 rounded-xl text-xs font-bold hover:bg-indigo-100 transition flex items-center justify-center gap-2 border border-transparent hover:border-indigo-200"
                    >
                        {loadingMatch === ad.id ? (
                            <>
                                <div className="w-3 h-3 border-2 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
                                Analisando perfis...
                            </>
                        ) : (
                            "🔍 Verificar compatibilidade com moradores"
                        )}
                    </button>
                )}
                    <button 
                    onClick={() => handleInterest(ad.id)}
                    className="w-full py-2 bg-gray-900 text-white rounded-lg font-bold hover:bg-black transition"
                    >
                    Demonstrar Interesse
                    </button>
                </div>
                ))}
            </div>
            </main>

        </div>
        </div>
    );
}