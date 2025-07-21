package br.com.rodoviaria.spring_clean_arch.infrastructure.pdf;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.gateway.PdfGeneratorGateway;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Implementação do gateway de geração de PDF que utiliza o Thymeleaf para processar
 * um template HTML e a biblioteca Flying Saucer para renderizá-lo como PDF.
 */
@Service // Anotação do Spring para que esta classe seja um Bean gerenciado e possa ser injetada.
public class ThymeleafPdfGenerator implements PdfGeneratorGateway {

    private final TemplateEngine templateEngine;

    // O TemplateEngine do Thymeleaf é injetado pelo Spring Boot automaticamente.
    public ThymeleafPdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public byte[] gerarPdfDeTicket(TicketEmailResponse ticket, ViagemResponse viagem) {
        // 1. Prepara o contexto com as variáveis que serão usadas no template HTML.
        Context context = new Context();
        context.setVariable("ticket", ticket);
        context.setVariable("viagem", viagem);

        // 2. Processa o template (ex: "nota-fiscal.html") e gera uma string HTML com os dados.
        String htmlProcessado = templateEngine.process("nota-fiscal", context);

        // 3. Usa o Flying Saucer para converter a string HTML em um PDF.
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlProcessado);
            renderer.layout();
            renderer.createPDF(outputStream);

            // Retorna o conteúdo do PDF como um array de bytes.
            return outputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            // Em uma aplicação real, aqui você logaria o erro com mais detalhes.
            // e.g., log.error("Erro ao gerar PDF para o ticket: {}", ticket.id(), e);
            throw new RuntimeException("Erro ao gerar o arquivo PDF.", e);
        }
    }
}