package view;

import presenter.IPersonaView;
import presenter.PersonaPresenterInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonaView extends JPanel implements IPersonaView {

    private PersonaPresenterInterface presenter;
    private final String entityName;

    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JTextField txtNume, txtPrenume, txtAnNastere, txtNationalitate;
    private JButton btnAdauga, btnActualizeaza, btnSterge, btnCurata;
    private String idSelectat = null;

    public PersonaView(String entityName) {
        this.entityName = entityName;
        initializeUI();
    }

    public void setPresenter(PersonaPresenterInterface presenter) {
        this.presenter = presenter;
    }

    // --- Getteri ---
    @Override public String getNume()          { return txtNume.getText().trim(); }
    @Override public String getPrenume()       { return txtPrenume.getText().trim(); }
    @Override public String getAnNastere()     { return txtAnNastere.getText().trim(); }
    @Override public String getNationalitate() { return txtNationalitate.getText().trim(); }
    @Override public String getIdSelectat()    { return idSelectat; }

    // --- Setteri ---
    @Override public void setNume(String v)         { txtNume.setText(v); }
    @Override public void setPrenume(String v)       { txtPrenume.setText(v); }
    @Override public void setAnNastere(String v)     { txtAnNastere.setText(v); }
    @Override public void setNationalitate(String v) { txtNationalitate.setText(v); }
    @Override public void setIdSelectat(String id)   { this.idSelectat = id; }

    @Override
    public void afiseazaEntitati(List<String[]> randuri) {
        modelTabel.setRowCount(0);
        for (String[] rand : randuri) modelTabel.addRow(rand);
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
        idSelectat = null;
        txtNume.setText("");
        txtPrenume.setText("");
        txtAnNastere.setText("");
        txtNationalitate.setText("");
        tabel.clearSelection();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormular = new JPanel(new GridBagLayout());
        panelFormular.setBorder(BorderFactory.createTitledBorder("Detalii " + entityName));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelFormular.add(new JLabel("Nume:"), gbc);
        gbc.gridx = 1; txtNume = new JTextField(20); panelFormular.add(txtNume, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelFormular.add(new JLabel("Prenume:"), gbc);
        gbc.gridx = 1; txtPrenume = new JTextField(20); panelFormular.add(txtPrenume, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelFormular.add(new JLabel("An Naștere:"), gbc);
        gbc.gridx = 1; txtAnNastere = new JTextField(20); panelFormular.add(txtAnNastere, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelFormular.add(new JLabel("Naționalitate:"), gbc);
        gbc.gridx = 1; txtNationalitate = new JTextField(20); panelFormular.add(txtNationalitate, gbc);

        JPanel panelButoane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdauga       = new JButton("Adaugă");
        btnActualizeaza = new JButton("Actualizează");
        btnSterge       = new JButton("Șterge");
        btnCurata       = new JButton("Curăță");
        panelButoane.add(btnAdauga);
        panelButoane.add(btnActualizeaza);
        panelButoane.add(btnSterge);
        panelButoane.add(btnCurata);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormular.add(panelButoane, gbc);
        add(panelFormular, BorderLayout.NORTH);

        String[] coloane = {"ID", "Nume", "Prenume", "An Naștere", "Naționalitate"};
        modelTabel = new DefaultTableModel(coloane, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        tabel.getColumnModel().getColumn(0).setWidth(0);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        btnAdauga.addActionListener(e       -> presenter.adauga());
        btnActualizeaza.addActionListener(e -> presenter.actualizeaza());
        btnSterge.addActionListener(e       -> presenter.sterge());
        btnCurata.addActionListener(e       -> curataFormular());

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rand = tabel.getSelectedRow();
                if (rand >= 0)
                    presenter.selecteaza((String) modelTabel.getValueAt(rand, 0));
            }
        });
    }
}