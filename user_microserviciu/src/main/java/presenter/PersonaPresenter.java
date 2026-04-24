package presenter;

import model.Persoana;
import model.repository.PersonaRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersonaPresenter<T extends Persoana>
        implements PersonaPresenterInterface {

    private final PersonaRepository<T> repository;
    private final Function<Object[], T> factory;
    private IPersonaView view;

    public PersonaPresenter(PersonaRepository<T> repository,
                            Function<Object[], T> factory) {
        this.repository = repository;
        this.factory    = factory;
    }

    public void setView(IPersonaView view) {
        this.view = view;
    }

    @Override
    public void adauga() {
        String nume    = view.getNume();
        String prenume = view.getPrenume();
        String anStr   = view.getAnNastere();
        String nat     = view.getNationalitate();

        if (nume.isEmpty() || prenume.isEmpty()) {
            view.afiseazaEroare("Numele și prenumele sunt obligatorii!");
            return;
        }
        try {
            int an = Integer.parseInt(anStr);
            T entitate = factory.apply(new Object[]{nume, prenume, an, nat});
            repository.adauga(entitate);
            incarcaToate();
            view.curataFormular();
            view.afiseazaMesaj("Adăugat cu succes!");
        } catch (NumberFormatException e) {
            view.afiseazaEroare("An naștere invalid!");
        }
    }

    @Override
    public void actualizeaza() {
        String id = view.getIdSelectat();
        if (id == null) { view.afiseazaEroare("Selectați un element din tabel!"); return; }
        try {
            T entitate = repository.getById(id);
            if (entitate != null) {
                entitate.setNume(view.getNume());
                entitate.setPrenume(view.getPrenume());
                entitate.setAnNastere(Integer.parseInt(view.getAnNastere()));
                entitate.setNationalitate(view.getNationalitate());
                repository.actualizeaza(entitate);
                incarcaToate();
                view.curataFormular();
                view.afiseazaMesaj("Actualizat cu succes!");
            }
        } catch (NumberFormatException e) {
            view.afiseazaEroare("An naștere invalid!");
        }
    }

    @Override
    public void sterge() {
        String id = view.getIdSelectat();
        if (id == null) { view.afiseazaEroare("Selectați un element din tabel!"); return; }
        repository.sterge(id);
        incarcaToate();
        view.curataFormular();
        view.afiseazaMesaj("Șters cu succes!");
    }

    @Override
    public void selecteaza(String id) {
        T entitate = repository.getById(id);
        if (entitate != null) {
            view.setIdSelectat(id);
            view.setNume(entitate.getNume());
            view.setPrenume(entitate.getPrenume());
            view.setAnNastere(String.valueOf(entitate.getAnNastere()));
            view.setNationalitate(entitate.getNationalitate());
        }
    }

    @Override
    public void incarcaToate() {
        List<String[]> randuri = repository.getAll().stream()
                .map(p -> new String[]{
                        p.getId(), p.getNume(), p.getPrenume(),
                        String.valueOf(p.getAnNastere()), p.getNationalitate()
                })
                .collect(Collectors.toList());
        view.afiseazaEntitati(randuri);
    }
}