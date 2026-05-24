package services.export;

import domain.Film;
import services.config.ExportMetadataSingleton;

import java.util.List;

/**
 * Export simplu în format text compatibil Word (.doc).
 */
public class DocExportStrategy implements FilmExportStrategy {

    @Override
    public String export(List<Film> filme) {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista filme - ").append(ExportMetadataSingleton.getInstance().getOrganizationName()).append("\r\n");
        sb.append(ExportMetadataSingleton.getInstance().getExportHeader()).append("\r\n");
        sb.append("================================\r\n\r\n");
        for (Film f : filme) {
            sb.append("Titlu: ").append(f.getTitlu()).append("\r\n");
            sb.append("An: ").append(f.getAnRealizare()).append("\r\n");
            sb.append("Tip: ").append(f.getTipFilm()).append("\r\n");
            sb.append("Categorie: ").append(f.getCategorieFilm()).append("\r\n");
            sb.append("Regizor ID: ").append(f.getRegizorId()).append("\r\n");
            sb.append("Scenarist ID: ").append(f.getScenaristId()).append("\r\n");
            sb.append("Producator ID: ").append(f.getProducatorId()).append("\r\n");
            if (f.getDescriere() != null) {
                sb.append("Descriere: ").append(f.getDescriere()).append("\r\n");
            }
            sb.append("--------------------------------\r\n");
        }
        return sb.toString();
    }

    @Override
    public String getContentType() {
        return "application/msword";
    }

    @Override
    public String getFileExtension() {
        return "doc";
    }
}
