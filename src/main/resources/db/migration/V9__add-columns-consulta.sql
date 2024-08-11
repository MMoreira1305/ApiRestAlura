ALTER TABLE consultas
ADD COLUMN data_cancelamento date,
ADD COLUMN motivo_cancelamento VARCHAR(100),
ADD COLUMN status_consulta VARCHAR(1);

update consultas set status_consulta = 'A';

ALTER TABLE consultas MODIFY COLUMN status_consulta varchar(1) NOT NULL;
