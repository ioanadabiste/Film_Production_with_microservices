package services.export;

import domain.Film;
import services.config.ExportMetadataSingleton;

import java.util.List;

public class CsvExportStrategy implements FilmExportStrategy {

    @Override
    public String export(List<Film> filme) {
        String meta = ExportMetadataSingleton.getInstance().getExportHeader();
        StringBuilder sb = new StringBuilder("# ").append(meta).append("\n");
        sb.append("id,titlu,anRealizare,tipFilm,categorieFilm,regizorId,scenaristId,producatorId\n");
        for (Film f : filme) {
            sb.append(f.getId()).append(",")
                    .append(f.getTitlu()).append(",")
                    .append(f.getAnRealizare()).append(",")
                    .append(f.getTipFilm()).append(",")
                    .append(f.getCategorieFilm()).append(",")
                    .append(f.getRegizorId()).append(",")
                    .append(f.getScenaristId()).append(",")
                    .append(f.getProducatorId()).append("\n");
        }
        return sb.toString();
    }

    @Override public String getContentType()   { return "text/csv"; }
    @Override public String getFileExtension() { return "csv"; }
}