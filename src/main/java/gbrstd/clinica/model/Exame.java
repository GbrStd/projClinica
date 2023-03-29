package gbrstd.clinica.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Exame implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(nullable = false)
    private String documentPath;

    @Column
    private String fileName;

    public void setDocumentPath(String documentPath) {
        if (this.documentPath != null) {
            deleteFile();
        }
        this.documentPath = documentPath;
    }

    @PostRemove
    private void deleteFile() {
        File file = new File(documentPath);
        if (file.exists()) {
            file.delete();
        }
    }

    @PrePersist
    @PreUpdate
    private void setFileName() {
        this.fileName = documentPath.substring(documentPath.lastIndexOf(File.separator) + 1);
    }

}
