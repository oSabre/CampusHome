import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function HousingGroupPage() {
    const { adId } = useParams();
    const navigate = useNavigate();
    
    const [group, setGroup] = useState(null);
    const [tasks, setTasks] = useState([]);
    const [newTask, setNewTask] = useState("");
    const [loading, setLoading] = useState(true);
    const [isEditingRules, setIsEditingRules] = useState(false);
    const [rulesDraft, setRulesDraft] = useState("");
    
    const userId = localStorage.getItem('userId');
    const userRole = localStorage.getItem('userRole'); // 'OWNER' ou 'STUDENT'

    useEffect(() => {
        fetchGroupData();
        fetchTasks();
    }, [adId]);

    const fetchGroupData = async () => {
        try {
            const res = await api.get(`/housing-groups/advertisement/${adId}`);
            setGroup(res.data);
            setRulesDraft(res.data.houseRules);
        } catch (err) {
            console.error("Erro ao carregar grupo:", err);
            alert("Não foi possível carregar os dados desta moradia.");
            navigate(-1);
        } finally {
            setLoading(false);
        }
    };

    const fetchTasks = async () => {
        try {
            const res = await api.get(`/tasks/advertisement/${adId}`);
            setTasks(res.data);
        } catch (err) { console.error("Erro ao carregar tarefas", err); }
    };

    const handleAddTask = async (e) => {
        e.preventDefault();
        if (!newTask.trim()) return;
        try {
            const res = await api.post(`/tasks/advertisement/${adId}`, { description: newTask });
            setTasks([...tasks, res.data]);
            setNewTask("");
        } catch (err) { alert("Erro ao adicionar tarefa"); }
    };

    const handleDeleteTask = async (taskId) => {
        try {
            await api.delete(`/tasks/${taskId}`);
            setTasks(tasks.filter(t => t.id !== taskId));
        } catch (err) { console.error(err); }
    };

    const handleToggleTask = async (taskId) => {
        try {
            // Pegamos o status atual da task para saber se estamos marcando como "feita"
            const task = tasks.find(t => t.id === taskId);
            const isCompleting = !task.completed;

            // Enviamos o userId no corpo ou na query para o backend saber quem premiar
            const res = await api.patch(`/tasks/${taskId}/toggle?userId=${userId}`);
            
            setTasks(tasks.map(t => t.id === taskId ? res.data : t));
            
            // Se a tarefa foi concluída, recarregamos os dados do grupo 
            // para atualizar o Ranking de XP na hora!
            if (isCompleting) {
                fetchGroupData();
            }
        } catch (err) { 
            console.error("Erro ao atualizar XP:", err); 
        }
    };

    const handleUpdateRules = async () => {
        try {
            // Enviamos como objeto para o Map no Java capturar corretamente
            await api.put(`/housing-groups/${group.id}/rules`, { rules: rulesDraft });
            setGroup({ ...group, houseRules: rulesDraft });
            setIsEditingRules(false);
            alert("Regras atualizadas com sucesso!");
        } catch (err) {
            alert("Erro ao atualizar regras. Verifique o console.");
        }
    };

    if (loading) return <div className="p-10 text-center">Carregando...</div>;

    return (
        <div className="min-h-screen bg-slate-50 p-4 md:p-8">
            {/* HEADER IGUAL AO ANTERIOR */}
            <header className="max-w-6xl mx-auto mb-8 flex justify-between items-end">
                <div>
                    <h1 className="text-4xl font-black text-slate-900">{group.advertisementTitle}</h1>
                    <p className="text-blue-600 font-bold mt-1">Área de Convivência</p>
                </div>
                <button onClick={() => navigate(-1)} className="bg-white border-2 border-slate-200 px-4 py-2 rounded-xl font-bold text-slate-600">Sair</button>
            </header>

            <main className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-8">
                
                {/* COLUNA ESQUERDA: REGRAS */}
                <aside className="lg:col-span-1 space-y-6">
                    <section className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-bold">📜 Regras</h2>
                            {userRole === 'OWNER' && !isEditingRules && (
                                <button onClick={() => setIsEditingRules(true)} className="text-xs font-black text-blue-600">Editar</button>
                            )}
                        </div>
                        {isEditingRules ? (
                            <div className="space-y-2">
                                <textarea className="w-full p-3 bg-slate-50 rounded-xl text-sm border-2 border-blue-50" rows="5" value={rulesDraft} onChange={(e) => setRulesDraft(e.target.value)} />
                                <button onClick={handleUpdateRules} className="w-full bg-blue-600 text-white py-2 rounded-xl font-bold text-sm">Salvar</button>
                            </div>
                        ) : (
                            <div className="bg-amber-50 p-4 rounded-2xl text-sm italic">{group.houseRules}</div>
                        )}
                    </section>

                    {/* LISTA DE MORADORES */}
                    <section className="bg-white p-6 rounded-3xl shadow-sm border border-slate-200">
                        <h2 className="text-xl font-bold mb-4">👥 Moradores</h2>
                        <div className="space-y-3">
                            {group.residents?.map(res => (
                                <div key={res.id} className="flex items-center p-2 bg-slate-50 rounded-xl">
                                    <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center text-white text-[10px] font-bold mr-3">{res.name.charAt(0)}</div>
                                    <div>
                                        <p className="text-xs font-bold text-slate-800">{res.name}</p>
                                        <p className="text-[9px] text-slate-400 uppercase">{res.course}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </section>
                </aside>

                {/* COLUNA DIREITA: CENTRAL DE TAREFAS */}
                <div className="lg:col-span-2">
                    <section className="bg-white p-8 rounded-3xl shadow-sm border border-slate-200 min-h-[500px] flex flex-col">
                        <h2 className="text-2xl font-black text-slate-800 mb-6 flex items-center">
                            <span className="bg-orange-100 text-orange-600 w-10 h-10 rounded-xl flex items-center justify-center mr-3">✅</span>
                            Tarefas Coletivas
                        </h2>

                        {/* INPUT PARA NOVA TAREFA */}
                        <form onSubmit={handleAddTask} className="mb-8 flex gap-2">
                            <input 
                                type="text" 
                                placeholder="Ex: Comprar papel higiênico..." 
                                className="flex-1 bg-slate-50 border-2 border-slate-100 rounded-2xl px-4 py-3 outline-none focus:border-orange-200 transition"
                                value={newTask}
                                onChange={(e) => setNewTask(e.target.value)}
                            />
                            <button type="submit" className="bg-orange-500 text-white px-6 rounded-2xl font-bold hover:bg-orange-600 transition shadow-lg shadow-orange-100">
                                +
                            </button>
                        </form>
                    
                        <section className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 mb-8">
                            <h3 className="text-xs font-black text-slate-400 uppercase tracking-[0.2em] mb-6 flex items-center">
                                <span className="mr-2 text-lg">🏆</span> Ranking de Convivência
                            </h3>
                            
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                {/* Ordenamos os moradores pelo XP que vem do backend */}
                                {group.residents?.sort((a, b) => (b.xp || 0) - (a.xp || 0)).map((resident, index) => (
                                    <div key={resident.id} className="flex items-center justify-between p-3 bg-slate-50 rounded-xl border border-slate-100 relative overflow-hidden">
                                        {/* Badge de posição para o primeiro lugar */}
                                        {index === 0 && <div className="absolute top-0 right-0 bg-yellow-400 text-[8px] font-bold px-2 py-0.5 rounded-bl-lg">TOP 1</div>}
                                        
                                        <div className="flex items-center gap-3">
                                            <div className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs ${
                                                index === 0 ? 'bg-yellow-100 text-yellow-700' : 'bg-slate-200 text-slate-500'
                                            }`}>
                                                {index + 1}º
                                            </div>
                                            <div>
                                                <p className="text-sm font-bold text-slate-800 leading-none">{resident.name}</p>
                                                <p className="text-[10px] text-slate-400 mt-1 uppercase font-black">{resident.xp || 0} XP</p>
                                            </div>
                                        </div>

                                        {/* Ícone de Rank baseado no XP (o mesmo que usamos no dashboard) */}
                                        <span className="text-xl">
                                            {resident.xp >= 100 ? '👑' : resident.xp >= 50 ? '⭐' : resident.xp >= 10 ? '🌱' : '🥚'}
                                        </span>
                                    </div>
                                ))}
                            </div>
                        </section>

                        {/* LISTAGEM DE TAREFAS */}
                        <div className="flex-1 space-y-3">
                            {tasks.length > 0 ? tasks.map(task => (
                                <div 
                                    key={task.id} 
                                    className={`group flex items-center justify-between p-4 rounded-2xl border-2 transition-all ${task.completed ? 'bg-slate-50 border-transparent opacity-60' : 'bg-white border-slate-100 shadow-sm'}`}
                                >
                                    <div className="flex items-center gap-4">
                                        <input 
                                            type="checkbox" 
                                            checked={task.completed}
                                            onChange={() => handleToggleTask(task.id)}
                                            className="w-5 h-5 rounded-full border-slate-300 text-orange-500 focus:ring-orange-500 cursor-pointer"
                                        />
                                        <span className={`font-medium ${task.completed ? 'line-through text-slate-400' : 'text-slate-700'}`}>
                                            {task.description}
                                        </span>
                                    </div>
                                    <button 
                                        onClick={() => handleDeleteTask(task.id)}
                                        className="text-slate-300 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-all font-bold px-2"
                                    >
                                        Excluir
                                    </button>
                                </div>
                            )) : (
                                <div className="h-full flex flex-col items-center justify-center text-slate-300 italic py-20">
                                    <p>Tudo em ordem por aqui!</p>
                                    <p className="text-xs">Nenhuma tarefa pendente.</p>
                                </div>
                            )}
                        </div>
                    </section>
                </div>
            </main>
        </div>
    );
}