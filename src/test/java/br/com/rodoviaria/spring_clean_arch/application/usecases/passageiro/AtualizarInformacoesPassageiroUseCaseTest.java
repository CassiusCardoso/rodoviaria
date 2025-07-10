package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarInformacoesPassageiroUseCaseTest {
    @Mock
    private PassageiroRepository repository;

    @InjectMocks
    private AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;

    private UUID passageiroId;
    private Passageiro passageiro;

    @BeforeEach
    void setUp(){
        passageiroId = UUID.randomUUID();
        passageiro = new Passageiro(
                passageiroId,
                "John Doe",
                new Email("john.doe@gmail.com"),
                new Senha("Senha@Valida1"),
                new Cpf("259.174.501-37"),
                new Telefone("(11) 98888-7777"),
                true // O passageiro começa ATIVO
        );
    }

    @Test
    @DisplayName("Deve atualizar um passageiro após receber uma requisição válida")
    void deveAtualizarPassageiroComSucesso_QuandoRequisicaoValida(){

        // ARRANGE
        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "John Doe novo",
                "john.doe.novo@gmail.com",
                "Senha123@",
                "129.425.841-90",
                "(11) 98888-7777"
        );
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiro));

        // Quando o método 'salvar' for chamado, retornar o passageiro com atualizações
        when(repository.salvar(any(Passageiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        AtualizarInformacoesPassageiroResponse response = atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);

        // ASSERT
        assertNotNull(response);
        assertEquals("John doe novo", response.nome());
        assertEquals("john.doe.novo@gmail.com", response.email());

        ArgumentCaptor<Passageiro> passageiroCaptor = ArgumentCaptor.forClass(Passageiro.class);

        verify(repository, times(1)).salvar(passageiroCaptor.capture());

        // Pegue o passageiro capturado e verifique se ele contém os dados atualizados.
        Passageiro passageiroSalvo = passageiroCaptor.getValue();
        assertEquals("John Doe Novo", passageiroSalvo.getNome());
        assertEquals("john.doe.novo@gmail.com", passageiroSalvo.getEmail().getEmail());
        assertEquals("55566677788", passageiroSalvo.getCpf().getCpf()); // Comparando com o CPF limpo

    }
}
