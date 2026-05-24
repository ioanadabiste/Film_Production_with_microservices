import React, { useEffect } from 'react';

export function Modal({ title, onClose, children }) {
    useEffect(() => {
        const onKey = (e) => { if (e.key === 'Escape') onClose(); };
        window.addEventListener('keydown', onKey);
        return () => window.removeEventListener('keydown', onKey);
    }, [onClose]);

    return (
        <div
            role="dialog"
            aria-modal="true"
            onClick={onClose}
            style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.5)', zIndex: 1000, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: 16 }}
        >
            <div onClick={(e) => e.stopPropagation()} style={{ background: '#fff', borderRadius: 8, padding: 24, minWidth: 320, maxWidth: 750, maxHeight: '90vh', overflowY: 'auto', width: '100%' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 16, gap: 12 }}>
                    <h3 style={{ margin: 0, flex: 1 }}>{title}</h3>
                    <button type="button" onClick={onClose} style={{ padding: '6px 12px', background: '#6c757d', color: '#fff', border: 'none', borderRadius: 4, cursor: 'pointer' }}>Inchide</button>
                </div>
                {children}
            </div>
        </div>
    );
}