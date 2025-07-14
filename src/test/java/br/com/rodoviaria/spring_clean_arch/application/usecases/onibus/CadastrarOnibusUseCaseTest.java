package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.CadastrarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CadastrarOnibusUseCaseTest {
    @Mock
    private OnibusRepository onibusRepository;

    @Mock
    private OnibusMapper onibusMapper;

    private CadastrarOnibusRequest request;

    @InjectMocks
    private CadastrarOnibusUseCase useCase;

    @BeforeEach
    void setUp(){
        request = new CadastrarOnibusRequest(
                "ABC1234",
                "Mercedes I4",
                100
        );
    }

    @Test
    @DisplayName("Deve cadastrar um ônibus com sucesso quandos os dados forem válidos")
    void deveCadastrarOnibusComSucesso_QuandoDadosSaoValidos(){
        // ARRANGE
        // Simular que não existe um ônibus com a placa
        when(onibusRepository.buscarPelaPlaca(request.placa())).thenReturn(Optional.empty());

        // Simular o comportamento do salvar() para retornar o ônibus que foi passado a ele.
        when(onibusRepository.salvar(any(Onibus.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Simular o comportamento do mapper
        OnibusResponse responseEsperado = new OnibusResponse(UUID.randomUUID(), request.placa(), request.modelo(), request.capacidade(), true, LocalDateTime.now());

        // Configurado para retornar o response
        when(onibusMapper.toResponse(any(Onibus.class))).thenReturn(responseEsperado);

        // ACT
        OnibusResponse responseReal = useCase.execute(request);

        // ASSERT
        // Verificar se a resposta recebida não é nula e se corresponde ao esperado
        assertNotNull(responseEsperado);
        assertEquals(responseEsperado.placa(), responseReal.placa());
        assertEquals(responseEsperado.modelo(), responseReal.modelo());

        // Captura a entidade Onibus que foi enviada no '.salvar()'
        ArgumentCaptor<Onibus> onibusArgumentCaptor = ArgumentCaptor.forClass(Onibus.class);
        verify(onibusRepository).salvar(onibusArgumentCaptor.capture());
        Onibus onibusSalvo = onibusArgumentCaptor.getValue();

        // Verificar se a entidade foi criada corretamente
        assertEquals(request.placa(), onibusSalvo.getPlaca().getValor());
        assertTrue(onibusSalvo.getAtivo());

    }

    @Test
    @DisplayName("Deve lançar OnibusInvalidoException quando a placa já existir")
    void deveLancarExcecao_QuandoPlacaJaExistir(){
        // ARRANGE
        // Criar um mock de ônibus que já existiria no banco de dados
        Onibus onibusExistente = mock(Onibus.class);

        // Simular que esse ônibus foi encontrado
        when(onibusRepository.buscarPelaPlaca(request.placa())).thenReturn(Optional.of(onibusExistente));

        // ACT e ASSERT
        assertThrows(OnibusInvalidoException.class, () -> {
            useCase.execute(request);
        });

        // Garante que o método 'salvar' NUNCA foi chamado, pois a operação deve falhar antes
        verify(onibusRepository, never()).salvar(any(Onibus.class));
    }
}
