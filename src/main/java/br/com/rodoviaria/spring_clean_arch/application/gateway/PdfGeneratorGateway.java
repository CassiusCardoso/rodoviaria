package br.com.rodoviaria.spring_clean_arch.application.gateway;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;

public interface PdfGeneratorGateway {
    /**
     * Gera um arquivo PDF com base nos dados de um ticket de viagem.
     *
     * @param ticket Os dados do ticket a serem incluídos no PDF.
     * @param viagem Os dados da viagem correspondente.
     * @return Um array de bytes (byte[]) representando o arquivo PDF gerado.
     */
    byte[] gerarPdfDeTicket(TicketEmailResponse ticket, ViagemResponse viagem);
}
