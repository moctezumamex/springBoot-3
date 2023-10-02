package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class AgendaDeConsultaService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    /* @Autowired Es para inyectar la información del
    repositorio en la variable consultaRepository */
    @Autowired
    private ConsultaRepository consultaRepository;
    public void agendar(DatosAgendarConsulta datosAgendarConsulta)
    {
        if(pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent())
        {
            throw new ValidacionDeIntegridad("Este Id de Paciente no fue encontrado");
        }

        if(datosAgendarConsulta.idMedico()!=null && medicoRepository.existsById(datosAgendarConsulta.idMedico()))
        {
            throw new ValidacionDeIntegridad("Este Id de Medico no fue encontrado");
        }

        var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();

        //var medico = medicoRepository.findById(datosAgendarConsulta.idMedico()).get();
var medico = seleccionarMedico(datosAgendarConsulta);
        var consulta = new Consulta(null, medico, paciente,datosAgendarConsulta.fecha());

        consultaRepository.save(consulta);
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datosAgendarConsulta) {
        if(datosAgendarConsulta.idMedico()!=null)
        {
            return medicoRepository.getReferenceById(datosAgendarConsulta.idMedico());
        }
        if(datosAgendarConsulta.especialidad()==null)
        {
            throw new ValidacionDeIntegridad("Seleccione una especialidad");
        }

        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datosAgendarConsulta.especialidad(),datosAgendarConsulta.fecha());
    }
}
