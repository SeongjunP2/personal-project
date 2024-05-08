create table sports_teams (
    id number generated always as identity,
    league varchar2(100 char) not null,
    team varchar2(100 char) not null,
    emblem_path varchar2(2000 char) not null,
    constraint sports_teams_pk primary key(id)
);

---- 리그 테이블 생성
--create table leagues (
--    league_id number generated always as identity,
--    league_name varchar2(100 char),
--    constraint league_pk primary key(league_id)
--);
--
---- 리그 생성
--insert into leagues (league_name)
--values ('프리미어 리그');
--
--insert into leagues (league_name)
--values ('KBO 리그');

create table schedule_date (
    id number generated always as identity,
    team varchar2(100 char) not null,
    other_team varchar2(100 char) not null,
    created_date varchar2(100 char) not null,
    constraint schedule_date_pk primary key(id)
);

drop table schedule_date;

select created_date from schedule_date where team = '첼시';