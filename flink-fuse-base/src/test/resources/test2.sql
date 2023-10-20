create temporary table datagen_source
(
    f_sequence   int,
    f_random     int,
    f_random_str string
) with ('connector' = 'datagen');

select *
from datagen_source
limit 100;