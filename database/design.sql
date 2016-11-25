--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.3
-- Dumped by pg_dump version 9.1.3
-- Started on 2015-05-12 08:10:33

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 187 (class 3079 OID 11639)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2020 (class 0 OID 0)
-- Dependencies: 187
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 161 (class 1259 OID 101235)
-- Dependencies: 6
-- Name: aiconstant; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE aiconstant (
    id integer NOT NULL,
    airule_id integer,
    name2 character varying(50),
    datatype character varying(1) NOT NULL,
    datavalue character varying(250),
    description character varying(200),
    datavaluetext text
);


ALTER TABLE public.aiconstant OWNER TO postgres;

--
-- TOC entry 162 (class 1259 OID 101241)
-- Dependencies: 6 161
-- Name: aiconstant_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE aiconstant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.aiconstant_id_seq OWNER TO postgres;

--
-- TOC entry 2021 (class 0 OID 0)
-- Dependencies: 162
-- Name: aiconstant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE aiconstant_id_seq OWNED BY aiconstant.id;


--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 162
-- Name: aiconstant_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('aiconstant_id_seq', 3191, true);


--
-- TOC entry 163 (class 1259 OID 101243)
-- Dependencies: 6
-- Name: aimodel; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE aimodel (
    id integer NOT NULL,
    datecreate timestamp with time zone,
    version character varying(50),
    isactive integer,
    body text,
    target character varying(50),
    mimetype character varying(150),
    datechange timestamp with time zone
);


ALTER TABLE public.aimodel OWNER TO postgres;

--
-- TOC entry 164 (class 1259 OID 101249)
-- Dependencies: 6 163
-- Name: aimodel_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE aimodel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.aimodel_id_seq OWNER TO postgres;

--
-- TOC entry 2023 (class 0 OID 0)
-- Dependencies: 164
-- Name: aimodel_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE aimodel_id_seq OWNED BY aimodel.id;


--
-- TOC entry 2024 (class 0 OID 0)
-- Dependencies: 164
-- Name: aimodel_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('aimodel_id_seq', 1, true);


--
-- TOC entry 165 (class 1259 OID 101251)
-- Dependencies: 6
-- Name: aimodelparams; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE aimodelparams (
    aimodel_id integer NOT NULL,
    name character varying(250) NOT NULL,
    valuetext text,
    valuenumber integer,
    valuedate timestamp with time zone,
    valuefloat double precision
);


ALTER TABLE public.aimodelparams OWNER TO postgres;

--
-- TOC entry 166 (class 1259 OID 101257)
-- Dependencies: 6
-- Name: airule; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE airule (
    id integer NOT NULL,
    packagename character varying(100),
    body text,
    ruletype integer,
    description character varying(200),
    kbase character varying(30)
);


ALTER TABLE public.airule OWNER TO postgres;

--
-- TOC entry 167 (class 1259 OID 101263)
-- Dependencies: 6 166
-- Name: airule_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE airule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.airule_id_seq OWNER TO postgres;

--
-- TOC entry 2025 (class 0 OID 0)
-- Dependencies: 167
-- Name: airule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE airule_id_seq OWNED BY airule.id;


--
-- TOC entry 2026 (class 0 OID 0)
-- Dependencies: 167
-- Name: airule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('airule_id_seq', 44, true);


--
-- TOC entry 168 (class 1259 OID 101265)
-- Dependencies: 6
-- Name: attr_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE attr_id_seq
    START WITH 31
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attr_id_seq OWNER TO sa;

--
-- TOC entry 2027 (class 0 OID 0)
-- Dependencies: 168
-- Name: attr_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('attr_id_seq', 39, true);


--
-- TOC entry 169 (class 1259 OID 101267)
-- Dependencies: 1937 6
-- Name: attributes; Type: TABLE; Schema: public; Owner: sa; Tablespace: 
--

CREATE TABLE attributes (
    id integer DEFAULT nextval('attr_id_seq'::regclass) NOT NULL,
    name text,
    value text,
    id_element integer
);


ALTER TABLE public.attributes OWNER TO sa;

--
-- TOC entry 170 (class 1259 OID 101274)
-- Dependencies: 1938 1940 1941 6
-- Name: bizaction; Type: TABLE; Schema: public; Owner: sa; Tablespace: 
--

CREATE TABLE bizaction (
    id integer NOT NULL,
    bizactiontype_id integer NOT NULL,
    signalref character varying(150),
    businessobjectclass character varying(150),
    description character varying(150),
    assignee character varying(150),
    candidategroups character varying(250),
    candidateusers character varying(250),
    forone integer,
    formany integer,
    sqlfilter text,
    datafilter text,
    schedule character varying(250),
    runprocessdefkey character varying(150),
    issystem integer,
    retryinterval integer,
    errorcode character varying(50),
    errormessage text,
    laststart timestamp with time zone,
    lastend timestamp with time zone,
    processdefkey character varying(150),
    ruleenabled text,
    ruleaction text,
    isactive integer DEFAULT 0 NOT NULL,
    isrequired integer,
    isatomic integer,
    plugin character varying(50),
    listid integer,
    iscsingleton integer DEFAULT 0 NOT NULL,
    next_id integer,
    txversion integer DEFAULT 0
);


ALTER TABLE public.bizaction OWNER TO sa;

--
-- TOC entry 171 (class 1259 OID 101281)
-- Dependencies: 6 170
-- Name: bizaction_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE bizaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bizaction_id_seq OWNER TO sa;

--
-- TOC entry 2028 (class 0 OID 0)
-- Dependencies: 171
-- Name: bizaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--

ALTER SEQUENCE bizaction_id_seq OWNED BY bizaction.id;


--
-- TOC entry 2029 (class 0 OID 0)
-- Dependencies: 171
-- Name: bizaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('bizaction_id_seq', 54, true);


--
-- TOC entry 186 (class 1259 OID 402233)
-- Dependencies: 6
-- Name: bizactionevent; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE bizactionevent (
    id integer NOT NULL,
    bizaction_id integer NOT NULL,
    eventcode integer NOT NULL
);


ALTER TABLE public.bizactionevent OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 402231)
-- Dependencies: 6 186
-- Name: bizactionevent_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE bizactionevent_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bizactionevent_id_seq OWNER TO postgres;

--
-- TOC entry 2030 (class 0 OID 0)
-- Dependencies: 185
-- Name: bizactionevent_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE bizactionevent_id_seq OWNED BY bizactionevent.id;


--
-- TOC entry 2031 (class 0 OID 0)
-- Dependencies: 185
-- Name: bizactionevent_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('bizactionevent_id_seq', 4, true);


--
-- TOC entry 172 (class 1259 OID 101283)
-- Dependencies: 6
-- Name: bizactiontype; Type: TABLE; Schema: public; Owner: sa; Tablespace: 
--

CREATE TABLE bizactiontype (
    id integer NOT NULL,
    name2 character varying(250)
);


ALTER TABLE public.bizactiontype OWNER TO sa;

--
-- TOC entry 173 (class 1259 OID 101286)
-- Dependencies: 6
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: sa; Tablespace: 
--

CREATE TABLE databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20)
);


ALTER TABLE public.databasechangelog OWNER TO sa;

--
-- TOC entry 174 (class 1259 OID 101292)
-- Dependencies: 6
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: sa; Tablespace: 
--

CREATE TABLE databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO sa;

--
-- TOC entry 175 (class 1259 OID 101295)
-- Dependencies: 6
-- Name: desc_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE desc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.desc_id_seq OWNER TO sa;

--
-- TOC entry 2032 (class 0 OID 0)
-- Dependencies: 175
-- Name: desc_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('desc_id_seq', 31, true);


--
-- TOC entry 176 (class 1259 OID 101297)
-- Dependencies: 1942 6
-- Name: elements; Type: TABLE; Schema: public; Owner: sa; Tablespace: 
--

CREATE TABLE elements (
    id integer DEFAULT nextval('desc_id_seq'::regclass) NOT NULL,
    id_parent integer,
    type text,
    value text,
    id_etap integer,
    nn integer
);


ALTER TABLE public.elements OWNER TO sa;

--
-- TOC entry 2033 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN elements.nn; Type: COMMENT; Schema: public; Owner: sa
--

COMMENT ON COLUMN elements.nn IS 'порядковый номер';


--
-- TOC entry 177 (class 1259 OID 101304)
-- Dependencies: 6
-- Name: etap_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE etap_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.etap_id_seq OWNER TO sa;

--
-- TOC entry 2034 (class 0 OID 0)
-- Dependencies: 177
-- Name: etap_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('etap_id_seq', 1, true);


--
-- TOC entry 178 (class 1259 OID 101306)
-- Dependencies: 1943 6
-- Name: etap; Type: TABLE; Schema: public; Owner: sa; Tablespace: 
--

CREATE TABLE etap (
    id integer DEFAULT nextval('etap_id_seq'::regclass) NOT NULL,
    name text
);


ALTER TABLE public.etap OWNER TO sa;

--
-- TOC entry 179 (class 1259 OID 101313)
-- Dependencies: 6
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 2035 (class 0 OID 0)
-- Dependencies: 179
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 3829, true);


--
-- TOC entry 180 (class 1259 OID 101315)
-- Dependencies: 6
-- Name: holidays; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE holidays (
    id integer NOT NULL,
    databeg date,
    dataend date,
    kind integer,
    name character varying(30)
);


ALTER TABLE public.holidays OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 101318)
-- Dependencies: 180 6
-- Name: holidays_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE holidays_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.holidays_id_seq OWNER TO postgres;

--
-- TOC entry 2036 (class 0 OID 0)
-- Dependencies: 181
-- Name: holidays_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE holidays_id_seq OWNED BY holidays.id;


--
-- TOC entry 2037 (class 0 OID 0)
-- Dependencies: 181
-- Name: holidays_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('holidays_id_seq', 1, false);


--
-- TOC entry 182 (class 1259 OID 101320)
-- Dependencies: 6
-- Name: report; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE report (
    id integer NOT NULL,
    reporttype_id integer NOT NULL,
    name2 character varying(200),
    body text,
    reportexecutor character varying(200),
    mimetype character varying(200)
);


ALTER TABLE public.report OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 101326)
-- Dependencies: 6 182
-- Name: report_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.report_id_seq OWNER TO postgres;

--
-- TOC entry 2038 (class 0 OID 0)
-- Dependencies: 183
-- Name: report_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE report_id_seq OWNED BY report.id;


--
-- TOC entry 2039 (class 0 OID 0)
-- Dependencies: 183
-- Name: report_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('report_id_seq', 1, true);


--
-- TOC entry 184 (class 1259 OID 101328)
-- Dependencies: 6
-- Name: reporttype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE reporttype (
    id integer NOT NULL,
    name2 character varying(200)
);


ALTER TABLE public.reporttype OWNER TO postgres;

--
-- TOC entry 1934 (class 2604 OID 101331)
-- Dependencies: 162 161
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY aiconstant ALTER COLUMN id SET DEFAULT nextval('aiconstant_id_seq'::regclass);


--
-- TOC entry 1935 (class 2604 OID 101332)
-- Dependencies: 164 163
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY aimodel ALTER COLUMN id SET DEFAULT nextval('aimodel_id_seq'::regclass);


--
-- TOC entry 1936 (class 2604 OID 101333)
-- Dependencies: 167 166
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY airule ALTER COLUMN id SET DEFAULT nextval('airule_id_seq'::regclass);


--
-- TOC entry 1939 (class 2604 OID 101334)
-- Dependencies: 171 170
-- Name: id; Type: DEFAULT; Schema: public; Owner: sa
--

ALTER TABLE ONLY bizaction ALTER COLUMN id SET DEFAULT nextval('bizaction_id_seq'::regclass);


--
-- TOC entry 1946 (class 2604 OID 402236)
-- Dependencies: 185 186 186
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bizactionevent ALTER COLUMN id SET DEFAULT nextval('bizactionevent_id_seq'::regclass);


--
-- TOC entry 1944 (class 2604 OID 101335)
-- Dependencies: 181 180
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY holidays ALTER COLUMN id SET DEFAULT nextval('holidays_id_seq'::regclass);


--
-- TOC entry 1945 (class 2604 OID 101336)
-- Dependencies: 183 182
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report ALTER COLUMN id SET DEFAULT nextval('report_id_seq'::regclass);


--
-- TOC entry 2000 (class 0 OID 101235)
-- Dependencies: 161
-- Data for Name: aiconstant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY aiconstant (id, airule_id, name2, datatype, datavalue, description, datavaluetext) FROM stdin;
3612	7	winPay.payUrl	C	http://win-pay24.ru/partner/28/payout	\N	\N
3793	7	winPay.statusUrl	C	http://win-pay24.ru/partner/28/check	\N	\N
3584	7	winPay.syncMode	C	ASYNC	\N	\N
28	8	cryptoservice.provider.default	C	SC	\N	\N
27	8	cryptoservice.securerandom.path	C	/tmp/microcredit/securerandoms/	\N	\N
30	8	cryptoservice.spep.url	C	http://195.245.214.33:7777/esv/?wsdl	\N	\N
31	8	cryptoservice.certificate.validation.interval	N	86400000	\N	\N
35	10	1	C	alfaMarengo,manualPay	\N	\N
37	10	2	C	alfaMarengo,manualPay	\N	\N
39	10	4	C	yandexPay,manualPay	\N	\N
44	11	credit.stake.default	F	0.025	\N	\N
40	10	5	C	qiwiPay,manualPay	\N	\N
38	10	3	C	contactPay,manualPay	\N	\N
66	19	waitPayment	N	2	\N	\N
69	23	sms.prolong.declined	C	Продление по вашему займу отменено	\N	\N
72	26	waitChange	C	P2D	\N	\N
1	2	credit.sum.min	F	1000.00	Минимальная сумма займа для незарегистрированного пользователя	\N
3	2	credit.stake.min	F	0.02	Минимальная ставка для незарегистрированного пользователя	\N
5	2	credit.stake.max	F	0.04	Максимальная ставка для незарегистрированного пользователя	\N
10	2	credit.stake.default	F	0.04	Ставка по умолчанию для незарегистрированного пользователя	\N
6	2	credit.days.min	N	2	Минимальное количество дней для незарегистрированного пользователя	\N
2	2	credit.sum.max	F	10000.00	Максимальная сумма займа для незарегистрированного пользователя	\N
1696	8	cryptoservice.keysDir	C	c:\\\\Users\\\\helen1\\\\1\\\\	\N	\N
3137	17	email.credit.overdue.subject	C	Вы просрочили выплату займа	Заголовок письма о просрочке займа	\N
3138	17	email.credit.overdue.body	C	\N	Текст письма о просрочке займа	Я Вам доверяю, но Вы просрочили срок возврата займа. Прошу, верните займ или продлите его срок как можно скорее. Ваш Растороп.
3596	7	rusStandart.useWork	N	0	\N	\N
3139	30	default.body	C	\N	\N	Я Вам доверяю, но Вы просрочили срок возврата займа. Прошу, верните займ или продлите его срок как можно скорее. Ваш Растороп.
3141	30	default.subject	C	Вы просрочили выплату займа	\N	\N
3140	29	sms.name	C	Я Вам доверяю, но у Вас заканчивается срок возврата займа. Прошу, верните займ или продлите его срок как можно скорее. Ваш Растороп. 8 (800) 775-22-05	\N	\N
3570	7	okbCais.useWork	N	0	\N	\N
3142	7	rusStandart.requestScoring	N	0	\N	\N
3143	32	sms.login.additional	C	cassaonline@yandex.ru	Логин для запасного сервера смс	\N
3144	32	sms.password.additional	C	qwertyu123	Пароль для запасного сервера смс	\N
3145	32	sms.url.additional	C	https://gate.smsaero.ru/send/	Запасной url для сервера смс	\N
3146	32	sms.login	C	cassaonline	Логин для сервера смс	\N
3147	32	sms.password	C	xopygibu	Пароль для сервера смс	\N
3148	32	sms.url	C	https://www.smstraffic.ru/multi.php	Url для сервера смс	\N
3149	32	sms.from	C	RASTOROP	От кого смс	\N
3152	33	email.prefix	C	yandex.ru	Url для сервера email	\N
3153	34	socnetwork.url	C	http://rastorop.ru/main/rest/social/	Url для соц.сетей	\N
3154	35	geo.url	C	http://ipgeobase.ru:7020/geo?ip=	Url для гео.информации	\N
3151	33	email.password	C	qwertyu123	Пароль для сервера email	\N
3150	33	email.login	C	mail@rastorop.ru	Логин для сервера email	\N
3675	7	contactPay.useFake	N	0	\N	\N
3762	7	okbIdv.useWork	N	0	\N	\N
3798	7	rusStandart.requestAntifrod	N	0	\N	\N
3157	37	refinance.days.max	N	30	Максимально дней рефинансирования	\N
76	17	sms.credit.overdue	C	Я Вам доверяю, но у Вас заканчивается срок возврата займа. Прошу, верните займ или продлите его срок как можно скорее. Ваш Растороп. 8 (800) 775-22-05	Тест смс о просрочке займа	\N
3158	37	refinance.days.overdue.available	N	14	На какой день просрочки предлагается рефинансирование	\N
3159	37	refinance.days.wait.payment	N	2	Сколько дней ждем оплаты рефинансирования	\N
3156	37	refinance.stake	F	0.001	Процентная ставка при рефинансировании	\N
3155	37	refinance.payment.sum.min	F	500.00	Минимальная сумма погашения при рефинансировании	\N
3163	41	waitRefinance	N	2	\N	\N
3164	41	waitDraft	C	P1D	\N	\N
57	17	sms.credit.ready	C	Вам отправлен заём на сумму %5.0f рублей. Ваш Растороп. 8 (800) 775-22-05	Текст смс отправления займа	\N
22	5	sms.accepted	C	Поздравляю, Ваш заём на сумму %5.0f рублей одобрен. Теперь Вам необходимо подписать договор в личном кабинете. Сразу после этого я отправлю Вам деньги. Ваш Растороп. 8 (800) 775-22-05	Текст смс одобрения займа	\N
24	5	sms.rejected	C	К сожалению, мы не можем выдать Вам заём на сумму %5.0f рублей. Ваш Растороп. 8 (800) 775-22-05	Текст смс отказа в займе	\N
62	5	email.accepted.body	C	\N	Текст письма одобрения займа	<p>Поздравляем!</p><p>Вам одобрен заём на сумму %5.0f рублей на %d дней.</p><p>Для получения займа, Вам необходимо подписать оферту <a href="http://rastorop.ru/client"> в своем личном кабинете.</a> </p><p>Сразу после этого я отправлю Вам деньги.</p><p>Ваш <b>Растороп</b> 8 (800) 775-22-05</p>
3129	17	sms.rejected	C	К сожалению, мы не можем выдать Вам займ на сумму %5.0f рублей. Ваш Растороп. 8 (800) 775-22-05	\N	\N
68	23	sms.prolong.accepted	C	Ваш займ продлён до %1$td/%1$tm/%1$ty включительно. Ваш Растороп. 8 (800) 775-22-05	\N	\N
3126	27	email.success.subject	C	Регистрация в системе микрозаймов Растороп	\N	\N
3127	27	email.success.body	C	\N	\N	<p>Поздравляем!</p>\n                <p>Ваша заявка на получение займа принята и будет обработана в ближайшее время. Теперь у Вас есть личный кабинет, которым Вы можете пользоваться для получения информации о статусе заявок, а также для получения новых займов.</p>\n                <p>В целях безопасности просим удалить это письмо. Благодарим Вас за использование нашей системы. </p>\n                <p>Ваш <b>Растороп</b> 8 (800) 775-22-05.</p>            \n                       \n        
3191	40	bonus.pay.max.sum	F	3500	Максимальная сумма, которую можно оплатить бонусами	\N
3128	27	sms.success.password	C	Пароль от личного кабинета Растороп %s. Обязательно смените при первом входе. Ваш Растороп. 8 (800) 775-22-05\n        	\N	\N
65	17	email.credit.ready.body	C	\N	Текст письма отправления займа	<p>Поздравляем!</p><p>Вам отправлена сумма займа %5.0f рублей.</p><p>Посмотреть информацию про займ, а также погасить его, Вы можете <a href="http://rastorop.ru/client"> в своем личном кабинете.</a> </p><p>Ваш <b>Растороп</b> 8 (800) 775-22-05</p>
3190	40	bonus.pay.procent	F	0.3	Процент платежа, который можно оплатить бонусами	\N
58	18	sms.credit.closed	C	Займ полностью выплачен и закрыт, буду рад увидеть Вас снова. Ваш Растороп. 8 (800) 775-22-05	\N	\N
59	18	sms.credit.closed.partially	C	Спасибо, я получил часть займа, но не забудьте погасить его полностью. Вся информация в личном кабинете. Ваш Растороп. 8 (800) 775-22-05	\N	\N
74	5	email.rejected.subject	C	Вам отказано в займе на сумму %5.0f рублей.	Заголовок письма отказа в займе	\N
78	17	email.credit.oferta.subject	C	Ваша оферта в системе Растороп по займу %1$s	Заголовок письма отправления оферты займа	\N
60	18	sms.credit.closed.over	C	Займ полностью выплачен и закрыт, переплата будет отправлена на телефон. Ваш Растороп. 8 (800) 775-22-05	\N	\N
61	18	sms.payment.received	С	Оплата по займу получена. Ваш Растороп. 8 (800) 775-22-05	\N	\N
63	5	email.accepted.subject	C	Ваш заём на сумму %5.0f рублей одобрен.	Заголовок письма одобрения займа	\N
3132	28	manualPayment	N	0	\N	\N
64	17	email.credit.ready.subject	C	Вам отправлен займ на сумму %5.0f рублей.	Заголовок письма отправления займа	\N
79	17	email.credit.oferta.body	C	%2$s	Текст письма отправления оферты займа	%2$s
80	23	email.prolong.oferta.subject	C	Ваша оферта в системе Растороп по займу %1$s	Заголовок письма отправления оферты продления	\N
81	23	email.prolong.oferta.body	C	%2$s	Текст письма отправления оферты продления	%2$s
8	2	credit.days.max	N	31	Максимальное количество дней для незарегистрированного пользователя	\N
13	3	credit.sum.min	F	1000.00	Минимальная сумма займа для зарегистрированного пользователя	\N
17	3	credit.sum.max	F	15000.00	Максимальная сумма займа для зарегистрированного пользователя	\N
11	3	credit.stake.min	F	0.02	Минимальная ставка для зарегистрированного пользователя	\N
12	3	credit.stake.max	F	0.04	Максимальная ставка для зарегистрированного пользователя	\N
14	3	credit.stake.default	F	0.04	Ставка по умолчанию для зарегистрированного пользователя	\N
15	3	credit.days.min	N	2	Минимальное количество дней для зарегистрированного пользователя	\N
16	3	credit.days.max	N	31	Максимальное количество дней для зарегистрированного пользователя	\N
29	4	credit.prolongs.max	N	10	Максимальное количество продлений	\N
21	4	credit.comission	F	0.00	Комиссия при продлении	\N
23	4	credit.days.sum.max	N	38	Максимальное количество дней займа	\N
20	4	credit.stake.default	F	0.04	Ставка по умолчанию для продления	\N
18	4	credit.days.min	N	2	Минимальное количество дней продления	\N
19	4	credit.days.max	N	31	Максимальное количество дней продления	\N
56	9	waitSignOferta	C	P5D	Ждем подписания оферты	\N
55	9	clearIncompletedRequest	C	R/P30D	Ждем удаления недозаполненной заявки	\N
45	11	credit.comission	F	0.00	Комиссия при просрочке	\N
46	11	credit.stake.lgot	F	0	Ставка льготного периода при просрочке	\N
49	11	credit.stake.common	F	0.00054794521	Ставка при просрочке	\N
52	11	credit.stake.overdue	F	0.001	Ставка при сильной просрочке	\N
47	11	credit.days.min.lgot	N	1	Минимальное количество дней для льготного периода	\N
48	11	credit.days.max.lgot	N	14	Максимальное количество дней для льготного периода	\N
50	11	credit.days.min.common	N	15	Минимальное количество дней для просрочки	\N
51	11	credit.days.max.common	N	99	Максимальное количество дней для просрочки	\N
53	11	credit.days.min.overdue	N	100	Минимальное количество дней для сильной просрочки	\N
54	11	credit.days.max.overdue	N	365	Максимальное количество дней для сильной просрочки	\N
3130	17	email.rejected.subject	C	Вам отказано в займе на сумму %5.0f рублей	Заголовок письма отказа в займе	\N
3131	17	email.rejected.body	C	\N	Текст письма отказа в займе	<p>К сожалению, мы не можем выдать Вам займ на сумму %5.0f рублей.</p><p>Ваш Растороп. 8 (800) 775-22-05</p>
32	9	manualPayment	N	0	Возможность ручного платежа	\N
3134	6	manualPayment	N	0	\N	\N
75	5	email.rejected.body	C	\N	Текст письма отказа в займе	<p>К сожалению, мы не можем выдать Вам займ на сумму %5.0f рублей.</p><p>Ваш Растороп. 8 (800) 775-22-05</p>
25	6	manualDecision	N	1	Принятие решения кредитным менеджером вручную	\N
3165	42	default.subject	C	Предлагаем рефинансирование	\N	\N
3166	42	default.body	C	\N	\N	Предлагаем рефинансирование - текст письма
3168	3	credit.sum.addition	F	1000.00	Добавление к максимальной сумме	\N
3187	40	bonus.rate	F	1	Курс обмена бонусов к рублю	\N
3162	40	invite.bonus.amount	F	10000	Количество бонусов за приглашенного друга	\N
3179	7	okbIdv.cacheDays	N	10	\N	\N
3180	7	okbCais.cacheDays	N	10	\N	\N
3181	7	rusStandart.cacheDays	N	10	\N	\N
3182	7	equifax.cacheDays	N	10	\N	\N
3183	7	sociohub.cacheDays	N	10	\N	\N
3184	7	sociohub.minCorrectnessScore	N	75	\N	\N
3185	7	skb.cacheDays	N	10	\N	\N
3188	35	phone.db.url	C	http://localhost:8080/phonenumbers/api/phonenumber/check/	Сайт с БД телефонов	\N
3189	35	fms.db.url	C	http://localhost:8080/	Сайт с БД недействительных паспортов	\N
3556	7	winPay.balanceUrl	C	http://win-pay24.ru/partner/28/balance	\N	\N
3500	7	winPay.secretKey	C	7yte4	\N	\N
3539	7	okbIdv.useFake	N	0	\N	\N
3477	7	contactAcq.packetSchedule	C	\N	\N	\N
3478	7	rusStandart.retrySchedule	C	\N	\N	\N
3479	7	contactPay.senderFakeRegion	C	Moscow area	\N	\N
3480	7	contactAcq.retrySchedule	C	\N	\N	\N
3481	7	rusStandart.workServerURL	C	\N	\N	\N
3482	7	contactPay.processPacketName	C	subPacket	\N	\N
3483	7	rusStandart.processPacketName	C	subPacket	\N	\N
3484	7	contactAcq.retryInterval	N	60000	\N	\N
3485	7	alfaMarengo.testBik	C	\N	\N	\N
3486	7	verify.retrySchedule	C	\N	\N	\N
3487	7	yandexPay.processPacketName	C	subPacket	\N	\N
3488	7	alphaAcq.asyncSchedule	C	\N	\N	\N
3489	7	alphaAcq.mode	C	SINGLE	\N	\N
3490	7	contactPay.mode	C	SINGLE	\N	\N
3491	7	payonlineAcq.merchantId	C	\N	\N	\N
3492	7	winPay.packetInterval	N	60000	\N	\N
3493	7	yandexPay.workUrl	C	https://calypso.yamoney.ru:9094/	\N	\N
3494	7	okbCais.executionMode	C	AUTOMATIC	\N	\N
3495	7	alphaAcq.processName	C	subStandart	\N	\N
3496	7	yandexAcq.asyncInterval	N	0	\N	\N
3497	7	yandexAcq.useFake	N	1	\N	\N
3498	7	qiwiAcq.useFake	N	0	\N	\N
3499	7	yandexAcq.packetInterval	N	0	\N	\N
3501	7	winPay.processPacketName	C	subPacket	\N	\N
3502	7	yandexAcq.retrySchedule	C	\N	\N	\N
3503	7	yandexPay.packetInterval	N	0	\N	\N
3504	7	yandexAcq.asyncSchedule	C	\N	\N	\N
3505	7	payonlineAcq.statusUrl	C	\N	\N	\N
3506	7	alfaMarengo.workOrderURL	C	\N	\N	\N
3507	7	manualPay.asyncSchedule	C	\N	\N	\N
3508	7	verify.taskExecuteSingle.wfAction0	C	name=Вопросы заданы;signalRef=msgMore;forProcess=true;candidateGroups=verificator	\N	\N
3509	7	okbCais.processName	C	subStandart	\N	\N
3510	7	okbCais.mode	C	SINGLE	\N	\N
3511	7	equifax.executionMode	C	AUTOMATIC	\N	\N
3512	7	manualPay.processPacketName	C	subPacket	\N	\N
3513	7	qiwiGateAcq.retryInterval	N	120000	\N	\N
3514	7	payonlineAcq.verificationCardUrl	C	\N	\N	\N
3515	7	verify.fakeEJBName	C	\N	\N	\N
3516	7	rusStandart.fakeEJBName	C	\N	\N	\N
3517	7	contactPay.senderFakeCity	C	MOSCOW	\N	\N
3518	7	alfaMarengo.packetSchedule	C	\N	\N	\N
3519	7	okbIdv.executionMode	C	AUTOMATIC	\N	\N
3520	7	contactPay.processName	C	subStandart	\N	\N
3521	7	contactAcq.processName	C	subStandart	\N	\N
3522	7	alfaMarengo.workInn	C	\N	\N	\N
3523	7	account1C.processPacketName	C	subPacket	\N	\N
3524	7	okbCais.packetSchedule	C	\N	\N	\N
3525	7	account1CPay.processName	C	subStandart	\N	\N
3526	7	account1CAcq.asyncInterval	N	60000	\N	\N
3527	7	okbIdv.packetSchedule	C	\N	\N	\N
3528	7	yandexAcq.fakeEJBName	C	\N	\N	\N
3529	7	manualPay.taskExecuteSingle.name	C	taskExecuteSingle	\N	\N
3530	7	alfaMarengo.retrySchedule	C	\N	\N	\N
3531	7	qiwiGateAcq.asyncInterval	N	60000	\N	\N
3532	7	okbIdv.retrySchedule	C	\N	\N	\N
3533	7	manualPay.packetSchedule	C	\N	\N	\N
3534	7	rusStandart.requestFSSP	N	0	\N	\N
3535	7	rusStandart.requestPayment	N	0	\N	\N
3536	7	qiwiAcq.statusUrl	C	\N	\N	\N
3537	7	payonlineAcq.processName	C	subStandart	\N	\N
3538	7	contactPay.senderFakeName	C	Vladimir	\N	\N
3540	7	manualPay.processName	C	subStandart	\N	\N
3541	7	verify.workEJBName	C	\N	\N	\N
3542	7	account1CPay.packetSchedule	C	\N	\N	\N
3543	7	alphaAcq.syncMode	C	SYNC	\N	\N
3544	7	account1CAcq.workEJBName	C	\N	\N	\N
3545	7	okbCais.asyncSchedule	C	\N	\N	\N
3546	7	yandexPay.mode	C	SINGLE	\N	\N
3547	7	verify.asyncInterval	N	60000	\N	\N
3548	7	account1CPay.retryInterval	N	0	\N	\N
3549	7	alphaAcq.workEJBName	C	\N	\N	\N
3550	7	account1C.retryInterval	N	120000	\N	\N
3551	7	winPay.fakeEJBName	C		\N	\N
3552	7	payonlineAcq.retrySchedule	C	\N	\N	\N
3553	7	contactAcq.asyncSchedule	C	\N	\N	\N
3554	7	rusStandart.asyncSchedule	C	\N	\N	\N
3555	7	manualPay.syncMode	C	SYNC	\N	\N
3557	7	okbIdv.processPacketName	C	subPacket	\N	\N
3558	7	payonlineAcq.fakeEJBName	C	\N	\N	\N
3559	7	rusStandart.retryInterval	N	120000	\N	\N
3560	7	okbCais.asyncInterval	N	0	\N	\N
3561	7	verify.processPacketName	C	subPacket	\N	\N
3562	7	alphaAcq.packetSchedule	C	\N	\N	\N
3563	7	rusStandart.workEJBName	C	\N	\N	\N
3564	7	qiwiGateAcq.processName	C	subStandart	\N	\N
3565	7	alfaMarengo.workStatementURL	C	\N	\N	\N
3566	7	qiwiAcq.retrySchedule	C	\N	\N	\N
3567	7	qiwiAcq.login	C	\N	\N	\N
3568	7	account1CPay.fakeEJBName	C	\N	\N	\N
3569	7	account1CPay.workEJBName	C	\N	\N	\N
3571	7	yandexAcq.packetSchedule	C	\N	\N	\N
3572	7	equifax.packetInterval	N	60001	\N	\N
3573	7	account1CAcq.fakeEJBName	C	\N	\N	\N
3574	7	qiwiAcq.processName	C	subStandart	\N	\N
3575	7	alphaAcq.processPacketName	C	subPacket	\N	\N
3576	7	account1CAcq.packetInterval	N	60000	\N	\N
3577	7	equifax.processPacketName	C	subPacket	\N	\N
3578	7	winPay.retrySchedule	C		\N	\N
3579	7	qiwiAcq.syncMode	C	SYNC	\N	\N
3580	7	okbCais.useFake	N	0	\N	\N
3581	7	alfaMarengo.workOrganization	C	\N	\N	\N
3582	7	contactPay.asyncInterval	N	60000	\N	\N
3583	7	qiwiGateAcq.processPacketName	C	subPacket	\N	\N
3585	7	yandexAcq.processPacketName	C	subPacket	\N	\N
3586	7	contactAcq.workEJBName	C	\N	\N	\N
3587	7	alfaMarengo.testAccount	C	\N	\N	\N
3588	7	rusStandart.useFake	N	0	\N	\N
3589	7	account1CAcq.syncMode	C	SYNC	\N	\N
3590	7	yandexPay.workEJBName	C	\N	\N	\N
3591	7	contactAcq.scid	C	\N	\N	\N
3592	7	contactPay.syncMode	C	ASYNC	\N	\N
3593	7	account1C.workEJBName	C	\N	\N	\N
3594	7	okbCais.retryInterval	N	60000	\N	\N
3595	7	qiwiGateAcq.packetSchedule	C	\N	\N	\N
3597	7	alfaMarengo.workAccount	C	\N	\N	\N
3598	7	contactPay.workUrl	C	\N	\N	\N
3599	7	contactPay.testUrl	C	\N	\N	\N
3600	7	okbIdv.retryInterval	N	60000	\N	\N
3601	7	qiwiGateAcq.syncMode	C	SYNC	\N	\N
3602	7	contactAcq.packetInterval	N	120000	\N	\N
3603	7	qiwiAcq.orderUrl	C	\N	\N	\N
3604	7	qiwiAcq.providerId	C	\N	\N	\N
3605	7	contactAcq.executionMode	C	AUTOMATIC	\N	\N
3606	7	payonlineAcq.rebillUrl	C	\N	\N	\N
3607	7	contactPay.retryInterval	N	60000	\N	\N
3608	7	rusStandart.requestVerify	N	0	\N	\N
3609	7	yandexAcq.useWork	N	0	\N	\N
3610	7	contactPay.asyncSchedule	C	\N	\N	\N
3611	7	okbCais.fakeEJBName	C	\N	\N	\N
3613	7	equifax.syncMode	C	SYNC	\N	\N
3614	7	payonlineAcq.workEJBName	C	\N	\N	\N
3615	7	yandexAcq.shopId	N	18561	\N	\N
3616	7	equifax.packetSchedule	C	\N	\N	\N
3617	7	yandexAcq.processName	C	subStandart	\N	\N
3618	7	contactAcq.mode	C	SINGLE	\N	\N
3619	7	alphaAcq.executionMode	C	\N	\N	\N
3620	7	equifax.fakeEJBName	C	\N	\N	\N
3621	7	winPay.asyncInterval	N	60000	\N	\N
3622	7	contactPay.senderFakeLastName	C	Generous	\N	\N
3623	7	rusStandart.testServerURL	C	\N	\N	\N
3624	7	okbIdv.asyncSchedule	C	\N	\N	\N
3625	7	contactPay.packetSchedule	C	\N	\N	\N
3626	7	contactPay.useWork	N	0	\N	\N
3627	7	yandexPay.useFake	N	1	\N	\N
3628	7	alphaAcq.fakeEJBName	C	\N	\N	\N
3629	7	yandexAcq.workUrl	C	https://money.yandex.ru/eshop.xml	\N	\N
3630	7	payonlineAcq.cancelUrl	C	\N	\N	\N
3631	7	okbIdv.syncMode	C	SYNC	\N	\N
3632	7	alfaMarengo.useFake	N	1	\N	\N
3633	7	equifax.asyncInterval	N	0	\N	\N
3634	7	account1C.mode	C	PACKET	\N	\N
3635	7	qiwiAcq.processPacketName	C	subPacket	\N	\N
3636	7	account1CPay.useFake	N	0	\N	\N
3637	7	okbCais.retrySchedule	C	\N	\N	\N
3638	7	verify.asyncSchedule	C	\N	\N	\N
3639	7	account1CAcq.asyncSchedule	C	\N	\N	\N
3640	7	account1C.asyncSchedule	C	\N	\N	\N
3641	7	contactPay.senderFakeZipCode	C	120120	\N	\N
3642	7	payonlineAcq.asyncInterval	N	60000	\N	\N
3643	7	contactPay.senderFakeDocWhom	C	GYVD	\N	\N
3644	7	yandexPay.asyncSchedule	C	\N	\N	\N
3645	7	qiwiGateAcq.workEJBName	C	\N	\N	\N
3646	7	qiwiGateAcq.mode	C	SINGLE	\N	\N
3647	7	alfaMarengo.workEJBName	C	\N	\N	\N
3648	7	account1C.packetInterval	N	60000	\N	\N
3649	7	contactAcq.workUrl	C	\N	\N	\N
3650	7	okbIdv.mode	C	SINGLE	\N	\N
3651	7	payonlineAcq.useFake	N	0	\N	\N
3652	7	contactPay.packetInterval	N	0	\N	\N
3653	7	account1CPay.asyncSchedule	C	\N	\N	\N
3654	7	account1CAcq.retryInterval	N	120000	\N	\N
3655	7	equifax.processName	C	subStandart	\N	\N
3656	7	account1C.executionMode	C	AUTOMATIC	\N	\N
3657	7	manualPay.taskExecuteSingle.description	C	\N	\N	\N
3658	7	payonlineAcq.syncMode	C	SYNC	\N	\N
3659	7	account1CPay.mode	C	SINGLE	\N	\N
3660	7	yandexPay.fakeEJBName	C	\N	\N	\N
3661	7	verify.mode	C	SINGLE	\N	\N
3662	7	account1C.packetSchedule	C	\N	\N	\N
3663	7	qiwiAcq.executionMode	C	\N	\N	\N
3664	7	qiwiAcq.password	C	\N	\N	\N
3665	7	winPay.processName	C	subStandart	\N	\N
3666	7	contactAcq.fakeEJBName	C	\N	\N	\N
3667	7	account1CPay.packetInterval	N	0	\N	\N
3668	7	alfaMarengo.testOrganization	C	\N	\N	\N
3669	7	alphaAcq.useFake	N	0	\N	\N
3670	7	okbCais.workEJBName	C	\N	\N	\N
3671	7	yandexAcq.retryInterval	N	0	\N	\N
3672	7	contactPay.senderFakeDocdate	C	20020423	\N	\N
3673	7	winPay.packetSchedule	C		\N	\N
3674	7	manualPay.retrySchedule	C	\N	\N	\N
3676	7	payonlineAcq.executionMode	C	\N	\N	\N
3677	7	okbIdv.asyncInterval	N	0	\N	\N
3678	7	winPay.executionMode	C	AUTOMATIC	\N	\N
3679	7	rusStandart.processName	C	subStandart	\N	\N
3680	7	yandexAcq.syncMode	C	SYNC	\N	\N
3681	7	manualPay.packetInterval	N	0	\N	\N
3682	7	qiwiAcq.payUrl	C	\N	\N	\N
3683	7	contactPay.senderFakeAddress	C	MOSCOW Kremlin 5	\N	\N
3684	7	yandexPay.testUrl	C	https://bo-demo02.yamoney.ru:9094/	\N	\N
3685	7	manualPay.asyncInterval	N	0	\N	\N
3686	7	alfaMarengo.testInn	C	\N	\N	\N
3687	7	yandexPay.asyncInterval	N	60000	\N	\N
3688	7	manualPay.executionMode	C	\N	\N	\N
3689	7	qiwiGateAcq.packetInterval	N	60000	\N	\N
3690	7	alfaMarengo.fakeEJBName	C	\N	\N	\N
3691	7	okbIdv.processName	C	subStandart	\N	\N
3692	7	winPay.useFake	N	0	\N	\N
3693	7	verify.packetInterval	N	60000	\N	\N
3694	7	contactAcq.useWork	N	0	\N	\N
3695	7	account1CAcq.useFake	N	0	\N	\N
3696	7	rusStandart.requestFMS	N	0	\N	\N
3697	7	alfaMarengo.processName	C	subStandart	\N	\N
3698	7	okbCais.syncMode	C	SYNC	\N	\N
3699	7	account1C.fakeEJBName	C	\N	\N	\N
3700	7	payonlineAcq.retryInterval	N	60000	\N	\N
3701	7	contactPay.senderFakeDocSerNum	C	1234123456	\N	\N
3702	7	manualPay.retryInterval	N	0	\N	\N
3703	7	alfaMarengo.executionMode	C	\N	\N	\N
3704	7	manualPay.useFake	N	0	\N	\N
3705	7	account1CAcq.mode	C	PACKET	\N	\N
3706	7	account1CAcq.executionMode	C	AUTOMATIC	\N	\N
3707	7	contactAcq.testUrl	C	\N	\N	\N
3708	7	alfaMarengo.workBik	C	\N	\N	\N
3709	7	account1C.syncMode	C	SYNC	\N	\N
3710	7	alfaMarengo.processPacketName	C	subPacket	\N	\N
3711	7	contactPay.keyStoreAlias	C	Testkey	\N	\N
3712	7	verify.useFake	N	0	\N	\N
3713	7	account1C.retrySchedule	C	\N	\N	\N
3714	7	account1CPay.retrySchedule	C	\N	\N	\N
3715	7	payonlineAcq.processPacketName	C	subPacket	\N	\N
3716	7	okbCais.packetInterval	N	0	\N	\N
3717	7	qiwiAcq.asyncSchedule	C	\N	\N	\N
3718	7	contactPay.senderFakeDocExpDate	C	20720423	\N	\N
3719	7	equifax.useWork	N	0	\N	\N
3720	7	contactPay.executionMode	C	AUTOMATIC	\N	\N
3721	7	alphaAcq.asyncInterval	N	0	\N	\N
3722	7	contactPay.senderFakeDocName	C	PASSPORT	\N	\N
3723	7	rusStandart.requestFNS	N	0	\N	\N
3724	7	rusStandart.syncMode	C	SYNC	\N	\N
3725	7	contactPay.senderFakeSurName	C	Ivanovich	\N	\N
3726	7	okbIdv.packetInterval	N	0	\N	\N
3727	7	qiwiAcq.fakeEJBName	C	\N	\N	\N
3728	7	contactPay.keyStorePassword	C	changeit	\N	\N
3729	7	contactAcq.asyncInterval	N	60000	\N	\N
3730	7	alphaAcq.retrySchedule	C	\N	\N	\N
3731	7	contactPay.senderFakeBirthday	C	19700112	\N	\N
3732	7	okbIdv.workEJBName	C	\N	\N	\N
3733	7	account1CPay.executionMode	C	\N	\N	\N
3734	7	48.providerId	N	48	\N	\N
3735	7	yandexPay.executionMode	C	AUTOMATIC	\N	\N
3736	7	qiwiAcq.mode	C	SINGLE	\N	\N
3737	7	manualPay.workEJBName	C	\N	\N	\N
3738	7	winPay.mode	C	SINGLE	\N	\N
3739	7	qiwiGateAcq.useFake	N	0	\N	\N
3740	7	winPay.workEJBName	C		\N	\N
3741	7	account1CPay.asyncInterval	N	0	\N	\N
3742	7	alfaMarengo.retryInterval	N	60000	\N	\N
3743	7	qiwiGateAcq.fakeEJBName	C	\N	\N	\N
3744	7	payonlineAcq.packetInterval	N	60000	\N	\N
3745	7	contactAcq.processPacketName	C	subPacket	\N	\N
3746	7	yandexPay.retryInterval	N	60000	\N	\N
3747	7	okbIdv.fakeEJBName	C	\N	\N	\N
3748	7	qiwiAcq.asyncInterval	N	0	\N	\N
3749	7	contactAcq.shopId	C	\N	\N	\N
3750	7	qiwiGateAcq.asyncSchedule	C	\N	\N	\N
3751	7	alfaMarengo.asyncInterval	N	120000	\N	\N
3752	7	qiwiAcq.packetSchedule	C	\N	\N	\N
3753	7	payonlineAcq.packetSchedule	C	\N	\N	\N
3754	7	alfaMarengo.asyncSchedule	C	\N	\N	\N
3755	7	okbIdv.trustStoreName	C	\N	\N	\N
3756	7	yandexPay.processName	C	subStandart	\N	\N
3757	7	contactAcq.syncMode	C	SYNC	\N	\N
3758	7	equifax.mode	C	SINGLE	\N	\N
3759	7	equifax.retrySchedule	C	\N	\N	\N
3760	7	alfaMarengo.mode	C	SINGLE	\N	\N
3761	7	account1CAcq.packetSchedule	C	\N	\N	\N
3763	7	alfaMarengo.syncMode	C	SYNC	\N	\N
3764	7	equifax.useFake	N	0	\N	\N
3765	7	contactPay.workEJBName	C	\N	\N	\N
3766	7	contactPay.PPcode	C	\N	\N	\N
3767	7	verify.syncMode	C	SYNC	\N	\N
3768	7	qiwiGateAcq.retrySchedule	C	\N	\N	\N
3769	7	yandexPay.agentId	C	200382	\N	\N
3770	7	verify.taskExecuteSingle.name	C	taskExecuteSingle	\N	\N
3771	7	okbCais.processPacketName	C	subPacket	\N	\N
3772	7	rusStandart.packetSchedule	C	\N	\N	\N
3773	7	winPay.retryInterval	N	120000	\N	\N
3774	7	rusStandart.asyncInterval	N	0	\N	\N
3775	7	yandexPay.packetSchedule	C	\N	\N	\N
3776	7	payonlineAcq.privateSecurityKey	C	\N	\N	\N
3777	7	equifax.retryInterval	N	120000	\N	\N
3778	7	manualPay.fakeEJBName	C	\N	\N	\N
3779	7	rusStandart.mode	C	SINGLE	\N	\N
3780	7	winPay.asyncSchedule	C		\N	\N
3781	7	payonlineAcq.mode	C	SINGLE	\N	\N
3782	7	yandexPay.useWork	N	0	\N	\N
3783	7	qiwiAcq.packetInterval	N	0	\N	\N
3784	7	alfaMarengo.testStatementURL	C	\N	\N	\N
3785	7	qiwiGateAcq.executionMode	C	\N	\N	\N
3786	7	rusStandart.packetInterval	N	0	\N	\N
3787	7	equifax.workEJBName	C	\N	\N	\N
3788	7	manualPay.mode	C	SINGLE	\N	\N
3789	7	equifax.asyncSchedule	C	\N	\N	\N
3790	7	yandexAcq.password	C	QvdC40Y8g0LjQvdGE	\N	\N
3791	7	yandexAcq.scid	N	56212	\N	\N
3792	7	contactPay.retrySchedule	C	\N	\N	\N
3794	7	yandexAcq.workEJBName	C	\N	\N	\N
3795	7	account1CAcq.processPacketName	C	subPacket	\N	\N
3796	7	verify.processName	C	subStandart	\N	\N
3797	7	account1CAcq.retrySchedule	C	\N	\N	\N
3799	7	alphaAcq.packetInterval	N	0	\N	\N
3800	7	qiwiAcq.retryInterval	N	0	\N	\N
3801	7	alfaMarengo.useWork	N	0	\N	\N
3802	7	payonlineAcq.bankCardUrl	C	\N	\N	\N
3803	7	yandexPay.syncMode	C	ASYNC	\N	\N
3804	7	account1CAcq.processName	C	subStandart	\N	\N
3805	7	contactAcq.password	C	219AFD28-CCC2-4CD7-BBBC-9058F761B1CD	\N	\N
3806	7	qiwiAcq.workEJBName	C	\N	\N	\N
3807	7	verify.taskExecuteSingle.description	C	Задать вопросы	\N	\N
3808	7	contactAcq.useFake	N	0	\N	\N
3809	7	verify.packetSchedule	C	\N	\N	\N
3810	7	account1C.asyncInterval	N	60000	\N	\N
3811	7	rusStandart.executionMode	C	AUTOMATIC	\N	\N
3812	7	contactPay.keyStoreName	C	\N	\N	\N
3813	7	verify.executionMode	C	MANUAL	\N	\N
3814	7	payonlineAcq.asyncSchedule	C	\N	\N	\N
3815	7	account1C.processName	C	subStandart	\N	\N
3816	7	contactPay.fakeEJBName	C	\N	\N	\N
3817	7	account1C.useFake	N	0	\N	\N
3818	7	rusStandart.fakeXmlAnswer	C	\N	\N	\N
3819	7	alfaMarengo.packetInterval	N	120000	\N	\N
3820	7	verify.retryInterval	N	120000	\N	\N
3821	7	account1CPay.processPacketName	C	subPacket	\N	\N
3822	7	yandexAcq.mode	C	SINGLE	\N	\N
3823	7	yandexAcq.executionMode	C	AUTOMATIC	\N	\N
3824	7	yandexAcq.testUrl	C	https://demomoney.yandex.ru/eshop.xml	\N	\N
3825	7	alfaMarengo.testOrderURL	C	\N	\N	\N
3826	7	alphaAcq.retryInterval	N	0	\N	\N
3827	7	yandexPay.retrySchedule	C	\N	\N	\N
3828	7	account1CPay.syncMode	C	SYNC	\N	\N
\.


--
-- TOC entry 2001 (class 0 OID 101243)
-- Dependencies: 163
-- Data for Name: aimodel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY aimodel (id, datecreate, version, isactive, body, target, mimetype, datechange) FROM stdin;
481	2014-06-05 23:23:12.137+07	0.7	0	if (! decisionState.nextLabel) {\r\n\tdecisionState.start();\r\n\tdecisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n\tdecisionState.nextLabel = 'label_finish';\r\n} else if (decisionState.nextLabel == 'label_finish') {\t\r\n\tactionProcessor.saveCreditRequestDecision(creditRequest.id, true, null);\r\n\tdecisionState.finish(true);\r\n}	credit.decision	text/javascript	\N
401	2014-06-02 15:19:18.334+07	0.2	0	if (! decisionState.nextLabel) {\r\n\r\n\tdecisionState.start();\r\n\tdecisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n\tdecisionState.nextLabel = 'label_finish';\r\n} else if (decisionState.nextLabel == 'label_finish') {\t\r\n\tactionProcessor.saveCreditRequestDecision(creditRequest.id, true, null);\r\n\tdecisionState.finish(true);\r\n}	credit.decision	text/javascript	\N
477	2014-06-05 22:59:01.089+07	0.3	0	if (! decisionState.nextLabel) {\r\n67890\r\n\tdecisionState.start();\r\n\tdecisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n\tdecisionState.nextLabel = 'label_finish';\r\n} else if (decisionState.nextLabel == 'label_finish') {\t\r\n\tactionProcessor.saveCreditRequestDecision(creditRequest.id, true, null);\r\n\tdecisionState.finish(true);\r\n}	credit.decision	text/javascript	\N
50	2014-11-11 00:00:00+07	1	0	if (! decisionState.nextLabel) \r\n{\r\n\tdecisionState.start();\r\n\tdecisionState.vars.put('challenger', 'false');\r\n\tdecisionState.vars.put('RejectCode', 'ACCPT');\r\n\tvar chlCode = Math.random();\r\n        //100% challenger for test only!\r\n\tif (chlCode<=1) \r\n\t{\r\n\t\tdecisionState.vars.put('challenger', 'true');\r\n\t}\r\n        // MACs\r\n        var res = PeopleSearch.searchBlackList(creditRequest.peopleMain);\r\n\tif (res.count>0)\r\n\t{\r\n\t\tfor (var black in res.data) \r\n\t\t{\r\n\t\t\tif (creditRequest.dateContest.getTime() - black.dateBeg.getTime()>=0 && black.dateEnd.getTime()-creditRequest.dateContest.getTime()>=0)\r\n\t\t\t{\r\n                                decisionState.finish(true, MConst.refuseCode.MC001);\r\n\t\t\t}\r\n        }\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age>80)\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC003);\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age<18)\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC004);\r\n\t}\r\n\tif (((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<20 && (creditRequest.dateContest.getTime() - creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>20) || ((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<45 && (creditRequest.dateContest.getTime() - creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>45) || (creditRequest.peopleMain.activePassport.docDate.getDate()==creditRequest.peopleMain.peoplePersonalActive.birthDate.getDate() && creditRequest.peopleMain.activePassport.docDate.getMonth()==creditRequest.peopleMain.peoplePersonalActive.birthDate.getMonth() && (Math.round(creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())==20 || Math.round(creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())==45)))\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC005);\r\n\t}\r\n\tif ((creditRequest.peopleMain.activePassport.series=='0000') || (creditRequest.peopleMain.activePassport.series=='1111') || (creditRequest.peopleMain.activePassport.series=='2222') || (creditRequest.peopleMain.activePassport.series=='3333') || (creditRequest.peopleMain.activePassport.series=='4444') || (creditRequest.peopleMain.activePassport.series=='5555') || (creditRequest.peopleMain.activePassport.series=='6666') || (creditRequest.peopleMain.activePassport.series=='7777') || (creditRequest.peopleMain.activePassport.series=='8888') || (creditRequest.peopleMain.activePassport.series=='9999') || \r\n\t(creditRequest.peopleMain.activePassport.number=='000000') || (creditRequest.peopleMain.activePassport.number=='111111') || (creditRequest.peopleMain.activePassport.number=='222222') || (creditRequest.peopleMain.activePassport.number=='333333') || (creditRequest.peopleMain.activePassport.number=='444444') || (creditRequest.peopleMain.activePassport.number=='555555') || (creditRequest.peopleMain.activePassport.number=='666666') || (creditRequest.peopleMain.activePassport.number=='777777') || (creditRequest.peopleMain.activePassport.number=='888888') || (creditRequest.peopleMain.activePassport.number=='999999') || ((creditRequest.peopleMain.activePassport.series=='1234') && (creditRequest.peopleMain.activePassport.number=='123456')))\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC006);\r\n\t}\r\n       var residCode, registrCode, workCode;\r\n        if (creditRequest.peopleMain.residentAddress==null)\r\n        {\r\n            residCode='-1';\r\n        }\r\n        else\r\n        {\r\n            residCode=creditRequest.peopleMain.residentAddress.regionCode;\r\n        }\r\n        if (creditRequest.peopleMain.registerAddress==null)\r\n        {\r\n            registrCode='-2';\r\n        }\r\n        else\r\n        {\r\n            registrCode=creditRequest.peopleMain.registerAddress.regionCode;\r\n        }\r\n        if (creditRequest.peopleMain.workAddress==null)\r\n        {\r\n            workCode='-3';\r\n        }\r\n        else\r\n        {\r\n            workCode=creditRequest.peopleMain.workAddress.regionCode;\r\n        }\r\n\tif (!((residCode == registrCode) || (residCode == workCode) || (workCode == registrCode)))\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC007'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC007);\r\n            }\r\n\t}\r\n\tif (creditRequest.creditDays<1 || creditRequest.creditDays>31)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC008'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC008);\r\n            }\r\n\t}\r\n\tif (creditRequest.creditSum<1000)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC009'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC009);\r\n            }\r\n\t}\t\r\n\tif (creditRequest.creditSum>10000)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC010'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC010);\r\n            }\r\n\t}\t\t\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==2 || creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==3)\r\n\t{\r\n            if ((creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==1 && creditRequest.peopleMain.peoplePersonalActive.age<60) || (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2 && creditRequest.peopleMain.peoplePersonalActive.age<55))\r\n            {\r\n                if (decisionState.vars.get('challenger')=='true')\r\n                {\r\n                    decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC011'));\r\n                }\r\n                else\r\n                {\r\n                    decisionState.finish(true, MConst.refuseCode.MC011);\r\n                }\r\n            }\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==6 && creditRequest.peopleMain.peoplePersonalActive.age<45)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC011'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC011);\r\n            }\t\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==4 || creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==7)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC012'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC012);\r\n            }\t\r\n\t}\r\n\tvar numContacts=0;\r\n\tif (creditRequest.peopleMain.workPhone!=null)\r\n\t{\r\n\t\tif (creditRequest.peopleMain.workPhone.value.slice(6)!='000000' && creditRequest.peopleMain.workPhone.value.slice(6)!='111111' && creditRequest.peopleMain.workPhone.value.slice(6)!='222222' && creditRequest.peopleMain.workPhone.value.slice(6)!='333333' && creditRequest.peopleMain.workPhone.value.slice(6)!='444444' && creditRequest.peopleMain.workPhone.value.slice(6)!='555555' && creditRequest.peopleMain.workPhone.value.slice(6)!='666666' && creditRequest.peopleMain.workPhone.value.slice(6)!='777777' && creditRequest.peopleMain.workPhone.value.slice(6)!='888888' && creditRequest.peopleMain.workPhone.value.slice(6)!='999999' && creditRequest.peopleMain.workPhone.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.workPhone.value.toString().substr(1,6)!='987654')\r\n\t\t{\r\n\t\t\tnumContacts++;\r\n\t\t}\r\n\t}\r\n\tif (creditRequest.peopleMain.homePhoneReg!=null)\r\n\t{\r\n\t\tif (creditRequest.peopleMain.homePhoneReg.value.slice(6)!='000000' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='111111' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='222222' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='333333' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='444444' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='555555' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='666666' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='777777' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='888888' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='999999' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,6)!='987654')\r\n\t\t{\r\n\t\t\tnumContacts++;\r\n\t\t}\r\n\t}\r\n\tif (creditRequest.peopleMain.homePhone!=null)\r\n\t{\t\r\n\t\tif (creditRequest.peopleMain.homePhone.value.slice(6)!='000000' && creditRequest.peopleMain.homePhone.value.slice(6)!='111111' && creditRequest.peopleMain.homePhone.value.slice(6)!='222222' && creditRequest.peopleMain.homePhone.value.slice(6)!='333333' && creditRequest.peopleMain.homePhone.value.slice(6)!='444444' && creditRequest.peopleMain.homePhone.value.slice(6)!='555555' && creditRequest.peopleMain.homePhone.value.slice(6)!='666666' && creditRequest.peopleMain.homePhone.value.slice(6)!='777777' && creditRequest.peopleMain.homePhone.value.slice(6)!='888888' && creditRequest.peopleMain.homePhone.value.slice(6)!='999999' && creditRequest.peopleMain.homePhone.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhone.value.toString().substr(1,6)!='987654')\r\n\t\t{\r\n\t\t\tnumContacts++;\r\n\t\t}\r\n\t}\r\n\tif (numContacts<1)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC013'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC013);\r\n            }\t\t\r\n\t}\r\n\t\r\n    /*if\t((creditRequest.dateContest.getTime()-creditRequest.peopleMain.currentEmployment.dateStartWork.getTime())/(1000*60*60*24)<30)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC014'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC014);\r\n            }\t\t\r\n\t}*/\r\n  \tvar trueSalary=0;\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==0)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary/12;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==1)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary/6;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==2)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary/3;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==3)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==4)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary*2;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==5)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary*4;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==8)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary;\r\n\t}\t\r\n\tif (residCode=='50' || residCode=='77')\r\n\t{\r\n\t\tif (trueSalary<8000) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC015'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.MC015);\r\n                    }\t\t\t\t\r\n\t\t}\r\n\t} else if (residCode=='47' || residCode=='78')\r\n\t{\r\n\t\tif (trueSalary<6000) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC015'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.MC015);\r\n                    }\t\t\t\r\n\t\t}\t\r\n\t} else \r\n\t{\r\n\t\tif (trueSalary<5000) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC015'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.MC015);\r\n                    }\t\t\t\t\r\n\t\t}\t\r\n\t}\r\n\tif\t((creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==1 && creditRequest.peopleMain.peoplePersonalActive.age<21) || (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2 && creditRequest.peopleMain.peoplePersonalActive.age<20))\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC016'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC016);\r\n            }\t\t\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age>72)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC017'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC017);\r\n            }\t\r\n\t}\r\n\t\r\n\tvar res = CreditRequestSearch.search('peopleMain.id', creditRequest.peopleMain.id); \r\n\tif (res.count>0)\r\n\t{\r\n\t\tfor (var cr in res.data) \r\n\t\t{\r\n\t\t\t//if (DatesUtils.daysDiff(cr.dateContest,creditRequest.dateContest)<7)       \r\n                        if (((creditRequest.dateContest.getTime()-cr.dateContest.getTime())/(1000*60*60*24*365))<7)\r\n\t\t\t{\r\n                            if (decisionState.vars.get('challenger')=='true')\r\n                            {\r\n                                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC018'));\r\n                            }\r\n                            else\r\n                            {\r\n                                decisionState.finish(true, MConst.refuseCode.MC018);\r\n                            }\t\t\t\t\t\r\n\t\t\t}\r\n\t\t\t\r\n\t\t}\r\n\t}\r\n\t\r\n  \r\n  \r\n  var score1=20;\r\n  \r\n\tif (creditRequest.peopleMain.currentEmployment.education.codeInteger==4 || creditRequest.peopleMain.currentEmployment.education.codeInteger==5 || creditRequest.peopleMain.currentEmployment.education.codeInteger==6)\r\n\t{\r\n\tscore1=score1+6;\r\n\t}\r\n  \r\n    if (creditRequest.peopleMain.currentEmployment.profession!=null) {\r\n\r\n\t  if (creditRequest.peopleMain.currentEmployment.profession.codeInteger==21 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==2)\r\n\t  {\r\n\t  }\r\n\t  else if (creditRequest.peopleMain.currentEmployment.profession.codeInteger==5 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==13 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==10 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==24 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==98 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==15 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==19 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==22 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==11)\r\n\t  {\r\n\t\tscore1=score1+12;\r\n\t  }\r\n\t  else\r\n\t  {\r\n\t\tscore1=score1+30;\r\n\t  }\r\n    }// end if profession not null\r\n  \r\n\tif (creditRequest.peopleMain.peopleMiscActive.car)\r\n\t{\r\n\t\tscore1=score1+13;\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age>36)\r\n\t{\r\n\t\tscore1=score1+10;\r\n\t}\r\n\tif (creditRequest.peopleMain.homePhoneReg!=null && creditRequest.peopleMain.homePhone!=null)\r\n\t{\r\n\t\tif (creditRequest.peopleMain.homePhoneReg.value.slice(6)!='000000' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='111111' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='222222' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='333333' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='444444' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='555555' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='666666' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='777777' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='888888' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='999999' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,6)!='987654' && creditRequest.peopleMain.homePhone.value.slice(6)!='000000' && creditRequest.peopleMain.homePhone.value.slice(6)!='111111' && creditRequest.peopleMain.homePhone.value.slice(6)!='222222' && creditRequest.peopleMain.homePhone.value.slice(6)!='333333' && creditRequest.peopleMain.homePhone.value.slice(6)!='444444' && creditRequest.peopleMain.homePhone.value.slice(6)!='555555' && creditRequest.peopleMain.homePhone.value.slice(6)!='666666' && creditRequest.peopleMain.homePhone.value.slice(6)!='777777' && creditRequest.peopleMain.homePhone.value.slice(6)!='888888' && creditRequest.peopleMain.homePhone.value.slice(6)!='999999' && creditRequest.peopleMain.homePhone.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhone.value.toString().substr(1,6)!='987654' && creditRequest.peopleMain.homePhone.available == true && creditRequest.peopleMain.homePhoneReg.available == true)\r\n\t\t{\r\n\t\t\tscore1=score1+10;\r\n\t\t}\r\n\t}\r\n\tif (((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>13.9 && (creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<14.2) || ((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>19.9 && (creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<20.2) || ((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>44.9 && (creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<45.2))\r\n\t{\r\n\t\tscore1=score1+15;\r\n\t} else if (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2 && (creditRequest.peopleMain.peopleMiscActive.marriage.codeInteger==1 || creditRequest.peopleMain.peopleMiscActive.marriage.codeInteger==2 || creditRequest.peopleMain.peopleMiscActive.marriage.codeInteger==3))\r\n\t{\r\n\t\tscore1=score1+15;\r\n\t}\r\n\tif (creditRequest.creditDays<=7)\r\n\t{\r\n\t\tscore1=score1+35;\r\n\t}\r\n\telse if (creditRequest.creditDays<=15)\r\n\t{\r\n\t\tscore1=score1+14;\r\n\t}\r\n\tif (creditRequest.creditSum<=3000)\r\n\t{\r\n\t\tscore1=score1+31;\r\n\t}\r\n\telse if (creditRequest.creditSum<=5000)\r\n\t{\r\n\t\tscore1=score1+17;\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2)\r\n\t{\r\n\t\tscore1=score1+11;\r\n\t}\r\n  \r\n  if (creditRequest.peopleMain.currentEmployment.typeWork!=null)\r\n  {\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=9 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=6 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=7 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=2 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=4)\r\n\t{\r\n\t\tif ((creditRequest.dateContest.getTime()-creditRequest.peopleMain.currentEmployment.dateStartWork.getTime())/(1000*60*60*24*30)>7.9)\r\n\t\t{\r\n\t\t\tscore1=score1+12;\r\n\t\t} else if ((creditRequest.dateContest.getTime()-creditRequest.peopleMain.currentEmployment.dateStartWork.getTime())/(1000*60*60*24*30)>2.5)\r\n\t\t{\r\n\t\t\tscore1=score1+4;\r\n\t\t}\r\n\t}\r\n\telse\r\n\t{\r\n\t\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=9 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=7 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=4)\r\n\t\t{\r\n\t\t\tscore1=score1+4;\r\n\t\t}\r\n\t}\r\n  }//end if typework not null\r\n  \r\n\t\r\n\tif (creditRequest.peopleMain.peopleMiscActive.children!=null)\r\n\t{\r\n\t\tscore1=score1+10;\r\n\t}\r\n  \r\n\tif (score1<=100)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('JS001'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.JS001);\r\n            }\t\t\r\n\t}\r\n\r\n\t\r\n\tdecisionState.nextLabel = 'internal_history';\r\n} \r\nelse if (decisionState.nextLabel == 'internal_history') \r\n{\r\n\t// Checking internal history\r\n\r\n  if (creditRequest.peopleMain.hasCredits>0) \r\n  {\r\n\r\n\t  for (var credit in creditRequest.peopleMain.credits) \r\n\t  {\r\n\t\tif (credit.isSameOrg && !credit.isOver) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('IH003'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.IH003);\r\n                    }\t\t\t\r\n\t\t}\r\n\t  }\r\n  }\r\n  \r\n\tvar sum1 = 0;\r\n\tvar maxdelq = 0;\r\n  \r\n  if (creditRequest.peopleMain.hasCredits>0) \r\n  {\r\n\tfor (var credit in creditRequest.peopleMain.credits) \r\n\t{\r\n\t\tif (credit.isSameOrg ) \r\n\t\t{\r\n          if (credit.currentOverdueDebt!=null) {\r\n\t\t\tsum1 = sum1 + credit.currentOverdueDebt;\r\n          }\r\n          if (credit.maxDelay!=null){\r\n\t\t\tif (maxdelq<=credit.maxDelay)\r\n\t\t\t{\r\n\t\t\t\tmaxdelq=credit.maxDelay;\r\n\t\t\t}\r\n          }\r\n\t\t}\r\n\t}\r\n  }\r\n\tif (sum1>1000 || sum1>creditRequest.creditSum*0.75)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('IH001'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.IH001);\r\n            }\t\t\r\n\t}\r\n\tif (maxdelq>=90)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('IH004'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.IH004);\r\n            }\t\t\r\n\t}\r\n\t\t\t\r\n\tdecisionState.nextLabel = 'external_history_get';\r\n}\r\nelse if (decisionState.nextLabel == 'external_history_get') \r\n{\r\n    // Calling external history services\r\n    decisionState.externalSystems.get('plugin_okbIdv').needCall = true;    \r\n    decisionState.externalSystems.get('plugin_okbCais').needCall = true;\r\n    decisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n  \r\n  \tdecisionState.nextLabel = 'final_decision';\r\n}\r\nelse if (decisionState.nextLabel == 'final_decision') \r\n{ \r\n\t// Final decision\r\n\tdecisionState.finish(true, MConst.decision.RESULT_ACCEPT);\r\n}\r\n	credit.decision	text/javascript	\N
2	2014-11-23 00:00:00+07	1.2	0	if (! decisionState.nextLabel) \r\n{\r\n\tdecisionState.start();\r\n\tdecisionState.vars.put('challenger', 'false');\r\n\tdecisionState.vars.put('RejectCode', 'ACCPT');\r\n\tvar chlCode = Math.random();\r\n        //100% challenger for test only!\r\n\tif (chlCode<=1) \r\n\t{\r\n\t\tdecisionState.vars.put('challenger', 'true');\r\n\t}\r\n        // MACs\r\n        var res = PeopleSearch.searchBlackList(creditRequest.peopleMain);\r\n\tif (res.count>0)\r\n\t{\r\n\t\tfor (var black in res.data) \r\n\t\t{\r\n\t\t\tif (creditRequest.dateContest.getTime() - black.dateBeg.getTime()>=0 && black.dateEnd.getTime()-creditRequest.dateContest.getTime()>=0)\r\n\t\t\t{\r\n                                decisionState.finish(true, MConst.refuseCode.MC001);\r\n\t\t\t}\r\n        }\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age>80)\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC003);\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age<18)\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC004);\r\n\t}\r\n\tif (((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<20 && (creditRequest.dateContest.getTime() - creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>20) || ((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<45 && (creditRequest.dateContest.getTime() - creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>45) || (creditRequest.peopleMain.activePassport.docDate.getDate()==creditRequest.peopleMain.peoplePersonalActive.birthDate.getDate() && creditRequest.peopleMain.activePassport.docDate.getMonth()==creditRequest.peopleMain.peoplePersonalActive.birthDate.getMonth() && (Math.round(creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())==20 || Math.round(creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())==45)))\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC005);\r\n\t}\r\n\tif ((creditRequest.peopleMain.activePassport.series=='0000') || (creditRequest.peopleMain.activePassport.series=='1111') || (creditRequest.peopleMain.activePassport.series=='2222') || (creditRequest.peopleMain.activePassport.series=='3333') || (creditRequest.peopleMain.activePassport.series=='4444') || (creditRequest.peopleMain.activePassport.series=='5555') || (creditRequest.peopleMain.activePassport.series=='6666') || (creditRequest.peopleMain.activePassport.series=='7777') || (creditRequest.peopleMain.activePassport.series=='8888') || (creditRequest.peopleMain.activePassport.series=='9999') || \r\n\t(creditRequest.peopleMain.activePassport.number=='000000') || (creditRequest.peopleMain.activePassport.number=='111111') || (creditRequest.peopleMain.activePassport.number=='222222') || (creditRequest.peopleMain.activePassport.number=='333333') || (creditRequest.peopleMain.activePassport.number=='444444') || (creditRequest.peopleMain.activePassport.number=='555555') || (creditRequest.peopleMain.activePassport.number=='666666') || (creditRequest.peopleMain.activePassport.number=='777777') || (creditRequest.peopleMain.activePassport.number=='888888') || (creditRequest.peopleMain.activePassport.number=='999999') || ((creditRequest.peopleMain.activePassport.series=='1234') && (creditRequest.peopleMain.activePassport.number=='123456')))\r\n\t{\r\n                decisionState.finish(true, MConst.refuseCode.MC006);\r\n\t}\r\n       var residCode, registrCode, workCode;\r\n        if (creditRequest.peopleMain.residentAddress==null)\r\n        {\r\n            residCode='-1';\r\n        }\r\n        else\r\n        {\r\n            residCode=creditRequest.peopleMain.residentAddress.regionCode;\r\n        }\r\n        if (creditRequest.peopleMain.registerAddress==null)\r\n        {\r\n            registrCode='-2';\r\n        }\r\n        else\r\n        {\r\n            registrCode=creditRequest.peopleMain.registerAddress.regionCode;\r\n        }\r\n        if (creditRequest.peopleMain.workAddress==null)\r\n        {\r\n            workCode='-3';\r\n        }\r\n        else\r\n        {\r\n            workCode=creditRequest.peopleMain.workAddress.regionCode;\r\n        }\r\n\tif (!((residCode == registrCode) || (residCode == workCode) || (workCode == registrCode)))\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC007'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC007);\r\n            }\r\n\t}\r\n\tif (creditRequest.creditDays<1 || creditRequest.creditDays>31)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC008'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC008);\r\n            }\r\n\t}\r\n\tif (creditRequest.creditSum<1000)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC009'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC009);\r\n            }\r\n\t}\t\r\n\tif (creditRequest.creditSum>10000)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC010'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC010);\r\n            }\r\n\t}\t\t\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==2 || creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==3)\r\n\t{\r\n            if ((creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==1 && creditRequest.peopleMain.peoplePersonalActive.age<60) || (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2 && creditRequest.peopleMain.peoplePersonalActive.age<55))\r\n            {\r\n                if (decisionState.vars.get('challenger')=='true')\r\n                {\r\n                    decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC011'));\r\n                }\r\n                else\r\n                {\r\n                    decisionState.finish(true, MConst.refuseCode.MC011);\r\n                }\r\n            }\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==6 && creditRequest.peopleMain.peoplePersonalActive.age<45)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC011'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC011);\r\n            }\t\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==4 || creditRequest.peopleMain.currentEmployment.typeWork.codeInteger==7)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC012'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC012);\r\n            }\t\r\n\t}\r\n\tvar numContacts=0;\r\n\tif (creditRequest.peopleMain.workPhone!=null)\r\n\t{\r\n\t\tif (creditRequest.peopleMain.workPhone.value.slice(6)!='000000' && creditRequest.peopleMain.workPhone.value.slice(6)!='111111' && creditRequest.peopleMain.workPhone.value.slice(6)!='222222' && creditRequest.peopleMain.workPhone.value.slice(6)!='333333' && creditRequest.peopleMain.workPhone.value.slice(6)!='444444' && creditRequest.peopleMain.workPhone.value.slice(6)!='555555' && creditRequest.peopleMain.workPhone.value.slice(6)!='666666' && creditRequest.peopleMain.workPhone.value.slice(6)!='777777' && creditRequest.peopleMain.workPhone.value.slice(6)!='888888' && creditRequest.peopleMain.workPhone.value.slice(6)!='999999' && creditRequest.peopleMain.workPhone.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.workPhone.value.toString().substr(1,6)!='987654')\r\n\t\t{\r\n\t\t\tnumContacts++;\r\n\t\t}\r\n\t}\r\n\tif (creditRequest.peopleMain.homePhoneReg!=null)\r\n\t{\r\n\t\tif (creditRequest.peopleMain.homePhoneReg.value.slice(6)!='000000' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='111111' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='222222' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='333333' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='444444' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='555555' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='666666' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='777777' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='888888' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='999999' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,6)!='987654')\r\n\t\t{\r\n\t\t\tnumContacts++;\r\n\t\t}\r\n\t}\r\n\tif (creditRequest.peopleMain.homePhone!=null)\r\n\t{\t\r\n\t\tif (creditRequest.peopleMain.homePhone.value.slice(6)!='000000' && creditRequest.peopleMain.homePhone.value.slice(6)!='111111' && creditRequest.peopleMain.homePhone.value.slice(6)!='222222' && creditRequest.peopleMain.homePhone.value.slice(6)!='333333' && creditRequest.peopleMain.homePhone.value.slice(6)!='444444' && creditRequest.peopleMain.homePhone.value.slice(6)!='555555' && creditRequest.peopleMain.homePhone.value.slice(6)!='666666' && creditRequest.peopleMain.homePhone.value.slice(6)!='777777' && creditRequest.peopleMain.homePhone.value.slice(6)!='888888' && creditRequest.peopleMain.homePhone.value.slice(6)!='999999' && creditRequest.peopleMain.homePhone.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhone.value.toString().substr(1,6)!='987654')\r\n\t\t{\r\n\t\t\tnumContacts++;\r\n\t\t}\r\n\t}\r\n\tif (numContacts<1)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC013'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC013);\r\n            }\t\t\r\n\t}\r\n\t\r\n    /*if\t((creditRequest.dateContest.getTime()-creditRequest.peopleMain.currentEmployment.dateStartWork.getTime())/(1000*60*60*24)<30)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC014'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC014);\r\n            }\t\t\r\n\t}*/\r\n  \tvar trueSalary=0;\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==0)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary/12;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==1)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary/6;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==2)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary/3;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==3)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==4)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary*2;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==5)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary*4;\r\n\t}\r\n\tif (creditRequest.peopleMain.currentEmployment.duration.codeInteger==8)\r\n\t{\r\n\t\ttrueSalary=creditRequest.peopleMain.currentEmployment.salary;\r\n\t}\t\r\n\tif (residCode=='50' || residCode=='77')\r\n\t{\r\n\t\tif (trueSalary<8000) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC015'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.MC015);\r\n                    }\t\t\t\t\r\n\t\t}\r\n\t} else if (residCode=='47' || residCode=='78')\r\n\t{\r\n\t\tif (trueSalary<6000) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC015'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.MC015);\r\n                    }\t\t\t\r\n\t\t}\t\r\n\t} else \r\n\t{\r\n\t\tif (trueSalary<5000) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC015'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.MC015);\r\n                    }\t\t\t\t\r\n\t\t}\t\r\n\t}\r\n\tif\t((creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==1 && creditRequest.peopleMain.peoplePersonalActive.age<21) || (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2 && creditRequest.peopleMain.peoplePersonalActive.age<20))\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC016'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC016);\r\n            }\t\t\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age>72)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC017'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.MC017);\r\n            }\t\r\n\t}\r\n\t\r\n\t/*var res = CreditRequestSearch.search('peopleMain.id', creditRequest.peopleMain.id); \r\n\tif (res.count>0)\r\n\t{\r\n\t\tfor (var cr in res.data) \r\n\t\t{\r\n\t\t\t//if (DatesUtils.daysDiff(cr.dateContest,creditRequest.dateContest)<7)       \r\n                        if (((creditRequest.dateContest.getTime()-cr.dateContest.getTime())/(1000*60*60*24*365))<7)\r\n\t\t\t{\r\n                            if (decisionState.vars.get('challenger')=='true')\r\n                            {\r\n                                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC018'));\r\n                            }\r\n                            else\r\n                            {\r\n                                decisionState.finish(true, MConst.refuseCode.MC018);\r\n                            }\t\t\t\t\t\r\n\t\t\t}\r\n\t\t\t\r\n\t\t}\r\n\t}*/\r\n\t\r\n\tvar d = new java.util.Date(creditRequest.dateContest.getTime());\r\n\td.setHours(d.getHours() - 12);\r\n// something wrong with d-object :(\r\n//        var res = EventLogSearch.search("creditRequest.id",creditRequest.id, "eventCode", 4)\r\n//        if (res.count>0)\r\n//        {\r\n//                for (var log in res.data)\r\n//                {\r\n//                        var res2 = EventLogSearch.search('ipaddress',log.ipaddress,'eventTime.from', d, 'eventTime.to', creditRequest.dateContest,"eventCode", 4);\r\n//                        if (res2.count>0)\r\n//                        {\r\n//                            if (decisionState.vars.get('challenger')=='true')\r\n//                            {\r\n//                                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC020'));\r\n//                            }\r\n//                            else\r\n//                            {\r\n//                                decisionState.finish(true, MConst.refuseCode.MC020);\r\n//                            }\t\r\n//                        }\r\n//                }\r\n//        }\r\n//        else\r\n//        {\r\n//            if (decisionState.vars.get('challenger')=='true')\r\n//            {\r\n//                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('MC020'));\r\n//            }\r\n//            else\r\n//            {\r\n//                decisionState.finish(true, MConst.refuseCode.MC020);\r\n//            }\t\r\n//        }\r\n\t\r\n\t\r\n\tdecisionState.nextLabel = 'internal_history';\r\n} \r\nelse if (decisionState.nextLabel == 'internal_history') \r\n{\r\n\t// Checking internal history\r\n\r\n  if (creditRequest.peopleMain.hasCredits>0) \r\n  {\r\n\r\n\t  for (var credit in creditRequest.peopleMain.credits) \r\n\t  {\r\n\t\tif (credit.isSameOrg && !credit.isOver) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('IH003'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.IH003);\r\n                    }\t\t\t\r\n\t\t}\r\n\t  }\r\n  }\r\n  \r\n\tvar sum1 = 0;\r\n\tvar maxdelq = 0;\r\n  \r\n  if (creditRequest.peopleMain.hasCredits>0) \r\n  {\r\n\tfor (var credit in creditRequest.peopleMain.credits) \r\n\t{\r\n\t\tif (credit.isSameOrg ) \r\n\t\t{\r\n          if (credit.currentOverdueDebt!=null) {\r\n\t\t\tsum1 = sum1 + credit.currentOverdueDebt;\r\n          }\r\n          if (credit.maxDelay!=null){\r\n\t\t\tif (maxdelq<=credit.maxDelay)\r\n\t\t\t{\r\n\t\t\t\tmaxdelq=credit.maxDelay;\r\n\t\t\t}\r\n          }\r\n\t\t}\r\n\t}\r\n  }\r\n\tif (sum1>1000 || sum1>creditRequest.creditSum*0.75)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('IH001'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.IH001);\r\n            }\t\t\r\n\t}\r\n\tif (maxdelq>=90)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('IH004'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.IH004);\r\n            }\t\t\r\n\t}\r\n\t\t\t\r\n\t//decisionState.nextLabel = 'final_decision';\r\n\tdecisionState.nextLabel = 'external_history_get';\r\n}\r\nelse if (decisionState.nextLabel == 'external_history_get') \r\n{\r\n    // Calling external history services\r\n    decisionState.externalSystems.get('plugin_okbCais').needCall = true;\r\n    decisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n    decisionState.nextLabel = 'external_history';\r\n}\r\nelse if (decisionState.nextLabel == 'external_history') \r\n{\r\n\t// External\r\n\t\t\r\n\tvar sum1 = 0;\r\n\tvar maxdelq = 0;\r\n\tvar numCredits = 0;\r\n\tvar actCredits = 0;\r\n\tvar maxdelqtotal = 0;\r\n        \r\n        \r\n    if (creditRequest.peopleMain.hasCredits>0) \r\n    {\r\n        \r\n\t   for (i=0;i<creditRequest.peopleMain.credits.size();i++) \r\n\t   {\r\n                decisionState.vars.put('creditDataBeg'.concat(i), creditRequest.peopleMain.credits.get(i).creditDataBeg);\r\n                decisionState.vars.put('creditDataEnd'.concat(numCredits), creditRequest.peopleMain.credits.get(i).creditDataEnd);\r\n                decisionState.vars.put('creditDataEndFact'.concat(numCredits), creditRequest.peopleMain.credits.get(i).creditDataEndFact);\r\n                decisionState.vars.put('creditSumDebt'.concat(numCredits), creditRequest.peopleMain.credits.get(i).creditSumDebt);\r\n                if (creditRequest.peopleMain.credits.get(i).creditStatus!=null)\r\n                    decisionState.vars.put('creditStatus'.concat(numCredits), creditRequest.peopleMain.credits.get(i).creditStatus.name);\r\n                decisionState.vars.put('creditSum'.concat(numCredits), creditRequest.peopleMain.credits.get(i).creditSum);\r\n                decisionState.vars.put('isSameOrg'.concat(numCredits), creditRequest.peopleMain.credits.get(i).isSameOrg);\r\n                \r\n                \r\n\t\t        numCredits++;\r\n         \r\n         if (creditRequest.peopleMain.credits.get(i).isOver!=null){ \r\n\t\t    if (!creditRequest.peopleMain.credits.get(i).isOver)\r\n\t\t    {\r\n\t\t\t    actCredits++;\r\n\t\t    } \r\n         }//end if isover\r\n         \r\n          if (creditRequest.peopleMain.credits.get(i).maxDelay!=null) {\r\n\t\t     if (maxdelqtotal<=creditRequest.peopleMain.credits.get(i).maxDelay)\r\n\t\t      {\r\n\t\t\t   maxdelqtotal=creditRequest.peopleMain.credits.get(i).maxDelay;\r\n\t\t       }\r\n          }//end if max delay\r\n\t\t\r\n          if (!creditRequest.peopleMain.credits.get(i).isSameOrg) \r\n\t\t  {\r\n\t\t\tif (creditRequest.peopleMain.credits.get(i).dateLastUpdate==null)\r\n\t\t\t{\r\n               if (creditRequest.peopleMain.credits.get(i).currentOverdueDebt!=null){\r\n\t\t  \t\t sum1 = sum1 + creditRequest.peopleMain.credits.get(i).currentOverdueDebt;\r\n               } \r\n\t\t\t}  \t\t\t\r\n            else if (((creditRequest.dateContest.getTime()-creditRequest.peopleMain.credits.get(i).dateLastUpdate.getTime())/(1000*60*60*24*365))<100)\r\n\t\t\t{\r\n              if (creditRequest.peopleMain.credits.get(i).currentOverdueDebt!=null){\r\n\r\n\t\t\t\tsum1 = sum1 + creditRequest.peopleMain.credits.get(i).currentOverdueDebt; \r\n              }\r\n\t\t\t}//end if last update\r\n\t\t\t\r\n            if (creditRequest.peopleMain.credits.get(i).maxDelay!=null){\r\n               if (maxdelq<=creditRequest.peopleMain.credits.get(i).maxDelay){\r\n\t\t\t\t  maxdelq=creditRequest.peopleMain.credits.get(i).maxDelay;\r\n               } \r\n\t\t\t}//end if max delay\r\n            \r\n\t\t}//end if same org\r\n\t \r\n       }//end for\r\n\r\n     }//end if hascredits\r\n\r\n\r\n    decisionState.vars.put('numCredits', numCredits);\r\n\t\r\n    if (numCredits<=0)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('EH005'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.EH005);\r\n            }\t\r\n\t}\r\n\tif (sum1>1000 || sum1>creditRequest.creditSum*0.75)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('EH001'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.EH001);\r\n            }\t\r\n\t}\r\n\tif (maxdelq>=90)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('EH004'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.EH004);\r\n            }\t\r\n\t}\r\n\tif (actCredits>=5 && creditRequest.creditSum>=7000)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('EH006'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.EH006);\r\n            }\t\r\n\t}\r\n\r\n\tvar score1=20;\r\n  \r\n\tif (creditRequest.peopleMain.currentEmployment.education.codeInteger==4 || creditRequest.peopleMain.currentEmployment.education.codeInteger==5 || creditRequest.peopleMain.currentEmployment.education.codeInteger==6)\r\n\t{\r\n\tscore1=score1+6;\r\n\t}\r\n  \r\n    if (creditRequest.peopleMain.currentEmployment.profession!=null) {\r\n\r\n\t  if (creditRequest.peopleMain.currentEmployment.profession.codeInteger==21 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==2)\r\n\t  {\r\n\t  }\r\n\t  else if (creditRequest.peopleMain.currentEmployment.profession.codeInteger==5 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==13 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==10 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==24 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==98 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==15 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==19 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==22 || creditRequest.peopleMain.currentEmployment.profession.codeInteger==11)\r\n\t  {\r\n\t\tscore1=score1+12;\r\n\t  }\r\n\t  else\r\n\t  {\r\n\t\tscore1=score1+30;\r\n\t  }\r\n    }// end if profession not null\r\n  \r\n\tif (creditRequest.peopleMain.peopleMiscActive.car)\r\n\t{\r\n\t\tscore1=score1+13;\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.age>36)\r\n\t{\r\n\t\tscore1=score1+10;\r\n\t}\r\n\tif (creditRequest.peopleMain.homePhoneReg!=null && creditRequest.peopleMain.homePhone!=null)\r\n\t{\r\n\t\tif (creditRequest.peopleMain.homePhoneReg.value.slice(6)!='000000' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='111111' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='222222' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='333333' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='444444' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='555555' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='666666' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='777777' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='888888' && creditRequest.peopleMain.homePhoneReg.value.slice(6)!='999999' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhoneReg.value.toString().substr(1,6)!='987654' && creditRequest.peopleMain.homePhone.value.slice(6)!='000000' && creditRequest.peopleMain.homePhone.value.slice(6)!='111111' && creditRequest.peopleMain.homePhone.value.slice(6)!='222222' && creditRequest.peopleMain.homePhone.value.slice(6)!='333333' && creditRequest.peopleMain.homePhone.value.slice(6)!='444444' && creditRequest.peopleMain.homePhone.value.slice(6)!='555555' && creditRequest.peopleMain.homePhone.value.slice(6)!='666666' && creditRequest.peopleMain.homePhone.value.slice(6)!='777777' && creditRequest.peopleMain.homePhone.value.slice(6)!='888888' && creditRequest.peopleMain.homePhone.value.slice(6)!='999999' && creditRequest.peopleMain.homePhone.value.toString().substr(1,5)!='12345' && creditRequest.peopleMain.homePhone.value.toString().substr(1,6)!='987654' && creditRequest.peopleMain.homePhone.available == true && creditRequest.peopleMain.homePhoneReg.available == true)\r\n\t\t{\r\n\t\t\tscore1=score1+10;\r\n\t\t}\r\n\t}\r\n\tif (((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>13.9 && (creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<14.2) || ((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>19.9 && (creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<20.2) || ((creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)>44.9 && (creditRequest.peopleMain.activePassport.docDate.getTime()-creditRequest.peopleMain.peoplePersonalActive.birthDate.getTime())/(1000*60*60*24*365)<45.2))\r\n\t{\r\n\t\tscore1=score1+15;\r\n\t} else if (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2 && (creditRequest.peopleMain.peopleMiscActive.marriage.codeInteger==1 || creditRequest.peopleMain.peopleMiscActive.marriage.codeInteger==2 || creditRequest.peopleMain.peopleMiscActive.marriage.codeInteger==3))\r\n\t{\r\n\t\tscore1=score1+15;\r\n\t}\r\n\tif (creditRequest.creditDays<=7)\r\n\t{\r\n\t\tscore1=score1+35;\r\n\t}\r\n\telse if (creditRequest.creditDays<=15)\r\n\t{\r\n\t\tscore1=score1+14;\r\n\t}\r\n\tif (creditRequest.creditSum<=3000)\r\n\t{\r\n\t\tscore1=score1+31;\r\n\t}\r\n\telse if (creditRequest.creditSum<=5000)\r\n\t{\r\n\t\tscore1=score1+17;\r\n\t}\r\n\tif (creditRequest.peopleMain.peoplePersonalActive.gender.codeInteger==2)\r\n\t{\r\n\t\tscore1=score1+11;\r\n\t}\r\n  \r\n  if (creditRequest.peopleMain.currentEmployment.typeWork!=null)\r\n  {\r\n\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=9 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=6 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=7 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=2 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=4)\r\n\t{\r\n\t\tif ((creditRequest.dateContest.getTime()-creditRequest.peopleMain.currentEmployment.dateStartWork.getTime())/(1000*60*60*24*30)>7.9)\r\n\t\t{\r\n\t\t\tscore1=score1+12;\r\n\t\t} else if ((creditRequest.dateContest.getTime()-creditRequest.peopleMain.currentEmployment.dateStartWork.getTime())/(1000*60*60*24*30)>2.5)\r\n\t\t{\r\n\t\t\tscore1=score1+4;\r\n\t\t}\r\n\t}\r\n\telse\r\n\t{\r\n\t\tif (creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=9 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=7 && creditRequest.peopleMain.currentEmployment.typeWork.codeInteger!=4)\r\n\t\t{\r\n\t\t\tscore1=score1+4;\r\n\t\t}\r\n\t}\r\n  }//end if typework not null\r\n  \r\n\tif (maxdelq==0 && numCredits>0)\r\n\t{\r\n\t\tscore1=score1+27;\r\n\t}\r\n\tif (creditRequest.peopleMain.peopleMiscActive.children!=null)\r\n\t{\r\n\t\tscore1=score1+10;\r\n\t}\r\n  \r\n\tif (score1<=100)\r\n\t{\r\n            if (decisionState.vars.get('challenger')=='true')\r\n            {\r\n                decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('JS001'));\r\n            }\r\n            else\r\n            {\r\n                decisionState.finish(true, MConst.refuseCode.JS001);\r\n            }\t\t\r\n\t}\r\n\t\r\n    if (creditRequest.hasVerif>0)\r\n    {\r\n\t  for (var v in creditRequest.verif) \r\n\t  { \r\n\t\tif (v.verificationMark < 50 || v.validationMark < 10) \r\n\t\t{\r\n                    if (decisionState.vars.get('challenger')=='true')\r\n                    {\r\n                        decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('ES001'));\r\n                    }\r\n                    else\r\n                    {\r\n                        decisionState.finish(true, MConst.refuseCode.ES001);\r\n                    }\t\r\n\t\t}\r\n\t  }//end for\r\n    }//end if has verif\r\n\r\n\tdecisionState.nextLabel = 'verification_get';\r\n}\r\nelse if (decisionState.nextLabel == 'verification_get') \r\n{\r\n\t// Calling verification service\r\n//    decisionState.externalSystems.get('plugin_verify').needCall = true;\r\n//    verify.question('VerConclusion').add();    \r\n//    if (Math.random()<0.5)\r\n//    {\r\n//        verify.question('VerHoroscope').add();\r\n//    }\r\n//        if (Math.random()<0.5)\r\n//    {\r\n//        verify.question('VerStreet').add();\r\n//    }\r\n//    if (Math.random()<0.5)\r\n//    {\r\n//        verify.question('VerAim').add();\r\n//    }\r\n//    if (Math.random()<0.5 && (creditRequest.peopleMain.currentEmployment.education.codeInteger==4 || creditRequest.peopleMain.currentEmployment.education.codeInteger==5 || creditRequest.peopleMain.currentEmployment.education.codeInteger==6))\r\n//    {\r\n//        verify.question('VerUniversity1').add();\r\n//    }\r\n//    if (Math.random()<0.5 && (creditRequest.peopleMain.currentEmployment.education.codeInteger==4 || creditRequest.peopleMain.currentEmployment.education.codeInteger==5 || creditRequest.peopleMain.currentEmployment.education.codeInteger==6))\r\n//    {\r\n//        verify.question('VerUniversity2').add();\r\n//    }\r\n//    if (Math.random()<0.5)\r\n//    {\r\n//        verify.question('VerStranger').add();\r\n//    }\r\n//    if (Math.random()<0.5)\r\n//    {\r\n//        verify.question('VerFather').add();\r\n//    }\r\n//    if (Math.random()<0.5)\r\n//    {\r\n//        verify.question('VerMobile').add();\r\n//    }    \r\n//    if (Math.random()<0.5 && creditRequest.peopleMain.peopleMiscActive.marriage.codeInteger==0)\r\n//    {\r\n//        verify.question('VerSpouce').add();\r\n//    }\r\n//    if (Math.random()<0.5)\r\n//    {\r\n//        verify.question('VerAge').add();\r\n//    }    \r\n//    if (Math.random()<0.5 && (creditRequest.peopleMain.currentEmployment.education.codeInteger==0 || creditRequest.peopleMain.currentEmployment.education.codeInteger==3 || creditRequest.peopleMain.currentEmployment.education.codeInteger==2 || creditRequest.peopleMain.currentEmployment.education.codeInteger==1))\r\n//    {\r\n//        verify.question('VerSchool').add();\r\n//    }    \r\n        \r\n    decisionState.nextLabel = 'verification';\r\n}\r\nelse if (decisionState.nextLabel == 'verification') \r\n{\r\n// Verification results\r\n// \r\n//\tuse later - for (var vv in creditRequest.answers) \r\n//\t\r\n//    if (verify.question('VerConclusion').status!=MConst.answerStatus.ANSWER_STATUS_NOT_ASKED && verify.question('VerConclusion').status!=MConst.answerStatus.ANSWER_STATUS_DENIAL)\r\n//\t{\r\n//            if (verify.question('VerConclusion')!=9)\r\n//            {\r\n//                if (decisionState.vars.get('challenger')=='true')\r\n//                {\r\n//                    decisionState.vars.put('RejectCode', decisionState.vars.get('RejectCode').concat('VF001'));\r\n//                }\r\n//                else\r\n//                {\r\n//                    decisionState.finish(true, MConst.refuseCode.VF001);\r\n//                }\t                \r\n//            }\r\n//\t}\t\r\n\tdecisionState.nextLabel = 'final_decision';\r\n}\r\nelse if (decisionState.nextLabel == 'final_decision') \r\n{ \r\n\t// Final decision\r\n\tdecisionState.finish(true, MConst.decision.RESULT_ACCEPT);\r\n}\r\n	credit.decision	text/javascript	\N
2907	2014-08-15 20:57:48.258+07	0.8	0	if (! decisionState.nextLabel) {\r\n\tdecisionState.start();\r\n\tdecisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n\tdecisionState.externalSystems.get('plugin_verify').needCall = true;\r\n        decisionState.externalSystems.get('plugin_okbIdv').needCall = false;\r\n        decisionState.externalSystems.get('plugin_okbCais').needCall = false;\r\n\tverify.question('age.verify').add();\r\n\tverify.question('credit.last').add();\r\n\tdecisionState.nextLabel = 'label_finish';\r\n} else if (decisionState.nextLabel == 'label_finish') {\t\r\n\tdecisionState.finish(true, MConst.decision.RESULT_ACCEPT);\r\n}\r\n	credit.decision	text/javascript	\N
2908	2014-08-15 21:08:20.295+07	0.9	0	if (! decisionState.nextLabel) {\r\n67890\r\n\tdecisionState.start();\r\n\tdecisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n\tdecisionState.nextLabel = 'label_finish';\r\n} else if (decisionState.nextLabel == 'label_finish') {\t\r\n\tactionProcessor.saveCreditRequestDecision(creditRequest.id, true, null);\r\n\tdecisionState.finish(true);\r\n}	credit.decision	text/javascript	\N
3829	2015-03-18 08:47:46.583+07	12345	2	if (! decisionState.nextLabel) {\r\n\tdecisionState.start();\r\n\t//decisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n\t//decisionState.externalSystems.get('plugin_okbIdv').needCall = true;\r\n        //decisionState.externalSystems.get('plugin_okbCais').needCall = true;\r\n\t//decisionState.externalSystems.get('plugin_equifax').needCall = true;\r\n  //decisionState.externalSystems.get('plugin_verify').needCall = true;\t\r\n  //verify.question('client.marry').add();\r\n  //verify.question('client.work').add();\r\n  //verify.question('age.verify').add(); verify.question('salary.verify').add();\r\n   decisionState.nextLabel = 'label_check';\r\n } else if (decisionState.nextLabel == 'label_check') {\r\n    \r\n   var ncredits = 0;\r\n   \r\n     if (creditRequest.hasCredits>0) \r\n    {\r\n     for (i=0; i<creditRequest.credits.size(); i++)  \r\n     {\r\n      if (creditRequest.credits.get(i).partner.id!=6) \r\n       {\r\n\r\n        ncredits++;\r\n       }\r\n     }\r\n    }\r\n   \r\n  decisionState.vars.put('ncredits', ncredits);\r\n   if (ncredits==0)\r\n   {  \r\n    // decisionState.finish(true, MConst.refuseCode.MC02);\r\n   }\r\n\r\n   var ver = 0;\r\n   \r\n     if (creditRequest.hasVerif>0) \r\n    {\r\n     for (i=0; i<creditRequest.verif.size(); i++)  \r\n     {\r\n      \r\n        ver++;\r\n     }\r\n    }\r\n   \r\n  decisionState.vars.put('ver', ver);\r\n  var dinfow;\r\n   if (creditRequest.deviceInfo!=null){\r\n     dinfow=creditRequest.deviceInfo.resolutionWidth;\r\n   }\r\n   decisionState.vars.put('dinfow', dinfow);\r\n  decisionState.nextLabel = 'label_finish';\r\n} else if (decisionState.nextLabel == 'label_finish') {\t\r\n\tdecisionState.finish(true, MConst.decision.RESULT_ACCEPT);\r\n}\r\n	credit.decision	text/javascript	2015-03-18 08:50:35.573+07
483	2014-06-05 23:26:36.047+07	0.6	1	if (! decisionState.nextLabel) {\r\n\tdecisionState.start();\r\n\t//decisionState.externalSystems.get('plugin_rusStandart').needCall = true;\r\n\t//decisionState.externalSystems.get('plugin_okbIdv').needCall = true;\r\n        //decisionState.externalSystems.get('plugin_okbCais').needCall = true;\r\n\t//decisionState.externalSystems.get('plugin_equifax').needCall = true;\r\n  //decisionState.externalSystems.get('plugin_verify').needCall = true;\t\r\n  //verify.question('client.marry').add();\r\n  //verify.question('client.work').add();\r\n  //verify.question('age.verify').add(); verify.question('salary.verify').add();\r\n   decisionState.nextLabel = 'label_check';\r\n } else if (decisionState.nextLabel == 'label_check') {\r\n    \r\n   var ncredits = 0;\r\n   \r\n     if (creditRequest.hasCredits>0) \r\n    {\r\n     for (i=0; i<creditRequest.credits.size(); i++)  \r\n     {\r\n      if (creditRequest.credits.get(i).partner.id!=6) \r\n       {\r\n\r\n        ncredits++;\r\n       }\r\n     }\r\n    }\r\n   \r\n  decisionState.vars.put('ncredits', ncredits);\r\n   if (ncredits==0)\r\n   {  \r\n    // decisionState.finish(true, MConst.refuseCode.MC02);\r\n   }\r\n\r\n   var ver = 0;\r\n   \r\n     if (creditRequest.hasVerif>0) \r\n    {\r\n     for (i=0; i<creditRequest.verif.size(); i++)  \r\n     {\r\n      \r\n        ver++;\r\n     }\r\n    }\r\n   \r\n  decisionState.vars.put('ver', ver);\r\n   \r\n  decisionState.nextLabel = 'label_finish';\r\n} else if (decisionState.nextLabel == 'label_finish') {\t\r\n\tdecisionState.finish(true, MConst.decision.RESULT_ACCEPT);\r\n}\r\n	credit.decision	text/javascript	\N
\.


--
-- TOC entry 2002 (class 0 OID 101251)
-- Dependencies: 165
-- Data for Name: aimodelparams; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY aimodelparams (aimodel_id, name, valuetext, valuenumber, valuedate, valuefloat) FROM stdin;
\.


--
-- TOC entry 2003 (class 0 OID 101257)
-- Dependencies: 166
-- Data for Name: airule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY airule (id, packagename, body, ruletype, description, kbase) FROM stdin;
2	ru.simplgroupp.rules.newrequest.limits	\N	2	Константы для займа - незарегистрированный пользователь	\N
3	ru.simplgroupp.rules.existuser.newrequest.limits	\N	2	Константы для займа - зарегистрированный пользователь	\N
4	ru.simplgroupp.rules.prolong.limits	\N	2	Константы для продления займа	\N
11	ru.simplgroupp.rules.overdue.limits	\N	2	Константы для просроченного займа	\N
8	ru.simplgroupp.crypto.common	\N	2	Константы для подписания ЭЦП	\N
6	ru.simplgroupp.bp.options.procConsiderCR	\N	2	Константы для процесса принятия решения по займу	\N
9	ru.simplgroupp.bp.options.procCR	\N	2	Константы для процесса выдачи займа	\N
10	ru.simplgroupp.payment.systems.order	\N	2	Порядок использования платежных систем	\N
5	ru.simplgroupp.bp.messages.procConsiderCR	\N	2	Сообщения для процесса принятия решения по займу	\N
7	ru.simplgroupp.plugins	\N	2	Рабочие настройки	\N
14	ru.simplgroupp.rules.creditreturn.limits	package ru.simplgroupp.rules.creditreturn.limits;\n\nimport ru.simplgroupp.rules.NameValueRuleResult\nimport ru.simplgroupp.rules.CreditRequestContext\nimport ru.simplgroupp.transfer.Credit\nimport ru.simplgroupp.transfer.PeopleMain\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport java.util.Map\n\nrule "ru.simplgroupp.rules.creditreturn.limits.1"\nwhen \n    $cparams : Map();\n    $credit : Credit();\n    $bResult : NameValueRuleResult();\nthen\n    $bResult.setValue("money.min", $cparams.get(CreditCalculatorBeanLocal.SUM_ABOVE));\nend	1	Параметры для возвращаемой суммы	kbReturn
1	ru.simplgroupp.rules.watched	package ru.simplgroupp.rules.watched\n\nimport java.util.List\n\nrule "ru.simplgroupp.rules.watched"\nwhen \n    eval(true);\n    $a : List();\nthen\n    $a.add(1); \nend\n	1	\N	kbWatchedFields
13	ru.simplgroupp.rules.creditreturn.condition	package ru.simplgroupp.rules.creditreturn.condition;\n\nimport ru.simplgroupp.rules.BooleanRuleResult\n\nrule "ru.simplgroupp.rules.creditreturn.condition.1"\nwhen \n    eval(true);\n    $bResult : BooleanRuleResult();\nthen\n    $bResult.setResultValue(true);\nend	1	Доступно ли продление	kbReturn
17	ru.simplgroupp.bp.messages.procCR	\N	2	Сообщения для основного процесса	\N
18	ru.simplgroupp.bp.messages.procReturnCR	\N	2	Сообщения для процесса возврата кредита	\N
19	ru.simplgroupp.bp.options.procProlongCR	\N	2	Константы для процесса продления	\N
23	ru.simplgroupp.bp.messages.procProlongCR	\N	2	Сообщения для процесса продления	\N
24	ru.simplgroupp.bo.options.ru.simplgroupp.transfer.CreditRequest	\N	2	Константы для заявки	\N
26	ru.simplgroupp.bp.options.procCreditRequestmsgChangeRequest	\N	2	Настро для процесса изменений в заявке	\N
15	ru.simplgroupp.rules.credit.calculator.initial	package ru.simplgroupp.rules.credit.calculator.initial;\r\n\r\nimport ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.toolkit.common.NameValuePair\r\nimport ru.simplgroupp.interfaces.service.HolidaysService\r\nimport java.util.Date\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.transfer.Account\r\nimport org.apache.commons.lang.time.DateUtils\r\n\r\nrule "ru.simplgroupp.rules.credit.calculator.initial.1"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $cparams : Map();\r\n    $ruleKey : NameValuePair(name == "ruleKey" && value == "ru.simplgroupp.rules.credit.calculator.initial.1" );\r\nthen\r\n    double creditSum = ((Double) $cparams.get(CreditCalculatorBeanLocal.SUM_MAIN)).doubleValue();\r\n    double creditStake = ((Double) $cparams.get(CreditCalculatorBeanLocal.STAKE_INITIAL)).doubleValue();\r\n    int creditDays = ((Number) $cparams.get(CreditCalculatorBeanLocal.DAYS_INITIAL)).intValue();\r\n    \r\n    creditDays = creditDays + 1;\r\n    double sumPercent = CalcUtils.roundHalfUp(creditSum * creditStake * creditDays,0);\r\n    \r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_MAIN, new Double(creditSum));\r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_PERCENT, new Double(sumPercent));\r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_BACK, new Double(creditSum + sumPercent));\r\nend\r\n\r\nrule "ru.simplgroupp.rules.credit.calculator.initial.2"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $ccRequest : CreditRequest();\r\n    $holiServ : HolidaysService();\r\nthen\r\n    double creditSum = $ccRequest.getCreditSum();\r\n    double creditStake = $ccRequest.getStake();\r\n    int creditDays = $ccRequest.getCreditDays();\r\n    Date actualDateStart = $ccRequest.getDateSign();\r\n    \r\n    if ($ccRequest.getAccount().getAccountType().getCodeInteger() == Account.BANK_TYPE) {\r\n     int holiDays = $holiServ.getDaysOfHolidays(actualDateStart);\r\n     creditDays = creditDays - holiDays;\r\n     actualDateStart = DateUtils.addDays(actualDateStart, holiDays);\r\n    }\r\n    \r\n    double sumPercent = CalcUtils.roundHalfUp(creditSum * creditStake * (creditDays+1),0);\r\n    \r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_MAIN, new Double(creditSum));\r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_PERCENT, new Double(sumPercent));\r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_BACK, new Double(creditSum + sumPercent));\r\n    $bResult.setValue(CreditCalculatorBeanLocal.DAYS_ACTUAL, new Integer(creditDays));\r\n    $bResult.setValue(CreditCalculatorBeanLocal.DATE_START_ACTUAL, actualDateStart);\r\nend	1	Рассчитываем первоначальные параметры кредита	kbCreditCalc
27	ru.simplgroupp.bp.messages.procNewCR	\N	2	Сообщения для процесса procNewCR	\N
28	ru.simplgroupp.bp.options.procPayment	\N	2	Настройки для процесса procPayment	\N
29	ru.simplgroupp.ba.messages.ru.simplgroupp.transfer.Credit.msgOverdueSMS	\N	2	Тексты писем для ru.simplgroupp.transfer.Credit.msgOverdueSMS	\N
30	ru.simplgroupp.ba.messages.ru.simplgroupp.transfer.Credit.msgOverdueEmail	\N	2	Тексты писем для ru.simplgroupp.transfer.Credit.msgOverdueEmail	\N
32	ru.simplgroupp.sms.common	\N	2	Константы для отправки смс из системы	\N
33	ru.simplgroupp.email.common	\N	2	Константы для отправки email из системы	\N
34	ru.simplgroupp.socnetwork.common	\N	2	Константы для соц.сетей	\N
37	ru.simplgroupp.product.R001.1	\N	2	Константы для рефинансирования	\N
35	ru.simplgroupp.site.common	\N	2	Константы запроса информации с разных сайтов	\N
20	ru.simplgroupp.rules.credit.calculator.stake1	package ru.simplgroupp.rules.credit.calculator.stake1;\r\ndialect "java"\r\n\r\nimport ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.util.DatesUtils\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.transfer.Credit\r\nimport ru.simplgroupp.toolkit.common.IntegerRange\r\nimport ru.simplgroupp.toolkit.common.DateRange\r\nimport ru.simplgroupp.toolkit.common.Convertor;\r\nfunction double calcStakeInitial(double creditSum, int creditDays, double stakeMin, double stakeMax) {\r\n    Double c = 0.0435275977526034 * Math.pow(1.00000396879854, creditSum) * Math.pow(0.972552886121762, creditDays);\r\n    double d = CalcUtils.roundHalfUp( c, 3);\r\n    if (d < stakeMin) {\r\n       d = stakeMin;\r\n    }\r\n    if (d > stakeMax) {\r\n       d = stakeMax;\r\n    }\r\n    Double s = d * 1000 % 10;\r\n    if (s != 0) {\r\n     if (s == 1 || s == 6) {\r\n      d = CalcUtils.roundHalfUp( d - 0.001, 3);\r\n        }\r\n        if (s == 2 || s == 7) {\r\n         d = CalcUtils.roundHalfUp( d - 0.002, 3);\r\n     }\r\n        if (s == 3 || s == 8) {\r\n         d = CalcUtils.roundHalfUp( d + 0.002, 3);\r\n        }\r\n        if (s == 4 || s == 9) {\r\n         d = CalcUtils.roundHalfUp( d + 0.001, 3);\r\n        }\r\n    }    \r\n return d;\r\n}\r\n\r\nrule "ru.simplgroupp.rules.credit.calculator.stake1.1"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $cparams : Map();\r\n    $creditSum : Double();\r\n    $creditDays : Integer();\r\nthen\r\n    double stakeMin = ((Double) $cparams.get(CreditCalculatorBeanLocal.STAKE_LIMIT_MIN)).doubleValue();\r\n    double stakeMax = ((Double) $cparams.get(CreditCalculatorBeanLocal.STAKE_LIMIT_MAX)).doubleValue();\r\n    \r\n    double d = calcStakeInitial($creditSum, $creditDays, stakeMin, stakeMax); \r\n    \r\n    $bResult.setValue(CreditCalculatorBeanLocal.STAKE_INITIAL, new Double(d));\r\nend\r\n\r\nrule "ru.simplgroupp.rules.credit.calculator.stake1.2"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $cparams : Map();\r\n    $credit : Credit();\r\n    $dateCalc : Date();    \r\nthen \r\n CreditRequest ccRequest = $credit.getCreditRequest();\r\n \r\n   \r\n Object[] scaleDays = CalcUtils.intRangeScale(\r\n  0, $credit.getCreditDays(),\r\n  $credit.getCreditDays(), ((Integer) $cparams.get("credit.days.max.lgot")).intValue()+$credit.getCreditDays() ,\r\n  ((Integer) $cparams.get("credit.days.min.common")).intValue() +$credit.getCreditDays()-1, \r\n                ((Integer) $cparams.get("credit.days.max.common")).intValue()+$credit.getCreditDays(),\r\n  ((Integer) $cparams.get("credit.days.min.overdue")).intValue() +$credit.getCreditDays()-1, \r\n                ((Integer) $cparams.get("credit.days.max.overdue")).intValue()+$credit.getCreditDays()\r\n );\r\n\r\n \r\n Double initialStake = new Double(0.0);\r\n if ($cparams.get(CreditCalculatorBeanLocal.STAKE_INITIAL) == null) {\r\n     double stakeMin = ((Double) $cparams.get(CreditCalculatorBeanLocal.STAKE_LIMIT_MIN)).doubleValue();\r\n     double stakeMax = ((Double) $cparams.get(CreditCalculatorBeanLocal.STAKE_LIMIT_MAX)).doubleValue();    \r\n     double d = calcStakeInitial(ccRequest.getCreditSum(), ccRequest.getCreditDays(), stakeMin, stakeMax);   \r\n     initialStake = new Double(d);\r\n } else {\r\n  initialStake = (Double) $cparams.get(CreditCalculatorBeanLocal.STAKE_INITIAL);\r\n }\r\n\r\n \r\n Double[] stakeValues = new Double[] {\r\n  initialStake,\r\n  (Double) $cparams.get("credit.stake.lgot")+initialStake,\r\n  (Double) $cparams.get("credit.stake.common")+initialStake,\r\n  (Double) $cparams.get("credit.stake.overdue")+initialStake \r\n };\r\n Object[] scaleDates = CalcUtils.daysToDatesScale($credit.getCreditDataBeg(), scaleDays);\r\n \r\n double sumMain = 0.0;\r\n        double sumAdd = 0.0;\r\n Date dateStart = $credit.getDateMainRemain();\r\n sumMain = $credit.getSumMainRemain();\r\n \r\n if (! $credit.getPartialPayment() ) {\r\n  // не было частичной оплаты займа\r\n    sumAdd=sumMain*initialStake;\r\n }\r\n \r\n // рассчитываем, в каком интервале мы находимся\r\n int startInterval = CalcUtils.getScalePos(scaleDates, dateStart);\r\n int calcInterval = CalcUtils.getScalePos(scaleDates, $dateCalc);\r\n // рассчитываем сумму процентов\r\n double sumPercent = CalcUtils.calcSumPercent(sumMain, dateStart, stakeValues, scaleDates, $dateCalc)+sumAdd;\r\n \r\n $bResult.setValue(CreditCalculatorBeanLocal.INVERVAL_CURRENT, calcInterval);\r\n $bResult.setValue(CreditCalculatorBeanLocal.INVERVAL_START, startInterval); \r\nif (calcInterval!=-1){ \r\n $bResult.setValue(CreditCalculatorBeanLocal.DAYS_CURRENT_START, Convertor.integerRangeFrom(scaleDays[calcInterval])); \r\n $bResult.setValue(CreditCalculatorBeanLocal.DAYS_CURRENT_END, Convertor.integerRangeTo(scaleDays[calcInterval]));\r\n $bResult.setValue(CreditCalculatorBeanLocal.DATE_CURRENT_START, Convertor.dateRangeFrom(scaleDates[calcInterval])); \r\n $bResult.setValue(CreditCalculatorBeanLocal.DATE_CURRENT_END, Convertor.dateRangeTo(scaleDates[calcInterval])); \r\n $bResult.setValue(CreditCalculatorBeanLocal.STAKE_CURRENT, new Double(stakeValues[calcInterval])); \r\n} else {\r\n  $bResult.setValue(CreditCalculatorBeanLocal.STAKE_CURRENT, initialStake);\r\n}\r\n $bResult.setValue(CreditCalculatorBeanLocal.SUM_MAIN, sumMain);\r\n $bResult.setValue(CreditCalculatorBeanLocal.SUM_PERCENT, new Double(sumPercent));\r\n $bResult.setValue(CreditCalculatorBeanLocal.SUM_ABOVE, new Double(sumPercent));\r\n $bResult.setValue(CreditCalculatorBeanLocal.SUM_BACK, new Double( sumMain + sumPercent));\r\n \r\nend	1	Первоначальная ставка	kbCreditCalc
41	ru.simplgroupp.bp.options.procRefinance	\N	2	Настройки для процесса procRefinance	\N
42	ru.simplgroupp.ba.messages.ru.simplgroupp.transfer.Credit.msgRefinanceEmail	\N	2	\N	\N
40	ru.simplgroupp.rules.bonus.limits	\N	2	Константы для начисления бонусов	\N
43	ru.simplgroupp.rules.credit.calculator.params	package ru.simplgroupp.rules.credit.calculator.params;\r\ndialect "java"\r\n\r\nimport ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.toolkit.common.Convertor;\r\n\r\nrule "ru.simplgroupp.rules.credit.calculator.params.1"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $cparams : Map();\r\nthen\r\n    int creditCount=Convertor.toInteger($cparams.get(CreditCalculatorBeanLocal.CREDIT_CLOSED_COUNT));\r\n    double sumMaxUnknown=((Double) $cparams.get(CreditCalculatorBeanLocal.CREDIT_SUM_MAX_UNKNOWN)).doubleValue();\r\n    if (creditCount==0){\r\n        $bResult.setValue(CreditCalculatorBeanLocal.CREDIT_SUM_MAX, CalcUtils.roundFloor(sumMaxUnknown,0));\r\n\r\n    } else {\r\n       double sumMax = ((Double) $cparams.get(CreditCalculatorBeanLocal.CREDIT_SUM_MAX)).doubleValue();\r\n       double sumMaxCalc=((Double) $cparams.get(CreditCalculatorBeanLocal.CREDIT_SUM_ADDITION)).doubleValue()*creditCount+sumMaxUnknown;\r\n       if (sumMaxCalc<=sumMax){\r\n         $bResult.setValue(CreditCalculatorBeanLocal.CREDIT_SUM_MAX, CalcUtils.roundFloor(sumMaxCalc,0));\r\n       } else {\r\n         $bResult.setValue(CreditCalculatorBeanLocal.CREDIT_SUM_MAX, CalcUtils.roundFloor(sumMax,0));\r\n\r\n       }  \r\n    }\r\n   \r\n   $bResult.setValue(CreditCalculatorBeanLocal.CREDIT_SUM_MIN, ((Double) $cparams.get(CreditCalculatorBeanLocal.CREDIT_SUM_MIN)).doubleValue());\r\n   $bResult.setValue(CreditCalculatorBeanLocal.CREDIT_DAYS_MIN,Convertor.toInteger($cparams.get(CreditCalculatorBeanLocal.CREDIT_DAYS_MIN)));\r\n   $bResult.setValue(CreditCalculatorBeanLocal.CREDIT_DAYS_MAX,Convertor.toInteger($cparams.get(CreditCalculatorBeanLocal.CREDIT_DAYS_MAX)));\r\n\r\n    \r\nend\r\n\r\n	1	Рассчет параметров инициализации кредита	kbCreditParams
44	ru.simplgroupp.rules.credit.calculator.bonus	package ru.simplgroupp.rules.credit.calculator.bonus;\r\ndialect "java"\r\n\r\nimport ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.toolkit.common.Convertor;\r\n\r\nrule "ru.simplgroupp.rules.credit.calculator.bonus.1"\r\nwhen \r\n    $bResult  : NameValueRuleResult();\r\n    $cparams  : Map();\r\n    $sumBack  : Double();\r\n    $bonusSum : Double();\r\nthen\r\n    double sumMoney=((Double) $cparams.get(CreditCalculatorBeanLocal.BONUS_RATE)).doubleValue()*$bonusSum;\r\n\r\n    //max sum from sumBack\r\n    double sumMax=((Double) $cparams.get(CreditCalculatorBeanLocal.BONUS_PAY_PERCENT)).doubleValue()*$sumBack;\r\n\r\n    if (sumMax>sumMoney){\r\n        sumMax=sumMoney;\r\n    }\r\n    double payMaxSum = ((Double) $cparams.get(CreditCalculatorBeanLocal.BONUS_PAY_MAX_SUM)).doubleValue();\r\n \r\n    if (sumMax>payMaxSum){\r\n       sumMax=payMaxSum;\r\n    }    \r\n  \r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_BONUS_MONEY_FOR_PAYMENT, CalcUtils.roundFloor(sumMax,0));\r\n    double sumBonus=sumMax/((Double) $cparams.get(CreditCalculatorBeanLocal.BONUS_RATE)).doubleValue();\r\n    $bResult.setValue(CreditCalculatorBeanLocal.SUM_BONUS_FOR_PAYMENT, CalcUtils.roundFloor(sumBonus,0));\r\n    \r\nend	1	Рассчет суммы к возврату бонусами	kbBonusCalc
\.


--
-- TOC entry 2004 (class 0 OID 101267)
-- Dependencies: 169
-- Data for Name: attributes; Type: TABLE DATA; Schema: public; Owner: sa
--

COPY attributes (id, name, value, id_element) FROM stdin;
31	name	peoplepersonal.surname	20
32	label	Фамилия	20
33	placeholder	Фамилия	20
34	name	peoplepersonal.name	24
35	label	Иِмя	24
36	placeholder	Имя	24
37	name	peoplepersonal.midname	28
38	label	Отчество	28
39	placeholder	Отчество	28
\.


--
-- TOC entry 2005 (class 0 OID 101274)
-- Dependencies: 170
-- Data for Name: bizaction; Type: TABLE DATA; Schema: public; Owner: sa
--

COPY bizaction (id, bizactiontype_id, signalref, businessobjectclass, description, assignee, candidategroups, candidateusers, forone, formany, sqlfilter, datafilter, schedule, runprocessdefkey, issystem, retryinterval, errorcode, errormessage, laststart, lastend, processdefkey, ruleenabled, ruleaction, isactive, isrequired, isatomic, plugin, listid, iscsingleton, next_id, txversion) FROM stdin;
8	4	msgProlongPay	ru.simplgroupp.transfer.Prolong	Заплатить проценты		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procProlongCR	\n\n\t\t		1	1	0	\N	\N	0	\N	0
22	4	msgAccept	ru.simplgroupp.transfer.CreditRequest	Изменения внесены		admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procCreditRequestmsgChangeRequest			1	1	0	\N	\N	0	\N	0
23	4	msgReject	ru.simplgroupp.transfer.CreditRequest	Изменения отклонены		admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procCreditRequestmsgChangeRequest			1	1	0	\N	\N	0	\N	0
24	4	msgMore	ru.simplgroupp.transfer.CreditRequest	Далее		verificator	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procConsiderCR			1	1	0	\N	\N	0	\N	0
25	4	msgAccept	ru.simplgroupp.transfer.CreditRequest	Дать кредит		creditmanager	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procConsiderCR			1	1	0	\N	\N	0	\N	0
7	4	msgProlongCancel	ru.simplgroupp.transfer.Prolong	Отменить продление		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procProlongCR	\nimport ru.simplgroupp.rules.BooleanRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport java.util.Map\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.SignalRef\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : BooleanRuleResult();\n    $cparams : Map();\n    $signalRef : SignalRef(processDefKey == "procProlongCR" && name == "msgProlongCancel");\nthen   \n    $bResult.setResultValue(false);\nend\t\t\n\t\t		1	1	0	\N	\N	0	\N	0
33	4	msgRestoreProcess1	ru.simplgroupp.transfer.CreditRequest	Восстановить процесс с начала	\N	admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess1");\r\n    $context : AbstractContext(getCreditRequest() != null);\r\n    $cparams: Map();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $bResult.setResultValue(new Boolean($context.getCreditRequest().getAcceptedCredit() == null&&($context.getCreditRequest().getStatus().getId()==1||$context.getCreditRequest().getStatus().getId()==5)));\r\nend\r\n\r\nrule "[$RULE_NAME_PREFIX$].2"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess1");\r\n    $context : AbstractContext( getCreditRequest() == null );\r\n    $cparams: Map();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $bResult.setResultValue(new Boolean(false));\r\nend\r\n	import ru.simplgroupp.rules.NameValueRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\nimport java.util.Map\nimport java.util.Date\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.rules.AbstractContext\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : NameValueRuleResult();\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess1");\n    $context : AbstractContext();\n    $actionProcessor : ActionProcessorBeanLocal();\nthen   \n    $actionProcessor.restoreProcess($context.getCreditRequest().getId(),'procCR::servicetask13');\nend	1	1	0	\N	\N	0	\N	0
2	4	msgChangeRequest	ru.simplgroupp.transfer.CreditRequest	Изменить данные	\N	callcenter	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgChangeRequest");\r\n    $context : AbstractContext(getCreditRequest() != null);\r\nthen   \r\n\t$bResult.setResultValue( ($context.getCreditRequest().getAcceptedCredit() != null) && (! $context.getCreditRequest().getAcceptedCredit().getIsOver()) );\r\nend\r\n\r\n	\N	1	1	0	\N	\N	0	\N	0
4	4	msgRemove	ru.simplgroupp.transfer.CreditRequest	Удалить	\N	admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRemove");\r\n    $context : AbstractContext(getCreditRequest() != null);\r\nthen   \r\n    $bResult.setResultValue(new Boolean($context.getCreditRequest().getAcceptedCredit() == null&&($context.getCreditRequest().getStatus().getId()==1||$context.getCreditRequest().getStatus().getId()==5)));\r\nend\r\n\r\nrule "[$RULE_NAME_PREFIX$].2"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRemove");\r\n    $context : AbstractContext( getCreditRequest() == null );\r\nthen   \r\n    $bResult.setResultValue(new Boolean(false));\r\nend\r\n	\N	1	1	0	\N	\N	0	\N	0
50	4	msgRefinanceCancel	ru.simplgroupp.transfer.Refinance	Отменить рефинансирование		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procRefinance	\n\t\n\t\t		1	1	0	\N	\N	0	\N	0
51	4	msgRefinancePay	ru.simplgroupp.transfer.Refinance	Заплатить минимальную сумму		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procRefinance	\n\n\t\t		1	1	0	\N	\N	0	\N	0
5	4	msgReturn	ru.simplgroupp.transfer.Credit	Вернуть кредит		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procCR	\nimport ru.simplgroupp.rules.BooleanRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport java.util.Map\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.SignalRef\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : BooleanRuleResult();\n    $cparams : Map();\n    $signalRef : SignalRef(processDefKey == "procCR" && name == "msgReturn");\nthen   \n    $bResult.setResultValue(true);\nend\t\n\t\t		1	1	0	\N	\N	0	\N	0
45	4	msgRestoreProcess2	ru.simplgroupp.transfer.CreditRequest	\N	\N	admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess1");\r\n    $context : AbstractContext(getCreditRequest() != null);\r\n    $cparams: Map();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $bResult.setResultValue(new Boolean($context.getCreditRequest().getAcceptedCredit() == null&&($context.getCreditRequest().getStatus().getId()==1||$context.getCreditRequest().getStatus().getId()==5)));\r\nend\r\n\r\nrule "[$RULE_NAME_PREFIX$].2"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess1");\r\n    $context : AbstractContext( getCreditRequest() == null );\r\n    $cparams: Map();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $bResult.setResultValue(new Boolean(false));\r\nend\r\n	import ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess2");\r\n    $context : AbstractContext();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $actionProcessor.restoreProcess($context.getCreditRequest().getId(),'procCR::taskSignOferta');\r\nend	1	1	0	\N	\N	0	\N	0
19	4	msgEdit	ru.simplgroupp.transfer.Credit	Редактировать	\N	creditmanager,admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N	0	\N	0
12	4	msgAddCreditRequest	ru.simplgroupp.transfer.PeopleMain	Взять кредит	\N	customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.transfer.Credit\r\nimport java.util.Date\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\n// если текущего кредита нет, или он закрыт, и открытой заявки нет\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.PeopleMain" && signalRef == "msgAddCreditRequest");\r\n    $context : AbstractContext( (getCredit() == null || getCredit().getIsOver()) && getCreditRequest() == null);\r\nthen\r\n   \t$bResult.setResultValue(true);\r\nend\r\n\r\n// если текущего кредита нет, или он закрыт, а открытая заявка есть\r\nrule "[$RULE_NAME_PREFIX$].2"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.PeopleMain" && signalRef == "msgAddCreditRequest");\r\n    $context : AbstractContext((getCredit() == null || getCredit().getIsOver()) && getCreditRequest() != null);\r\nthen\r\n   \t$bResult.setResultValue(false);\r\n   \t$bResult.addInfo(1, "У Вас уже есть открытая заявка");\r\nend\r\n\r\n// если текущий кредит\r\nrule "[$RULE_NAME_PREFIX$].3"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.PeopleMain" && signalRef == "msgAddCreditRequest");\r\n    $context : AbstractContext( getCredit() != null && (!getCredit().getIsOver()) );\r\nthen\r\n   \t$bResult.setResultValue(false);\r\n   \t$bResult.addInfo(2, "У Вас уже открыт заём");\r\nend	\N	1	1	0	\N	\N	0	\N	0
14	4	msgMore	ru.simplgroupp.transfer.CreditRequest	Далее		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procNewCR			1	1	0	\N	\N	0	\N	0
3	4	msgEdit	ru.simplgroupp.transfer.CreditRequest	Редактировать	\N	callcenter,verificator,creditmanager,admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgEdit");\r\n    $context : AbstractContext(getCreditRequest() != null);\r\nthen  \r\n    if ($context.getCurrentUser().getRoleNameDescription().contains("admin")) {\r\n      $bResult.setResultValue(new Boolean(true));\r\n    } else if ($context.getCurrentUser().getRoleNameDescription().contains("verificator")) { \r\n      $bResult.setResultValue(new Boolean($context.getCreditRequest().getDateContest() != null&&$context.getCreditRequest().getAcceptedCredit() == null&&($context.getCreditRequest().getStatus().getId()==1||$context.getCreditRequest().getStatus().getId()==5)));\r\n    } else if ($context.getCurrentUser().getRoleNameDescription().contains("callcenter")) {\r\n      $bResult.setResultValue(new Boolean($context.getCreditRequest().getDateContest() != null&&$context.getCreditRequest().getAcceptedCredit() == null&&$context.getCreditRequest().getStatus().getId()==5));\r\n    } else {\r\n      $bResult.setResultValue(new Boolean(false)); \r\n    }\r\nend\r\n\r\nrule "[$RULE_NAME_PREFIX$].2"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgEdit");\r\n    $context : AbstractContext( getCreditRequest() == null );\r\nthen   \r\n    $bResult.setResultValue(new Boolean(false));\r\nend\r\n	\N	1	1	0	\N	\N	0	\N	0
31	4	msgRestoreProcess	ru.simplgroupp.transfer.CreditRequest	Восстановить процесс	\N	admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess");\r\n    $context : AbstractContext(getCreditRequest() != null);\r\n    $cparams: Map(); \r\n    $actionProcessor : ActionProcessorBeanLocal();\r\n\r\nthen   \r\n    $bResult.setResultValue(new Boolean($context.getCreditRequest().getAcceptedCredit() == null&&($context.getCreditRequest().getStatus().getId()==1||$context.getCreditRequest().getStatus().getId()==5)));\r\nend\r\n\r\nrule "[$RULE_NAME_PREFIX$].2"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess");\r\n    $context : AbstractContext( getCreditRequest() == null );\r\n    $cparams: Map();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\n\r\nthen   \r\n    $bResult.setResultValue(new Boolean(false));\r\nend\r\n	import ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRestoreProcess");\r\n    $context : AbstractContext();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $actionProcessor.restoreProcess($context.getCreditRequest().getId(),'procCR::taskSignOferta');\r\nend	1	1	0	\N	\N	0	\N	0
26	4	msgReject	ru.simplgroupp.transfer.CreditRequest	Отказать		creditmanager	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procConsiderCR			1	1	0	\N	\N	0	\N	0
16	4	msgClientReject	ru.simplgroupp.transfer.CreditRequest	Отказ клиента		customer,creditmanager,verificator,admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgClientReject");\r\n    $context : AbstractContext();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $actionProcessor.refuseClient($context.getCreditRequest().getId(), $context.getCurrentUser().getId());\r\nend	1	1	0	\N	\N	0	\N	0
17	4	msgAccept	ru.simplgroupp.transfer.CreditRequest	Согласен		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procCR			1	1	0	\N	\N	0	\N	0
18	4	msgReject	ru.simplgroupp.transfer.CreditRequest	Не согласен		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procCR			1	1	0	\N	\N	0	\N	0
44	4	msgToCourt	ru.simplgroupp.transfer.Credit	Передать в суд	\N	creditmanager,admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport java.util.Map\nimport java.util.Date\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.util.DatesUtils\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.rules.AbstractContext\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : BooleanRuleResult();\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Credit" && signalRef == "msgToCourt");\n    $context : AbstractContext( getCredit() != null && getCredit().getCreditStatus().getCodeInteger() != 9 );\n    $cparams: Map();\n    $actionProcessor : ActionProcessorBeanLocal();\n\nthen   \n    $bResult.setResultValue(true);\nend\n	import ru.simplgroupp.rules.NameValueRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\nimport java.util.Map\nimport java.util.Date\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.rules.AbstractContext\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : NameValueRuleResult();\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Credit" && signalRef == "msgToCourt");\n    $context : AbstractContext();\n    $actionProcessor : ActionProcessorBeanLocal();\nthen   \n    $actionProcessor.toCourt($context.getCredit().getId() );\nend	1	1	1	\N	\N	0	\N	0
54	5	msgRefinanceRun	ru.simplgroupp.transfer.Refinance	Рефинансировать	\N	customer	\N	1	0	\N	\N	\N	procRefinance	0	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.SignalRef\r\nimport java.util.Date\r\nimport ru.simplgroupp.transfer.PeopleMain\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].3"\r\nwhen \r\n    $bResult : BooleanRuleResult(getResultValue() == null);\r\n    $cparams : Map();\r\n    $context : AbstractContext();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $bResult.setResultValue($context.getCredit() != null && $context.getCredit().getLastRefinance() != null && $context.getCredit().getLastRefinance().getIsActive() == 2 );\r\nend\t\r\n	\N	1	0	0	\N	\N	0	\N	0
53	4	msgRefinance	ru.simplgroupp.transfer.Credit	Рефинансировать	\N	customer	\N	1	0	\N	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.SignalRef\r\nimport java.util.Date\r\nimport ru.simplgroupp.transfer.PeopleMain\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult(getResultValue() == null);\r\n    $cparams : Map();\r\n    $context : AbstractContext();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $bResult.setResultValue($context.getCredit() != null && $context.getCredit().getLastRefinance() == null && $context.getCredit().getDraftProlong() == null );\r\nend\r\n	\N	1	0	0	\N	\N	0	\N	0
47	4	msgBonus	ru.simplgroupp.transfer.PeopleMain	Выдать бонус	\N	admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport java.util.Map\r\nimport ru.simplgroupp.rules.AbstractContext\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\n\r\nrule "[$RULE_NAME_PREFIX$].1" \r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $context : AbstractContext( getClient() != null );\r\n\t$actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.PeopleMain" && signalRef == "msgBonus");\r\n    $cparams: Map(); \r\nthen   \r\n\t\t$bResult.setResultValue(true);\r\nend\r\n	import ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.PeopleBonus\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $context : AbstractContext( getClient() != null );\r\n\t$actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.PeopleMain" && signalRef == "msgBonus");\r\n    $cparams: Map();\r\n\t$actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    if($cparams.get("invitedByPeopleId") != null)\r\n\t\t$actionProcessor.addBonus((Integer)$cparams.get("invitedByPeopleId"), $context.getClient().getId(),null, new Integer(ru.simplgroupp.transfer.PeopleBonus.BONUS_CODE_INVITE), new Integer(ru.simplgroupp.transfer.PeopleBonus.OPERATION_ADDED));\r\nend\r\n	1	1	0	\N	\N	0	\N	0
15	4	msgBadDebt	ru.simplgroupp.transfer.Credit	Списать как безнадёжный		creditmanager,admin	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procCR	\nimport ru.simplgroupp.rules.BooleanRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport java.util.Map\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.SignalRef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.transfer.Credit\nimport java.util.Date\nimport ru.simplgroupp.rules.AbstractContext\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : BooleanRuleResult();\n    $cparams : Map();\n    $signalRef : SignalRef(processDefKey == "procCR" && name == "msgBadDebt");\n    $context : AbstractContext();\nthen   \n    $bResult.setResultValue( $context.getCredit().isOverdue($context.getCurrentDate()));\nend\t\t\n\t\t		1	1	0	\N	\N	0	\N	0
6	4	msgProlong	ru.simplgroupp.transfer.Credit	Продлить кредит		customer	\N	1	0	\N	\N	\N	\N	1	\N	\N	\N	\N	\N	procCR	\nimport ru.simplgroupp.rules.BooleanRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport java.util.Map\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.SignalRef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.transfer.Credit\nimport java.util.Date\nimport ru.simplgroupp.rules.AbstractContext\nimport ru.simplgroupp.util.DatesUtils;\nimport ru.simplgroupp.toolkit.common.Convertor;\n\n\n// если текущий кредит еще не начался (сегодня выходной, начнется в понедельник)\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : BooleanRuleResult();\n    $cparams : Map();\n    $signalRef : SignalRef(processDefKey == "procCR" && name == "msgProlong");\n    $context : AbstractContext();\nthen\n\tif ( $context.getCreditRequest().getAcceptedCredit() != null && $context.getCreditRequest().getAcceptedCredit().getCreditDataBeg().after( $context.getCurrentDate() )) {\t   \n    \t  $bResult.setResultValue(false);\n    \t  $bResult.addInfo(1,"Ваш текущий заём ещё не начался");\n   \t} else {\n   \t\tint daysCreditMax = Convertor.toInteger($cparams.get("credit.days.sum.max"));\n   \t\tint numProlongsMax = Convertor.toInteger($cparams.get("credit.prolongs.max"));\n        //допускается дней просрочки  \n        int daysOverdueMax = Convertor.toInteger($cparams.get("credit.days.max.lgot"));\n \n   \t\tint daysCredit = DatesUtils.daysDiff($context.getCredit().getCreditDataEnd(), $context.getCredit().getCreditDataBeg());\n   \t\tint numProlongs = ($context.getCredit().getCreditLong()==null)?0:$context.getCredit().getCreditLong().intValue();\n        //есть дней просрочки\n   \t\tint daysCreditOverdue = DatesUtils.daysDiff($context.getCurrentDate(),$context.getCredit().getCreditDataEnd());\n\n   \t\tif (daysCredit >= daysCreditMax) {\n    \t\t  $bResult.setResultValue(false);\n    \t\t  $bResult.addInfo(2,"Вы не можете превысить максимальный срок займа " + String.valueOf(daysCreditMax) + " дней");   \t\t\n   \t\t}\n   \t\t\n   \t\tif (numProlongs >= numProlongsMax) {\n    \t\t  $bResult.setResultValue(false);\n    \t\t  $bResult.addInfo(3,"Заём не может быть продлён более " + String.valueOf(numProlongsMax) + " раз" );   \t\t   \t\t\n   \t\t}\n\n        if (daysCreditOverdue > daysOverdueMax) {\n    \t\t  $bResult.setResultValue(false);\n    \t\t  $bResult.addInfo(4,"Вы вышли из льготного периода, пролонгация не разрешена" );   \t\t   \t\t\n   \t\t}\n   \t}\nend\n\nrule "[$RULE_NAME_PREFIX$].2"\n\nwhen \n  $bResult : BooleanRuleResult();\n  $cparams : Map();\n  $signalRef : SignalRef(processDefKey == "procCR" && name == "msgProlong");\n  $context : AbstractContext(getCreditRequest() == null || getCreditRequest().getAcceptedCredit() == null || getCredit() == null);\n\nthen\n\t$bResult.setResultValue(false);\nend\n\n\t\t		1	1	0	\N	\N	0	\N	0
40	4	msgCancelPaymentContact	ru.simplgroupp.transfer.Payment	Отменить платёж в Контакт	\N	admin	\N	1	1	(c.accountId.accountTypeId.codeinteger = 3) and (c.isPaid = false) and (c.paymenttypeId.codeinteger = 1)  and (c.status=3)	\N	0 25 * * * * *	\N	1	1800000	\N	\N	2015-05-11 19:25:01.293+07	2015-05-11 19:25:01.591+07	\N	import java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.util.DatesUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\nimport ru.simplgroupp.rules.BooleanRuleResult\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Payment" && signalRef == "msgCancelPaymentContact");\r\n    $context : AbstractContext( isAutoRun() == false && getPayment() != null );\r\n    $cparams: Map();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\n\r\nthen   \r\n    $bResult.setResultValue(new Boolean( true ));\r\nend\r\n\r\nrule "[$RULE_NAME_PREFIX$].2"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Payment" && signalRef == "msgCancelPaymentContact");\r\n    $context : AbstractContext(isAutoRun() == true && getPayment() != null );\r\n    $cparams: Map();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\n\r\nthen   \r\n    $bResult.setResultValue(new Boolean( DatesUtils.daysDiff($context.getCurrentDate(), $context.getPayment().getCreateDate()) >=5 ));\r\nend\r\n	import ru.simplgroupp.rules.NameValueRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\nimport java.util.Map\nimport java.util.Date\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.rules.AbstractContext\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : NameValueRuleResult();\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Payment" && signalRef == "msgCancelPaymentContact");\n    $context : AbstractContext();\n    $actionProcessor : ActionProcessorBeanLocal();\nthen   \n    $actionProcessor.cancelPaymentContact($context.getPayment().getId() );\nend	1	1	0	\N	\N	3	\N	338
13	4	msgRecalc	ru.simplgroupp.transfer.Credit	Пересчитать проценты	\N	admin	\N	0	1	(c.issameorg = true) and (c.isover = false) and (c.creditdataendfact is null) and (c.creditdataend < :currentDate) and (c.isactive=1) and (c.creditStatusId.codeinteger <> 9)	\N	0 0 2 * * * *	\N	1	1800000	\N	\N	2015-05-11 08:41:23.901+07	2015-05-11 08:41:24.151+07	\N		import ru.simplgroupp.rules.NameValueRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\nimport java.util.Map\nimport java.util.Date\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.rules.AbstractContext\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : NameValueRuleResult();\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Credit" && signalRef == "msgRecalc");\n    $context : AbstractContext();\n    $actionProcessor : ActionProcessorBeanLocal();\nthen   \n    $actionProcessor.recalcOverdue($context.getCredit().getId(), $context.getCurrentDate() );\nend	1	1	0	\N	\N	3	\N	58
29	3	msgOverdueSMS	ru.simplgroupp.transfer.Credit	Послать смс по просрочке	\N	admin	\N	0	1	(c.issameorg = true) and (c.isover = false) and (c.creditdataendfact is null) and (c.creditStatusId.codeinteger = 1) and (c.isactive=1)	\N	0 0 4 * * * *		1	1800000	\N	\N	2015-05-11 08:41:22.337+07	2015-05-11 08:41:22.681+07	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.transfer.Credit\r\nimport java.util.Date\r\nimport ru.simplgroupp.rules.AbstractContext\r\nimport ru.simplgroupp.util.DatesUtils\r\nimport ru.simplgroupp.toolkit.common.DateRange\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Credit" && signalRef == "msgOverdueSMS");\r\n    $context : AbstractContext();\r\n    $cparams: Map();\r\nthen\r\n  int nCur = DatesUtils.daysDiffAny($context.getCurrentDate(), $context.getCredit().getCreditDataBeg() );\r\n  $bResult.setResultValue(nCur ==  ($context.getCredit().getCreditDays() - 1));\r\nend\r\n\r\n		1	1	0	\N	\N	3	\N	56
27	4	msgToCollector	ru.simplgroupp.transfer.Credit	Передать коллектору	\N	admin	\N	0	1	(c.issameorg = true) and (c.isover = false) and (c.creditdataendfact is null) and (c.creditStatusId.codeinteger = 8) and (c.isactive=1)	\N	0 0 3 * * * *	\N	1	1800000	\N	\N	2015-05-11 08:41:23.62+07	2015-05-11 08:41:23.886+07	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.transfer.Credit\r\nimport java.util.Date\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Credit" && signalRef == "msgToCollector");\r\n    $context : AbstractContext();\r\n    $cparams: Map();\r\nthen\r\n\tObject[] scaleDays = CalcUtils.intRangeScale(\r\n\t  0, $context.getCredit().getCreditDays(),\r\n\t  $context.getCredit().getCreditDays(), ((Integer) $cparams.get("credit.days.max.lgot")).intValue()+$context.getCredit().getCreditDays() ,\r\n\t  ((Integer) $cparams.get("credit.days.min.common")).intValue() +$context.getCredit().getCreditDays()-1, \r\n\t\t\t((Integer) $cparams.get("credit.days.max.common")).intValue()+$context.getCredit().getCreditDays(),\r\n\t  ((Integer) $cparams.get("credit.days.min.overdue")).intValue() +$context.getCredit().getCreditDays()-1, \t\t\t\t\r\n\t\t((Integer) $cparams.get("credit.days.max.overdue")).intValue()+$context.getCredit().getCreditDays()\r\n\t);\r\n\tObject[] scaleDates = CalcUtils.daysToDatesScale($context.getCredit().getCreditDataBeg(), scaleDays);\t\r\n\t int calcInterval = CalcUtils.getScalePos(scaleDates, $context.getCurrentDate());\r\n\t \r\n   \t$bResult.setResultValue(calcInterval >= 2);\r\nend\r\n\r\n	import ru.simplgroupp.rules.NameValueRuleResult\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\nimport java.util.Map\nimport java.util.Date\nimport ru.simplgroupp.util.CalcUtils\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\nimport ru.simplgroupp.transfer.CreditRequest\nimport ru.simplgroupp.rules.AbstractContext\n\nrule "[$RULE_NAME_PREFIX$].1"\nwhen \n    $bResult : NameValueRuleResult();\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.Credit" && signalRef == "msgToCollector");\n    $context : AbstractContext();\n    $actionProcessor : ActionProcessorBeanLocal();\nthen   \n    $actionProcessor.toCollector($context.getCredit().getId() );\nend	1	1	0	\N	\N	3	\N	48
52	2	msgRefinanceEmail	ru.simplgroupp.transfer.Credit	Послать письмо о рефинансировании	\N	admin,creditmanager	\N	1	1	(c.issameorg = true) and (c.isover = false) and (c.creditdataendfact is null) and ((c.creditStatusId.codeinteger = 8) or (c.creditStatusId.codeinteger = 10)) and (c.isactive=1)	\N	0 0 5 * * * *	\N	1	1800000	\N	\N	2015-05-11 08:41:24.198+07	2015-05-11 08:41:24.276+07	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.DatesUtils\r\nimport ru.simplgroupp.workflow.SignalRef\r\nimport java.util.Date\r\nimport ru.simplgroupp.transfer.PeopleMain\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $cparams : Map();\r\n    $context : AbstractContext($context.getCredit() != null && $context.getCredit().getIsActive() == 1 && ! $context.getCredit().getIsOver() && $context.getCredit().getCreditDataEndFact() == null );\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n    $bResult.setResultValue( DatesUtils.daysDiff($context.getCredit().getCreditDataEnd(), $context.getCurrentDate()) == 14 );\r\nend\r\n	\N	1	1	0	\N	\N	0	\N	60
30	4	msgRemoveIncompleted	ru.simplgroupp.transfer.CreditRequest	Удалить недозаполненные	\N	admin	\N	0	1	(c.statusId.id = 5)	\N	0 0 1 * * * *	\N	1	1800000	\N	\N	2015-05-11 08:41:24.573+07	2015-05-11 08:41:24.808+07	\N	import ru.simplgroupp.rules.BooleanRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport java.util.Map\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.transfer.Credit\r\nimport java.util.Date\r\nimport ru.simplgroupp.rules.AbstractContext\r\nimport ru.simplgroupp.util.DatesUtils\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : BooleanRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRemoveIncompleted");\r\n    $context : AbstractContext();\r\n    $cparams: Map();\r\nthen\t \r\n    $bResult.setResultValue(DatesUtils.daysDiffAny($context.getCurrentDate(), $context.getCreditRequest().getDateContest() ) >= 30 );\r\nend\r\n\r\n	import ru.simplgroupp.rules.NameValueRuleResult\r\nimport ru.simplgroupp.interfaces.CreditCalculatorBeanLocal\r\nimport ru.simplgroupp.interfaces.ActionProcessorBeanLocal\r\nimport java.util.Map\r\nimport java.util.Date\r\nimport ru.simplgroupp.util.CalcUtils\r\nimport ru.simplgroupp.workflow.WorkflowObjectActionDef\r\nimport ru.simplgroupp.transfer.CreditRequest\r\nimport ru.simplgroupp.rules.AbstractContext\r\n\r\nrule "[$RULE_NAME_PREFIX$].1"\r\nwhen \r\n    $bResult : NameValueRuleResult();\r\n    $actionDef : WorkflowObjectActionDef(businessObjectClass == "ru.simplgroupp.transfer.CreditRequest" && signalRef == "msgRemoveIncompleted");\r\n    $context : AbstractContext();\r\n    $actionProcessor : ActionProcessorBeanLocal();\r\nthen   \r\n  $actionProcessor.removeCreditRequest($context.getCreditRequest().getId() );\r\n\r\nend	1	1	0	\N	\N	3	\N	48
28	2	msgOverdueEmail	ru.simplgroupp.transfer.Credit	Послать email по просрочке	\N	admin	\N	0	1	(c.issameorg = true) and (c.isover = false) and (c.creditdataendfact is null) and ((c.creditStatusId.codeinteger = 8) or (c.creditStatusId.codeinteger = 10)) and (c.isactive=1)	\N	0 0 4 * * * *		1	1800000	\N	\N	2015-05-11 08:41:24.307+07	2015-05-11 08:41:24.526+07	\N			1	1	0	\N	\N	3	\N	53
\.


--
-- TOC entry 2014 (class 0 OID 402233)
-- Dependencies: 186
-- Data for Name: bizactionevent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY bizactionevent (id, bizaction_id, eventcode) FROM stdin;
1	47	8
\.


--
-- TOC entry 2006 (class 0 OID 101283)
-- Dependencies: 172
-- Data for Name: bizactiontype; Type: TABLE DATA; Schema: public; Owner: sa
--

COPY bizactiontype (id, name2) FROM stdin;
1	Выполнить правило
2	Отправить письмо
3	Отправить СМС
4	Выполнить в приложении
5	Запустить процесс
6	Запустить стартовую форму
\.


--
-- TOC entry 2007 (class 0 OID 101286)
-- Dependencies: 173
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: sa
--

COPY databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) FROM stdin;
115	helen	1.0.0/2015-05-01/changelog.xml	2015-05-01 12:45:35.637	97	EXECUTED	7:e701d79547ffaf020e76ee275b6b0930	sql		\N	3.2.2
120	helen	1.0.0/2015-05-07/changelog.xml	2015-05-07 14:02:58.021	104	EXECUTED	7:36a9b62e2860428d86e8cdbca9fdc104	sql		\N	3.2.2
121	Julia	1.0.0/2015-05-08/changelog.xml	2015-05-08 16:40:48.003	105	EXECUTED	7:35877970ec1c2889f70df5cdf809c533	sql		\N	3.2.2
123	helen	1.0.0/2015-05-08/changelog.xml	2015-05-09 08:36:07.541	107	EXECUTED	7:a1e8cb266e662799771f3cc5fb91a65e	update		\N	3.2.2
012	irk333	../design/1.0.0/changelog-master.xml	2014-10-07 10:28:34.414221	1	EXECUTED	7:96a18432ffbb68360ff25f04bbca0c6c	sql (x9)	test	\N	3.2.0
013	irk333	../design/1.0.0/changelog-master.xml	2014-10-07 10:41:12.836181	2	EXECUTED	7:e70c42286275cb418cdeebe53bfea347	sql (x2)	test	\N	3.2.0
014	irk333	../design/1.0.0/changelog-master.xml	2014-10-07 11:50:40.079076	3	EXECUTED	7:d5ad5654e4b47e150c84e95ffc907d15	insert	test	\N	3.2.0
015	irk333	../design/1.0.0/2014-10-07/changelog-master.xml	2014-10-07 13:16:54.603283	4	EXECUTED	7:de55a358b7f32795f4c2d1ebf59cace5	insert (x6)	test	\N	3.2.0
016	irk333	../design/1.0.0/2014-10-07/changelog-master.xml	2014-10-07 15:13:43.179489	5	EXECUTED	7:691e3b9e67dca2e2e51c4b8c3ac98a50	sql (x2)	test	\N	3.2.0
017	helen19711	../design/1.0.0/2014-10-11/changelog-master.xml	2014-10-14 10:29:33.982075	6	EXECUTED	7:b59225cb0481f96debf5ed9aa141171e	update		\N	3.2.2
018	helen19711	../design/1.0.0/2014-10-11/changelog-master.xml	2014-10-14 10:29:34.011539	7	EXECUTED	7:06bd64f4975cd8a90ca7e0180f5d2d32	sql (x10)		\N	3.2.2
019	helen19711	../design/1.0.0/2014-10-13/changelog-master.xml	2014-10-14 10:30:30.173757	8	EXECUTED	7:b59225cb0481f96debf5ed9aa141171e	update		\N	3.2.2
020	helen19711	../design/1.0.0/2014-10-13/changelog-master.xml	2014-10-14 10:32:59.613946	9	EXECUTED	7:c797d785b0a5cd8470b57dc12629dddf	insert		\N	3.2.2
021	aro	../design/1.0.0/2014-10-13/changelog-master.xml	2014-10-14 10:35:34.530194	10	EXECUTED	7:c2b0bb2a40bac8ef4bc78aad7cfcd05d	sql (x2)	added test hash parameter for Contact Callback Servlet	\N	3.2.2
022	irk333	../design/1.0.0/2014-10-13/changelog-master.xml	2014-10-14 10:35:34.816823	11	EXECUTED	7:e15526d34aff9b3a7431146d33a78c99	sql (x2)		\N	3.2.2
023	irk333	../design/1.0.0/2014-10-13/changelog-master.xml	2014-10-14 10:35:34.849546	12	EXECUTED	7:ee7d89d87b403e02ffafa0f0707c2aa7	update (x5), insert (x2)		\N	3.2.2
024	irk333	../design/1.0.0/2014-10-14/changelog.xml	2014-10-14 13:58:48.354607	13	EXECUTED	7:3db75e8f3cbc9a6e86b9268b2e6cdc50	sql (x3), update		\N	3.2.2
025	irk333	../design/1.0.0/2014-10-14/changelog.xml	2014-10-14 16:55:22.161714	14	EXECUTED	7:aa3ad80bd0d6f692816901e05b3c16a3	update		\N	3.2.2
017	helen19711	1.0.0/2014-10-11/changelog-master.xml	2014-11-03 15:22:16.88	15	EXECUTED	7:b59225cb0481f96debf5ed9aa141171e	update		\N	3.2.2
018	helen19711	1.0.0/2014-10-11/changelog-master.xml	2014-11-03 15:22:16.981	16	EXECUTED	7:06bd64f4975cd8a90ca7e0180f5d2d32	sql (x10)		\N	3.2.2
019	helen19711	1.0.0/2014-10-13/changelog-master.xml	2014-11-03 15:22:16.992	17	EXECUTED	7:b59225cb0481f96debf5ed9aa141171e	update		\N	3.2.2
044	helen19711	../design/1.0.0/2014-11-13/changelog.xml	2014-11-14 11:55:04.30433	34	EXECUTED	7:faf84a5e8e1a339844e34fbc1290b8c1	update	 	 	3.2.2
045	irk333	../design/1.0.0/2014-11-23/changelog.xml	2014-11-23 19:00:21.847315	35	EXECUTED	7:1ddb3e487ece3f83283f9f932757c2d3	sql	 	 	3.2.2
046	helen19711	../design/1.0.0/2014-11-27/changelog.xml	2014-11-27 18:23:58.787926	36	EXECUTED	7:fe369c10b4f9c5909d988f0c4bfd7f9b	sql	 	 	3.2.2
047	helen19711	../design/1.0.0/2014-11-28/changelog.xml	2014-11-30 14:09:25.135546	37	EXECUTED	7:0f4a56021ab780c57ee993a78d7c90d1	sql	 	 	3.2.2
073	irk333	../design/1.0.0/2014-12-02/changelog.xml	2014-12-03 08:32:14.675446	38	EXECUTED	7:6e50ab64ebc8e4bbec9b8711cd493e26	insert	 	 	3.2.2
074	irk333	../design/1.0.0/2014-12-04/changelog.xml	2014-12-04 16:51:23.655279	39	EXECUTED	7:7fed44ea8e4b4515ec5848e8aacd9802	update	 	 	3.2.2
075	irk333	../design/1.0.0/2014-12-05/changelog.xml	2014-12-05 21:07:30.906585	40	EXECUTED	7:f868991a5ae7c415b097a4f1b3289f83	update	 	 	3.2.2
076	helen19711	../design/1.0.0/2014-12-07/changelog.xml	2014-12-08 14:29:13.138296	41	EXECUTED	7:2542cbfc59bc506a2758430b1e383edb	update (x2)			3.2.2
077	helen19711	../design/1.0.0/2014-12-08/changelog-master.xml	2014-12-09 06:29:14.188581	42	EXECUTED	7:c72b86893ae638640351e8a9fe17fe5e	sql (x4)	 	 	3.2.2
078	irk333	../design/1.0.0/2014-12-12/changelog.xml	2014-12-12 10:41:47.894431	43	EXECUTED	7:56ac1ec31828d30491de35e52333b058	insert (x3)	 	 	3.2.2
079	irk333	../design/1.0.0/2014-12-21/changelog.xml	2014-12-22 14:02:16.957053	44	EXECUTED	7:4eff6c1172c8886cd45936bca2a4da37	insert, update			3.2.2
081	helen	../design/1.0.0/2014-12-23/changelog.xml	2014-12-23 16:11:37.908137	45	EXECUTED	7:95ef1c062eeda9cb2ff12d9ace73c1b5	sql	 	 	3.2.2
082	irk333	../design/1.0.0/2014-12-24/changelog.xml	2014-12-27 17:49:35.975878	46	EXECUTED	7:070ae8af4f511e8829f1ca95662867f2	insert (x3), sql	 	 	3.2.2
083	helen	../design/1.0.0/2014-12-28/changelog.xml	2014-12-28 11:33:34.589724	47	EXECUTED	7:5e16fef1351c471c3ef94c24497dfc96	sql	 	 	3.2.2
084	irk333	1.0.0/2014-12-28/changelog1.xml	2014-12-28 17:22:06.002645	56	EXECUTED	7:21a0c7c404692418b19814a2fd56d153	insert, update	 	 	3.2.2
085	irk333	1.0.0/2014-12-28/changelog1.xml	2014-12-28 17:22:06.25063	57	EXECUTED	7:94533c98fb234dacdce2c1c8ec43fadd	sql	 	 	3.2.2
086	helen	../design/1.0.0/2014-12-29/changelog.xml	2014-12-30 04:59:05.232237	58	EXECUTED	7:6391933476c2664fe2951527a906a1d7	update (x2), sql	 	 	3.2.2
087	irk333	1.0.0/2015-01-06/changelog.xml	2015-01-07 18:53:30.27678	60	EXECUTED	7:b9db5c6a6ff467ddd552f19efe4f1c0e	update, sql	 	 	3.2.2
088	irk333	1.0.0/2015-01-07/changelog.xml	2015-01-09 19:03:49.942886	61	EXECUTED	7:046e73cfb8ad53eb340a78014ff034f0	insert	 	 	3.2.2
089	irk333	../design/1.0.0/2015-01-12/changelog.xml	2015-01-12 10:11:17.232694	67	EXECUTED	7:d2f1996a88d7ea0b539b1eb684fd634c	sql	 	 	3.2.2
090	irk333	../design/1.0.0/2015-01-13/changelog.xml	2015-01-15 05:03:34.556532	68	EXECUTED	7:f3600b6d4a2726aa2fbcb1e4db02c46e	update	 	 	3.2.2
091	irk333	../design/1.0.0/2015-01-26/changelog.xml	2015-01-27 09:28:46.186135	69	EXECUTED	7:9c43c03ba61f3cd82863b2a4da9d11ae	insert	 	 	3.2.2
092	irk333	../design/1.0.0/2015-02-08/changelog.xml	2015-02-09 23:53:24.342404	70	EXECUTED	7:8741b9a120d984355d359b7ef7889a1c	sql	 	 	3.2.2
093	irk333	../design/1.0.0/2015-02-17/changelog.xml	2015-02-17 21:07:07.97274	71	EXECUTED	7:79b5eb56509cb08cc20531d15dcd92cf	update			3.2.2
094	irk333	../design/1.0.0/2015-02-17/changelog.xml	2015-02-17 21:07:07.97274	72	EXECUTED	7:79b5eb56509cb08cc20531d15dcd92cf	update			3.2.2
095	irk333	../design/1.0.0/2015-03-01/changelog.xml	2015-03-01 12:05:51.104564	73	EXECUTED	7:1437b9f619e1753946e6b226a2fa4738	insert	 	 	3.2.2
097	helen	../design/1.0.0/2015-03-09/changelog.xml	2015-03-10 06:01:18.972438	74	EXECUTED	7:abe309826978bdeaa6ce48583540c703	sql	 	 	3.2.2
099	irk333	../design/1.0.0/2015-03-31/changelog.xml	2015-03-31 18:21:10.758813	79	EXECUTED	7:661e680c7958c477a27a8bbbb9578021	sql	 	 	3.2.2
100	helen	1.0.0/2015-04-04/changelog.xml	2015-04-04 09:46:49.274	80	EXECUTED	7:3a53f1aa8fc217cb2bba7b1032a1bca2	sql (x2)		\N	3.2.2
113	irk333	1.0.0/2015-04-29/changelog.xml	2015-05-05 20:18:59.55	98	EXECUTED	7:c2df22bcb0341cacc9c30cecd835c984	update		\N	3.2.2
116	irk333	1.0.0/2015-05-05/changelog.xml	2015-05-05 20:18:59.893	99	EXECUTED	7:c2df22bcb0341cacc9c30cecd835c984	update		\N	3.2.2
101	helen19711	1.0.0/2015-04-06/changelog.xml	2015-04-06 21:09:22.232	81	EXECUTED	7:fc82eb0f0236252d73843a2e0b0ec822	insert		\N	3.2.2
102	helen19711	1.0.0/2015-04-08/changelog.xml	2015-04-08 08:43:56.475	82	EXECUTED	7:df3d81f2d6da8364cd1aaa023149e900	update		\N	3.2.2
102	irk333	1.0.0/2015-04-08/changelog.xml	2015-04-08 12:01:48.636	83	EXECUTED	7:a5a882586d45387e699d302941dd5c4b	sql (x2)		\N	3.2.2
103	julia	1.0.0/2015-04-08/changelog.xml	2015-04-08 18:21:27.145	84	EXECUTED	7:edd44b0d77f97ccc4d71c0582fdc4e21	sql, update		\N	3.2.2
117	helen	1.0.0/2015-05-05/changelog.xml	2015-05-05 20:19:01.238	100	EXECUTED	7:e27bd3436263f54965d0cda3cdc8f99b	sql		\N	3.2.2
104	helen	1.0.0/2015-04-09/changelog.xml	2015-04-09 15:11:53.099	85	EXECUTED	7:e4fcddd6fefe1256ed0a99cfaf0b125d	sql		\N	3.2.2
105	irk333	1.0.0/2015-04-10/changelog.xml	2015-04-10 22:21:36.148	86	EXECUTED	7:2a1107085ef4b891b99626f5eda3d600	sql		\N	3.2.2
117	irk333	1.0.0/2015-05-05/changelog.xml	2015-05-05 21:03:10.644	101	EXECUTED	7:7a866fe8a54a9d1bc4d8dc3b9dfda858	sql		\N	3.2.2
106	helen	1.0.0/2015-04-11/changelog.xml	2015-04-11 19:50:16.227	87	EXECUTED	7:5039a9ee94c03fc771b47ace1bbd3210	update		\N	3.2.2
118	julia	1.0.0/2015-05-06/changelog.xml	2015-05-07 12:47:35.365	102	EXECUTED	7:a8682a8be9dfab4aafa157dca671ebd1	update, sql		\N	3.2.2
119	irk333	1.0.0/2015-05-06/changelog.xml	2015-05-07 12:47:36.053	103	EXECUTED	7:a159d25148cbd9cf7da06563e06a393b	update, sql		\N	3.2.2
107	julia	1.0.0/2015-04-16/changelog.xml	2015-04-17 16:05:36.846	88	EXECUTED	7:4425b7c4a152124ebc7c0d549803ba27	sql, update		\N	3.2.2
122	helen	1.0.0/2015-05-08/changelog.xml	2015-05-08 17:13:32.254	106	EXECUTED	7:01f33632b4bc85363a9a25c9bd262b36	sql		\N	3.2.2
108	irk333	1.0.0/2015-04-18/changelog.xml	2015-04-22 21:05:24.464	89	EXECUTED	7:6a30881eff331058c59468d0cdf6c79b	sql		\N	3.2.2
109	irk333	1.0.0/2015-04-18/changelog.xml	2015-04-22 21:05:24.482	90	EXECUTED	7:17537dcd6a2b139e2cdb2e2c93b97df0	update (x2)		\N	3.2.2
110	irk333	1.0.0/2015-04-23/changelog.xml	2015-04-24 10:48:59.268	91	EXECUTED	7:45b6ee3d3b5bd908899f7a969b532f81	update		\N	3.2.2
110	irk333	1.0.0/2015-04-24/changelog.xml	2015-04-24 21:42:45.994	92	EXECUTED	7:47eebd8380dff5792bb02e993dd98fc0	sql		\N	3.2.2
111	julia	1.0.0/2015-04-24/changelog.xml	2015-04-24 22:22:50.718	93	EXECUTED	7:8925fb3f7540c6d59a855a3ae735e1d6	update		\N	3.2.2
112	julia	1.0.0/2015-04-25/changelog.xml	2015-04-25 16:45:29.055	94	EXECUTED	7:e6ba1295b946a1cb704b84133540cf6a	update		\N	3.2.2
113	helen	1.0.0/2015-04-29/changelog.xml	2015-04-29 14:47:58.808	95	EXECUTED	7:ae9108087f525fc59062ac1c14e5a2cd	sql (x2)		\N	3.2.2
124	helen	1.0.0/2015-05-08/changelog.xml	2015-05-09 08:59:08.613	108	EXECUTED	7:2171c98f630d10fb9468defd6aff1087	sql		\N	3.2.2
114	helen	1.0.0/2015-04-29/changelog.xml	2015-05-01 10:36:14.093	96	EXECUTED	7:3c6e142a27ee4267d65ccd8eb15ed665	update		\N	3.2.2
\.


--
-- TOC entry 2008 (class 0 OID 101292)
-- Dependencies: 174
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: sa
--

COPY databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- TOC entry 2009 (class 0 OID 101297)
-- Dependencies: 176
-- Data for Name: elements; Type: TABLE DATA; Schema: public; Owner: sa
--

COPY elements (id, id_parent, type, value, id_etap, nn) FROM stdin;
20	19	view	text	\N	1
24	19	view	text	\N	2
28	19	view	text	\N	3
2	\N	header	Основная информация	1	1
3	\N	body	\N	1	2
17	3	id	basic	\N	1
18	3	view	form	\N	2
19	3	elements	\N	\N	3
\.


--
-- TOC entry 2010 (class 0 OID 101306)
-- Dependencies: 178
-- Data for Name: etap; Type: TABLE DATA; Schema: public; Owner: sa
--

COPY etap (id, name) FROM stdin;
1	step1
\.


--
-- TOC entry 2011 (class 0 OID 101315)
-- Dependencies: 180
-- Data for Name: holidays; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY holidays (id, databeg, dataend, kind, name) FROM stdin;
\.


--
-- TOC entry 2012 (class 0 OID 101320)
-- Dependencies: 182
-- Data for Name: report; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY report (id, reporttype_id, name2, body, reportexecutor, mimetype) FROM stdin;
3	1	Оферта на продление	<p align="center"><b>Оферта <u>creditnumberoferta</u> от <u>datelong</u></b></p>\n\n<p align="center"><b>на продление договора потребительского займа</b></p>\n\n<p align="justify">Я (далее «Заемщик»), fio (паспорт <u>seriesdoc</u> <u>numberdoc</u> выдан <u>datedoc</u> <u>orgdoc</u>;\nадрес места регистрации: <u>addrreg</u>; дата регистрации <u>datreg</u>; номер мобильного телефона для связи и обмена информацией: <u>cellphone</u>;\nадрес электронной почты для связи и обмена информацией: <u>email</u>), предлагаю Обществу с ограниченной ответственностью «Касса-онлайн»  (далее «Кредитор») (номер в государственном реестре микрофинансовых организаций 651403029004957 от 09.04.2014 года; \nадрес места нахождения: 249038, Калужская область, г.Обнинск, улица Комарова, д. 10А; телефон справочной службы: 8 800 775 22 05; адрес интернет-сайта www.rastorop.ru) изменить Индивидуальные условия № <u>creditnumber</u> от <u>datesign</u> \nДоговора потребительского займа в части сроков действия договора и суммы возврата займа. Новым сроком возврата займа предлагаю установить <u>creditdataendnew г.</u>.</p>\n\n<p align="justify">Заемщик предупрежден Кредитором, что Кредитор акцептует Оферту Заемщика только после того, как Заемщик уплатит все проценты по Договору потребительского займа № <u>creditnumber</u>, начисленных \nна сумму основного долга на день направления Заемщиком Оферты на продление договора потребительского займа.</p>\n\n<p align="justify">Я, fio, подтверждаю свое намерение изменить срок Договора потребительского займа № <u>creditnumber</u>. После акцепта данной Оферты Кредитором новым сроком погашения займа будет creditdataendnew. \nНовой суммой возврата займа будет <u>creditsumbacknew рублей</u>.</p>\n\n<p align="justify">Сумма процентов к уплате составляет <u>sumpercent рублей</u>. Срок уплаты процентов составляет 2 дня с учетом дня подачи оферты.<p>\n\n<p align="justify">Оферта № <u>creditnumberoferta</u> от <u>datelong</u> подписана простой электронной подписью <u>smscode</u> (СМС код).</p>\n\n\n\n	template.simple	text/html
1	1	Оферта	<div align="RIGHT">\r\n\t<table cellspacing="0" style="" width="227" x-width="227">\r\n\t\t<col width="211" />\r\n\t\t<tbody>\r\n                 <tr>\r\n\t\t\t<td height="210" style="border:1px solid #00000a;padding:0in 0.08in;" valign="TOP" width="211">\r\n\t\t\t\t<p align="CENTER" >ПОЛНАЯ СТОИМОСТЬ ПОТРЕБИТЕЛЬСКОГО ЗАЙМА</p>\r\n\t\t\t\t<br />\r\n\t\t\t\t\r\n\t\t\t\t<p align="CENTER"><font size="4"><b>year_percent%</b></font></p>\r\n                                <p align="CENTER"><i>percent_string % годовых</i></p>\r\n\t\t\t</td>\r\n\t\t</tr>\r\n\t</tbody></table>\r\n</div>\r\n\r\n<p ></p>\r\n<p align="CENTER"><b>Индивидуальные условия № <u>creditnumber</u> от <u>datesign</u> </b></p>\r\n\r\n<p align="CENTER"><b>Договора потребительского займа (далее по тексту ИУ)</b></p>\r\n\r\n<p></p>\r\n\r\n<p align="JUSTIFY">Общество с ограниченной ответственностью «Касса-онлайн» (далее «Кредитор») (номер в государственном реестре микрофинансовых\r\nорганизаций 651403029004957 от 09.04.2014 года; адрес места нахождения: 249038, Калужская область, г.Обнинск, улица Комарова, д.10А; \r\nтелефон справочной службы: 8-800-775-2205; адрес интернет сайта <a target="_blank" href="www.rastorop.ru">www.rastorop.ru</a>)\r\nпредлагает Вам fio (далее – Заемщик) (паспорт <u>seriesdoc</u> <u>numberdoc</u> выдан <u>datedoc</u> <u>orgdoc</u>;\r\nадрес места регистрации: <u>addrreg</u>; дата регистрации <u>datreg</u>; номер мобильного телефона для связи и обмена информацией: <u>cellphone</u>;\r\nадрес электронной почты для связи и обмена информацией: <u>email</u>) заключить Договор потребительского\r\nзайма на следующих условиях:</p>\r\n\r\n<table cellspacing="0" >\r\n\t\r\n\t<tbody>\r\n        <tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="CENTER">№ п/п</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="CENTER">Условие</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="CENTER">Содержание условия</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">1.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Сумма займа или лимит кредитования и порядок\tего изменения</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY" style=""><font color="#00000a"><u>creditSum рублей</u> (<i>sumString</i>)</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">2.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Срок\tдействия договора, срок возврата займа</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Договор вступает в силу с даты его подписания Сторонами и действует до полного выполнения Сторонами своих обязательств по Договору. \r\n                        Срок возврата займа <u>creditDataend</u> г</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">3.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Валюта, в которой предоставляется займ</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Рубли РФ</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">4.</p>\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Процентная ставка (процентные ставки) (в процентах годовых) или порядок ее (их) определения</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY">year_percent % (percent_string % годовых)</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">5.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY">Порядок определения курса иностранной валюты\tпри переводе денежных средств кредитором\r\n\t\t\tтретьему лицу, указанному заемщиком</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Не применимо</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">6.</p>\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY">Количество, размер и периодичность (сроки) платежей заемщика по договору или порядок\r\n\t\t\tопределения этих платежей</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Единовременный возврат суммы займа <u>creditSum рублей</u> с процентами \r\n                         в размере <u>creditPercent рублей</u>, <u>creditDataend</u> г.</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">7.</p>\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Порядок изменения количества, размера и периодичности (сроков) платежей заемщика при частичном досрочном\r\n\t\t\tвозврате займа</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Отсутствует</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr>\r\n\t\t<td height="72" style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">8.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Способы исполнения заемщиком обязательств по договору по месту нахождения заемщика</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Исполнение\r\n\t\t\tЗаемщиком обязательств по Договору\r\n\t\t\tпотребительского займа может\r\n\t\t\tосуществляться любыми приемлемыми для\r\n\t\t\tЗаемщика способами, информация о\r\n\t\t\tкоторых размещена на сайте Кредитора\r\n\t\t\tв сети Интернет по адресу: <a target="_blank" href="www.rastorop.ru">www.rastorop.ru</a></p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">8.1.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Бесплатный способ исполнения заемщиком обязательств\r\n\t\t\tпо договору</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Все способы, указанные на сайте Кредитора\r\n\t\t\tв сети Интернет по адресу: <a target="_blank" href="www.rastorop.ru">www.rastorop.ru</a></p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">9.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Обязанность\tзаемщика заключить иные договоры</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Не применимо</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">10.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Обязанность\r\n\t\t\tзаемщика по предоставлению обеспечения\r\n\t\t\tисполнения обязательств по договору\r\n\t\t\tи требования к такому обеспечению</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY">Не применимо</p>\r\n\t\t</td>\r\n\t</tr>\r\n  </tbody>\r\n</table>\r\n<p></p>\r\n<table cellspacing="0" >\r\n\t\r\n\t<tbody><tr>\r\n\t\t<td colspan="3" style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="CENTER">Индивидуальные условия № <u>creditnumber</u> от <u>datesign</u> Договора потребительского займа</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="CENTER">№ п/п</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="CENTER">Условие</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="CENTER">Содержание условия</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">11.</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Цели использования заемщиком потребительского\r\n\t\t\tзайма</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Не применимо</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">12.</p>\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Ответственность\r\n\t\t\tзаемщика за ненадлежащее исполнение\r\n\t\t\tусловий договора, размер неустойки\r\n\t\t\t(штрафа, пени) или порядок их определения</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY">Исполнение обязательства по возврату Займа обеспечивается посредством возложения на Клиента \r\n                         обязанности по уплате Пени. Начиная со следующего дня после Дня погашения, \r\n                        на непогашенную сумму Займа начинает начисляться Пеня. Размер Пени за период с первого дня просрочки до \r\n                        девяносто девятого дня просрочки включительно составляет 20 (двадцать) процентов годовых от непогашенной суммы по нецелевому Займу. \r\n                        Начиная с сотого дня после Дня погашения (Нового дня погашения) и вплоть до момента фактического возврата Займа, на непогашенную сумму \r\n                         по нецелевому Займу начинает начисляться Пеня в размере 0,1 (одна десятая) процента за каждый день просрочки. </p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="LEFT">13.</p>\r\n\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY">Условие\r\n\t\t\tоб уступке кредитором третьим лицам\r\n\t\t\tправ (требований) по договору</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Кредитор\r\n\t\t\tвправе осуществлять уступку прав\r\n\t\t\t(требований) по договору потребительского\r\n\t\t\tкредита (займа) третьим лицам. При этом\r\n\t\t\tзаемщик сохраняет в отношении нового\r\n\t\t\tкредитора все права, предоставленные\r\n\t\t\tему в отношении первоначального\r\n\t\t\tкредитора в соответствии с федеральными\r\n\t\t\tзаконами</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">14.</p>\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Согласие\r\n\t\t\tзаемщика с общими условиями договора</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Согласен</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t   <p align="LEFT">15.</p>\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY">Услуги,\r\n\t\t\tоказываемые кредитором заемщику за\r\n\t\t\tотдельную плату и необходимые для\r\n\t\t\tзаключения договора, их цена или\r\n\t\t\tпорядок ее определения, а также согласие\r\n\t\t\tзаемщика на оказание таких услуг</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;">\r\n\t\t\t<p align="JUSTIFY" >Не применимо</p>\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr valign="TOP">\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="LEFT">16.</p>\r\n\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY">Способ\r\n\t\t\tобмена информацией между кредитором\r\n\t\t\tи заемщиком</p>\r\n\t\t</td>\r\n\t\t<td style="border:1px solid #00000a;padding:0in 0.08in;" >\r\n\t\t\t<p align="JUSTIFY" >Уведомления,\r\n\t\t\tписьма, извещения (далее – Информация)\r\n\t\t\tнаправляются Кредитором Заемщику\r\n\t\t\tодним из следующих способов:</p>\r\n\t\t\t<ul>\r\n\t\t\t\t<li><p align="JUSTIFY">путем\r\n\t\t\t\tнаправления СМС–сообщений на номер\r\n\t\t\t\tтелефона сотовой связи Заемщика,\r\n\t\t\t\tуказанный в настоящих ИУ;</p>\r\n\t\t\t\t</li>\r\n                                <li><p align="JUSTIFY">путем\r\n\t\t\t\tнаправления Информации на адрес\r\n\t\t\t\tэлектронной почты Заемщика, указанный\r\n\t\t\t\tв настоящих ИУ;</p>\r\n\t\t\t\t</li>\r\n                                <li><p align="JUSTIFY">путем\r\n\t\t\t\tразмещения информации в Личном\r\n\t\t\t\tкабинете Заемщика на сайте\r\n\t\t\t\t<a target="_blank" href="www.rastorop.ru">www.rastorop.ru</a>.</p>\r\n\t\t\t        </li>\r\n                        </ul>\r\n\t\t\t<p align="JUSTIFY" >Информация\r\n\t\t\t направляется Заемщиком Кредитору следующими способами:</p>\r\n\t\t\t<ul>\r\n\t\t\t\t<li><p align="JUSTIFY">устно при обращении Заемщика в Телефонный центр Кредитора;</p>\r\n\t\t\t\t</li>\r\n                                <li><p align="JUSTIFY">через\r\n\t\t\t\tЛичный кабинет на сайте <a target="_blank" href="www.rastorop.ru">www.rastorop.ru</a>;</p>\r\n\t\t\t\t</li>\r\n                                <li><p align="JUSTIFY">через\r\n\t\t\t\tСМС–сообщения с номера телефона\r\n\t\t\t\tсотовой связи Заемщика, указанного в\r\n\t\t\t\tнастоящих ИУ;</p>\r\n\t\t\t\t</li>\r\n                                <li><p align="JUSTIFY">через\r\n\t\t\t\tотделения почтовой связи простым или\r\n\t\t\t\tзаказным письмом по почтовому адресу\r\n\t\t\t\tместа нахождения Кредитора,\r\n\t\t\t\tуказанному в настоящих ИУ;</p>\r\n\t\t\t        </li>\r\n                        </ul>\r\n\t\t</td>\r\n\t</tr>\r\n       \r\n</tbody>\r\n</table>\r\n<p align="JUSTIFY"> fio проинформирован (а) Кредитором о своих правах, в том числе, о праве сообщить Кредитору согласие на получение потребительского займа на условиях, указанных в настоящих ИУ, в течение 5 рабочих дней со дня предоставления ИУ.\r\n</p>\r\n<p align="JUSTIFY">Я, fio, с Договором потребительского займа ознакомлен(а), согласен(на), и обязуюсь выполнять его условия. \r\nДоговор потребительского займа № <u>creditnumber</u> подписан простой электронной подписью <u>smscode</u> (СМС код) <u>datesign</u> </p>\r\n\r\n	template.simple	text/html
4	1	Письмо о выгрузке в ОКБ	№ - 1\r\nИмя участника - orgname\r\nОбратный адрес - orgemail\r\nТелефон - orgphone\r\nДата учета - dateupload\r\nКредитный портфель - creditcode\r\nИмя файла - filename\r\nРазмер файла - filesize\r\nДата создания файла - dateupload\n\r\n	template.simple	text/plain
2	1	Письмо	<table style="width:940px;margin:20px auto;font-family:'Myriad Pro',sans-serif;border:0">\r\n\t<tr>\r\n\t\t<td style="padding:0 0 10px">\r\n\t\t\t<table style="width:100%;border:0">\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t<a href="http://rastorop.ru/main" target="_blank"><img src="http://rastorop.ru/client/resources/mail/logo.png" alt="быстрые онлайн займы" width="190px"></a>\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t<p style="color:#ff6419;font-size: 13px;margin: 0;text-align: right;">Звонок по России бесплатный:<br><a style="font-size: 23px;font-weight: bold;">8 (800) 775-22-05</a></p>\t\t\t\t\t\t\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t</table>\r\n\t\t</td>\r\n\t</tr>\r\n\t<td style="padding:0 0 10px">\r\n\t\t\t<table style="width:100%;border:0">\r\n<tr>\r\n<td><img style="height: 260px; left: -200px;top: -50px;width: 224px;" src="http://rastorop.ru/client/resources/mail/bg.png" alt=""></td>\r\n\t\t<td>\r\n\t\t\t<div style="background: #CFC8BC;height: 1px;"></div>\r\n\t\t</td>\r\n\t\t\t\t<td style="border:1px solid #CFC8BC;border-radius:4px;float:right;padding:10px 35px;position:relative;width:640px;margin:80px 0">\r\n\t\t\t\t<h1 style="color:#ff5300;font-size: 22px;font-weight: normal;">Здравствуйте!</h1>\r\n\t\t\t\t<p style="color:#474747;font-size: 14px;">description</p>\r\n\t\t</td>\r\n\t</tr>\r\n</table>\r\n\t<tr>\r\n\t\t<td style="padding:20px 0;">\r\n\t\t\t<table style="width:100%;border:0">\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t<p style="color:#6b6b6b;font-size: 13px;margin: 0;">© 2014, ООО “Касса онлайн”<br> Номер в государственном реестре микрофинансовых организаций 651403029004957 от 09.04.2014 года.</p>\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td align="right">\r\n\t\t\t\t\t\t<a href="http://google.com/" target="_blank" style="margin-left:10px"><img src="http://rastorop.ru/client/resources/mail/gl.png" alt=""></a>\r\n\t\t\t\t\t\t<a href="https://vk.com/club70895331" target="_blank" style="margin-left:10px"><img src="http://rastorop.ru/client/resources/mail/vk.png" alt=""></a>\r\n\t\t\t\t\t\t<a href="http://my.mail.ru/mail/cassaonline/" target="_blank" style="margin-left:10px"><img src="http://rastorop.ru/client/resources/mail/ml.png" alt=""></a>\r\n\t\t\t\t\t\t<a href="http://www.odnoklassniki.ru/profile/564859433398" target="_blank" style="margin-left:10px"><img src="http://rastorop.ru/client/resources/mail/ok.png" alt=""></a>\r\n\t\t\t\t\t\t<a href="https://www.facebook.com/pages/Растороп/257285754457138" target="_blank" style="margin-left:10px"><img src="http://rastorop.ru/client/resources/mail/fb.png" alt=""></a>\r\n\t\t\t\t\t\t<a href="https://twitter.com/rastorop1" target="_blank" style="margin-left:10px"><img src="http://rastorop.ru/client/resources/mail/tw.png" alt=""></a>\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t</table>\r\n\t\t</td>\r\n\t</tr>\r\n</table>\r\n\r\n	template.simple	text/html
5	1	Оферта на рефинансирование	<p align="center"><b>Оферта <u>creditnumberoferta</u> от <u>dateref</u></b></p>\r\n\r\n<p align="center"><b>о реструктуризации Договора займа</b></p>\r\n\r\n<p align="justify">Я (далее «Заемщик»), fio (паспорт <u>seriesdoc</u> <u>numberdoc</u> выдан <u>datedoc</u> <u>orgdoc</u>;\r\nадрес места регистрации: <u>addrreg</u>; дата регистрации <u>datreg</u>; номер мобильного телефона для связи и обмена информацией: <u>cellphone</u>;\r\nадрес электронной почты для связи и обмена информацией: <u>email</u>), предлагаю Обществу с ограниченной ответственностью «Касса-онлайн»  (далее «Кредитор») (номер в государственном реестре микрофинансовых организаций 651403029004957 от 09.04.2014 года; \r\nадрес места нахождения: 249038, Калужская область, г.Обнинск, улица Комарова, д. 10А; телефон справочной службы: 8 800 775 22 05; адрес интернет-сайта www.rastorop.ru)\r\nпровести Реструктуризацию Договора потребительского займа № <u>creditnumber</u> от <u>datesign</u> и внести изменения в п.1, п.2, п.4, п.6 Индивидуальных условий № <u>creditnumber</u> от <u>datesign</u> \r\nДоговора потребительского займа.</p>\r\n\r\n<p align="justify">Заемщик предупрежден Кредитором, что Кредитор акцептует Оферту Заемщика только после того, как Заемщик осуществит платеж в размере <u>paysum</u> руб. по Договору потребительского займа № <u>creditnumber</u>, \r\nвключаемых в счёт погашения суммы Процентов и Основного долга на день направления Заемщиком Оферты о реструктуризации Договора займа (далее- Реструктурированная Задолженность).</p>\r\n\r\n<p align="justify">Срок действия Договора займа продлевается на <u>daysref</u> дней. В соответствии с условиями Оферты <u>creditnumberoferta</u> от <u>dateref</u> о Реструктуризации Договора займа сроком погашения \r\nРеструктурированной Задолженности является <u>creditdataendnew г.</u></p>\r\n \r\n<p align="justify">За пользование займом на срок реструктуризации Договора займа (<u>daysref</u> дней) Заемщик уплачивает Кредитору <u>percentref</u> процента от суммы Реструктурированной Задолженности  \r\nежедневно до момента погашения реструктурированной задолженности и процентов. C начала периода реструктуризации и до момента уплаты Заемщиком суммы Реструктурированной Задолженности, \r\nпроценты в размере, установленном в п.3.1. и п.3.4 Общих условий договора потребительского займа, прекращают начисляться со дня Акцепта Оферты о Реструктуризации Договора займа Кредитором.</p>\r\n \r\n<p align="justify">В соответствии с условиями Оферты <u>creditnumberoferta</u> от <u>dateref</u> о Реструктуризации Договора займа суммой Реструктурированной Задолженности  является <u>creditsum</u> руб.  \r\nРеструктурированная Задолженность  рассчитывается в соответствии с условиями и правилами, указанными в данной Оферте.</p>\r\n\r\n<p align="justify">Погашение Реструктурированной Задолженности и Процентов осуществляется единовременным платежом. Сумма платежа составляет <u>creditsumback</u> руб.\r\nРеструктурированная Задолженность может быть погашена досрочно, полностью или частично. Частичное досрочное погашение Реструктурированной Задолженности осуществляется несколькими платежами, \r\nпри этом денежные средства, полученные Кредитором от Заемщика, направляются на погашение Реструктурированной Задолженности в очередности, предусмотренной п.4.6. Общих условий договора потребительского займа.</p>\r\n\r\n<p align="justify">Я, fio, подтверждаю свое намерение провести Реструктуризацию Договора займа № <u>creditnumber</u>, внести изменения в п.1, п.2, п.4, п.6 Индивидуальных условий № <u>creditnumber</u> от <u>datesign</u>\r\nДоговора потребительского займа и принимаю условия данной Оферты.</p>\r\n\r\n<p align="justify">Оферта № <u>creditnumberoferta</u> от <u>dateref</u> подписана простой электронной подписью <u>smscode</u>.</p>\r\n\r\n\r\n\r\n	template.simple	text/html
\.


--
-- TOC entry 2013 (class 0 OID 101328)
-- Dependencies: 184
-- Data for Name: reporttype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY reporttype (id, name2) FROM stdin;
1	Одиночный
2	Список
3	Сводный
\.


--
-- TOC entry 1963 (class 2606 OID 101339)
-- Dependencies: 169 169
-- Name: attributes_pkey; Type: CONSTRAINT; Schema: public; Owner: sa; Tablespace: 
--

ALTER TABLE ONLY attributes
    ADD CONSTRAINT attributes_pkey PRIMARY KEY (id);


--
-- TOC entry 1976 (class 2606 OID 101341)
-- Dependencies: 176 176
-- Name: description_pkey; Type: CONSTRAINT; Schema: public; Owner: sa; Tablespace: 
--

ALTER TABLE ONLY elements
    ADD CONSTRAINT description_pkey PRIMARY KEY (id);


--
-- TOC entry 1978 (class 2606 OID 101343)
-- Dependencies: 178 178
-- Name: etap_pkey; Type: CONSTRAINT; Schema: public; Owner: sa; Tablespace: 
--

ALTER TABLE ONLY etap
    ADD CONSTRAINT etap_pkey PRIMARY KEY (id);


--
-- TOC entry 1982 (class 2606 OID 101345)
-- Dependencies: 180 180
-- Name: holidays_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY holidays
    ADD CONSTRAINT holidays_id PRIMARY KEY (id);


--
-- TOC entry 1950 (class 2606 OID 101347)
-- Dependencies: 161 161
-- Name: pk_aiconstant; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY aiconstant
    ADD CONSTRAINT pk_aiconstant PRIMARY KEY (id);


--
-- TOC entry 1954 (class 2606 OID 101349)
-- Dependencies: 163 163
-- Name: pk_aimodel; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY aimodel
    ADD CONSTRAINT pk_aimodel PRIMARY KEY (id);


--
-- TOC entry 1957 (class 2606 OID 101351)
-- Dependencies: 165 165 165
-- Name: pk_aimodelparams; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY aimodelparams
    ADD CONSTRAINT pk_aimodelparams PRIMARY KEY (aimodel_id, name);


--
-- TOC entry 1961 (class 2606 OID 101353)
-- Dependencies: 166 166
-- Name: pk_airule; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY airule
    ADD CONSTRAINT pk_airule PRIMARY KEY (id);


--
-- TOC entry 1968 (class 2606 OID 101355)
-- Dependencies: 170 170
-- Name: pk_bizaction; Type: CONSTRAINT; Schema: public; Owner: sa; Tablespace: 
--

ALTER TABLE ONLY bizaction
    ADD CONSTRAINT pk_bizaction PRIMARY KEY (id);


--
-- TOC entry 1993 (class 2606 OID 402238)
-- Dependencies: 186 186
-- Name: pk_bizactionevent; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY bizactionevent
    ADD CONSTRAINT pk_bizactionevent PRIMARY KEY (id);


--
-- TOC entry 1972 (class 2606 OID 101357)
-- Dependencies: 172 172
-- Name: pk_bizactiontype; Type: CONSTRAINT; Schema: public; Owner: sa; Tablespace: 
--

ALTER TABLE ONLY bizactiontype
    ADD CONSTRAINT pk_bizactiontype PRIMARY KEY (id);


--
-- TOC entry 1974 (class 2606 OID 101359)
-- Dependencies: 174 174
-- Name: pk_databasechangeloglock; Type: CONSTRAINT; Schema: public; Owner: sa; Tablespace: 
--

ALTER TABLE ONLY databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- TOC entry 1984 (class 2606 OID 101361)
-- Dependencies: 182 182
-- Name: pk_report; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY report
    ADD CONSTRAINT pk_report PRIMARY KEY (id);


--
-- TOC entry 1988 (class 2606 OID 101363)
-- Dependencies: 184 184
-- Name: pk_reporttype; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY reporttype
    ADD CONSTRAINT pk_reporttype PRIMARY KEY (id);


--
-- TOC entry 1947 (class 1259 OID 101364)
-- Dependencies: 161
-- Name: aiconstant_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX aiconstant_pk ON aiconstant USING btree (id);


--
-- TOC entry 1952 (class 1259 OID 101365)
-- Dependencies: 163
-- Name: aimodel_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX aimodel_pk ON aimodel USING btree (id);


--
-- TOC entry 1955 (class 1259 OID 101366)
-- Dependencies: 165 165
-- Name: aimodelparams_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX aimodelparams_pk ON aimodelparams USING btree (aimodel_id, name);


--
-- TOC entry 1964 (class 1259 OID 101367)
-- Dependencies: 170
-- Name: bizaction_pk; Type: INDEX; Schema: public; Owner: sa; Tablespace: 
--

CREATE UNIQUE INDEX bizaction_pk ON bizaction USING btree (id);


--
-- TOC entry 1990 (class 1259 OID 402239)
-- Dependencies: 186
-- Name: bizactionevent_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX bizactionevent_pk ON bizactionevent USING btree (id);


--
-- TOC entry 1970 (class 1259 OID 101368)
-- Dependencies: 172
-- Name: bizactiontype_pk; Type: INDEX; Schema: public; Owner: sa; Tablespace: 
--

CREATE UNIQUE INDEX bizactiontype_pk ON bizactiontype USING btree (id);


--
-- TOC entry 1979 (class 1259 OID 101369)
-- Dependencies: 180
-- Name: holidays_databeg; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holidays_databeg ON holidays USING btree (databeg);


--
-- TOC entry 1980 (class 1259 OID 101370)
-- Dependencies: 180
-- Name: holidays_dataend; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holidays_dataend ON holidays USING btree (dataend);


--
-- TOC entry 1991 (class 1259 OID 402241)
-- Dependencies: 186
-- Name: idxbaecode; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idxbaecode ON bizactionevent USING btree (eventcode);


--
-- TOC entry 1965 (class 1259 OID 101371)
-- Dependencies: 170
-- Name: idxbizobj; Type: INDEX; Schema: public; Owner: sa; Tablespace: 
--

CREATE INDEX idxbizobj ON bizaction USING btree (businessobjectclass);


--
-- TOC entry 1966 (class 1259 OID 101372)
-- Dependencies: 170
-- Name: idxbizproc; Type: INDEX; Schema: public; Owner: sa; Tablespace: 
--

CREATE INDEX idxbizproc ON bizaction USING btree (processdefkey);


--
-- TOC entry 1948 (class 1259 OID 101373)
-- Dependencies: 161
-- Name: idxname; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idxname ON aiconstant USING btree (name2);


--
-- TOC entry 1959 (class 1259 OID 101374)
-- Dependencies: 166
-- Name: idxpackagename; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idxpackagename ON airule USING btree (packagename);


--
-- TOC entry 1951 (class 1259 OID 101375)
-- Dependencies: 161
-- Name: relationship_1_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relationship_1_fk ON aiconstant USING btree (airule_id);


--
-- TOC entry 1985 (class 1259 OID 101376)
-- Dependencies: 182
-- Name: relationship_2_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relationship_2_fk ON report USING btree (reporttype_id);


--
-- TOC entry 1969 (class 1259 OID 101377)
-- Dependencies: 170
-- Name: relationship_4_fk; Type: INDEX; Schema: public; Owner: sa; Tablespace: 
--

CREATE INDEX relationship_4_fk ON bizaction USING btree (bizactiontype_id);


--
-- TOC entry 1958 (class 1259 OID 101378)
-- Dependencies: 165
-- Name: relationship_5_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relationship_5_fk ON aimodelparams USING btree (aimodel_id);


--
-- TOC entry 1994 (class 1259 OID 402240)
-- Dependencies: 186
-- Name: relbaact_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relbaact_fk ON bizactionevent USING btree (bizaction_id);


--
-- TOC entry 1986 (class 1259 OID 101379)
-- Dependencies: 182
-- Name: report_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX report_pk ON report USING btree (id);


--
-- TOC entry 1989 (class 1259 OID 101380)
-- Dependencies: 184
-- Name: reporttype_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX reporttype_pk ON reporttype USING btree (id);


--
-- TOC entry 1995 (class 2606 OID 101381)
-- Dependencies: 166 1960 161
-- Name: fk_aiconsta_relations_airule; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY aiconstant
    ADD CONSTRAINT fk_aiconsta_relations_airule FOREIGN KEY (airule_id) REFERENCES airule(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 1996 (class 2606 OID 101386)
-- Dependencies: 163 1953 165
-- Name: fk_aimodelp_relations_aimodel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY aimodelparams
    ADD CONSTRAINT fk_aimodelp_relations_aimodel FOREIGN KEY (aimodel_id) REFERENCES aimodel(id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 1997 (class 2606 OID 101391)
-- Dependencies: 1971 170 172
-- Name: fk_bizactio_relations_bizactio; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY bizaction
    ADD CONSTRAINT fk_bizactio_relations_bizactio FOREIGN KEY (bizactiontype_id) REFERENCES bizactiontype(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 1999 (class 2606 OID 402242)
-- Dependencies: 1967 170 186
-- Name: fk_bizactio_relbaact_bizactio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bizactionevent
    ADD CONSTRAINT fk_bizactio_relbaact_bizactio FOREIGN KEY (bizaction_id) REFERENCES bizaction(id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 1998 (class 2606 OID 101396)
-- Dependencies: 1987 182 184
-- Name: fk_report_relations_reportty; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report
    ADD CONSTRAINT fk_report_relations_reportty FOREIGN KEY (reporttype_id) REFERENCES reporttype(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2019 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-05-12 08:10:35

--
-- PostgreSQL database dump complete
--

