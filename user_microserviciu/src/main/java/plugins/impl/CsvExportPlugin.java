package plugins.impl;

import model.Film;
import plugins.ExportPlugin;
import java.util.List;

public class CsvExportPlugin implements ExportPlugin {

    @Override
    public String getName() { return "CSV Export"; }

    @Override
    public String getExtension() { return "csv"; }

    @Override
    public String export(List<Film> filme) {
        StringBuilder sb = new StringBuilder();
        sb.append("Id,Titlu,AnRealizare,TipFilm,CategorieFilm\n");
        for (Film f : filme) {
            sb.append(f.getId()).append(",")
                    .append(f.getTitlu()).append(",")
                    .append(f.getAnRealizare()).append(",")
                    .append(f.getTipFilm() != null ? f.getTipFilm().getDisplayName() : "").append(",")
                    .append(f.getCategorieFilm() != null ? f.getCategorieFilm().getDisplayName() : "")
                    .append("\n");
        }
        return sb.toString();
    }
}