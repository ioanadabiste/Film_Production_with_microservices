package view;

import presenter.FilmDetailsPresenter;
import presenter.IFilmDetailsView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class FilmDetailsDialog extends JDialog implements IFilmDetailsView {

    private JLabel lblTitlu;
    private JLabel lblAn, lblTip, lblCategorie, lblRegizor, lblScenarist;
    private JTextArea txtDescriere;
    private JLabel lblActori;
    private JPanel panelImagini;

    public FilmDetailsDialog(Frame parent, FilmDetailsPresenter presenter) {
        super(parent, "Detalii Film", true);
        initializeUI();
        presenter.setView(this);      // Presenter populează View-ul
        setSize(800, 600);
        setLocationRelativeTo(parent);
    }


    @Override
    public void setTitlu(String titlu) {
        lblTitlu.setText(titlu);
        setTitle("Detalii Film - " + titlu);
    }

    @Override public void setAnRealizare(String an)       { lblAn.setText(an); }
    @Override public void setTipFilm(String tip)          { lblTip.setText(tip); }
    @Override public void setCategorieFilm(String cat)    { lblCategorie.setText(cat); }
    @Override public void setDescriere(String descriere)  { txtDescriere.setText(descriere); }

    @Override
    public void setRegizor(String numeComplet, String an, String nat) {
        lblRegizor.setText(numeComplet + " (n. " + an + ", " + nat + ")");
    }

    @Override
    public void setScenarist(String numeComplet, String an, String nat) {
        lblScenarist.setText(numeComplet + " (n. " + an + ", " + nat + ")");
    }

    @Override
    public void setActori(String[] numeActori) {
        StringBuilder sb = new StringBuilder("<html>");
        if (numeActori.length == 0) {
            sb.append("Niciun actor adăugat");
        } else {
            for (String nume : numeActori) sb.append("• ").append(nume).append("<br>");
        }
        sb.append("</html>");
        lblActori.setText(sb.toString());
    }

    @Override
    public void setImagini(String[] caiImagini) {
        panelImagini.removeAll();
        for (String cale : caiImagini) {
            try {
                File f = new File(cale);
                if (f.exists()) {
                    Image img = ImageIO.read(f);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                        JLabel lbl = new JLabel(new ImageIcon(scaled));
                        lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                        panelImagini.add(lbl);
                    }
                } else {
                    panelImagini.add(placeholder("Imagine lipsă", Color.RED));
                }
            } catch (Exception e) {
                panelImagini.add(placeholder("Eroare imagine", Color.RED));
            }
        }
        panelImagini.revalidate();
        panelImagini.repaint();
    }


    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblTitlu = new JLabel("");
        lblTitlu.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitlu.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblTitlu);
        panelPrincipal.add(Box.createVerticalStrut(10));

        JPanel panelInfo = new JPanel(new GridLayout(0, 2, 10, 5));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Informații Generale"));
        panelInfo.add(new JLabel("An Realizare:"));  panelInfo.add(lblAn        = new JLabel());
        panelInfo.add(new JLabel("Tip Film:"));      panelInfo.add(lblTip       = new JLabel());
        panelInfo.add(new JLabel("Categorie:"));     panelInfo.add(lblCategorie = new JLabel());
        panelInfo.add(new JLabel("Regizor:"));       panelInfo.add(lblRegizor   = new JLabel());
        panelInfo.add(new JLabel("Scenarist:"));     panelInfo.add(lblScenarist = new JLabel());
        panelPrincipal.add(panelInfo);
        panelPrincipal.add(Box.createVerticalStrut(10));

        JPanel panelDescriere = new JPanel(new BorderLayout());
        panelDescriere.setBorder(BorderFactory.createTitledBorder("Descriere"));
        txtDescriere = new JTextArea(3, 40);
        txtDescriere.setEditable(false);
        txtDescriere.setLineWrap(true);
        txtDescriere.setWrapStyleWord(true);
        panelDescriere.add(new JScrollPane(txtDescriere), BorderLayout.CENTER);
        panelPrincipal.add(panelDescriere);
        panelPrincipal.add(Box.createVerticalStrut(10));

        JPanel panelActori = new JPanel(new BorderLayout());
        panelActori.setBorder(BorderFactory.createTitledBorder("Distribuție Actori"));
        lblActori = new JLabel();
        panelActori.add(lblActori, BorderLayout.CENTER);
        panelPrincipal.add(panelActori);
        panelPrincipal.add(Box.createVerticalStrut(10));

        panelImagini = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelImagini.setBorder(BorderFactory.createTitledBorder("Imagini din Film"));
        JScrollPane scrollImagini = new JScrollPane(panelImagini);
        scrollImagini.setPreferredSize(new Dimension(750, 200));
        panelPrincipal.add(scrollImagini);

        add(new JScrollPane(panelPrincipal), BorderLayout.CENTER);

        JPanel panelButon = new JPanel();
        JButton btnInchide = new JButton("Închide");
        btnInchide.addActionListener(e -> dispose());
        panelButon.add(btnInchide);
        add(panelButon, BorderLayout.SOUTH);
    }

    private JLabel placeholder(String text, Color culoare) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(200, 150));
        lbl.setBorder(BorderFactory.createLineBorder(culoare, 1));
        return lbl;
    }
}