package br.com.rodoviaria.spring_clean_arch.application.gateway;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;

/**
 * Porta de saída (Gateway) para o serviço de notificação de tickets.
 * A camada de aplicação depende desta abstração, e a camada de infraestrutura a implementa.
 */
public interface TicketNotificationGateway {

    /**
     * Envia os dados de um ticket para o sistema de notificação.
     *
     * @param ticketData O DTO com as informações necessárias para a notificação.
     */
    void enviarNotificacao(TicketEmailResponse ticketData);
}