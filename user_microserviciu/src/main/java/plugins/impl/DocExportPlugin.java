package plugins.impl;

import model.Film;
import plugins.ExportPlugin;
import java.util.List;

public class DocExportPlugin implements ExportPlugin {

    @Override
    public String getName() { return "DOC Export"; }

    @Override
    public String getExtension() { return "doc"; }

    @Override
    public String export(List<Film> filme) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RAPORT FILME - CASA DE PRODUCTIE ===\n\n");
        for (Film f : filme) {
            sb.append("Titlu: ").append(f.getTitlu()).append("\n");
            sb.append("An realizare: ").append(f.getAnRealizare()).append("\n");
            sb.append("Tip: ").append(f.getTipFilm() != null ?
                    f.getTipFilm().getDisplayName() : "N/A").append("\n");
            sb.append("Categorie: ").append(f.getCategorieFilm() != null ?
                    f.getCategorieFilm().getDisplayName() : "N/A").append("\n");
            sb.append("Descriere: ").append(f.getDescriere() != null ?
                    f.getDescriere() : "").append("\n");
            sb.append("--------------------------------\n");
        }
        return sb.toString();
    }
}