package com.rocku.notes.contract;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.swagger.v3.parser.OpenAPIV3Parser;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class NotesOpenApiContractTest {
    @Test
    void contractDefinesNotesCrudAndPrintRoutes() {
        var contractPath = Path.of("../../platform/api-contracts/openapi-notes.yaml").toString();
        var parsed = new OpenAPIV3Parser().readLocation(contractPath, null, null).getOpenAPI();

        assertNotNull(parsed, "openapi-notes.yaml must parse");
        assertTrue(parsed.getPaths().containsKey("/api/notes"));
        assertTrue(parsed.getPaths().containsKey("/api/notes/{id}"));
        assertTrue(parsed.getPaths().containsKey("/print/{id}"));
    }
}