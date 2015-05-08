/*
 * SURITTEC
 * Copyright 2015, TTUS TECNOLOGIA DA INFORMACAO LTDA, 
 * and individual contributors as indicated by the @authors tag
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package br.com.surittec.surispring.example.domain.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import br.com.surittec.surijpa.criteria.JPQL;
import br.com.surittec.surispring.example.domain.entity.Pessoa;
import br.com.surittec.surispring.jpa.repository.EntityRepository;

@Repository
public class PessoaRepository extends EntityRepository<Pessoa, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Exemplo de utilizacao do JPQL
	 */
	public List<Pessoa> findByNome(String nome) {

		JPQL jpql = jpql().from("Pessoa");

		if (StringUtils.isNotBlank(nome)) {
			jpql.where("nome = :nome").withParam("nome", nome);
		}

		return jpql.getResultList(Pessoa.class);
	}

	public boolean isUnique(Pessoa pessoa) {

		JPQL jpql = jpql().select("count(p)").from("Pessoa p");

		jpql.where("(p.cpf = :cpf or p.email = :email)");
		jpql.withParam("cpf", pessoa.getCpf());
		jpql.withParam("email", pessoa.getEmail());

		if (pessoa.getId() != null) {
			jpql.where("p.id != :id").withParam("id", pessoa.getId());
		}

		return jpql.getSingleResult(Long.class) == 0;
	}

}
