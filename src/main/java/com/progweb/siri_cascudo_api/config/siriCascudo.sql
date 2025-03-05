-- Criação do banco de dados (se necessário)
-- CREATE DATABASE siriCascudo;

-- Comando: psql -U postgres -d siriCascudo -f siri-cascudo-api/sql-database/siricascudo.sql

-- Configurações iniciais
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
'' SET standard_conforming_strings = on;
SELECT pg_catalog.set_config ('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
SET default_tablespace = '';
SET default_table_access_method = heap;

-- Definir o esquema padrão como "public"
SET search_path TO public;

-- Tabela category
CREATE TABLE public.category (
    id bigint NOT NULL,
    name character varying(255)
);
ALTER TABLE public.category OWNER TO postgres;

-- Sequência para category
CREATE SEQUENCE public.category_id_seq AS integer START
WITH
    1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.category_id_seq OWNER TO postgres;
ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;

-- Tabela customer
CREATE TABLE public.customer (
    id bigint NOT NULL,
    name character varying(255),
    email character varying(255),
    password character varying(255),
    address character varying(255),
    wallet double precision
);
ALTER TABLE public.customer OWNER TO postgres;

-- Tabela customer_roles
CREATE TABLE public.customer_roles (
    role character varying(255) NOT NULL,
    id_user bigint NOT NULL
);
ALTER TABLE public.customer_roles OWNER TO postgres;

-- Tabela ingredient
CREATE TABLE public.ingredient (
    id bigint NOT NULL,
    quantity integer,
    description character varying(255),
    price double precision
);
ALTER TABLE public.ingredient OWNER TO postgres;

-- Sequência para ingredient
CREATE SEQUENCE public.ingredient_id_seq AS integer START
WITH
    1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.ingredient_id_seq OWNER TO postgres;
ALTER SEQUENCE public.ingredient_id_seq OWNED BY public.ingredient.id;

-- Tabela product
CREATE TABLE public.product (
    id bigint NOT NULL,
    name character varying(255),
    description character varying(255),
    image character varying(255),
    price double precision,
    id_category bigint NOT NULL
);
ALTER TABLE public.product OWNER TO postgres;

-- Sequência para product
CREATE SEQUENCE public.product_id_seq AS integer START
WITH
    1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.product_id_seq OWNER TO postgres;
ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;

-- Tabela recipe
CREATE TABLE public.recipe (
    id_ingredient bigint NOT NULL,
    id_product bigint NOT NULL,
    quantity integer
);
ALTER TABLE public.recipe OWNER TO postgres;

-- Tabela sale
CREATE TABLE public.sale (
    id bigint NOT NULL,
    date timestamp(6) without time zone,
    payment_method character varying(255),
    id_user bigint NOT NULL
);
ALTER TABLE public.sale OWNER TO postgres;

-- Sequência para sale
CREATE SEQUENCE public.sale_id_seq AS integer START
WITH
    1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.sale_id_seq OWNER TO postgres;
ALTER SEQUENCE public.sale_id_seq OWNED BY public.sale.id;

-- Tabela sale_product
CREATE TABLE public.sale_product (
    id_sale bigint NOT NULL,
    id_product bigint NOT NULL,
    quantity integer,
    value double precision
);
ALTER TABLE public.sale_product OWNER TO postgres;

-- Sequência para customer
CREATE SEQUENCE public.user_id_seq AS integer START
WITH
    1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.user_id_seq OWNER TO postgres;
ALTER SEQUENCE public.user_id_seq OWNED BY public.customer.id;

-- Definindo valores padrão para as colunas de ID
ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);
ALTER TABLE ONLY public.customer ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);
ALTER TABLE ONLY public.ingredient ALTER COLUMN id SET DEFAULT nextval('public.ingredient_id_seq'::regclass);
ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);
ALTER TABLE ONLY public.sale ALTER COLUMN id SET DEFAULT nextval('public.sale_id_seq'::regclass);

-- Chaves primárias
ALTER TABLE ONLY public.category
ADD CONSTRAINT category_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.customer_roles
ADD CONSTRAINT custumer_roles_pkey PRIMARY KEY (id_user, role);
ALTER TABLE ONLY public.ingredient
ADD CONSTRAINT ingredient_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.product
ADD CONSTRAINT product_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.recipe
ADD CONSTRAINT recipe_pkey PRIMARY KEY (id_ingredient, id_product);
ALTER TABLE ONLY public.sale
ADD CONSTRAINT sale_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.sale_product
ADD CONSTRAINT sale_product_pkey PRIMARY KEY (id_sale, id_product);
ALTER TABLE ONLY public.customer
ADD CONSTRAINT user_pkey PRIMARY KEY (id);

-- Chaves estrangeiras com CASCADE
ALTER TABLE ONLY public.customer_roles
ADD CONSTRAINT fk3lgh3fxwlhwfbijwnv930gur1 FOREIGN KEY (id_user) REFERENCES public.customer (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY public.product
ADD CONSTRAINT product_id_category_fkey FOREIGN KEY (id_category) REFERENCES public.category (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY public.recipe
ADD CONSTRAINT recipe_id_ingredient_fkey FOREIGN KEY (id_ingredient) REFERENCES public.ingredient (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY public.recipe
ADD CONSTRAINT recipe_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.product (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY public.sale
ADD CONSTRAINT sale_id_customer_fkey FOREIGN KEY (id_user) REFERENCES public.customer (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY public.sale_product
ADD CONSTRAINT sale_product_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.product (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY public.sale_product
ADD CONSTRAINT sale_product_id_sale_fkey FOREIGN KEY (id_sale) REFERENCES public.sale (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- Função para atualizar a quantidade de ingredientes
CREATE OR REPLACE FUNCTION update_ingredient_quantity()
RETURNS TRIGGER AS $$
BEGIN
    -- Atualiza a quantidade de cada ingrediente usado na receita do produto vendido
    UPDATE public.ingredient
    SET quantity = public.ingredient.quantity - (r.quantity * NEW.quantity)
    FROM public.recipe r
    WHERE r.id_product = NEW.id_product
    AND public.ingredient.id = r.id_ingredient;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para atualizar a quantidade de ingredientes após uma venda
CREATE TRIGGER trg_update_ingredient_quantity
AFTER INSERT ON public.sale_product
FOR EACH ROW
EXECUTE FUNCTION update_ingredient_quantity();

-- Inserção do usuário admin
INSERT INTO public.customer (name, email, password, address, wallet)
VALUES ('admin', 'admin@siricascudo.com', 'Admin123*', 'Admin Address, 00', 200.00);

-- Definindo o papel de administrador para o usuário admin
INSERT INTO public.customer_roles (role, id_user)
VALUES ('ADMIN', (SELECT id FROM public.customer WHERE email = 'admin@siricascudo.com'));