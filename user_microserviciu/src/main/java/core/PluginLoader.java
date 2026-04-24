package core;

import plugins.ExportPlugin;
import plugins.StatisticsPlugin;
import java.util.ArrayList;
import java.util.List;

// NUCLEUL Microkernel - nu importa niciodata implementarile direct!
// Le descopera dinamic prin numele clasei
public class PluginLoader {

    // PRIVATA - descopera clasele de export disponibile
    private List<Class<? extends ExportPlugin>> discoverExportPlugins() {
        List<Class<? extends ExportPlugin>> discovered = new ArrayList<>();

        // In productie acestea ar fi citite dintr-un fisier de configurare
        // sau scanate dintr-un folder de plugin-uri
        String[] pluginClassNames = {
                "plugins.impl.export.CsvExportPlugin",
                "plugins.impl.export.DocExportPlugin"
        };

        for (String className : pluginClassNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (ExportPlugin.class.isAssignableFrom(clazz)) {
                    discovered.add(clazz.asSubclass(ExportPlugin.class));
                    System.out.println("[PluginLoader] Export plugin gasit: " + className);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("[PluginLoader] Plugin negasit: " + className);
            }
        }
        return discovered;
    }

    // PRIVATA - descopera clasele de statistici disponibile
    private List<Class<? extends StatisticsPlugin>> discoverStatisticsPlugins() {
        List<Class<? extends StatisticsPlugin>> discovered = new ArrayList<>();

        String[] pluginClassNames = {
                "plugins.impl.statistics.TopFilmeStatistic",
                "plugins.impl.statistics.FilmeDupaTipStatistic",
                "plugins.impl.statistics.RatingMediuStatistic"
        };

        for (String className : pluginClassNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (StatisticsPlugin.class.isAssignableFrom(clazz)) {
                    discovered.add(clazz.asSubclass(StatisticsPlugin.class));
                    System.out.println("[PluginLoader] Statistics plugin gasit: " + className);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("[PluginLoader] Plugin negasit: " + className);
            }
        }
        return discovered;
    }

    // PUBLICA - instantiaza plugin-urile de export
    public List<ExportPlugin> loadExportPlugins() {
        List<ExportPlugin> plugins = new ArrayList<>();
        for (Class<? extends ExportPlugin> clazz : discoverExportPlugins()) {
            try {
                plugins.add(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println("[PluginLoader] Eroare: " + e.getMessage());
            }
        }
        return plugins;
    }

    // PUBLICA - instantiaza plugin-urile de statistici
    public List<StatisticsPlugin> loadStatisticsPlugins() {
        List<StatisticsPlugin> plugins = new ArrayList<>();
        for (Class<? extends StatisticsPlugin> clazz : discoverStatisticsPlugins()) {
            try {
                plugins.add(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println("[PluginLoader] Eroare: " + e.getMessage());
            }
        }
        return plugins;
    }
}