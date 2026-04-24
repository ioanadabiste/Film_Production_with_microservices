package view;

import model.Actor;
import model.Regizor;
import model.Scenarist;
import model.repository.*;
import presenter.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class MainView extends JFrame {

    private PersonaView actorView;
    private PersonaView regizorView;
    private PersonaView scenaristView;
    private FilmView    filmView;

    private final MainPresenter mainPresenter;
    private final Connection    connection;

    public MainView(MainPresenter mainPresenter, Connection connection) {
        super("Sistem Management Producție Filme");
        this.mainPresenter = mainPresenter;
        this.connection    = connection;
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // --- Construiește Presenter-ele cu factory lambda ---
        PersonaPresenter<Actor> actorPresenter =
                new PersonaPresenter<>(mainPresenter.getActorRepo(),
                        args -> new Actor((String) args[0], (String) args[1],
                                (int)    args[2], (String) args[3]));

        PersonaPresenter<Regizor> regizorPresenter =
                new PersonaPresenter<>(mainPresenter.getRegizorRepo(),
                        args -> new Regizor((String) args[0], (String) args[1],
                                (int)    args[2], (String) args[3]));

        PersonaPresenter<Scenarist> scenaristPresenter =
                new PersonaPresenter<>(mainPresenter.getScenaristRepo(),
                        args -> new Scenarist((String) args[0], (String) args[1],
                                (int)    args[2], (String) args[3]));

        FilmPresenter filmPresenter = mainPresenter.getFilmPresenter();

        actorView     = new PersonaView("Actor");
        regizorView   = new PersonaView("Regizor");
        scenaristView = new PersonaView("Scenarist");
        filmView      = new FilmView();

        actorPresenter.setView(actorView);
        regizorPresenter.setView(regizorView);
        scenaristPresenter.setView(scenaristView);
        filmPresenter.setView(filmView);

        // --- Injectează Presenter în View
        actorView.setPresenter(actorPresenter);
        regizorView.setPresenter(regizorPresenter);
        scenaristView.setPresenter(scenaristPresenter);
        filmView.setPresenter(filmPresenter);

        // --- Încarcă datele inițiale ---
        actorPresenter.incarcaToate();
        regizorPresenter.incarcaToate();
        scenaristPresenter.incarcaToate();
        filmPresenter.incarcaToate();

        // --- Tab-uri ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Actori",     actorView);
        tabbedPane.addTab("Regizori",   regizorView);
        tabbedPane.addTab("Scenariști", scenaristView);
        tabbedPane.addTab("Filme",      filmView);

        // listener fără logică — doar apel spre Presenter
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            switch (idx) {
                case 0: actorPresenter.incarcaToate();     break;
                case 1: regizorPresenter.incarcaToate();   break;
                case 2: scenaristPresenter.incarcaToate(); break;
                case 3: filmPresenter.incarcaToate();      break;
            }
        });

        add(tabbedPane);

        // --- Meniu ---
        JMenuBar menuBar     = new JMenuBar();
        JMenu menuFisier     = new JMenu("Fișier");
        JMenuItem itemIesire = new JMenuItem("Ieșire");
        itemIesire.addActionListener(e -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        });
        menuFisier.add(itemIesire);

        JMenu menuAjutor     = new JMenu("Ajutor");
        JMenuItem itemDespre = new JMenuItem("Despre");
        itemDespre.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Sistem Management Producție Filme\nVersiune 1.0",
                        "Despre", JOptionPane.INFORMATION_MESSAGE));
        menuAjutor.add(itemDespre);

        menuBar.add(menuFisier);
        menuBar.add(menuAjutor);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseConfig dbConfig   = DatabaseManager.getConnectionWrapper(false);
                Connection     connection = dbConfig.getConnection();

                if (connection == null) {
                    JOptionPane.showMessageDialog(null,
                            "Eroare Conexiune DB!", "Eroare", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                FilmRepository      filmRepo      = new FilmRepository(connection);
                ActorRepository     actorRepo     = new ActorRepository(connection);
                RegizorRepository   regizorRepo   = new RegizorRepository(connection);
                ScenaristRepository scenaristRepo = new ScenaristRepository(connection);

                MainPresenter mainPresenter = new MainPresenter(
                        filmRepo, actorRepo, regizorRepo, scenaristRepo);

                new MainView(mainPresenter, connection).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}