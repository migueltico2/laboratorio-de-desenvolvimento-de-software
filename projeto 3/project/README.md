# laboratorio-de-desenvolvimento-de-software

## Histórias de usuários

Eu como usuário quero gerenciar meus dados pessoais para que eu possa atualizar, consultar ou apagar meus dados quando necessário.

Eu como usuário quero poder logar no sistema para que eu possa ter acesso aos serviços disponíveis.

Eu como professor quero poder transferir moedas para alunos para que eu possa recompensar o desempenho deles.

Eu como professor quero poder consultar o meu saldo de moedas para que eu possa verificar quantos moedas tenho disponíveis.

Eu como professor quero poder consultar o extrato de transações para que eu possa verificar os detalhes das moedas enviadas.

Eu como aluno quero ser notificado quando ganhar moedas para que eu possa ser informado sobre o ganho de moedas.

Eu como aluno quero poder consultar o meu saldo de moedas para que eu possa verificar quantos moedas tenho disponíveis.

Eu como aluno quero poder consultar o extrato de transações para que eu possa verificar os detalhes das moedas enviadas e recebidas.

Eu como aluno quero poder resgatar vantagens para que eu possa trocar moedas por vantagens disponíveis.

Eu como aluno quero poder consultar minhas vantagens disponíveis para que eu possa verificar as vantagens que posso resgatar.

Eu como aluno quero poder resgatar vantagens para que eu possa trocar moedas pelos benefícios disponíveis.

Eu como aluno quero ser receber o código para o resgate da vantagem para que eu possa utilizá-lo presencialmente.

Eu como instiuição quero poder me afiliar ao programa para que eu possa fornecer uma lista de professores.

Eu como instituição quero que, ao início de cada semestre, o sistema adicione 1000 moedas para cada professor para que eles possam recompenar os alunos.

Eu como empresa parceira quero poder me afiliar ao programa para que eu possa disponibilizar vantagens para os alunos.

Eu como empresa parceira quero ser notificada quando um aluno resgata uma vantagem para que eu possa conferir a troca.

## Mapeamanto Modelo ER

User(id, name, email, password)
Student(id, CFP, RG, address, course, user_id, account_id, institution_id)
Professor(id, CFP, department, user_id, account_id, institution_id)
Enterprise(id, CNPJ, type, user_id)
Account(id, coins)
Advantage(id, name, coins, description, image, enterprise_id)
History(id, name, coins, description, image, advantage_id, professor_id, student_id)