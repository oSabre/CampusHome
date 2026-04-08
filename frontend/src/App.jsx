import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import Register from './pages/Register'
import './App.css'

const Login = () => <div className="p-10 text-2xl">Tela de Login em breve...</div>;

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={
          <div className="h-screen bg-slate-900 flex flex-col items-center justify-center text-white">
            <h1 className="text-5xl font-bold mb-4">CampusHome</h1>
            <p className="text-blue-300">Ambiente pronto para desenvolvimento!</p>
            <div className="mt-6 space-x-4">
              <a href="/login" className="bg-blue-600 px-4 py-2 rounded">Ir para Login</a>
              <a href="/register" className="bg-green-600 px-4 py-2 rounded">Ir para Registro</a>
            </div>
          </div>
        } />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;