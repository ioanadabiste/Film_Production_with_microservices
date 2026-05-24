import React, { createContext, useState, useContext } from 'react';
import { apiService } from '../services/api';

const AuthContext = createContext();

const ROLE_MAP = { 0: 'angajat', 1: 'manager', 2: 'administrator' };

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null); // null = nelogat

    const login = async (email, password) => {
        const res = await apiService.login(email, password);
        if (!res.ok) throw new Error('Email sau parola gresite!');
        const data = await res.json();
        setUser({ ...data, role: ROLE_MAP[data.userType] ?? 'angajat' });
    };

    const logout = () => setUser(null);

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
