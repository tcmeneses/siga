package br.gov.jfrj.siga.sr.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.annotations.Type;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sr.model.SrAcao.SrAcaoVO;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao.SrItemConfiguracaoVO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name = "SR_CONFIGURACAO", schema = "SIGASR")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_SR")
public class SrConfiguracao extends CpConfiguracao {

	public static ActiveRecord<SrConfiguracao> AR = new ActiveRecord<>(SrConfiguracao.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4959384444345462871L;

	@Column(name = "FORMA_ACOMPANHAMENTO")
	private SrFormaAcompanhamento formaAcompanhamento;

	//@ManyToOne
	//@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	@Transient
	private SrItemConfiguracao itemConfiguracaoFiltro;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_ITEM", schema = "SIGASR", joinColumns={@JoinColumn(name="ID_CONFIGURACAO")}, inverseJoinColumns={@JoinColumn(name="ID_ITEM_CONFIGURACAO")})
	private List<SrItemConfiguracao> itemConfiguracaoSet;
	
	//@ManyToOne
	//@JoinColumn(name = "ID_ACAO")
	@Transient
	private SrAcao acaoFiltro;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_ACAO", schema = "SIGASR", joinColumns={@JoinColumn(name="ID_CONFIGURACAO")}, inverseJoinColumns={@JoinColumn(name="ID_ACAO")})
	private List<SrAcao> acoesSet;

	@Column(name = "GRAVIDADE")
	private SrGravidade gravidade;

	@Column(name = "TENDENCIA")
	private SrTendencia tendencia;

	@Column(name = "URGENCIA")
	private SrUrgencia urgencia;

	@ManyToOne
	@JoinColumn(name = "ID_ATENDENTE")
	private DpLotacao atendente;

	@ManyToOne
	@JoinColumn(name = "ID_POS_ATENDENTE")
	private DpLotacao posAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_EQUIPE_QUALIDADE")
	private DpLotacao equipeQualidade;

	@ManyToOne
	@JoinColumn(name = "ID_PRE_ATENDENTE")
	private DpLotacao preAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_ATRIBUTO")
	private SrAtributo atributo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESQUISA")
	private SrPesquisa pesquisaSatisfacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	private SrLista listaPrioridade;
	
	@Enumerated
	private SrPrioridade prioridade;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SR_LISTA_CONFIGURACAO", schema="SIGASR", joinColumns = @JoinColumn(name = "ID_CONFIGURACAO"), inverseJoinColumns = @JoinColumn(name = "ID_LISTA"))
	private List<SrLista> listaConfiguracaoSet;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_PERMISSAO", joinColumns = @JoinColumn(name = "ID_CONFIGURACAO"), inverseJoinColumns = @JoinColumn(name = "TIPO_PERMISSAO"), schema="SIGASR")
	private List<SrTipoPermissaoLista> tipoPermissaoSet;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ACORDO")
	private SrAcordo acordo;

	@Column(name = "FG_ATRIBUTO_OBRIGATORIO")
	@Type(type = "yes_no")
	private boolean atributoObrigatorio;

	@Column(name = "SLA_PRE_ATENDIMENTO_QUANT")
	private Integer slaPreAtendimentoQuantidade;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_PRE_ATENDIMENTO")
	private CpUnidadeMedida unidadeMedidaPreAtendimento;

	@Column(name = "SLA_ATENDIMENTO_QUANT")
	private Integer slaAtendimentoQuantidade;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_ATENDIMENTO")
	private CpUnidadeMedida unidadeMedidaAtendimento;

	@Column(name = "SLA_POS_ATENDIMENTO_QUANT")
	private Integer slaPosAtendimentoQuantidade;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_POS_ATENDIMENTO")
	private CpUnidadeMedida unidadeMedidaPosAtendimento;

	@Column(name = "MARGEM_SEGURANCA")
	private Integer margemSeguranca;

	@Lob
	@Column(name = "OBSERVACAO_SLA", length = 8192)
	private String observacaoSLA;

	@Column(name = "FG_DIVULGAR_SLA")
	@Type(type = "yes_no")
	private Boolean divulgarSLA;

	@Column(name = "FG_NOTIFICAR_GESTOR")
	@Type(type = "yes_no")
	private Boolean notificarGestor;

	@Column(name = "FG_NOTIFICAR_SOLICITANTE")
	@Type(type = "yes_no")
	private Boolean notificarSolicitante;

	@Column(name = "FG_NOTIFICAR_CADASTRANTE")
	@Type(type = "yes_no")
	private Boolean notificarCadastrante;

	@Column(name = "FG_NOTIFICAR_INTERLOCUTOR")
	@Type(type = "yes_no")
	private Boolean notificarInterlocutor;

	@Column(name = "FG_NOTIFICAR_ATENDENTE")
	@Type(type = "yes_no")
	private Boolean notificarAtendente;

	@Transient
	private SrSubTipoConfiguracao subTipoConfig;

	@Transient
	private boolean isHerdado;

	@Transient
	private boolean utilizarItemHerdado;

	public SrFormaAcompanhamento getFormaAcompanhamento() {
		return formaAcompanhamento;
	}

	public void setFormaAcompanhamento(SrFormaAcompanhamento formaAcompanhamento) {
		this.formaAcompanhamento = formaAcompanhamento;
	}

	public SrItemConfiguracao getItemConfiguracaoFiltro() {
		return itemConfiguracaoFiltro;
	}

	public void setItemConfiguracaoFiltro(SrItemConfiguracao itemConfiguracaoFiltro) {
		this.itemConfiguracaoFiltro = itemConfiguracaoFiltro;
	}

	public List<SrItemConfiguracao> getItemConfiguracaoSet() {
		return itemConfiguracaoSet;
	}

	public void setItemConfiguracaoSet(List<SrItemConfiguracao> itemConfiguracaoSet) {
		this.itemConfiguracaoSet = itemConfiguracaoSet;
	}

	public SrAcao getAcaoFiltro() {
		return acaoFiltro;
	}

	public void setAcaoFiltro(SrAcao acaoFiltro) {
		this.acaoFiltro = acaoFiltro;
	}

	public List<SrAcao> getAcoesSet() {
		return acoesSet;
	}

	public void setAcoesSet(List<SrAcao> acoesSet) {
		this.acoesSet = acoesSet;
	}

	public SrGravidade getGravidade() {
		return gravidade;
	}

	public void setGravidade(SrGravidade gravidade) {
		this.gravidade = gravidade;
	}

	public SrTendencia getTendencia() {
		return tendencia;
	}

	public void setTendencia(SrTendencia tendencia) {
		this.tendencia = tendencia;
	}

	public SrUrgencia getUrgencia() {
		return urgencia;
	}

	public void setUrgencia(SrUrgencia urgencia) {
		this.urgencia = urgencia;
	}

	public DpLotacao getAtendente() {
		return atendente;
	}

	public void setAtendente(DpLotacao atendente) {
		this.atendente = atendente;
	}

	public DpLotacao getPosAtendente() {
		return posAtendente;
	}

	public void setPosAtendente(DpLotacao posAtendente) {
		this.posAtendente = posAtendente;
	}

	public DpLotacao getEquipeQualidade() {
		return equipeQualidade;
	}

	public void setEquipeQualidade(DpLotacao equipeQualidade) {
		this.equipeQualidade = equipeQualidade;
	}

	public DpLotacao getPreAtendente() {
		return preAtendente;
	}

	public void setPreAtendente(DpLotacao preAtendente) {
		this.preAtendente = preAtendente;
	}

	public SrAtributo getAtributo() {
		return atributo;
	}

	public void setAtributo(SrAtributo atributo) {
		this.atributo = atributo;
	}

	public SrPesquisa getPesquisaSatisfacao() {
		return pesquisaSatisfacao;
	}

	public void setPesquisaSatisfacao(SrPesquisa pesquisaSatisfacao) {
		this.pesquisaSatisfacao = pesquisaSatisfacao;
	}

	public SrLista getListaPrioridade() {
		return listaPrioridade;
	}

	public void setListaPrioridade(SrLista listaPrioridade) {
		this.listaPrioridade = listaPrioridade;
	}

	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public List<SrTipoPermissaoLista> getTipoPermissaoSet() {
		return tipoPermissaoSet;
	}

	public void setTipoPermissaoSet(List<SrTipoPermissaoLista> tipoPermissaoSet) {
		this.tipoPermissaoSet = tipoPermissaoSet;
	}

	public SrAcordo getAcordo() {
		return acordo;
	}

	public void setAcordo(SrAcordo acordo) {
		this.acordo = acordo;
	}

	public boolean isAtributoObrigatorio() {
		return atributoObrigatorio;
	}

	public void setAtributoObrigatorio(boolean atributoObrigatorio) {
		this.atributoObrigatorio = atributoObrigatorio;
	}

	public Integer getSlaPreAtendimentoQuantidade() {
		return slaPreAtendimentoQuantidade;
	}

	public void setSlaPreAtendimentoQuantidade(Integer slaPreAtendimentoQuantidade) {
		this.slaPreAtendimentoQuantidade = slaPreAtendimentoQuantidade;
	}

	public CpUnidadeMedida getUnidadeMedidaPreAtendimento() {
		return unidadeMedidaPreAtendimento;
	}

	public void setUnidadeMedidaPreAtendimento(
			CpUnidadeMedida unidadeMedidaPreAtendimento) {
		this.unidadeMedidaPreAtendimento = unidadeMedidaPreAtendimento;
	}

	public Integer getSlaAtendimentoQuantidade() {
		return slaAtendimentoQuantidade;
	}

	public void setSlaAtendimentoQuantidade(Integer slaAtendimentoQuantidade) {
		this.slaAtendimentoQuantidade = slaAtendimentoQuantidade;
	}

	public CpUnidadeMedida getUnidadeMedidaAtendimento() {
		return unidadeMedidaAtendimento;
	}

	public void setUnidadeMedidaAtendimento(CpUnidadeMedida unidadeMedidaAtendimento) {
		this.unidadeMedidaAtendimento = unidadeMedidaAtendimento;
	}

	public Integer getSlaPosAtendimentoQuantidade() {
		return slaPosAtendimentoQuantidade;
	}

	public void setSlaPosAtendimentoQuantidade(Integer slaPosAtendimentoQuantidade) {
		this.slaPosAtendimentoQuantidade = slaPosAtendimentoQuantidade;
	}

	public CpUnidadeMedida getUnidadeMedidaPosAtendimento() {
		return unidadeMedidaPosAtendimento;
	}

	public void setUnidadeMedidaPosAtendimento(
			CpUnidadeMedida unidadeMedidaPosAtendimento) {
		this.unidadeMedidaPosAtendimento = unidadeMedidaPosAtendimento;
	}

	public Integer getMargemSeguranca() {
		return margemSeguranca;
	}

	public void setMargemSeguranca(Integer margemSeguranca) {
		this.margemSeguranca = margemSeguranca;
	}

	public String getObservacaoSLA() {
		return observacaoSLA;
	}

	public void setObservacaoSLA(String observacaoSLA) {
		this.observacaoSLA = observacaoSLA;
	}

	public Boolean getDivulgarSLA() {
		return divulgarSLA;
	}

	public void setDivulgarSLA(Boolean divulgarSLA) {
		this.divulgarSLA = divulgarSLA;
	}

	public Boolean getNotificarGestor() {
		return notificarGestor;
	}

	public void setNotificarGestor(Boolean notificarGestor) {
		this.notificarGestor = notificarGestor;
	}

	public Boolean getNotificarSolicitante() {
		return notificarSolicitante;
	}

	public void setNotificarSolicitante(Boolean notificarSolicitante) {
		this.notificarSolicitante = notificarSolicitante;
	}

	public Boolean getNotificarCadastrante() {
		return notificarCadastrante;
	}

	public void setNotificarCadastrante(Boolean notificarCadastrante) {
		this.notificarCadastrante = notificarCadastrante;
	}

	public Boolean getNotificarInterlocutor() {
		return notificarInterlocutor;
	}

	public void setNotificarInterlocutor(Boolean notificarInterlocutor) {
		this.notificarInterlocutor = notificarInterlocutor;
	}

	public Boolean getNotificarAtendente() {
		return notificarAtendente;
	}

	public void setNotificarAtendente(Boolean notificarAtendente) {
		this.notificarAtendente = notificarAtendente;
	}

	public SrSubTipoConfiguracao getSubTipoConfig() {
		return subTipoConfig;
	}

	public void setSubTipoConfig(SrSubTipoConfiguracao subTipoConfig) {
		this.subTipoConfig = subTipoConfig;
	}

	public boolean isHerdado() {
		return isHerdado;
	}

	public void setHerdado(boolean isHerdado) {
		this.isHerdado = isHerdado;
	}

	public boolean isUtilizarItemHerdado() {
		return utilizarItemHerdado;
	}

	public void setUtilizarItemHerdado(boolean utilizarItemHerdado) {
		this.utilizarItemHerdado = utilizarItemHerdado;
	}

	public SrConfiguracao() {
	}

	public SrConfiguracao(DpPessoa solicitante, CpComplexo local, SrItemConfiguracao item) {
		this.setDpPessoa(solicitante);
		this.setComplexo(local);
		this.itemConfiguracaoFiltro = item;
	}

	public Selecionavel getSolicitante() {
		if (this.getDpPessoa() != null)
			return this.getDpPessoa();
		else if (this.getLotacao() != null)
			return this.getLotacao();
		else if (this.getCargo() != null)
			return this.getCargo();
		else if (this.getFuncaoConfianca() != null)
			return this.getFuncaoConfianca();
		else return this.getCpGrupo();
	}

	public String getPesquisaSatisfacaoString() {
		return pesquisaSatisfacao.getNomePesquisa();
	}

	public String getAtributoObrigatorioString() {
		return atributoObrigatorio ? "Sim" : "N�o";
	}

	public void salvarComoDesignacao() throws Exception {
		setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		salvarComHistorico();
	}

	public void salvarComoInclusaoAutomaticaLista(SrLista srLista) throws Exception {
		setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA));
		adicionarListaConfiguracoes(srLista);
		salvarComHistorico();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarDesignacoes(boolean mostrarDesativados, DpLotacao atendente) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);
		
		if (atendente != null) {
			sb.append(" and conf.atendente.idLotacaoIni = ");
			sb.append(atendente.getIdLotacaoIni());
		}
		
		if (!mostrarDesativados)
			sb.append(" and conf.hisDtFim is null");
		else {
			sb.append(" and conf.idConfiguracao in (");
			sb.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			sb.append(" SrConfiguracao GROUP BY hisIdIni) ");
		}
		
		return em()
				.createQuery(sb.toString()).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAbrangenciasAcordo(boolean mostrarDesativados, SrAcordo acordo) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO);
		
		if (acordo != null) {
			sb.append(" and conf.acordo.hisIdIni = ");
			sb.append(acordo.getHisIdIni());
		}
		
		if (!mostrarDesativados)
			sb.append(" and conf.hisDtFim is null");
		else {
			sb.append(" and conf.idConfiguracao in (");
			sb.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			sb.append(" SrConfiguracao GROUP BY hisIdIni) ");
		}
		
		return em()
				.createQuery(sb.toString()).getResultList();
	}
	
	public void salvarComoAbrangenciaAcordo() throws Exception {
		setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));
		salvarComHistorico();
		
		
	}

	public void salvarComoPermissaoUsoLista() throws Exception {
		setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA));
		salvarComHistorico();
		
		
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarPermissoesUsoLista(SrLista lista,
			boolean mostrarDesativado) {
		StringBuffer sb = new StringBuffer(
				"select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA);
		sb.append(" and conf.listaPrioridade.hisIdIni = ");
		sb.append(lista.getHisIdIni());

		if (!mostrarDesativado)
			sb.append(" and conf.hisDtFim is null ");

		sb.append(" order by conf.orgaoUsuario");

		return em().createQuery(sb.toString()).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarInclusaoAutomatica(SrLista lista,
			boolean mostrarDesativado) {
		StringBuffer sb = new StringBuffer(
				"select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA);
		sb.append(" and conf.listaPrioridade.hisIdIni = ");
		sb.append(lista.getHisIdIni());

		if (!mostrarDesativado)
			sb.append(" and conf.hisDtFim is null ");

		sb.append(" order by conf.orgaoUsuario");

		return em().createQuery(sb.toString()).getResultList();
	}

	public void salvarComoAssociacaoAtributo() throws Exception {
		setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		salvarComHistorico();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAssociacoesAtributo(SrAtributo atributo, Boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		queryBuilder.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO);
		queryBuilder.append(" and conf.atributo.hisIdIni = ");
		queryBuilder.append(atributo.getHisIdIni());
		
		if (!mostrarDesativados) {
			queryBuilder.append(" and conf.hisDtFim is null ");
		} else {
			queryBuilder.append(" and conf.idConfiguracao IN (");
			queryBuilder.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			queryBuilder.append(" SrConfiguracao GROUP BY hisIdIni)) ");
		}
		queryBuilder.append(" order by conf.orgaoUsuario");
		
		return em().createQuery(queryBuilder.toString()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAssociacoesAtributo(Boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		queryBuilder.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO);
		
		if (!mostrarDesativados) {
			queryBuilder.append(" and conf.hisDtFim is null ");
		} else {
			queryBuilder.append(" and conf.idConfiguracao IN (");
			queryBuilder.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			queryBuilder.append(" SrConfiguracao GROUP BY hisIdIni)) ");
		}
		queryBuilder.append(" order by conf.orgaoUsuario");
		
		return em()
				.createQuery(queryBuilder.toString())
				.getResultList();
	}

	private static SrConfiguracao buscar(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		return (SrConfiguracao) SrConfiguracaoBL.get().buscaConfiguracao(conf,
				atributosDesconsideradosFiltro, null);
	}
	
	public static SrConfiguracao buscarDesignacao(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		return buscar(conf, new int[] { SrConfiguracaoBL.ATENDENTE});
	}
	
	public static SrConfiguracao buscarDesignacao(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		conf.setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		return buscar(conf, ArrayUtils.addAll(atributosDesconsideradosFiltro,
				new int[] { SrConfiguracaoBL.ATENDENTE }));
	}
	
	public static SrConfiguracao buscarAssociacao(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		return buscar(conf, new int[] {});
	}
	
	public static SrConfiguracao buscarAbrangenciaAcordo(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));
		return buscar(conf, new int[] {});
	}

	public static List<SrConfiguracao> listar(SrConfiguracao conf) throws Exception {
		return listar(conf, new int[] {});
	}

	public static List<SrConfiguracao> listar(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		return SrConfiguracaoBL.get().listarConfiguracoesAtivasPorFiltro(conf,
				atributosDesconsideradosFiltro);
	}

	@Override
	public Long getId() {
		return getIdConfiguracao();
	}

	@Override
	public void setId(Long id) {
		setIdConfiguracao(id);
	}

	public List<SrLista> getListaConfiguracaoSet() {
		return listaConfiguracaoSet;
	}

	public void setListaConfiguracaoSet(List<SrLista> listaConfiguracaoSet) {
		this.listaConfiguracaoSet = listaConfiguracaoSet;
	}
	
	public String getDescrItemConfiguracaoAtual() {
		String descrItemConfiguracao = null;
		if (this.itemConfiguracaoSet != null && this.itemConfiguracaoSet.size() > 0) {
			SrItemConfiguracao conf = this.itemConfiguracaoSet.get(this.itemConfiguracaoSet.size() -1);
			
			if (conf != null) {
				descrItemConfiguracao = conf.getAtual().getTituloItemConfiguracao();
				
				if (this.itemConfiguracaoSet.size() > 1)
					if (descrItemConfiguracao != null)
						descrItemConfiguracao = descrItemConfiguracao.concat(" ...");
					else descrItemConfiguracao = new String("...");
			}
		}
		else
			descrItemConfiguracao = new String();
		
		return descrItemConfiguracao;
	}
	
	public String getDescrTipoPermissao() {
		if (this.tipoPermissaoSet != null && this.tipoPermissaoSet.size() > 0) {
			SrTipoPermissaoLista tipoPermissao = this.tipoPermissaoSet.get(0);	
			return tipoPermissao.getDescrTipoPermissaoLista().concat(" ...");
		}
		return "";
	}
	
	public String getDescrAcaoAtual() {
		String descrAcao = null;
		if (this.acoesSet != null && this.acoesSet.size() > 0) {
			SrAcao acao = this.acoesSet.get(this.acoesSet.size() -1);
			
			if (acao != null) {
				descrAcao = acao.getAtual().getTituloAcao();
				
				if (this.acoesSet.size() > 1)
					if (descrAcao != null)
						descrAcao = descrAcao.concat(" ...");
					else
						descrAcao = new String("...");
			}
		}
		else
			descrAcao = new String();
		
		return descrAcao;
	}

	/**
	 * Método que retorna um número referente ao tipo de solicitante
	 * selecionado. Esse número refere-se ao índice do item selecionado no
	 * componente pessoaLotaFuncCargoSelecao.html
	 * 
	 * @return <li>1 para Pessoa; <li>2 para Lotação; <li>3 para Funcao; <li>4
	 *         para Cargo;
	 */
	public int getTipoSolicitante() {
		if (this.getLotacao() != null
				&& this.getLotacao().getLotacaoAtual() != null)
			return 2;
		else if (this.getFuncaoConfianca() != null)
			return 3;
		else if (this.getCargo() != null)
			return 4;
		else if (this.getCpGrupo() != null)
			return 5;
		else
			return 1;
	}

	/**

	 * Retorna um Json de {@link SrConfiguracaoVO} que contém:
	 * <li> {@link SrListaConfiguracaoVO}</li>
	 * <li> {@link SrItemConfiguracaoVO}</li>
	 * <li> {@link SrAcaoVO}</li>
	 * 
	 */
	public String getSrConfiguracaoJson() {
		return new SrConfiguracaoVO(listaConfiguracaoSet, itemConfiguracaoSet, acoesSet, null).toJson();
	}

	public String getSrConfiguracaoTipoPermissaoJson() {
		return new SrConfiguracaoVO(null, null, null, tipoPermissaoSet).toJson();
	}

	/**
	 * Classe que representa um {@link SrConfiguracaoVO VO} da classe
	 * {@link SrConfiguracao}.
	 * 
	 * @author DB1
	 */
	public class SrConfiguracaoVO {
		public List<SrLista.SrListaVO> listaVO; 
		public List<SrItemConfiguracao.SrItemConfiguracaoVO> listaItemConfiguracaoVO;
		public List<SrAcao.SrAcaoVO> listaAcaoVO;
		public List<SrTipoPermissaoLista.SrTipoPermissaoListaVO> listaTipoPermissaoListaVO;

		public SrConfiguracaoVO(List<SrLista> listaConfiguracaoSet, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet, List<SrTipoPermissaoLista> tipoPermissaoSet) {
			listaVO = new ArrayList<SrLista.SrListaVO>();
			listaItemConfiguracaoVO = new ArrayList<SrItemConfiguracao.SrItemConfiguracaoVO>();
			listaAcaoVO = new ArrayList<SrAcao.SrAcaoVO>();
			listaTipoPermissaoListaVO = new ArrayList<SrTipoPermissaoLista.SrTipoPermissaoListaVO>();
			
			if(listaConfiguracaoSet != null)
				for (SrLista item : listaConfiguracaoSet) {
					listaVO.add(item.toVO());
				}
			
			if(itemConfiguracaoSet != null)
				for (SrItemConfiguracao item : itemConfiguracaoSet) {
					listaItemConfiguracaoVO.add(item.toVO());
				}
			
			if(acoesSet != null)
				for (SrAcao item : acoesSet) {
					listaAcaoVO.add(item.toVO());
				}

			if(tipoPermissaoSet != null)
				for (SrTipoPermissaoLista item : tipoPermissaoSet) {
					listaTipoPermissaoListaVO.add(item.toVO());
				}
		}

		/**
		 * Converte o objeto para Json.
		 */
		public String toJson() {
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting().serializeNulls();
			Gson gson = builder.create();

			return gson.toJson(this);
		}
	}

	public int getNivelItemParaComparar() {
		int soma = 0;
		if (itemConfiguracaoSet != null && itemConfiguracaoSet.size() > 0){
			for (SrItemConfiguracao i : itemConfiguracaoSet){
				SrItemConfiguracao iAtual = i.getAtual();
				if (iAtual != null)
					soma += i.getNivel();
			}
			return soma / itemConfiguracaoSet.size();
		}
		return 0;
	}
	
	public int getNivelAcaoParaComparar() {
		int soma = 0;
		if (acoesSet != null && acoesSet.size() > 0){
			for (SrAcao i : acoesSet){
				SrAcao iAtual = i.getAtual();
				if (iAtual != null)
					soma += i.getNivel();
			}
			return soma / acoesSet.size();
		}
		return 0;
	}

	public SrItemConfiguracao getItemConfiguracaoUnitario() {
		if (itemConfiguracaoSet == null || itemConfiguracaoSet.size() == 0)
			return null;
		return itemConfiguracaoSet.get(0);
	}

	public void setItemConfiguracaoUnitario(SrItemConfiguracao itemConfiguracao) {
		itemConfiguracaoSet = new ArrayList<SrItemConfiguracao>();
		itemConfiguracaoSet.add(itemConfiguracao);
	}

	public SrAcao getAcaoUnitaria() {
		if (acoesSet == null || acoesSet.size() == 0)
			return null;
		return acoesSet.get(0);
	}

	public void setAcaoUnitaria(SrAcao acao) {
		acoesSet = new ArrayList<SrAcao>();
		acoesSet.add(acao);
	}

	@Override
	public CpConfiguracao getConfiguracaoAtual() {
		return super.getConfiguracaoAtual();
	}
	
	public static SrConfiguracao buscarConfiguracaoInsercaoAutomaticaLista(SrLista lista) throws Exception {
		SrLista listaAtual = lista.getListaAtual();
		
		for (CpConfiguracao cpConfiguracao : SrConfiguracaoBL.get().getListaPorTipo(CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA)) {
			SrConfiguracao srConfiguracao = (SrConfiguracao) cpConfiguracao;
			// DB: Nao implementei utilizando "contains" na lista por que implementacao do super.equals esta comparando instancias e nao iria funcionar nesse caso
			for (SrLista listaEncontrada : srConfiguracao.getListaConfiguracaoSet()) {
				// DB1: Conversamos com o Edson e por enquanto sera apenas uma configuracao para cada lista.
				if (srConfiguracao.getListaConfiguracaoSet() != null && listaEncontrada.getId().equals(listaAtual.getId())) {
					return srConfiguracao;
				}
			}
		}
		return new SrConfiguracao();
	}

	public void adicionarListaConfiguracoes(SrLista srLista) {
		if (this.listaConfiguracaoSet == null) {
			this.listaConfiguracaoSet = new ArrayList<SrLista>();
		}
		if (!this.listaConfiguracaoSet.contains(srLista)) {
			this.listaConfiguracaoSet.add(srLista);
		}
	}
	
	
}