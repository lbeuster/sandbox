CREATE TABLE languages(
    language_id int IDENTITY NOT NULL,
    name_eng varchar(50) NOT NULL,
    status int NOT NULL,
    iso639_a2 varchar(2) NOT NULL,
    iso639_a3 varchar(3) NOT NULL,
    iso3166_a2 varchar(2) NOT NULL,
)

