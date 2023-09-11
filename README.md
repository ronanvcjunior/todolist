# ToDoList

Use o comando abaixo para executar o toDoList na pasta determinada:

```bash
java -jar todolist.jar
```

Com isso irá criar uma pasta anônima chamada `.todolist/` e dentro dela dois arquivos `.csv`, o `tarefas.csv`, que salva as informações das tarefas salvas. E o arquivo `categorias.csv`, com as informações das categorias salvas.

## Seguem as opções:

- help: Mostra todas as opções disponíveis 
- add <nome> <dataTermino>: adiciona uma nova tarefa
- update <nome>: para fazer update em uma determinada tarefa, segue as opções:
  - -n <novoNome>: para renomear a tarefa
  - -d <descricao>: para alterar a descrição da tarefa
  - -t <dataTermino>: para alterar a data de termino da tarefa
  - -p <prioridade>: para alterar a prioridade da tarefa (valores de 1 até 5)
  - -c <categoria>: para adicionar uma categoria para a tarefa
  - -s <status>: para alterar o status da tarefa para as opções (ToDo, Doing e Done)
- tarefa list: para mostrar a lista de tarefas (ordenação padrão por prioridade), segue as opções:
  - -t: para mostrar a lista de tarefas por data de termino da tarefa
  - -s: para mostrar a lista de tarefas por status da tarefa
- delete <nome>: para deletar uma tarefa da lista
- status list: para mostrar a lista de status
- categoria <categoria>: para criar uma nova categoria
- categoria list: para mostrar a lista de categorias
- exit: Sai do programa


- Observações:
  - palavras compostas entre aspas ("")
  - formato da data (dd/MM/yyyy)

## FrontEnd

Pode ser visualizado com o link: https://ronanvcjunior.github.io/todolist/frontend/

usa-se o jquery e o select2.