<c:if test="${solicitacao.solicitante}">
<script>

$(document).ready(function() {
	notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao');
});

function carregarAcao(){
	frm = document.getElementById('formSolicitacao');
	params = '';
	for (i = 0; i < frm.length; i++){
		if (frm[i].name && frm[i].value)
			params = params + frm[i].name + '=' + escape(frm[i].value) + '&';
	}
	PassAjaxResponseToFunction('@{Application.exibirAcao()}?' + params, 'carregouAcao', null, false, null);
}

function carregouAcao(response, param){
	var div = document.getElementById('divAcao');
	div.innerHTML = response;
	var scripts = div.getElementsByTagName("script");
	for(var i=0;i<scripts.length;i++)  
	   eval(scripts[i].text);
}
</script>

<div class="gt-form-row gt-width-66" >
	<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label> <sigasr:selecao tipo="item"
	nome="solicitacao.itemConfiguracao"
	value="${solicitacao.itemConfiguracao}" grande="true" onchange="carregarAcao();notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao')"
	params="sol.solicitante=${solicitacao.solicitante?.idPessoa}&sol.local=${solicitacao.local?.idComplexo}
				&sol.cadastrante.idPessoa=${cadastrante?.idPessoa}&sol.lotaCadastrante.idLotacao=${lotaTitular?.idLotacao}" /> <span style="color: red"><sigasr:error
		nome="solicitacao.itemConfiguracao" /></span>
</div>
<div id="divAcao"><jsp:include page="Application/exibirAcao.html" /></div>
</c:if>