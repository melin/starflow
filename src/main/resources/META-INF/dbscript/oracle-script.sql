
    drop table wf_activityinst cascade constraints;

    drop table wf_participant cascade constraints;

    drop table wf_primarykey cascade constraints;

    drop table wf_processdefine cascade constraints;

    drop table wf_processinst cascade constraints;
 
    drop table wf_transctrl cascade constraints;

    drop table wf_workitem cascade constraints;

    create table wf_activityinst (
        ACTIVITYINSTID number(19,0) not null,
        ACTIVITYINSTNAME varchar2(100),
        DESCRIPTION varchar2(500),
        ACTIVITYTYPE varchar2(10),
        ACTIVITYDEFID varchar2(50),
        PROCESSINSTID number(19,0),
        CURRENTSTATE number(19,0),
        LIMITTIME number(19,0),
        CREATETIME date,
        STARTTIME date,
        ENDTIME date,
        FINALTIME date,
        primary key (ACTIVITYINSTID)
    );

    create table wf_participant (
        PARTICID number(19,0) not null,
        WORKITEMID number(19,0) not null,
        PARTICTYPE varchar2(20),
        PARTICIPANT varchar2(50),
        PARTICIPANT2 varchar2(50),
        primary key (PARTICID)
    );

    create table wf_primarykey (
        CODE number(19,0) not null,
        NAME varchar2(30) not null,
        primary key (CODE, NAME)
    );

    create table wf_processdefine (
        PROCESSDEFID number(19,0) not null,
        PROCESSDEFNAME varchar2(100),
        PROCESSCHNAME varchar2(100),
        DESCRIPTION varchar2(500),
        CURRENTSTATE number(10,0),
        VERSIONSIGN varchar2(20),
        LIMITTIME number(10,0),
        PROCESSDEFCONTENT clob,
        CREATETIME date,
        CREATOR varchar2(64),
        UPDATETIME date,
        UPDATOR varchar2(64),
        primary key (PROCESSDEFID)
    );

    create table wf_processinst (
        PROCESSINSTID number(19,0) not null,
        PROCESSDEFID number(19,0),
        PROCESSINSTNAME varchar2(100),
        CREATOR varchar2(50),
        CURRENTSTATE number(19,0),
        SUBFLOW varchar2(1),
        LIMITNUM number(19,0),
        CREATETIME date,
        STARTTIME date,
        ENDTIME date,
        FINALTIME date,
        MAINPROCINSTID number(19,0),
        PARENTPROCINSTID number(19,0),
        ACTIVITYINSTID number(19,0),
        primary key (PROCESSINSTID)
    );

    create table wf_transctrl (
        TRANSCTRLID number(19,0) not null,
        SRCACTDEFID varchar2(50),
        SRCACTDEFNAME varchar2(100),
        DESTACTTYPE varchar2(10),
        DESTACTDEFID varchar2(50),
        DESTACTDEFNAME varchar2(100),
        SRCACTTYPE varchar2(10),
        TRANSTIME date,
        PROCESSINSTID number(19,0),
        ISUSE varchar2(1),
        ISSTARTDESTACT varchar2(1),
        primary key (TRANSCTRLID)
    );

    create table wf_workitem (
        WORKITEMID number(19,0) not null,
        WORKITEMNAME varchar2(100),
        WORKITEMTYPE varchar2(10),
        CURRENTSTATE number(19,0),
        PARTICIPANT varchar2(200),
        LIMITTIME number(19,0),
        ACTIVITYINSTID number(19,0),
        PROCESSINSTID number(19,0),
        BIZSTATE number(19,0),
        STARTTIME date,
        ENDTIME date,
        FINALTIME date,
        activityDefId varchar2(50),
        primary key (WORKITEMID)
    );
