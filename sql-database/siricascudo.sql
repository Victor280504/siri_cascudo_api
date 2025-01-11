-- Create Database Comand: psql -U postgres -d siriCascudo -f caminho_para_o_dump.sql
-- Exemple: psql -U postgres -d siriCascudo -f siri-cascudo-api/sql-database/siricascudo.sql (Relative Rote by Github)

--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2024-11-26 19:37:36

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 16420)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16419)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.category_id_seq OWNER TO postgres;

--
-- TOC entry 4911 (class 0 OID 0)
-- Dependencies: 219
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- TOC entry 218 (class 1259 OID 16411)
-- Name: ingredient; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ingredient (
    id integer NOT NULL,
    quantity integer,
    description character varying,
    price numeric
);


ALTER TABLE public.ingredient OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16410)
-- Name: ingredient_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ingredient_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ingredient_id_seq OWNER TO postgres;

--
-- TOC entry 4912 (class 0 OID 0)
-- Dependencies: 217
-- Name: ingredient_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ingredient_id_seq OWNED BY public.ingredient.id;


--
-- TOC entry 225 (class 1259 OID 16455)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id integer NOT NULL,
    description character varying,
    image character varying,
    quantity integer,
    price numeric,
    id_category bigint NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16454)
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_id_seq OWNER TO postgres;

--
-- TOC entry 4913 (class 0 OID 0)
-- Dependencies: 224
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- TOC entry 226 (class 1259 OID 16468)
-- Name: recipe; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recipe (
    id_ingredient bigint NOT NULL,
    id_product bigint NOT NULL,
    quantity integer
);


ALTER TABLE public.recipe OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16441)
-- Name: sale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale (
    id integer NOT NULL,
    date date,
    payment_method character varying,
    id_user bigint NOT NULL
);


ALTER TABLE public.sale OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16440)
-- Name: sale_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sale_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sale_id_seq OWNER TO postgres;

--
-- TOC entry 4914 (class 0 OID 0)
-- Dependencies: 222
-- Name: sale_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sale_id_seq OWNED BY public.sale.id;


--
-- TOC entry 227 (class 1259 OID 16483)
-- Name: sale_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale_product (
    id_sale bigint NOT NULL,
    id_product bigint NOT NULL,
    quantity integer,
    value numeric
);


ALTER TABLE public.sale_product OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16400)
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id integer NOT NULL,
    name character varying,
    email character varying,
    password character varying,
    address character varying,
    login character varying
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16399)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_id_seq OWNER TO postgres;

--
-- TOC entry 4915 (class 0 OID 0)
-- Dependencies: 215
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;


--
-- TOC entry 221 (class 1259 OID 16428)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    id_user bigint NOT NULL,
    role character varying NOT NULL
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- TOC entry 4722 (class 2604 OID 16423)
-- Name: category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- TOC entry 4721 (class 2604 OID 16414)
-- Name: ingredient id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ingredient ALTER COLUMN id SET DEFAULT nextval('public.ingredient_id_seq'::regclass);


--
-- TOC entry 4724 (class 2604 OID 16458)
-- Name: product id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- TOC entry 4723 (class 2604 OID 16444)
-- Name: sale id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale ALTER COLUMN id SET DEFAULT nextval('public.sale_id_seq'::regclass);


--
-- TOC entry 4720 (class 2604 OID 16403)
-- Name: user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


--
-- TOC entry 4898 (class 0 OID 16420)
-- Dependencies: 220
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.category (id, name) FROM stdin;
\.


--
-- TOC entry 4896 (class 0 OID 16411)
-- Dependencies: 218
-- Data for Name: ingredient; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ingredient (id, quantity, description, price) FROM stdin;
\.


--
-- TOC entry 4903 (class 0 OID 16455)
-- Dependencies: 225
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, description, image, quantity, price, id_category) FROM stdin;
\.


--
-- TOC entry 4904 (class 0 OID 16468)
-- Dependencies: 226
-- Data for Name: recipe; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recipe (id_ingredient, id_product, quantity) FROM stdin;
\.


--
-- TOC entry 4901 (class 0 OID 16441)
-- Dependencies: 223
-- Data for Name: sale; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sale (id, date, payment_method, id_user) FROM stdin;
\.


--
-- TOC entry 4905 (class 0 OID 16483)
-- Dependencies: 227
-- Data for Name: sale_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sale_product (id_sale, id_product, quantity, value) FROM stdin;
\.


--
-- TOC entry 4894 (class 0 OID 16400)
-- Dependencies: 216
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (id, name, email, password, address, login) FROM stdin;
\.


--
-- TOC entry 4899 (class 0 OID 16428)
-- Dependencies: 221
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (id_user, role) FROM stdin;
\.


--
-- TOC entry 4916 (class 0 OID 0)
-- Dependencies: 219
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_id_seq', 1, false);


--
-- TOC entry 4917 (class 0 OID 0)
-- Dependencies: 217
-- Name: ingredient_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ingredient_id_seq', 1, false);


--
-- TOC entry 4918 (class 0 OID 0)
-- Dependencies: 224
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_id_seq', 1, false);


--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 222
-- Name: sale_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sale_id_seq', 1, false);


--
-- TOC entry 4920 (class 0 OID 0)
-- Dependencies: 215
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 1, false);


--
-- TOC entry 4732 (class 2606 OID 16427)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 4730 (class 2606 OID 16418)
-- Name: ingredient ingredient_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ingredient
    ADD CONSTRAINT ingredient_pkey PRIMARY KEY (id);


--
-- TOC entry 4726 (class 2606 OID 16409)
-- Name: user login; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT login UNIQUE (login) INCLUDE (login);


--
-- TOC entry 4738 (class 2606 OID 16462)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 4740 (class 2606 OID 16472)
-- Name: recipe recipe_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recipe
    ADD CONSTRAINT recipe_pkey PRIMARY KEY (id_ingredient, id_product);


--
-- TOC entry 4736 (class 2606 OID 16448)
-- Name: sale sale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (id);


--
-- TOC entry 4742 (class 2606 OID 16489)
-- Name: sale_product sale_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_product
    ADD CONSTRAINT sale_product_pkey PRIMARY KEY (id_sale, id_product);


--
-- TOC entry 4728 (class 2606 OID 16407)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 4734 (class 2606 OID 16434)
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id_user, role);


--
-- TOC entry 4745 (class 2606 OID 16463)
-- Name: product product_id_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_id_category_fkey FOREIGN KEY (id_category) REFERENCES public.category(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4746 (class 2606 OID 16478)
-- Name: recipe recipe_id_ingredient_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recipe
    ADD CONSTRAINT recipe_id_ingredient_fkey FOREIGN KEY (id_ingredient) REFERENCES public.ingredient(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4747 (class 2606 OID 16473)
-- Name: recipe recipe_id_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recipe
    ADD CONSTRAINT recipe_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.product(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4744 (class 2606 OID 16449)
-- Name: sale sale_id_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_id_user_fkey FOREIGN KEY (id_user) REFERENCES public."user"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4748 (class 2606 OID 16490)
-- Name: sale_product sale_product_id_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_product
    ADD CONSTRAINT sale_product_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.product(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4749 (class 2606 OID 16495)
-- Name: sale_product sale_product_id_sale_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_product
    ADD CONSTRAINT sale_product_id_sale_fkey FOREIGN KEY (id_sale) REFERENCES public.sale(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4743 (class 2606 OID 16435)
-- Name: user_roles user_roles_id_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_id_user_fkey FOREIGN KEY (id_user) REFERENCES public."user"(id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2024-11-26 19:37:37

--
-- PostgreSQL database dump complete
--

