package gbrstd.clinica.controller;

import gbrstd.clinica.model.Exame;
import gbrstd.clinica.service.ExameService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@Controller
@RequestMapping("/exame")
public class ExameController {

    private final ExameService exameService;

    public ExameController(ExameService exameService) {
        this.exameService = exameService;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadExame(@PathVariable Long id) {
        Exame exame = exameService.findById(id);
        final File file = new File(exame.getDocumentPath());
        final String fileName = exame.getFileName();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(new FileSystemResource(file));
    }

}
