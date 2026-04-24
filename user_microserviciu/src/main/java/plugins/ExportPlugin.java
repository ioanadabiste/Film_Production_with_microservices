package plugins;

import model.Film;
import java.util.List;

public interface ExportPlugin {
    String getName();
    String getExtension();
    String export(List<Film> filme);
}