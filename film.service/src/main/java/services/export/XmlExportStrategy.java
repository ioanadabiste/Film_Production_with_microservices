package services.export;

import domain.Film;
import services.config.ExportMetadataSingleton;

import java.util.List;

public class XmlExportStrategy implements FilmExportStrategy {

    @Override
    public String export(List<Film> filme) {
        String meta = ExportMetadataSingleton.getInstance().getExportHeader();
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!-- ").append(meta).append(" -->\n");
        sb.append("<filme>\n");
        for (Film f : filme) {
            sb.append("  <film>\n")
                    .append("    <id>").append(f.getId()).append("</id>\n")
                    .append("    <titlu>").append(f.getTitlu()).append("</titlu>\n")
                    .append("    <anRealizare>").append(f.getAnRealizare()).append("</anRealizare>\n")
                    .append("    <tipFilm>").append(f.getTipFilm()).append("</tipFilm>\n")
                    .append("    <categorieFilm>").append(f.getCategorieFilm()).append("</categorieFilm>\n")
                    .append("    <regizorId>").append(f.getRegizorId()).append("</regizorId>\n")
                    .append("    <scenaristId>").append(f.getScenaristId()).append("</scenaristId>\n")
                    .append("  </film>\n");
        }
        sb.append("</filme>");
        return sb.toString();
    }

    @Override public String getContentType()   { return "application/xml"; }
    @Override public String getFileExtension() { return "xml"; }
}