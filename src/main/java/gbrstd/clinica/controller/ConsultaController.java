package gbrstd.clinica.controller;

import gbrstd.clinica.constants.AppConstants;
import gbrstd.clinica.model.Clinica;
import gbrstd.clinica.model.Consulta;
import gbrstd.clinica.model.Exame;
import gbrstd.clinica.service.ClinicaService;
import gbrstd.clinica.service.ConsultaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
public class ConsultaController {

    private static final String MSG_EXAMS_ERROR = "Valide os campos de exames, tenha em mente que os arquivos devem ser PDF";
    private final ConsultaService consultaService;
    private final ClinicaService clinicaService;

    public ConsultaController(ConsultaService consultaService, ClinicaService clinicaService) {
        this.consultaService = consultaService;
        this.clinicaService = clinicaService;
    }

    private static boolean isValidExamDocument(MultipartFile file) {
        return !file.isEmpty() && file.getContentType() != null
                && file.getContentType().toLowerCase(Locale.ROOT).endsWith("pdf")
                && file.getOriginalFilename() != null;
    }

    private static void uploadExamDocument(Exame exame, MultipartFile file) throws IOException {
        String fileName = String.valueOf(System.currentTimeMillis());
        Path path = Paths.get(AppConstants.EXAMS_UPLOAD_DIR + fileName + ".pdf");
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        exame.setDocumentPath(path.toString());
    }

    @GetMapping("/clinica/{clinicaId}/consulta")
    public String listAllConsultas(@PathVariable("clinicaId") Long clinicaId, Model model) {
        model.addAttribute("consultas", clinicaService.findById(clinicaId).getConsultas());
        return "consulta/listConsultas";
    }

    @GetMapping("/consulta/{consultaId}")
    public String showConsulta(@PathVariable("consultaId") Long consultaId, Model model) {
        model.addAttribute("consulta", consultaService.findById(consultaId));
        return "consulta/showConsulta";
    }

    @GetMapping("/clinica/{clinicaId}/consulta/create")
    public String createConsultaForm(@PathVariable("clinicaId") Long clinicaId,
                                     HttpSession session,
                                     Model model) {

        Clinica clinica = clinicaService.findById(clinicaId);
        session.setAttribute("currentClinica", clinica);

        Consulta consulta = new Consulta();

        consulta.setClinica(clinica);

        model.addAttribute("consultaForm", consulta);
        model.addAttribute("especialistas", clinica.getEspecialistas());
        model.addAttribute("pacientes", clinica.getPacientes());
        model.addAttribute("clinicaId", clinicaId);

        return "consulta/createConsultaForm";
    }

    @GetMapping("/consulta/{consultaId}/edit")
    public String editConsultaForm(@PathVariable("consultaId") Long id,
                                   HttpSession session,
                                   Model model) {

        Consulta consulta = consultaService.findById(id);

        session.setAttribute("cacheExams", consulta.getExames());
        session.setAttribute("currentClinica", consulta.getClinica());

        model.addAttribute("consultaForm", consulta);
        model.addAttribute("especialistas", consulta.getClinica().getEspecialistas());
        model.addAttribute("pacientes", consulta.getClinica().getPacientes());
        model.addAttribute("clinicaId", consulta.getClinica().getId());

        return "consulta/editConsultaForm";
    }

    @PostMapping("/consulta/{id}/delete")
    public String deleteConsulta(@PathVariable("id") Long id) {
        Consulta consulta = consultaService.delete(id);
        return "redirect:/clinica/" + consulta.getClinica().getId() + "/consulta";
    }

    @PostMapping("/clinica/{clinicaId}/consulta/create")
    public String createConsultaSubmit(@PathVariable("clinicaId") Long clinicaId,
                                       @ModelAttribute("consultaForm") @Valid Consulta consulta,
                                       BindingResult consultaResult,
                                       @RequestParam(name = "fileUpload", required = false) MultipartFile[] files,
                                       HttpSession session,
                                       SessionStatus status,
                                       Model model) throws IOException {

        consulta.setClinica((Clinica) session.getAttribute("currentClinica"));

        boolean hasErrors = consultaResult.hasErrors();

        Map<Exame, MultipartFile> examsMap = new HashMap<>();
        if (consulta.getExames() != null && files != null) {
            for (int i = 0; i < consulta.getExames().size(); i++) {
                if (i < files.length && isValidExamDocument(files[i])) {
                    final Exame exame = consulta.getExames().get(i);
                    examsMap.put(exame, files[i]);
                } else {
                    model.addAttribute("errorExams", MSG_EXAMS_ERROR);
                    hasErrors = true;
                    break;
                }
            }
        }

        if (hasErrors) {
            model.addAttribute("consultaForm", consulta);
            model.addAttribute("especialistas", consulta.getClinica().getEspecialistas());
            model.addAttribute("pacientes", consulta.getClinica().getPacientes());
            model.addAttribute("clinicaId", clinicaId);

            return "consulta/createConsultaForm";
        }

        if (!examsMap.isEmpty()) {
            for (Map.Entry<Exame, MultipartFile> entry : examsMap.entrySet()) {
                Exame exam = entry.getKey();
                MultipartFile file = entry.getValue();
                uploadExamDocument(exam, file);
            }
        }
        consultaService.save(consulta);

        status.setComplete();
        session.removeAttribute("currentClinica");

        return "redirect:/clinica/" + clinicaId + "/consulta";
    }

    @PostMapping("/consulta/{consultaId}/edit")
    public String editConsultaSubmit(@ModelAttribute("consultaForm") @Valid Consulta consulta,
                                     BindingResult consultaResult,
                                     @RequestParam(name = "fileUpload", required = false) MultipartFile[] files,
                                     HttpSession session,
                                     SessionStatus status,
                                     Model model) throws IOException {

        consulta.setClinica((Clinica) session.getAttribute("currentClinica"));

        List<Exame> cacheExams = (List<Exame>) session.getAttribute("cacheExams");
        if (cacheExams == null) {
            cacheExams = new ArrayList<>();
        }

        boolean hasErrors = consultaResult.hasErrors();

        Map<Exame, MultipartFile> examsMap = new HashMap<>();
        if (consulta.getExames() != null && files != null) {
            for (int i = 0; i < consulta.getExames().size(); i++) {
                Exame exame = consulta.getExames().get(i);
                if (i < cacheExams.size()) {
                    cacheExams.stream()
                            .filter(e -> e.getId().equals(exame.getId()))
                            .findFirst()
                            .ifPresent(e -> exame.setDocumentPath(e.getDocumentPath()));
                }
                MultipartFile file = files[i];
                if (file != null) {
                    if (file.getOriginalFilename() != null
                            && file.getOriginalFilename().isBlank()
                            && exame.getDocumentPath() != null)
                        continue;
                    if (isValidExamDocument(file)) {
                        examsMap.put(exame, file);
                    } else {
                        model.addAttribute("errorExams", MSG_EXAMS_ERROR);
                        hasErrors = true;
                        break;
                    }
                }
            }
        }

        if (hasErrors) {
            model.addAttribute("consultaForm", consulta);
            model.addAttribute("especialistas", consulta.getClinica().getEspecialistas());
            model.addAttribute("pacientes", consulta.getClinica().getPacientes());
            model.addAttribute("clinicaId", consulta.getClinica().getId());

            return "consulta/editConsultaForm";
        }

        if (!examsMap.isEmpty()) {
            for (Map.Entry<Exame, MultipartFile> entry : examsMap.entrySet()) {
                Exame exam = entry.getKey();
                MultipartFile file = entry.getValue();
                uploadExamDocument(exam, file);
            }
        }
        consultaService.save(consulta);

        status.setComplete();
        session.removeAttribute("currentClinica");
        session.removeAttribute("cacheExams");

        return "redirect:/clinica/" + consulta.getClinica().getId() + "/consulta";
    }

    @PostMapping("/consulta/addExame")
    public String addExame(@ModelAttribute("consultaForm") Consulta consulta) {
        consulta.getExames().add(new Exame());
        return "consulta/fragments/dynamicExams :: dynamicExams";
    }

    @PostMapping("/consulta/removeExame")
    public String removeExame(@ModelAttribute("consultaForm") Consulta consulta, @RequestParam("removeExame") int index) {
        consulta.getExames().remove(index);
        return "consulta/fragments/dynamicExams :: dynamicExams";
    }

}
