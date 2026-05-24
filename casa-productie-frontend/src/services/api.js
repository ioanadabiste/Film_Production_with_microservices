/**
 * GET: Gateway (8080), apoi direct microserviciul.
 * POST/PUT/DELETE: un singur URL (evita inregistrari duplicate).
 */
const GATEWAY = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const DIRECT_BY_PREFIX = [
    ['/api/FilmActor', 'http://localhost:5237'],
    ['/api/FilmImagine', 'http://localhost:5174'],
    ['/api/Film', 'http://localhost:5174'],
    ['/api/Regizor', 'http://localhost:5174'],
    ['/api/Scenarist', 'http://localhost:5174'],
    ['/api/Producator', 'http://localhost:5174'],
    ['/api/Actor', 'http://localhost:5237'],
    ['/api/User', 'http://localhost:5230'],
    ['/api/Statistic', 'http://localhost:5238'],
];

export { GATEWAY, DIRECT_BY_PREFIX };

export class ApiError extends Error {
    constructor(message, status) {
        super(message);
        this.status = status;
    }
}

function directBase(path) {
    let best = null;
    for (const [prefix, base] of DIRECT_BY_PREFIX) {
        if (path.startsWith(prefix) && (!best || prefix.length > best.prefix.length)) {
            best = { prefix, base };
        }
    }
    return best ? best.base : null;
}

let gatewayReachable = null;
let gatewayCheckAt = 0;

async function pingGateway() {
    const probes = [`${GATEWAY}/api/User`, `${GATEWAY}/api/Film`, `${GATEWAY}/api/Statistic`];
    for (const url of probes) {
        try {
            const ctrl = new AbortController();
            const timer = setTimeout(() => ctrl.abort(), 4000);
            const r = await fetch(url, { signal: ctrl.signal });
            clearTimeout(timer);
            if (r.status < 500) return true;
        } catch { /* next probe */ }
    }
    return false;
}

async function isGatewayReachable() {
    const now = Date.now();
    if (gatewayReachable !== null && now - gatewayCheckAt < 15000) {
        return gatewayReachable;
    }
    gatewayReachable = await pingGateway();
    gatewayCheckAt = now;
    return gatewayReachable;
}

async function urlForMutation(path) {
    const direct = directBase(path);
    if (await isGatewayReachable()) return `${GATEWAY}${path}`;
    if (direct) return `${direct}${path}`;
    return `${GATEWAY}${path}`;
}

function mutationUrls(path) {
    const direct = directBase(path);
    if (direct) return [`${direct}${path}`];
    return [`${GATEWAY}${path}`];
}

async function fetchMutationWithFallback(path, options) {
    const urls = mutationUrls(path);
    let lastErr = null;
    for (const url of urls) {
        try {
            return await fetch(url, options);
        } catch (err) {
            lastErr = err;
        }
    }
    const hint = path.startsWith('/api/User')
        ? 'Porneste User Service (demo) pe 5230 si/sau Gateway pe 8080.'
        : 'Verifica microserviciul sau Gateway 8080.';
    throw new ApiError(`Conexiune esuata (${options.method || 'POST'} ${path}). ${hint}`, 0, lastErr);
}

function urlsForRead(path) {
    const list = [`${GATEWAY}${path}`];
    const direct = directBase(path);
    if (direct) list.push(`${direct}${path}`);
    return [...new Set(list)];
}

function isMutation(method = 'GET') {
    return !['GET', 'HEAD'].includes((method || 'GET').toUpperCase());
}

async function apiFetch(path, options = {}) {
    const method = (options.method || 'GET').toUpperCase();

    if (isMutation(method)) {
        return fetchMutationWithFallback(path, options);
    }

    for (const url of urlsForRead(path)) {
        try {
            return await fetch(url, options);
        } catch { /* next */ }
    }
    throw new ApiError(`Nu pot citi datele. Gateway: ${GATEWAY}`, 0);
}

async function apiJson(path, options = {}) {
    const res = await apiFetch(path, options);
    if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new ApiError(text || `Eroare ${res.status}`, res.status);
    }
    return res.json();
}

async function apiText(path, options = {}) {
    const res = await apiFetch(path, options);
    if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new ApiError(text || `Eroare ${res.status}`, res.status);
    }
    return res.text();
}

const jsonHeaders = { 'Content-Type': 'application/json' };

async function pingService(url) {
    try {
        const ctrl = new AbortController();
        const timer = setTimeout(() => ctrl.abort(), 4000);
        const r = await fetch(url, { signal: ctrl.signal });
        clearTimeout(timer);
        return r.ok;
    } catch {
        return false;
    }
}

export const apiService = {
    checkBackend: async () => {
        gatewayReachable = null;
        const gateway = await pingGateway();
        gatewayReachable = gateway;
        gatewayCheckAt = Date.now();
        return {
            gateway,
            user: await pingService('http://localhost:5230/api/User'),
            film: await pingService('http://localhost:5174/api/Film'),
            actor: await pingService('http://localhost:5237/api/Actor'),
            stats: await pingService('http://localhost:5238/api/Statistic'),
        };
    },

    login: async (email, password) => {
        gatewayReachable = null;
        return fetchMutationWithFallback('/api/User/login', {
            method: 'POST',
            headers: jsonHeaders,
            body: JSON.stringify({ email, password })
        });
    },

    getUserById: (id) => apiJson(`/api/User/${id}`),
    getUsers: () => apiJson('/api/User'),
    getUsersByType: (type) => apiJson(`/api/User/byType/${type}`),
    createUser: (user) => apiText('/api/User', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(user) }),
    updateUser: (user) => apiText('/api/User', { method: 'PUT', headers: jsonHeaders, body: JSON.stringify(user) }),
    deleteUser: (id) => apiText(`/api/User/${id}`, { method: 'DELETE' }),
    exportUsersCsv: () => apiText('/api/User/export'),
    sendNotification: (n) => apiText('/api/User/notify', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(n) }),

    getActors: () => apiJson('/api/Actor'),
    getActorById: (id) => apiJson(`/api/Actor/${id}`),
    searchActors: (nume) => apiJson(`/api/Actor/search/${encodeURIComponent(nume)}`),
    createActor: (a) => apiText('/api/Actor', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(a) }),
    updateActor: (a) => apiText('/api/Actor', { method: 'PUT', headers: jsonHeaders, body: JSON.stringify(a) }),
    deleteActor: (id) => apiText(`/api/Actor/${id}`, { method: 'DELETE' }),

    getActorsByFilm: (idFilm) => apiJson(`/api/FilmActor/byFilm/${idFilm}`),
    getFilmsByActor: (idActor) => apiJson(`/api/FilmActor/byActor/${idActor}`),
    addActorToFilm: (fa) => apiText('/api/FilmActor', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(fa) }),
    removeActorFromFilm: (id) => apiText(`/api/FilmActor/${id}`, { method: 'DELETE' }),

    getFilms: () => apiJson('/api/Film'),
    getFilmById: (id) => apiJson(`/api/Film/${id}`),
    searchFilms: (titlu) => apiJson(`/api/Film/search/${encodeURIComponent(titlu)}`),
    filterByTip: (tip) => apiJson(`/api/Film/filterByTip/${tip}`),
    filterByCategorie: (cat) => apiJson(`/api/Film/filterByCategorie/${cat}`),
    filterByAn: (an) => apiJson(`/api/Film/filterByAn/${an}`),
    createFilm: (film) => apiJson('/api/Film', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(film) }),
    updateFilm: (film) => apiText('/api/Film', { method: 'PUT', headers: jsonHeaders, body: JSON.stringify(film) }),
    deleteFilm: (id) => apiText(`/api/Film/${id}`, { method: 'DELETE' }),
    exportFilmsCsv: () => apiText('/api/Film/export/csv'),
    exportFilmsJson: () => apiText('/api/Film/export/json'),
    exportFilmsXml: () => apiText('/api/Film/export/xml'),
    exportFilmsDoc: () => apiText('/api/Film/export/doc'),

    getFilmImages: (idFilm) => apiJson(`/api/FilmImagine/byFilm/${idFilm}`),
    addFilmImage: (fi) => apiText('/api/FilmImagine', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(fi) }),

    getRegizori: () => apiJson('/api/Regizor'),
    createRegizor: (r) => apiText('/api/Regizor', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(r) }),
    updateRegizor: (r) => apiText('/api/Regizor', { method: 'PUT', headers: jsonHeaders, body: JSON.stringify(r) }),
    deleteRegizor: (id) => apiText(`/api/Regizor/${id}`, { method: 'DELETE' }),
    getScenaristi: () => apiJson('/api/Scenarist'),
    createScenarist: (s) => apiText('/api/Scenarist', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(s) }),
    updateScenarist: (s) => apiText('/api/Scenarist', { method: 'PUT', headers: jsonHeaders, body: JSON.stringify(s) }),
    deleteScenarist: (id) => apiText(`/api/Scenarist/${id}`, { method: 'DELETE' }),
    getProducatori: () => apiJson('/api/Producator'),
    createProducator: (p) => apiText('/api/Producator', { method: 'POST', headers: jsonHeaders, body: JSON.stringify(p) }),
    updateProducator: (p) => apiText('/api/Producator', { method: 'PUT', headers: jsonHeaders, body: JSON.stringify(p) }),
    deleteProducator: (id) => apiText(`/api/Producator/${id}`, { method: 'DELETE' }),

    getTopRating: () => apiJson('/api/Statistic/topRating'),
    getTopViews: () => apiJson('/api/Statistic/topVizualizari'),
    getAllStats: () => apiJson('/api/Statistic'),
};

export const filmImageUrl = (img) => img?.caleImagine || img?.caleimagine || '';
