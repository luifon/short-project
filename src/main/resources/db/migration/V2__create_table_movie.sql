CREATE table newproj.tb_movie
(
    id           UUID,
    title        varchar(30),
    release_date timestamp,
    synopsis text,
    user_rating       int not null
        check (user_rating >= 0 and user_rating <= 10)
);