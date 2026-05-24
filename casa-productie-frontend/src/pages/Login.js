import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

export const Login = () => {
    const { login } = useAuth();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            await login(email, password);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Eroare la autentificare');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{
            minHeight: '100vh', display: 'flex', alignItems: 'center',
            justifyContent: 'center', backgroundColor: '#1a252f'
        }}>
            <div style={{
                backgroundColor: 'white', borderRadius: '12px',
                padding: '40px', width: '360px', boxShadow: '0 10px 40px rgba(0,0,0,0.4)'
            }}>
                <div style={{ textAlign: 'center', marginBottom: '30px' }}>
                    <h2 style={{ margin: '10px 0 4px', color: '#2c3e50' }}>Casa de Productie</h2>
                    <p style={{ color: '#7f8c8d', margin: 0, fontSize: '14px' }}>Autentificare</p>
                </div>

                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
                    <div>
                        <label style={{ fontSize: '13px', color: '#555', display: 'block', marginBottom: '5px' }}>Email</label>
                        <input
                            type="email"
                            value={email}
                            onChange={e => setEmail(e.target.value)}
                            required
                            placeholder="email@firma.ro"
                            style={{
                                width: '100%', padding: '10px 12px', border: '1px solid #ddd',
                                borderRadius: '6px', fontSize: '14px', boxSizing: 'border-box'
                            }}
                        />
                    </div>
                    <div>
                        <label style={{ fontSize: '13px', color: '#555', display: 'block', marginBottom: '5px' }}>Parola</label>
                        <input
                            type="password"
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                            required
                            placeholder="********"
                            style={{
                                width: '100%', padding: '10px 12px', border: '1px solid #ddd',
                                borderRadius: '6px', fontSize: '14px', boxSizing: 'border-box'
                            }}
                        />
                    </div>

                    {error && (
                        <div style={{
                            backgroundColor: '#fdecea', color: '#c0392b',
                            padding: '10px 12px', borderRadius: '6px', fontSize: '13px'
                        }}>
                            {error}
                        </div>
                    )}

                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            backgroundColor: loading ? '#95a5a6' : '#2c3e50',
                            color: 'white', border: 'none', padding: '12px',
                            borderRadius: '6px', fontSize: '15px', cursor: loading ? 'not-allowed' : 'pointer',
                            marginTop: '6px', fontWeight: 'bold'
                        }}
                    >
                        {loading ? 'Se autentifica...' : 'Intra in aplicatie'}
                    </button>
                </form>

                <div style={{ marginTop: '24px', padding: '14px', backgroundColor: '#f8f9fa', borderRadius: '8px', fontSize: '12px', color: '#555' }}>
                    <p style={{ margin: '0 0 8px', fontWeight: 'bold', color: '#7f8c8d' }}>Conturi demo:</p>
                    <div>Angajat: angajat@firma.ro / angajat123</div>
                    <div>Manager: manager@firma.ro / manager123</div>
                    <div>Administrator: admin@firma.ro / admin123</div>
                </div>
            </div>
        </div>
    );
};

