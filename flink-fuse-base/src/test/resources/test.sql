create catalog myhive with (
    'type' = 'hive',
    'hive-conf-dir' = '/opt/hive-conf'
);

create temporary table heros (
    hname string,
    hpower string,
    hage int
) with (
  'connector' = 'faker',
    'rows-per-second' = '100',
    'fields.hname.expression' = '#{superhero.name}',
    'fields.hpower.expression' = '#{superhero.power}',
    'fields.hpower.null-rate' = '0.05',
    'fields.hage.expression' = '#{number.numberbetween ''0'',''1000''}'
);

insert into myhive.test.heros select * from heros;