-- DDL commands --

    alter table if exists CONTRACT 
        drop constraint if exists FK_contract__customer;

    alter table if exists CONTRACT_INVOICE_TYPE 
        drop constraint if exists FK_contract_invoice_type__contract;

    alter table if exists CONTRACT_INVOICE_TYPE 
        drop constraint if exists FK_contract_invoice_type__invoice_type;

    alter table if exists CONTRACT_PERSON 
        drop constraint if exists FK_contract_person__contract;

    alter table if exists CONTRACT_PERSON 
        drop constraint if exists FK_contract_person__person;

    alter table if exists CONTRACT_SERVER 
        drop constraint if exists FK_contract_server__contract;

    alter table if exists CONTRACT_SERVER 
        drop constraint if exists FK_contract_server__server;

    alter table if exists CONTRACT_TARIFF 
        drop constraint if exists FK_contract_tariff__contract;

    alter table if exists CONTRACT_TARIFF 
        drop constraint if exists FK_contract_tariff__tariff;

    alter table if exists CUSTOMER 
        drop constraint if exists FK_customer__contact_person;

    alter table if exists DAILY_USAGE 
        drop constraint if exists FK_daily_usage__production_level;

    alter table if exists DAILY_USAGE 
        drop constraint if exists FK_daily_usage__server;

    alter table if exists PRICE_LIST 
        drop constraint if exists FK_price_list__tariff;

    alter table if exists TRANSACTION 
        drop constraint if exists FK_transaction__contract;

    alter table if exists TRANSACTION 
        drop constraint if exists FK_transaction__transaction_type;

    alter table if exists TRANSACTION 
        drop constraint if exists FK_invoice__attachment;

    alter table if exists TRANSACTION 
        drop constraint if exists FK_invoice__invoice_type;

    drop table if exists CONTRACT cascade;

    drop table if exists CONTRACT_INVOICE_TYPE cascade;

    drop table if exists CONTRACT_PERSON cascade;

    drop table if exists CONTRACT_SERVER cascade;

    drop table if exists CONTRACT_TARIFF cascade;

    drop table if exists CUSTOMER cascade;

    drop table if exists DAILY_USAGE cascade;

    drop table if exists FILE cascade;

    drop table if exists INVOICE_TYPE cascade;

    drop table if exists PERSON cascade;

    drop table if exists PRICE_LIST cascade;

    drop table if exists PRODUCTION_LEVEL cascade;

    drop table if exists SERVER cascade;

    drop table if exists TARIFF cascade;

    drop table if exists TRANSACTION cascade;

    drop table if exists TRANSACTION_TYPE cascade;

    drop sequence if exists hibernate_sequence;

    create table if not exists CONTRACT (
        id int4 not null,
        lock_version int4 not null,
        balance numeric(16, 2) not null,
        evidence_number int4 not null,
        name varchar(250) not null,
        note varchar(1000),
        begin_date date not null,
        end_date date,
        customer_id int4 not null,
        primary key (id)
    );

    create table if not exists CONTRACT_INVOICE_TYPE (
        id int4 not null,
        lock_version int4 not null,
        begin_date date not null,
        end_date date,
        contract_id int4 not null,
        invoice_type_id int4 not null,
        primary key (id)
    );

    create table if not exists CONTRACT_PERSON (
        id int4 not null,
        contract_id int4 not null,
        person_id int4 not null,
        primary key (id)
    );

    create table if not exists CONTRACT_SERVER (
        id int4 not null,
        lock_version int4 not null,
        begin_date date not null,
        end_date date,
        contract_id int4 not null,
        server_id int4 not null,
        primary key (id)
    );

    create table if not exists CONTRACT_TARIFF (
        id int4 not null,
        lock_version int4 not null,
        begin_date date not null,
        end_date date,
        contract_id int4 not null,
        tariff_id int4 not null,
        primary key (id)
    );

    create table if not exists CUSTOMER (
        id int4 not null,
        lock_version int4 not null,
        billing_note varchar(1000),
        name varchar(250) not null,
        note varchar(1000),
        contact_person_id int4 not null,
        primary key (id)
    );

    create table if not exists DAILY_USAGE (
        id int4 not null,
        backup_gb numeric(10, 2),
        cpu int4,
        usage_date date not null,
        memory_mb int4,
        power_state boolean,
        prov_space_gb numeric(10, 2),
        server_name varchar(100) not null,
        used_space_gb numeric(10, 2),
        prod_level_id int4,
        server_id int4 not null,
        primary key (id)
    );

    create table if not exists FILE (
        id int4 not null,
        content oid,
        content_type varchar(250),
        name varchar(250) not null,
        size int8 not null,
        primary key (id)
    );

    create table if not exists INVOICE_TYPE (
        id int4 not null,
        title varchar(100) not null,
        primary key (id)
    );

    create table if not exists PERSON (
        id int4 not null,
        lock_version int4 not null,
        city varchar(100),
        city_part varchar(100),
        country varchar(100),
        desc_number varchar(15),
        orient_number varchar(8),
        street varchar(100),
        zip varchar(15),
        email varchar(250) not null,
        first_name varchar(250),
        note varchar(1000),
        phone varchar(40),
        second_name varchar(500),
        title_after varchar(30),
        title_before varchar(30),
        primary key (id)
    );

    create table if not exists PRICE_LIST (
        id int4 not null,
        lock_version int4 not null,
        backup_gb_price numeric(10, 2) not null,
        cpu_price numeric(10, 2) not null,
        memory_mb_price numeric(10, 2) not null,
        begin_date date not null,
        end_date date,
        space_gb_price numeric(10, 2) not null,
        tariff_id int4 not null,
        primary key (id)
    );

    create table if not exists PRODUCTION_LEVEL (
        id int4 not null,
        code varchar(20) not null,
        title varchar(250),
        primary key (id)
    );

    create table if not exists SERVER (
        id int4 not null,
        lock_version int4 not null,
        name varchar(100) not null,
        server_id varchar(50) not null,
        primary key (id)
    );

    create table if not exists TARIFF (
        id int4 not null,
        lock_version int4 not null,
        name varchar(250) not null,
        valid boolean not null,
        primary key (id)
    );

    create table if not exists TRANSACTION (
        disc varchar(3) not null,
        id int4 not null,
        lock_version int4 not null,
        amount numeric(16, 2) not null,
        decisive_date date not null,
        evidence_number int4,
        note varchar(1000),
        tx_order int4 not null,
        title varchar(250),
        begin_date date not null,
        end_date date,
        contract_id int4 not null,
        transaction_type_id int4 not null,
        attachment_id int4,
        invoice_type_id int4,
        primary key (id)
    );

    create table if not exists TRANSACTION_TYPE (
        id int4 not null,
        title varchar(100) not null,
        primary key (id)
    );

    alter table if exists CONTRACT 
        add constraint UK_contract__evidence_number  unique (evidence_number);

    alter table if exists CONTRACT_PERSON 
        add constraint UK_contract_person__contract__person  unique (contract_id, person_id);

    alter table if exists CUSTOMER 
        add constraint UK_customer__name  unique (name);

    alter table if exists DAILY_USAGE 
        add constraint UK_daily_usage__date__server_id  unique (usage_date, server_id);

    alter table if exists PERSON 
        add constraint UK_person__email  unique (email);

    alter table if exists PRODUCTION_LEVEL 
        add constraint UK_production_level__code  unique (code);

    alter table if exists SERVER 
        add constraint UK_server__server_id  unique (server_id);

    alter table if exists TRANSACTION 
        add constraint UK_transaction__tx_order__contract_id  unique (tx_order, contract_id);

    alter table if exists CONTRACT 
        add constraint FK_contract__customer 
        foreign key (customer_id) 
        references CUSTOMER;

    alter table if exists CONTRACT_INVOICE_TYPE 
        add constraint FK_contract_invoice_type__contract 
        foreign key (contract_id) 
        references CONTRACT;

    alter table if exists CONTRACT_INVOICE_TYPE 
        add constraint FK_contract_invoice_type__invoice_type 
        foreign key (invoice_type_id) 
        references INVOICE_TYPE;

    alter table if exists CONTRACT_PERSON 
        add constraint FK_contract_person__contract 
        foreign key (contract_id) 
        references CONTRACT;

    alter table if exists CONTRACT_PERSON 
        add constraint FK_contract_person__person 
        foreign key (person_id) 
        references PERSON;

    alter table if exists CONTRACT_SERVER 
        add constraint FK_contract_server__contract 
        foreign key (contract_id) 
        references CONTRACT;

    alter table if exists CONTRACT_SERVER 
        add constraint FK_contract_server__server 
        foreign key (server_id) 
        references SERVER;

    alter table if exists CONTRACT_TARIFF 
        add constraint FK_contract_tariff__contract 
        foreign key (contract_id) 
        references CONTRACT;

    alter table if exists CONTRACT_TARIFF 
        add constraint FK_contract_tariff__tariff 
        foreign key (tariff_id) 
        references TARIFF;

    alter table if exists CUSTOMER 
        add constraint FK_customer__contact_person 
        foreign key (contact_person_id) 
        references PERSON;

    alter table if exists DAILY_USAGE 
        add constraint FK_daily_usage__production_level 
        foreign key (prod_level_id) 
        references PRODUCTION_LEVEL;

    alter table if exists DAILY_USAGE 
        add constraint FK_daily_usage__server 
        foreign key (server_id) 
        references SERVER;

    alter table if exists PRICE_LIST 
        add constraint FK_price_list__tariff 
        foreign key (tariff_id) 
        references TARIFF;

    alter table if exists TRANSACTION 
        add constraint FK_transaction__contract 
        foreign key (contract_id) 
        references CONTRACT;

    alter table if exists TRANSACTION 
        add constraint FK_transaction__transaction_type 
        foreign key (transaction_type_id) 
        references TRANSACTION_TYPE;

    alter table if exists TRANSACTION 
        add constraint FK_invoice__attachment 
        foreign key (attachment_id) 
        references FILE;

    alter table if exists TRANSACTION 
        add constraint FK_invoice__invoice_type 
        foreign key (invoice_type_id) 
        references INVOICE_TYPE;

    create sequence hibernate_sequence;

-- initial DML commands --

    -- INVOICE_TYPE --
    insert into invoice_type (id, title) values(1, 'měsíční');
    insert into invoice_type (id, title) values(2, 'čtvrtletní');
    insert into invoice_type (id, title) values(3, 'pololetní');
    insert into invoice_type (id, title) values(4, 'roční');

    -- TRANSACTION_TYPE --
    insert into transaction_type (id, title) values(1, 'faktura');
    insert into transaction_type (id, title) values(2, 'příchozí platba');
    insert into transaction_type (id, title) values(3, 'extra položka');
    
    -- PRODUCTION_LEVEL --
    insert into production_level (id, code, title) values(1, 'p1', null);
    insert into production_level (id, code, title) values(2, 'p2', null);
    insert into production_level (id, code, title) values(3, 'p3', null);
    insert into production_level (id, code, title) values(4, 'testing', null);
    insert into production_level (id, code, title) values(5, 'off1', null);
    insert into production_level (id, code, title) values(6, 'off2', null);
    insert into production_level (id, code, title) values(7, 'off3', null);
    insert into production_level (id, code, title) values(8, 'off-testing', null);
