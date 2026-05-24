package services.export;

import domain.Film;
import java.util.List;

public interface FilmExportStrategy {
    String export(List<Film> filme);
    String getContentType();
    String getFileExtension();
}