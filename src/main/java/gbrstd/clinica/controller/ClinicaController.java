package gbrstd.clinica.controller;

import gbrstd.clinica.model.Clinica;
import gbrstd.clinica.model.Endereco;
import gbrstd.clinica.model.Especialidade;
import gbrstd.clinica.service.ClinicaService;
import gbrstd.clinica.service.EspecialidadeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/clinica")
public class ClinicaController {

    private final ClinicaService clinicaService;

    private final EspecialidadeService especialidadeService;

    public ClinicaController(ClinicaService clinicaService, EspecialidadeService especialidadeService) {
        this.clinicaService = clinicaService;
        this.especialidadeService = especialidadeService;
    }

    @GetMapping
    public String listAllClinicas(Model model) {
        model.addAttribute("clinicas", clinicaService.findAll());
        return "clinica/listClinicas";
    }

    @GetMapping("/{id}")
    public String showClinica(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clinica", clinicaService.findById(id));
        return "clinica/showClinica";
    }

    @GetMapping("/create")
    public String createClinicaForm(Clinica clinica,
                                    Endereco endereco,
                                    Especialidade especialidade,
                                    Model model) {

        model.addAttribute("clinicaForm", clinica);
        model.addAttribute("enderecoForm", endereco);
        model.addAttribute("especialidadeForm", especialidade);
        model.addAttribute("especialidades", especialidadeService.findAll());

        return "clinica/createClinicaForm";
    }

    @GetMapping("/{id}/edit")
    public String editClinicaForm(@PathVariable("id") Long id,
                                  Model model) {

        Clinica clinica = clinicaService.findById(id);

        model.addAttribute("clinicaForm", clinica);
        model.addAttribute("especialidadeForm", clinica.getEspecialidade());
        model.addAttribute("especialidades", especialidadeService.findAll());

        return "clinica/editClinicaForm";
    }

    @PostMapping("/{id}/delete")
    public String deleteClinica(@PathVariable("id") Long id) {
        clinicaService.delete(id);
        return "redirect:/clinica";
    }

    @PostMapping("/create")
    public String createClinicaSubmit(@ModelAttribute("clinicaForm") @Valid Clinica clinica,
                                      BindingResult resultClinica,
                                      @ModelAttribute("enderecoForm") @Valid Endereco endereco,
                                      BindingResult resultEndereco,
                                      @ModelAttribute("especialidadeForm") @Valid Especialidade especialidade,
                                      BindingResult resultEspecialidade,
                                      Model model) {

        if (resultClinica.hasErrors() || resultEndereco.hasErrors() || resultEspecialidade.hasErrors()) {
            return createClinicaForm(clinica, endereco, especialidade, model);
        }

        clinica.setEndereco(endereco);
        clinica.setEspecialidade(especialidade);

        clinicaService.save(clinica);

        return "redirect:/clinica";
    }

    @PostMapping("/{id}/edit")
    public String editClinicaSubmit(@PathVariable("id") Long id,
                                    @ModelAttribute("clinicaForm") @Valid Clinica clinica,
                                    BindingResult resultClinica,
                                    @ModelAttribute("especialidadeForm") @Valid Especialidade especialidade,
                                    BindingResult resultEspecialidade,
                                    Model model) {

        if (resultClinica.hasErrors() || resultEspecialidade.hasErrors()) {
            model.addAttribute("clinicaForm", clinica);
            model.addAttribute("especialidadeForm", especialidade);
            model.addAttribute("especialidades", especialidadeService.findAll());

            return "clinica/editClinicaForm";
        }

        clinica.setEspecialidade(especialidade);

        clinicaService.save(clinica);

        return "redirect:/clinica";
    }

}
