package plugins;

import model.Film;
import java.util.List;

public interface StatisticsPlugin {
    String getName();
    String calculate(List<Film> filme);
}