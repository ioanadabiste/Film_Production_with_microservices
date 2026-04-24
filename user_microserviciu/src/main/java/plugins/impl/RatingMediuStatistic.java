package plugins.impl;

import model.Film;
import plugins.StatisticsPlugin;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RatingMediuStatistic implements StatisticsPlugin {

    @Override
    public String getName() { return "Numar Filme pe Categorie"; }

    @Override
    public String calculate(List<Film> filme) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FILME PE CATEGORIE ===\n");

        Map<String, Long> grouped = filme.stream()
                .filter(f -> f.getCategorieFilm() != null)
                .collect(Collectors.groupingBy(
                        f -> f.getCategorieFilm().getDisplayName(),
                        Collectors.counting()
                ));

        grouped.forEach((cat, count) ->
                sb.append("- ").append(cat)
                        .append(": ").append(count)
                        .append(" filme\n"));

        return sb.toString();
    }
}