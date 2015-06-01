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
        drop constraint if exists FK_daily_usage__daily_import;
        
    alter table if exists DAILY_USAGE 
        drop constraint if exists FK_daily_usage__production_level;

    alter table if exists DAILY_USAGE 
        drop constraint if exists FK_daily_usage__server;

    alter table if exists INVOICE_DAILY_USAGE 
        drop constraint if exists FK_invoice_daily_usage__daily_usage;

    alter table if exists INVOICE_DAILY_USAGE 
        drop constraint if exists FK_invoice_daily_usage__invoice;        
        
    alter table if exists INVOICE_PRICE_LIST 
        drop constraint if exists FK_invoice_price_list__invoice;

    alter table if exists INVOICE_PRICE_LIST 
        drop constraint if exists FK_invoice_price_list__price_list;

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
    
    drop table if exists DAILY_IMPORT cascade;

    drop table if exists DAILY_USAGE cascade;

    drop table if exists FILE cascade;
    
    drop table if exists INVOICE_DAILY_USAGE cascade;

    drop table if exists INVOICE_PRICE_LIST cascade;

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
        lock_version int4 not null,
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

    create table if not exists DAILY_IMPORT (
        id int4 not null,
        lock_version int4 not null,
        all_lines int4,
        import_date date not null,
        error_lines int4,
        import_begin_timestamp timestamp not null,
        import_end_timestamp timestamp,
        ok_lines int4,
        protocol text,
        report text,
        report_name varchar(100) not null,
        success boolean,
        warn_lines int4,
        primary key (id)
    );
    
    create table if not exists DAILY_USAGE (
        id int4 not null,
        lock_version int4 not null,
        backup_gb numeric(10, 2) not null,
        cpu int4 not null,
        memory_mb int4 not null,
        power_state boolean not null,
        prov_space_gb numeric(10, 2) not null,
        server_name varchar(100) not null,
        used_space_gb numeric(10, 2) not null,
        daily_import_id int4 not null,
        prod_level_id int4 not null,
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

    create table if not exists INVOICE_DAILY_USAGE (
        id int4 not null,
        lock_version int4 not null,
        daily_usage_id int4 not null,
        invoice_id int4 not null,
        primary key (id)
    );    
    
    create table if not exists INVOICE_PRICE_LIST (
        id int4 not null,
        lock_version int4 not null,
        invoice_id int4 not null,
        price_list_id int4 not null,
        primary key (id)
    );

    create table if not exists INVOICE_TYPE (
        id int4 not null,
        divisor int4 not null,
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
        no_price_list boolean,
        begin_date date,
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

    alter table if exists CONTRACT_INVOICE_TYPE 
        add constraint UK_contract_invoice_type__contract_id__invoice_type_id__begin_date  unique (contract_id, invoice_type_id, begin_date);

    alter table if exists CONTRACT_PERSON 
        add constraint UK_contract_person__contract_id__person_id  unique (contract_id, person_id);

    alter table if exists CONTRACT_SERVER 
        add constraint UK_contract_server__contract_id__server_id__begin_date  unique (contract_id, server_id, begin_date);

    alter table if exists CONTRACT_TARIFF 
        add constraint UK_contract_tariff__contract_id__tariff_id__begin_date  unique (contract_id, tariff_id, begin_date);

    alter table if exists CUSTOMER 
        add constraint UK_customer__name  unique (name);

    alter table if exists DAILY_IMPORT 
        add constraint UK_daily_import__date  unique (import_date);        
        
    alter table if exists DAILY_USAGE 
        add constraint UK_daily_usage__daily_import_id__server_id  unique (daily_import_id, server_id);

    alter table if exists INVOICE_DAILY_USAGE 
        add constraint UK_invoice_daily_usage__invoice_id__daily_usage_id  unique (invoice_id, daily_usage_id);        
        
    alter table if exists INVOICE_PRICE_LIST 
        add constraint UK_invoice_price_list__invoice_id__price_list_id  unique (invoice_id, price_list_id);

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
        add constraint FK_daily_usage__daily_import 
        foreign key (daily_import_id) 
        references DAILY_IMPORT;        
        
    alter table if exists DAILY_USAGE 
        add constraint FK_daily_usage__production_level 
        foreign key (prod_level_id) 
        references PRODUCTION_LEVEL;

    alter table if exists DAILY_USAGE 
        add constraint FK_daily_usage__server 
        foreign key (server_id) 
        references SERVER;

    alter table if exists INVOICE_DAILY_USAGE 
        add constraint FK_invoice_daily_usage__daily_usage 
        foreign key (daily_usage_id) 
        references DAILY_USAGE;

    alter table if exists INVOICE_DAILY_USAGE 
        add constraint FK_invoice_daily_usage__invoice 
        foreign key (invoice_id) 
        references TRANSACTION;        
        
    alter table if exists INVOICE_PRICE_LIST 
        add constraint FK_invoice_price_list__invoice 
        foreign key (invoice_id) 
        references TRANSACTION;

    alter table if exists INVOICE_PRICE_LIST 
        add constraint FK_invoice_price_list__price_list 
        foreign key (price_list_id) 
        references PRICE_LIST;

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
    insert into invoice_type (id, title, divisor) values(1, 'měsíční', 1);
    insert into invoice_type (id, title, divisor) values(2, 'čtvrtletní', 3);
    insert into invoice_type (id, title, divisor) values(3, 'pololetní', 6);
    insert into invoice_type (id, title, divisor) values(4, 'roční', 12);

    -- TRANSACTION_TYPE --
    insert into transaction_type (id, title) values(1, 'faktura');
    insert into transaction_type (id, title) values(2, 'příchozí platba');
    insert into transaction_type (id, title) values(3, 'extra položka');
    
    -- PRODUCTION_LEVEL --
    insert into production_level (id, code, title) values(1, 'init', null);
    insert into production_level (id, code, title) values(2, 'p1', null);
    insert into production_level (id, code, title) values(3, 'p2', null);
    insert into production_level (id, code, title) values(4, 'p3', null);
    insert into production_level (id, code, title) values(5, 'testing', null);
    insert into production_level (id, code, title) values(6, 'off1', null);
    insert into production_level (id, code, title) values(7, 'off2', null);
    insert into production_level (id, code, title) values(8, 'off3', null);
    insert into production_level (id, code, title) values(9, 'off-testing', null);

