package plugins.impl;

import model.Film;
import plugins.StatisticsPlugin;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilmeDupaTipStatistic implements StatisticsPlugin {

    @Override
    public String getName() { return "Distributie Filme dupa Tip"; }

    @Override
    public String calculate(List<Film> filme) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DISTRIBUTIE DUPA TIP ===\n");

        Map<String, Long> grouped = filme.stream()
                .filter(f -> f.getTipFilm() != null)
                .collect(Collectors.groupingBy(
                        f -> f.getTipFilm().getDisplayName(),
                        Collectors.counting()
                ));

        grouped.forEach((tip, count) ->
                sb.append("- ").append(tip)
                        .append(": ").append(count)
                        .append(" filme\n"));

        return sb.toString();
    }
}