--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.0
-- Dumped by pg_dump version 9.5.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

CREATE TABLE candidato (
    idcandidato bigint DEFAULT nextval(('public.candidato_idcandidato_seq'::text)::regclass) NOT NULL,
    nome character varying(100),
    cpf character varying(11) NOT NULL,
    datanascimento date,
    nomemae character varying(100),
    email character varying(100) NOT NULL,
    endereco character varying(250)
);

CREATE SEQUENCE candidato_idcandidato_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE inscricao (
    idinscricao integer NOT NULL,
    idcandidato integer NOT NULL,
    idprograma integer NOT NULL
);

CREATE TABLE programa (
    idprograma integer NOT NULL,
    descricao character varying(50)
);

ALTER TABLE ONLY candidato
    ADD CONSTRAINT candidato_cpf_key UNIQUE (cpf);

ALTER TABLE ONLY candidato
    ADD CONSTRAINT candidato_email_key UNIQUE (email);

ALTER TABLE ONLY candidato
    ADD CONSTRAINT candidato_pkey PRIMARY KEY (idcandidato);

ALTER TABLE ONLY inscricao
    ADD CONSTRAINT inscricao_idcandidato_key UNIQUE (idcandidato);

ALTER TABLE ONLY inscricao
    ADD CONSTRAINT inscricao_idprograma_key UNIQUE (idprograma);

ALTER TABLE ONLY inscricao
    ADD CONSTRAINT inscricao_pkey PRIMARY KEY (idinscricao);

ALTER TABLE ONLY programa
    ADD CONSTRAINT programa_pkey PRIMARY KEY (idprograma);

ALTER TABLE ONLY inscricao
    ADD CONSTRAINT inscricao_idcandidato_fkey FOREIGN KEY (idcandidato) REFERENCES candidato(idcandidato);

ALTER TABLE ONLY inscricao
    ADD CONSTRAINT inscricao_idprograma_fkey FOREIGN KEY (idprograma) REFERENCES programa(idprograma);