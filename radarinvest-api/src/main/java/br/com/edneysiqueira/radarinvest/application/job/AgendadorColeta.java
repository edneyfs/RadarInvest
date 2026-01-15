package br.com.edneysiqueira.radarinvest.application.job;

import br.com.edneysiqueira.radarinvest.application.service.ColetaProventosService;
import br.com.edneysiqueira.radarinvest.application.service.MonitorNoticiasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AgendadorColeta {

    private final ColetaProventosService coletaProventosService;
    private final MonitorNoticiasService monitorNoticiasService;

    // Roda todos os dias as 08:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void agendarColetaProventos() {
        log.info("Iniciando Job Agendado: Coleta Proventos e Notícias");
        coletaProventosService.executarColetaGeral();
        monitorNoticiasService.executarMonitoramento();
    }

    // Roda a cada 30 min para testes (opcional)
    // @Scheduled(fixedRate = 1800000)
    // public void runPreview() { ... }
}
