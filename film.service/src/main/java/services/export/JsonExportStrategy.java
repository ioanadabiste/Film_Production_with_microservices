package services.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Film;
import services.config.ExportMetadataSingleton;

import java.util.List;

public class JsonExportStrategy implements FilmExportStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String export(List<Film> filme) {
        try {
            ObjectNode root = mapper.createObjectNode();
            root.put("meta", ExportMetadataSingleton.getInstance().getExportHeader());
            root.set("filme", mapper.valueToTree(filme));
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override public String getContentType()   { return "application/json"; }
    @Override public String getFileExtension() { return "json"; }
}