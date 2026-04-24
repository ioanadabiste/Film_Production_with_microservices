package view;

import presenter.FilmDetailsPresenter;
import presenter.FilmPresenter;
import presenter.IFilmView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilmView extends JPanel implements IFilmView {

    private FilmPresenter presenter;

    private JTable tabelFilme;
    private DefaultTableModel modelTabel;
    private JTextField txtTitlu, txtAn, txtDescriere, txtFiltruAn;
    private JComboBox<String[]> cbRegizor, cbScenarist;
    private JComboBox<String> cbTip, cbCategorie, cbFiltruTip, cbFiltruCategorie;
    private JList<String[]> listActori;
    private DefaultListModel<String[]> modelActori;
    private JTextField txtImagini;
    private String idFilmSelectat = null;

    public FilmView() {
        initializeUI();
    }

    public void setPresenter(FilmPresenter presenter) {
        this.presenter = presenter;
    }

    @Override public String getTitlu()          { return txtTitlu.getText().trim(); }
    @Override public String getAnRealizare()    { return txtAn.getText().trim(); }
    @Override public String getDescriere()      { return txtDescriere.getText().trim(); }
    @Override public String getIdFilmSelectat() { return idFilmSelectat; }

    @Override public String getFiltruTip() {
        String s = (String) cbFiltruTip.getSelectedItem();
        return (s == null || s.isEmpty()) ? null : s;
    }
    @Override public String getFiltruCategorie() {
        String s = (String) cbFiltruCategorie.getSelectedItem();
        return (s == null || s.isEmpty()) ? null : s;
    }
    @Override public String getFiltruAn()       { return txtFiltruAn.getText().trim(); }

    @Override public String getTipFilm()        { return (String) cbTip.getSelectedItem(); }
    @Override public String getCategorieFilm()  { return (String) cbCategorie.getSelectedItem(); }

    @Override public String getRegizorId() {
        String[] sel = (String[]) cbRegizor.getSelectedItem();
        return sel != null ? sel[0] : null;
    }
    @Override public String getScenaristId() {
        String[] sel = (String[]) cbScenarist.getSelectedItem();
        return sel != null ? sel[0] : null;
    }
    @Override public List<String> getActorIdsSelectati() {
        return Arrays.stream(listActori.getSelectedValuesList().toArray(new String[0][]))
                .map(arr -> arr[0])
                .collect(Collectors.toList());
    }
    @Override public List<String> getCaiImagini() {
        String text = txtImagini.getText().trim();
        if (text.isEmpty()) return List.of();
        return Arrays.asList(text.split(";"));
    }

    @Override
    public void afiseazaFilme(List<String[]> randuri) {
        modelTabel.setRowCount(0);
        for (String[] rand : randuri) modelTabel.addRow(rand);
    }

    @Override
    public void populeazaRegizori(List<String[]> optiuni) {
        cbRegizor.removeAllItems();
        for (String[] opt : optiuni) cbRegizor.addItem(opt);
    }

    @Override
    public void populeazaScenaristi(List<String[]> optiuni) {
        cbScenarist.removeAllItems();
        for (String[] opt : optiuni) cbScenarist.addItem(opt);
    }

    @Override
    public void populeazaActori(List<String[]> optiuni) {
        modelActori.clear();
        for (String[] opt : optiuni) modelActori.addElement(opt);
    }

    @Override
    public void afiseazaMesaj(String mesaj) {
        JOptionPane.showMessageDialog(this, mesaj);
    }

    @Override
    public void afiseazaEroare(String eroare) {
        JOptionPane.showMessageDialog(this, eroare, "Eroare", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void curataFormular() {
        idFilmSelectat = null;
        txtTitlu.setText("");
        txtAn.setText("");
        txtDescriere.setText("");
        txtImagini.setText("");
        cbRegizor.setSelectedIndex(-1);
        cbScenarist.setSelectedIndex(-1);
        listActori.clearSelection();
        tabelFilme.clearSelection();
    }

    @Override
    public void deschideDetalii(FilmDetailsPresenter detailsPresenter) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        FilmDetailsDialog dialog = new FilmDetailsDialog(parent, detailsPresenter);
        dialog.setVisible(true);
    }

    @Override
    public void setImaginiSelectate(String[] cai) {
        txtImagini.setText(String.join(";", cai));
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormular = new JPanel(new GridBagLayout());
        panelFormular.setBorder(BorderFactory.createTitledBorder("Detalii Film"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelFormular.add(new JLabel("Titlu:"), gbc);
        gbc.gridx = 1; txtTitlu = new JTextField(20); panelFormular.add(txtTitlu, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelFormular.add(new JLabel("An:"), gbc);
        gbc.gridx = 1; txtAn = new JTextField(20); panelFormular.add(txtAn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelFormular.add(new JLabel("Tip:"), gbc);
        gbc.gridx = 1;
        cbTip = new JComboBox<>(new String[]{"","ARTISTIC","SERIAL"});
        panelFormular.add(cbTip, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelFormular.add(new JLabel("Categorie:"), gbc);
        gbc.gridx = 1;
        cbCategorie = new JComboBox<>(new String[]{"ACTIUNE","DRAMA","COMEDIE","SF","HORROR","ROMANTIC","THRILLER"});
        panelFormular.add(cbCategorie, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelFormular.add(new JLabel("Regizor:"), gbc);
        gbc.gridx = 1;
        cbRegizor = new JComboBox<>();
        cbRegizor.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
                if (value instanceof String[]) setText(((String[]) value)[1]);
                return this;
            }
        });
        panelFormular.add(cbRegizor, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panelFormular.add(new JLabel("Scenarist:"), gbc);
        gbc.gridx = 1;
        cbScenarist = new JComboBox<>();
        cbScenarist.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
                if (value instanceof String[]) setText(((String[]) value)[1]);
                return this;
            }
        });
        panelFormular.add(cbScenarist, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panelFormular.add(new JLabel("Actori:"), gbc);
        gbc.gridx = 1;
        modelActori = new DefaultListModel<>();
        listActori  = new JList<>(modelActori);
        listActori.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listActori.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
                if (value instanceof String[]) setText(((String[]) value)[1]);
                return this;
            }
        });
        panelFormular.add(new JScrollPane(listActori), gbc);

        gbc.gridx = 0; gbc.gridy = 7; panelFormular.add(new JLabel("Imagini:"), gbc);
        gbc.gridx = 1;
        JPanel panelImagini = new JPanel(new BorderLayout(4, 0));
        txtImagini = new JTextField(14);
        txtImagini.setEditable(false);
        JButton btnImagini = new JButton("Alege...");
        panelImagini.add(txtImagini, BorderLayout.CENTER);
        panelImagini.add(btnImagini, BorderLayout.EAST);
        panelFormular.add(panelImagini, gbc);

        gbc.gridx = 0; gbc.gridy = 8; panelFormular.add(new JLabel("Descriere:"), gbc);
        gbc.gridx = 1; txtDescriere = new JTextField(20); panelFormular.add(txtDescriere, gbc);

        JPanel panelButoane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdauga       = new JButton("Adaugă");
        JButton btnActualizeaza = new JButton("Actualizează");
        JButton btnSterge       = new JButton("Șterge");
        JButton btnDetalii      = new JButton("Detalii");
        JButton btnCurata       = new JButton("Curăță");
        panelButoane.add(btnAdauga);
        panelButoane.add(btnActualizeaza);
        panelButoane.add(btnSterge);
        panelButoane.add(btnDetalii);
        panelButoane.add(btnCurata);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        panelFormular.add(panelButoane, gbc);


        JPanel panelFiltre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltre.setBorder(BorderFactory.createTitledBorder("Filtre"));
        cbFiltruTip = new JComboBox<>(new String[]{"","FILM_ARTISTIC","SERIAL"});
        cbFiltruCategorie = new JComboBox<>(new String[]{"","ACTIUNE","DRAMA","COMEDIE","SF","HORROR","ROMANTIC","THRILLER"});
        txtFiltruAn = new JTextField(6);
        JButton btnFiltreaza = new JButton("Filtrează");
        panelFiltre.add(new JLabel("Tip:"));       panelFiltre.add(cbFiltruTip);
        panelFiltre.add(new JLabel("Categorie:")); panelFiltre.add(cbFiltruCategorie);
        panelFiltre.add(new JLabel("An:"));        panelFiltre.add(txtFiltruAn);
        panelFiltre.add(btnFiltreaza);

        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.add(panelFormular, BorderLayout.CENTER);
        panelNord.add(panelFiltre, BorderLayout.SOUTH);
        add(panelNord, BorderLayout.NORTH);

        String[] coloane = {"ID", "Titlu", "An", "Tip", "Categorie"};
        modelTabel = new DefaultTableModel(coloane, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelFilme = new JTable(modelTabel);
        tabelFilme.getColumnModel().getColumn(0).setMinWidth(0);
        tabelFilme.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelFilme.getColumnModel().getColumn(0).setWidth(0);
        add(new JScrollPane(tabelFilme), BorderLayout.CENTER);


        btnAdauga.addActionListener(e       -> presenter.adauga());
        btnActualizeaza.addActionListener(e -> presenter.actualizeaza());
        btnSterge.addActionListener(e       -> presenter.sterge());
        btnDetalii.addActionListener(e      -> presenter.veziDetalii());
        btnFiltreaza.addActionListener(e    -> presenter.filtreaza());
        btnImagini.addActionListener(e      -> presenter.alegeImagini());
        btnCurata.addActionListener(e       -> curataFormular());

        tabelFilme.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rand = tabelFilme.getSelectedRow();
                if (rand >= 0)
                    idFilmSelectat = (String) modelTabel.getValueAt(rand, 0);
            }
        });
    }
}