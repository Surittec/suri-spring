package br.com.surittec.surispring.example.util.i18n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.surittec.surispring.faces.scope.ApplicationScoped;

@Component("br.com.surittec.surispring.example.util.i18n.Messages")
@ApplicationScoped
public class Messages {

	/*
	 * Pessoa
	 */
	@Value("#{msgProps['pessoa.cadastro.falha.unicidade']}")
	private String pessoaCadastroFalhaUnicidade;

	/*
	 * Global
	 */

	@Value("#{msgProps['global.registro.nao.encontrado']}")
	private String globalRegistroNaoEncontrado;

	@Value("#{msgProps['global.dados.incluidos.sucesso']}")
	private String globalDadosIncluidosSucesso;

	@Value("#{msgProps['global.dados.alterados.sucesso']}")
	private String globalDadosAlteradosSucesso;

	@Value("#{msgProps['global.registro.removido.sucesso']}")
	private String globalRegistroRemovidoSucesso;

	/*
	 * Getters
	 */

	public String pessoaCadastroFalhaUnicidade() {
		return pessoaCadastroFalhaUnicidade;
	}

	public String globalRegistroNaoEncontrado() {
		return globalRegistroNaoEncontrado;
	}

	public String globalDadosIncluidosSucesso() {
		return globalDadosIncluidosSucesso;
	}

	public String globalDadosAlteradosSucesso() {
		return globalDadosAlteradosSucesso;
	}

	public String globalRegistroRemovidoSucesso() {
		return globalRegistroRemovidoSucesso;
	}

}
