create table football_teams (
    team_id number generated always as identity,
    league varchar2(100 char) not null,
    team varchar2(100 char) not null,
    country varchar2(100 char) not null,
    emblem_path varchar2(1000 char) not null,
    constraint football_team_pk primary key(team_id)
);

create table schedule_date (
    team varchar2(100 char) not null,
    created_date timestamp default systimestamp
);