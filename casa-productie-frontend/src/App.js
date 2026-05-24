import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Dashboard } from './pages/Dashboard';
import { Login } from './pages/Login';
import { translations } from './context/LangContext';

const ROLE_LABELS = { angajat: 'Angajat', manager: 'Manager', administrator: 'Administrator' };

const AppContent = () => {
    const [lang, setLang] = useState('ro');
    const { user, logout } = useAuth();
    const t = translations[lang];

    if (!user) return <Login />;

    return (
        <div style={{ fontFamily: 'Arial, sans-serif' }}>
            <header style={{
                display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                padding: '12px 20px', backgroundColor: '#2c3e50', color: 'white'
            }}>
                <h1 style={{ margin: 0, fontSize: '20px' }}>{t.title}</h1>

                <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
                    <select
                        value={lang}
                        onChange={e => setLang(e.target.value)}
                        style={{ padding: '4px 8px', borderRadius: '4px' }}
                    >
                        <option value="ro">Romana</option>
                        <option value="en">English</option>
                        <option value="fr">Francais</option>
                    </select>

                    <div style={{
                        display: 'flex', alignItems: 'center', gap: '10px',
                        backgroundColor: 'rgba(255,255,255,0.1)',
                        padding: '6px 12px', borderRadius: '20px'
                    }}>
                        <span style={{ fontSize: '13px' }}>
                            <strong>{user.name} {user.surname}</strong>
                            &nbsp;|&nbsp;
                            <span style={{ color: '#f39c12' }}>{ROLE_LABELS[user.role]}</span>
                        </span>
                        <button
                            type="button"
                            onClick={logout}
                            style={{
                                backgroundColor: '#e74c3c', color: 'white',
                                border: 'none', borderRadius: '4px',
                                padding: '3px 10px', cursor: 'pointer', fontSize: '12px'
                            }}
                        >
                            Logout
                        </button>
                    </div>
                </div>
            </header>

            <Dashboard lang={lang} />
        </div>
    );
};

export default function App() {
    return (
        <AuthProvider>
            <AppContent />
        </AuthProvider>
    );
}
