create table log_cancelamento (

    id bigint not null,
    medico_id bigint not null,
    paciente_id bigint not null,
    data datetime not null,
    data_cancelamento datetime not null,

    primary key(id),
    constraint fk_log_cancelamento_medico_id foreign key(medico_id) references medicos(id),
    constraint fk_log_cancelamento_paciente_id foreign key(paciente_id) references pacientes(id)
);