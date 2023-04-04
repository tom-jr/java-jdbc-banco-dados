# Conhecendo o ByteBank
O projeto tem por objetivo realizar transações com um banco de dados usando a biblioteca do JDBC. Para economia
de tempo foi disponibilizado um projeto que será o ponto de partida para que possamos iniciar já na implementação.

# Instalação do Banco de dados
No curso se usa o MYSQL no meu caso irei usar o PostgreSQL pois tenho mais experiencia e tempo de desenvolvimento nele

Então podemos criar um banco de dados com o nome de *byte_bank*
~~~ sql
create table conta (
    numero int not null ,
    saldo decimal(10,0) default null,
    cliente_nome varchar(60) default null,
    cliente_cpf varchar(11) default null,
    client_email varchar(120) default null,
    primary key (numero)
);
~~~
