
    drop table wf_activityinst cascade;

    drop table wf_participant cascade;

    drop table wf_primarykey cascade;

    drop table wf_processdefine cascade;
 
    drop table wf_processinst cascade;

    drop table wf_transctrl cascade;

    drop table wf_workitem cascade;

    create table wf_activityinst (
        ACTIVITYINSTID int8 not null,
        ACTIVITYINSTNAME varchar(100),
        DESCRIPTION varchar(500),
        ACTIVITYTYPE varchar(10),
        ACTIVITYDEFID varchar(50),
        PROCESSINSTID int8,
        CURRENTSTATE int8,
        LIMITTIME int8,
        CREATETIME timestamp,
        STARTTIME timestamp,
        ENDTIME timestamp,
        FINALTIME timestamp,
        primary key (ACTIVITYINSTID)
    );

    create table wf_participant (
        PARTICID int8 not null,
        WORKITEMID int8 not null,
        PARTICTYPE varchar(20),
        PARTICIPANT varchar(50),
        PARTICIPANT2 varchar(50),
        primary key (PARTICID)
    );

    create table wf_primarykey (
        CODE int8 not null,
        NAME varchar(30) not null,
        primary key (CODE, NAME)
    );

    create table wf_processdefine (
        PROCESSDEFID int8 not null,
        PROCESSDEFNAME varchar(100),
        PROCESSCHNAME varchar(100),
        DESCRIPTION varchar(500),
        CURRENTSTATE int4,
        VERSIONSIGN varchar(20),
        LIMITTIME int4,
        PROCESSDEFCONTENT varchar(65535),
        CREATETIME timestamp,
        CREATOR varchar(64),
        UPDATETIME timestamp,
        UPDATOR varchar(64),
        primary key (PROCESSDEFID)
    );

    create table wf_processinst (
        PROCESSINSTID int8 not null,
        PROCESSDEFID int8,
        PROCESSINSTNAME varchar(100),
        CREATOR varchar(50),
        CURRENTSTATE int8,
        SUBFLOW varchar(1),
        LIMITNUM int8,
        CREATETIME timestamp,
        STARTTIME timestamp,
        ENDTIME timestamp,
        FINALTIME timestamp,
        MAINPROCINSTID int8,
        PARENTPROCINSTID int8,
        ACTIVITYINSTID int8,
        primary key (PROCESSINSTID)
    );

    create table wf_transctrl (
        TRANSCTRLID int8 not null,
        SRCACTDEFID varchar(50),
        SRCACTDEFNAME varchar(100),
        DESTACTTYPE varchar(10),
        DESTACTDEFID varchar(50),
        DESTACTDEFNAME varchar(100),
        SRCACTTYPE varchar(10),
        TRANSTIME timestamp,
        PROCESSINSTID int8,
        ISUSE varchar(1),
        ISSTARTDESTACT varchar(1),
        primary key (TRANSCTRLID)
    );

    create table wf_workitem (
        WORKITEMID int8 not null,
        WORKITEMNAME varchar(100),
        WORKITEMTYPE varchar(10),
        CURRENTSTATE int8,
        PARTICIPANT varchar(200),
        LIMITTIME int8,
        ACTIVITYINSTID int8,
        PROCESSINSTID int8,
        BIZSTATE int8,
        STARTTIME timestamp,
        ENDTIME timestamp,
        FINALTIME timestamp,
        activityDefId varchar(50),
        primary key (WORKITEMID)
    );
