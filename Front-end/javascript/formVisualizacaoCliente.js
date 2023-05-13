function buscar() {
  const url = "http://localhost:8080/clientes-com-vendas";

  fetch(url)
    .then(response => response.json())
    .then(data => {
      const boxview = document.querySelector(".boxview");
      const infoCliente = document.querySelector(".info_cliente");

      // Remove linhas anteriores da tabela, mantendo apenas a primeira linha de cabeçalho
      while (infoCliente.rows.length > 1) {
        infoCliente.deleteRow(1);
      }

      // Adiciona cada cliente como uma nova linha na tabela
      data.forEach(cliente => {
        const row = infoCliente.insertRow();
        row.insertCell().textContent = cliente.Nome_Cliente;
        row.insertCell().textContent = cliente.Nome_Gerencia;
        row.insertCell().textContent = cliente.nome;

        row.cells[0].classList.add("Nome_Cliente");
        row.cells[1].classList.add("Nome_Gerencia");
        row.cells[2].classList.add("nome");
       });

      // Exibe a boxview
    })
    .catch(error => console.error(error));
};

// Executa a função buscar


// Executa a função buscar assim que o documento HTML é completamente carregado
document.addEventListener('DOMContentLoaded', buscar);


const botao = document.getElementById("botao");
botao.addEventListener("click", function() {
  window.location.href = "formCliente.html";
});