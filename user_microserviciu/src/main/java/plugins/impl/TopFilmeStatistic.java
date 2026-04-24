package plugins.impl;

import model.Film;
import plugins.StatisticsPlugin;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TopFilmeStatistic implements StatisticsPlugin {

    @Override
    public String getName() { return "Top 3 Filme (cele mai recente)"; }

    @Override
    public String calculate(List<Film> filme) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TOP 3 FILME DUPA AN ===\n");
        AtomicInteger rank = new AtomicInteger(1);
        filme.stream()
                .sorted(Comparator.comparingInt(Film::getAnRealizare).reversed())
                .limit(3)
                .forEach(f -> sb.append(rank.getAndIncrement()).append(". ")
                        .append(f.getTitlu())
                        .append(" (").append(f.getAnRealizare()).append(")")
                        .append("\n"));
        return sb.toString();
    }
}