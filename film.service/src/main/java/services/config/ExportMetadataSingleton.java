package services.config;

import java.time.LocalDate;

/**
 * Singleton Pattern (GoF) – o singura instanta pentru metadatele exporturilor
 * (nume organizatie, antet) folosita de toate strategiile de export.
 */
public final class ExportMetadataSingleton {

    private static volatile ExportMetadataSingleton instance;

    private final String organizationName;

    private ExportMetadataSingleton() {
        this.organizationName = "Casa de Productie Film";
    }

    public static ExportMetadataSingleton getInstance() {
        if (instance == null) {
            synchronized (ExportMetadataSingleton.class) {
                if (instance == null) {
                    instance = new ExportMetadataSingleton();
                }
            }
        }
        return instance;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getExportHeader() {
        return organizationName + " - export " + LocalDate.now();
    }
}
