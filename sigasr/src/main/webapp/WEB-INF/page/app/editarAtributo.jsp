<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Cadastro de Atributo">

<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script src="/sigasr/public/javascripts/detalhe-tabela.js"></script>

<script type="text/javascript">

	function ocultaAssociacoes(){
		if ($("#objetivo").val() == 1){
			$("#associacoes").show();
		} else {
			$("#associacoes").hide();
		}
	}

	var tableAssociacao,
		colunas = {};
	colunas.botaoExpandir =               0;
	colunas.idItemConfiguracao =          1;
	colunas.tituloItemConfiguracao =      2;
	colunas.siglaItemConfiguracao =       3;
	colunas.idAcao =                      4;
	colunas.tituloAcao =                  5;
	colunas.siglaAcao =                   6;
	colunas.atributoObrigatorio =  		  7;
	colunas.atributoObrigatorioString =   8;
	colunas.idAssociacao =                9;
	colunas.botaoExcluir =                10;

	//removendo a referencia de '$' para o jQuery
	$.noConflict();

	var table = null;
	
	jQuery( document ).ready(function( $ ) {

		ocultaAssociacoes();
		
		/* Table initialization */
		table = $('#associacao_table')
			.on('draw.dt', function() {
						if(table) {
							var btn = $('.bt-expandir'),
								expandir = btn.hasClass('expandido');
							
							$('#associacao_table').mostrarDetalhes(detalhesListaAssociacao, table);
							$('#associacao_table').expandirContrairLinhas(expandir);
						}
					})
			.dataTable({
			"language": {
				"emptyTable":     "Não existem resultados",
			    "info":           "Mostrando de _START_ a _END_ do total de _TOTAL_ registros",
			    "infoEmpty":      "Mostrando de 0 a 0 do total de 0 registros",
			    "infoFiltered":   "(filtrando do total de _MAX_ registros)",
			    "infoPostFix":    "",
			    "thousands":      ".",
			    "lengthMenu":     "Mostrar _MENU_ registros",
			    "loadingRecords": "Carregando...",
			    "processing":     "Processando...",
			    "search":         "Filtrar:",
			    "zeroRecords":    "Nenhum registro encontrado",
			    "paginate": {
			        "first":      "Primeiro",
			        "last":       "Último",
			        "next":       "Próximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordenação crescente",
			        "sortDescending": ": clique para ordenação decrescente"
			    }
			},
			"columnDefs": [{
				"targets": [4],
				"searchable": false,
				"sortable" : false
			},
			{
				"targets": [colunas.idItemConfiguracao, 
							colunas.siglaItemConfiguracao, 
							colunas.idAcao, 
							colunas.siglaAcao,
							colunas.atributoObrigatorio,
							colunas.idAssociacao],
				"visible": false
			}]
		});

		$('#associacao_table tbody').on('click', 'tr', function () {
			var itemSelecionado = table.api().row(this).data();
			
			if (itemSelecionado != undefined) {
				table.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	            
				atualizarAssociacaoModal(itemSelecionado);
			    associacaoModalAbrir(true);
			}
		});
		$('#associacao_table').mostrarDetalhes(detalhesListaAssociacao, table);

		tableAssociacao = table;
	});

	function inserirAssociacao() {
		limparDadosAssociacaoModal();
		associacaoModalAbrir(false);
	};

	function associacaoModalAbrir(isEdicao) {
		isEditing = isEdicao;
		
		if (isEdicao)
			$("#associacao_dialog").dialog('option', 'title', 'Alterar Associação');
		else
			$("#associacao_dialog").dialog('option', 'title', 'Incluir Associação');
		
		$("#associacao_dialog").dialog('open');
	};

	$("#associacao_dialog").dialog({
	    autoOpen: false,
	    height: 'auto',
	    width: 'auto',
	    modal: true,
	    resizable: false
	});

	function detalhesListaAssociacao(d) {
		var tr = $('<tr class="detail">'),
			td = $('<td colspan="6">'),
			table = $('<table class="datatable" cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">');
			
		table.append(htmlConteudo(d, "Item de configuração:", colunas.siglaItemConfiguracao, colunas.tituloItemConfiguracao));
		table.append(htmlConteudo(d, "Ação:", colunas.siglaAcao, colunas.tituloAcao));
		
		td.append(table);
		tr.append(td);
	    
	    return tr;
	};

	function htmlConteudo(d, titulo, indiceSigla, indiceDescricao) {
		var trItem = $('<tr>'),
			tdTitulo = $('<td><b>' + titulo + '</b></td'),
			tdConteudo = $('<td>'),
			table = $('<table>'),
			trDetalhe = $('<tr>'),
			tdSigla = $('<td>' + d[indiceSigla] + "</td>"),
			tdDescricao = $('<td>' +  d[indiceDescricao] + '</td>');
		
		trDetalhe.append(tdSigla);
		trDetalhe.append(tdDescricao);
		table.append(trDetalhe);
		tdConteudo.append(table);
		trItem.append(tdTitulo);
		return trItem.append(tdConteudo);
	};

	// Limpa os dados da tela.
	function limparDadosAssociacaoModal() {

		$("#itemConfiguracaoUnitario").val('');
		$("#itemConfiguracaoUnitario_descricao").val('');
		$("#itemConfiguracaoUnitario_sigla").val('');
		$("#itemConfiguracaoUnitarioSpan").html('');
		$("#acaoUnitaria").val('');
		$("#acaoUnitaria_descricao").val('');
		$("#acaoUnitaria_sigla").val('');
		$("#acaoUnitariaSpan").html('');
		$("#checkatributoObrigatorio")[0].checked = false;
	}
	
	// Alimenta os campos do Popup antes de abrir ao usuário.
	function atualizarAssociacaoModal(itemArray) {
		limparDadosAssociacaoModal();

		// Atualiza campos Booleanos
		itemArray[colunas.atributoObrigatorio] = transformStringToBoolean(itemArray[colunas.atributoObrigatorio]);

		// Atualiza campos Selecao
		$("#itemConfiguracaoUnitario").val(itemArray[colunas.idItemConfiguracao]);
		$("#itemConfiguracaoUnitario_descricao").val(itemArray[colunas.tituloItemConfiguracao]);
		$("#itemConfiguracaoUnitario_sigla").val(itemArray[colunas.siglaItemConfiguracao]);
		$("#itemConfiguracaoUnitarioSpan").html(itemArray[colunas.tituloItemConfiguracao]);
		$("#acaoUnitaria").val(itemArray[colunas.idAcao]);
		$("#acaoUnitaria_descricao").val(itemArray[colunas.tituloAcao]);
		$("#acaoUnitaria_sigla").val(itemArray[colunas.siglaAcao]);
		$("#acaoUnitariaSpan").html(itemArray[colunas.tituloAcao]);
		$("#idConfiguracao").val(itemArray[colunas.idAssociacao]);
	}

	function transformStringToBoolean(value) {
		if (value.constructor.name == 'String')
			return value == 'true';
		else
			return value;
	}

	function serializeAssociacao(row) {
		var params = "";
		
		// caso exista algum item na tabela
		//if (row[colunas.idItemConfiguracao] != '' && row[colunas.idItemConfiguracao] > 0)
			params += '&associacao.itemConfiguracaoUnitario=' + row[colunas.idItemConfiguracao];
		
		//if (row[colunas.idAcao] != '' && row[colunas.idAcao] > 0)
        	params += '&associacao.acaoUnitaria=' + row[colunas.idAcao];
		
		if (row[colunas.atributoObrigatorio] != '')
        	params += '&associacao.atributoObrigatorio=' + row[colunas.atributoObrigatorio];
		
		if (row[colunas.idAssociacao] != '')
        	params += '&associacao.idConfiguracao=' + row[colunas.idAssociacao];

		if ($("#idAtributo").val() != undefined && $("#idAtributo").val() != '')
			params += '&associacao.atributo.idAtributo=' + $("#idAtributo").val();

		return params;
	};

	function gravarAssociacao() {
		var idAssociacao = $("#idConfiguracao").val() != undefined ? $("#idConfiguracao").val() : '';
		
		var row = [
					'',                                                                    // colunas.botaoExpandir
					$("#itemConfiguracaoUnitario").val(),                                  // colunas.idItemConfiguracao
					formatDescricaoLonga($("#itemConfiguracaoUnitario_descricao").val()),  // colunas.tituloItemConfiguracao
					$("#itemConfiguracaoUnitario_sigla").val(),                            // colunas.siglaItemConfiguracao
					$("#acaoUnitaria").val(),											   // colunas.idAcao
					formatDescricaoLonga($("#acaoUnitaria_descricao").val()),              // colunas.tituloAcao
					$("#acaoUnitaria_sigla").val(),                                        // colunas.siglaAcao
					$("#checkatributoObrigatorio")[0].checked,							   // colunas.atributoObrigatorio
					getAtributoObrigatorioString(),										   // colunas.atributoObrigatorioString
					idAssociacao,														   // colunas.idAssociacao
					''                                                                     // colunas.botaoExcluir
	   			];

		$.ajax({
	         type: "POST",
	         url: "@{Application.gravarAssociacao()}",
	         data: serializeAssociacao(row),
	         dataType: "text",
	         success: function(response) {
		        row[colunas.idAssociacao] = response;
				var html = 
				'<td class="gt-celula-nowrap" style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">' +
					'<a class="once desassociar" onclick="desassociar(event, ${assoc?.idConfiguracao})" title="Remover permissão">' +
						'<input class="idAssociacao" type="hidden" value="${assoc?.idConfiguracao}"/>' +
						'<img id="imgCancelar" src="/siga/css/famfamfam/icons/cancel_gray.png" style="margin-right: 5px;">' + 
					'</a>' +
				'</td>';
						         
		        row[colunas.botaoExcluir] = html;

	          	if (isEditing) {
	          		tableAssociacao.api().row('.selected').data(row);
			    }
			    else {
			    	tableAssociacao.api().row.add(row).draw();
			    }
	          	associacaoModalFechar();
				$('.desassociar').bind('click', desassociar);
	         },
	         error: function(response) {
	        	$('#modal-associacao').hide(); 

	        	var modalErro = $('#"modal-associacao-error"');
	        	modalErro.find("h3").html(response.responseText);
	        	modalErro.show(); 
	         }
       });

	}
	
	function associacaoModalFechar() {
		isEditing = false;
		$("#associacao_dialog").dialog("close");
		limparDadosAssociacaoModal();
	}

	function formatDescricaoLonga(descricao) {
		if (descricao != null) {
			return descricao + " ...";
		}
		return descricao;
	}

	function getAtributoObrigatorioString() {
		var isChecked = $("#checkatributoObrigatorio")[0].checked;
		return isChecked ? "Sim": "Não";
	}

	function desassociar(event, idAssociacaoDesativar) {
		event.stopPropagation()
		
		var me = $(this),
			tr = $(event.currentTarget).parent().parent()[0],
			row = table.api().row(tr).data(),
			idAssociacao = idAssociacaoDesativar ? idAssociacaoDesativar : row[colunas.idAssociacao];
			idAtributo = $("#idAtributo").val();
			
			location.href='@{Application.desativarAssociacaoEdicao()}?' + jQuery.param({idAtributo : idAtributo, idAssociacao : idAssociacao});
	}

</script>

<div class="gt-bd clearfix">
	<div class="gt-content">
		<h2>Cadastro de Atributo</h2>

		<div class="gt-form gt-content-box">
			<form action="@Application.gravarAtributo()" enctype="multipart/form-data" onsubmit="javascript: return block()"> 
			<c:if test="${att?.idAtributo}"> <input
				type="hidden" name="att.idAtributo"
				id="idAtributo"
				value="${att.idAtributo}"> </c:if>
			<c:if test="${not empty errors}">
			<p class="gt-error">Alguns campos obrigatórios não foram
				preenchidos <sigasr:error/></p>
			</c:if>
			<div class="gt-form-row gt-width-66">
				<label>Nome</label> <input type="text"
					name="att.nomeAtributo"
					value="${att?.nomeAtributo}" size="60" />
					<span style="color: red"><sigasr:error
					nome="att.nomeAtributo" /></span>
			</div>
			<div class="gt-form-row gt-width-66">
				<label>Descrição</label> <input type="text"
					name="att.descrAtributo"
					value="${att?.descrAtributo}" size="60" />
			</div>
			<div class="gt-form-row gt-width-66">
				<label>C&oacute;digo</label> <input type="text"
					name="att.codigoAtributo"
					value="${att?.codigoAtributo}" size="60" />
			</div>
			<div class="gt-form-row gt-width-66">
				<label>Objetivo do atributo</label> <sigasr:select id="objetivo" name="att.objetivoAtributo.idObjetivo" items="${objetivos}"
				labelProperty="descrObjetivo" valueProperty="idObjetivo" value="${att.objetivoAtributo.idObjetivo}" style="width:393px;"
				onchange="javascript:ocultaAssociacoes();" />
			</div>
			<div class="gt-form-row gt-width-66">
				<label>Tipo de atributo</label> <sigasr:select name="att.tipoAtributo" items="${SrTipoAtributo.values()}"
				labelProperty="descrTipoAtributo" value="${tipoAtributoAnterior}" style="width:393px;" />
			</div>
			<div class="gt-form-row gt-width-66" id="vlPreDefinidos" style="display: none;">
				<label>Valores pré-definidos (Separados por ponto-e-vígula(;))</label> 
				<input type="text"
					name="att.descrPreDefinido"
					id="descrPreDefinido"
					value="${att?.descrPreDefinido}" size="60" />
					<span style="color: red" id="erroDescrPreDefinido"><sigasr:error
					nome="att.descrPreDefinido" /></span>
			</div>
			<div id="associacoes">
			<div class="container">
				<div class="title-table">
					<h3 style="padding-top: 25px;">Associações</h3>
				</div>
			</div>
			<div class="gt-content-box dataTables_div">
				<table id="associacao_table" border="0" class="gt-table display">
					<thead>
						<tr>
							<th style="color: #333">
								<button class="gt-btn-medium gt-btn-left bt-expandir">
									<span id="iconeBotaoExpandirTodos">+</span>
								</button>
							</th>
							<th>idItemConfiguracao</th>
							<th><i>Item</i></th>
							<th>siglaItemConfiguracao</th>
							<th>idAcao</th>
							<th>Ação</th>
							<th>siglaAcao</th>
							<th>Obrigatório</th>
							<th>Obrigatório</th>
							<th>idAssociacao</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${associacoes}" var="assoc">
						<tr>
							<td class="gt-celula-nowrap details-control"></td>
							<td>${assoc.itemConfiguracaoUnitario?.atual?.idItemConfiguracao}</td>
							<td>${assoc.itemConfiguracaoUnitario?.atual?.tituloItemConfiguracao}</td>
							<td>${assoc.itemConfiguracaoUnitario?.atual?.siglaItemConfiguracao}</td>
							<td>${assoc.acaoUnitaria?.atual?.idAcao}</td>
							<td>${assoc.acaoUnitaria?.atual?.tituloAcao}</td>
							<td>${assoc.acaoUnitaria?.atual?.siglaAcao}</td>
							<td>${assoc.atributoObrigatorio}</td>
							<td>${assoc.atributoObrigatorioString}</td>
							<td>${assoc.idConfiguracao}</td>
							<td class="gt-celula-nowrap" style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
								<a class="once desassociar" onclick="desassociar(event, ${assoc?.idConfiguracao})" title="Remover permissão">
									<input class="idAssociacao" type="hidden" value="${assoc?.idConfiguracao}"/>
									<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;"> 
								</a>
							</td>
						</tr>
						</a>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="gt-table-buttons">
							<a href="javascript: inserirAssociacao()" class="gt-btn-medium gt-btn-left">Incluir</a>
						</div>
			</div><!-- id="associacoes" -->
			<div class="gt-form-row">
				<input type="submit" value="Gravar"
					class="gt-btn-medium gt-btn-left" />
				<a href="@{Application.listarAtributo}" class="gt-btn-medium gt-btn-left">Cancelar</a>
			</div>
			</form>
		</div>
	</div>
</div>

<sigasr:modal nome="associacao" titulo="Cadastrar Associa&ccedil;&atilde;o">
	<div class="gt-form gt-content-box">
		<input id="idConfiguracao" type="hidden" name="idConfiguracao"> 
	
		<div class="gt-form-row gt-width-100">
			<label>Item</label> 
			<sigasr:selecao tipo="item"
				nome="itemConfiguracaoUnitario"
				value="${itemConfiguracaoUnitario?.atual}" />
		</div>
	
		<div class="gt-form-row gt-width-100">
			<label>Serviço</label> 
			<sigasr:selecao tipo="acao"
			nome="acaoUnitaria" value="${acaoUnitaria?.atual}" />
		</div>
		<div class="gt-form-row">
			<label><siga:checkbox name="atributoObrigatorio"
				value="${atributoObrigatorio}" /> Obrigatório</label>
		</div>
		<div class="gt-form-row">
			<a href="javascript: gravarAssociacao()" class="gt-btn-medium gt-btn-left">Gravar</a>
			<a href="javascript: associacaoModalFechar()" class="gt-btn-medium gt-btn-left">Cancelar</a>
		</div>
	</div>
	<div class="gt-content-box" id="modal-associacao-error" style="display: none;">
		<table width="100%">
			<tr>
				<td align="center" valign="middle">
					<table class="form" width="50%">
						<tr>
							<td style="text-align: center; padding-top: 10px;">
								<h3></h3>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</sigasr:modal>

<script type="text/javascript">
function verificarTipoAtributo() {
	if($("select[name='att.tipoAtributo']").val() === 'VL_PRE_DEFINIDO') {
		$('#vlPreDefinidos').show();
		return;
	}
	$('#vlPreDefinidos').hide();
};

verificarTipoAtributo();

$("select[name='att.tipoAtributo']").change(function() {
	verificarTipoAtributo(); 
});

if($('#erroDescrPreDefinido').html()) {
	$("select[name='att.tipoAtributo']").val('VL_PRE_DEFINIDO');
	$('#vlPreDefinidos').show();
};
</script>