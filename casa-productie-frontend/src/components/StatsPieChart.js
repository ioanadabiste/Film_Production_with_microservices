import React from 'react';
import { PieChart, Pie, Cell, ResponsiveContainer } from 'recharts';

const PIE_PALETTE = [
    '#4472C4', '#C0504D', '#9BBB59', '#8064A2', '#4BACC6',
    '#F79646', '#548DD4', '#E46C9A', '#7FBF7F', '#A5A5A5'
];

const RADIAN = Math.PI / 180;
const INTERNAL_PCT = 0.09;

function renderHybridLabel({ cx, cy, midAngle, innerRadius, outerRadius, percent, name }) {
    const pct = Math.round(percent * 100);
    const text = `${name} ${pct}%`;

    if (percent >= INTERNAL_PCT) {
        const r = innerRadius + (outerRadius - innerRadius) * 0.55;
        const x = cx + r * Math.cos(-midAngle * RADIAN);
        const y = cy + r * Math.sin(-midAngle * RADIAN);
        return (
            <text x={x} y={y} fill="#1a1a1a" textAnchor="middle" dominantBaseline="central" fontSize={12} fontWeight={600}>
                {text}
            </text>
        );
    }

    const r = outerRadius + 22;
    const x = cx + r * Math.cos(-midAngle * RADIAN);
    const y = cy + r * Math.sin(-midAngle * RADIAN);
    return (
        <text x={x} y={y} fill="#1a1a1a" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central" fontSize={11} fontWeight={500}>
            {text}
        </text>
    );
}

function renderHybridLabelLine(props) {
    const { percent, cx, cy, midAngle, outerRadius } = props;
    if (percent >= INTERNAL_PCT) return null;
    const sx = cx + outerRadius * Math.cos(-midAngle * RADIAN);
    const sy = cy + outerRadius * Math.sin(-midAngle * RADIAN);
    const ex = cx + (outerRadius + 18) * Math.cos(-midAngle * RADIAN);
    const ey = cy + (outerRadius + 18) * Math.sin(-midAngle * RADIAN);
    return <line x1={sx} y1={sy} x2={ex} y2={ey} stroke="#333" strokeWidth={1} />;
}

export function StatsPieChart({ title, subtitle, data, nameKey, valueKey, emptyText }) {
    const rows = (Array.isArray(data) ? data : [])
        .map((d) => ({
            name: (String(d[nameKey] ?? '').trim() || '—').slice(0, 32),
            value: Math.max(0, Number(d[valueKey]) || 0)
        }))
        .filter((d) => d.value > 0);

    if (rows.length === 0) {
        return (
            <div className="stats-pie-card">
                <h3 className="stats-pie-title">{title}</h3>
                {subtitle && <p className="stats-pie-subtitle">{subtitle}</p>}
                <div className="stats-empty">{emptyText || 'Nu există date încă.'}</div>
            </div>
        );
    }

    return (
        <div className="stats-pie-card">
            <h3 className="stats-pie-title">{title}</h3>
            {subtitle && <p className="stats-pie-subtitle">{subtitle}</p>}
            <ResponsiveContainer width="100%" height={360}>
                <PieChart margin={{ top: 8, right: 48, bottom: 8, left: 48 }}>
                    <Pie data={rows} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={115}
                        paddingAngle={0.5} label={renderHybridLabel} labelLine={renderHybridLabelLine}>
                        {rows.map((entry, i) => (
                            <Cell key={`${entry.name}-${i}`} fill={PIE_PALETTE[i % PIE_PALETTE.length]} stroke="#fff" strokeWidth={1} />
                        ))}
                    </Pie>
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
}
