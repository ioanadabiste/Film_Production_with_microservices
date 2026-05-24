import React, { useState, useEffect, useCallback, useRef } from 'react';
import { apiService, ApiError, filmImageUrl } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { translations } from '../context/LangContext';
import { StatsPieChart } from '../components/StatsPieChart';
import { Modal } from '../components/Modal';

const safeArray = (d) => Array.isArray(d) ? d : [];

const TIP_FILM_OPTIONS = ['ARTISTIC', 'SERIAL'];
const CATEGORIE_OPTIONS = ['ACTIUNE', 'DRAMA', 'COMEDIE', 'HORROR', 'SF', 'DOCUMENTAR', 'ANIMATIE'];
const TIP_LABEL = { ARTISTIC: 'Artistic', SERIAL: 'Serial' };

// â”€â”€ BAR CHART â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
function StatisticsPanel({ statsRating, statsViews, movies, lang }) {
    const textsByLang = {
        ro: {
            ratingTitle: 'Distributia rating-urilor filmelor',
            ratingSub: '(pondere relativa in top rating)',
            viewsTitle: 'Distributia vizualizarilor',
            viewsSub: '(pondere relativa in top vizualizari)',
            catTitle: 'Distributia filmelor pe categorie',
            catSub: '(numar de filme in catalog)'
        },
        en: {
            ratingTitle: 'Film rating distribution',
            ratingSub: '(relative share in top ratings)',
            viewsTitle: 'View distribution',
            viewsSub: '(relative share in top views)',
            catTitle: 'Films by category',
            catSub: '(number of films in catalog)'
        },
        fr: {
            ratingTitle: 'Distribution des notes',
            ratingSub: '(part relative top notes)',
            viewsTitle: 'Distribution des vues',
            viewsSub: '(part relative top vues)',
            catTitle: 'Films par categorie',
            catSub: '(nombre de films)'
        }
    };
    const L = textsByLang[lang] || textsByLang.ro;
    const categoryData = Object.entries(
        safeArray(movies).reduce((acc, m) => {
            const cat = m.categorieFilm || 'Necunoscut';
            acc[cat] = (acc[cat] || 0) + 1;
            return acc;
        }, {})
    ).map(([cat, count]) => ({ categorieFilm: cat, nrFilme: count }));
    return (
        <div className="stats-charts-grid">
            <StatsPieChart title={L.ratingTitle} subtitle={L.ratingSub} data={statsRating} nameKey="titluFilm" valueKey="rating" />
            <StatsPieChart title={L.viewsTitle} subtitle={L.viewsSub} data={statsViews} nameKey="titluFilm" valueKey="nrVizualizari" />
            {categoryData.length > 0 && (
                <StatsPieChart title={L.catTitle} subtitle={L.catSub} data={categoryData} nameKey="categorieFilm" valueKey="nrFilme" />
            )}
        </div>
    );
}


// â”€â”€ ACTOR MULTI-SELECT â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
// Thumbnails 1-3 imagini per film in lista
function FilmImagesCell({ images }) {
    if (!images || images.length === 0) {
        return <span style={{ color: '#999', fontSize: 11 }}>-</span>;
    }
    return (
        <div style={{ display: 'flex', gap: 4 }}>
            {images.map((img, i) => (
                <img
                    key={i}
                    src={filmImageUrl(img)}
                    alt=""
                    style={{ width: 40, height: 28, objectFit: 'cover', borderRadius: 3, border: '1px solid #ccc' }}
                />
            ))}
        </div>
    );
}

const EMPTY_PERSON = { nume: '', prenume: '', anNastere: 1970, nationalitate: 'Romana', foto: '' };

function PersonFormFields({ person, onChange, inputStyle, t, showCompany }) {
    return (
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, marginTop: 8 }}>
            <input style={inputStyle} placeholder="Nume" required value={person.nume} onChange={e => onChange({ ...person, nume: e.target.value })} />
            <input style={inputStyle} placeholder="Prenume" required value={person.prenume} onChange={e => onChange({ ...person, prenume: e.target.value })} />
            <input style={{ ...inputStyle, width: 90 }} type="number" placeholder={t.birthYear} value={person.anNastere}
                onChange={e => onChange({ ...person, anNastere: parseInt(e.target.value, 10) || 1970 })} />
            <input style={inputStyle} placeholder={t.nationality} value={person.nationalitate} onChange={e => onChange({ ...person, nationalitate: e.target.value })} />
            <input style={{ ...inputStyle, minWidth: 220 }} placeholder={t.photoUrl} value={person.foto ?? ''} onChange={e => onChange({ ...person, foto: e.target.value })} />
            {showCompany && (
                <input style={inputStyle} placeholder={t.company} value={person.companie ?? ''} onChange={e => onChange({ ...person, companie: e.target.value })} />
            )}
        </div>
    );
}

// Permite selectarea mai multor actori dintr-o lista cu checkbox-uri
function ActorMultiSelect({ actors, selectedIds, onChange }) {
    const [search, setSearch] = useState('');
    const filtered = actors.filter(a =>
        `${a.prenume} ${a.nume}`.toLowerCase().includes(search.toLowerCase())
    );
    const toggle = (id) => {
        if (selectedIds.includes(id)) onChange(selectedIds.filter(x => x !== id));
        else onChange([...selectedIds, id]);
    };
    return (
        <div style={{ border: '1px solid #ccc', borderRadius: 4, padding: 8, maxHeight: 180, overflowY: 'auto' }}>
            <input
                placeholder="Cauta actor..."
                value={search}
                onChange={e => setSearch(e.target.value)}
                style={{ width: '100%', marginBottom: 6, padding: '4px 8px', borderRadius: 4, border: '1px solid #ccc', fontSize: 13, boxSizing: 'border-box' }}
            />
            {filtered.length === 0 && <div style={{ color: '#999', fontSize: 13 }}>Niciun actor gasit.</div>}
            {filtered.map(a => (
                <label key={a.id} style={{ display: 'flex', alignItems: 'center', gap: 6, padding: '3px 0', cursor: 'pointer', fontSize: 13 }}>
                    <input
                        type="checkbox"
                        checked={selectedIds.includes(a.id)}
                        onChange={() => toggle(a.id)}
                    />
                    {a.prenume} {a.nume}
                    {a.foto && <img src={a.foto} alt="" style={{ width: 22, height: 22, borderRadius: '50%', objectFit: 'cover' }} />}
                </label>
            ))}
        </div>
    );
}

// â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
export const Dashboard = ({ lang }) => {
    const { user } = useAuth();
    const t = translations[lang];

    // â”€â”€ Data state â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const [movies, setMovies] = useState([]);
    const [actors, setActors] = useState([]);
    const [regizori, setRegizori] = useState([]);
    const [scenaristi, setScenaristi] = useState([]);
    const [producatori, setProducatori] = useState([]);
    const [statsRating, setStatsRating] = useState([]);
    const [statsViews, setStatsViews] = useState([]);
    const [usersList, setUsersList] = useState([]);

    // â”€â”€ Filter / search state â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const [filmSearch, setFilmSearch] = useState('');
    const [filterTip, setFilterTip] = useState('');
    const [filterCat, setFilterCat] = useState('');
    const [filterAn, setFilterAn] = useState('');
    const [actorSearch, setActorSearch] = useState('');
    const [filterUserType, setFilterUserType] = useState('');

    // â”€â”€ Add Film form â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const [newFilm, setNewFilm] = useState({
        titlu: '', descriere: '', anRealizare: 2024,
        tipFilm: 'ARTISTIC', categorieFilm: 'ACTIUNE',
        regizorId: '', scenaristId: '', producatorId: ''
    });
    const [newFilmActorIds, setNewFilmActorIds] = useState([]); // actori selectati pentru filmul nou

    // â”€â”€ Add Actor form â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const [newActor, setNewActor] = useState({ ...EMPTY_PERSON, anNastere: 1990 });
    const [newRegizor, setNewRegizor] = useState({ ...EMPTY_PERSON });
    const [newScenarist, setNewScenarist] = useState({ ...EMPTY_PERSON });
    const [newProducator, setNewProducator] = useState({ ...EMPTY_PERSON, companie: '' });

    // â”€â”€ Add User form (admin) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const [newUser, setNewUser] = useState({ name: '', surname: '', email: '', password: '', phone: '', userType: 0 });

    // â”€â”€ Notification form â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const [notifUserId, setNotifUserId] = useState('');
    const [notifMsg, setNotifMsg] = useState('');
    const [notifType, setNotifType] = useState('EMAIL');

    // â”€â”€ Modals â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const [selectedFilm, setSelectedFilm] = useState(null);       // modal imagini
    const [filmImages, setFilmImages] = useState([]);
    const [newImageUrl, setNewImageUrl] = useState('');
    const [editFilm, setEditFilm] = useState(null);
    const [editActor, setEditActor] = useState(null);
    const [editUser, setEditUser] = useState(null);
    const [detailFilm, setDetailFilm] = useState(null);           // modal detalii film
    const [detailActors, setDetailActors] = useState([]);         // actorii filmului selectat
    const [detailDirector, setDetailDirector] = useState(null);
    const [detailScreenwriter, setDetailScreenwriter] = useState(null);
    const [detailProducer, setDetailProducer] = useState(null);
    const [detailFilmImages, setDetailFilmImages] = useState([]);
    const [filmImagesMap, setFilmImagesMap] = useState({});
    const [detailRegizorFilme, setDetailRegizorFilme] = useState(null);
    const [detailScenaristFilme, setDetailScenaristFilme] = useState(null);
    const [detailActorFilme, setDetailActorFilme] = useState(null);
    const [backendStatus, setBackendStatus] = useState({ gateway: false, user: false, film: false, actor: false, stats: false });

    const showError = (err) => {
        const msg = err instanceof ApiError ? err.message : (err?.message || 'Eroare');
        alert(msg);
    };

    const submittingRef = useRef(false);
    const runOnce = async (fn) => {
        if (submittingRef.current) return;
        submittingRef.current = true;
        try {
            await fn();
        } finally {
            submittingRef.current = false;
        }
    };

    // ── Load data ──────────────────────────────────────────────────────────
    const loadFilms = useCallback(() => {
        apiService.getFilms().then(d => setMovies(safeArray(d))).catch(console.error);
    }, []);
    const loadActors = useCallback(() => {
        apiService.getActors().then(d => setActors(safeArray(d))).catch(console.error);
    }, []);
    const loadUsers = useCallback(() => {
        apiService.getUsers().then(d => setUsersList(safeArray(d))).catch(console.error);
    }, []);

    useEffect(() => {
        apiService.checkBackend().then(setBackendStatus).catch(() =>
            setBackendStatus({ gateway: false, user: false, film: false, actor: false, stats: false })
        );
    }, []);

    useEffect(() => {
        const role = user.role;
        if (role === 'angajat' || role === 'manager') {
            loadFilms();
            if (role === 'angajat' || role === 'manager') loadActors();
            apiService.getRegizori().then(d => setRegizori(safeArray(d))).catch(console.error);
            apiService.getScenaristi().then(d => setScenaristi(safeArray(d))).catch(console.error);
            apiService.getProducatori().then(d => setProducatori(safeArray(d))).catch(console.error);
        }
        if (role === 'manager') {
            apiService.getTopRating().then(d => setStatsRating(safeArray(d))).catch(console.error);
            apiService.getTopViews().then(d => setStatsViews(safeArray(d))).catch(console.error);
        }
        if (role === 'administrator') loadUsers();
    }, [user.role, loadFilms, loadActors, loadUsers]);

    useEffect(() => {
        if (movies.length === 0) {
            setFilmImagesMap({});
            return;
        }
        Promise.all(
            movies.map(m =>
                apiService.getFilmImages(m.id)
                    .then(imgs => ({ id: m.id, imgs: safeArray(imgs).slice(0, 3) }))
                    .catch(() => ({ id: m.id, imgs: [] }))
            )
        ).then(results => {
            const map = {};
            results.forEach(r => { map[r.id] = r.imgs; });
            setFilmImagesMap(map);
        });
    }, [movies]);

    useEffect(() => {
        if (regizori.length > 0 && !newFilm.regizorId)
            setNewFilm(p => ({ ...p, regizorId: regizori[0].id }));
    }, [regizori]);
    useEffect(() => {
        if (scenaristi.length > 0 && !newFilm.scenaristId)
            setNewFilm(p => ({ ...p, scenaristId: scenaristi[0].id }));
    }, [scenaristi]);
    useEffect(() => {
        if (producatori.length > 0 && !newFilm.producatorId)
            setNewFilm(p => ({ ...p, producatorId: producatori[0].id }));
    }, [producatori]);

    const sortFilms = (list) => [...list].sort((a, b) => {
        const tipCmp = String(a.tipFilm ?? '').localeCompare(String(b.tipFilm ?? ''));
        if (tipCmp !== 0) return tipCmp;
        return (a.anRealizare ?? 0) - (b.anRealizare ?? 0);
    });

    // â”€â”€ Filtered data â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const filteredMovies = sortFilms(movies.filter(m => {
        if (filmSearch && !m.titlu?.toLowerCase().includes(filmSearch.toLowerCase())) return false;
        if (filterTip && m.tipFilm !== filterTip) return false;
        if (filterCat && m.categorieFilm !== filterCat) return false;
        if (filterAn && String(m.anRealizare) !== filterAn) return false;
        return true;
    }));
    const filteredActors = actors.filter(a =>
        !actorSearch || a.nume?.toLowerCase().includes(actorSearch.toLowerCase()) || a.prenume?.toLowerCase().includes(actorSearch.toLowerCase())
    );
    const filteredUsers = usersList.filter(u =>
        filterUserType === '' || u.userType === parseInt(filterUserType)
    );

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const regizorName = (id) => {
        const r = regizori.find(x => x.id === id);
        return r ? `${r.prenume} ${r.nume}` : `ID ${id}`;
    };
    const scenaristName = (id) => {
        const s = scenaristi.find(x => x.id === id);
        return s ? `${s.prenume} ${s.nume}` : `ID ${id}`;
    };
    const producatorName = (id) => {
        const p = producatori.find(x => x.id === id);
        return p ? `${p.prenume} ${p.nume}` : `ID ${id}`;
    };

    // â”€â”€ Handlers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const handleAddFilm = (e) => {
        e.preventDefault();
        runOnce(async () => {
        try {
            const created = await apiService.createFilm({
                ...newFilm,
                regizorId: parseInt(newFilm.regizorId),
                scenaristId: parseInt(newFilm.scenaristId),
                producatorId: parseInt(newFilm.producatorId)
            });
            const filmId = created?.id;
            let actorFail = 0;
            if (filmId && newFilmActorIds.length > 0) {
                for (const actorId of newFilmActorIds) {
                    try {
                        await apiService.addActorToFilm({
                            idFilm: parseInt(filmId, 10),
                            idActor: parseInt(actorId, 10),
                            rol: 'Personaj principal'
                        });
                    } catch { actorFail++; }
                }
            }
            if (actorFail > 0) {
                alert(`Film salvat (id ${filmId}), dar ${actorFail} actor(i) nu s-au legat.`);
            } else {
                alert('Film adaugat!');
            }
            loadFilms();
            setNewFilm({
                titlu: '', descriere: '', anRealizare: 2024, tipFilm: 'ARTISTIC', categorieFilm: 'ACTIUNE',
                regizorId: regizori[0]?.id ?? '', scenaristId: scenaristi[0]?.id ?? '', producatorId: producatori[0]?.id ?? ''
            });
            setNewFilmActorIds([]);
        } catch (err) { showError(err); }
        });
    };

    const handleDeleteFilm = (id) => {
        if (!window.confirm('Sigur stergi filmul?')) return;
        apiService.deleteFilm(id).then(msg => { alert(msg); loadFilms(); });
    };
    const handleUpdateFilm = (e) => {
        e.preventDefault();
        apiService.updateFilm(editFilm).then(msg => { alert(msg); setEditFilm(null); loadFilms(); });
    };
    const handleAddActor = (e) => {
        e.preventDefault();
        runOnce(async () => {
            try {
                const msg = await apiService.createActor(newActor);
                alert(msg);
                loadActors();
                setNewActor({ ...EMPTY_PERSON, anNastere: 1990 });
            } catch (err) { showError(err); }
        });
    };
    const reloadRegizori = () => apiService.getRegizori().then(d => setRegizori(safeArray(d))).catch(console.error);
    const reloadScenaristi = () => apiService.getScenaristi().then(d => setScenaristi(safeArray(d))).catch(console.error);
    const reloadProducatori = () => apiService.getProducatori().then(d => setProducatori(safeArray(d))).catch(console.error);
    const handleAddRegizor = (e) => {
        e.preventDefault();
        runOnce(async () => {
            try {
                const msg = await apiService.createRegizor(newRegizor);
                alert(msg);
                reloadRegizori();
                setNewRegizor({ ...EMPTY_PERSON });
            } catch (err) { showError(err); }
        });
    };
    const handleAddScenarist = (e) => {
        e.preventDefault();
        runOnce(async () => {
            try {
                const msg = await apiService.createScenarist(newScenarist);
                alert(msg);
                reloadScenaristi();
                setNewScenarist({ ...EMPTY_PERSON });
            } catch (err) { showError(err); }
        });
    };
    const handleAddProducator = (e) => {
        e.preventDefault();
        runOnce(async () => {
            try {
                const msg = await apiService.createProducator(newProducator);
                alert(msg);
                reloadProducatori();
                setNewProducator({ ...EMPTY_PERSON, companie: '' });
            } catch (err) { showError(err); }
        });
    };
    const handleDeleteActor = (id) => {
        if (!window.confirm('Sigur stergi actorul?')) return;
        apiService.deleteActor(id).then(msg => { alert(msg); loadActors(); });
    };
    const handleUpdateActor = (e) => {
        e.preventDefault();
        apiService.updateActor(editActor).then(msg => { alert(msg); setEditActor(null); loadActors(); });
    };
    const handleViewImages = (film) => {
        setSelectedFilm(film);
        apiService.getFilmImages(film.id).then(d => setFilmImages(safeArray(d))).catch(() => setFilmImages([]));
    };
    const handleAddImage = () => {
        if (!newImageUrl) return;
        apiService.addFilmImage({ idFilm: selectedFilm.id, caleImagine: newImageUrl })
            .then(() => { apiService.getFilmImages(selectedFilm.id).then(d => setFilmImages(safeArray(d))); setNewImageUrl(''); });
    };
    const handleSearchFilm = () => {
        if (!filmSearch) { loadFilms(); return; }
        apiService.searchFilms(filmSearch).then(d => setMovies(safeArray(d))).catch(console.error);
    };
    const handleSearchActor = () => {
        if (!actorSearch) { loadActors(); return; }
        apiService.searchActors(actorSearch).then(d => setActors(safeArray(d))).catch(console.error);
    };
    const handleSendNotif = (e) => {
        e.preventDefault();
        apiService.sendNotification({ type: notifType, userId: parseInt(notifUserId), message: notifMsg })
            .then(msg => {
                alert(`${msg}\n\nVerifica consola IntelliJ la User Service (demo) pentru logul ${notifType}.`);
                setNotifMsg(''); setNotifUserId('');
            })
            .catch(showError);
    };
    const handleCreateUser = (e) => {
        e.preventDefault();
        runOnce(async () => {
            try {
                const msg = await apiService.createUser({ ...newUser, userType: parseInt(newUser.userType, 10) });
                alert(msg);
                loadUsers();
                setNewUser({ name: '', surname: '', email: '', password: '', phone: '', userType: 0 });
            } catch (err) { showError(err); }
        });
    };
    const handleDeleteUser = (id) => {
        if (!window.confirm('Sigur stergi utilizatorul?')) return;
        apiService.deleteUser(id).then(msg => { alert(msg); loadUsers(); });
    };
    const handleUpdateUser = (e) => {
        e.preventDefault();
        apiService.updateUser(editUser).then(msg => { alert(msg); setEditUser(null); loadUsers(); });
    };
    const handleExportUsers = () => {
        apiService.exportUsersCsv().then(csv => {
            const blob = new Blob([csv], { type: 'text/csv' });
            const a = document.createElement('a'); a.href = URL.createObjectURL(blob); a.download = 'utilizatori.csv'; a.click();
        });
    };
    const handleExportFilms = (fmt) => {
        const fn = fmt === 'csv' ? apiService.exportFilmsCsv
            : fmt === 'json' ? apiService.exportFilmsJson
            : fmt === 'doc' ? apiService.exportFilmsDoc
            : apiService.exportFilmsXml;
        fn().then(data => {
            const mime = fmt === 'json' ? 'application/json'
                : fmt === 'xml' ? 'application/xml'
                : fmt === 'doc' ? 'application/msword'
                : 'text/csv';
            const blob = new Blob([data], { type: mime });
            const a = document.createElement('a'); a.href = URL.createObjectURL(blob); a.download = `filme.${fmt}`; a.click();
        });
    };

    // â”€â”€ Deschide modal detalii film (regizor, scenarist, actori) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    const handleViewDetail = async (film) => {
        setDetailFilm(film);
        setDetailActors([]);
        setDetailFilmImages([]);
        setDetailDirector(regizori.find(r => r.id === film.regizorId) ?? null);
        setDetailScreenwriter(scenaristi.find(s => s.id === film.scenaristId) ?? null);
        setDetailProducer(producatori.find(p => p.id === film.producatorId) ?? null);
        try {
            const imgs = await apiService.getFilmImages(film.id);
            setDetailFilmImages(safeArray(imgs).slice(0, 3));
        } catch {
            setDetailFilmImages([]);
        }
        try {
            const filmActors = await apiService.getActorsByFilm(film.id);
            // filmActors are { idFilm, idActor, rol } â€“ resolve names
            const resolved = await Promise.all(
                safeArray(filmActors).map(async fa => {
                    const actor = actors.find(a => a.id === fa.idActor) ?? await apiService.getActorById(fa.idActor).catch(() => null);
                    return { ...fa, actor };
                })
            );
            setDetailActors(resolved);
        } catch { setDetailActors([]); }
    };

    const tipLabel = TIP_LABEL;
    const userTypeName = (tp) => ['Angajat', 'Manager', 'Administrator'][tp] ?? tp;

    const inputStyle = { padding: '6px 10px', borderRadius: 4, border: '1px solid #ccc', fontSize: 13 };
    const btnStyle = (color) => ({ padding: '6px 14px', background: color, color: '#fff', border: 'none', borderRadius: 4, cursor: 'pointer', fontSize: 13 });
    const sectionStyle = { background: '#f8f9fa', border: '1px solid #dee2e6', borderRadius: 8, padding: 20, marginBottom: 24 };

    return (
        <div style={{ padding: 20, maxWidth: 1400, margin: '0 auto' }}>

            {backendStatus.gateway && (
                <div style={{ background: '#d4edda', color: '#155724', padding: 10, borderRadius: 8, marginBottom: 16, fontSize: 13 }}>
                    <strong>API Gateway (8080) activ</strong>
                </div>
            )}
            {backendStatus.user && !backendStatus.gateway && (
                <div style={{ background: '#fff3cd', color: '#856404', padding: 12, borderRadius: 8, marginBottom: 16, fontSize: 13 }}>
                    Gateway (8080) nu raspunde in browser - folosim legatura directa la microservicii.
                </div>
            )}

            {/* â•â•â• ANGAJAT + MANAGER â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {(user.role === 'angajat' || user.role === 'manager') && (<>

                {/* FILME */}
                <div style={sectionStyle}>
                    <h2 style={{ marginTop: 0 }}>{t.movies}</h2>

                    {/* Search + Filter */}
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, marginBottom: 12 }}>
                        <input style={inputStyle} placeholder={t.searchPlaceholder} value={filmSearch}
                            onChange={e => setFilmSearch(e.target.value)}
                            onKeyDown={e => e.key === 'Enter' && handleSearchFilm()} />
                        <button style={btnStyle('#3498db')} onClick={handleSearchFilm}>{t.search}</button>
                        <select style={inputStyle} value={filterTip} onChange={e => setFilterTip(e.target.value)}>
                            <option value="">{t.allTypes}</option>
                            {TIP_FILM_OPTIONS.map(o => <option key={o} value={o}>{o}</option>)}
                        </select>
                        <select style={inputStyle} value={filterCat} onChange={e => setFilterCat(e.target.value)}>
                            <option value="">{t.allCategories}</option>
                            {CATEGORIE_OPTIONS.map(o => <option key={o} value={o}>{o}</option>)}
                        </select>
                        <input style={{ ...inputStyle, width: 100 }} type="number" placeholder={t.filterByYear}
                            value={filterAn} onChange={e => setFilterAn(e.target.value)} />
                        <button style={btnStyle('#7f8c8d')} onClick={() => { setFilmSearch(''); setFilterTip(''); setFilterCat(''); setFilterAn(''); loadFilms(); }}>Reset</button>
                        {(user.role === 'angajat' || user.role === 'manager') && (<>
                            <button style={btnStyle('#27ae60')} onClick={() => handleExportFilms('csv')}>Export CSV</button>
                            <button style={btnStyle('#e67e22')} onClick={() => handleExportFilms('json')}>Export JSON</button>
                            <button style={btnStyle('#8e44ad')} onClick={() => handleExportFilms('xml')}>Export XML</button>
                            <button style={btnStyle('#2980b9')} onClick={() => handleExportFilms('doc')}>Export DOC</button>
                        </>)}
                    </div>

                    {/* Films table */}
                    <div style={{ overflowX: 'auto' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
                            <thead>
                                <tr style={{ background: '#2c3e50', color: '#fff' }}>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>ID</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Imagini</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>{t.titleField}</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>An</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Tip</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Categorie</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Regizor</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Scenarist</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Producator</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Actiuni</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredMovies.map((m, i) => (
                                    <tr key={m.id} style={{ background: i % 2 === 0 ? '#fff' : '#f2f2f2' }}>
                                        <td style={{ padding: '6px 10px' }}>{m.id}</td>
                                        <td style={{ padding: '6px 10px' }}>
                                            <FilmImagesCell images={filmImagesMap[m.id] || []} />
                                        </td>
                                        <td style={{ padding: '6px 10px' }}>
                                            <button type="button" style={{ background: 'none', border: 'none', padding: 0, color: '#2980b9', cursor: 'pointer', fontWeight: 'bold' }}
                                                onClick={() => handleViewDetail(m)}>{m.titlu}</button>
                                        </td>
                                        <td style={{ padding: '6px 10px' }}>{m.anRealizare}</td>
                                        <td style={{ padding: '6px 10px' }}>{tipLabel[m.tipFilm] ?? m.tipFilm}</td>
                                        <td style={{ padding: '6px 10px' }}>{m.categorieFilm}</td>
                                        <td style={{ padding: '6px 10px' }}>{regizorName(m.regizorId)}</td>
                                        <td style={{ padding: '6px 10px' }}>{scenaristName(m.scenaristId)}</td>
                                        <td style={{ padding: '6px 10px' }}>{producatorName(m.producatorId)}</td>
                                        <td style={{ padding: '6px 10px', display: 'flex', gap: 4, flexWrap: 'wrap' }}>
                                            <button type="button" style={btnStyle('#17a2b8')} onClick={() => handleViewDetail(m)}>Detalii</button>
                                            <button type="button" style={btnStyle('#3498db')} onClick={() => handleViewImages(m)}>{t.images}</button>
                                            {user.role === 'angajat' && <>
                                                <button style={btnStyle('#f39c12')} onClick={() => setEditFilm({ ...m })}>Edit</button>
                                                <button style={btnStyle('#e74c3c')} onClick={() => handleDeleteFilm(m.id)}>Sterge</button>
                                            </>}
                                        </td>
                                    </tr>
                                ))}
                                {filteredMovies.length === 0 && (
                                    <tr><td colSpan={10} style={{ padding: 16, textAlign: 'center', color: '#999' }}>Niciun film gasit.</td></tr>
                                )}
                            </tbody>
                        </table>
                    </div>

                    {/* Add Film Form â€“ ANGAJAT ONLY */}
                    {user.role === 'angajat' && (
                        <details style={{ marginTop: 16 }}>
                            <summary style={{ cursor: 'pointer', fontWeight: 'bold', color: '#27ae60' }}>+ {t.add} Film</summary>
                            <form onSubmit={handleAddFilm} style={{ display: 'flex', flexDirection: 'column', gap: 10, marginTop: 10 }}>
                                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
                                    <input style={inputStyle} placeholder={t.titleField} required
                                        value={newFilm.titlu} onChange={e => setNewFilm(p => ({ ...p, titlu: e.target.value }))} />
                                    <input style={inputStyle} placeholder={t.descField}
                                        value={newFilm.descriere} onChange={e => setNewFilm(p => ({ ...p, descriere: e.target.value }))} />
                                    <input style={{ ...inputStyle, width: 80 }} type="number" placeholder="An"
                                        value={newFilm.anRealizare} onChange={e => setNewFilm(p => ({ ...p, anRealizare: parseInt(e.target.value) }))} />
                                    <select style={inputStyle} value={newFilm.tipFilm} onChange={e => setNewFilm(p => ({ ...p, tipFilm: e.target.value }))}>
                                        {TIP_FILM_OPTIONS.map(o => <option key={o}>{o}</option>)}
                                    </select>
                                    <select style={inputStyle} value={newFilm.categorieFilm} onChange={e => setNewFilm(p => ({ ...p, categorieFilm: e.target.value }))}>
                                        {CATEGORIE_OPTIONS.map(o => <option key={o}>{o}</option>)}
                                    </select>
                                </div>

                                {/* â”€â”€ DROPDOWN REGIZOR â”€â”€ */}
                                <label style={{ fontSize: 13 }}>
                                    <strong>Regizor:</strong>
                                    <select style={{ ...inputStyle, marginLeft: 8 }}
                                        value={newFilm.regizorId}
                                        onChange={e => setNewFilm(p => ({ ...p, regizorId: parseInt(e.target.value) }))}>
                                        {regizori.map(r => (
                                            <option key={r.id} value={r.id}>{r.prenume} {r.nume}</option>
                                        ))}
                                    </select>
                                </label>

                                {/* â”€â”€ DROPDOWN SCENARIST â”€â”€ */}
                                <label style={{ fontSize: 13 }}>
                                    <strong>Scenarist:</strong>
                                    <select style={{ ...inputStyle, marginLeft: 8 }}
                                        value={newFilm.scenaristId}
                                        onChange={e => setNewFilm(p => ({ ...p, scenaristId: parseInt(e.target.value) }))}>
                                        {scenaristi.map(s => (
                                            <option key={s.id} value={s.id}>{s.prenume} {s.nume}</option>
                                        ))}
                                    </select>
                                </label>

                                <label style={{ fontSize: 13 }}>
                                    <strong>Producator:</strong>
                                    <select style={{ ...inputStyle, marginLeft: 8 }}
                                        value={newFilm.producatorId}
                                        onChange={e => setNewFilm(p => ({ ...p, producatorId: parseInt(e.target.value) }))}>
                                        {producatori.map(p => (
                                            <option key={p.id} value={p.id}>{p.prenume} {p.nume}{p.companie ? ` (${p.companie})` : ''}</option>
                                        ))}
                                    </select>
                                </label>

                                {/* â”€â”€ ACTORI MULTI-SELECT â”€â”€ */}
                                <div style={{ fontSize: 13 }}>
                                    <strong>Actori in distributie:</strong>
                                    <span style={{ color: '#666', marginLeft: 8 }}>
                                        {newFilmActorIds.length === 0
                                            ? '(niciun actor selectat)'
                                            : `${newFilmActorIds.length} actor(i) selectat(i)`}
                                    </span>
                                    <div style={{ marginTop: 6 }}>
                                        <ActorMultiSelect
                                            actors={actors}
                                            selectedIds={newFilmActorIds}
                                            onChange={setNewFilmActorIds}
                                        />
                                    </div>
                                </div>

                                <div>
                                    <button type="submit" style={btnStyle('#27ae60')}>+ {t.add}</button>
                                </div>
                            </form>
                        </details>
                    )}
                </div>

                {user.role === 'angajat' && (
                    <div style={sectionStyle}>
                        <h2 style={{ marginTop: 0 }}>{t.catalogTeam}</h2>
                        <p style={{ fontSize: 13, color: '#666', marginTop: 0 }}>{t.catalogHint}</p>

                        <details style={{ marginBottom: 12 }}>
                            <summary style={{ cursor: 'pointer', fontWeight: 'bold', color: '#27ae60' }}>+ {t.add} {t.directors}</summary>
                            <form onSubmit={handleAddRegizor}>
                                <PersonFormFields person={newRegizor} onChange={setNewRegizor} inputStyle={inputStyle} t={t} />
                                <button type="submit" style={{ ...btnStyle('#27ae60'), marginTop: 8 }}>{t.add}</button>
                            </form>
                        </details>

                        <details style={{ marginBottom: 12 }}>
                            <summary style={{ cursor: 'pointer', fontWeight: 'bold', color: '#27ae60' }}>+ {t.add} {t.screenwriters}</summary>
                            <form onSubmit={handleAddScenarist}>
                                <PersonFormFields person={newScenarist} onChange={setNewScenarist} inputStyle={inputStyle} t={t} />
                                <button type="submit" style={{ ...btnStyle('#27ae60'), marginTop: 8 }}>{t.add}</button>
                            </form>
                        </details>

                        <details style={{ marginBottom: 12 }}>
                            <summary style={{ cursor: 'pointer', fontWeight: 'bold', color: '#27ae60' }}>+ {t.add} {t.producers}</summary>
                            <form onSubmit={handleAddProducator}>
                                <PersonFormFields person={newProducator} onChange={setNewProducator} inputStyle={inputStyle} t={t} showCompany />
                                <button type="submit" style={{ ...btnStyle('#27ae60'), marginTop: 8 }}>{t.add}</button>
                            </form>
                        </details>

                        <details>
                            <summary style={{ cursor: 'pointer', fontWeight: 'bold', color: '#27ae60' }}>+ {t.add} {t.actors}</summary>
                            <form onSubmit={handleAddActor}>
                                <PersonFormFields person={newActor} onChange={setNewActor} inputStyle={inputStyle} t={t} />
                                <button type="submit" style={{ ...btnStyle('#27ae60'), marginTop: 8 }}>{t.add}</button>
                            </form>
                        </details>
                    </div>
                )}

                {/* ACTORI - doar manager */}
                {user.role === 'manager' && <div style={sectionStyle}>
                    <h2 style={{ marginTop: 0 }}>{t.actors}</h2>
                    <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                        <input style={inputStyle} placeholder={t.searchActorPlaceholder} value={actorSearch}
                            onChange={e => setActorSearch(e.target.value)}
                            onKeyDown={e => e.key === 'Enter' && handleSearchActor()} />
                        <button style={btnStyle('#3498db')} onClick={handleSearchActor}>{t.search}</button>
                        <button style={btnStyle('#7f8c8d')} onClick={() => { setActorSearch(''); loadActors(); }}>Reset</button>
                    </div>

                    {/* Carduri actori cu foto + filmele lor (manager) */}
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 16 }}>
                            {filteredActors.map(a => (
                                <div key={a.id} style={{ border: '1px solid #dee2e6', borderRadius: 8, padding: 12, width: 180, textAlign: 'center', cursor: 'pointer' }}
                                    onClick={async () => {
                                        const filmActorList = await apiService.getFilmsByActor(a.id).catch(() => []);
                                        const filmNames = safeArray(filmActorList).map(fa => {
                                            const f = movies.find(m => m.id === fa.idFilm);
                                            return f ? `${f.titlu} (${fa.rol ?? '-'})` : `Film #${fa.idFilm}`;
                                        });
                                        setDetailActorFilme({ actor: a, filme: filmNames });
                                    }}>
                                    {a.foto
                                        ? <img src={a.foto} alt={a.nume} style={{ width: 60, height: 60, borderRadius: '50%', objectFit: 'cover', marginBottom: 8 }} />
                                        : <div style={{ width: 60, height: 60, borderRadius: '50%', background: '#dee2e6', margin: '0 auto 8px' }} />}
                                    <div><strong>{a.prenume} {a.nume}</strong></div>
                                    <div style={{ fontSize: 12, color: '#666' }}>{a.nationalitate}</div>
                                </div>
                            ))}
                            {filteredActors.length === 0 && <span style={{ color: '#999' }}>Niciun actor.</span>}
                    </div>
                </div>}

                {/* REGIZORI (MANAGER) */}
                {user.role === 'manager' && (
                    <div style={sectionStyle}>
                        <h2 style={{ marginTop: 0 }}>{t.directors}</h2>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 16 }}>
                            {regizori.map(r => (
                                <div key={r.id} style={{ border: '1px solid #dee2e6', borderRadius: 8, padding: 12, width: 180, textAlign: 'center', cursor: 'pointer' }}
                                    onClick={() => {
                                        const filme = movies.filter(m => m.regizorId === r.id).map(m => m.titlu);
                                        setDetailRegizorFilme({ regizor: r, filme });
                                    }}>
                                    {r.foto
                                        ? <img src={r.foto} alt={r.nume} style={{ width: 60, height: 60, borderRadius: '50%', objectFit: 'cover', marginBottom: 8 }} />
                                        : <div style={{ width: 60, height: 60, borderRadius: '50%', background: '#dee2e6', margin: '0 auto 8px' }} />}
                                    <div><strong>{r.prenume} {r.nume}</strong></div>
                                    <div style={{ fontSize: 12, color: '#666' }}>{r.nationalitate}</div>
                                    <div style={{ fontSize: 11, color: '#3498db', marginTop: 4 }}>
                                        {movies.filter(m => m.regizorId === r.id).length} film(e)
                                    </div>
                                </div>
                            ))}
                            {regizori.length === 0 && <span style={{ color: '#999' }}>Niciun regizor.</span>}
                        </div>
                    </div>
                )}

                {/* SCENARISTI (MANAGER) */}
                {user.role === 'manager' && (
                    <div style={sectionStyle}>
                        <h2 style={{ marginTop: 0 }}>{t.screenwriters}</h2>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 16 }}>
                            {scenaristi.map(s => (
                                <div key={s.id} style={{ border: '1px solid #dee2e6', borderRadius: 8, padding: 12, width: 180, textAlign: 'center', cursor: 'pointer' }}
                                    onClick={() => {
                                        const filme = movies.filter(m => m.scenaristId === s.id).map(m => m.titlu);
                                        setDetailScenaristFilme({ scenarist: s, filme });
                                    }}>
                                    {s.foto
                                        ? <img src={s.foto} alt={s.nume} style={{ width: 60, height: 60, borderRadius: '50%', objectFit: 'cover', marginBottom: 8 }} />
                                        : <div style={{ width: 60, height: 60, borderRadius: '50%', background: '#dee2e6', margin: '0 auto 8px' }} />}
                                    <div><strong>{s.prenume} {s.nume}</strong></div>
                                    <div style={{ fontSize: 12, color: '#666' }}>{s.nationalitate}</div>
                                    <div style={{ fontSize: 11, color: '#3498db', marginTop: 4 }}>
                                        {movies.filter(m => m.scenaristId === s.id).length} film(e)
                                    </div>
                                </div>
                            ))}
                            {scenaristi.length === 0 && <span style={{ color: '#999' }}>Niciun scenarist.</span>}
                        </div>
                    </div>
                )}

            </>)}

            {/* â•â•â• STATISTICI (MANAGER) â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {user.role === 'manager' && (
                <div style={sectionStyle}>
                    <h2 style={{ marginTop: 0 }}>{t.graphStats}</h2>
                    <StatisticsPanel statsRating={statsRating} statsViews={statsViews} movies={movies} lang={lang} />
                </div>
            )}

            {/* â•â•â• ADMINISTRATOR â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {user.role === 'administrator' && (
                <div style={sectionStyle}>
                    <h2 style={{ marginTop: 0 }}>{t.adminUsers}</h2>
                    <div style={{ display: 'flex', gap: 8, marginBottom: 12, flexWrap: 'wrap' }}>
                        <select style={inputStyle} value={filterUserType} onChange={e => setFilterUserType(e.target.value)}>
                            <option value="">Toate tipurile</option>
                            <option value="0">Angajat</option>
                            <option value="1">Manager</option>
                            <option value="2">Administrator</option>
                        </select>
                        <button style={btnStyle('#27ae60')} onClick={handleExportUsers}>{t.exportCsv}</button>
                    </div>
                    <div style={{ overflowX: 'auto' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
                            <thead>
                                <tr style={{ background: '#2c3e50', color: '#fff' }}>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>ID</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Nume</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Email</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Telefon</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Tip</th>
                                    <th style={{ padding: '8px 10px', textAlign: 'left' }}>Actiuni</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredUsers.map((u, i) => (
                                    <tr key={u.id?.userId ?? u.id} style={{ background: i % 2 === 0 ? '#fff' : '#f2f2f2' }}>
                                        <td style={{ padding: '6px 10px' }}>{u.id?.userId ?? u.id}</td>
                                        <td style={{ padding: '6px 10px' }}>{u.name} {u.surname}</td>
                                        <td style={{ padding: '6px 10px' }}>{u.email}</td>
                                        <td style={{ padding: '6px 10px' }}>{u.phone ?? '-'}</td>
                                        <td style={{ padding: '6px 10px' }}>{userTypeName(u.userType)}</td>
                                        <td style={{ padding: '6px 10px', display: 'flex', gap: 4 }}>
                                            <button style={btnStyle('#f39c12')} onClick={() => setEditUser({ ...u, id: u.id?.userId ?? u.id })}>Edit</button>
                                            <button style={btnStyle('#e74c3c')} onClick={() => handleDeleteUser(u.id?.userId ?? u.id)}>Sterge</button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                    <details style={{ marginTop: 16 }}>
                        <summary style={{ cursor: 'pointer', fontWeight: 'bold', color: '#27ae60' }}>+ {t.addUser}</summary>
                        <form onSubmit={handleCreateUser} style={{ display: 'flex', flexWrap: 'wrap', gap: 8, marginTop: 10 }}>
                            <input style={inputStyle} placeholder="Nume" required value={newUser.name} onChange={e => setNewUser(p => ({ ...p, name: e.target.value }))} />
                            <input style={inputStyle} placeholder="Prenume" required value={newUser.surname} onChange={e => setNewUser(p => ({ ...p, surname: e.target.value }))} />
                            <input style={inputStyle} type="email" placeholder="Email" required value={newUser.email} onChange={e => setNewUser(p => ({ ...p, email: e.target.value }))} />
                            <input style={inputStyle} type="password" placeholder="Parola" required value={newUser.password} onChange={e => setNewUser(p => ({ ...p, password: e.target.value }))} />
                            <input style={inputStyle} placeholder="Telefon" value={newUser.phone} onChange={e => setNewUser(p => ({ ...p, phone: e.target.value }))} />
                            <select style={inputStyle} value={newUser.userType} onChange={e => setNewUser(p => ({ ...p, userType: e.target.value }))}>
                                <option value={0}>Angajat</option>
                                <option value={1}>Manager</option>
                                <option value={2}>Administrator</option>
                            </select>
                            <button type="submit" style={btnStyle('#27ae60')}>+ {t.add}</button>
                        </form>
                    </details>
                    <div style={{ marginTop: 24, borderTop: '1px solid #dee2e6', paddingTop: 16 }}>
                        <h4 style={{ marginTop: 0 }}>{t.sendNotif}</h4>
                        <form onSubmit={handleSendNotif} style={{ display: 'flex', gap: 8, flexWrap: 'wrap', alignItems: 'center' }}>
                            <input style={{ ...inputStyle, width: 100 }} type="number" placeholder="ID User" required value={notifUserId} onChange={e => setNotifUserId(e.target.value)} />
                            <select style={inputStyle} value={notifType} onChange={e => setNotifType(e.target.value)}>
                                <option value="EMAIL">Email</option>
                                <option value="SMS">SMS</option>
                                <option value="WHATSAPP">WhatsApp</option>
                            </select>
                            <input style={{ ...inputStyle, width: 300 }} placeholder="Mesaj..." required value={notifMsg} onChange={e => setNotifMsg(e.target.value)} />
                            <button type="submit" style={btnStyle('#e74c3c')}>{t.send}</button>
                        </form>
                    </div>
                </div>
            )}

            {/* â•â•â• MODAL: Detalii Film (regizor + scenarist + actori) â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {detailFilm && (
                <Modal title={`Detalii: ${detailFilm.titlu}`} onClose={() => { setDetailFilm(null); setDetailActors([]); setDetailDirector(null); setDetailScreenwriter(null); setDetailProducer(null); setDetailFilmImages([]); }}>
                    {detailFilmImages.length > 0 && (
                        <div style={{ display: 'flex', gap: 8, marginBottom: 16, flexWrap: 'wrap' }}>
                            {detailFilmImages.map((img, i) => (
                                <img key={i} src={filmImageUrl(img)} alt="" style={{ maxWidth: 200, maxHeight: 120, objectFit: 'cover', borderRadius: 6, border: '1px solid #dee2e6' }} />
                            ))}
                        </div>
                    )}
                    <div style={{ display: 'flex', gap: 20, flexWrap: 'wrap' }}>
                        <div style={{ flex: 1, minWidth: 200 }}>
                            <p><strong>An realizare:</strong> {detailFilm.anRealizare}</p>
                            <p><strong>Tip:</strong> {tipLabel[detailFilm.tipFilm] ?? detailFilm.tipFilm}</p>
                            <p><strong>Categorie:</strong> {detailFilm.categorieFilm}</p>
                            {detailFilm.descriere && <p><strong>Descriere:</strong> {detailFilm.descriere}</p>}
                        </div>

                        {/* Regizor */}
                        <div style={{ flex: 1, minWidth: 160 }}>
                            <strong>Regizor</strong>
                            {detailDirector ? (
                                <div style={{ marginTop: 8, textAlign: 'center' }}>
                                    {detailDirector.foto && <img src={detailDirector.foto} alt="" style={{ width: 50, height: 50, borderRadius: '50%', objectFit: 'cover' }} />}
                                    <div>{detailDirector.prenume} {detailDirector.nume}</div>
                                    <div style={{ fontSize: 12, color: '#666' }}>{detailDirector.nationalitate}</div>
                                </div>
                            ) : <div style={{ color: '#999', fontSize: 13, marginTop: 8 }}>-</div>}
                        </div>

                        {/* Scenarist */}
                        <div style={{ flex: 1, minWidth: 160 }}>
                            <strong>Scenarist</strong>
                            {detailScreenwriter ? (
                                <div style={{ marginTop: 8, textAlign: 'center' }}>
                                    {detailScreenwriter.foto && <img src={detailScreenwriter.foto} alt="" style={{ width: 50, height: 50, borderRadius: '50%', objectFit: 'cover' }} />}
                                    <div>{detailScreenwriter.prenume} {detailScreenwriter.nume}</div>
                                    <div style={{ fontSize: 12, color: '#666' }}>{detailScreenwriter.nationalitate}</div>
                                </div>
                            ) : <div style={{ color: '#999', fontSize: 13, marginTop: 8 }}>-</div>}
                        </div>

                        {/* Producator */}
                        <div style={{ flex: 1, minWidth: 160 }}>
                            <strong>Producator</strong>
                            {detailProducer ? (
                                <div style={{ marginTop: 8, textAlign: 'center' }}>
                                    {detailProducer.foto && <img src={detailProducer.foto} alt="" style={{ width: 50, height: 50, borderRadius: '50%', objectFit: 'cover' }} />}
                                    <div>{detailProducer.prenume} {detailProducer.nume}</div>
                                    <div style={{ fontSize: 12, color: '#666' }}>{detailProducer.companie ?? detailProducer.nationalitate}</div>
                                </div>
                            ) : <div style={{ color: '#999', fontSize: 13, marginTop: 8 }}>-</div>}
                        </div>
                    </div>

                    {/* Actori */}
                    <div style={{ marginTop: 16 }}>
                        <strong>Distributie ({detailActors.length} actori)</strong>
                        {detailActors.length === 0
                            ? <p style={{ color: '#999', fontSize: 13 }}>Niciun actor asociat.</p>
                            : (
                                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 10, marginTop: 10 }}>
                                    {detailActors.map((fa, i) => (
                                        <div key={i} style={{ textAlign: 'center', width: 90 }}>
                                            {fa.actor?.foto
                                                ? <img src={fa.actor.foto} alt="" style={{ width: 48, height: 48, borderRadius: '50%', objectFit: 'cover' }} />
                                                : <div style={{ width: 48, height: 48, borderRadius: '50%', background: '#dee2e6', lineHeight: '48px', fontSize: 18, margin: '0 auto' }}></div>}
                                            <div style={{ fontSize: 12, marginTop: 4 }}>{fa.actor ? `${fa.actor.prenume} ${fa.actor.nume}` : `Actor #${fa.idActor}`}</div>
                                            {fa.rol && <div style={{ fontSize: 11, color: '#3498db' }}>{fa.rol}</div>}
                                        </div>
                                    ))}
                                </div>
                            )}
                    </div>
                </Modal>
            )}

            {/* â•â•â• MODAL: Film Images â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {selectedFilm && (
                <Modal title={`Imagini: ${selectedFilm.titlu}`} onClose={() => { setSelectedFilm(null); setFilmImages([]); }}>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 10, marginBottom: 16 }}>
                        {filmImages.length === 0 && <p style={{ color: '#999' }}>Nicio imagine adaugata.</p>}
                        {filmImages.slice(0, 3).map((img, i) => (
                            <img key={i} src={img.caleImagine} alt={`img-${i}`}
                                style={{ width: 150, height: 100, objectFit: 'cover', borderRadius: 6, border: '1px solid #ddd' }}
                                onError={e => { e.target.src = 'https://via.placeholder.com/150x100?text=No+Image'; }} />
                        ))}
                    </div>
                    <div style={{ display: 'flex', gap: 8 }}>
                        <input style={{ ...inputStyle, flex: 1 }} placeholder="URL imagine noua" value={newImageUrl} onChange={e => setNewImageUrl(e.target.value)} />
                        <button type="button" style={btnStyle('#3498db')} onClick={handleAddImage}>Adauga</button>
                    </div>
                    <p style={{ fontSize: 12, color: '#888', marginTop: 8 }}>Intre 1 si 3 imagini per film sunt afisate.</p>
                </Modal>
            )}

            {/* â•â•â• MODAL: Edit Film â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {editFilm && (
                <Modal title="Edit Editeaza Film" onClose={() => setEditFilm(null)}>
                    <form onSubmit={handleUpdateFilm} style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                        <label>Titlu<input style={inputStyle} required value={editFilm.titlu} onChange={e => setEditFilm(p => ({ ...p, titlu: e.target.value }))} /></label>
                        <label>Descriere<input style={inputStyle} value={editFilm.descriere ?? ''} onChange={e => setEditFilm(p => ({ ...p, descriere: e.target.value }))} /></label>
                        <label>An Realizare<input style={inputStyle} type="number" value={editFilm.anRealizare} onChange={e => setEditFilm(p => ({ ...p, anRealizare: parseInt(e.target.value) }))} /></label>
                        <label>Tip Film
                            <select style={inputStyle} value={editFilm.tipFilm} onChange={e => setEditFilm(p => ({ ...p, tipFilm: e.target.value }))}>
                                {TIP_FILM_OPTIONS.map(o => <option key={o}>{o}</option>)}
                            </select>
                        </label>
                        <label>Categorie
                            <select style={inputStyle} value={editFilm.categorieFilm} onChange={e => setEditFilm(p => ({ ...p, categorieFilm: e.target.value }))}>
                                {CATEGORIE_OPTIONS.map(o => <option key={o}>{o}</option>)}
                            </select>
                        </label>
                        <label>Regizor
                            <select style={inputStyle} value={editFilm.regizorId} onChange={e => setEditFilm(p => ({ ...p, regizorId: parseInt(e.target.value) }))}>
                                {regizori.map(r => <option key={r.id} value={r.id}>{r.prenume} {r.nume}</option>)}
                            </select>
                        </label>
                        <label>Scenarist
                            <select style={inputStyle} value={editFilm.scenaristId} onChange={e => setEditFilm(p => ({ ...p, scenaristId: parseInt(e.target.value) }))}>
                                {scenaristi.map(s => <option key={s.id} value={s.id}>{s.prenume} {s.nume}</option>)}
                            </select>
                        </label>
                        <label>Producator
                            <select style={inputStyle} value={editFilm.producatorId} onChange={e => setEditFilm(p => ({ ...p, producatorId: parseInt(e.target.value) }))}>
                                {producatori.map(p => <option key={p.id} value={p.id}>{p.prenume} {p.nume}</option>)}
                            </select>
                        </label>
                        <div style={{ display: 'flex', gap: 8 }}>
                            <button type="submit" style={btnStyle('#27ae60')}>Salveaza</button>
                            <button type="button" style={btnStyle('#7f8c8d')} onClick={() => setEditFilm(null)}>Anuleaza</button>
                        </div>
                    </form>
                </Modal>
            )}

            {/* â•â•â• MODAL: Edit Actor (nefolosit â€“ pastrat pentru compatibilitate) â•â•â• */}
            {editActor && false && (
                <Modal title="Edit Editeaza Actor" onClose={() => setEditActor(null)}>
                    <form onSubmit={handleUpdateActor} style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                        <label>Nume<input style={inputStyle} required value={editActor.nume} onChange={e => setEditActor(p => ({ ...p, nume: e.target.value }))} /></label>
                        <label>Prenume<input style={inputStyle} required value={editActor.prenume} onChange={e => setEditActor(p => ({ ...p, prenume: e.target.value }))} /></label>
                        <label>An nastere<input style={inputStyle} type="number" value={editActor.anNastere} onChange={e => setEditActor(p => ({ ...p, anNastere: parseInt(e.target.value) }))} /></label>
                        <label>Nationalitate<input style={inputStyle} value={editActor.nationalitate} onChange={e => setEditActor(p => ({ ...p, nationalitate: e.target.value }))} /></label>
                        <label>URL Foto<input style={inputStyle} value={editActor.foto ?? ''} onChange={e => setEditActor(p => ({ ...p, foto: e.target.value }))} /></label>
                        <div style={{ display: 'flex', gap: 8 }}>
                            <button type="submit" style={btnStyle('#27ae60')}>Salveaza</button>
                            <button type="button" style={btnStyle('#7f8c8d')} onClick={() => setEditActor(null)}>Anuleaza</button>
                        </div>
                    </form>
                </Modal>
            )}

            {/* â•â•â• MODAL: Edit User â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {editUser && (
                <Modal title="Edit Editeaza Utilizator" onClose={() => setEditUser(null)}>
                    <form onSubmit={handleUpdateUser} style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                        <label>Nume<input style={inputStyle} required value={editUser.name} onChange={e => setEditUser(p => ({ ...p, name: e.target.value }))} /></label>
                        <label>Prenume<input style={inputStyle} required value={editUser.surname} onChange={e => setEditUser(p => ({ ...p, surname: e.target.value }))} /></label>
                        <label>Email<input style={inputStyle} type="email" required value={editUser.email} onChange={e => setEditUser(p => ({ ...p, email: e.target.value }))} /></label>
                        <label>Telefon<input style={inputStyle} value={editUser.phone ?? ''} onChange={e => setEditUser(p => ({ ...p, phone: e.target.value }))} /></label>
                        <label>Tip Utilizator
                            <select style={inputStyle} value={editUser.userType} onChange={e => setEditUser(p => ({ ...p, userType: parseInt(e.target.value) }))}>
                                <option value={0}>Angajat</option>
                                <option value={1}>Manager</option>
                                <option value={2}>Administrator</option>
                            </select>
                        </label>
                        <p style={{ fontSize: 12, color: '#e74c3c' }}>La salvare, utilizatorul va fi notificat automat prin Email, SMS si WhatsApp.</p>
                        <div style={{ display: 'flex', gap: 8 }}>
                            <button type="submit" style={btnStyle('#27ae60')}>Salveaza + Notifica</button>
                            <button type="button" style={btnStyle('#7f8c8d')} onClick={() => setEditUser(null)}>Anuleaza</button>
                        </div>
                    </form>
                </Modal>
            )}

            {/* â•â•â• MODAL: Regizor â€“ lista filme regizate â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {detailRegizorFilme && (
                <Modal title={`${detailRegizorFilme.regizor.prenume} ${detailRegizorFilme.regizor.nume}`} onClose={() => setDetailRegizorFilme(null)}>
                    {detailRegizorFilme.regizor.foto && (
                        <div style={{ textAlign: 'center', marginBottom: 12 }}>
                            <img src={detailRegizorFilme.regizor.foto} alt="" style={{ width: 80, height: 80, borderRadius: '50%', objectFit: 'cover' }} />
                        </div>
                    )}
                    <p><strong>Nationalitate:</strong> {detailRegizorFilme.regizor.nationalitate}</p>
                    <p><strong>Filme regizate ({detailRegizorFilme.filme.length}):</strong></p>
                    <ul style={{ marginTop: 4 }}>
                        {detailRegizorFilme.filme.length === 0
                            ? <li style={{ color: '#999' }}>Niciun film.</li>
                            : detailRegizorFilme.filme.map((titlu, i) => <li key={i}>{titlu}</li>)}
                    </ul>
                </Modal>
            )}

            {/* â•â•â• MODAL: Scenarist â€“ lista filme â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {detailScenaristFilme && (
                <Modal title={`${detailScenaristFilme.scenarist.prenume} ${detailScenaristFilme.scenarist.nume}`} onClose={() => setDetailScenaristFilme(null)}>
                    {detailScenaristFilme.scenarist.foto && (
                        <div style={{ textAlign: 'center', marginBottom: 12 }}>
                            <img src={detailScenaristFilme.scenarist.foto} alt="" style={{ width: 80, height: 80, borderRadius: '50%', objectFit: 'cover' }} />
                        </div>
                    )}
                    <p><strong>Nationalitate:</strong> {detailScenaristFilme.scenarist.nationalitate}</p>
                    <p><strong>Filme cu scenariu ({detailScenaristFilme.filme.length}):</strong></p>
                    <ul style={{ marginTop: 4 }}>
                        {detailScenaristFilme.filme.length === 0
                            ? <li style={{ color: '#999' }}>Niciun film.</li>
                            : detailScenaristFilme.filme.map((titlu, i) => <li key={i}>{titlu}</li>)}
                    </ul>
                </Modal>
            )}

            {/* â•â•â• MODAL: Actor â€“ lista filme â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â• */}
            {detailActorFilme && (
                <Modal title={`${detailActorFilme.actor.prenume} ${detailActorFilme.actor.nume}`} onClose={() => setDetailActorFilme(null)}>
                    {detailActorFilme.actor.foto && (
                        <div style={{ textAlign: 'center', marginBottom: 12 }}>
                            <img src={detailActorFilme.actor.foto} alt="" style={{ width: 80, height: 80, borderRadius: '50%', objectFit: 'cover' }} />
                        </div>
                    )}
                    <p><strong>Nationalitate:</strong> {detailActorFilme.actor.nationalitate}</p>
                    <p><strong>Filme in care a jucat ({detailActorFilme.filme.length}):</strong></p>
                    <ul style={{ marginTop: 4 }}>
                        {detailActorFilme.filme.length === 0
                            ? <li style={{ color: '#999' }}>Niciun film.</li>
                            : detailActorFilme.filme.map((f, i) => <li key={i}>{f}</li>)}
                    </ul>
                </Modal>
            )}
        </div>
    );
};
