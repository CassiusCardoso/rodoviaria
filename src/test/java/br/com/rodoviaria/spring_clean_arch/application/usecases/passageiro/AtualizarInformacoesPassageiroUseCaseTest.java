// Conteúdo corrigido para AtualizarInformacoesPassageiroUseCaseTest.java
package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.NomeInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarInformacoesPassageiroUseCaseTest {
    @Mock
    private PassageiroRepository repository;
    @Mock
    private PassageiroMapper passageiroMapper;
    @InjectMocks
    private AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;

    private UUID passageiroId;
    private Passageiro passageiro;

    @BeforeEach
    void setUp() {
        passageiroId = UUID.randomUUID();
        // CORREÇÃO AQUI:
        passageiro = new Passageiro(
                passageiroId,
                "John Doe",
                new Email("john.doe@gmail.com"),
                Senha.carregar("Senha@Valida1"), // <<-- MUDANÇA
                new Cpf("259.174.501-37"),
                new Telefone("(11) 98888-7777"),
                true
        );
    }

    // O resto do arquivo permanece igual
    @Test
    @DisplayName("Deve atualizar um passageiro após receber uma requisição válida")
    void deveAtualizarPassageiroComSucesso_QuandoRequisicaoForValida(){
        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest("John Doe Novo", "john.doe.novo@gmail.com", "Senha123@", "129.425.841-90", "(11) 98888-7777");
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiro));
        when(repository.salvar(any(Passageiro.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passageiroMapper.toAtualizarInformacoesResponse(any(Passageiro.class))).thenReturn(new AtualizarInformacoesPassageiroResponse("John Doe Novo", "john.doe.novo@gmail.com", "129.425.841-90", "(11) 98888-7777", true, null));
        AtualizarInformacoesPassageiroResponse response = atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        assertNotNull(response);
        assertEquals("John Doe Novo", response.nome());
        assertEquals("john.doe.novo@gmail.com", response.email());
        ArgumentCaptor<Passageiro> passageiroCaptor = ArgumentCaptor.forClass(Passageiro.class);
        verify(repository, times(1)).salvar(passageiroCaptor.capture());
        Passageiro passageiroSalvo = passageiroCaptor.getValue();
        assertEquals("John Doe Novo", passageiroSalvo.getNome());
        assertEquals("john.doe.novo@gmail.com", passageiroSalvo.getEmail().getEmail());
        String cpfLimpo = request.cpf().replaceAll("[^\\d]", "");
        assertEquals(cpfLimpo, passageiroSalvo.getCpf().getCpf());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o passageiro for inativo")
    void deveLancarExcecao_QuandoPassageiroForInativo(){
        Passageiro passageiroInativo = passageiro.desativar();
        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest("Nome Novo", "email.novo@gmail.com", "NovaSenha@123", "987.654.321-00", "(33) 94444-5555");
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInativo));
        assertThrows(PassageiroInvalidoException.class, () -> {
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        });
        verify(repository, never()).salvar(any(Passageiro.class));
    }
}