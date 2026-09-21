package br.com.contabancaria.service;

import br.com.contabancaria.exception.SaldoInsuficienteException;
import br.com.contabancaria.model.Conta;
import br.com.contabancaria.model.ContaCorrente;
import br.com.contabancaria.model.ContaPoupanca;
import br.com.contabancaria.model.TipoTransacao;
import br.com.contabancaria.model.Transacao;
import br.com.contabancaria.repository.ContaRepository;
import br.com.contabancaria.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CorrentistaService correntistaService;

    private ContaService service;

    @BeforeEach
    void setUp() {
        service = new ContaService(contaRepository, transacaoRepository, correntistaService);
    }

    @Test
    void deveDepositarERegistrarTransacao() {
        ContaPoupanca conta = poupanca();
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        service.depositar(1L, valor("100.00"));

        assertEquals(valor("100.00"), conta.getSaldo());
        verificarTransacao(TipoTransacao.DEPOSITO, "100.00");
    }

    @Test
    void deveSacarQuandoHouverSaldo() {
        ContaPoupanca conta = poupanca();
        conta.depositar(valor("100.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        service.sacar(1L, valor("40.00"));

        assertEquals(valor("60.00"), conta.getSaldo());
        verificarTransacao(TipoTransacao.SAQUE, "40.00");
    }

    @Test
    void devePermitirSaqueDaContaCorrenteAteOLimite() {
        ContaCorrente conta = corrente();
        conta.depositar(valor("50.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        service.sacar(1L, valor("100.00"));

        assertEquals(valor("-50.00"), conta.getSaldo());
    }

    @Test
    void deveLancarExcecaoQuandoSaqueEstourarSaldoELimite() {
        ContaCorrente conta = corrente();
        conta.depositar(valor("50.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        assertThrows(SaldoInsuficienteException.class, () -> service.sacar(1L, valor("151.00")));

        assertEquals(valor("50.00"), conta.getSaldo());
        verify(transacaoRepository, never()).save(any(Transacao.class));
    }

    @Test
    void deveAplicarRendimentoNaPoupanca() {
        ContaPoupanca conta = poupanca();
        conta.depositar(valor("1000.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        service.aplicarRendimento(1L, valor("1.50"));

        assertEquals(valor("1015.00"), conta.getSaldo());
        verificarTransacao(TipoTransacao.RENDIMENTO, "15.00");
    }

    @Test
    void deveAplicarJurosSobreSaldoNegativoDaCorrente() {
        ContaCorrente conta = corrente();
        conta.depositar(valor("50.00"));
        conta.sacar(valor("100.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        service.aplicarJuros(1L, valor("10.00"));

        assertEquals(valor("-55.00"), conta.getSaldo());
        verificarTransacao(TipoTransacao.JUROS, "5.00");
    }

    private ContaPoupanca poupanca() {
        return new ContaPoupanca("123", null);
    }

    private ContaCorrente corrente() {
        return new ContaCorrente("456", null, valor("100.00"));
    }

    private BigDecimal valor(String valor) {
        return new BigDecimal(valor);
    }

    private void verificarTransacao(TipoTransacao tipo, String valor) {
        ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(captor.capture());
        assertEquals(tipo, captor.getValue().getTipo());
        assertEquals(new BigDecimal(valor), captor.getValue().getValor());
        verify(contaRepository).save(any(Conta.class));
    }
}