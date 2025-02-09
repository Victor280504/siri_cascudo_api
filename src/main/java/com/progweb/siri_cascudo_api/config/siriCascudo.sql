-- Criação do banco de dados (se necessário)
-- CREATE DATABASE siriCascudo;

-- Comand: psql -U postgres -d siriCascudo -f siri-cascudo-api/sql-database/siricascudo.sql

-- Conectando ao banco de dados
\c siriCascudo;

-- Configurações iniciais
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
SET default_tablespace = '';
SET default_table_access_method = heap;

-- Tabela: category
CREATE TABLE public.category (
    id integer NOT NULL,
    name character varying
);
ALTER TABLE public.category OWNER TO postgres;

-- Sequência: category_id_seq
CREATE SEQUENCE public.category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.category_id_seq OWNER TO postgres;
ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;

-- Tabela: ingredient
CREATE TABLE public.ingredient (
    id integer NOT NULL,
    quantity integer,
    description character varying,
    price numeric
);
ALTER TABLE public.ingredient OWNER TO postgres;

-- Sequência: ingredient_id_seq
CREATE SEQUENCE public.ingredient_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.ingredient_id_seq OWNER TO postgres;
ALTER SEQUENCE public.ingredient_id_seq OWNED BY public.ingredient.id;

-- Tabela: product
CREATE TABLE public.product (
    id integer NOT NULL,
    name character varying,
    description character varying,
    image character varying,
    quantity integer,
    price numeric,
    id_category bigint NOT NULL
);
ALTER TABLE public.product OWNER TO postgres;

-- Sequência: product_id_seq
CREATE SEQUENCE public.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.product_id_seq OWNER TO postgres;
ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;

-- Tabela: recipe
CREATE TABLE public.recipe (
    id_ingredient bigint NOT NULL,
    id_product bigint NOT NULL,
    quantity integer
);
ALTER TABLE public.recipe OWNER TO postgres;

-- Tabela: sale
CREATE TABLE public.sale (
    id integer NOT NULL,
    date date,
    payment_method character varying,
    id_user bigint NOT NULL
);
ALTER TABLE public.sale OWNER TO postgres;

-- Sequência: sale_id_seq
CREATE SEQUENCE public.sale_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.sale_id_seq OWNER TO postgres;
ALTER SEQUENCE public.sale_id_seq OWNED BY public.sale.id;

-- Tabela: sale_product
CREATE TABLE public.sale_product (
    id_sale bigint NOT NULL,
    id_product bigint NOT NULL,
    quantity integer,
    value numeric
);
ALTER TABLE public.sale_product OWNER TO postgres;

-- Tabela: user
CREATE TABLE public."user" (
    id integer NOT NULL,
    name character varying,
    email character varying,
    password character varying,
    address character varying
);
ALTER TABLE public."user" OWNER TO postgres;

-- Sequência: user_id_seq
CREATE SEQUENCE public.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.user_id_seq OWNER TO postgres;
ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;

-- Tabela: user_roles
CREATE TABLE public.user_roles (
    id_user bigint NOT NULL,
    role character varying NOT NULL
);
ALTER TABLE public.user_roles OWNER TO postgres;

-- Definindo valores padrão para as colunas de ID
ALTER TABLE ONLY public.category
    ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);

ALTER TABLE ONLY public.ingredient
    ALTER COLUMN id SET DEFAULT nextval('public.ingredient_id_seq'::regclass);

ALTER TABLE ONLY public.product
    ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);

ALTER TABLE ONLY public.sale
    ALTER COLUMN id SET DEFAULT nextval('public.sale_id_seq'::regclass);

ALTER TABLE ONLY public."user"
    ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);

-- Inserindo dados nas tabelas
COPY public.category (id, name) FROM stdin;
1   Categoria1
2   Categoria2
\.

COPY public.ingredient (id, quantity, description, price) FROM stdin;
1   10  Ingrediente1  5.50
2   20  Ingrediente2  3.00
\.

COPY public.product (id, name, description, image, quantity, price, id_category) FROM stdin;
1   Produto1  Descrição1  imagem1.jpg  5  10.00  1
2   Produto2  Descrição2  imagem2.jpg  3  15.00  2
\.

COPY public.recipe (id_ingredient, id_product, quantity) FROM stdin;
1   1   2
2   2   1
\.

COPY public.sale (id, date, payment_method, id_user) FROM stdin;
1   2024-01-01  Cartão  1
2   2024-01-02  Dinheiro  2
\.

COPY public.sale_product (id_sale, id_product, quantity, value) FROM stdin;
1   1   1   10.00
2   2   2   30.00
\.

COPY public."user" (id, name, email, password, address) FROM stdin;
1   Usuário1  usuario1@example.com  senha1  Endereço1
2   Usuário2  usuario2@example.com  senha2  Endereço2
\.

COPY public.user_roles (id_user, role) FROM stdin;
1   ROLE_ADMIN
2   ROLE_user
\.

-- Definindo chaves primárias
ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);

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

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id_user, role);

-- Definindo chaves estrangeiras
ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_id_category_fkey FOREIGN KEY (id_category) REFERENCES public.category(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY public.recipe
    ADD CONSTRAINT recipe_id_ingredient_fkey FOREIGN KEY (id_ingredient) REFERENCES public.ingredient(id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE ONLY public.recipe
    ADD CONSTRAINT recipe_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.product(id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_id_user_fkey FOREIGN KEY (id_user) REFERENCES public."user"(id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE ONLY public.sale_product
    ADD CONSTRAINT sale_product_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.product(id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE ONLY public.sale_product
    ADD CONSTRAINT sale_product_id_sale_fkey FOREIGN KEY (id_sale) REFERENCES public.sale(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_id_user_fkey FOREIGN KEY (id_user) REFERENCES public."user"(id) ON UPDATE CASCADE ON DELETE CASCADE;

-- Função para recalcular a quantidade de produtos com base nos ingredientes
CREATE OR REPLACE FUNCTION public.update_product_quantity()
RETURNS TRIGGER AS $$
BEGIN
    -- Atualiza a quantidade de todos os produtos que dependem dos ingredientes
    UPDATE public.product p
    SET quantity = (
        SELECT COALESCE(MIN(i.quantity / r.quantity), 0)
        FROM public.recipe r
        JOIN public.ingredient i ON r.id_ingredient = i.id
        WHERE r.id_product = p.id
    )
    WHERE EXISTS (
        SELECT 1
        FROM public.recipe r
        WHERE r.id_product = p.id
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para atualizar a quantidade de produtos após INSERT, UPDATE ou DELETE na tabela recipe
CREATE TRIGGER trg_recipe_change
AFTER INSERT OR UPDATE OR DELETE ON public.recipe
FOR EACH ROW
EXECUTE FUNCTION public.update_product_quantity();

-- Trigger para atualizar a quantidade de produtos após UPDATE na tabela ingredient
CREATE TRIGGER trg_ingredient_update
AFTER UPDATE ON public.ingredient
FOR EACH ROW
EXECUTE FUNCTION public.update_product_quantity();

-- Finalizando
COMMIT;