package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusNotification;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.MeterValueDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.MeterValuesCompletoDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.SampledValueDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.StatusNotificationDTO;
import br.projeto_integrador.aplicativo.backend.repositories.ConectorRepository;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ConectorRepository conectorRepository;


    public TransacaoService(TransacaoRepository transacaoRepository, ConectorRepository conectorRepository) {
        this.transacaoRepository = transacaoRepository;
        this.conectorRepository = conectorRepository;
    }


    private Transacao buscarTransacaoPorConectorETransacaoID(Conector conector, Long transactionId) {


        Transacao transacao = transacaoRepository.findByIdTransacao(transactionId)
                .orElse(null);

        if (transacao == null) {

            System.out.println("Associando transactionId com transação existente");

            transacao = transacaoRepository
                    .findTopByConectorAndStatusTransacao(conector, StatusTransacao.Preparing)
                    .orElse(null);

            if (transacao != null) {
                transacao.setIdTransacao(transactionId);
            }
        }

        return transacao;
    }

    //atualizar constantemente a tabela
    public void processarMeterValuesCiclo(MeterValuesCompletoDTO payload){

        String chargerId = payload.charger_id();
        Integer connectorId = payload.connector_id();

        Conector conector = conectorRepository
                .findByCarregador_IdCarregadorAndConnectorIdNoCarregador(chargerId, connectorId)
                .orElseThrow(() -> new RuntimeException("Conector não encontrado"));


        Transacao transacao = buscarTransacaoPorConectorETransacaoID(conector, Long.valueOf(payload.transaction_id()));

        if (transacao == null) {
            System.out.println("Nenhuma transação aberta encontrada");
            return;
        }

        for (MeterValueDTO meter : payload.meter_value()) {

            for (SampledValueDTO sample : meter.sampled_value()) {

                String measurand = sample.measurand();
                Double value = sample.value();

                if (measurand == null || value == null) {
                    continue;
                }

                switch (measurand) {

                    //energia inicial + informações periodicas
                    case "Energy.Active.Import.Register":

                        if ("Transaction.Begin".equals(sample.context())
                                && transacao.getMeterStart() == null) {

                            transacao.setMeterStart(value);
                            transacao.setStatusTransacao(StatusTransacao.Charging);

                        } else if ("Sample.Periodic".equals(sample.context())) {

                            Double energiaConsumida = value - transacao.getMeterStart();
                            Double energiaKwH = energiaConsumida/1000;

                            transacao.setEnergiaConsumida(energiaKwH);

                            BigDecimal valorGasto = BigDecimal.valueOf(energiaKwH)
                                    .multiply(transacao.getValorEnergia());

                            transacao.setValorRecarga(valorGasto);

                        }

                        else if ("Transaction.End".equals(sample.context())) {

                            transacao.setMeterStop(value);

                            Double energiaConsumida = value - transacao.getMeterStart();
                            Double energiaKwH = energiaConsumida/1000;

                            transacao.setEnergiaConsumida(energiaKwH);
                            transacao.setStatusTransacao(StatusTransacao.Finishing);
                            transacao.setDataFim(LocalDateTime.now());

                            BigDecimal valorGasto = BigDecimal.valueOf(energiaKwH)
                                    .multiply(transacao.getValorEnergia());

                            transacao.setValorRecarga(valorGasto);

                        }


                        break;

                    case "Energy.Active.Import.Interval":
                        break;

                    case "Power.Active.Import":
                        break;

                    case "Power.Offered":
                        break;

                    case "Current.Import":
                        break;

                    case "Current.Offered":
                        break;

                    case "SoC":

                        if (transacao.getSocAtual() == null) {
                            transacao.setSocAtual(value);
                        }

                        transacao.setSocAtual(value);

                        break;


                    case "Power.Factor":
                        break;

                }
            }

            if (meter.timestamp() != null) {
                conector.setDataAtualizacao(meter.timestamp().toLocalDateTime());
            }
        }

        conectorRepository.save(conector);
        transacaoRepository.save(transacao);
    }


}
